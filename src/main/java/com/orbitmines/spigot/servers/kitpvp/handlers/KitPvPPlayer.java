package com.orbitmines.spigot.servers.kitpvp.handlers;

import com.orbitmines.api.utils.NumberUtils;
import com.orbitmines.spigot.api.handlers.Data;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.chat.ActionBar;
import com.orbitmines.spigot.api.handlers.chat.ComponentMessage;
import com.orbitmines.spigot.api.handlers.chat.Title;
import com.orbitmines.spigot.api.nms.entity.EntityNms;
import com.orbitmines.spigot.api.nms.itemstack.ItemStackNms;
import com.orbitmines.spigot.servers.kitpvp.KitPvP;
import com.orbitmines.spigot.servers.kitpvp.handlers.passives.DummyEvent;
import com.orbitmines.spigot.servers.kitpvp.handlers.passives.Passive;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nullable;
import java.util.*;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class KitPvPPlayer extends OMPlayer {

    private static Map<Player, KitPvPPlayer> players = new HashMap<>();

    private final KitPvP kitPvP;

    private final LevelData levelData;

    private boolean spectator;

    private boolean spawnProtection;
    private KitPvPKit.Level selectedKit;
    private int killStreak;

    public KitPvPPlayer(KitPvP kitPvP, Player player) {
        super(player);

        this.kitPvP = kitPvP;

        levelData = new LevelData(this);
        killStreak = 0;
    }

    @Override
    protected void onLogin() {
        players.put(player, this);

        clearFullInventory();
        clearPotionEffects();

        /* Lobby Kit */
        kitPvP.getLobbyKit(this).setItems(this);
        player.getInventory().setHeldItemSlot(4);

        /* Force Update */
        getData();
        /* Update Level */
        levelData.update(false);
        levelData.updateExperienceBar();

        /* Set Scoreboard */
        setScoreboard(new KitPvP.Scoreboard(orbitMines, this));
    }

    @Override
    protected void onLogout() {
        EntityNms nms = orbitMines.getNms().entity();

        if (selectedKit != null && player.getHealth() != nms.getAttribute(player, EntityNms.Attribute.MAX_HEALTH))
            player.damage(nms.getAttribute(player, EntityNms.Attribute.MAX_HEALTH), player.getLastDamageCause().getEntity());

        players.remove(player);
    }

    @Override
    protected void onFirstLogin() {

    }

    @Override
    public boolean canReceiveVelocity() {
        return true;
    }

    @Override
    public Collection<ComponentMessage.TempTextComponent> getChatPrefix() {
        return Arrays.asList(
                        new ComponentMessage.TempTextComponent(levelData.getPrefix(), HoverEvent.Action.SHOW_TEXT, getName() + "\n§7Level: " + levelData.getColor() + "§l" + levelData.getLevel() + "\n" + (levelData.getLevel() == LevelData.maxLevel ? levelData.getColor() + "Max Level" : "§7Next level: §e§l" + NumberUtils.locale(levelData.getCurrentLevelXp()) + " XP §7§l/ " + NumberUtils.locale(levelData.getNextLevelXp()))),

                        new ComponentMessage.TempTextComponent(isSpectator() ? " §eSpec " : "")
                );
    }

    public boolean hasSpawnProtection() {
        return spawnProtection;
    }

    public KitPvPKit.Level getSelectedKit() {
        return selectedKit;
    }

    /* Death & Kills */
    public void processKill(PlayerDeathEvent event, KitPvPPlayer killed) {
        addKill();

        /* Passives */
        {
            ItemStackNms nms = kitPvP.getOrbitMines().getNms().customItem();

            for (ItemStack item : getInventory().getContents()) {
                if (item == null)
                    continue;

                Map<Passive, Integer> passives = Passive.from(nms, item, Passive.Interaction.KILL_PLAYER);

                /* No Passives on this item */
                if (passives == null)
                    continue;

                for (Passive passive : passives.keySet()) {
                    passive.getHandler().trigger(event, passives.get(passive));
                }
            }
        }

        /* Coins */
        int coins = KitPvP.COINS_PER_KILL;

        coins += applyMultiplier(coins, getCoinMultiplier());
        if (CoinBooster.ACTIVE != null)
            coins += applyMultiplier(coins, CoinBooster.ACTIVE.getType().getMultiplier());

        /* Experience */
        int experience = KitPvP.XP_PER_KILL;

        experience += applyMultiplier(experience, getXPMultiplier());

        /* Give Rewards */
        addCoinsExperience(coins, experience, true);

        /* Kill Hologram */
        //TODO
    }

    private int applyMultiplier(int current, double multiplier) {
        return (int) (current * (multiplier - 1));
    }

    public void processDeath(PlayerDeathEvent event, @Nullable KitPvPPlayer killer) {
        addDeath();

        this.selectedKit = null;
        this.killStreak = 0;

        levelData.updateExperienceBar();

        //event.setDeathMessage(null);
        //TODO DEATH MESSAGES + DISCORD

        //TODO SPAWN BED
    }

    /* Joining Map */

    public void joinMap() {
        joinMap(getLastSelected());
    }

    public void joinMap(KitPvPKit.Level selectedKit) {
        teleportToMap();
        setSelectedKit(selectedKit);
        player.getInventory().setHeldItemSlot(0);
    }

    public void setSelectedKit(KitPvPKit.Level selectedKit) {
        this.selectedKit = selectedKit;

        clearFullInventory();
        clearPotionEffects();

        /* Apply Attributes */
        EntityNms nms = orbitMines.getNms().entity();
        nms.setAttribute(player, EntityNms.Attribute.MAX_HEALTH, selectedKit.getMaxHealth());
        player.setHealth(selectedKit.getMaxHealth());
        nms.setAttribute(player, EntityNms.Attribute.KNOCKBACK_RESISTANCE, selectedKit.getKnockbackResistance());
        nms.setAttribute(player, EntityNms.Attribute.ATTACK_DAMAGE, 2.0D);

        /* Remove Attack Delay */
        nms.setAttribute(player, EntityNms.Attribute.ATTACK_SPEED, 16.0D);

        /* Give Kit */
        selectedKit.getKit().setItems(this);

        /* Update Last Selected Kit */
        KitPvPData data = getData();
        data.setLastSelectedId(selectedKit.getHandler().getId());
        data.setLastSelectedLevel(selectedKit.getLevel());

//        updateNpcs();

        new Title(selectedKit.getHandler().getDisplayName(), "§a§lLevel " + selectedKit.getLevel(), 20, 40, 20).send(this);

        /* Passives on select */
        ItemStackNms itemStackNms = orbitMines.getNms().customItem();
        for (ItemStack item : player.getInventory().getContents()) {
            if (item == null)
                continue;

            Map<Passive, Integer> passives = Passive.from(itemStackNms, item, Passive.Interaction.ON_SELECT);

            /* No Passives on this item */
            if (passives == null)
                continue;

            for (Passive passive : passives.keySet()) {
                passive.getHandler().trigger(new DummyEvent(kitPvP, this), passives.get(passive));
            }
        }
    }

    public int getKillStreak() {
        return killStreak;
    }

    public void teleportToMap() {
        KitPvPMap.CURRENT.teleport(this);

        /* Spawn Protection */
        if (player.getGameMode() != GameMode.SPECTATOR) {
            spawnProtection = true;

            new BukkitRunnable() {
                @Override
                public void run() {
                    spawnProtection = false;
                }
            }.runTaskLater(orbitMines, 40);
        }

        /* Teleport sound */
        playSound(Sound.ENTITY_ENDERMAN_TELEPORT);
    }

    /* Spectator */
    public boolean isSpectator() {
        return spectator;
    }

    public void setSpectator(boolean spectator) {
        this.spectator = spectator;

        clearFullInventory();
        clearPotionEffects();

        if (spectator) {
            kitPvP.getSpectatorKit(this).setItems(this);
            player.getInventory().setHeldItemSlot(4);

            teleportToMap();

            player.setGameMode(GameMode.CREATIVE);
            player.setAllowFlight(true);
            player.setFlying(true);
        } else {
            kitPvP.getLobbyKit(this).setItems(this);
            player.getInventory().setHeldItemSlot(4);

            player.teleport(kitPvP.getSpawnLocation(player));

            player.setGameMode(GameMode.SURVIVAL);
            player.setFlying(false);
            player.setAllowFlight(false);
        }

        playSound(Sound.ENTITY_ENDERMAN_TELEPORT);
    }

    public LevelData getLevelData() {
        return levelData;
    }

    /*
        Coins
     */

    public int getCoins() {
        return getData().getCoins();
    }

    public void addCoins(int amount, boolean notify) {
        if (notify) {
            double coinMultiplier = getCoinMultiplier();
            new ActionBar(this, () -> "§6+" + amount + " " + (amount == 1 ? "Coin" : "Coins" + (coinMultiplier == 1.0D ? "" : " §7(" + vipRank.getPrefixColor().getChatColor() + "x" + String.format("%.2f", coinMultiplier) + "§7" + (CoinBooster.ACTIVE == null ? "" : ", §ex" + String.format("%.2f", CoinBooster.ACTIVE.getType().getMultiplier()) + "§7") + ")")), 100).send();
        }

        getData().addCoins(amount);
    }

    public void removeCoins(int amount) {
        getData().removeCoins(amount);
    }

    /*
        Experience
     */

    public int getExperience() {
        return getData().getExperience();
    }

    public void addExperience(int amount, boolean notify) {
        if (levelData.getLevel() == LevelData.maxLevel)
            return;

        if (notify) {
            double experienceMultiplier = getXPMultiplier();
            new ActionBar(this, () -> "§e+" + amount + " XP" + (experienceMultiplier == 1.0D ? "" : " §7(" + vipRank.getPrefixColor().getChatColor() + "x" + String.format("%.2f", experienceMultiplier) + "§7)"), 100).send();
        }

        getData().addExperience(amount);

        levelData.update(true);
    }

    public void addCoinsExperience(int coins, int experience, boolean notify) {
        if (notify) {
            double coinMultiplier = getCoinMultiplier();
            double experienceMultiplier = getXPMultiplier();

            String message = "§6§l+" + coins + " " + (coins == 1 ? "Coin" : "Coins") + (coinMultiplier == 1.0D ? (CoinBooster.ACTIVE == null ? "" : " §7(§ex" + String.format("%.2f", CoinBooster.ACTIVE.getType().getMultiplier()) + "§7)") : " §7(" + vipRank.getPrefixColor().getChatColor() + "x" + String.format("%.2f", coinMultiplier) + "§7" + (CoinBooster.ACTIVE == null ? "" : ", §ex" + String.format("%.2f", CoinBooster.ACTIVE.getType().getMultiplier()) + "§7") + ")") + "§7, " + "§e§l+" + experience + " XP" + (experienceMultiplier == 1.0D ? "" : " §7(" + vipRank.getPrefixColor().getChatColor() + "x" + String.format("%.2f", experienceMultiplier) + "§7)");

            new ActionBar(this, () -> message, 100).send();

            new BukkitRunnable() {
                @Override
                public void run() {
                    sendMessage(message);
                }
            }.runTaskLater(orbitMines, 1);
        }

        addCoins(coins, false);
        addExperience(experience, false);
    }

    /*
        Kills
     */

    public int getKills() {
        return getData().getKills();
    }

    public void addKill() {
        getData().addKill(selectedKit.getHandler().getId());
    }

    /*
        Deaths
     */

    public int getDeaths() {
        return getData().getDeaths();
    }

    public void addDeath() {
        getData().addDeath(selectedKit.getHandler().getId());
    }

    /*
        BestStreak
     */

    public int getBestStreak() {
        return getData().getBestStreak();
    }

    private void setBestStreak(int bestStreak) {
        getData().setBestStreak(selectedKit.getHandler().getId(), bestStreak);
    }

    /*
        LastSelected
     */
    public KitPvPKit.Level getLastSelected() {
        return KitPvPKit.getKit(getData().getLastSelectedId()).getLevel(getData().getLastSelectedLevel());
    }

    /*
        KitData
     */

    public KitPvPData.KitData getKitData(KitPvPKit kit) {
        return getData().getKitData(kit.getId());
    }

    /* Personal Multiplier */
    public double getCoinMultiplier() {
        switch (vipRank) {

            case NONE:
            case IRON:
            case GOLD:
                return 1.0;
            case DIAMOND:
                return 1.2;
            case EMERALD:
                return 1.5;
        }
        throw new IllegalStateException();
    }

    public double getXPMultiplier() {
        switch (vipRank) {

            case NONE:
            case IRON:
                return 1.0;
            case GOLD:
                return 1.5;
            case DIAMOND:
                return 2.0;
            case EMERALD:
                return 2.5;
        }
        throw new IllegalStateException();
    }

    protected KitPvPData getData() {
        return (KitPvPData) getData(Data.Type.KITPVP);
    }

    public static KitPvPPlayer getPlayer(Player player) {
        return player == null ? null : players.getOrDefault(player, null);
    }

    public static KitPvPPlayer getPlayer(String name) {
        return getPlayer(Bukkit.getPlayer(name));
    }

    public static KitPvPPlayer getPlayer(UUID uuid) {
        return getPlayer(Bukkit.getPlayer(uuid));
    }

    public static Collection<KitPvPPlayer> getKitPvPPlayers() {
        return players.values();
    }
}
