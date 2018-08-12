package com.orbitmines.api;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */
public enum Donation {

    /* Prices are in EUR */

    UNKNOWN(0, 0.00, Icon.OM_ICON, "§8§lUNKNOWN", ""),
    DONATION(1, 0.00, Icon.OM_ICON, "§3§lDonation", ""),

    VIP_IRON(2, 5.00, Icon.VIP_IRON, VipRank.IRON.getDisplayName(), ""),
    VIP_GOLD(3, 10.00, Icon.VIP_GOLD, VipRank.GOLD.getDisplayName(), ""),
    VIP_DIAMOND(4, 25.00, Icon.VIP_DIAMOND,  VipRank.DIAMOND.getDisplayName(), ""),
    VIP_EMERALD(5, 50.00, Icon.VIP_EMERALD, VipRank.EMERALD.getDisplayName(), ""),

    VIP_UPGRADE_IRON_GOLD(6, 5.00, Icon.VIP_UPGRADE_IRON_GOLD, VipRank.IRON.getDisplayName() + "§r §7» " + VipRank.GOLD.getDisplayName(), ""),
    VIP_UPGRADE_IRON_DIAMOND(7, 20.00, Icon.VIP_UPGRADE_IRON_DIAMOND, VipRank.IRON.getDisplayName() + "§r §7» " + VipRank.DIAMOND.getDisplayName(), ""),
    VIP_UPGRADE_IRON_EMERALD(8, 45.00, Icon.VIP_UPGRADE_IRON_EMERALD, VipRank.IRON.getDisplayName() + "§r §7» " + VipRank.EMERALD.getDisplayName(), ""),
    VIP_UPGRADE_GOLD_DIAMOND(9, 15.00, Icon.VIP_UPGRADE_GOLD_DIAMOND, VipRank.GOLD.getDisplayName() + "§r §7» " + VipRank.DIAMOND.getDisplayName(), ""),
    VIP_UPGRADE_GOLD_EMERALD(10, 40.00, Icon.VIP_UPGRADE_GOLD_EMERALD, VipRank.GOLD.getDisplayName() + "§r §7» " + VipRank.EMERALD.getDisplayName(), ""),
    VIP_UPGRADE_DIAMOND_EMERALD(11, 25.00, Icon.VIP_UPGRADE_DIAMOND_EMERALD, VipRank.DIAMOND.getDisplayName() + "§r §7» " + VipRank.EMERALD.getDisplayName(), ""),

    SOLARS_1500(12, 5.00, Icon.OM_ICON, "§e§l1,500 Solars", ""),
    SOLARS_4000(13, 10.00, Icon.OM_ICON, "§e§l4,000 Solars", ""),
    SOLARS_12500(14, 25.00, Icon.OM_ICON, "§e§l112,500 Solars", ""),

    SURVIVAL_CLAIMBLOCKS_10000(15, 5.00, Server.SURVIVAL, Icon.SURVIVAL, "§9§l10,000 Claimblocks", ""),
    SURVIVAL_CLAIMBLOCKS_25000(16, 10.00, Server.SURVIVAL, Icon.SURVIVAL, "§9§l25,000 Claimblocks", ""),
    SURVIVAL_CLAIMBLOCKS_75000(17, 25.00, Server.SURVIVAL, Icon.SURVIVAL, "§9§l75,000 Claimblocks", ""),

    @Deprecated
    SURVIVAL_CLAIMBLOCKS_50000(18, 17.50, Server.SURVIVAL, Icon.SURVIVAL, "§9§l50,000 Claimblocks", ""),

    SURVIVAL_HOMES_25(19, 2.50, Server.SURVIVAL, Icon.SURVIVAL, "§6§l25 Homes", ""),
    SURVIVAL_HOMES_100(20, 7.50, Server.SURVIVAL, Icon.SURVIVAL, "§6§l100 Homes", ""),

