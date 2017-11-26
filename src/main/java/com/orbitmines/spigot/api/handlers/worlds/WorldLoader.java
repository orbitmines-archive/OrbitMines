package com.orbitmines.spigot.api.handlers.worlds;

import com.orbitmines.api.database.Column;
import com.orbitmines.api.database.Database;
import com.orbitmines.api.database.Table;
import com.orbitmines.api.database.tables.TableMaps;
import com.orbitmines.spigot.api.handlers.worlds.flatgenerator.WorldCreatorFlat;
import com.orbitmines.spigot.api.handlers.worlds.voidgenerator.WorldCreatorVoid;
import com.orbitmines.spigot.api.utils.WorldUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;

import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class WorldLoader {

    private final String resourceFolder;

    private List<World> worlds;
    private List<World> normalWorlds;
    private boolean cleanUpPlayerData;

    /* If there are no worlds on the server that need inventories to be saved set cleanUpPlayerData = true */
    public WorldLoader(String resourceFolder, boolean cleanUpPlayerData) {
        this.resourceFolder = resourceFolder;

        this.worlds = new ArrayList<>();
        this.normalWorlds = new ArrayList<>();
        this.cleanUpPlayerData = cleanUpPlayerData;

        cleanUpData();

        /* Cleanup WorldData after crash or forcibly closed servers */
        String worldContainer = Bukkit.getWorldContainer().getAbsoluteFile().getPath();

        List<Map<Column, String>> entries = Database.get().getEntries(Table.MAPS, TableMaps.WORLD_NAME);

        for (Map<Column, String> entry : entries) {
            File file = new File(worldContainer + "/" + entry.get(TableMaps.WORLD_NAME));

            if (file.exists())
                deleteDirectory(file);
        }
    }

    /*
        loadWorld : Create normal worlds; need saving.
     */
    public World loadWorld(String worldFile) {
        return loadWorld(worldFile, false);
    }

    public World loadWorld(String worldFile, boolean removeEntities) {
        return loadWorld(worldFile, removeEntities, Type.NORMAL);
    }

    public World loadWorld(String worldFile, boolean removeEntities, Type type) {
        try {
            World world = Bukkit.createWorld(type.getWorldCreator().getConstructor(String.class).newInstance(worldFile));
            world.setAutoSave(true);
            normalWorlds.add(world);

            if (removeEntities)
                WorldUtils.removeEntities(world);

            return world;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
    *   The following is only for lobbies & maps that don't need saving.
    *   They are stored in {resources.yml-path}/<world name>.zip
    *   The zip will look like this:
    *
    *   DO NOT use spaces in world names.
    *
    *   World.zip
    *      -> World
    *         -> regions
    *         -> data
    *         etc.
    * */

    /*
        fromZip : Pre-configured worlds.
     */

    public World fromZip(String worldFile) {
        return fromZip(worldFile, false);
    }

    public World fromZip(String worldFile, boolean removeEntities) {
        return fromZip(worldFile, removeEntities, Type.NORMAL);
    }

    public World fromZip(String worldFile, boolean removeEntities, Type type) {
        try {
            extractZip(new File(resourceFolder + "/" + worldFile + ".zip"), Bukkit.getWorldContainer().getAbsoluteFile());
            World world = Bukkit.createWorld(type.getWorldCreator().getConstructor(String.class).newInstance(worldFile));
            worlds.add(world);

            if (removeEntities)
                WorldUtils.removeEntities(world);

            return world;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<World> getWorlds() {
        return worlds;
    }

    private void extractZip(File archive, File destDir) throws IOException {
        if (!destDir.exists()) {
            destDir.mkdirs();
        }

        ZipFile zipFile = new ZipFile(archive);
        Enumeration<? extends ZipEntry> entries = zipFile.entries();

        byte[] buffer = new byte[16384];
        int len;
        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            String entryFileName = entry.getName();
            File dir = buildDirectoryHierarchyFor(entryFileName, destDir);

            if (!dir.exists()) {
                dir.mkdirs();
            }

            if (!entry.isDirectory()) {
                File file = new File(destDir, entryFileName);
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }

                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                BufferedInputStream bis = new BufferedInputStream(zipFile.getInputStream(entry));
                while ((len = bis.read(buffer)) > 0) {
                    bos.write(buffer, 0, len);
                }

                bos.flush();
                bos.close();
                bis.close();

            }
        }
        zipFile.close();
    }

    private File buildDirectoryHierarchyFor(String entryName, File destDir) {
        int lastIndex = entryName.lastIndexOf('/');
        String internalPathToEntry = entryName.substring(0, lastIndex + 1);
        return new File(destDir, internalPathToEntry);
    }

    public void cleanUp() {
        for (World world : worlds) {
            cleanUp(world);
        }
        for (World world : normalWorlds) {
            Bukkit.unloadWorld(world, true);
        }

        cleanUpData();
    }

    private void cleanUpData() {
        if (cleanUpPlayerData) {
            deletePlayerData("world");
            deletePlayerData("world_nether");
            deletePlayerData("world_the_end");
        }
        deleteDirectory(new File(Bukkit.getWorldContainer().getAbsoluteFile().getPath() + "/__MACOSX"));
    }

    public void cleanUp(World world) {
        Bukkit.unloadWorld(world, false);
        deleteDirectory(world.getWorldFolder());
    }

    private boolean deleteDirectory(File path) {
        if (path.exists()) {
            File files[] = path.listFiles();
            assert files != null;

            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    deleteDirectory(files[i]);
                } else {
                    files[i].delete();
                }
            }
        }
        return (path.delete());
    }

    private void deletePlayerData(String worldName) {
        delete(worldName, "playerdata");
        delete(worldName, "stats");
    }

    private void delete(String worldName, String fileName) {
        File playerFilesDir = new File(worldName + "/" + fileName);
        if (playerFilesDir.isDirectory()) {
            String[] playerDats = playerFilesDir.list();
            for (String playerDat : playerDats) {
                File datFile = new File(playerFilesDir, playerDat);
                datFile.delete();
            }
        }
    }

    public enum Type {

        NORMAL(WorldCreator.class),
        FLAT(WorldCreatorFlat.class),
        VOID(WorldCreatorVoid.class);

        private final Class<? extends WorldCreator> worldCreator;

        Type(Class<? extends WorldCreator> worldCreator) {
            this.worldCreator = worldCreator;
        }

        public Class<? extends WorldCreator> getWorldCreator() {
            return worldCreator;
        }
    }
}
