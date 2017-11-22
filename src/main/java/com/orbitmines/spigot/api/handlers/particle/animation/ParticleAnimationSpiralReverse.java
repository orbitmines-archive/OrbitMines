package com.orbitmines.spigot.api.handlers.particle.animation;

import com.orbitmines.spigot.api.handlers.particle.ParticleAnimation;
import org.bukkit.Location;
import org.bukkit.util.Vector;

/*
* OrbitMines - @author Fadi Shawki - 29-7-2017
*/
public abstract class ParticleAnimationSpiralReverse extends ParticleAnimation {

    protected double angle;

    protected Location from;
    protected Location to;

    protected Vector fromTo;
    protected double distance;

    protected double steps;
    protected double circleLength;
    protected Vector circleStep;
    protected double circleStepDistance;

    protected double startRadius;
    protected double maxRadius;
    protected double circleStepRadius;

    protected int currentStep;

    public ParticleAnimationSpiralReverse(org.bukkit.Particle particle, Location from, Location to) {
        super(particle, from);

        this.from = from;
        this.to = to;

        fromTo = to.subtract(from).toVector();
        distance = new Vector().distance(fromTo);

        steps = getCircleAmount();
        circleLength = distance / steps;
        /* Vector step per circle */
        circleStep = fromTo.clone().multiply(1 / steps);
        circleStepDistance = distance * (1 / steps);

        startRadius = getStartRadius();
        maxRadius = getMaxRadius();

        circleStepRadius = 1 / (maxRadius / startRadius);

        currentStep = (int) steps;
        angle = 360;
    }

    /* Before particle is sent*/
    public abstract void onAnimation();

    /* After particle is sent*/
    public abstract void afterAnimation();

    /* After cycle is finished */
    public abstract void finishedCycle();

    /* Amount of particles in circle */
    public abstract int getAmount();

    /* Circle width (start) */
    public abstract double getStartRadius();

    /* Circle width (max) */
    public abstract double getMaxRadius();

    /* Circle amount in spiral */
    public abstract int getCircleAmount();

    /* Amount of particles per ParticleAnimation#getInterval */
    public abstract int getAmountPerTick();

    @Override
    public void playAnimation() {
        for (int i = 0; i < getAmountPerTick(); i++) {
            if (angle < 0) {
                angle += 360;

                if (currentStep == 0) {
                    stop();
                    angle = 360;
                    currentStep = (int) steps;
                    vector = new Vector();
                    start();
                    finishedCycle();
                    break;
                } else {
                    currentStep--;
                }
            }

            angle -= (360 / getAmount());

            double rad = toRadians(angle);

            double multiplier = 1D / (double)getAmount();
            double radius = circleStepRadius * ((currentStep + 1) + multiplier);

            vector.setX(radius * Math.sin(rad));
            vector.setY(vector.getY() - /* TODO + / - */ multiplier * circleStepDistance);
            vector.setZ(radius * Math.cos(rad));

            onAnimation();

            send();
        }

        afterAnimation();
    }
}
