package com.orbitmines.spigot.servers.survival.handlers;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.Message;
import com.orbitmines.api.Rarity;
import com.orbitmines.api.Server;
import com.orbitmines.spigot.api.Loot;
import com.orbitmines.spigot.api.handlers.achievements.Achievement;
import com.orbitmines.spigot.api.handlers.achievements.AchievementHandler;
import com.orbitmines.spigot.api.handlers.achievements.StoredProgressAchievement;

public enum SurvivalAchievements implements Achievement {

    DIAMONDS("Diamonds!", Rarity.RARE,
            new Loot.Instance[] {
                new Loot.Instance(Loot.PRISMS, 3000),
                new Loot.Instance(Loot.SOLARS, 75)
            },
            new Message("Mine 250 Diamonds.")
    ),
    LOADS_OF_EXPERIENCE("Loads of Experience", Rarity.RARE,
            new Loot.Instance[] {
                new Loot.Instance(Loot.PRISMS, 5000),
                new Loot.Instance(Loot.SOLARS, 100)
            },
            new Message("Bereik Level 100.", "Reach Level 100.")
    ),
    SALESMAN("Salesman", Rarity.RARE,
            new Loot.Instance[] {
                new Loot.Instance(Loot.PRISMS, 7000),
                new Loot.Instance(Loot.SOLARS, 100)
            },
            new Message("Verdien 100,000 Credits", "Earn 100,000 Credits"),
            new Message("met Chest Shops", "through Chest Shops.")
    ),
    TIME_WITHERED_AWAY("Time Withered Away", Rarity.EPIC,
            new Loot.Instance[] {
                new Loot.Instance(Loot.PRISMS, 10000),
                new Loot.Instance(Loot.SOLARS, 150)
            },
            new Message("Verzamel 15 Wither", "Gather 15 Wither"),
            new Message("Skeleton Skulls.", "Skeleton Skulls.")
    ),
    CROWDED("Crowded", Rarity.EPIC,
            new Loot.Instance[] {
                    new Loot.Instance(Loot.PRISMS, 15000),
                    new Loot.Instance(Loot.SOLARS, 175)
            },
            new Message("Heb 10 verschillende", "Have 10 different"),
            new Message("spelers tegelijkertijd", "players in your claims"),
            new Message("in jouw claims.", "all at once.")
    );

    static {
        DIAMONDS.handler = new StoredProgressAchievement(DIAMONDS, 250);
        LOADS_OF_EXPERIENCE.handler = new StoredProgressAchievement(LOADS_OF_EXPERIENCE, 100);
        SALESMAN.handler = new StoredProgressAchievement(SALESMAN, 100000);
        TIME_WITHERED_AWAY.handler = new StoredProgressAchievement(TIME_WITHERED_AWAY, 15);
        CROWDED.handler = new StoredProgressAchievement(CROWDED, 10);
    }

    private final String name;
    private final Rarity rarity;
    private final Loot.Instance[] rewards;
    private final Message[] description;

    private AchievementHandler handler;

    SurvivalAchievements(String name, Rarity rarity, Loot.Instance reward, Message... description) {
        this(name, rarity, new Loot.Instance[] { reward }, description);
    }

    SurvivalAchievements(String name, Rarity rarity, Loot.Instance[] rewards, Message... description) {
        this.name = name;
        this.rarity = rarity;
        this.rewards = rewards;
        this.description = description;
    }

    @Override
    public int getId() {
        return ordinal();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Server getServer() {
        return Server.SURVIVAL;
    }

    @Override
    public Rarity getRarity() {
        return rarity;
    }

    @Override
    public Loot.Instance[] getRewards() {
        return rewards;
    }

    @Override
    public Message[] getDescription() {
        return description;
    }

    @Override
    public AchievementHandler getHandler() {
        return handler;
    }
}
