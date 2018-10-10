package com.orbitmines.spigot.api.handlers.leaderboard.podium;

import com.orbitmines.api.CachedPlayer;
import com.orbitmines.api.Color;
import com.orbitmines.api.VipRank;
import com.orbitmines.api.database.Column;
import com.orbitmines.api.database.Database;
import com.orbitmines.api.database.Table;
import com.orbitmines.api.database.Where;
import com.orbitmines.spigot.api.handlers.leaderboard.LeaderBoard;
import com.orbitmines.spigot.api.handlers.npc.Hologram;
import com.orbitmines.spigot.api.utils.BlockDataUtils;
import com.orbitmines.spigot.api.utils.WorldUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/*
 * OrbitMines - @author Fadi Shawki - 29-7-2017
 */
public class DefaultPodiumLeaderBoard extends LeaderBoard {

    private static List<Podium> podiums = new ArrayList<>();

    protected int size;

    protected List<Map<Column, String>> ordered;

    public DefaultPodiumLeaderBoard(Location location, String[] data, int size, Table table, Column uuidColumn, Column column, Where... wheres) {
        super(location, table, uuidColumn, column, wheres);

        int place = Integer.parseInt(data[1]);
        float yaw = Float.parseFloat(data[2]);

        if (podiums.size() != 1) {
            podiums.add(new Podium(location, place, yaw));
            getLeaderBoards().remove(this);
            return;
        }

        this.size = size;
        this.ordered = new ArrayList<>();

        podiums.add(new Podium(location, place, yaw));
    }

    @Override
    public void update() {
        /* Clear from previous update */
        this.ordered.clear();

        /* Update top {size} players */
        List<Map<Column, String>> entries = Database.get().getEntries(table, columnArray, wheres);

        this.ordered = getOnLeaderBoard(entries);

        /* Update Hologram */
        for (Podium podium : podiums) {
            podium.update();
        }
    }

    public int getSize() {
        return size;
    }

    /* Override this method to change the displayed uuids */
    protected List<Map<Column, String>> getOnLeaderBoard(List<Map<Column, String>> entries) {
        List<Map<Column, String>> ordered = new ArrayList<>(entries);
        ordered.sort((m1, m2) -> Integer.parseInt(m2.get(columnArray[1])) - Integer.parseInt(m1.get(columnArray[1])));

        if (ordered.size() > size)
            ordered = ordered.subList(0, size);

        return ordered;
    }

    /* Override this method to change to change the message displayed at the end */
    public String getValue(CachedPlayer player, int count) {
        return "§6" + count + "";
    }

    private final class Podium {

        private final Location location;
        private final int place;
        private final float yaw;

        private final Hologram hologram;

        private CachedPlayer player;
        private int count;

        public Podium(Location location, int place, float yaw) {
            this.location = location;
            this.place = place;
            this.yaw = yaw;

            hologram = new Hologram(location, 0, Hologram.Face.UP);

//            String placeString;
//            switch (place) {
//                case 1:
//                    placeString = "§6§l1"
//                    break;
//            }
//            if (place == 1)
//                placeString = "§6§l1st";
//            else if ()
//                placeString

            hologram.addLine(() -> {
                String playerName;
                Color prefixColor;

                if (player != null) {
                    playerName = player.getPlayerName();
                    prefixColor = player.getRankPrefixColor();
                } else {
                    playerName =  "UNKNOWN PLAYER";
                    prefixColor = VipRank.NONE.getPrefixColor();
                }

                return prefixColor.getChatColor() + playerName;
            }, false);

            hologram.addLine(() -> getValue(player, count), false);
            hologram.create();
        }

        public Location getLocation() {
            return location;
        }

        public int getPlace() {
            return place;
        }

        public float getYaw() {
            return yaw;
        }

        public void update() {
            if (ordered == null)
                return;

            if (ordered.size() < place) {
                player = null;
                count = 0;

                Block block = BlockDataUtils.setBlock(location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ(), Material.SKELETON_SKULL);

                Skull skull = (Skull) block.getState();
                skull.setRotation(WorldUtils.fromYaw(yaw));

                block.getState().update(true);
                return;
            }

            Map<Column, String> entry = ordered.get(place - 1);

            UUID uuid = UUID.fromString(entry.get(columnArray[0]));
            this.player = CachedPlayer.getPlayer(uuid);
            this.count = Integer.parseInt(entry.get(columnArray[1]));

            hologram.update();

            Block block = BlockDataUtils.setBlock(location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ(), Material.PLAYER_HEAD);

            Skull skull = (Skull) block.getState();
            skull.setOwningPlayer(Bukkit.getOfflinePlayer(uuid));
            skull.setRotation(WorldUtils.fromYaw(yaw));

            block.getState().update(true);
        }
    }
}
