package com.orbitmines.api.punishment.offences;
/*
 * OrbitMines - @author Fadi Shawki - 16-6-2018
 */

import com.orbitmines.api.punishment.Punishment;

public enum Offence {

    GAME_PLAY("Game Play", Punishment.Type.BAN),
    CHAT("Chat", Punishment.Type.MUTE),
    HACKING("Hacking", Punishment.Type.BAN),
    SKIN("Skin", Punishment.Type.BAN),
    NAME("Name", Punishment.Type.BAN),
    NETWORK_BAN("Network Ban", Punishment.Type.BAN);

    private final String name;
    private final Punishment.Type type;

    Offence(String name, Punishment.Type type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public Punishment.Type getType() {
        return type;
    }
}
