package com.orbitmines.spigot.api.nms.npc;

/**
 * Created by Fadi on 30-4-2016.
 */
public interface NpcNms {

    void setClassFields();

    void addCustomEntity(Class entityClass, String name, int id);

}
