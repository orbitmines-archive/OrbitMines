package com.orbitmines.spigot.api.handlers.particle;

import com.orbitmines.spigot.api.runnables.SpigotRunnable;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collection;

/*
* OrbitMines - @author Fadi Shawki - 29-7-2017
*/
public abstract class ParticleAnimation extends Particle {

    protected boolean positive;
    protected int index;

    protected SpigotRunnable runnable;

    public ParticleAnimation(org.bukkit.Particle particle) {
        this(particle, null);
    }

    public ParticleAnimation(org.bukkit.Particle particle, Location location) {
        super(particle, location);

        runnable = new SpigotRunnable(getInterval(), false) {
            @Override
            public void run() {
                playAnimation();
            }
        };
    }

    public abstract void playAnimation();

    public abstract SpigotRunnable.Time getInterval();

    public void stop() {
        runnable.cancel();
    }

    public void start() {
        runnable.start();
    }

    public float getOppositeX() {
        return (float) (entity != null ? entity.getLocation().getX() : location.getX()) - (float) vector.getX();
    }

    public float getOppositeY() {
        return (float) (entity != null ? entity.getLocation().getY() : location.getY()) - (float) vector.getY();
    }

    public float getOppositeZ() {
        return (float) (entity != null ? entity.getLocation().getZ() : location.getZ()) - (float) vector.getZ();
    }

    public void sendOpposite() {
        sendOpposite(location.getWorld().getPlayers());
    }

    public void sendOpposite(Player... players) {
        sendOpposite(Arrays.asList(players));
    }

    public void sendOpposite(Collection<? extends Player> players) {
        orbitMines.getNms().particle().send(players, particle, longDistance, getOppositeX(), getOppositeY(), getOppositeZ(), xOff, yOff, zOff, speed, amount);
    }

    public void sendInvert() {
        sendInvert(location.getWorld().getPlayers());
    }

    public void sendInvert(Player... players) {
        sendInvert(Arrays.asList(players));
    }

    public void sendInvert(Collection<? extends Player> players) {
        orbitMines.getNms().particle().send(players, particle, longDistance, getOppositeX(), getY(), getOppositeZ(), xOff, yOff, zOff, speed, amount);
    }

    public void sendOppositeInvert() {
        sendOppositeInvert(location.getWorld().getPlayers());
    }

    public void sendOppositeInvert(Player... players) {
        sendOppositeInvert(Arrays.asList(players));
    }

    public void sendOppositeInvert(Collection<? extends Player> players) {
        orbitMines.getNms().particle().send(players, particle, longDistance, getX(), getOppositeY(), getZ(), xOff, yOff, zOff, speed, amount);
    }

    /* Library methods */
    protected double toRadians(double angle) {
        return Math.toRadians(angle);
    }

    /* y = |a| * sin( |b|(x + c) ) + d */
    protected double sinusoide(double x, double a, double b, double c, double d) {
        return Math.abs(a) * Math.sin(Math.abs(b) * (x + c)) + d;
    }

    protected void rotateAroundY(double angle) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);

        double vX = vector.getX();
        double vZ = vector.getZ();

        vector.setX(vX * cos + vZ * sin);
        vector.setZ(vX * -sin + vZ * cos);
    }

    protected void rotateAroundX(double angle) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);

        double vY = vector.getY();
        double vZ = vector.getZ();

        vector.setY(vY * cos - vZ * sin);
        vector.setZ(vY * sin + vZ * cos);
    }

    protected void rotateAroundZ(double angle) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);

        double vX = vector.getX();
        double vY = vector.getY();

        vector.setX(vX * cos - vY * sin);
        vector.setY(vX * sin + vY * cos);
    }
}
