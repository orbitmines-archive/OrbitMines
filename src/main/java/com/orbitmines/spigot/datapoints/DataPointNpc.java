package com.orbitmines.spigot.datapoints;

import com.orbitmines.api.Server;
import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.api.Mob;
import com.orbitmines.spigot.api.datapoints.DataPointLoader;
import com.orbitmines.spigot.api.datapoints.DataPointSign;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.npc.NPC;
import com.orbitmines.spigot.api.runnables.SpigotRunnable;
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
                NPC npc = new NPC(Mob.SKELETON, location, Server.SURVIVAL.getDisplayName(), new NPC.InteractAction() {
                    @Override
                    public void click(OMPlayer player, NPC clicked) {
                        player.connect(Server.SURVIVAL, true);
                    }
                });

                npc.spawn();
                npc.setItemInMainHand(new ItemStack(Material.STONE_HOE));

                registerServerNpc(Server.SURVIVAL, npc);
                break;
            }
            case "FOG":
                break;
            case "UHSURVIVAL":
                break;
            case "SKYBLOCK":
                break;
            case "CREATIVE":
                break;
            case "KITPVP":
                break;
            case "PRISON":
                break;

            case "EMPTY_SLOT":
                break;

            case "MG_SW":
                break;
            case "MG_UHC":
                break;
            case "MG_SG":
                break;
            case "MG_CB":
                break;
            case "MG_SC":
                break;
            case "MG_GA":
                break;
            case "MG_SP":
                break;
            default:
                /* Otherwise pass it on to the ServerHandler */
                OrbitMines.getInstance().getServerHandler().setupNpc(string, location);
                break;
        }
    }

    private void registerServerNpc(Server server, NPC npc) {
        new SpigotRunnable(SpigotRunnable.TimeUnit.SECOND, 5) {
            @Override
            public void run() {
                Server.Status status = server.getStatus();

                switch (status) {

                    case ONLINE:
                        int players = server.getPlayers();
                        int maxPlayers = server.getMaxPlayers();
                        npc.setDisplayName(server.getDisplayName() + " §8- " + server.getColor().getChatColor() + players + "§7/" + maxPlayers);
                        break;
                    case OFFLINE:
                    case MAINTENANCE:
                        npc.setDisplayName(server.getDisplayName() + " §8- " + status.getColor().getChatColor() + "§l" + status.getName());
                        break;
                }
            }
        };
    }
}
