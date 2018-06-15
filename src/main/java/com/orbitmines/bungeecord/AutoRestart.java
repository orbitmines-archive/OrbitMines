package com.orbitmines.bungeecord;
/*
 * OrbitMines - @author Fadi Shawki - 15-6-2018
 */

import com.orbitmines.api.*;
import com.orbitmines.api.database.*;
import com.orbitmines.api.database.Set;
import com.orbitmines.api.database.tables.TableServerData;
import com.orbitmines.api.database.tables.TableVotes;
import com.orbitmines.api.utils.DateUtils;
import com.orbitmines.api.utils.TimeUtils;
import com.orbitmines.bungeecord.handlers.BungeePlayer;
import com.orbitmines.bungeecord.handlers.timer.Timer;
import com.orbitmines.bungeecord.runnables.BungeeRunnable;
import com.orbitmines.bungeecord.utils.ConsoleUtils;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.chat.ComponentSerializer;
import net.md_5.bungee.protocol.packet.BossBar;

import java.nio.charset.Charset;
import java.time.Month;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class AutoRestart extends BungeeRunnable {

    private final OrbitMinesBungee bungee;
    private final String startUpMonth;
    private final int startUpYear;
    private final int startUpDayOfMonth;

    public AutoRestart(OrbitMinesBungee bungee) {
        super(TimeUnit.MINUTE, 10);

        this.bungee = bungee;
        this.startUpMonth = DateUtils.getMonth();
        this.startUpYear = DateUtils.getYear();
        this.startUpDayOfMonth = getDayOfMonth();

        /* If the last monthly vote rewards were given to the players, when last_vote_month does not equal this month. */
        if (Database.get().contains(Table.SERVER_DATA, TableServerData.DATA, new Where(TableServerData.SERVER, "BUNGEE"), new Where(TableServerData.TYPE, "LAST_VOTE_MONTH"))
                && !Database.get().getString(Table.SERVER_DATA, TableServerData.DATA, new Where(TableServerData.SERVER, "BUNGEE"), new Where(TableServerData.TYPE, "LAST_VOTE_MONTH")).equals(startUpMonth + startUpYear)) {
            giveEndOfMonthRewards();
        }
    }

    @Override
    public void run() {
        int today = getDayOfMonth();

        ConsoleUtils.msg("DayOfMonth: " + today);

        if (startUpDayOfMonth == today)
            return;

        ConsoleUtils.msg("HourOfDay: " + Calendar.getInstance().get(Calendar.HOUR_OF_DAY));

        /* Initiate Restart at 4 o'clock */
        if (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) == 4) {
            initiateRestart(false);
            return;
        }

        /* Initiate Restart at midnight at the end of the month and give players rewards (top voters) */
        if (today == 1)
            initiateRestart(true);
    }

    private int getDayOfMonth() {
        return Calendar.getInstance().get(Calendar.DATE);
    }

    private void initiateRestart(boolean voteRestart) {
        new Timer(new BungeeRunnable.Time(TimeUnit.MINUTE, 10), new BungeeRunnable.Time(TimeUnit.SECOND, 1)) {
            @Override
            public void onFinish() {
                /* Kick all Players */
                for (BungeePlayer omp : BungeePlayer.getPlayers()) {
                    omp.getPlayer().disconnect("§7§lOrbit§8§lMines\n" + omp.lang("§c§lRestarten...", "§c§lRestarting...") + "\n\n" + omp.lang("§7De server is aan het restarten! Je kan binnen een paar minuten weer joinen!", "§7The server is restarting! You will be able to join the server within a few minutes."));
                }

                /* Shutdown all servers */
                for (Server server : Server.values()) {
                    ServerInfo info = bungee.getServer(server);
                    if (info != null)
                        bungee.getMessageHandler().dataTransfer(PluginMessage.SHUTDOWN, info);
                }

                /* Give Vote Rewards */
                if (voteRestart)
                    giveEndOfMonthRewards();

                /* Shutdown Proxy */
                ProxyServer.getInstance().stop();
            }

            @Override
            public void onInterval() {
                BossBar bossBar = new BossBar(UUID.nameUUIDFromBytes(("BBB:" + new AtomicInteger(1).getAndIncrement()).getBytes(Charset.forName("UTF-8"))), 0);
                bossBar.setColor(2);
                bossBar.setHealth(getProgress());

                bossBar.setTitle(ComponentSerializer.toString(new TextComponent("§7§lOrbit§8§lMines §c§lRestarting in " + TimeUtils.fromTimeStamp(getRemainingSeconds() * 1000, Language.ENGLISH) + "...")));

                BungeePlayer.getPlayers().forEach(omp -> omp.getPlayer().unsafe().sendPacket(bossBar));
            }
        };
    }

    private void giveEndOfMonthRewards() {
        if (!Database.get().contains(Table.SERVER_DATA, TableServerData.DATA, new Where(TableServerData.SERVER, "BUNGEE"), new Where(TableServerData.TYPE, "LAST_VOTE_MONTH")))
            Database.get().insert(Table.SERVER_DATA, "BUNGEE", "LAST_VOTE_MONTH", startUpMonth + startUpYear);
        else
            Database.get().update(Table.SERVER_DATA, new Set(TableServerData.DATA, startUpMonth + startUpYear), new Where(TableServerData.SERVER, "BUNGEE"), new Where(TableServerData.TYPE, "LAST_VOTE_MONTH"));

        /*
                Top 3 Voters:
         */

        String prevMonth = DateUtils.getPrevMonth();
        int year = prevMonth.equalsIgnoreCase(Month.DECEMBER.toString()) ? DateUtils.getYear() -1 : DateUtils.getYear();
        Column voteColumn = TableVotes.getByMonth(prevMonth, year);

        List<Map<Column, String>> entries = Database.get().getEntries(Table.VOTES, new Column[] { TableVotes.UUID, voteColumn });

        int totalCount = 0;
        Map<String, Integer> map = new HashMap<>();

        for (Map<Column, String> entry : entries) {
            String uuidString = entry.get(TableVotes.UUID);
            int count = Integer.parseInt(entry.get(voteColumn));

            /* No rewards if player has 0 votes (Idle month / community goal) */
            if (count == 0)
                continue;

            map.put(uuidString, count);
            totalCount += count;
        }

        List<String> ordered = new ArrayList<>(map.keySet());
        ordered.sort(Comparator.comparing(map::get));

        TopVoterReward[] values = TopVoterReward.values();
        int topVoterCount = values.length;

        if (ordered.size() > topVoterCount)
            ordered = ordered.subList(ordered.size() -topVoterCount, ordered.size());

        for (int i = 0; i < ordered.size(); i++) {
            String stringUUID = ordered.get(ordered.size() -1 -i);
            int votes = map.get(stringUUID);

            TopVoterReward reward = values[i];

            int steps = (votes - (votes % TopVoterReward.STEP_VOTES)) / TopVoterReward.STEP_VOTES;
            int voucher = steps * reward.getStep();

            Database.get().insert(Table.LOOT, stringUUID, reward.getLoot(), Rarity.LEGENDARY.toString(), voucher + "", "&9&l&o#" + (i + 1) + " Voter (" + prevMonth + " " + year + ")");
        }

        /*
                Community Goal
         */

        if (totalCount >= TopVoterReward.COMMUNITY_GOAL) {
            for (String stringUUID : map.keySet()) {
                int votes = map.get(stringUUID);
                int solars = votes * TopVoterReward.COMMUNITY_GOAL_SOLARS_PER_VOTE;

                Database.get().insert(Table.LOOT, stringUUID, "SOLARS", Rarity.RARE.toString(), solars + "", "&a&l&oCommunity Goal " + prevMonth + " " + year + " (" + (votes == 1 ? "Vote" : "Votes") + ")");
            }
        }
    }
}
