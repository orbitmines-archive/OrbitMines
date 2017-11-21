package com.orbitmines.spigot.api.handlers;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class ConfigHandler {

    private JavaPlugin plugin;

    private Map<String, FileConfiguration> configs;
    private Map<String, File> files;

    public ConfigHandler(JavaPlugin plugin) {
        this.plugin = plugin;
        configs = new HashMap<>();
        files = new HashMap<>();
    }

    public void setup(String... configs) {
        if (!plugin.getDataFolder().exists())
            plugin.getDataFolder().mkdir();

        for (String config : configs) {
            File f = new File(plugin.getDataFolder(), config + ".yml");
            files.put(config, f);

            if (!f.exists()) {
                try {
                    Files.copy(plugin.getResource(config + ".yml"), f.toPath());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

            this.configs.put(config, YamlConfiguration.loadConfiguration(f));
        }
    }

    public FileConfiguration get(String config) {
        return configs.get(config);
    }

    public void save(String config) {
        try {
            get(config).save(files.get(config));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void reload(String config) {
        configs.put(config, YamlConfiguration.loadConfiguration(files.get(config)));
    }
}
