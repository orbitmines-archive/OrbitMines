package com.orbitmines.spigot.api.options.chestshops;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.database.Column;
import com.orbitmines.api.database.Database;
import com.orbitmines.api.database.Table;
import com.orbitmines.api.database.Where;
import com.orbitmines.api.database.tables.TableChestShops;
import com.orbitmines.api.utils.NumberUtils;
import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.itembuilders.ItemBuilder;
import com.orbitmines.spigot.api.handlers.itemhandlers.ItemHoverBlockTarget;
import com.orbitmines.spigot.api.handlers.scoreboard.DefaultScoreboard;
import com.orbitmines.spigot.api.handlers.scoreboard.ScoreboardSet;
import com.orbitmines.spigot.api.options.ApiOption;
import com.orbitmines.spigot.api.utils.Serializer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public abstract class ChestShopHandler implements ApiOption {

    @Override
    public void setup(OrbitMines orbitMines) {
        ChestShop.handler = this;

        /* Register Events */
        orbitMines.getServer().getPluginManager().registerEvents(new ChestShopEvents(orbitMines), orbitMines);

        /* Load Shops */
        for (Map<Column, String> entry : Database.get().getEntries(Table.CHEST_SHOPS, new Where(TableChestShops.SERVER, orbitMines.getServerHandler().getServer().toString()))) {
            Long id = Long.parseLong(entry.get(TableChestShops.ID));
            UUID owner = UUID.fromString(entry.get(TableChestShops.OWNER));
            Location location = Serializer.parseLocation(entry.get(TableChestShops.LOCATION));
            int materialId = Integer.parseInt(entry.get(TableChestShops.MATERIAL_ID));
            short durability = Short.parseShort(entry.get(TableChestShops.DURABILITY));
            ChestShop.Type type = ChestShop.Type.valueOf(entry.get(TableChestShops.TYPE));
            int amount = Integer.parseInt(entry.get(TableChestShops.AMOUNT));
            int price = Integer.parseInt(entry.get(TableChestShops.PRICE));

            new ChestShop(id, owner, location, materialId, durability, type, amount, price);
        }

        new ItemHoverBlockTarget(new ItemBuilder(Material.SIGN), true, Material.CHEST, Material.SIGN_POST, Material.WALL_SIGN) {

            @Override
            public void onTargetEnter(OMPlayer omp) {
                omp.resetScoreboard();
                omp.setScoreboard(new ChestShopScoreboard(orbitMines, omp));
            }

            @Override
            public void onLeave(OMPlayer omp) {
                omp.resetScoreboard();
                omp.setScoreboard(getNewScoreboardInstance(orbitMines, omp));
            }
        };
    }

    public abstract int getMoney(UUID uuid);

    public abstract void addMoney(UUID uuid, int count);

    public abstract void removeMoney(UUID uuid, int count);

    public abstract String getCurrencyDisplay(int count);

    public abstract char getCurrencySymbol();

    public abstract String getScoreboardCurrencyName();

    public abstract ScoreboardSet getNewScoreboardInstance(OrbitMines orbitMines, OMPlayer omp);

    public abstract List<World> getWorlds();

    public static class ChestShopScoreboard extends DefaultScoreboard {

        public ChestShopScoreboard(OrbitMines orbitMines, OMPlayer omp) {
            super(omp,
                    () -> orbitMines.getScoreboardAnimation().get(),
                    () -> "§f§m------------------------",
                    () -> ChestShop.handler.getScoreboardCurrencyName(),
                    () -> " " + NumberUtils.locale(ChestShop.handler.getMoney(omp.getUUID())),
                    () -> "",
                    () -> orbitMines.getServerHandler().getServer().getColor().getChatColor() + "§lSHOP SETUP",
                    () -> "§7Shop:Buy §8" + omp.lang("OF", "OR") + " §7Shop:Sell",
                    () -> "§7<Item ID> : <Durability>",
                    () -> omp.lang("§7<Hoeveelheid> : <Prijs>", "§7<Amount> : <Price>"),
                    () -> " ",
                    () -> orbitMines.getServerHandler().getServer().getColor().getChatColor() + "§lSHOP " + omp.lang("VOORBEELD", "EXAMPLE"),
                    () -> "§7Shop:Sell",
                    () -> "§74 : 0",
                    () -> "§764 : 128",
                    () -> "  "

            );
        }
    }
}
