package com.orbitmines.bungeecord.handlers;

import com.google.common.base.Charsets;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class ConfigHandler {

    private Plugin plugin;

    private Map<String, Configuration> configs;
    private Map<String, File> files;

    public ConfigHandler(Plugin plugin) {
        this.plugin = plugin;
        configs = new HashMap<>();
        files = new HashMap<>();
    }

    public void setup(String... configs) {
        if (!plugin.getDataFolder().exists())
            plugin.getDataFolder().mkdir();

        File fD = new File(plugin.getDataFolder() + "/configs");
        if (!fD.exists())
            fD.mkdir();

        for (String config : configs) {
            File f = new File(plugin.getDataFolder() + "/configs", config + ".yml");
            files.put(config, f);

            if (!f.exists()) {
                copyFile(config + ".yml", f.toPath());
            }

            try {
                this.configs.put(config, YamlConfiguration.getProvider(YamlConfiguration.class).load(new InputStreamReader(new FileInputStream(f), Charsets.UTF_8)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Configuration get(String config) {
        return configs.get(config);
    }

    public void save(String config) {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(get(config), new File(plugin.getDataFolder() + "/configs", config + ".yml"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void reload(String config) {
        try {
            this.configs.put(config, YamlConfiguration.getProvider(YamlConfiguration.class).load(new InputStreamReader(new FileInputStream(this.files.get(config)), Charsets.UTF_8)));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void copyFile(String filename, Path path) {
        try {
            Files.copy(plugin.getResourceAsStream(filename), path);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
