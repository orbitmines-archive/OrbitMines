package com.orbitmines.spigot.api.cmds.moderator;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.Color;
import com.orbitmines.api.StaffRank;
import com.orbitmines.spigot.api.handlers.GUI;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.cmd.StaffCommand;
import com.orbitmines.spigot.api.runnables.SpigotRunnable;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

public class CommandInvSee extends StaffCommand {

    private String[] alias = { "/invsee" };

    public CommandInvSee() {
        super(StaffRank.MODERATOR);
    }

    @Override
    public String[] getAlias() {
        return alias;
    }

    @Override
    public String getHelp(OMPlayer omp) {
        return "<player>";
    }

    @Override
    public void onDispatch(OMPlayer omp, String[] a) {
        if (a.length == 2) {
            Player p2 = Bukkit.getPlayer(a[1]);

            if (p2 != null) {
                InventoryViewer view = new InventoryViewer(p2);
                view.open(omp);

                new SpigotRunnable(SpigotRunnable.TimeUnit.SECOND, 1) {
                    @Override
                    public void run() {
                        if (!p2.isOnline() || !omp.getPlayer().isOnline() || omp.getPlayer().getOpenInventory().getTopInventory() == null || omp.getPlayer().getOpenInventory().getTopInventory().getName() == null || !omp.getPlayer().getOpenInventory().getTopInventory().getName().equals(view.getInventory().getName())) {
                            cancel();

                            if (omp.getPlayer().isOnline())
                                omp.getPlayer().closeInventory();

                            return;
                        }

                        view.update();
                    }
                };
            } else {
                omp.sendMessage("InvSee", Color.RED, "§7Player §6" + a[1] + " §7is niet online!", "§7Player §6" + a[1] + " §7isn't online!");
            }
        } else {
            getHelpMessage(omp).send(omp);
        }
    }

    private class InventoryViewer extends GUI {

        private Player viewing;

        public InventoryViewer(Player viewing) {
            this.viewing = viewing;

            newInventory(45, "§0§l" + viewing.getName() + "'s Inventory");
        }

        @Override
        protected boolean onOpen(OMPlayer omp) {
            update();

            return true;
        }

        public void update() {
            PlayerInventory inv = viewing.getInventory();

            for(int i = 0; i < 27; i++){
                add(i, new EmptyItemInstance(inv.getItem(i + 9)));
            }
            for(int i = 27; i < 36; i++){
                add(i, new EmptyItemInstance(inv.getItem(i - 27)));
            }
            add(36, new EmptyItemInstance(inv.getHelmet()));
            add(37, new EmptyItemInstance(inv.getChestplate()));
            add(38, new EmptyItemInstance(inv.getLeggings()));
            add(39, new EmptyItemInstance(inv.getBoots()));

            add(41, new EmptyItemInstance(inv.getItemInOffHand()));
        }
    }
}
