package com.orbitmines.spigot.api.options.chestshops;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.Color;
import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.utils.PlayerUtils;
import com.orbitmines.spigot.api.utils.WorldUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class ChestShopEvents implements Listener {

    private final OrbitMines orbitMines;

    public ChestShopEvents(OrbitMines orbitMines) {
        this.orbitMines = orbitMines;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();

        if (block == null || (block.getType() != Material.SIGN && block.getType() != Material.WALL_SIGN))
            return;

        ChestShop shop = ChestShop.getChestShop(block);

        if (shop == null)
            return;

        OMPlayer omp = OMPlayer.getPlayer(event.getPlayer());

        if (shop.getOwner() != null && shop.getOwner().toString().equals(omp.getUUID().toString())) {
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK)
                new ChestShopEditorGUI(shop).open(omp);

            /* Else let them destroy it */

            return;
        }

        Chest chest = WorldUtils.getChestAtSign(block.getLocation());

        if (chest == null) {
            omp.sendMessage("Shop", Color.RED, "Er is geen chest verbonden aan deze shop!", "There is no chest connected to this shop!");
            return;
        }

        switch (event.getAction()) {
            case LEFT_CLICK_BLOCK: {
                if (omp.getItemInMainHand() == null)
                    new ChestShopViewerGUI(chest).open(omp);
                break;
            }
            case RIGHT_CLICK_BLOCK: {
                switch (shop.getType()) {
                    case BUY: {
                        if (shop.canBuy()) {
                            int money = ChestShop.handler.getMoney(omp.getUUID());

                            if (money >= shop.getPrice()) {
                                if (PlayerUtils.getEmptySlotCount(omp.getPlayer().getInventory()) >= PlayerUtils.getSlotsRequired(shop.getMaterial(), shop.getAmount())) {
                                    shop.buy(omp);
                                } else {
                                    omp.sendMessage("Shop", Color.RED, "Je inventory zit vol!", "Your inventory is full!");
                                }
                            } else {
                                int needed = shop.getPrice() - money;
                                omp.sendMessage("Shop", Color.RED, "Je hebt nog " + ChestShop.handler.getCurrencyDisplay(needed) + " §7nodig!", "You need " + ChestShop.handler.getCurrencyDisplay(needed) + " §7more in order to buy this.");
                            }
                        } else {
                            omp.sendMessage("Shop", Color.RED, "Je kan hier niks meer kopen, het is §cuitverkocht§7!", "You can no longer buy here, it's §csold out§7!");
                        }
                        break;
                    }
                    case SELL: {
                        if (shop.canSell()) {
                            if (shop.hasMoney()) {
                                if (PlayerUtils.getAmount(omp.getPlayer(), shop.getMaterial()) >= shop.getAmount()) {
                                    shop.sell(omp);
                                } else {
                                    omp.sendMessage("Shop", Color.RED, "Je hebt niet genoeg items!", "You don't have enough items!");
                                }
                            } else {
                                omp.sendMessage("Shop", Color.RED, "De eigenaar van die shop kan dat niet meer betalen!", "The owner of that shop can no longer afford that payment!");
                            }
                        } else {
                            omp.sendMessage("Shop", Color.RED, "Je kan hier niks meer verkopen, de chest zit §cvol§7!", "You can no longer sell here, the chest is §cfull§7!");
                        }
                        break;
                    }
                }
                break;
            }
        }
    }

    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        if (event.isCancelled())
            return;

        String[] lines = event.getLines();

        if (lines[0] == null || (!lines[0].equalsIgnoreCase("[shop]")))
            return;

        OMPlayer omp = OMPlayer.getPlayer(event.getPlayer());
        Block block = event.getBlock();

        if (WorldUtils.getChestAtSign(block.getLocation()) == null) {
            block.breakNaturally();
            omp.sendMessage("Shop", Color.RED, "De sign moet naast een chest staan!", "The sign has to be placed next to a chest!");
            return;
        }

        ChestShop shop = new ChestShop(omp.getUUID(), block.getLocation(), Material.COBBLESTONE, ChestShop.Type.BUY, 1, 1);

        omp.sendMessage("Shop", Color.LIME, "Je hebt een nieuwe Chest Shop gemaakt!", "You have successfully created a new Chest Shop!");

        new BukkitRunnable() {
            @Override
            public void run() {
                shop.update();
            }
        }.runTaskLater(orbitMines, 1);
    }
}
