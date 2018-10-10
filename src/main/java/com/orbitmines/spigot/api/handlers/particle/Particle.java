package com.orbitmines.spigot.api.handlers.particle;

import com.orbitmines.spigot.OrbitMines;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.Collection;

/*
* OrbitMines - @author Fadi Shawki - 29-7-2017
*/
public class Particle {

    protected OrbitMines orbitMines;
    protected org.bukkit.Particle particle;
    protected boolean longDistance;
    protected Location location;
    protected Entity entity;
    protected float xOff;
    protected float yOff;
    protected float zOff;
    protected float speed;
    protected int amount;

    protected Vector vector;

    public Particle(org.bukkit.Particle particle) {
        this(particle, null);
    }

    public Particle(org.bukkit.Particle particle, Location location) {
        this.orbitMines = OrbitMines.getInstance();
        this.particle = particle;
        this.longDistance = true;
        this.location = location;
        this.xOff = 0;
        this.yOff = 0;
        this.zOff = 0;
        this.speed = 0;
        this.amount = 1;

        vector = new Vector(0, 0, 0);
    }

    public org.bukkit.Particle getParticle() {
        return particle;
    }

    public void setParticle(org.bukkit.Particle particle) {
        this.particle = particle;
    }

    public boolean isLongDistance() {
        return longDistance;
    }

    public void setLongDistance(boolean longDistance) {
        this.longDistance = longDistance;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void fixOnEntity(Entity entity) {
        this.entity = entity;
    }

    public float getX() {
        return (float) (entity != null ? entity.getLocation().getX() : location.getX()) + (float) vector.getX();
    }

    public float getY() {
        return (float) (entity != null ? entity.getLocation().getY() : location.getY()) + (float) vector.getY();
    }

    public float getZ() {
        return (float) (entity != null ? entity.getLocation().getZ() : location.getZ()) + (float) vector.getZ();
    }

    public void setOff(float xOff, float yOff, float zOff) {
        this.xOff = xOff;
        this.yOff = yOff;
        this.zOff = zOff;
    }

    public float getXOff() {
        return xOff;
    }

    public float getYOff() {
        return yOff;
    }

    public float getZOff() {
        return zOff;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    /* Only 0-255 */
    public void setRGB(int red, int green, int blue) {
        xOff = (red == 0 ? 0.001f : red) / 255;
        yOff = (green == 0 ? 0.001f: green) / 255;
        zOff = (blue == 0 ? 0.001f : blue) / 255;

        speed = 1;
        amount = 0;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Vector getVector() {
        return vector;
    }

    public void send() {
        send(location.getWorld().getPlayers());
    }

    public void send(Player... players) {
        send(Arrays.asList(players));
    }

    public void send(Collection<? extends Player> players) {
        for (Player player : players) {
            player.spawnParticle(particle, new Location(location.getWorld(), getX(), getY(), getZ()), amount, xOff, yOff, zOff, speed);
        }
    }
}
