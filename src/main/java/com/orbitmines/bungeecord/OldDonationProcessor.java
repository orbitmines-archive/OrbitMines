package com.orbitmines.bungeecord;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.Donation;
import com.orbitmines.api.VipRank;
import com.orbitmines.api.utils.uuid.UUIDUtils;
import com.orbitmines.bungeecord.commands.console.CommandDonation;
import com.orbitmines.bungeecord.handlers.ConfigHandler;
import com.orbitmines.bungeecord.handlers.cmd.ConsoleCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OldDonationProcessor {

    private final ConfigHandler handler;
    private final String config;

    public OldDonationProcessor(ConfigHandler handler, String config) {
        this.handler = handler;
        this.config = config;
    }

    public void load() {
        List<String> entries = handler.get(config).getStringList("donations");

        CommandDonation cmd = (CommandDonation) ConsoleCommand.getCommand("donation");

        List<Process> processes = new ArrayList<>();

        for (String entry : entries) {
            String[] a = entry.split("\\|");
            String name = a[0];
            UUID uuid = UUIDUtils.parse(a[1]);
            Donation donation = Donation.valueOf(a[2]);
            double price = Double.parseDouble(a[3]);
            String[] times = a[4].split(" ");
            String[] dates = times[0].split("-");

            String date = dates[2] + "-" + dates[1] + "-" + dates[0].substring(2);
            String time = times[1].substring(0, times[1].length() - 3);

            Process process = new Process(donation, name, uuid, price, date, time);

            removeRedundantRanks(processes, process);

            processes.add(process);
        }

        for (Process process : processes) {
            process.execute(cmd);
        }
    }

    private void removeRedundantRanks(List<Process> processes, Process p) {
        VipRank rank = p.donation.getRank();

        if (rank == null)
            return;

        p.lootDonation = fromRank(rank);

        for (Process process : processes) {
            if (!process.uuid.toString().equals(p.uuid.toString()))
                continue;

            VipRank processRank = process.donation.getRank();

            if (processRank != null && processRank.ordinal() < rank.ordinal())
                process.addToLoot = false;
        }
    }

    private Donation fromRank(VipRank rank) {
        switch (rank) {
            case IRON:
                return Donation.VIP_IRON;
            case GOLD:
                return Donation.VIP_GOLD;
            case DIAMOND:
                return Donation.VIP_DIAMOND;
            case EMERALD:
                return Donation.VIP_EMERALD;
        }
        return null;
    }

    private class Process {

        private final Donation donation;
        private final String name;
        private final UUID uuid;
        private final double price;
        private final String date;
        private final String time;
        private boolean addToLoot;
        private Donation lootDonation;

        public Process(Donation donation, String name, UUID uuid, double price, String date, String time) {
            this.donation = donation;
            this.name = name;
            this.uuid = uuid;
            this.price = price;
            this.date = date;
            this.time = time;
            this.addToLoot = true;
        }

        public void execute(CommandDonation cmd) {
            cmd.execute(null, new String[] { donation.getId() + "", name + "[Old Donation Processor]", uuid.toString(), price + "", "EUR", date, time, addToLoot + "", (lootDonation == null ? "-1" : lootDonation.getId() + "") });
        }
    }
}
