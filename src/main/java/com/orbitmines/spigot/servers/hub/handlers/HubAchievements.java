package com.orbitmines.spigot.servers.hub.handlers;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.Message;
import com.orbitmines.api.Rarity;
import com.orbitmines.api.Server;
import com.orbitmines.spigot.api.Loot;
import com.orbitmines.spigot.api.handlers.Data;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.achievements.Achievement;
import com.orbitmines.spigot.api.handlers.achievements.AchievementHandler;
import com.orbitmines.spigot.api.handlers.achievements.EmptyAchievementHandler;
import com.orbitmines.spigot.api.handlers.achievements.StoredProgressAchievement;
import com.orbitmines.spigot.api.handlers.data.VoteData;

public enum HubAchievements implements Achievement {

    DISCORD_LINK("Discord Link", Rarity.COMMON,
            new Loot.Instance[] {
                    new Loot.Instance(Loot.PRISMS, 1250)
            },
            new Message("Link je Discord User", "Link your Discord User"),
            new Message("aan je OrbitMines account.", "to your OrbitMines account.")
    ),
    ORBITMINES_SUPPORTER("OrbitMines Supporter", Rarity.EPIC,
            new Loot.Instance[] {
                    new Loot.Instance(Loot.SOLARS, 1000)
            },
            new Message("Vote 250 keer.", "Vote 250 times.")
    );

    static {
        DISCORD_LINK.handler = new EmptyAchievementHandler(DISCORD_LINK);

        ORBITMINES_SUPPORTER.handler = new StoredProgressAchievement(ORBITMINES_SUPPORTER, 250) {
            @Override
            public int getCurrent(OMPlayer omp) {
                return ((VoteData) omp.getData(Data.Type.VOTES)).getTotalVotes();
            }
        };
    }

    private final String name;
    private final Rarity rarity;
    private final Loot.Instance[] rewards;
    private final Message[] description;

    private AchievementHandler handler;

    HubAchievements(String name, Rarity rarity, Loot.Instance reward, Message... description) {
        this(name, rarity, new Loot.Instance[] { reward }, description);
    }

    HubAchievements(String name, Rarity rarity, Loot.Instance[] rewards, Message... description) {
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
        return Server.HUB;
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
