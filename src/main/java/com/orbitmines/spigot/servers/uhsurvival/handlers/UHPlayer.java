package com.orbitmines.spigot.servers.uhsurvival.handlers;

import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.servers.uhsurvival.UHSurvival;
import com.orbitmines.spigot.servers.uhsurvival.handlers.map.mapsection.MapSection;
import com.orbitmines.spigot.servers.uhsurvival.handlers.mob.Mob;
import com.orbitmines.spigot.servers.uhsurvival.handlers.profile.PlayerProfile;
import com.orbitmines.spigot.servers.uhsurvival.handlers.tool.Tool;
import com.orbitmines.spigot.servers.uhsurvival.handlers.tool.ToolInventory;
import com.orbitmines.spigot.servers.uhsurvival.utils.enums.Action;
import com.orbitmines.spigot.servers.uhsurvival.utils.enums.Enchantment;
import com.orbitmines.spigot.servers.uhsurvival.utils.enums.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

/**
 * Created by Robin on 2/27/2018.
 */
public class UHPlayer extends OMPlayer {

    private static HashMap<UUID, UHPlayer> players = new HashMap<>();

    private UHSurvival uhSurvival;

    private World world;
    private MapSection section;

    private ToolInventory inventory;

    private PlayerProfile playerProfile;

    public UHPlayer(UHSurvival uhSurvival, Player player) {
        super(player);
        this.uhSurvival = uhSurvival;
        this.playerProfile = new PlayerProfile(this.getUUID());
        if (playerProfile.isBanned()) {
            player.kickPlayer("//TODO: KICK PLAYER MESSAGE!");
        }
    }

    /* STATIC METHODS */
    public static HashMap<UUID, UHPlayer> getUHPlayers() {
        return players;
    }

    public static UHPlayer getUHPlayer(UUID id) {
        return players.get(id);
    }

    public static UHPlayer getUHPlayer(Player p) {
        return getUHPlayer(p.getUniqueId());
    }

    @Override
    protected void onLogin() {
        players.put(getUUID(), this);
        this.world = World.getWorldByEnvironment(getWorld().getEnvironment());
        if (world != null && world.getMap() != null) {
            this.section = world.getMap().getMapSection(getLocation());
        }
        this.inventory = new ToolInventory(this.getInventory());
    }

    @Override
    protected void onLogout() {
        players.remove(getUUID(), this);
        if (section != null) {
            section.removePlayer(this.getUUID());
        }
        playerProfile.update(PlayerProfile.SaveType.ALL);
    }

    @Override
    public void onVote(int votes) {

    }

    @Override
    public boolean canReceiveVelocity() {
        return false;
    }

    /* PROFILE METHODS */
    public PlayerProfile getProfile() {
        return playerProfile;
    }

    /* TOOL METHODS */
    public ToolInventory getUHInventory() {
        return inventory;
    }

    /* MAP METHODS */
    public MapSection getSection() {
        return section;
    }

    public World getUHWorld() {
        return world;
    }

    /*  UH-PLAYERS METHODS  */
    public void attack(Entity entity, EntityDamageByEntityEvent event) {
        Tool tool = inventory.getMainHand();
        if (entity instanceof Player) {
            UHPlayer defender = getUHPlayer(entity.getUniqueId());
            if (this.section.canPvP() && defender.getSection().canPvP()) {
                event.setCancelled(defender.protect(this.getPlayer(), event));
                if (!event.isCancelled()) {
                    uhSurvival.getEnchantmentManager().output(tool.getEnchantments(), Action.HIT, event, false);
                }
            } else {
                event.setCancelled(true);
            }
        } else {
            Mob mob = world.getMap().getMapSection(entity.getLocation()).getMob(entity);
            if (mob != null) {
                mob.protect(uhSurvival, event);
                if (!event.isCancelled()) {
                    tool.addExp(mob.getType().getItemExp());
                    uhSurvival.getEnchantmentManager().output(tool.getEnchantments(), Action.HIT, event, false);
                }
            }
        }
    }

    public boolean protect(Entity entity, Event event) {
        Tool[] armor = inventory.getArmor();
        int addedExp = 0;
        if (entity instanceof Player) {
            addedExp = 120;
        } else {
            Mob mob = world.getMap().getMapSection(entity.getLocation()).getMob(entity);
            if (mob != null) {
                addedExp = mob.getType().getArmorExp();
            }
        }
        HashMap<Enchantment, Integer> enchantment = new HashMap<>();
        for (Tool piece : armor) {
            if (piece != null) {
                piece.addExp(addedExp);
                for (Enchantment e : piece.getEnchantments().keySet()) {
                    if (!enchantment.containsKey(e)) {
                        enchantment.put(e, piece.getEnchantment(e));
                    } else {
                        int level = piece.getEnchantment(e) > enchantment.get(e) ? piece.getEnchantment(e) : enchantment.get(e);
                        enchantment.put(e, level);
                    }
                }
            }
        }
        uhSurvival.getEnchantmentManager().output(enchantment, Action.PROTECT, event, false);
        return false;
    }

    public void shoot(EntityShootBowEvent event) {
        Tool bow = null;
        ItemStack b = event.getBow();
        if (inventory.getMainHand().equals(b)) {
            bow = inventory.getMainHand();
        } else if (inventory.getOffHand().equals(b)) {
            bow = inventory.getOffHand();
        }
        if (bow != null) {
            uhSurvival.getEnchantmentManager().output(bow.getEnchantments(), Action.SHOOT, event, true);
        }
    }
}
