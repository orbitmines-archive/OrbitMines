package com.orbitmines.spigot.gui;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.Color;
import com.orbitmines.api.utils.NumberUtils;
import com.orbitmines.spigot.api.handlers.GUI;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.itembuilders.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.InventoryClickEvent;

public class PrismSolarShopGUI extends GUI {

    public PrismSolarShopGUI() {
        newInventory(45, "§0§lePrism & Solar Shop");
    }

    @Override
    protected boolean onOpen(OMPlayer omp) {
        add(1, 3, new EmptyItemInstance(new ItemBuilder(Material.SUNFLOWER, 1, "§e§l" + NumberUtils.locale(omp.getSolars()) + " Solar" + (omp.getSolars() == 1 ? "" : "s")).build()));

        add(1, 5, new EmptyItemInstance(new ItemBuilder(Material.PRISMARINE_SHARD, 1, "§9§l" + NumberUtils.locale(omp.getPrisms()) + " Prism" + (omp.getPrisms() == 1 ? "" : "s")).build()));
        return true;
    }

    public abstract class ShopItem extends ItemInstance {

        private final Currency currency;
        private final int price;

        public ShopItem(OMPlayer omp, Currency currency, int price, ItemBuilder item, String... description) {
            super(null);

            this.currency = currency;
            this.price = price;

            item.addLore("§7" + omp.lang("Prijs", "Price") + ": " + currency.color.getChatColor() + "§l" + NumberUtils.locale(price) + " " + currency.name);
            item.addLore("");

            for (String desc : description) {
                item.addLore("§7 - " + desc);
            }

            item.addLore("");
            item.addLore(omp.lang("§aKlik hier om te kopen.", "§aClick here to buy."));

            this.itemStack = item.build();
        }

        public abstract void give(OMPlayer omp);

        /* Override to use */
        public boolean canReceive(OMPlayer omp) {
            return true;
        }

        /* Override to use */
        public void cantReceive(OMPlayer omp) {

        }

        @Override
        public void onClick(InventoryClickEvent event, OMPlayer omp) {
            if (currency.has(omp, price)) {
                if (canReceive(omp)) {
                    currency.remove(omp, price);
                    give(omp);
                    reopen(omp);
                }
            } else {
                omp.playSound(Sound.ENTITY_ENDERMAN_SCREAM);
                omp.sendMessage("Shop", Color.RED, "Je hebt niet genoeg " + currency.color.getChatColor() + "§l" + currency.name + "§7.", "You don't have enough " + currency.color.getChatColor() + "§l" + currency.name + "§7.");
            }
        }
    }

    public enum Currency {

        PRISMS(Color.BLUE, "Prisms") {
            @Override
            public int get(OMPlayer omp) {
                return omp.getPrisms();
            }

            @Override
            public void remove(OMPlayer omp, int price) {
                omp.removePrisms(price);
            }
        },
        SOLARS(Color.YELLOW, "Solars") {
            @Override
            public int get(OMPlayer omp) {
                return omp.getSolars();
            }

            @Override
            public void remove(OMPlayer omp, int price) {
                omp.removeSolars(price);
            }
        };

        private final Color color;
        private final String name;

        Currency(Color color, String name) {
            this.color = color;
            this.name = name;
        }

        public boolean has(OMPlayer omp, int price) {
            return get(omp) >= price;
        }

        public int get(OMPlayer omp) {
            throw new IllegalStateException();
        }

        public void remove(OMPlayer omp, int price) {
            throw new IllegalStateException();
        }
    }
}
