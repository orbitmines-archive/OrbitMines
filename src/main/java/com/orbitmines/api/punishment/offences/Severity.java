package com.orbitmines.api.punishment.offences;
/*
 * OrbitMines - @author Fadi Shawki - 16-6-2018
 */

import com.orbitmines.api.Message;
import com.orbitmines.api.punishment.Punishment;

import java.util.concurrent.TimeUnit;

public enum Severity {

    WARNING(new Message("Waarschuwing", "Warning"), Punishment.Duration.NONE),
    SEV_1(new Message("Lichte Overtreding", "Light Offence"), Punishment.Duration.EXPIRE_DATE, TimeUnit.HOURS.toMillis(2), TimeUnit.HOURS.toMillis(2)),
    SEV_2(new Message("Middelmatige Overtreding", "Moderate Offence"), Punishment.Duration.EXPIRE_DATE, TimeUnit.DAYS.toMillis(2), TimeUnit.HOURS.toMillis(16)),
    SEV_3(new Message("Zware Overtreding", "Severe Offence"), Punishment.Duration.PERMANENT);

    private final Message name;
    private final Punishment.Duration duration;
    private final long muteMillis;
    private final long banMillis;

    Severity(Message name, Punishment.Duration duration) {
        this(name, duration, 0, 0);
    }

    Severity(Message name, Punishment.Duration duration, long muteMillis, long banMillis) {
        this.name = name;
        this.duration = duration;
        this.muteMillis = muteMillis;
        this.banMillis = banMillis;
    }

    public Message getName() {
        return name;
    }

    public Punishment.Duration getDuration() {
        return duration;
    }

    public long getMillis(Punishment.Type type) {
        switch (type) {

            case MUTE:
                return muteMillis;
            case BAN:
                return banMillis;
            default:
                return 0;
        }
    }

    public Severity next() {
        Severity[] severities = Severity.values();

        if (ordinal() == severities.length -1)
            return this;

        return severities[ordinal() + 1];
    }
}
