package com.orbitmines.spigot.servers.kitpvp.handlers.gui;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.Color;
import com.orbitmines.api.Message;
import com.orbitmines.api.VipRank;
import com.orbitmines.api.utils.TimeUtils;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.gui.PrismSolarShopGUI;
import com.orbitmines.spigot.servers.kitpvp.handlers.CoinBooster;
import com.orbitmines.spigot.servers.kitpvp.handlers.KitPvPPlayer;

public class CoinBoosterGUI extends PrismSolarShopGUI {

    public CoinBoosterGUI() {
        newInventory(45, "§0§lCoin Boosters");
    }

    @Override
    protected boolean onOpen(OMPlayer player) {
        KitPvPPlayer omp = (KitPvPPlayer) player;

        if (CoinBooster.ACTIVE != null)
            return false;

        int slot = 2;
        for (CoinBooster.Type type : CoinBooster.Type.values()) {
            add(3, slot, new ShopItem(omp, Currency.SOLARS, type.getPrice(), type.getIcon().setDisplayName(type.getDisplayName()), "§6§lx" + String.format("%.2f", type.getMultiplier()) + " Coin Booster", "§b§l" + TimeUtils.fromTimeStamp(type.getDuration().getTicks() * 50, omp.getLanguage()), "§7§l" + omp.lang("Werkt op iedereen", "Applies to everyone")) {
                @Override
                public void give(OMPlayer player) {
                    KitPvPPlayer omp = (KitPvPPlayer) player;

                    omp.sendMessage("Shop", Color.BLUE, "Je hebt een " + type.getDisplayName() + " §7gekocht.", "You have purchased a" + (type.getVipRank() == VipRank.IRON || type.getVipRank() == VipRank.EMERALD ? "n" : "") + " " + type.getDisplayName() + "§7.");

                    CoinBooster booster = new CoinBooster(type, omp.getUUID());
                    booster.start();

                    omp.getPlayer().closeInventory();
                }

                @Override
                public boolean canReceive(OMPlayer omp) {
                    if (CoinBooster.ACTIVE != null) {
                        omp.sendMessage("Boosters", Color.RED, "Er is al een §6§lCoin Booster§7 actief.", "There is already an active §6§lCoin Booster§7.");
                        omp.getPlayer().closeInventory();
                        return false;
                    } else if (!omp.isEligible(type.getVipRank())) {
                        omp.sendMessage(Message.REQUIRE_RANK(type.getVipRank()));
                        return false;
                    }
                    return true;
                }
            });
            slot++;
        }

        return super.onOpen(omp);
    }
}
