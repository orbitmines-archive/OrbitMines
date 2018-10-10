package com.orbitmines.spigot.servers.kitpvp.handlers;

import com.orbitmines.spigot.api.datapoints.DataPoint;
import com.orbitmines.spigot.api.datapoints.DataPointHandler;
import com.orbitmines.spigot.api.datapoints.DataPointType;
import com.orbitmines.spigot.servers.kitpvp.datapoints.KitPvPDataPointMapBarrier;
import com.orbitmines.spigot.servers.kitpvp.datapoints.KitPvPDataPointMapSpawnpoint;
import com.orbitmines.spigot.servers.kitpvp.datapoints.KitPvPDataPointMapSpectatorSpawnpoint;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */
public class KitPvPMapDataPointHandler extends DataPointHandler {

    public KitPvPMapDataPointHandler() {
        super(Type.values());
    }

    public enum Type implements DataPointType {

        BARRIER() {
            @Override
            public DataPoint newInstance() {
                return new KitPvPDataPointMapBarrier();
            }
        },
        SPAWNPOINT() {
            @Override
            public DataPoint newInstance() {
                return new KitPvPDataPointMapSpawnpoint();
            }
        },
        SPECTATOR_SPAWNPOINT() {
            @Override
            public DataPoint newInstance() {
                return new KitPvPDataPointMapSpectatorSpawnpoint();
            }
        };

    }
}
