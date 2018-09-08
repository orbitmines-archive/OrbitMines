package com.orbitmines.api.punishment;

import com.orbitmines.api.CachedPlayer;
import com.orbitmines.api.Language;
import com.orbitmines.api.Message;
import com.orbitmines.api.database.Database;
import com.orbitmines.api.database.Set;
import com.orbitmines.api.database.Table;
import com.orbitmines.api.database.Where;
import com.orbitmines.api.database.tables.TablePunishments;
import com.orbitmines.api.punishment.offences.Offence;
import com.orbitmines.api.punishment.offences.Severity;
import com.orbitmines.api.utils.DateUtils;
import com.orbitmines.api.utils.TimeUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/*
 * OrbitMines - @author Fadi Shawki - 16-6-2018
 */

public class Punishment {

    public static final long EXPIRE_IN = TimeUnit.DAYS.toMillis(60);

    private final UUID punished;
    private final Offence offence;
    private final Severity severity;

    /* duration */
    private final Date from;
    private final Date to;

    /* by */
    private final UUID punishedBy;
    private final String reason;

    /* when pardoned */
    private boolean pardoned;
    private Date pardonedOn;
    private UUID pardonedBy;
    private String pardonedReason;

    public Punishment(UUID punished, Offence offence, Severity severity, Date to, UUID punishedBy, String reason) {
        this.punished = punished;
        this.offence = offence;
        this.severity = severity;
        this.from = DateUtils.now();
        this.to = to;
        this.punishedBy = punishedBy;
        this.reason = reason;

        Database.get().insert(Table.PUNISHMENTS, punished.toString(), offence.toString(), severity.toString(), DateUtils.FORMAT.format(from), DateUtils.FORMAT.format(to), punishedBy.toString(), reason.replaceAll("'", ""), "0", "null", "null", "null");
    }

    public Punishment(UUID punished, Offence offence, Severity severity, Date from, Date to, UUID punishedBy, String reason, boolean pardoned, Date pardonedOn, UUID pardonedBy, String pardonedReason) {
        this.punished = punished;
        this.offence = offence;
        this.severity = severity;
        this.from = from;
        this.to = to;
        this.punishedBy = punishedBy;
        this.reason = reason;
        this.pardoned = pardoned;
        this.pardonedOn = pardonedOn;
        this.pardonedBy = pardonedBy;
        this.pardonedReason = pardonedReason;
    }

    public CachedPlayer getPunished() {
        return CachedPlayer.getPlayer(punished);
    }

    public Offence getOffence() {
        return offence;
    }

    public Severity getSeverity() {
        return severity;
    }

    /* If the punishment is currently active */
    public boolean isActive() {
        return severity != Severity.WARNING && !pardoned && (severity.getDuration() == Duration.PERMANENT || DateUtils.now().compareTo(to) < 0);
    }

    /* returns true when the punishment is expired: when it is no longer taken into account for further offences */
    public boolean hasExpired() {
        return pardoned || !isActive() && to.compareTo(DateUtils.now()) >= 0;
    }

    public String getExpireInString(Language language) {
        return TimeUtils.fromTimeStamp(to.getTime() - DateUtils.now().getTime(), language);
    }

    public String getBanString(Language language) {//TODO COMPONENT
        CachedPlayer punishedBy = getPunishedBy();
        return "§8§lOrbit§7§lMines§r\n" +
                new Message("§7Je bent §c§lGEBANNED§r§7!\n", "§7You have been §c§lBANNED§r§7!\n").lang(language) +
                "\n" +
                (severity.getDuration() == Duration.PERMANENT ? "§c§lPERMANENT§r" : "§c" + getExpireInString(language)) + "\n" +
                "\n" +
                "§7" + new Message("Overtreding", "Offence").lang(language) + ": §c" + offence.getName().lang(language) + "\n" +
                "§7" + new Message("Reden", "Reason").lang(language) + ": §c" + reason + "\n" +
                "§7" + new Message("Gebanned door", "Banned by").lang(language) + ": " + punishedBy.getRankPrefix() + punishedBy.getPlayerName() + "\n" +
                "\n" +
                "§7" + new Message("Gebanned op", "Banned on").lang(language) + ": §c" + getFromString(DateUtils.FORMAT) + "\n" +
                (severity.getDuration() == Duration.PERMANENT ? "" : "§7" + new Message("Gebanned tot", "Banned until").lang(language) + ": §c" + getToString(DateUtils.FORMAT));
    }

    public Date getFrom() {
        return from;
    }

    public String getFromString(SimpleDateFormat format) {
        return format.format(from);
    }

    public Date getTo() {
        return to;
    }

    public String getToString(SimpleDateFormat format) {
        return format.format(to);
    }

    public CachedPlayer getPunishedBy() {
        return CachedPlayer.getPlayer(punishedBy);
    }

    public String getReason() {
        return reason;
    }

    public boolean isPardoned() {
        return pardoned;
    }

    public Date getPardonedOn() {
        return pardonedOn;
    }

    public String getPardonedOnString(SimpleDateFormat format) {
        return format.format(pardonedOn);
    }

    public CachedPlayer getPardonedBy() {
        return CachedPlayer.getPlayer(pardonedBy);
    }

    public String getPardonedReason() {
        return pardonedReason;
    }

    public void pardon(UUID pardonedBy, String pardonedReason) {
        this.pardoned = true;
        this.pardonedOn = DateUtils.now();
        this.pardonedBy = pardonedBy;
        this.pardonedReason = pardonedReason;

        Database.get().update(Table.PUNISHMENTS, new Set[] {
                new Set(TablePunishments.PARDONED, 1),
                new Set(TablePunishments.PARDONED_ON, DateUtils.FORMAT.format(pardonedOn)),
                new Set(TablePunishments.PARDONED_BY, pardonedBy.toString()),
                new Set(TablePunishments.PARDONED_REASON, pardonedReason)
        }, wheres());
    }

    private Where[] wheres() {
        return new Where[] {
                new Where(TablePunishments.UUID, punished.toString()),
                new Where(TablePunishments.OFFENCE, offence.toString()),
                new Where(TablePunishments.SEVERITY, severity.toString()),
                new Where(TablePunishments.FROM, DateUtils.FORMAT.format(from)),
                new Where(TablePunishments.TO, DateUtils.FORMAT.format(to)),
                new Where(TablePunishments.PUNISHED_BY, punishedBy.toString()),
                new Where(TablePunishments.REASON, reason)
        };
    }

    public enum Type {

        MUTE(new Message("Mute"), TimeUnit.DAYS.toMillis(60)),
        BAN(new Message("Ban"), TimeUnit.HOURS.toMillis(32));

        private final Message name;
        private final long permanentAfter;

        Type(Message name, long permanentAfter) {
            this.name = name;
            this.permanentAfter = permanentAfter;
        }

        public Message getName() {
            return name;
        }

        public long getPermanentAfter() {
            return permanentAfter;
        }
    }

    public enum Duration {

        NONE,
        EXPIRE_DATE,
        PERMANENT

    }
}
