package com.orbitmines.spigot.servers.kitpvp.handlers;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.Color;
import com.orbitmines.api.utils.NumberUtils;
import com.orbitmines.spigot.servers.kitpvp.handlers.passives.PassiveArrowRegen;
import com.orbitmines.spigot.servers.kitpvp.handlers.passives.PassiveBowLightning;
import com.orbitmines.spigot.servers.kitpvp.handlers.passives.PassiveWreckerOfWorlds;

public enum Passive {

    BOW_LIGHTNING("Lightning", Color.SILVER, new PassiveBowLightning()),
    WRECKER_OF_WORLDS("Wrecker of Worlds", Color.SILVER, new PassiveWreckerOfWorlds()),




    ARROW_REGEN("Arrow Regen", Color.WHITE, new PassiveArrowRegen()),
    ;

    private final String name;
    private final Color color;
    private final Handler handler;

    Passive(String name, Color color, Handler handler) {
        this.name = name;
        this.color = color;
        this.handler = handler;
    }

    public String getName() {
        return name;
    }

    public String getDisplayName(int level) {
        return color.getChatColor() + name + " " + NumberUtils.toRoman(level);
    }

    public Color getColor() {
        return color;
    }

    public Handler getHandler() {
        return handler;
    }

    public interface Handler {

        void trigger(KitPvPPlayer player, KitPvPPlayer triggeredBy, int level);

    }
}
