package com.orbitmines.api.punishment.offences;
/*
 * OrbitMines - @author Fadi Shawki - 16-6-2018
 */

import com.orbitmines.api.punishment.Punishment;

import java.util.concurrent.TimeUnit;

public enum Severity {

    WARNING("Warning", Punishment.Duration.NONE),
    SEV_1("Severity 1", Punishment.Duration.EXPIRE_DATE, TimeUnit.HOURS.toMillis(2), TimeUnit.HOURS.toMillis(2)),
    SEV_2("Severity 2", Punishment.Duration.EXPIRE_DATE, TimeUnit.DAYS.toMillis(2), TimeUnit.HOURS.toMillis(16)),
    SEV_3("Severity 3", Punishment.Duration.PERMANENT);

    private final String name;
    private final Punishment.Duration duration;
    private final long muteMillis;
    private final long banMillis;

    Severity(String name, Punishment.Duration duration) {
        this(name, duration, 0, 0);
    }

    Severity(String name, Punishment.Duration duration, long muteMillis, long banMillis) {
        this.name = name;
        this.duration = duration;
        this.muteMillis = muteMillis;
        this.banMillis = banMillis;
    }

    public String getName() {
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
