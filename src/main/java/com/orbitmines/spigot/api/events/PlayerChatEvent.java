package com.orbitmines.spigot.api.events;

import com.orbitmines.api.Color;
import com.orbitmines.api.Message;
import com.orbitmines.api.PluginMessage;
import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class PlayerChatEvent implements Listener {

    private final OrbitMines orbitMines;

    public PlayerChatEvent() {
        this.orbitMines = OrbitMines.getInstance();
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        OMPlayer omp = OMPlayer.getPlayer(event.getPlayer());
        event.setCancelled(true);

        if (omp.isLoggedIn()) {
            if (omp.isMuted()) {
                omp.sendMessage("Mute", Color.RED, "§7Je bent gemute!", "§7You have been muted!");//TODO GIVE player indication how long the mute lasts
                return;
            }

            orbitMines.getServerHandler().toMinecraft(omp, event.getMessage());
            orbitMines.getServerHandler().toDiscord(event, omp);
            return;
        }

        /* 2FA */
        switch (orbitMines.get2FA().login(omp, event.getMessage())) {

            case SUCCESSFUL:
                omp.sendMessage(Message.PREFIX_2FA, Color.LIME, "Welcome back, " + omp.getName() + "§7.");
                omp.playSound(Sound.ENTITY_ARROW_HIT_PLAYER);
                orbitMines.getServerHandler().getMessageHandler().dataTransfer(PluginMessage.LOGIN_2FA, omp.getUUID().toString());
                break;
            case INVALID_CODE:
                omp.kick("§8§lOrbit§7§lMines §c§l2FA§r\n§7Invalid Code\n\n§7IGN: §8" + omp.getName() + "\n§7UUID: §8" + omp.getUUID().toString());
                break;
        }
    }
}
