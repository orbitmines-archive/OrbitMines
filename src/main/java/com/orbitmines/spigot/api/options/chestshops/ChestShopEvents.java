package com.orbitmines.spigot.api.options.chestshops;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.Color;
import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.utils.ItemUtils;
import com.orbitmines.spigot.api.utils.PlayerUtils;
import com.orbitmines.spigot.api.utils.WorldUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
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

        if (block == null || (block.getType() != Material.SIGN_POST && block.getType() != Material.WALL_SIGN))
            return;

        ChestShop shop = ChestShop.getChestShop(block);

        if (shop == null)
            return;

        OMPlayer omp = OMPlayer.getPlayer(event.getPlayer());

        if (shop.getOwner().toString().equals(omp.getUUID().toString())) {
            omp.sendMessage("Shop", Color.RED, "Je kan je eigen Chest Shop niet gebruiken!", "You cannot use your own Chest Shop!");
            return;
        }

        Chest chest = WorldUtils.getChestAtSign(block.getLocation());

        if (chest == null) {
            omp.sendMessage("Shop", Color.RED, "Er is geen chest verbonden aan deze shop!", "There is no chest connected to this shop!");
            return;
        }

        switch (event.getAction()) {
            case LEFT_CLICK_BLOCK: {
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
                                omp.sendMessage("Shop", Color.RED, "Je hebt nog " + ChestShop.handler.getCurrencyDisplay(needed) + " §7nodig!", "You need " + ChestShop.handler.getCurrencyDisplay(needed) + " more in order to buy this.");
                            }
                        } else {
                            omp.sendMessage("Shop", Color.RED, "Je kan hier niks meer kopen, het is §cuitverkocht§7!", "You can no longer buy here, it's §csold out§7!");
                        }
                        break;
                    }
                    case SELL: {
                        if (shop.canSell()) {
                            int money = ChestShop.handler.getMoney(shop.getOwner());

                            if (money >= shop.getPrice()) {
                                if (PlayerUtils.getAmount(omp.getPlayer(), shop.getMaterial(), shop.getDurability()) >= shop.getAmount()) {
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

        if (lines[0] == null || (!lines[0].equalsIgnoreCase("shop:buy") && !lines[0].equalsIgnoreCase("shop:sell")))
            return;

        OMPlayer omp = OMPlayer.getPlayer(event.getPlayer());
        Block block = event.getBlock();

        if (WorldUtils.getChestAtSign(block.getLocation()) == null) {
            block.breakNaturally();
            omp.sendMessage("Shop", Color.RED, "De sign moet naast een chest staan!", "The sign has to be placed next to a chest!");
            return;
        }

        ChestShop.Type type = ChestShop.Type.valueOf(lines[0].substring(5).toUpperCase());

        int materialdId, amount, price;
        short durability;
        Material material;

        if (lines[1] != null && !lines[1].equals("")) {
            String[] line1 = lines[1].replace(" ", "").split(":");

            if (line1.length == 2 && !line1[0].equals("") && !line1[1].equals("")) {
                try {
                    materialdId = Integer.parseInt(line1[0]);
                } catch(NumberFormatException ex) {
                    block.breakNaturally();
                    omp.sendMessage("Shop", Color.RED, "Ongeldige item id '" + line1[0] + "'.", "Invalid item id '" + line1[0] + "'.");
                    return;
                }

                try {
                    material = Material.getMaterial(materialdId);
                } catch (IllegalArgumentException ex) {
                    block.breakNaturally();
                    omp.sendMessage("Shop", Color.RED, "Er bestaat geen item met het id '" + materialdId + "'.", "The item with id '" + materialdId + "' does not exist.");
                    return;
                }

                try {
                    durability = Short.parseShort(line1[1]);
                } catch(NumberFormatException ex) {
                    block.breakNaturally();
                    omp.sendMessage("Shop", Color.RED, "Ongeldige item durability '" + line1[1] + "'.", "Invalid item durability '" + line1[1] + "'.");
                    return;
                }

                if (ItemUtils.getName(material, durability, false) == null) {
                    block.breakNaturally();
                    omp.sendMessage("Shop", Color.RED, "Er bestaat geen item met het id '" + materialdId + "' en durability '" + durability + "'.", "The item with id '" + materialdId + "' and durability '" + durability + "' does not exist.");
                    return;
                }
            } else {
                block.breakNaturally();
                omp.sendMessage("Shop", Color.RED, "Lijn 2 klopt niet! (<Item ID> : <Durability>)", "Line 2 isn't setup correctly! (<Item ID> : <Durability>)");
                return;
            }
        } else {
            block.breakNaturally();
            omp.sendMessage("Shop", Color.RED, "Lijn 2 klopt niet! (<Item ID> : <Durability>)", "Line 2 isn't setup correctly! (<Item ID> : <Durability>)");
            return;
        }

        if (lines[2] != null && !lines[2].equals("")) {
            String[] line2 = lines[2].replace(" ", "").split(":");

            if (line2.length == 2 && !line2[0].equals("") && !line2[1].equals("")) {
                try {
                    amount = Integer.parseInt(line2[0]);
                } catch(NumberFormatException ex) {
                    block.breakNaturally();
                    omp.sendMessage("Shop", Color.RED, "Ongeldige item hoeveelheid '" + line2[0] + "'.", "Invalid item amount '" + line2[0] + "'.");
                    return;
                }

                try {
                    price = Integer.parseInt(line2[1]);
                } catch(NumberFormatException ex) {
                    block.breakNaturally();
                    omp.sendMessage("Shop", Color.RED, "Ongeldige prijs '" + line2[1] + "'.", "Invalid price '" + line2[1] + "'.");
                    return;
                }
            } else {
                block.breakNaturally();
                omp.sendMessage("Shop", Color.RED, "Lijn 3 klopt niet! (<Prijs> : <Hoeveelheid>)", "Line 3 isn't setup correctly! (<Price> : <Amount>)");
                return;
            }
        } else {
            block.breakNaturally();
            omp.sendMessage("Shop", Color.RED, "Lijn 3 klopt niet! (<Prijs> : <Hoeveelheid>)", "Line 3 isn't setup correctly! (<Price> : <Amount>)");
            return;
        }

        if (price > ChestShop.MAX_PRICE) {
            block.breakNaturally();
            omp.sendMessage("Shop", Color.RED, "De prijs mag maximaal " + ChestShop.handler.getCurrencyDisplay(ChestShop.MAX_PRICE) + "§7 zijn.", "De price cannot be higher than " + ChestShop.handler.getCurrencyDisplay(ChestShop.MAX_PRICE) + "§7.");
            return;
        }

        if (amount > ChestShop.MAX_AMOUNT) {
            block.breakNaturally();
            omp.sendMessage("Shop", Color.RED, "Je kan niet meer dan " + ChestShop.MAX_AMOUNT + " items per keer verkopen.", "You can't sell more than " + ChestShop.MAX_AMOUNT + " items at once.");
            return;
        }

        ChestShop shop = new ChestShop(omp.getUUID(), block.getLocation(), materialdId, durability, type, amount, price);

        omp.sendMessage("Shop", Color.LIME, "Je hebt een nieuwe Chest Shop gemaakt!", "You have successfully created a new Chest Shop!");

        new BukkitRunnable() {
            @Override
            public void run() {
                shop.update();
            }
        }.runTaskLater(orbitMines, 1);
    }
}