    SURVIVAL_WARP_1(21, 7.50, Server.SURVIVAL, Icon.SURVIVAL, "§3§l1 Warp", "");

    private final int id;
    private final double price;
    private final Server server;
    private final Icon icon;
    private final String title;
    private final String[] description;

    Donation(int id, double price, Icon icon, String title, String... description) {
        this(id, price, null, icon, title, description);
    }

    Donation(int id, double price, Server server, Icon icon, String title, String... description) {
        this.id = id;
        this.price = price;
        this.server = server;
        this.icon = icon;
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

    public Icon getIcon() {
        return icon;
    }

    public String getTitle() {
        return title;
    }

    public String[] getDescription() {
        return description;
    }

    public VipRank getRank() {
        switch (this) {

            case VIP_IRON:
                return VipRank.IRON;
            case VIP_GOLD:
            case VIP_UPGRADE_IRON_GOLD:
                return VipRank.GOLD;
            case VIP_DIAMOND:
            case VIP_UPGRADE_IRON_DIAMOND:
            case VIP_UPGRADE_GOLD_DIAMOND:
                return VipRank.DIAMOND;
            case VIP_EMERALD:
            case VIP_UPGRADE_IRON_EMERALD:
            case VIP_UPGRADE_GOLD_EMERALD:
            case VIP_UPGRADE_DIAMOND_EMERALD:
                return VipRank.EMERALD;
        }
        return null;
    }

    public VipRank getUpgradedFrom() {
        switch (this) {

            case VIP_UPGRADE_IRON_GOLD:
            case VIP_UPGRADE_IRON_DIAMOND:
            case VIP_UPGRADE_IRON_EMERALD:
                return VipRank.IRON;
            case VIP_UPGRADE_GOLD_DIAMOND:
            case VIP_UPGRADE_GOLD_EMERALD:
                return VipRank.GOLD;
            case VIP_UPGRADE_DIAMOND_EMERALD:
                return VipRank.DIAMOND;
        }
        return null;
    }

    public static Donation getById(int id) {
        for (Donation donation : values()) {
            if (donation.getId() == id)
                return donation;
        }
        return Donation.UNKNOWN;
    }

    public enum Icon {

        OM_ICON("https://i.imgur.com/E1oDT11.png"),

        SURVIVAL("https://i.imgur.com/lRqhPy1.png"),
        KITPVP("https://i.imgur.com/P2ytvwc.png"),
        CREATIVE("https://i.imgur.com/b1MACP9.png"),
        MINIGAMES("https://i.imgur.com/2xTgLAg.png"),
        SKYBLOCK("https://i.imgur.com/OI1mz7Z.png"),
        PRISON("https://i.imgur.com/GhGTIDH.png"),
        FOG("https://i.imgur.com/YruBbXy.png"),

        VIP_IRON("https://i.imgur.com/YGr0ahP.png"),
        VIP_GOLD("https://i.imgur.com/UzUGCWn.png"),
        VIP_DIAMOND("https://i.imgur.com/uNavArR.png"),
        VIP_EMERALD("https://i.imgur.com/4po6vti.png"),

        VIP_UPGRADE_IRON_GOLD("https://i.imgur.com/7KHWiX2.png"),
        VIP_UPGRADE_IRON_DIAMOND("https://i.imgur.com/XnQmsc3.png"),
        VIP_UPGRADE_IRON_EMERALD("https://i.imgur.com/ZK4LnwH.png"),

        VIP_UPGRADE_GOLD_DIAMOND("https://i.imgur.com/Kp9rGfy.png"),
        VIP_UPGRADE_GOLD_EMERALD("https://i.imgur.com/nNhHa04.png"),

        VIP_UPGRADE_DIAMOND_EMERALD("https://i.imgur.com/EASmD0X.png");

        private final String url;

        Icon(String url) {
            this.url = url;
        }

        public String getUrl() {
            return url;
        }
    }
}
