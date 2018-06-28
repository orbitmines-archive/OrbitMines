package com.orbitmines.spigot.servers.uhsurvival2.utils;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

/**
 * Created by Robin on 2/27/2018.
 */
public class FileBuilder {

    private File file;
    private File directory;
    private YamlConfiguration yaml;

    public FileBuilder(String directory, String fileName){
        this.directory = new File(directory);
        this.file = new File(directory,  fileName + ".yml");
        this.createFile();
        this.yaml = YamlConfiguration.loadConfiguration(file);
    }

    public FileBuilder(File file){
        this.file = file;
        this.directory = file.getParentFile();
        this.createFile();
        this.yaml = YamlConfiguration.loadConfiguration(file);
    }

    private void createFile(){
        if(!directory.exists()){
            directory.mkdir();
        }
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void deleteFile(){
        file.delete();
    }

    public void set(String name, Object obj) {
        yaml.set(name, obj);
    }

    public Object get(String name){
        return yaml.get(name);
    }

    public void save(){
        try {
            yaml.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
