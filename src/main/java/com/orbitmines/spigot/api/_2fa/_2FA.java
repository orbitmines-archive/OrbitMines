package com.orbitmines.spigot.api._2fa;

import com.google.zxing.WriterException;
import com.orbitmines.api.Color;
import com.orbitmines.api.Message;
import com.orbitmines.api.PluginMessage;
import com.orbitmines.api.database.Database;
import com.orbitmines.api.database.Table;
import com.orbitmines.api.database.Where;
import com.orbitmines.api.database.tables.Table2FA;
import com.orbitmines.api.utils.uuid.UUIDUtils;
import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.api.Freezer;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.chat.ComponentMessage;
import com.orbitmines.spigot.api.handlers.itembuilders.ItemBuilder;
import com.orbitmines.spigot.api.runnables.SpigotRunnable;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class _2FA {

    private final GoogleAuthenticator GOOGLE_AUTH = new GoogleAuthenticator();

    private OrbitMines orbitMines;

    private Map<OMPlayer, String> tempKeys;
    private Map<OMPlayer, ItemStack[]> contents;
    private Map<OMPlayer, ItemStack[]> armorContents;
    private Map<OMPlayer, BukkitTask> tasks;

    public _2FA() {
        orbitMines = OrbitMines.getInstance();
        this.tempKeys = new HashMap<>();
        this.contents = new HashMap<>();
        this.armorContents = new HashMap<>();
        this.tasks = new HashMap<>();
    }

    public void initiateLogin(OMPlayer omp) {
        orbitMines.getServerHandler().getMessageHandler().dataTransfer(PluginMessage.SET_LOGGING_IN, omp.getUUID().toString());

        omp.setLoggedIn(false);
        omp.freeze(Freezer.MOVE_AND_JUMP);

        contents.put(omp, omp.getPlayer().getInventory().getContents());
        armorContents.put(omp, omp.getPlayer().getInventory().getArmorContents());

        omp.clearFullInventory();

        if (!Database.get().contains(Table._2FA, Table2FA.UUID, new Where(Table2FA.UUID, omp.getUUID().toString())))
            processNewLogin(omp);

        omp.sendMessage(Message.ENTER_2FA);

        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                tempKeys.remove(omp);
                contents.remove(omp);
                armorContents.remove(omp);

                if (omp.getPlayer().isOnline() && !omp.isLoggedIn())
                    omp.kick("§8§lOrbit§7§lMines §c§l2FA§r\n§7Timeout\n\n§7IGN: §8" + omp.getName() + "\n§7UUID: §8" + omp.getUUID().toString());
            }
        }.runTaskLater(orbitMines, SpigotRunnable.TimeUnit.MINUTE.getTicks() * 3);
        tasks.put(omp, task);
    }

    public Result login(OMPlayer omp, String code) {
        Integer password;

        try {
            password = Integer.parseInt(code.replace(" ", ""));
        } catch (NumberFormatException ex) {
            return Result.INVALID_CODE;
        }

        String secret;

        if (tempKeys.containsKey(omp)) {
            secret = tempKeys.get(omp);
        } else {
            secret = getSecret(omp.getUUID());
        }

        boolean authorized = GOOGLE_AUTH.authorize(secret, password);

        if (!authorized) {
            return Result.INVALID_CODE;
        } else if (tempKeys.containsKey(omp)) {
            omp.sendMessage(Message.PREFIX_2FA, Color.LIME, "2FA is succesvol ingesteld.", "Successfully setup your 2FA!");
            omp.sendMessage(Message.PREFIX_2FA, Color.BLUE, "Je zal elke 24 uur gevraagd worden naar je 2FA code.", "You will be asked for your 2FA code every 24 hours.");

            tempKeys.remove(omp);

            Database.get().insert(Table._2FA, Table._2FA.values(omp.getUUID().toString(), secret));
        }

        omp.setLoggedIn(true);
        omp.clearFreeze();

        omp.getPlayer().getInventory().setContents(contents.get(omp));
        omp.getPlayer().getInventory().setArmorContents(armorContents.get(omp));

        omp.on2FALogin();

        return Result.SUCCESSFUL;
    }

    public void onLogout(OMPlayer omp) {
        if (!tasks.containsKey(omp))
            return;

        tasks.get(omp).cancel();
        tempKeys.remove(omp);

        /* Restore Inventory */
        if (contents.containsKey(omp))
            omp.getPlayer().getInventory().setContents(contents.get(omp));

        if (armorContents.containsKey(omp))
            omp.getPlayer().getInventory().setArmorContents(armorContents.get(omp));

        contents.remove(omp);
        armorContents.remove(omp);
    }

    public void processNewLogin(OMPlayer omp) {
        String secret = GOOGLE_AUTH.createCredentials().getKey();

        tempKeys.put(omp, secret);

        omp.sendMessage(Message.PREFIX_2FA, Color.BLUE, "Gebruik de QR code op de kaart om je 2FA code in te stellen.", "§r§7Initiating new 2FA. Use the QR code on the map.");

        QRMapRenderer qrMap;
        try {
            qrMap = new QRMapRenderer(this, omp, secret);
        } catch (WriterException e) {
            e.printStackTrace();
            qrMap = null;
        }

        if (qrMap == null) {
            ComponentMessage cM = new ComponentMessage();
            cM.add(new Message(Message.PREFIX_2FA, Color.RED, "§r§7Er is een fout opgetreden, ", "§r§7An error occurred, please "));
            cM.add(new Message("§6klik hier", "§6click here"), ClickEvent.Action.OPEN_URL, new Message("https://www.google.com/chart?chs=250x250&cht=qr&chl=" + otpAuth(omp, secret)), HoverEvent.Action.SHOW_TEXT, new Message("§7Open de QR Code in je browser.", "§7Open QR Code in browser."));
            cM.add(new Message("§7."));

            cM.send(omp);
        } else {
            MapView mapView = Bukkit.createMap(omp.getWorld());

            ItemStack item = new ItemBuilder(Material.MAP, 1, "§c§l2FA").setDamage(mapView.getId()).build();//TODO DOES THIS STILL WORK ? DAMAGE=MAPVIEW.GETID

            omp.getPlayer().getInventory().setItem(0, item);
            omp.getPlayer().getInventory().setHeldItemSlot(0);

            for (MapRenderer mapRenderer : mapView.getRenderers()) {
                mapView.removeRenderer(mapRenderer);
            }
            mapView.addRenderer(qrMap);

            omp.getPlayer().sendMap(mapView);
        }
    }

    public String otpAuth(OMPlayer omp, String secret) {
        return "otpauth://totp/" + "OrbitMines_2FA_IGN_" + omp.getName() + "_UUID_" + UUIDUtils.trim(omp.getUUID()) + "@" + OrbitMines.DOMAIN + "?secret=" + secret;
    }

    private String getSecret(UUID uuid) {
        return Database.get().getString(Table._2FA, Table2FA.SECRET, new Where(Table2FA.UUID, uuid.toString()));
    }

    public enum Result {

        SUCCESSFUL,
        INVALID_CODE;

    }
}
