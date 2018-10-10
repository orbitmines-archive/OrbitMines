package com.orbitmines.api.punishment.offences;
/*
 * OrbitMines - @author Fadi Shawki - 16-6-2018
 */

import com.orbitmines.api.Message;
import com.orbitmines.api.punishment.Punishment;

public enum Offence {

    PUNISHMENT(new Message("Punishment"), Punishment.Type.BAN),
    CHAT(new Message("Chat Mute"), Punishment.Type.MUTE),
    HACKING(new Message("Hacken", "Hacking"), Punishment.Type.BAN),
    SKIN(new Message("Skin"), Punishment.Type.BAN),
    NAME(new Message("Name"), Punishment.Type.BAN),
    NETWORK_BAN(new Message("Permanente Ban", "Permanent Ban"), Punishment.Type.BAN);

    private final Message name;
    private final Punishment.Type type;

    Offence(Message name, Punishment.Type type) {
        this.name = name;
        this.type = type;
    }

    public Message getName() {
        return name;
    }

    public Punishment.Type getType() {
        return type;
    }
}
