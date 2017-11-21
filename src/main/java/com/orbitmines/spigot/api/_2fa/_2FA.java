package com.orbitmines.spigot.api._2fa;

import com.google.zxing.WriterException;
import com.madblock.api.database.Column;
import com.madblock.api.database.Database;
import com.madblock.api.database.Table;
import com.madblock.api.database.Where;
import com.madblock.api.database.tables.Table2FA;
import com.madblock.api.utils.Message;
import com.madblock.api.utils.UUIDUtils;
import com.madblock.spigot.MadBlock;
import com.madblock.spigot.api.Freezer;
import com.madblock.spigot.api.handlers.OMPlayer;
import com.madblock.spigot.api.handlers.chat.ComponentMessage;
import com.madblock.spigot.api.handlers.itembuilders.ItemBuilder;
import com.madblock.spigot.api.runnables.SpigotRunnable;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/*
* MadBlock, LLC CONFIDENTIAL - @author Fadi Shawki - 2017
* __________________
*
*  2017 MadBlock, LLC 
*  All Rights Reserved.
*
* NOTICE:  All information contained herein is, and remains
* the property of MadBlock, LLC and its suppliers,
* if any.  The intellectual and technical concepts contained
* herein are proprietary to MadBlock, LLC
* and its suppliers and may be covered by U.S. and Foreign Patents,
* patents in process, and are protected by trade secret or copyright law.
* Dissemination of this information or reproduction of this material
* is strictly forbidden unless prior written permission is obtained
* from MadBlock, LLC.
*/
public class _2FA {

    private final GoogleAuthenticator GOOGLE_AUTH = new GoogleAuthenticator();

    private MadBlock madBlock;

    private Map<OMPlayer, String> tempKeys;
    private Map<OMPlayer, ItemStack[]> contents;
    private Map<OMPlayer, ItemStack[]> armorContents;

    public _2FA() {
        madBlock = MadBlock.getInstance();
        this.tempKeys = new HashMap<>();
        this.contents = new HashMap<>();
        this.armorContents = new HashMap<>();
    }

    public void initiateLogin(OMPlayer omp) {
        mbp.setLoggedIn(false);
        mbp.freeze(Freezer.MOVE_AND_JUMP);

        contents.put(mbp, mbp.getPlayer().getInventory().getContents());
        armorContents.put(mbp, mbp.getPlayer().getInventory().getArmorContents());

        mbp.clearFullInventory();

        if (!Database.get().contains(Table._2FA, new Column[] { Table2FA.UUID }, new Where(Table2FA.UUID, mbp.getUUID().toString())))
            processNewLogin(mbp);

        mbp.sendMessage(Message.PREFIX_2FA, Message.ENTER_2FA);

        new BukkitRunnable() {
            @Override
            public void run() {
                tempKeys.remove(mbp);
                contents.remove(mbp);
                armorContents.remove(mbp);

                if (mbp.isLoggedIn())
                    return;

                mbp.kick("§c§lMad§9§lBlock §c§l2FA§r\n§7Timeout\n\n§7IGN: " + mbp.getName() + "\n§7UUID: " + mbp.getUUID().toString());
            }
        }.runTaskLater(madBlock, SpigotRunnable.TimeUnit.MINUTE.getTicks() * 3);
    }

    public Result login(OMPlayer omp, String code) {
        Integer password;

        try {
            password = Integer.parseInt(code.replace(" ", ""));
        } catch (NumberFormatException ex) {
            return Result.INVALID_CODE;
        }

        String secret;

        if (tempKeys.containsKey(mbp)) {
            secret = tempKeys.get(mbp);
        } else {
            secret = getSecret(mbp.getUUID());
        }

        boolean authorized = GOOGLE_AUTH.authorize(secret, password);

        if (!authorized) {
            return Result.INVALID_CODE;
        } else if (tempKeys.containsKey(mbp)) {
            mbp.sendMessage(Message.PREFIX_2FA, "Successfully setup your 2FA!");
            mbp.sendMessage(Message.PREFIX_2FA, "You will be asked for your 2FA code every 24 hours.");

            tempKeys.remove(mbp);

            Database.get().insert(Table._2FA, Table._2FA.values(mbp.getUUID().toString(), secret));
        }

        mbp.setLoggedIn(true);
        mbp.clearFreeze();

        mbp.getPlayer().getInventory().setContents(contents.get(mbp));
        mbp.getPlayer().getInventory().setArmorContents(armorContents.get(mbp));

        return Result.SUCCESSFUL;
    }

    public void processNewLogin(OMPlayer omp) {
        String secret = GOOGLE_AUTH.createCredentials().getKey();

        tempKeys.put(mbp, secret);

        mbp.sendMessage(Message.PREFIX_2FA, "§r§7Initiating new 2FA. Use the QR code on the map.");

        QRMapRenderer qrMap;
        try {
            qrMap = new QRMapRenderer(this, mbp, secret);
        } catch (WriterException e) {
            e.printStackTrace();
            qrMap = null;
        }

        if (qrMap == null) {
            ComponentMessage cM = new ComponentMessage();
            cM.addPart(Message.FORMAT(Message.PREFIX_2FA, "§r§7n error occurred, please "));
            cM.addPart("§6click here", ClickEvent.Action.OPEN_URL, "https://www.google.com/chart?chs=250x250&cht=qr&chl=" + otpAuth(mbp, secret), HoverEvent.Action.SHOW_TEXT, "§7Open QR Code in browser.");
            cM.addPart("§7.");

            cM.send(mbp);
        } else {
            MapView mapView = Bukkit.createMap(mbp.getWorld());

            ItemStack item = new ItemBuilder(Material.MAP, 1, mapView.getId(), "§c§l2FA").build();

            mbp.getPlayer().getInventory().setItem(0, item);
            mbp.getPlayer().getInventory().setHeldItemSlot(0);

            for (MapRenderer mapRenderer : mapView.getRenderers()) {
                mapView.removeRenderer(mapRenderer);
            }
            mapView.addRenderer(qrMap);

            mbp.getPlayer().sendMap(mapView);
        }
    }

    public String otpAuth(OMPlayer omp, String secret) {
        return "otpauth://totp/" + "MadBlock_2FA_IGN_" + mbp.getName() + "_UUID_" + UUIDUtils.trim(mbp.getUUID()) + "@" + MadBlock.DOMAIN + "?secret=" + secret;
    }

    private String getSecret(UUID uuid) {
        return Database.get().getString(Table._2FA, Table2FA.SECRET, new Where(Table2FA.UUID, uuid.toString()));
    }

    public enum Result {

        SUCCESSFUL,
        INVALID_CODE;

    }
}
