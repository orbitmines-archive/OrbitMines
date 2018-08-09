package com.orbitmines.bungeecord.commands.moderator;

import com.orbitmines.api.Color;
import com.orbitmines.api.Server;
import com.orbitmines.api.StaffRank;
import com.orbitmines.bungeecord.OrbitMinesBungee;
import com.orbitmines.bungeecord.handlers.BungeePlayer;
import com.orbitmines.bungeecord.handlers.cmd.StaffCommand;
import com.orbitmines.bungeecord.runnables.BungeeRunnable;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.ChatEvent;
import org.bukkit.boss.BossBar;

import java.util.HashMap;
import java.util.Map;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class CommandMaintenance extends StaffCommand {

    private String[] alias = { "/maintenance" };

    private OrbitMinesBungee bungee;

    private Map<Server, BungeeRunnable> runnables;
    private Map<Server, BossBar> bossBar;

    public CommandMaintenance(OrbitMinesBungee bungee) {
        super(StaffRank.MODERATOR);

        this.bungee = bungee;
        this.runnables = new HashMap<>();

        for (Server server : Server.values()) {
            if (server.getStatus() == Server.Status.MAINTENANCE)
                startNewRunnable(server);

//            net.md_5.bungee.protocol.packet.BossBar bossBar = new net.md_5.bungee.protocol.packet.BossBar(UUID.nameUUIDFromBytes(("BBB:" + new AtomicInteger(1).getAndIncrement()).getBytes(Charset.forName("UTF-8"))), 0);
//            bossBar.setColor(2);
//            bossBar.setHealth(getProgress());
//
//            bossBar.setTitle(ComponentSerializer.toString(new TextComponent("§8§lOrbit§7§lMines §c§lRestarting in " + TimeUtils.fromTimeStamp(getRemainingSeconds() * 1000, Language.ENGLISH) + "...")));
//
//            BungeePlayer.getPlayers().forEach(omp -> omp.getPlayer().unsafe().sendPacket(bossBar));
        }
    }

    @Override
    public String[] getAlias() {
        return alias;
    }

    @Override
    public String getHelp(BungeePlayer omp) {
        return "<server>";
    }

    @Override
    public void onDispatch(ChatEvent event, BungeePlayer omp, String[] a) {
        if (a.length < 2) {
            getHelpMessage(omp).send(omp);
            return;
        }

        Server server = bungee.getServer(a[1]);

        if (server == null) {
            omp.sendMessage("Maintenance", Color.RED, "§7Ongeldige server.", "§7Invalid server.");
            return;
        }

        if (server.getStatus() == Server.Status.MAINTENANCE) {
            server.setStatus(Server.Status.OFFLINE);
            omp.sendMessage("Maintenance", Color.LIME, server.getDisplayName() + "§7 is nu uit " + Server.Status.MAINTENANCE.getDisplayName() + "§7.", server.getDisplayName() + "§7 is no longer in " + Server.Status.MAINTENANCE.getDisplayName() + "§7.");

            clearRunnable(server);

            return;
        }

        server.setStatus(Server.Status.MAINTENANCE);
        startNewRunnable(server);

        for (BungeePlayer player : BungeePlayer.getPlayers(server)) {
            if (player.isEligible(StaffRank.MODERATOR))
                continue;

            ServerInfo fallback = player.getFallBackServer();

            if (fallback == null) {
                omp.getPlayer().disconnect(omp.lang("§8§lOrbit§7§lMines\n§7Kan geen server vinden om mee te verbinden.\n\n§7(" + server.getDisplayName() + "§r§7 is in " + Server.Status.MAINTENANCE.getDisplayName() + "§r§7)", "§8§lOrbit§7§lMines\n§7Cannot find a server to fall back to.\n\n§7(" + server.getDisplayName() + "§r§7 is in " + Server.Status.MAINTENANCE.getDisplayName() + "§r§7)"));
                continue;
            }

            player.connect(bungee.getServer(fallback), true);
        }
        omp.sendMessage("Maintenance", Color.LIME, server.getDisplayName() + "§7 is nu in " + Server.Status.MAINTENANCE.getDisplayName() + "§7.", server.getDisplayName() + "§7 is now in " + Server.Status.MAINTENANCE.getDisplayName() + "§7.");
    }

    private void startNewRunnable(Server server) {
//        runnables.put(server, new BungeeRunnable() {
//            @Override
//            public void run() {
//
//            }
//        });
    }

    private void clearRunnable(Server server) {

    }
}
