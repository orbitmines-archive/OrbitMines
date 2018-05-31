package com.orbitmines.spigot.datapoints;

import com.orbitmines.api.Server;
import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.api.Mob;
import com.orbitmines.spigot.api.datapoints.DataPointLoader;
import com.orbitmines.spigot.api.datapoints.DataPointSign;
import com.orbitmines.spigot.api.handlers.npc.MobNpc;
import com.orbitmines.spigot.api.handlers.scoreboard.ScoreboardString;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */
public class DataPointNpc extends DataPointSign {

    private Map<String, List<Location>> npcLocations;

    public DataPointNpc() {
        super("NPC", Type.IRON_PLATE, Material.WOOL, DyeColor.YELLOW.getWoolData());

        npcLocations = new HashMap<>();
    }

    @Override
    public boolean buildAt(DataPointLoader loader, Location location, String[] data) {
        String type;
        if (data.length >= 1) {
            type = data[0];
        } else {
            failureMessage = "Npc Type not given.";
            return false;
        }

        float yaw;
        if (data.length >= 2) {
            try {
                yaw = Float.parseFloat(data[1]);
            } catch(NumberFormatException ex) {
                failureMessage = "Invalid Yaw.";
                return false;
            }
        } else {
            failureMessage = "Yaw not given.";
            return false;
        }

        float pitch;
        if (data.length >= 3) {
            try {
                pitch = Float.parseFloat(data[2]);
            } catch(NumberFormatException ex) {
                failureMessage = "Invalid Pitch.";
                return false;
            }
        } else {
            failureMessage = "Pitch not given.";
            return false;
        }

        location.setYaw(yaw);
        location.setPitch(pitch);
        location.add(0.5, 0, 0.5);

        if (!npcLocations.containsKey(type))
            npcLocations.put(type, new ArrayList<>());

        npcLocations.get(type).add(location);

        /* Add delay for LeaderBoard setup */
        new BukkitRunnable() {
            @Override
            public void run() {
                setupNpc(type, location);
            }
        }.runTaskLater(OrbitMines.getInstance(), 1);

        return true;
    }

    @Override
    public boolean setup() {
        return true;
    }

    public Map<String, List<Location>> getNpcLocations() {
        return npcLocations;
    }

    private void setupNpc(String string, Location location) {
        switch (string.toUpperCase()) {
            /* Check any global Npcs */
            case "SURVIVAL": {
                MobNpc npc = new MobNpc(Mob.SKELETON, location, getNpcDisplayName(Server.SURVIVAL));
                npc.setInteractAction((event, omp) -> omp.connect(Server.SURVIVAL, true));

                npc.create();
                npc.setItemInMainHand(new ItemStack(Material.STONE_HOE));
                break;
            }
            case "FOG":
            case "UHSURVIVAL": {
                MobNpc npc = new MobNpc(Mob.CHICKEN, location, getNpcDisplayName(Server.UHSURVIVAL));
                npc.setInteractAction((event, omp) -> omp.connect(Server.UHSURVIVAL, true));

                npc.create();
                break;
            }
            case "SKYBLOCK":
            case "CREATIVE":
            case "KITPVP":
            case "PRISON":
            case "EMPTY_SLOT": {
                MobNpc npc = new MobNpc(Mob.WITHER_SKELETON, location, () -> "§8§lComing Soon");
                npc.create();
                break;
            }
            case "MG_SW":
            case "MG_UHC":
            case "MG_SG":
            case "MG_CB":
            case "MG_SC":
            case "MG_GA":
            case "MG_SP": {
                MobNpc npc = new MobNpc(Mob.WITHER_SKELETON, location, () -> "§8§lComing Soon");
                npc.create();
                break;
            }
            default:
                /* Otherwise pass it on to the ServerHandler */
                OrbitMines.getInstance().getServerHandler().setupNpc(string, location);
                break;
        }
    }

    private ScoreboardString[] getNpcDisplayName(Server server) {
        return new ScoreboardString[]{
                () -> "§7§lOrbit§8§lMines " + server.getDisplayName(),
                () -> {
                    Server.Status status = server.getStatus();
                    return status != Server.Status.ONLINE ? status.getColor().getChatColor() + "§l" + status.getName() : server.getColor().getChatColor() + "§l" + server.getPlayers() + " §7§l/ " + server.getMaxPlayers();
                }
        };
    }
}
