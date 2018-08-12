package com.orbitmines.spigot.servers.survival.events;

import com.orbitmines.spigot.api.Mob;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.chat.ActionBar;
import com.orbitmines.spigot.api.handlers.itembuilders.ItemBuilder;
import com.orbitmines.spigot.api.nms.world.WorldNms;
import com.orbitmines.spigot.api.utils.ItemUtils;
import com.orbitmines.spigot.servers.survival.Survival;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class SpawnerEvents implements Listener {

    private WorldNms nms;

    public SpawnerEvents(WorldNms nms) {
        this.nms = nms;
    }

    /* Stop players from changing spawner type */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInteract(PlayerInteractEvent event) {
        if (event.isCancelled() || event.getClickedBlock().getType() != Material.SPAWNER)
            return;

        Player player = event.getPlayer();
        ItemStack mainHand = player.getInventory().getItemInMainHand();
        ItemStack offHand = player.getInventory().getItemInOffHand();

        if (mainHand != null && ItemUtils.EGGS.contains(mainHand.getType()) || offHand != null && ItemUtils.EGGS.contains(offHand.getType())) {
            event.setCancelled(true);
            OMPlayer.getPlayer(player).updateInventory();
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBreak(BlockBreakEvent event) {
        if (event.isCancelled())
            return;

        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (event.getBlock().getType() != Material.SPAWNER) {
            if (Survival.SPAWNER_MINER.equals(item)) {
                OMPlayer omp = OMPlayer.getPlayer(player);

                new ActionBar(omp, () -> omp.lang("§c§lJe kan alleen maar §5§lSpawners§c§l minen met deze Pickaxe.", "§c§lYou can only mine §5§lSpawners§c§l with this Pickaxe"), 100).send();
                event.setCancelled(true);
                omp.updateInventory();
            }

            return;
        }

        if (!Survival.SPAWNER_MINER.equals(item))
            return;

        Mob mob = nms.getSpawner(event.getBlock().getLocation());

        event.setCancelled(true);
        event.getBlock().setType(Material.AIR);
        event.setExpToDrop(0);

        player.getWorld().dropItemNaturally(event.getBlock().getLocation(), getSpawner(mob).build());

        player.getInventory().setItemInMainHand(null);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlace(BlockPlaceEvent event) {
        if (event.isCancelled() || event.getBlock().getType() != Material.SPAWNER)
            return;

        ItemStack item = event.getItemInHand();
        Mob mob = getMob(item);

        nms.setSpawner(event.getBlockPlaced().getLocation(), mob);
    }

    private ItemBuilder getSpawner(Mob mob) {
        return new ItemBuilder(Material.SPAWNER, 1, "§d§l" + mob.getName() + " Spawner");
    }

    private Mob getMob(ItemStack itemStack) {
        if (itemStack == null || itemStack.getItemMeta() == null || itemStack.getItemMeta().getDisplayName() == null)
            return Mob.PIG;

        String parts = itemStack.getItemMeta().getDisplayName().replaceAll(" Spawner", "");
        if (parts.length() < 5)
            return Mob.PIG;

        Mob mob = Mob.from(parts.substring(4));

        return mob != null ? mob : Mob.PIG;
    }
}
