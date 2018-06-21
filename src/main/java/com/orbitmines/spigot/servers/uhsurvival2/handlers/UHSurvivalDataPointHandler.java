package com.orbitmines.spigot.servers.uhsurvival2.handlers;

import com.orbitmines.spigot.api.datapoints.DataPoint;
import com.orbitmines.spigot.api.datapoints.DataPointHandler;
import com.orbitmines.spigot.api.datapoints.DataPointType;
import com.orbitmines.spigot.datapoints.DataPointNpc;

public class UHSurvivalDataPointHandler extends DataPointHandler {

    public UHSurvivalDataPointHandler(){
        super(Type.values());
    }

    public enum Type implements DataPointType {
        NPC(){
            @Override
            public DataPoint newInstance(){
                return new DataPointNpc();
            }
        }
    }
}
