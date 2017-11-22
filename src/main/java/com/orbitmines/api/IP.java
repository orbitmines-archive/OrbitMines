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

    private static List<IP> ips = new ArrayList<>();

    private final UUID uuid;
    private String currentServer;
    private String lastIp;
    private String lastLogin;
    private long lastLoginInMillis;
    private List<String> allIps;

    public IP(UUID uuid, String lastIp) {
        ips.add(this);

        this.uuid = uuid;
        this.lastIp = lastIp;
        this.currentServer = null;
        this.lastLogin = getDate(DateUtils.FORMAT);
        updateLastLoginInMillis();
        this.allIps = new ArrayList<>(Collections.singletonList(lastIp));

        updateCurrentServer();
    }

    public IP(UUID uuid, String lastIp, String lastLogin, List<String> allIps) {
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

    public String getLastLoginInTimeUnit() {
        return TimeUtils.biggestTimeUnit(System.currentTimeMillis() - lastLoginInMillis);
    }

    public List<String> getAllIps() {
        return allIps;
    }

    public void set(String ip) {
        lastIp = ip;
        lastLogin = getDate(DateUtils.FORMAT);

        /* Max of 3 IPs stored in history */
        if (!allIps.contains(ip)) {
            if (allIps.size() == 3)
                allIps.remove(0);

            allIps.add(ip);
        }
    }

    private String getDate(SimpleDateFormat format) {
        return format.format(new Date(Calendar.getInstance().getTimeInMillis()));
    }

    private String serializeHistory() {
        StringBuilder stringBuilder = new StringBuilder();
        for (String ip : allIps) {
            stringBuilder.append(ip);
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
            for (String address : ip.getAllIps()) {
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

        List<String> history = new ArrayList<>();
        Collections.addAll(history, values.get(TableIPs.HISTORY).split("\\|"));

        return new IP(uuid, values.get(TableIPs.LAST_IP), values.get(TableIPs.LAST_LOGIN), history);
    }
}
