package com.orbitmines.api;
/*
 * OrbitMines - @author Fadi Shawki - 12-6-2018
 */

import com.orbitmines.api.utils.DateUtils;
import com.orbitmines.api.utils.LootUtils;

import java.time.Month;

public enum TopVoterReward {

    FIRST_PLACE(LootUtils.BUYCRAFT_VOUCHER, 5),
    SECOND_PLACE(LootUtils.BUYCRAFT_VOUCHER, 3),
    THIRD_PLACE(LootUtils.BUYCRAFT_VOUCHER, 2);

    public static final int COMMUNITY_GOAL = 2000;
    public static final int COMMUNITY_GOAL_SOLARS_PER_VOTE = 2;

    public static final PersonalAchievement[] MONTHLY_ACHIEVEMENT_VOTES = {
        new PersonalAchievement(1, 50, 2500, 0),
        new PersonalAchievement(2, 100, 0, 250),
        new PersonalAchievement(3, DateUtils.getMonthEnum() == Month.FEBRUARY ? 140 : 150, 2500, 250)
    };

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

    public static class PersonalAchievement {

        private final int tier;
        private final int votes;
        private final int prisms;
        private final int solars;

        public PersonalAchievement(int tier, int votes, int prisms, int solars) {
            this.tier = tier;
            this.votes = votes;
            this.prisms = prisms;
            this.solars = solars;
        }

        public int getTier() {
            return tier;
        }

        public int getVotes() {
            return votes;
        }

        public int getPrisms() {
            return prisms;
        }

        public int getSolars() {
            return solars;
        }
    }
}
