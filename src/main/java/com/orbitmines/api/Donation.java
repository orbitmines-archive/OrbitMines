package com.orbitmines.api;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */
public enum Donation {

    /* Prices are in EUR */

    DONATION(0, 0.00, "§3§lDonation", ""),

    VIP_IRON(1, 5.00, VipRank.IRON.getDisplayName(), ""),
    VIP_GOLD(2, 10.00, VipRank.GOLD.getDisplayName(), ""),
    VIP_DIAMOND(3, 25.00, VipRank.DIAMOND.getDisplayName(), ""),
    VIP_EMERALD(4, 50.00, VipRank.EMERALD.getDisplayName(), ""),

    VIP_UPGRADE_IRON_GOLD(5, 5.00, VipRank.IRON.getDisplayName() + "§r §7» " + VipRank.GOLD.getDisplayName(), ""),
    VIP_UPGRADE_IRON_DIAMOND(6, 20.00, VipRank.IRON.getDisplayName() + "§r §7» " + VipRank.DIAMOND.getDisplayName(), ""),
    VIP_UPGRADE_IRON_EMERALD(7, 45.00, VipRank.IRON.getDisplayName() + "§r §7» " + VipRank.EMERALD.getDisplayName(), ""),
    VIP_UPGRADE_GOLD_DIAMOND(8, 15.00, VipRank.GOLD.getDisplayName() + "§r §7» " + VipRank.DIAMOND.getDisplayName(), ""),
    VIP_UPGRADE_GOLD_EMERALD(9, 40.00, VipRank.GOLD.getDisplayName() + "§r §7» " + VipRank.EMERALD.getDisplayName(), ""),
    VIP_UPGRADE_DIAMOND_EMERALD(10, 25.00, VipRank.DIAMOND.getDisplayName() + "§r §7» " + VipRank.EMERALD.getDisplayName(), ""),

    SOLARS_1000(11, 3.00, "§e§l1,000 Solars", ""),
    SOLARS_1750(12, 5.00, "§e§l1,750 Solars", ""),
    SOLARS_3750(13, 10.00, "§e§l3,750 Solars", ""),
    SOLARS_6000(14, 15.00, "§e§l6,000 Solars", ""),
    SOLARS_10000(15, 20.00, "§e§l10,000 Solars", ""),
    SOLARS_25000(16, 40.00, "§e§l25,000 Solars", ""),

    SURVIVAL_CLAIMBLOCKS_10000(17, 5.00, Server.SURVIVAL, "§9§l10,000 Claimblocks", ""),
    SURVIVAL_CLAIMBLOCKS_25000(18, 10.00, Server.SURVIVAL, "§9§l25,000 Claimblocks", ""),
    SURVIVAL_CLAIMBLOCKS_50000(19, 17.50, Server.SURVIVAL, "§9§l50,000 Claimblocks", ""),
    SURVIVAL_HOMES_50(20, 2.50, Server.SURVIVAL, "§6§l50 Homes", ""),
    SURVIVAL_CHEST_SHOPS_5(21, 2.50, Server.SURVIVAL, "§6§l5 Chest Shops", ""),
    SURVIVAL_CHEST_SHOPS_25(22, 10.00, Server.SURVIVAL, "§6§l25 Chest Shops", ""),
    SURVIVAL_WARP_1(23, 7.50, Server.SURVIVAL, "§3§l1 Warp", "");

    private final int id;
    private final double price;
    private final Server server;
    private final String title;
    private final String[] description;

    Donation(int id, double price, String title, String... description) {
        this(id, price, null, title, description);
    }

    Donation(int id, double price, Server server, String title, String... description) {
        this.id = id;
        this.price = price;
        this.server = server;
        this.title = title;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public double getPrice() {
        return price;
    }

    public Server getServer() {
        return server;
    }

    public String getTitle() {
        return title;
    }

    public String[] getDescription() {
        return description;
    }

    public static Donation getById(int id) {
        for (Donation donation : values()) {
            if (donation.getId() == id)
                return donation;
        }
        throw new IllegalArgumentException();
    }
}
