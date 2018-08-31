package com.orbitmines.spigot.servers.survival.cmds.vip;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.mojang.authlib.GameProfile;
import com.orbitmines.api.*;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.cmd.VipCommand;
import com.orbitmines.spigot.api.runnables.SpigotRunnable;
import com.orbitmines.spigot.servers.survival.handlers.SurvivalPlayer;
import net.minecraft.server.v1_13_R2.DimensionManager;
import net.minecraft.server.v1_13_R2.EntityPlayer;
import net.minecraft.server.v1_13_R2.MinecraftServer;
import net.minecraft.server.v1_13_R2.PlayerInteractManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class CommandEnderchest extends VipCommand {

    private String[] alias = { "/enderchest" };

    public CommandEnderchest() {
        super(Server.SURVIVAL, VipRank.DIAMOND);
    }

    @Override
    public String[] getAlias() {
        return alias;
    }

    @Override
    public String getHelp(OMPlayer omp) {
        return omp.isEligible(StaffRank.ADMIN) ? "(player)" : null;
    }

    @Override
    public void onDispatch(OMPlayer omp, String[] a) {

        if (a.length != 1 && omp.isEligible(StaffRank.ADMIN)) {
            CachedPlayer cachedPlayer = CachedPlayer.getPlayer(a[1]);

            if (cachedPlayer == null) {
                omp.sendMessage("Enderchest", Color.RED, "§7Kan die speler niet vinden.", "§7That player cannot be found.");
                return;
            }

            SurvivalPlayer omp2 = SurvivalPlayer.getPlayer(cachedPlayer.getUUID());
            Player target;

            if (omp2 != null) {
                target = omp2.getPlayer();
            } else {
                OfflinePlayer player = Bukkit.getOfflinePlayer(cachedPlayer.getUUID());
                MinecraftServer minecraftserver = MinecraftServer.getServer();
                GameProfile gameprofile = new GameProfile(player.getUniqueId(), player.getName());
                EntityPlayer entity = new EntityPlayer(minecraftserver, minecraftserver.getWorldServer(DimensionManager.OVERWORLD), gameprofile, new PlayerInteractManager(minecraftserver.getWorldServer(DimensionManager.OVERWORLD)));

                target = entity.getBukkitEntity();

                if (target != null) {
                    target.loadData();
                } else {
                    omp.sendMessage("Enderchest", Color.RED, "§7Kan die speler niet vinden.", "§7That player cannot be found.");
                    return;
                }
            }

            Inventory inventory = target.getEnderChest();

            omp.getPlayer().openInventory(inventory);

            new SpigotRunnable(SpigotRunnable.TimeUnit.TICK, 1) {
                @Override
                public void run() {
                    if (!omp.getPlayer().isOnline() || !inventory.equals(omp.getPlayer().getOpenInventory().getTopInventory())) {
                        cancel();

                        if (omp.getPlayer().isOnline())
                            omp.getPlayer().closeInventory();

                        return;
                    }

                    if (omp2 != null) {
                        /* Player is Online, when he's offline, close inventory */
                        if (omp2.getPlayer().isOnline())
                            return;

                        cancel();
                        omp.getPlayer().closeInventory();
                    } else {
                        /* Player is offline, when he's online, close inventory */
                        SurvivalPlayer omp2 = SurvivalPlayer.getPlayer(cachedPlayer.getUUID());

                        if (omp2 == null) {
                            target.saveData();
                            return;
                        }

                        cancel();
                        omp.getPlayer().closeInventory();
                    }
                }
            };
        } else {
            omp.getPlayer().openInventory(omp.getPlayer().getEnderChest());
        }
    }
}
