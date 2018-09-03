package com.orbitmines.spigot.api.cmds;
/*
 * OrbitMines - @author Fadi Shawki - 12-6-2018
 */

import com.orbitmines.api.*;
import com.orbitmines.api.database.Table;
import com.orbitmines.api.database.tables.TableVotes;
import com.orbitmines.api.utils.DateUtils;
import com.orbitmines.api.utils.NumberUtils;
import com.orbitmines.api.utils.TimeUtils;
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
            cM.add(new Message("§3§l" + reward.getStep() + "€§r §7(OrbitMines Shop Voucher)"), HoverEvent.Action.SHOW_TEXT, new Message(
                    "§3§l" + reward.getStep() + "€\n" +
                            "§7§oOrbitMines Shop Voucher"
            ));
            cM.send(omp);
        }

        {
            /* Community Goal */

            String description = "§a§lCommunity Goal\n" +
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
                            );

            ComponentMessage cM = new ComponentMessage();

            cM.add(new Message(" §a§lCommunity Goal\n"), HoverEvent.Action.SHOW_TEXT, new Message(description));
            cM.add(new Message("  §7- "));
            cM.add(new Message("§9§l" + NumberUtils.locale(getTotalCount()) + " §7§l/ " + NumberUtils.locale(TopVoterReward.COMMUNITY_GOAL) + " Votes"), HoverEvent.Action.SHOW_TEXT, new Message(description));
            cM.send(omp);
        }

        {
            /* Personal Achievements */

            int votes = ((VoteData) omp.getData(Data.Type.VOTES)).getVotes();
            TopVoterReward.PersonalAchievement current = null;
            for (int i = 0; i < TopVoterReward.MONTHLY_ACHIEVEMENT_VOTES.length; i++) {
                if (i == 0 || votes > TopVoterReward.MONTHLY_ACHIEVEMENT_VOTES[i - 1].getVotes())
                    current = TopVoterReward.MONTHLY_ACHIEVEMENT_VOTES[i];
            }

            ComponentMessage cM = new ComponentMessage();
            StringBuilder description = new StringBuilder("§a§l" + omp.lang("Maandelijkse", "Monthly") + " Achievement");

            for (TopVoterReward.PersonalAchievement achievement : TopVoterReward.MONTHLY_ACHIEVEMENT_VOTES) {
                description.append("\n\n").append("§a§lTier ").append(NumberUtils.toRoman(achievement.getTier())).append(" §7» ");

                if (votes > achievement.getVotes())
                    description.append(omp.lang("§d§lVOLTOOID", "§d§lACHIEVED"));
                else
                    description.append("§9§l").append(votes).append(" §7§l/ ").append(achievement.getVotes());

                if (achievement.getPrisms() != 0)
                    description.append("\n §7- §9§l").append(NumberUtils.locale(achievement.getPrisms())).append(" Prisms");
                if (achievement.getSolars() != 0)
                    description.append("\n §7- §e§l").append(NumberUtils.locale(achievement.getSolars())).append(" Solars");
            }

            cM.add(new Message(" §a§l" + omp.lang("Maandelijkse", "Monthly") + " Achievement " + NumberUtils.toRoman(current.getTier()) + "\n"), HoverEvent.Action.SHOW_TEXT, new Message(description.toString()));
            cM.add(new Message("  §7- "));
            cM.add(new Message("§9§l" + votes + " §7§l/ " + current.getVotes() + " Votes"), HoverEvent.Action.SHOW_TEXT, new Message(description.toString()));
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
            omp.sendMessage(" §8§lOrbit§7§lMines §9§lVote Links");

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
