package com.orbitmines.bungeecord.commands;

import com.orbitmines.api.Color;
import com.orbitmines.api.StaffRank;
import com.orbitmines.api.database.Database;
import com.orbitmines.api.database.Table;
import com.orbitmines.api.database.Where;
import com.orbitmines.api.database.tables.TableFriends;
import com.orbitmines.api.settings.Settings;
import com.orbitmines.api.settings.SettingsType;
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
public class CommandMsg extends Command {

    private String[] alias = { "/msg", "/message", "/m", "/tell", "/whisper", "/t", "/w" };

    @Override
    public String[] getAlias() {
        return alias;
    }

    @Override
    public String getHelp(BungeePlayer omp) {
        return "<player> <message>";
    }

    @Override
    public void dispatch(ChatEvent event, BungeePlayer omp, String[] a) {
        if (omp.isMuted()) {
            omp.sendMessage("Mute", Color.RED, "§7Je bent gemute!", "§7You have been muted!");//TODO GIVE player indication how long the mute lasts
            return;
        }

        if (a.length < 3) {
            getHelpMessage(omp).send(omp);
            return;
        }

        BungeePlayer omp2 = BungeePlayer.getPlayer(a[1]);

        if (omp2 == null) {
            omp.sendMessage("Msg", Color.RED, a[1] + " is niet online.", a[1] + " is not online.");
            return;
        } else if (omp.getPlayer() == omp2.getPlayer()) {
            omp.sendMessage("Msg", Color.RED, "Je kan geen bericht naar jezelf versturen.", "You can't send a message to yourself.");
            return;
        }

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
        for (int i = 2; i < a.length; i++) {
            if (i != 2)
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
