package com.orbitmines.api.punishment;
/*
 * OrbitMines - @author Fadi Shawki - 16-6-2018
 */

public class PunishmentHandler {
//
//    private static List<PunishmentHandler> handlers = new ArrayList<>();
//
//    private final UUID uuid;
//    private final List<Punishment> punishments;
//
//    public PunishmentHandler(UUID uuid) {
//        this.uuid = uuid;
//        this.punishments = new ArrayList<>();
//
//        handlers.add(this);
//
//        update();
//    }
//
//    public CachedPlayer getPlayer() {
//        return CachedPlayer.getPlayer(uuid);
//    }
//
//    public void addPunishment(Punishment punishment) {
//        this.punishments.add(punishment);
//    }
//
//    public List<Punishment> getPunishments(boolean withExpired) {
//        if (withExpired)
//            return new ArrayList<>(this.punishments);
//
//        List<Punishment> punishments = new ArrayList<>();
//
//        for (Punishment punishment : this.punishments) {
//            if (!punishment.hasExpired())
//                punishments.add(punishment);
//        }
//
//        return punishments;
//    }
//
//    public Punishment getActivePunishment(Punishment.Type type) {
//        for (Punishment punishment : getPunishments(false)) {
//            if (punishment.getOffence().getType() == type && punishment.isActive())
//                return punishment;
//        }
//        return null;
//    }
//
//    public Punishment getActivePunishment(Offence offence) {
//        for (Punishment punishment : getPunishments(false)) {
//            if (punishment.getOffence() == offence && punishment.isActive())
//                return punishment;
//        }
//        return null;
//    }
//
//    public List<Punishment> getPunishments(Offence offence, boolean withExpired) {
//        List<Punishment> punishments = new ArrayList<>();
//
//        for (Punishment punishment : getPunishments(withExpired)) {
//            if (punishment.getOffence() == offence)
//                punishments.add(punishment);
//        }
//
//        return punishments;
//    }
//
//    public List<Punishment> getActivePunishments() {
//        List<Punishment> punishments = new ArrayList<>();
//
//        for (Punishment punishment : getPunishments(false)) {
//            if (punishment.isActive())
//                punishments.add(punishment);
//        }
//
//        return punishments;
//    }
//
//    public Date getPunishedTo(Offence offence, Severity realSev) {
//        if (realSev == Severity.WARNING || realSev == Severity.SEV_3)
//            return DateUtils.now();
//
//        long millis = getMillis(offence, realSev);
//
//        Date date = DateUtils.now();
//        date.setTime(date.getTime() + millis);
//
//        return date;
//    }
//
//    public Severity getRealSeverity(Offence offence, Severity severity) {
//        List<Punishment> punishments = getPunishments(offence, false);
//
//        /* First get Real Severity from history */
//        for (Punishment punishment : punishments) {
//            if (severity.ordinal() >= punishment.getSeverity().ordinal())
//                continue;
//
//            severity = severity.next();
//            break;
//        }
//
//        /* Run that severity against the expected time, make the Severity permanent if it is too long */
//        if (getMillis(offence, severity) > offence.getType().getPermanentAfter())
//            severity = Severity.SEV_3;
//
//        return severity;
//    }
//
//    public long getMillis(Offence offence, Severity realSev) {
//        Map<Severity, Integer> count = new HashMap<>();
//
//        /* Add all previous punishment over the last two days to the punishment */
//        for (Punishment punishment : getPunishments(offence, false)) {
//            if (punishment.getSeverity().getMillis(offence.getType()) == 0)
//                continue;
//
//            if (!count.containsKey(punishment.getSeverity()))
//                count.put(punishment.getSeverity(), 1);
//            else
//                count.put(punishment.getSeverity(), count.get(punishment.getSeverity()) + 1);
//        }
//
//        /* Add real severity */
//        if (!count.containsKey(realSev))
//            count.put(realSev, 1);
//        else
//            count.put(realSev, count.get(realSev) + 1);
//
//        long millis = 0;
//
//        /* 2h / 4h / 8h / 16h / 32h */
//        for (Severity sev : count.keySet()) {
//            millis += Math.pow(sev.getMillis(offence.getType()), count.get(sev));
//        }
//
//        return millis;
//    }
//
//    public void update() {
//        punishments.clear();
//
//        List<Map<Column, String>> entries = Database.get().getEntries(Table.PUNISHMENTS, new Where(TablePunishments.UUID, uuid.toString()));
//
//        for (Map<Column, String> entry : entries) {
//            Offence offence = Offence.valueOf(entry.get(TablePunishments.OFFENCE));
//            Severity severity = Severity.valueOf(entry.get(TablePunishments.SEVERITY));
//            Date from = DateUtils.parse(DateUtils.FORMAT, entry.get(TablePunishments.FROM));
//            Date to = DateUtils.parse(DateUtils.FORMAT, entry.get(TablePunishments.TO));
//            UUID punishedBy = UUID.fromString(entry.get(TablePunishments.PUNISHED_BY));
//            String reason = entry.get(TablePunishments.REASON);
//
//            boolean pardoned = Integer.parseInt(entry.get(TablePunishments.PARDONED)) != 0;
//            Date pardonedOn = pardoned ? DateUtils.parse(DateUtils.FORMAT, entry.get(TablePunishments.PARDONED_ON)) : null;
//            UUID pardonedBy = pardoned ? UUID.fromString(entry.get(TablePunishments.PARDONED_BY)) : null;
//            String pardonedReason = pardoned ? entry.get(TablePunishments.PARDONED_REASON) : null;
//
//            punishments.add(new Punishment(uuid, offence, severity, from, to, punishedBy, reason, pardoned, pardonedOn, pardonedBy, pardonedReason));
//        }
//    }
//
//    public static PunishmentHandler getHandler(UUID uuid) {
//        for (PunishmentHandler handler : handlers) {
//            if (handler.uuid.toString().equals(uuid.toString()))
//                return handler;
//        }
//
//        return new PunishmentHandler(uuid);
//    }
//
//    public static List<PunishmentHandler> getHandlers() {
//        return handlers;
//    }
}
