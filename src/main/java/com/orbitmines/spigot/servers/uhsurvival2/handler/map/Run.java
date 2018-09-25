package com.orbitmines.spigot.servers.uhsurvival2.handler.map;

import com.orbitmines.spigot.servers.uhsurvival2.handler.map.section.MapSection;

public class Run {

    public static void main(String[] args){
        Map map = new Map(-1000, 1000, -1000, 1000, 10);
        for(MapSection mapSection : map.getSections()){
            System.out.println(mapSection.getX() + "|" + mapSection.getZ());
        }
    }

}
