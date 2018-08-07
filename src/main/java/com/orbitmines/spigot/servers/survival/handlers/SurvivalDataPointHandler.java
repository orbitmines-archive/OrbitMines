package com.orbitmines.spigot.servers.survival.handlers;

import com.orbitmines.spigot.api.datapoints.DataPoint;
import com.orbitmines.spigot.api.datapoints.DataPointHandler;
import com.orbitmines.spigot.api.datapoints.DataPointType;
import com.orbitmines.spigot.datapoints.DataPointLeaderBoard;
import com.orbitmines.spigot.datapoints.DataPointNpc;
import com.orbitmines.spigot.datapoints.DataPointPatchNotes;
import com.orbitmines.spigot.servers.hub.datapoints.HubDataPointSpawnpoint;
import com.orbitmines.spigot.servers.hub.datapoints.HubDataPointStaffHologram;
import com.orbitmines.spigot.servers.survival.datapoints.SurvivalDataPointEndReset;
import com.orbitmines.spigot.servers.survival.datapoints.SurvivalDataPointNetherReset;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */
public class SurvivalDataPointHandler extends DataPointHandler {

    public SurvivalDataPointHandler() {
        super(Type.values());
    }

    public enum Type implements DataPointType {

        LEADERBOARD() {
            @Override
            public DataPoint newInstance() {
                return new DataPointLeaderBoard();
            }
        },
        NPC() {
            @Override
            public DataPoint newInstance() {
                return new DataPointNpc();
            }
        },
        PATCH_NOTES() {
            @Override
            public DataPoint newInstance() {
                return new DataPointPatchNotes();
            }
        },

        SPAWNPOINT() {
            @Override
            public DataPoint newInstance() {
                return new HubDataPointSpawnpoint();
            }
        },
        STAFF_HOLO() {
            @Override
            public DataPoint newInstance() {
                return new HubDataPointStaffHologram();
            }
        },

        NETHER_RESET() {
            @Override
            public DataPoint newInstance() {
                return new SurvivalDataPointNetherReset();
            }
        },
        END_RESET() {
            @Override
            public DataPoint newInstance() {
                return new SurvivalDataPointEndReset();
            }
        };

    }
}
