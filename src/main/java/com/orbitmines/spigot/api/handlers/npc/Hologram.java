package com.orbitmines.spigot.api.handlers.npc;

import com.orbitmines.spigot.api.handlers.scoreboard.ScoreboardString;
import com.orbitmines.spigot.api.utils.Serializer;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */
public class Hologram extends Npc {

    /* Y-offset between holograms */
    public static final double Y_OFFSET_PER_LINE = 0.25;

    private static ArrayList<Hologram> holograms = new ArrayList<>();

    protected ArrayList<Line> lines;

    protected double yOff;
    protected Face face;

    public Hologram(Location spawnLocation) {
        this(spawnLocation, 1.75, Face.DOWN);
    }

    public Hologram(Location spawnLocation, double yOff, Face face) {
        super(spawnLocation.clone().add(0, yOff, 0));

        this.yOff = yOff;
        this.face = face;

        this.lines = new ArrayList<>();
    }

    @Override
    protected void spawn() {
        for (Line line : lines) {
            line.spawn(false);
        }
    }

    @Override
    protected void despawn() {
        for (Line line : lines) {
            line.despawn();
        }
    }

    @Override
    public void update() {
        for (Line line : lines) {
            line.update();
        }
    }

    @Override
    protected Collection<Entity> getEntities() {
        List<Entity> armorStands = new ArrayList<>();
        for (Line line : lines) {
            armorStands.add(line.armorStand);
        }
        return armorStands;
    }

    @Override
    protected void addToList() {
        holograms.add(this);
    }

    @Override
    protected void removeFromList() {
        holograms.remove(this);
    }

    @Override
    public void setSpawnLocation(Location spawnLocation) {
        super.setSpawnLocation(spawnLocation.clone().add(0, yOff, 0));
    }

    public void addLine(ScoreboardString line, boolean spawn) {
        Line l = new Line(line);
        lines.add(l);

        addLine(l, spawn);
    }

    public void addEmptyLine(boolean spawn) {
        Line l = new Line(null);
        lines.add(l);

        addLine(l, spawn);
    }

    private void addLine(Line l, boolean spawn) {
        if (!spawn)
            return;

        switch (face) {

            case UP:
                for (Line l1 : lines) {
                    l1.move(Face.UP);
                }
                break;
        }
        l.spawn(true);
    }

    public void setLine(int index, ScoreboardString line) throws IndexOutOfBoundsException {
        Line l = getLine(index);
        l.setLine(line);

        l.update();
    }

    public void removeLine(int index) throws IndexOutOfBoundsException {
        Line l = getLine(index);
        lines.remove(l);

        l.despawn();

        /* Last line, so no need to update all the others */
        if (index > lines.size())
            return;

        /* Move all the lines underneath the removed line up. */
        for (int i = index; i <= lines.size(); i++) {
            getLine(index).move(face.reverse());
        }
    }

    public void removeAllLines() {
        for (Line line : lines) {
            removeLine(getIndexOf(line));
        }
    }

    public void hideLine(int index) throws IndexOutOfBoundsException {
        Line l = getLine(index);
        l.setHidden(true);

        l.update();
    }

    public void hideLines() throws IndexOutOfBoundsException {
        for (int i = 1; i <= lines.size(); i++) {
            hideLine(i);
        }
    }

    private Line getLine(int index) throws IndexOutOfBoundsException {
        switch (face) {

            case DOWN:
                return lines.get(index - 1);
            case UP:
                return lines.get(lines.size() - index);
            default:
                throw new IllegalArgumentException();
        }
    }

    private int getIndexOf(Line line) {
        switch (face) {

            case DOWN:
                return lines.indexOf(line);
            case UP:
                return lines.size() - lines.indexOf(line) - 1;
            default:
                throw new IllegalArgumentException();
        }
    }

    public List<String> getLines() {
        List<String> lines = new ArrayList<>();
        for (Line line : this.lines) {
            lines.add(line.line.getString());
        }
        return lines;
    }

    public double getYOff() {
        return yOff;
    }

    public void setYOff(double yOff) {
        this.yOff = yOff;
    }

    public Face getFace() {
        return face;
    }

    public void setFace(Face face) {
        this.face = face;
    }

    public String serialize() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Line line : lines) {
            stringBuilder.append(line.getLine().getString().replaceAll("§", "&"));
            stringBuilder.append(";");
        }

        return Serializer.serialize(spawnLocation.clone().subtract(0, yOff, 0)) + "~" + stringBuilder.toString().substring(0, stringBuilder.length() -1);
    }

    public static ArrayList<Hologram> getHolograms() {
        return holograms;
    }

    public static Hologram getHologram(Entity entity) {
        for (Hologram hologram : holograms) {
            for (Line line : hologram.lines) {
                if (line.getArmorStand() != null && line.getArmorStand() == entity)
                    return hologram;
            }
        }
        return null;
    }

    protected class Line {

        private ArmorStand armorStand;
        private ScoreboardString line;

        private boolean hidden;

        public Line(ScoreboardString line) {
            this.line = line;
            this.hidden = false;
        }

        public ArmorStand getArmorStand() {
            return armorStand;
        }

        public ScoreboardString getLine() {
            return line;
        }

        public void setLine(ScoreboardString line) {
            this.line = line;
        }

        public boolean isHidden() {
            return hidden;
        }

        public void setHidden(boolean hidden) {
            this.hidden = hidden;
        }

        public void spawn(boolean updateWatchers) {
            armorStand = plugin.getNms().armorStand().spawn(new Location(spawnLocation.getWorld(), spawnLocation.getX(), spawnLocation.getY() - (getIndexOf(this) * Y_OFFSET_PER_LINE * face.getMultiplier()), spawnLocation.getZ()), false);
            armorStand.setMarker(true);
            armorStand.setRemoveWhenFarAway(false);
            armorStand.setGravity(false);

            /* Hide for non-watchers, if the line was spawned separately, and not at once with all the other lines through Npc#create */
            if (updateWatchers)
                updateWatchers();

            update();
        }

        public void despawn() {
            if (armorStand != null)
                armorStand.remove();
        }

        public void update() {
            if (hidden || line == null || line.getString() == null) {
                armorStand.setCustomName(null);
                armorStand.setCustomNameVisible(false);
            } else {
                armorStand.setCustomName(line.getString());
                armorStand.setCustomNameVisible(true);
            }
        }

        public void move(Face face) {
            if (armorStand == null)
                return;

            armorStand.teleport(armorStand.getLocation().add(0, Y_OFFSET_PER_LINE * face.reverse().getMultiplier(), 0));
        }
    }

    public enum Face {

        DOWN(1.0),
        UP(-1.0);

        private final double multiplier;

        Face(double multiplier) {
            this.multiplier = multiplier;
        }

        public double getMultiplier() {
            return multiplier;
        }

        public Face reverse() {
            return this == DOWN ? UP : DOWN;
        }
    }
}
