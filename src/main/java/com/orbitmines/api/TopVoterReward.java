package com.orbitmines.api;
/*
 * OrbitMines - @author Fadi Shawki - 12-6-2018
 */

public enum TopVoterReward {

    FIRST_PLACE("BUYCRAFT_VOUCHER", 5),
    SECOND_PLACE("BUYCRAFT_VOUCHER", 2),
    THIRD_PLACE("BUYCRAFT_VOUCHER", 1);

    public static final int STEP_VOTES = 25;

    public static final int COMMUNITY_GOAL = 1000;
    public static final int COMMUNITY_GOAL_SOLARS_PER_VOTE = 2;

    public static final int MONTHLY_ACHIEVEMENT_VOTES = 50;
    public static final int MONTHLY_ACHIEVEMENT_PRISMS = 5000;

    private final String loot;
    private final int step;

    TopVoterReward(String loot, int step) {
        this.loot = loot;
        this.step = step;
    }

    public String getLoot() {
        return loot;
    }

    public int getStep() {
        return step;
    }
}
