package com.orbitmines.spigot.servers.survival.handlers;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.Color;
import com.orbitmines.api.Language;
import com.orbitmines.api.Server;
import com.orbitmines.api.database.*;
import com.orbitmines.api.database.Set;
import com.orbitmines.api.database.tables.TableServerData;
import com.orbitmines.api.database.tables.survival.TableSurvivalPlayers;
import com.orbitmines.api.utils.DirectoryUtils;
import com.orbitmines.api.utils.TimeUtils;
import com.orbitmines.spigot.api.handlers.npc.Hologram;
import com.orbitmines.spigot.api.handlers.timer.Timer;
import com.orbitmines.spigot.api.handlers.worlds.WorldLoader;
import com.orbitmines.spigot.api.runnables.SpigotRunnable;
import com.orbitmines.spigot.api.utils.Serializer;
import com.orbitmines.spigot.servers.survival.Survival;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public enum ResetTimer {

    //TODO TEST
    NETHER_RESET("Nether", WorldLoader.Type.NETHER, Color.RED, TimeUnit.DAYS, 90) {
        @Override
        public World getWorld(Survival survival) {
            return survival.getWorld_nether();
        }

        @Override
        public void setWorld(Survival survival, World world) {
            survival.setWorld_nether(world);
        }
    },
    END_RESET("End", WorldLoader.Type.NETHER, Color.YELLOW, TimeUnit.DAYS, 180) {
        @Override
        public World getWorld(Survival survival) {
            return survival.getWorld_the_end();
        }

        @Override
        public void setWorld(Survival survival, World world) {
            survival.setWorld_the_end(world);
        }
    };

    private final String name;
    private final WorldLoader.Type worldType;
    private final Color color;
    private final TimeUnit timeUnit;
    private final int amount;

    private long nextReset;
    private List<Hologram> holograms;
    private SpigotRunnable runnable;

    ResetTimer(String name, WorldLoader.Type worldType, Color color, TimeUnit timeUnit, int amount) {
        this.name = name;
        this.worldType = worldType;
        this.color = color;
        this.timeUnit = timeUnit;//TODO TEST
        this.amount = amount;
        this.holograms = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public WorldLoader.Type getWorldType() {
        return worldType;
    }

    public String getDisplayName() {
        return color.getChatColor() + "§l" + name;
    }

    public Color getColor() {
        return color;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public int getAmount() {
        return amount;
    }

    public List<Hologram> getHolograms() {
        return holograms;
    }

    public long getNextReset() {
        return nextReset;
    }

    private long nextReset() {
        this.nextReset = System.currentTimeMillis() + timeUnit.toMillis(amount);
        return nextReset;
    }

    public String getResetInString(Language language) {
        long left = nextReset - System.currentTimeMillis();
        return left >= 0 ? TimeUtils.fromTimeStamp(left, language) : "Resetting...";
    }

    public World getWorld(Survival survival) {
        throw new IllegalStateException();
    }

    public void setWorld(Survival survival, World world) {
        throw new IllegalStateException();
    }

    public void setup(Survival survival) {
        if (!Database.get().contains(Table.SERVER_DATA, TableServerData.DATA, new Where(TableServerData.SERVER, survival.getServer().toString()), new Where(TableServerData.TYPE, toString())))
            Database.get().insert(Table.SERVER_DATA, survival.getServer().toString(), toString(), nextReset() + "");
        else
            this.nextReset = Database.get().getLong(Table.SERVER_DATA, TableServerData.DATA, new Where(TableServerData.SERVER, survival.getServer().toString()), new Where(TableServerData.TYPE, toString()));

        /* If the reset should've already happened when the server starts. */
        if (nextReset - System.currentTimeMillis() <= 0)
            reset(survival);
        else
            startRunnable(survival);
    }

    private void reset(Survival survival) {
        String worldName = getWorld(survival).getName();

        survival.getOrbitMines().broadcast("Survival", Color.BLUE, "De " + getDisplayName() + "§7 is aan het resetten...", "The " + getDisplayName() + "§7 is resetting...");

        for (SurvivalPlayer omp : SurvivalPlayer.getSurvivalPlayers()) {
            omp.connect(Server.HUB, true);
        }

        delete(survival);
        setWorld(survival, survival.getOrbitMines().getWorldLoader().loadWorld(worldName, false, worldType));

        Database.get().update(Table.SERVER_DATA, new Set(TableServerData.DATA, nextReset() + ""), new Where(TableServerData.SERVER, survival.getServer().toString()), new Where(TableServerData.TYPE, toString()));

        Bukkit.shutdown();
    }

    private void startRunnable(Survival survival) {
        if (runnable != null)
            runnable.cancel();

        long left = nextReset - System.currentTimeMillis();

        /* Only start reset runnable if the reset will happen today, as the server restarts every 24 hours :) */
        if (left <= TimeUnit.DAYS.toMillis(1)) {
            if (left > TimeUnit.MINUTES.toMillis(5))
                runnable = new Timer(new SpigotRunnable.Time(SpigotRunnable.TimeUnit.SECOND, (int) (left / 1000F) - 5), new SpigotRunnable.Time(SpigotRunnable.TimeUnit.SECOND, 1)) {
                    @Override
                    public void onInterval() {
                        for (Hologram hologram : holograms) {
                            hologram.update();
                        }
                    }

                    @Override
                    public void onFinish() {
                        initiateReset(survival, (int) (nextReset - System.currentTimeMillis()) / 1000);
                    }
                }.getRunnable();
            else
                initiateReset(survival, (int) left / 1000);
        } else {
            /* Otherwise we just update all the holograms every minute instead of every second */
            runnable = new SpigotRunnable(SpigotRunnable.TimeUnit.MINUTE, 1) {
                @Override
                public void run() {
                    for (Hologram hologram : holograms) {
                        hologram.update();
                    }
                }
            };
        }
    }

    private void initiateReset(Survival survival, int seconds) {
        if (runnable != null)
            runnable.cancel();

        BossBar bossBar = Bukkit.createBossBar("", BarColor.RED, BarStyle.SOLID);

        runnable = new Timer(new SpigotRunnable.Time(SpigotRunnable.TimeUnit.SECOND, seconds), new SpigotRunnable.Time(SpigotRunnable.TimeUnit.SECOND, 1)) {
            @Override
            public void onInterval() {
                for (Hologram hologram : holograms) {
                    hologram.update();
                }

                bossBar.setTitle(Server.SURVIVAL.getDisplayName() + " §c§lRestarting in " + TimeUtils.fromTimeStamp((getRemainingTicks() / 20) * 1000, Language.ENGLISH) + "...");
                bossBar.setProgress(getProgress());

                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (!bossBar.getPlayers().contains(player))
                        bossBar.addPlayer(player);
                }
            }

            @Override
            public void onFinish() {
                reset(survival);
            }
        }.getRunnable();
    }

    private void delete(Survival survival) {
        World world = getWorld(survival);

        if (world != null) {
            File worldFile = world.getWorldFolder();

            /* Teleport all players online to spawn */
            for (SurvivalPlayer omp : SurvivalPlayer.getSurvivalPlayers()) {
                if (!omp.getWorld().getName().equals(world.getName()))
                    continue;

                omp.getPlayer().teleport(survival.getLobbySpawn());
                omp.updateLogoutLocation();
            }

            /* Update logout location for all players */
            Database.get().update(Table.SURVIVAL_PLAYERS, new Set(TableSurvivalPlayers.LOGOUT_LOCATION, Serializer.serialize(survival.getLobbySpawn())), new Where(Where.Operator.LIKE, TableSurvivalPlayers.LOGOUT_LOCATION, world.getName() + "%"));

            /* Delete All Claims in the world */
            survival.getClaimHandler().deleteClaims(world);

            /* Unload World */
            Bukkit.unloadWorld(world, true);

            /* Backup World */
            backup(survival);

            /* Delete World */
            DirectoryUtils.deleteDirectory(worldFile);
        }
    }

    private void backup(Survival survival) {
        File file = getWorld(survival).getWorldFolder();
        File dataFolder = survival.getOrbitMines().getDataFolder();

        File backupsFolder = new File(dataFolder.getPath() + "/WorldBackups");

        if (!backupsFolder.exists())
            backupsFolder.mkdir();

        String date = new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss").format(new Date());

        File backup = new File(dataFolder.getPath() + "/WorldBackups/" + file.getName() + "_" + date);

        int i = 1;
        while (backup.exists()) {
            backup.renameTo(new File(dataFolder.getPath() + "/WorldBackups/" + file.getName() + "_" +
                    date + "-#" + i));
        }

        try {
            backup.mkdir();
            FileUtils.copyDirectory(file, backup);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
