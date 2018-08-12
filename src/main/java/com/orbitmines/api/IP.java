package com.orbitmines.api;

import com.orbitmines.api.database.*;
import com.orbitmines.api.database.Set;
import com.orbitmines.api.database.tables.TableIPs;
import com.orbitmines.api.utils.DateUtils;
import com.orbitmines.api.utils.TimeUtils;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class IP {

    private static final int MAX_HISTORY = 1000;
    public static final String UNKNOWN = "UNKNOWN";

    private static List<IP> ips = new ArrayList<>();

    private final UUID uuid;
    private String currentServer;
    private String lastIp;
    private String lastLogin;
    private long lastLoginInMillis;
    private Map<String, String> allIps;

    public IP(UUID uuid, String lastIp) {
        ips.add(this);

        this.uuid = uuid;
        this.lastIp = lastIp;
        this.currentServer = null;
        this.lastLogin = getDate(DateUtils.FORMAT);
        updateLastLoginInMillis();
        this.allIps = new HashMap<>();
        this.allIps.put(lastIp, lastLogin);

        updateCurrentServer();
    }

    public IP(UUID uuid, String lastIp, String lastLogin, Map<String, String> allIps) {
        ips.add(this);

        this.uuid = uuid;
        this.currentServer = null;
        this.lastIp = lastIp;
        this.lastLogin = lastLogin.substring(0, lastLogin.length() -2); /* Remove decimal */
        updateLastLoginInMillis();
        this.allIps = allIps;

        updateCurrentServer();
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getCurrentServer() {
        return currentServer;
    }

    public void setCurrentServer(Server server) {
        this.currentServer = server == null ? "null" : server.toString();

        Database.get().update(Table.IPS, new Set(TableIPs.CURRENT_SERVER, currentServer), new Where(TableIPs.UUID, uuid.toString()));
    }

    public String getLastIp() {
        return lastIp;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public String getLastLoginInTimeUnit(Language language) {
        return TimeUtils.biggestTimeUnit(System.currentTimeMillis() - lastLoginInMillis, language);
    }

    public Map<String, String> getAllIps() {
        return allIps;
    }

    public void set(String ip) {
        lastIp = ip;
        lastLogin = getDate(DateUtils.FORMAT);

        /* Max of 1000 IPs stored in history */
        if (!allIps.containsKey(ip) && allIps.size() == MAX_HISTORY)
            allIps.remove(allIps.get(new ArrayList<>(allIps.keySet()).get(0)));

        allIps.remove(ip);
        allIps.put(ip, lastLogin);
    }

    private String getDate(SimpleDateFormat format) {
        return format.format(new Date(Calendar.getInstance().getTimeInMillis()));
    }

    private String serializeHistory() {
        StringBuilder stringBuilder = new StringBuilder();
        for (String ip : allIps.keySet()) {
            stringBuilder.append(ip);
            stringBuilder.append("~");
            stringBuilder.append(allIps.get(ip));
            stringBuilder.append("|");
        }
        return stringBuilder.toString().substring(0, stringBuilder.length() -1);
    }

    public void insert() {
        Database.get().insert(Table.IPS, Table.IPS.values(uuid.toString(), "null", lastIp, lastLogin, serializeHistory()));
    }

    public void update() {
        Database.get().update(Table.IPS, new Set[] {

                new Set(TableIPs.LAST_IP, lastIp),
                new Set(TableIPs.LAST_LOGIN, lastLogin),
                new Set(TableIPs.HISTORY, serializeHistory())

        }, new Where(TableIPs.UUID, uuid.toString()));
    }

    public void updateLastLogin() {
        lastLogin = Database.get().getString(Table.IPS, TableIPs.LAST_LOGIN, new Where(TableIPs.UUID, uuid.toString()));
        updateLastLoginInMillis();
    }

    private void updateLastLoginInMillis() {
        try {
            lastLoginInMillis = DateUtils.FORMAT.parse(lastLogin).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void updateCurrentServer() {
        currentServer = Database.get().getString(Table.IPS, TableIPs.CURRENT_SERVER, new Where(TableIPs.UUID, uuid.toString()));

        /* offline */
        if (currentServer.equals("null"))
            currentServer = null;
    }

    public static IP getIp(UUID uuid) {
        for (IP ip : ips) {
            if (ip.getUuid().toString().equals(uuid.toString()))
                return ip;
        }
        return fromDatabase(uuid);
    }

    public static List<IP> getIpInfo(String ipString) {
        List<IP> ipList = new ArrayList<>();
        for (IP ip : ips) {
            for (String address : ip.getAllIps().keySet()) {
                if (address.equals(ipString)) {
                    ipList.add(ip);
                    break;
                }
            }
        }
        return ipList;
    }

    public static List<IP> getIps() {
        return ips;
    }

    public static IP update(UUID uuid, String ipString) {
        IP ip = getIp(uuid);
        if (ip == null) {
            ip = fromDatabase(uuid);

            /* New IP: Insert it into the database */
            if (ip == null) {
                ip = new IP(uuid, ipString);
                ip.insert();
                return ip;
            }
        }

        ip.set(ipString);
        ip.update();

        return ip;
    }

    private static IP fromDatabase(UUID uuid) {
        if (!Database.get().contains(Table.IPS, TableIPs.UUID, new Where(TableIPs.UUID, uuid.toString())))
            return null;

        Map<Column, String> values = Database.get().getValues(Table.IPS, new Where(TableIPs.UUID, uuid.toString()));

        Map<String, String> history = new HashMap<>();
        for (String historyEntry : values.get(TableIPs.HISTORY).split("\\|")) {
            String[] data = historyEntry.split("~");

            if (data.length == 1)
                history.put(data[0], UNKNOWN);
            else
                history.put(data[0], data[1]);
        }

        return new IP(uuid, values.get(TableIPs.LAST_IP), values.get(TableIPs.LAST_LOGIN), history);
    }

    public static void loadAll() {
        for (Map<Column, String> entry : Database.get().getEntries(Table.IPS)) {
            Map<String, String> history = new HashMap<>();

            for (String historyEntry : entry.get(TableIPs.HISTORY).split("\\|")) {
                String[] data = historyEntry.split("~");

                if (data.length == 1)
                    history.put(data[0], UNKNOWN);
                else
                    history.put(data[0], data[1]);
            }

            new IP(UUID.fromString(entry.get(TableIPs.UUID)), entry.get(TableIPs.LAST_IP), entry.get(TableIPs.LAST_LOGIN), history);
        }
    }
}
