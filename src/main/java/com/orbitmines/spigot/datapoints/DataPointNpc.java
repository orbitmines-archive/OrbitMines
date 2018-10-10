package com.orbitmines.spigot.datapoints;

import com.orbitmines.api.Server;
import com.orbitmines.api.ServerList;
import com.orbitmines.api.VipRank;
import com.orbitmines.api.utils.TimeUtils;
import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.api.Mob;
import com.orbitmines.spigot.api.PeriodLoot;
import com.orbitmines.spigot.api.datapoints.DataPointLoader;
import com.orbitmines.spigot.api.datapoints.DataPointSign;
import com.orbitmines.spigot.api.handlers.Data;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.data.LootData;
import com.orbitmines.spigot.api.handlers.data.PeriodLootData;
import com.orbitmines.spigot.api.handlers.data.VoteData;
import com.orbitmines.spigot.api.handlers.itembuilders.ItemBuilder;
import com.orbitmines.spigot.api.handlers.npc.ArmorStandNpc;
import com.orbitmines.spigot.api.handlers.npc.MobNpc;
import com.orbitmines.spigot.api.handlers.npc.PersonalisedMobNpc;
import com.orbitmines.spigot.api.handlers.scoreboard.ScoreboardString;
import com.orbitmines.spigot.api.runnables.SpigotRunnable;
import com.orbitmines.spigot.api.utils.PlayerUtils;
import com.orbitmines.spigot.api.utils.VectorUtils;
import com.orbitmines.spigot.api.utils.WorldUtils;
import com.orbitmines.spigot.servers.hub.gui.LootGUI;
import com.orbitmines.spigot.servers.kitpvp.KitPvP;
import com.orbitmines.spigot.servers.kitpvp.handlers.KitPvPPlayer;
import com.orbitmines.spigot.servers.kitpvp.handlers.gui.KitSelectorGUI;
import com.orbitmines.spigot.servers.survival.gui.SurvivalPrismSolarShopGUI;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;

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
        super("NPC", Type.IRON_PLATE, Material.YELLOW_WOOL);

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
                MobNpc npc = new MobNpc(Mob.DOLPHIN, location, getNpcDisplayName(Server.SURVIVAL));
                npc.setInteractAction((event, omp) -> omp.connect(Server.SURVIVAL, true));

                npc.create();

                startUpdate(npc);
                break;
            }
            case "KITPVP": {
                MobNpc npc = new MobNpc(Mob.DROWNED, location, getNpcDisplayName(Server.KITPVP));
                npc.setInteractAction((event, omp) -> omp.connect(Server.KITPVP, true));
                npc.create();

                npc.setItemInMainHand(new ItemBuilder(Material.IRON_SWORD).build());
                npc.setHelmet(new ItemBuilder(Material.CHAINMAIL_HELMET).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).build());
                npc.setChestPlate(new ItemBuilder(Material.CHAINMAIL_CHESTPLATE).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).build());
                npc.setLeggings(new ItemBuilder(Material.CHAINMAIL_LEGGINGS).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).build());
                npc.setBoots(new ItemBuilder(Material.CHAINMAIL_BOOTS).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).build());

                startUpdate(npc);
                break;
            }
            case "FOG":
            case "UHSURVIVAL":
            case "SKYBLOCK":
            case "CREATIVE":
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


            case "SURVIVAL_SHOP": {
                MobNpc npc = new MobNpc(Mob.DOLPHIN, location, () -> "§8§lOrbit§7§lMines " + Server.SURVIVAL.getDisplayName(), () -> "§9§lPrism §7§l& §e§lSolar §3§lShop");
                npc.setInteractAction((event, omp) -> new SurvivalPrismSolarShopGUI().open(omp));

                npc.create();
                break;
            }
            case "KIT_SELECTOR": {
                PersonalisedMobNpc npc = new PersonalisedMobNpc(Mob.DROWNED, location) {
                    @Override
                    public ScoreboardString[] getLines(OMPlayer player) {
                        KitPvPPlayer omp = (KitPvPPlayer) player;
                        return new ScoreboardString[] {
                                () -> "§c§lKit Selector",
                                null,
                                () -> "§7§l" + omp.lang("Laatst Geselecteerd:", "Last Selected:"),
                                () -> omp.getLastSelected().getHandler().getDisplayName() + " §a§lLvl " + omp.getLastSelected().getLevel()
                        };
                    }
                };
                npc.setInteractAction((event, omp) -> new KitSelectorGUI((KitPvP) OrbitMines.getInstance().getServerHandler()).open(omp));

                npc.create();

                npc.setChestPlate(new ItemBuilder(Material.DIAMOND_CHESTPLATE).build());
                npc.setItemInMainHand(new ItemBuilder(Material.DIAMOND_CHESTPLATE).build());

                break;
            }
            case "BACK_TO_HUB": {
                MobNpc npc = new MobNpc(Mob.WITHER_SKELETON, location, () -> "§7§lBack to " + Server.HUB.getDisplayName());
                npc.setInteractAction((event, omp) -> omp.connect(Server.HUB, true));

                npc.create();

                npc.setHelmet(new ItemBuilder(Material.WHITE_STAINED_GLASS).build());
                npc.setItemInMainHand(new ItemBuilder(Material.ENDER_PEARL).build());

                break;
            }
            case "LOOT": {
                PersonalisedMobNpc npc = new PersonalisedMobNpc(Mob.TURTLE, location.clone().add(0, 1, 0)) {
                    @Override
                    public ScoreboardString[] getLines(OMPlayer omp) {
                        VoteData voteData = (VoteData) omp.getData(Data.Type.VOTES);
                        voteData.updateVoteTimeStamps();
                        LootData lootData = (LootData) omp.getData(Data.Type.LOOT);
                        PeriodLootData periodLootData = (PeriodLootData) omp.getData(Data.Type.PERIOD_LOOT);

                        int maxVotes = ServerList.values().length;
                        PeriodLoot[] values = PeriodLoot.values();

                        return new ScoreboardString[] {
                                () -> "§a§lSpace§2§lTurtle",
                                () -> {
                                    int lootCount = lootData.getLoot().size();

                                    PeriodLoot shortestDuration = null;
                                    long duration = 0;

                                    for (PeriodLoot loot : values) {
                                        /* Dont display loot specificly for a server */
                                        if (loot.getServer() != null && loot.getServer() != OrbitMines.getInstance().getServerHandler().getServer())
                                            continue;

                                        boolean hasRank = true;
                                        switch (loot) {
                                            case MONTHLY_VIP:
                                            case SURVIVAL_BACK_CHARGES:
                                                hasRank = omp.getVipRank() != VipRank.NONE;
                                                break;
                                            case SURVIVAL_SPAWNER_ITEM:
                                                hasRank = omp.getVipRank() == VipRank.EMERALD;
                                                break;
                                        }

                                        if (hasRank && periodLootData.canCollect(loot)) {
                                            lootCount++;
                                            continue;
                                        }

                                        if (!hasRank)
                                            continue;

                                        long d = periodLootData.getCooldown(loot);

                                        if (shortestDuration != null && duration < d)
                                            continue;

                                        shortestDuration = loot;
                                        duration = d;
                                    }

                                    return lootCount != 0 ? (color ? "§a" : "§2") + "§l" + omp.lang("VERZAMEL", "COLLECT") + " " + lootCount + " " + (lootCount == 1 ? "ITEM" : "ITEMS") : "§7" + omp.lang("Meer loot over", "More loot in") + " §a§l" + TimeUtils.fromTimeStamp(duration * 1000L, omp.getLanguage());
                                },
                                () -> {
                                    int votes = maxVotes - voteData.getVoteTimeStamps().size();
                                    return votes == 0 ? null : omp.lang("§9§l" + votes + " " + (votes == 1 ? "VOTE" : "VOTES") + " OVER", "§9§l" + votes + " " + (votes == 1 ? "VOTE" : "VOTES") + " LEFT");
                                }
                        };
                    }
                };

                npc.setInteractAction((event, omp) -> new LootGUI().open(omp));

                npc.create();

                startLootUpdate(npc);

                {
                    LivingEntity entity = (LivingEntity) npc.getEntity();
                    Location inFront = PlayerUtils.getTargetBlock(entity, 2).getLocation().add(0.5, 0, 0.5);

                    Location helmetLoc = npc.getSpawnLocation().clone().add(VectorUtils.point2D(npc.getSpawnLocation().toVector(), inFront.toVector()).multiply(0.35)).subtract(0, 0.5, 0);

                    ArmorStandNpc helmet = new ArmorStandNpc(helmetLoc.clone());
                    helmet.setGravity(false);
                    helmet.setVisible(false);
                    helmet.setSmall(true);
                    helmet.setInteractAction(npc.getInteractAction());
                    helmet.setHelmet(new ItemBuilder(Material.LIME_STAINED_GLASS).build());
                    helmet.setHeadPose(new EulerAngle(WorldUtils.pitchToDegree(entity), 0, 0));

                    helmet.create();
                }
                break;
            }
            default:
                /* Otherwise pass it on to the ServerHandler */
                OrbitMines.getInstance().getServerHandler().setupNpc(string, location);
                break;
        }
    }

    private void startUpdate(MobNpc npc) {
        new SpigotRunnable(SpigotRunnable.TimeUnit.SECOND, 1) {
            @Override
            public void run() {
                npc.update();
            }
        };
    }

    private List<PersonalisedMobNpc> lootNpcs = new ArrayList<>();
    private boolean startedLoot = false;
    private boolean color = false;

    private void startLootUpdate(PersonalisedMobNpc npc) {
        lootNpcs.add(npc);

        if (startedLoot)
            return;

        startedLoot = true;

        new SpigotRunnable(SpigotRunnable.TimeUnit.TICK, 5) {
            @Override
            public void run() {
                color = !color;

                for (PersonalisedMobNpc npc : lootNpcs) {
                    npc.update();
                }
            }
        };
    }

    private ScoreboardString[] getNpcDisplayName(Server server) {
        //TODO REMOVE!
        if (server == Server.KITPVP)
            return new ScoreboardString[]{
                    () -> "§c§lNEW!",
                    () -> "§8§lOrbit§7§lMines " + server.getDisplayName(),
                    () -> {
                        Server.Status status = server.getStatus();
                        return status != Server.Status.ONLINE ? status.getColor().getChatColor() + "§l" + status.getName() : server.getColor().getChatColor() + "§l" + server.getPlayers() + " §7§l/ " + server.getMaxPlayers();
                    }
            };

        return new ScoreboardString[]{
                () -> "§8§lOrbit§7§lMines " + server.getDisplayName(),
                () -> {
                    Server.Status status = server.getStatus();
                    return status != Server.Status.ONLINE ? status.getColor().getChatColor() + "§l" + status.getName() : server.getColor().getChatColor() + "§l" + server.getPlayers() + " §7§l/ " + server.getMaxPlayers();
                }
        };
    }
}
