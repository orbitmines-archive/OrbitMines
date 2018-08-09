package com.orbitmines.spigot.api.options.chestshops;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.Color;
import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.api.handlers.GUI;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.itembuilders.ItemBuilder;
import com.orbitmines.spigot.api.nms.anvilgui.AnvilNms;
import com.orbitmines.spigot.api.utils.ItemUtils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class ChestShopEditorGUI extends GUI {

    private final OrbitMines orbitMines;

    private final ChestShop shop;

    private Material material;
    private int amount;
    private int price;
    private ChestShop.Type type;

    public ChestShopEditorGUI(ChestShop shop) {
        this.orbitMines = OrbitMines.getInstance();

        this.shop = shop;
        this.material = shop.getMaterial();
        this.amount = shop.getAmount();
        this.price = shop.getPrice();
        this.type = shop.getType();

        newInventory(27, "§0§lChest Shop Editor");
    }

    @Override
    protected boolean onOpen(OMPlayer omp) {
        String color = orbitMines.getServerHandler().getServer().getColor().getChatColor();

        add(1, 1, new ItemInstance(new ItemBuilder(material, 1, "§7§lItem: " + color + "§l" + ItemUtils.getName(material)).build()) {
            @Override
            public void onClick(InventoryClickEvent e, OMPlayer omp) {
                AnvilNms anvil = orbitMines.getNms().anvilGui(omp.getPlayer(), (event) -> {
                    String name = event.getName();

                    if (event.getSlot() != AnvilNms.AnvilSlot.OUTPUT || name.equals("")) {
                        event.setWillClose(false);
                        event.setWillDestroy(false);
                        return;
                    }

                    Material material;
                    try {
                        material = Material.valueOf(name.replace("minecraft:", "").replaceAll(" ", "_").toUpperCase());
                    } catch (IllegalArgumentException ex) {
                        event.setWillClose(false);
                        event.setWillDestroy(false);
                        event.getAnvilNms().setSlot(AnvilNms.AnvilSlot.OUTPUT, new ItemBuilder(ChestShopEditorGUI.this.material, 1, omp.lang("minecraft:", "minecraft:")).build());

                        omp.sendMessage("Shop", Color.RED, "Dat is een ongeldig item naam.", "That is an invalid item name.");

                        return;
                    }

                    event.setWillClose(true);
                    event.setWillDestroy(true);

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            ChestShopEditorGUI.this.material = material;
                            reopen(omp);
                        }
                    }.runTaskLater(orbitMines, 2);

                }, new AnvilNms.AnvilCloseEvent() {
                    @Override
                    public void onClose() {
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                reopen(omp);
                            }
                        }.runTaskLater(orbitMines, 1);
                    }
                });

                anvil.getItems().put(AnvilNms.AnvilSlot.INPUT_LEFT, new ItemBuilder(material, 1, omp.lang("minecraft:", "minecraft:")).build());

                anvil.open();
            }
        });

        add(1, 2, new ItemInstance(new ItemBuilder(Material.MAP, 1, "§7§l" + omp.lang("Hoeveelheid", "Amount") + ": " + color + "§l" + amount).build()) {
            @Override
            public void onClick(InventoryClickEvent e, OMPlayer omp) {
                AnvilNms anvil = orbitMines.getNms().anvilGui(omp.getPlayer(), (event) -> {
                    String name = event.getName();

                    if (event.getSlot() != AnvilNms.AnvilSlot.OUTPUT || name.equals("")) {
                        event.setWillClose(false);
                        event.setWillDestroy(false);
                        return;
                    }

                    int amount;
                    try {
                        amount = Integer.parseInt(name);
                    } catch (NumberFormatException ex) {
                        event.setWillClose(false);
                        event.setWillDestroy(false);
                        event.getAnvilNms().setSlot(AnvilNms.AnvilSlot.OUTPUT, new ItemBuilder(Material.MAP, 1, ChestShopEditorGUI.this.amount + "").build());

                        omp.sendMessage("Shop", Color.RED, "Dat is een ongeldige hoeveelheid.", "That is an invalid amount.");

                        return;
                    }

                    if (amount < 1 || amount > ChestShop.MAX_AMOUNT) {
                        event.setWillClose(false);
                        event.setWillDestroy(false);
                        event.getAnvilNms().setSlot(AnvilNms.AnvilSlot.OUTPUT, new ItemBuilder(Material.MAP, 1, amount + "").build());

                        omp.sendMessage("Shop", Color.RED, "De hoeveelheid moet tussen de " + color + "§l1§7 en " + color + "§l" + ChestShop.MAX_AMOUNT + " §7liggen.", "The amount has to be between " + color + "§l1 §7and " + color + "§l" + ChestShop.MAX_AMOUNT + "§7.");
                        return;
                    }

                    event.setWillClose(true);
                    event.setWillDestroy(true);

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            ChestShopEditorGUI.this.amount = amount;
                            reopen(omp);
                        }
                    }.runTaskLater(orbitMines, 2);

                }, new AnvilNms.AnvilCloseEvent() {
                    @Override
                    public void onClose() {
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                reopen(omp);
                            }
                        }.runTaskLater(orbitMines, 1);
                    }
                });

                anvil.getItems().put(AnvilNms.AnvilSlot.INPUT_LEFT, new ItemBuilder(Material.MAP, 1, amount + "").build());

                anvil.open();
            }
        });

        add(1, 3, new ItemInstance(ChestShop.handler.getCurrencyIcon().setDisplayName("§7§l" + omp.lang("Prijs", "Price") + ": " + ChestShop.handler.getCurrencyDisplay(price)).build()) {
            @Override
            public void onClick(InventoryClickEvent e, OMPlayer omp) {
                AnvilNms anvil = orbitMines.getNms().anvilGui(omp.getPlayer(), (event) -> {
                    String name = event.getName();

                    if (event.getSlot() != AnvilNms.AnvilSlot.OUTPUT || name.equals("")) {
                        event.setWillClose(false);
                        event.setWillDestroy(false);
                        return;
                    }

                    int price;
                    try {
                        price = Integer.parseInt(name);
                    } catch (NumberFormatException ex) {
                        event.setWillClose(false);
                        event.setWillDestroy(false);
                        event.getAnvilNms().setSlot(AnvilNms.AnvilSlot.OUTPUT, ChestShop.handler.getCurrencyIcon().setDisplayName(ChestShopEditorGUI.this.price + "").build());

                        omp.sendMessage("Shop", Color.RED, "Dat is een ongeldige prijs.", "That is an invalid price.");

                        return;
                    }

                    if (price < 1 || price > ChestShop.MAX_PRICE) {
                        event.setWillClose(false);
                        event.setWillDestroy(false);
                        event.getAnvilNms().setSlot(AnvilNms.AnvilSlot.OUTPUT, ChestShop.handler.getCurrencyIcon().setDisplayName(ChestShopEditorGUI.this.price + "").build());

                        omp.sendMessage("Shop", Color.RED, "De prijs moet tussen de " + color + "§l1 §7en " + color + "§l" + ChestShop.MAX_PRICE + " §7liggen.", "The price has to be between " + color + "§l1 §7and " + color + "§l" + ChestShop.MAX_PRICE + "§7.");

                        return;
                    }

                    event.setWillClose(true);
                    event.setWillDestroy(true);

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            ChestShopEditorGUI.this.price = price;
                            reopen(omp);
                        }
                    }.runTaskLater(orbitMines, 2);

                }, new AnvilNms.AnvilCloseEvent() {
                    @Override
                    public void onClose() {
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                reopen(omp);
                            }
                        }.runTaskLater(orbitMines, 1);
                    }
                });

                anvil.getItems().put(AnvilNms.AnvilSlot.INPUT_LEFT, ChestShop.handler.getCurrencyIcon().setDisplayName(price + "").build());

                anvil.open();
            }
        });

        add(1, 4, new ItemInstance(new ItemBuilder(type == ChestShop.Type.BUY ? Material.CHEST_MINECART : Material.HOPPER_MINECART, 1, "§7§lShop Type: " + color + "§l" + omp.lang(type.getName())).build()) {
            @Override
            public void onClick(InventoryClickEvent event, OMPlayer omp) {
                type = type.next();
                reopen(omp);
            }
        });

        add(1, 6, new ItemInstance(new ItemBuilder(Material.SIGN, 1, omp.lang("§a§lOpslaan", "§a§lSave")).build()) {
            @Override
            public void onClick(InventoryClickEvent event, OMPlayer omp) {
                shop.save(material, amount, price, type);
                omp.getPlayer().closeInventory();
                omp.playSound(Sound.ENTITY_ARROW_HIT_PLAYER);
            }
        });

        add(1, 7, new ItemInstance(new ItemBuilder(Material.BARRIER, 1, omp.lang("§c§lAnnuleren", "§c§lCancel")).build()) {
            @Override
            public void onClick(InventoryClickEvent event, OMPlayer omp) {
                omp.getPlayer().closeInventory();
            }
        });

        return true;
    }
}
