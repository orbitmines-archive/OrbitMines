package com.orbitmines.spigot.api.cmds;
/*
 * OrbitMines - @author Fadi Shawki - 12-6-2018
 */

import com.orbitmines.api.Color;
import com.orbitmines.api.Message;
import com.orbitmines.api.ServerList;
import com.orbitmines.api.TopVoterReward;
import com.orbitmines.api.database.Table;
import com.orbitmines.api.database.tables.TableVotes;
import com.orbitmines.api.utils.DateUtils;
import com.orbitmines.api.utils.NumberUtils;
import com.orbitmines.api.utils.TimeUtils;
import com.orbitmines.api.CachedPlayer;
import com.orbitmines.spigot.api.handlers.Data;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.chat.ComponentMessage;
import com.orbitmines.spigot.api.handlers.cmd.Command;
import com.orbitmines.spigot.api.handlers.data.VoteData;
import com.orbitmines.spigot.api.handlers.leaderboard.cmd.DefaultCommandLeaderBoard;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;

public class CommandTopVoters extends DefaultCommandLeaderBoard {

    private String[] alias = { "/topvoters", "/topvoters", "/voters" };

    public CommandTopVoters() {
        super("Top Voters of " + DateUtils.getMonth() + " " + DateUtils.getYear(), Color.BLUE, null, 5, Table.VOTES, TableVotes.UUID, TableVotes.VOTES);

        new CommandVote();
    }

    @Override
    public String[] getAlias() {
        return alias;
    }

    @Override
    public void onDispatch(OMPlayer omp, String[] a) {
        sendVoteRewardMessage(omp);
    }

    @Override
    public String getHelp() {
        return null;
    }

    @Override
    public String getValue(CachedPlayer player, int count) {
        return "§9" + count + " " + (count == 1 ? "Vote" : "Votes");
    }

