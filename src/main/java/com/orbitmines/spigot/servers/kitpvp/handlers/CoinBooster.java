package com.orbitmines.spigot.servers.kitpvp.handlers;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.CachedPlayer;
import com.orbitmines.api.Color;
import com.orbitmines.api.Language;
import com.orbitmines.api.VipRank;
import com.orbitmines.api.utils.TimeUtils;
import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.api.handlers.itembuilders.ItemBuilder;
import com.orbitmines.spigot.api.handlers.timer.Timer;
import com.orbitmines.spigot.api.runnables.SpigotRunnable;
import org.bukkit.Material;

import java.util.UUID;

public class CoinBooster {

    public static CoinBooster ACTIVE;

    private final OrbitMines orbitMines;

    private final Type type;
    private final CachedPlayer player;

    private Timer timer;

    public CoinBooster(Type type, UUID uuid) {
        this.orbitMines = OrbitMines.getInstance();

        this.type = type;
        this.player = CachedPlayer.getPlayer(uuid);
    }

    public void start() {
        String name = player.getRankPrefixColor().getChatColor() + player.getPlayerName();

        orbitMines.broadcast("", Color.ORANGE, name + " §7heeft een " + player.getRankPrefixColor().getChatColor() + "§l" + type.multiplier + "x Coin Booster §7geactiveerd (§6" + TimeUtils.fromTimeStamp(type.duration.getTicks() * 50, Language.DUTCH) + "§7).", name + " §7has activated a " + type.getColor().getChatColor() + "§l" + type.multiplier + "x Coin Booster §7(§6" + TimeUtils.fromTimeStamp(type.duration.getTicks() * 50, Language.ENGLISH) + "§7).");

        timer = new Timer(type.duration, new SpigotRunnable.Time(SpigotRunnable.TimeUnit.MINUTE, 5)) {
            @Override
            public void onInterval() {
                orbitMines.broadcast("", Color.ORANGE, name + "'s " + type.getColor().getChatColor() + "§l" + type.multiplier + "x Coin Booster §7verloopt in §6" + TimeUtils.fromTimeStamp(getRemainingTicks() * 50, Language.DUTCH) + "§7.", name + "'s " + type.getColor().getChatColor() + "§l" + type.multiplier + "x Coin Booster §7expires in §6" + TimeUtils.fromTimeStamp(getRemainingTicks() * 50, Language.ENGLISH) + "§7.");
            }

            @Override
            public void onFinish() {
                orbitMines.broadcast("", Color.ORANGE, name + "'s " + type.getColor().getChatColor() + "§l" +  type.multiplier + "x Coin Booster §7is verlopen.", name + "'s " + type.getColor().getChatColor() + "§l" + type.multiplier + "x Coin Booster §7has expired.");
                ACTIVE = null;
            }
        };

        ACTIVE = this;
    }

    public Type getType() {
        return type;
    }

    public CachedPlayer getPlayer() {
        return player;
    }

    public Timer getTimer() {
        return timer;
    }

    public enum Type {

        DEFAULT(1.25, VipRank.NONE, 250, "Coin Booster", VipRank.NONE.getPrefixColor(), new ItemBuilder(Material.GOLD_NUGGET), new SpigotRunnable.Time(SpigotRunnable.TimeUnit.MINUTE, 30)),
        IRON(1.5, VipRank.IRON, 250, VipRank.IRON.getName() + " Coin Booster", VipRank.IRON.getPrefixColor(), new ItemBuilder(Material.IRON_INGOT), new SpigotRunnable.Time(SpigotRunnable.TimeUnit.MINUTE, 30)),
        GOLD(1.75, VipRank.GOLD, 250, VipRank.GOLD.getName() + " Coin Booster", VipRank.GOLD.getPrefixColor(), new ItemBuilder(Material.GOLD_INGOT), new SpigotRunnable.Time(SpigotRunnable.TimeUnit.MINUTE, 30)),
        DIAMOND(2.0, VipRank.DIAMOND, 250, VipRank.DIAMOND.getName() + " Coin Booster", VipRank.DIAMOND.getPrefixColor(), new ItemBuilder(Material.DIAMOND), new SpigotRunnable.Time(SpigotRunnable.TimeUnit.MINUTE, 30)),
        EMERALD(2.5, VipRank.EMERALD, 250, VipRank.EMERALD.getName() + " Coin Booster", VipRank.EMERALD.getPrefixColor(), new ItemBuilder(Material.EMERALD), new SpigotRunnable.Time(SpigotRunnable.TimeUnit.MINUTE, 30));

        private final double multiplier;
        private final VipRank vipRank;
        private final int price;
        private final String name;
        private final Color color;
        private final ItemBuilder icon;
        private final SpigotRunnable.Time duration;

        Type(double multiplier, VipRank vipRank, int price, String name, Color color, ItemBuilder icon, SpigotRunnable.Time duration) {
            this.multiplier = multiplier;
            this.vipRank = vipRank;
            this.price = price;
            this.name = name;
            this.color = color;
            this.icon = icon;
            this.duration = duration;
        }

        public double getMultiplier() {
            return multiplier;
        }

        public VipRank getVipRank() {
            return vipRank;
        }

        public int getPrice() {
            return price;
        }

        public String getName() {
            return name;
        }

        public Color getColor() {
            return color;
        }

        public String getDisplayName() {
            return color.getChatColor() + "§l" + name;
        }

        public ItemBuilder getIcon() {
            return icon.clone();
        }

        public SpigotRunnable.Time getDuration() {
            return duration;
        }
    }
}
