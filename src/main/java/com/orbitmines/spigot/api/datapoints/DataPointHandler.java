package com.orbitmines.spigot.api.datapoints;

import com.orbitmines.api.Server;
import com.orbitmines.spigot.api.handlers.OrbitMinesMap;
import com.orbitmines.spigot.servers.hub.handlers.HubDataPointHandler;
import org.bukkit.Bukkit;

import java.util.*;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */
public class DataPointHandler {

    private static Map<Server, Map<OrbitMinesMap.Type, DataPointHandler>> handlers = new HashMap<>();

    static {
        /* Setup all DataPoint Handlers */
        for (Server server : Server.values()) {
            handlers.put(server, new HashMap<>());
        }

        //TODO
        handlers.get(Server.HUB).put(OrbitMinesMap.Type.LOBBY, new HubDataPointHandler());
    }

    protected Map<DataPointType, DataPoint> types;

    public DataPointHandler(DataPointType... types) {
        this(Arrays.asList(types));
    }

    public DataPointHandler(Collection<? extends DataPointType> types) {
        this.types = new HashMap<>();

        add(types);
    }

    protected void add(Collection<? extends DataPointType> types) {
        for (DataPointType type : types) {
            this.types.put(type, type.newInstance());
        }
    }

    public Map<DataPointType, DataPoint> getTypes() {
        return types;
    }

    public void setup() {
        for (DataPoint dataPoint : types.values()) {
            if (!dataPoint.setup())
                Bukkit.broadcastMessage(dataPoint.failureMessage);
        }
    }

    public void clearDataPoints() {
        for (DataPointType type : new ArrayList<>(types.keySet())) {
            this.types.put(type, type.newInstance());
        }
    }

    public DataPoint getDataPoint(DataPointType type) {
        return types.get(type);
    }

    public Map<DataPoint.Type, List<DataPoint>> getAsMap() {
        Map<DataPoint.Type, List<DataPoint>> dataPoints = new HashMap<>();

        for (DataPoint dataPoint : types.values()) {
            if (!dataPoints.containsKey(dataPoint.getType()))
                dataPoints.put(dataPoint.getType(), new ArrayList<>());

            dataPoints.get(dataPoint.getType()).add(dataPoint);
        }

        return dataPoints;
    }

    public static DataPointHandler getHandler(Server server, OrbitMinesMap.Type type) {
        return handlers.get(server).get(type);
    }
}
