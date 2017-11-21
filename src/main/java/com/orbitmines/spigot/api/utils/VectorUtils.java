package com.orbitmines.spigot.api.utils;

import com.orbitmines.api.utils.RandomUtils;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class VectorUtils {

    public static Vector point(Vector from, Vector to) {
        return to.clone().subtract(from);
    }

    public static Vector point2D(Vector from, Vector to) {
        return to.clone().subtract(from).setY(0);
    }

    public static Vector setLength(Vector vector, double length) {
        return vector.normalize().multiply(length);
    }

    public static Vector cap(Vector vector) {
        return cap(vector, 1D);
    }

    public static Vector cap(Vector vector, double maxLength) {
        return vector.length() > maxLength ? vector.normalize().multiply(maxLength) : vector;
    }

    public static Vector invert(Vector vector) {
        return vector.multiply(new Vector(-1, 1, -1));
    }

    public static Vector opposite(Vector vector) {
        return vector.multiply(-1);
    }

    public static Vector invertOpposite(Vector vector) {
        return vector.multiply(new Vector(1, -1, 1));
    }

    public static double distance(Vector from, Vector to) {
        return from.distance(to);
    }

    public static double distance2D(Vector from, Vector to) {
        return from.clone().setY(0).distance(to.clone().setY(0));
    }

    public static Vector absY(Vector vector) {
        return vector.setY(Math.abs(vector.getY()));
    }

    public static Vector random() {
        return random(1);
    }

    public static Vector random(double multiplier) {
        Random r = RandomUtils.RANDOM;
        return new Vector((r.nextDouble() - 0.5) * multiplier, (r.nextDouble() - 0.5) * multiplier, (r.nextDouble() - 0.5) * multiplier);
    }

    public static Vector randomUp() {
        return randomUp(1);
    }

    public static Vector randomUp(double multiplier) {
        return new Vector((Math.random() - 0.5) * multiplier, Math.abs((Math.random() - 0.5) * multiplier), (Math.random() - 0.5) * multiplier);
    }

    public static Vector randomCone(final Vector vector, double cone) {
        Vector v1 = vector.clone();
        final double length = v1.length();
        v1.add(VectorUtils.random(cone));
        return setLength(v1, length);
    }

    public static Set<Vector> line(Vector point1, Vector point2) {
        return line(point1, point2, 1D);
    }

    public static Set<Vector> line(Vector point1, Vector point2, double gap) {
        Set<Vector> set = new HashSet<>();
        Vector v1 = point(point1, point2);
        double distance = v1.length();
        int points = (int) (distance / gap);
        Vector v2 = v1.clone().normalize().multiply(gap);

        for (int i = 1; i <= points; i++) {
            set.add(point1.clone().add(v2.clone().multiply(i)));
        }

        return set;
    }

    public static Set<Vector> line(Vector point1, Vector point2, int points) {
        Set<Vector> set = new HashSet<>();
        Vector v1 = point(point1, point2);
        double distance = v1.length();
        double gap = distance / points;
        Vector v2 = v1.clone().normalize().multiply(gap);

        for (int i = 0; i < points; i++) {
            set.add(point1.add(v2.clone().multiply(i)));
        }

        return set;
    }
}
