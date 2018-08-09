package com.orbitmines.spigot.servers.survival.cmds.vip;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.Color;
import com.orbitmines.api.Message;
import com.orbitmines.api.Server;
import com.orbitmines.api.VipRank;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.chat.ComponentMessage;
import com.orbitmines.spigot.api.handlers.cmd.VipCommand;
import com.orbitmines.spigot.servers.survival.handlers.SurvivalPlayer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CommandTpHere extends VipCommand {

    private String[] alias = { "/tphere", "/teleporthere" };

    public CommandTpHere() {
        super(Server.SURVIVAL, VipRank.EMERALD);
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
    public void onDispatch(OMPlayer player, String[] a) {
        SurvivalPlayer omp = (SurvivalPlayer) player;
        Player p = omp.getPlayer();

        if (a.length == 2) {
            Player p2 = Bukkit.getPlayer(a[1]);

            if (p2 != null) {
                if (p2 != p) {
                    SurvivalPlayer omp2 = SurvivalPlayer.getPlayer(p2);

                    omp2.getTpHereRequests().add(p.getName());
                    omp2.getTpRequests().remove(p.getName());

                    omp.sendMessage("Teleport", Color.LIME, "§7Teleport verzoek verzonden naar §6" + omp2.getName() + "§7!", "§7Teleport request sent to §6" + omp2.getName() + "§7!");
                    omp2.sendMessage("Teleport", Color.BLUE, omp.getName() + "§7 wilt jouw naar hem/haar teleporteren.", omp.getName() + "§7 wants you to teleport to them.");

                    ComponentMessage cm = new ComponentMessage();
                    cm.add(new Message("Teleport", Color.LIME, "  §7Klik hier om te §aAccepteren§7.", "  §7Click here to §aAccept§7."), ClickEvent.Action.RUN_COMMAND, new Message("/accept " + p.getName()), HoverEvent.Action.SHOW_TEXT, new Message("§7Teleporteren naar " + omp.getName() + "§7.", "§7Teleport to " + omp.getName() + "§7."));
                    cm.send(omp2);
                } else {
                    omp.sendMessage("Teleport", Color.LIME, "§7Je kan niet naar jezelf toe §6teleporten§7!", "§7You can't §6teleport§7 to yourself!");
                }
            } else {
                omp.sendMessage("Teleport", Color.LIME, "§6" + a[1] + "§7 is niet online!", "§6" + a[1] + "§7 is not online!");
            }
        } else {
            getHelpMessage(omp).send(omp);
        }
    }
}
