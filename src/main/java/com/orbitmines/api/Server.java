package com.orbitmines.api;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public enum Server {

    KITPVP("KitPvP", Color.RED),
    PRISON("Prison", Color.MAROON),
    CREATIVE("Creative", Color.FUCHSIA),
    HUB("Hub", Color.TEAL),
    SURVIVAL("Survival", Color.LIME),
    SKYBLOCK("SkyBlock", Color.PURPLE),
    FOG("FoG", Color.YELLOW),
    MINIGAMES("MiniGames", Color.WHITE);

    private final String name;
    private final Color color;

    Server(String name, Color color) {
        this.name = name;
        this.color = color;
        this.players = 0;
        this.maxPlayers = 0;
        this.status = Status.OFFLINE;
    }

    public String getName() {
        return name;
    }

    public Color getColor() {
        return color;
    }

    public int getPlayers() {
        return players;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public Status getStatus() {
        return status;
    }

    public enum Status {

        ONLINE("Online", Color.LIME),
        OFFLINE("Offline", Color.RED),
        MAINTENANCE("Maintenance", Color.FUCHSIA);

        private final String name;
        private final Color color;

        Status(String name, Color color) {
            this.name = name;
            this.color = color;
        }

        public String getName() {
            return name;
        }

        public Color getColor() {
            return color;
        }
    }
}
