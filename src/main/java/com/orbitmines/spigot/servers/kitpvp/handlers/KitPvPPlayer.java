package com.orbitmines.spigot.servers.kitpvp.handlers;

import com.orbitmines.api.database.*;
import com.orbitmines.api.database.tables.kitpvp.TableKitPvPPlayers;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import org.bukkit.entity.Player;

import java.util.Map;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class KitPvPPlayer extends OMPlayer {

    private int coins;

    public KitPvPPlayer(Player player) {
        super(player);

        coins = 0;
    }

    @Override
    protected void onLogin() {
        if (!Database.get().contains(Table.KITPVP_PLAYERS, TableKitPvPPlayers.UUID, new Where(TableKitPvPPlayers.UUID, getUUID().toString()))) {
            Database.get().insert(Table.KITPVP_PLAYERS, Table.KITPVP_PLAYERS.values(getUUID().toString(), coins + ""));
        } else {
            Map<Column, String> entry = Database.get().getValues(Table.KITPVP_PLAYERS, new Column[]{
                    TableKitPvPPlayers.COINS,
            }, new Where(TableKitPvPPlayers.UUID, getUUID().toString()));

            coins = Integer.parseInt(entry.get(TableKitPvPPlayers.COINS));
        }
    }

    @Override
    protected void onLogout() {

    }

    @Override
    public boolean canReceiveVelocity() {
        return false;
    }

    public int getCoins() {
        return coins;
    }

    public void addCoins(int amount) {
        coins += amount;

        updateCoins();
    }

    public void removeCoins(int amount) {
        coins -= amount;

        updateCoins();
    }

    private void updateCoins() {
        Database.get().update(Table.KITPVP_PLAYERS, new Set(TableKitPvPPlayers.COINS, coins), new Where(TableKitPvPPlayers.UUID, getUUID().toString()));
    }
}
