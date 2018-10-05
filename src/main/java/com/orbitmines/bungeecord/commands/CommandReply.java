package com.orbitmines.bungeecord.commands;

import com.orbitmines.api.Color;
import com.orbitmines.api.StaffRank;
import com.orbitmines.api.database.Database;
import com.orbitmines.api.database.Table;
import com.orbitmines.api.database.Where;
import com.orbitmines.api.database.tables.TableFriends;
import com.orbitmines.api.settings.Settings;
import com.orbitmines.api.settings.SettingsType;
import com.orbitmines.api.utils.CommandLibrary;
import com.orbitmines.bungeecord.handlers.BungeePlayer;
import com.orbitmines.bungeecord.handlers.cmd.Command;
import com.orbitmines.spigot.api.handlers.Data;
import com.orbitmines.spigot.api.handlers.data.SettingsData;
import com.orbitmines.spigot.api.utils.Serializer;
import net.md_5.bungee.api.event.ChatEvent;

import java.util.List;
import java.util.UUID;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class CommandReply extends Command {

    public CommandReply(){
        super(CommandLibrary.REPLY);
    }

    @Override
    public void dispatch(ChatEvent event, BungeePlayer omp, String[] a) {
        if (omp.isMuted()) {
            omp.sendMessage("Mute", Color.RED, "§7Je bent gemute!", "§7You have been muted!");//TODO GIVE player indication how long the mute lasts
            return;
        }

        if (a.length < 2) {
            getHelpMessage(omp).send(omp);
            return;
        }

        if (!omp.hasLastMsg() || !omp.getLastMsg().getPlayer().isConnected()) {
            omp.sendMessage("Msg", Color.RED, "Je hebt niemand om op te reageren.", "You have nobody to reply to.");
            omp.setLastMsg(null);
            return;
        }

        BungeePlayer omp2 = omp.getLastMsg();

        SettingsData data = (SettingsData) omp2.getData(Data.Type.SETTINGS);
        data.load();

        SettingsType type = data.getSettings().get(Settings.PRIVATE_MESSAGES);

        if (type != SettingsType.ENABLED && !omp.isEligible(StaffRank.MODERATOR)) {
            switch (type) {
                case DISABLED: {
                    omp.sendMessage("Msg", Color.RED, omp2.getName() + " §7heeft privé berichten uit staan.", omp2.getName() + " §7has private messages disabled.");
                    return;
                }
                case ONLY_FRIENDS: {
                    List<UUID> friends = Serializer.parseUUIDList(Database.get().getString(Table.FRIENDS, TableFriends.FRIENDS, new Where(TableFriends.UUID, omp2.getUUID().toString())));

                    if (!friends.contains(omp.getUUID())) {
                        omp.sendMessage("Msg", Color.RED, omp2.getName() + " §7heeft privé berichten op alleen §bVrienden§7 staan.", omp2.getName() + " §7has private messages set to §bFriends§7 only.");
                        return;
                    }
                    break;
                }
                case ONLY_FAVORITE_FRIENDS: {
                    List<UUID> favorites = Serializer.parseUUIDList(Database.get().getString(Table.FRIENDS, TableFriends.FAVORITE, new Where(TableFriends.UUID, omp2.getUUID().toString())));

                    if (!favorites.contains(omp.getUUID())) {
                        omp.sendMessage("Msg", Color.RED, omp2.getName() + " §7heeft privé berichten op alleen §6Favoriete Vrienden§7 staan.", omp2.getName() + " §7has private messages set to §6Favorite Friends§7 only.");
                        return;
                    }
                    break;
                }
            }
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 1; i < a.length; i++) {
            if (i != 1)
                stringBuilder.append(" ");

            stringBuilder.append(a[i]);
        }
        String message = stringBuilder.toString();

        omp.sendMessage("§7§l" + omp.lang("Jij", "You") + " §7» " + omp2.getRankPrefix() + omp2.getName() + " §f§l" + message);
        omp2.sendMessage(omp.getRankPrefix() + omp.getName() + " §7» §7§l" + omp2.lang("Jij", "You") + " §f§l" + message);

        omp.setLastMsg(omp2);
        omp2.setLastMsg(omp);
    }
}
