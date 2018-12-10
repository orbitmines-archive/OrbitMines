package com.orbitmines.spigot.servers.hub.handlers.Cosmetics;

import com.orbitmines.spigot.api.handlers.OMPlayer;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class ComposedItem {

    public enum TargetType {HEAD, BODY}

    private List<ComposedItemPart> parts;
    private Vector offset;
    private TargetType target;
    private OMPlayer player;

    public ComposedItem (OMPlayer player, TargetType target, Vector offset) {
        parts = new ArrayList<>();
        this.offset = offset;
        this.target = target;
        this.player = player;
    }

    public ComposedItem addPart(ComposedItemPart part) {
        parts.add(part);
        return this;
    }

    public ComposedItem removePart(ComposedItemPart part) {
        if (parts.contains(part)) parts.remove(part);
        return this;
    }

    public void update() {
        Location loc;
        switch (target) {
            case HEAD:
                loc = player.getPlayer().getEyeLocation().add(offset);
                break;
            default:
                loc = player.getPlayer().getLocation().add(offset);
        }
        for (ComposedItemPart part : parts) {
            part.update(loc);
        }
    }

    public void setPlayer(OMPlayer player) {
        this.player = player;
    }
}
