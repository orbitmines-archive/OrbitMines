package com.orbitmines.spigot.api.options.chestshops;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.*;
import com.orbitmines.api.database.Database;
import com.orbitmines.api.database.Set;
import com.orbitmines.api.database.Table;
import com.orbitmines.api.database.Where;
import com.orbitmines.api.database.tables.TableChestShops;
import com.orbitmines.api.database.tables.TableServerData;
import com.orbitmines.api.utils.NumberUtils;
import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.runnables.SpigotRunnable;
import com.orbitmines.spigot.api.handlers.timer.Timer;
import com.orbitmines.spigot.api.utils.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class ChestShop {

    public static ChestShopHandler handler;
    public static final int MAX_AMOUNT = 999;
    public static final int MAX_PRICE = 999999;

    private static final List<ChestShop> chestShops = new ArrayList<>();

    private final OrbitMines orbitMines;

    private final Long id;
    private final UUID owner;

    protected final Location location;
    private Material material;

    private Type type;
    private int amount;
    private int price;

    private String itemName;
    private int nameLength;
    private int index;
    private boolean scroll;
    private SpigotRunnable updateRunnable;

    public ChestShop(UUID owner, Location location, Material material, Type type, int amount, int price) {
        this(nextId(), owner, location, material, type, amount, price);

        Database.get().insert(Table.CHEST_SHOPS, orbitMines.getServerHandler().getServer().toString(), id + "", owner.toString(), Serializer.serialize(location), material.toString(), type.toString(), amount + "", price + "");
    }

    public ChestShop(Long id, UUID owner, Location location, Material material, Type type, int amount, int price) {
        chestShops.add(this);

        this.orbitMines = OrbitMines.getInstance();

        this.id = id;
        this.owner = owner;
        this.location = location;
        this.material = material;
        this.type = type;
        this.amount = amount;
        this.price = price;

        this.itemName = ItemUtils.getName(material);
        this.nameLength = itemName.length();
        this.index = 0;
        this.scroll = itemName.length() > 16;

        updateRunnable = new SpigotRunnable(SpigotRunnable.TimeUnit.TICK, 10) {
            @Override
            public void run() {
                if (!scroll || itemName.length() <= 16) {
                    update();
                    return;
                }

                int maxIndex = itemName.length() - 16;
                index++;

                if (index > maxIndex) {
                    index = maxIndex;
                    scroll = false;

                    new Timer(new Time(SpigotRunnable.TimeUnit.SECOND, 1)) {
                        @Override
                        public void onFinish() {
                            index = 0;
                        }
                    };

                    new Timer(new Time(SpigotRunnable.TimeUnit.SECOND, 4)) {
                        @Override
                        public void onFinish() {
                            scroll = true;
                        }
                    };
                }

                update();
            }
        };
    }

    public Long getId() {
        return id;
    }

    public UUID getOwner() {
        return owner;
    }

    public Location getLocation() {
        return location;
    }

    public Material getMaterial() {
        return material;
    }

    public void save(Material material, int amount, int price, Type type) {
        this.material = material;
        this.amount = amount;
        this.price = price;
        this.type = type;

        this.itemName = ItemUtils.getName(material);
        this.nameLength = itemName.length();
        this.index = 0;
        this.scroll = itemName.length() > 16;

        if (!chestShops.contains(this))
            return;

        Database.get().update(Table.CHEST_SHOPS, new Set[] {
                new Set(TableChestShops.MATERIAL, material.toString()),
                new Set(TableChestShops.AMOUNT, amount),
                new Set(TableChestShops.PRICE, price),
                new Set(TableChestShops.TYPE, type.toString()),
        }, new Where(TableChestShops.SERVER, orbitMines.getServerHandler().getServer().toString()), new Where(TableChestShops.ID, id));
    }

    public Type getType() {
        return type;
    }

    public int getAmount() {
        return amount;
    }

    public int getPrice() {
        return price;
    }

    public boolean canBuy() {
        return PlayerUtils.getAmount(WorldUtils.getChestAtSign(this.location).getInventory(), material) >= this.amount;
    }

    public boolean canSell() {
        return PlayerUtils.getEmptySlotCount(WorldUtils.getChestAtSign(this.location).getInventory()) >= PlayerUtils.getSlotsRequired(material, this.amount);
    }

    public boolean hasMoney() {
        return owner == null || handler.getMoney(this.owner) >= this.price;
    }

    public void buy(OMPlayer omp) {
        Chest chest = WorldUtils.getChestAtSign(this.location);

        List<ItemStack> bought = PlayerUtils.removeItems(chest.getInventory(), material, amount);
        omp.getPlayer().getInventory().addItem(bought.toArray(new ItemStack[bought.size()]));

        omp.updateInventory();

        handler.removeMoney(omp.getUUID(), this.price);

        if (owner != null)
            handler.addMoney(owner, this.price);

        String color = orbitMines.getServerHandler().getServer().getColor().getChatColor();

        CachedPlayer owner = this.owner != null ? CachedPlayer.getPlayer(this.owner) : null;

        String ownerName = owner != null ? (owner.getRankPrefixColor().getChatColor() + owner.getPlayerName()) : orbitMines.getServerHandler().getServer().getDisplayName();
        omp.sendMessage("Shop", Color.LIME, "Je hebt " + color + "§l" + itemName + " §7(" + color + "§l" + this.amount + "x§7) gekocht van " + ownerName + "§7 voor " + handler.getCurrencyDisplay(this.price) + "§7.", "You've bought " + color + "§l" + itemName + " §7(" + color + "§l" + this.amount + "x§7) from " + ownerName + "§7 for " + handler.getCurrencyDisplay(this.price) + "§7.");

        if (owner == null)
            return;

        OMPlayer omp2 = OMPlayer.getPlayer(this.owner);

        if (omp2 == null)
            return;

        omp2.sendMessage("Shop", Color.LIME, omp.getName() + " §7heeft " + color + "§l" + itemName + " §7(" + color + "§l" + this.amount + "x§7) van je gekocht voor " + handler.getCurrencyDisplay(this.price) + "§7.", ownerName + "§7 bought " + color + "§l" + itemName + " §7(" + color + "§l" + this.amount + "x§7) from you for " + handler.getCurrencyDisplay(this.price) + "§7.");
    }

    public void sell(OMPlayer omp) {
        if (owner == null)
            throw new IllegalStateException();

        Chest chest = WorldUtils.getChestAtSign(this.location);

        List<ItemStack> sold = PlayerUtils.removeItems(omp.getPlayer(), material, amount);
        chest.getInventory().addItem(sold.toArray(new ItemStack[sold.size()]));

        omp.updateInventory();

        handler.removeMoney(owner, this.price);
        handler.addMoney(omp.getUUID(), this.price);

        String color = orbitMines.getServerHandler().getServer().getColor().getChatColor();
        CachedPlayer owner = CachedPlayer.getPlayer(this.owner);
        String ownerName = owner.getRankPrefixColor().getChatColor() + owner.getPlayerName();
        omp.sendMessage("Shop", Color.LIME, "Je hebt " + color + "§l" + itemName + " §7(" + color + "§l" + this.amount + "x§7) verkocht aan " + ownerName + "§7 voor " + handler.getCurrencyDisplay(this.price) + "§7.", "You've sold " + color + "§l" + itemName + " §7(" + color + "§l" + this.amount + "x§7) to " + ownerName + "§7 for " + handler.getCurrencyDisplay(this.price) + "§7.");

        OMPlayer omp2 = OMPlayer.getPlayer(this.owner);

        if (omp2 == null)
            return;

        omp2.sendMessage("Shop", Color.LIME, omp.getName() + " §7heeft " + color + "§l" + itemName + " §7(" + color + "§l" + this.amount + "x§7) aan je verkocht voor " + handler.getCurrencyDisplay(this.price) + "§7.", ownerName + "§7 sold " + color + "§l" + itemName + " §7(" + color + "§l" + this.amount + "x§7) to you for " + handler.getCurrencyDisplay(this.price) + "§7.");
    }

    public void update() {
        Block block = this.location.getBlock();

        if (block.getType() != Material.SIGN && block.getType() != Material.WALL_SIGN) {
            delete();
            return;
        }

        List<Player> players = WorldUtils.getNearbyPlayers(location, 16);

        if (players.size() == 0)
            return;

        Map<Language, String[]> languages = new HashMap<>();

        for (Language language : Language.values()) {
            String[] lines = new String[4];
            lines[0] = type.getDisplayName(type.useable(this), language);
            lines[1] = nameLength <= 16 ? itemName : itemName.substring(index, index + 16);
            lines[2] = NumberUtils.locale(this.amount) + " : " + NumberUtils.locale(this.price) + handler.getCurrencySymbol();
            lines[3] = this.owner != null ? CachedPlayer.getPlayer(this.owner).getPlayerName() : orbitMines.getServerHandler().getServer().getDisplayName();

            languages.put(language, lines);
        }

        for (Player player : players) {
            player.sendSignChange(this.location, languages.get(OMPlayer.getPlayer(player).getLanguage()));
        }
    }

    public void delete() {
        chestShops.remove(this);

        Database.get().delete(Table.CHEST_SHOPS, new Where(TableChestShops.SERVER, orbitMines.getServerHandler().getServer().toString()), new Where(TableChestShops.ID, id));

        if (updateRunnable != null)
            updateRunnable.cancel();
    }

    private static Long nextId() {
        Server server = OrbitMines.getInstance().getServerHandler().getServer();

        if (!Database.get().contains(Table.SERVER_DATA, TableServerData.DATA, new Where(TableServerData.TYPE, "NEXT_SHOP_ID"), new Where(TableServerData.SERVER, server.toString())))
            Database.get().insert(Table.SERVER_DATA, server.toString(), "NEXT_SHOP_ID", "0");

        Long nextId = Database.get().getLong(Table.SERVER_DATA, TableServerData.DATA, new Where(TableServerData.TYPE, "NEXT_SHOP_ID"), new Where(TableServerData.SERVER, server.toString()));

        /* update next id */
        Database.get().update(Table.SERVER_DATA, new Set(TableServerData.DATA, nextId + 1), new Where(TableServerData.TYPE, "NEXT_SHOP_ID"), new Where(TableServerData.SERVER, server.toString()));

        return nextId;
    }

    public static List<ChestShop> getChestShops() {
        return chestShops;
    }

    public static ChestShop getChestShop(Block block) {
        for (ChestShop shop : chestShops) {
            if (LocationUtils.equals(shop.getLocation().getBlock(), block))
                return shop;
        }

        return null;
    }

    public enum Type {

        BUY(new Message("Koop", "Buy")) {
            @Override
            public boolean useable(ChestShop shop) {
                return shop.canBuy();
            }
        },
        SELL(new Message("Verkoop", "Sell")) {
            @Override
            public boolean useable(ChestShop shop) {
                return shop.canSell();
            }
        };

        private final Message name;

        Type(Message name) {
            this.name = name;
        }

        public Message getName() {
            return name;
        }

        public String getDisplayName(boolean useable, Language language) {
            return (useable ? Color.GREEN : Color.MAROON).getChatColor() + "§l" + name.lang(language);
        }

        public boolean useable(ChestShop shop) {
            throw new IllegalStateException();
        }

        public Type next() {
            Type[] values = Type.values();

            return ordinal() == values.length - 1 ? values[0] : values[ordinal() + 1];
        }
    }
}