    public void sendVoteRewardMessage(OMPlayer omp) {
        omp.sendMessage(" §a§lRewards");

        TopVoterReward[] values = TopVoterReward.values();
        for (int i = 0; i < values.length; i++) {
            TopVoterReward reward = values[i];

            String color;
            switch (i) {
                case 0:
                    color = "§6";
                    break;
                case 1:
                    color = "§7";
                    break;
                case 2:
                    color = "§c";
                    break;
                default:
                    color = "§8";
                    break;
            }

            ComponentMessage cM = new ComponentMessage();
            cM.add(new Message("  " + color + "§l" + (i + 1) + ".§r "));
            cM.add(new Message("§3§l" + reward.getStep() + "€/" + TopVoterReward.STEP_VOTES + " Votes§r §7(OrbitMines Shop Voucher)"), HoverEvent.Action.SHOW_TEXT, new Message(
                    "§3§l" + reward.getStep() + "€/" + TopVoterReward.STEP_VOTES + " Votes\n" +
                            "§7§oOrbitMines Shop Voucher\n" +
                            "\n" +
                            omp.lang("§7Je krijgt " + reward.getStep() + "€ per " + TopVoterReward.STEP_VOTES + " Votes.\n", "§7You'll receive " + reward.getStep() + "€ per " + TopVoterReward.STEP_VOTES + " Votes.\n") +
                            omp.lang(
                                    "\n§7§oVoorbeeld:\nAls je deze maand de top voter bent,\n",
                                    "\n§7§oExample:\nIf you are the top voter this month,\n"
                            ) +
                            omp.lang(
                                    "§7§oen je hebt 100 votes, krijg je 100/" + TopVoterReward.STEP_VOTES + " = " + (reward.getStep() * 4) + "€.",
                                    "§7§oand you have 100 votes, then you'll\nreceive 100/"  + TopVoterReward.STEP_VOTES + " = " + (reward.getStep() * 4) + "€."
                            )
            ));
            cM.send(omp);
        }
        omp.sendMessage(" §a§lCommunity Goal");

        {
            ComponentMessage cM = new ComponentMessage();
            cM.add(new Message("  §7- "));
            cM.add(new Message("§9§l" + NumberUtils.locale(getTotalCount()) + " §7§l/ " + NumberUtils.locale(TopVoterReward.COMMUNITY_GOAL) + " Votes"), HoverEvent.Action.SHOW_TEXT, new Message(
                    "§a§lCommunity Goal\n" +
                            (getTotalCount() > TopVoterReward.COMMUNITY_GOAL ? omp.lang("§d§lVOLTOOID", "§d§lACHIEVED") : omp.lang("§d§lNOG NIET VOLTOOID", "§c§lNOT YET ACHIEVED")) + "\n" +
                            "\n" +
                            omp.lang(
                                    "§7Iedereen die gevoten heeft krijg\n§e§l" + TopVoterReward.COMMUNITY_GOAL_SOLARS_PER_VOTE + " Solars§7 per vote.\n",
                                    "§7Everyone who voted will receive\n§e§l" + TopVoterReward.COMMUNITY_GOAL_SOLARS_PER_VOTE +" Solars§7 per vote.\n"
                            ) +
                            omp.lang(
                                    "\n§7§oVoorbeeld:\nAls de community goal bereikt is,\n",
                                    "\n§7§oExample:\nIf the community goal is achieved,\n"
                            ) +
                            omp.lang(
                                    "§7§oen je hebt 100 votes, krijg je " + (100 * TopVoterReward.COMMUNITY_GOAL_SOLARS_PER_VOTE) + " Solars.",
                                    "§7§oand you have 100 votes, then\nyou'll receive " + (100 * TopVoterReward.COMMUNITY_GOAL_SOLARS_PER_VOTE) + " Solars."
                            )
            ));
            cM.send(omp);
        }

        omp.sendMessage(" §a§l" + omp.lang("Maandelijkse", "Monthly") + " Achievement");
        {
            ComponentMessage cM = new ComponentMessage();
            cM.add(new Message("  §7- "));

            int votes = ((VoteData) omp.getData(Data.Type.VOTES)).getVotes();

            cM.add(new Message("§9§l" + votes + " §7§l/ " + TopVoterReward.MONTHLY_ACHIEVEMENT_VOTES + " Votes"), HoverEvent.Action.SHOW_TEXT, new Message(
                    "§a§l" + omp.lang("Maandelijkse", "Monthly") + " Achievement" + "\n" +
                            (votes > 50 ? omp.lang("§d§lVOLTOOID", "§d§lACHIEVED") : omp.lang("§d§lNOG NIET VOLTOOID", "§c§lNOT YET ACHIEVED")) + "\n" +
                            "\n" +
                            omp.lang(
                                    "§7Je krijgt §9§l" + NumberUtils.locale(TopVoterReward.MONTHLY_ACHIEVEMENT_PRISMS) + "§7 Prisms§7.",
                                    "§7You'll receive §9§l" + NumberUtils.locale(TopVoterReward.MONTHLY_ACHIEVEMENT_PRISMS) + " Prisms§7."
                            )
            ));
            cM.send(omp);
        }
    }

    public class CommandVote extends Command {

        private String[] alias = { "/vote" };

        public CommandVote() {
            super(null);
        }

        @Override
        public String[] getAlias() {
            return alias;
        }

        @Override
        public String getHelp(OMPlayer omp) {
            return null;
        }

        @Override
        public void dispatch(OMPlayer omp, String[] a) {
            VoteData data = ((VoteData) omp.getData(Data.Type.VOTES));
            data.updateVoteTimeStamps();

            omp.sendMessage("");
            omp.sendMessage(" §7§lOrbit§8§lMines §9§lVote Links");

            ServerList[] serverLists = ServerList.values();
            for (int i = 0; i < serverLists.length; i++) {
                ServerList serverList = serverLists[i];

                boolean canVote = !data.getVoteTimeStamps().containsKey(serverList);
                String color = canVote ? "§9" : "§c";

                ComponentMessage cM = new ComponentMessage();
                cM.add(new Message("  §7- "));
                cM.add(new Message(color + "§lVote Link " + (i + 1)), ClickEvent.Action.OPEN_URL, new Message(serverList.getUrl()),
                        HoverEvent.Action.SHOW_TEXT, canVote ?
                                new Message("§7Klik hier om §9Vote Link " + (i + 1) + "§7 te openen.", "§7Click here to open §9Vote Link " + (i + 1) + "§7.") :
                                new Message(omp.lang("§7Je kan weer voten in §9", "§7You can vote again in §9") + TimeUtils.fromTimeStamp(serverList.getCooldown(data.getVoteTimeStamps().get(serverList)) * 1000, omp.getLanguage()) + "§7."));

                cM.send(omp);
            }

            sendVoteRewardMessage(omp);
        }
    }
}
