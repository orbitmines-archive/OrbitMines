package com.orbitmines.spigot.servers.kitpvp.handlers;

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
import com.orbitmines.spigot.api.handlers.achievements.StoredProgressAchievement;
import com.orbitmines.spigot.servers.kitpvp.handlers.kits.KitDrunk;
import com.orbitmines.spigot.servers.kitpvp.handlers.kits.KitEnchanter;
import com.orbitmines.spigot.servers.kitpvp.handlers.kits.KitMage;

import java.util.List;

public enum KitPvPAchievements implements Achievement {

    THOR("Thor", Rarity.RARE,
            new Loot.Instance[] {
                    new Loot.Instance(Loot.PRISMS, 2500),
                    new Loot.Instance(Loot.SOLARS, 50)
            },
            new Message("Krijg 100 Kills met", "Get 100 Kills with"),
            new Message("Jarnbjorn.")
    ),
    KINGSLAYER("Kingslayer", Rarity.RARE,
            new Loot.Instance[] {
                    new Loot.Instance(Loot.PRISMS, 4500),
                    new Loot.Instance(Loot.SOLARS, 75)
            },
            new Message("Krijg 150 King Kills.", "Get 150 King Kills.")
    ),
    MERLIN("Merlin", Rarity.RARE,
            new Loot.Instance[] {
                    new Loot.Instance(Loot.PRISMS, 6500),
                    new Loot.Instance(Loot.SOLARS, 100)
            },
            new Message("Krijg een 15 kill streak", "Get a 15 kill streak"),
            new Message("met een spellcaster.", "with any spellcaster.")
    ),
    HUNTER("Hunter", Rarity.RARE,
            new Loot.Instance[] {
                    new Loot.Instance(Loot.PRISMS, 8500),
                    new Loot.Instance(Loot.SOLARS, 100)
            },
            new Message("Krijg 100 Bunny Kills", "Get 100 Bunny Kills"),
            new Message("als Archer.", "as Archer.")
    ),
    DRUNKEN_FOOL("Drunken Fool", Rarity.EPIC,
            new Loot.Instance[] {
                    new Loot.Instance(Loot.PRISMS, 11000),
                    new Loot.Instance(Loot.SOLARS, 125)
            },
            new Message("Doe 5,000 damage", "Deal 5,000 damage"),
            new Message("met Drunk.", "as Drunk.")
    ),
    MANSLAUGHTER("Manslaughter", Rarity.EPIC,
            new Loot.Instance[] {
                    new Loot.Instance(Loot.PRISMS, 14000),
                    new Loot.Instance(Loot.SOLARS, 175)
            },
            new Message("Krijg 1,000 Kills.", "Get 1,000 Kills.")
    ),
    KEEPING_SCORE("Keeping Score", Rarity.EPIC,
            new Loot.Instance[] {
                    new Loot.Instance(Loot.PRISMS, 20000),
                    new Loot.Instance(Loot.SOLARS, 250)
            },
            new Message("Doe 200,000 damage", "Deal 200,000 damage"),
            new Message("in totaal.", "in total.")
    );

    static {
        THOR.handler = new StoredProgressAchievement(THOR, 100);
        MERLIN.handler = new StoredProgressAchievement(MERLIN, 15) {
            @Override
            public int getCurrent(OMPlayer omp) {
                KitPvPData data = (KitPvPData) omp.getData(Data.Type.KITPVP);
                //TODO Better way to do this?
                List<KitPvPData.KitData> kitData = data.getAllKitData(KitMage.ID, KitEnchanter.ID);

                int highest = 0;
                for (KitPvPData.KitData kit : kitData) {
                    if (kit.getBestStreak() > highest)
                        highest = kit.getBestStreak();
                }

                return highest;
            }
        };
        KINGSLAYER.handler = new StoredProgressAchievement(KINGSLAYER, 150);
        HUNTER.handler = new StoredProgressAchievement(HUNTER, 100);
        DRUNKEN_FOOL.handler = new StoredProgressAchievement(DRUNKEN_FOOL, 5000) {
            @Override
            public int getCurrent(OMPlayer omp) {
                KitPvPData data = (KitPvPData) omp.getData(Data.Type.KITPVP);
                return (int) Math.floor(data.getKitData(KitDrunk.ID).getDamageDealt());
            }
        };
        MANSLAUGHTER.handler = new StoredProgressAchievement(MANSLAUGHTER, 1000) {
            @Override
            public int getCurrent(OMPlayer omp) {
                KitPvPData data = (KitPvPData) omp.getData(Data.Type.KITPVP);
                return data.getKills();
            }
        };
        KEEPING_SCORE.handler = new StoredProgressAchievement(KEEPING_SCORE, 200000) {
            @Override
            public int getCurrent(OMPlayer omp) {
                KitPvPData data = (KitPvPData) omp.getData(Data.Type.KITPVP);
                return (int) Math.floor(data.getDamageDealt());
            }
        };
    }

    private final String name;
    private final Rarity rarity;
    private final Loot.Instance[] rewards;
    private final Message[] description;

    private AchievementHandler handler;

    KitPvPAchievements(String name, Rarity rarity, Loot.Instance reward, Message... description) {
        this(name, rarity, new Loot.Instance[] { reward }, description);
    }
    KitPvPAchievements(String name, Rarity rarity, Loot.Instance[] rewards, Message... description) {
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
        return Server.KITPVP;
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
