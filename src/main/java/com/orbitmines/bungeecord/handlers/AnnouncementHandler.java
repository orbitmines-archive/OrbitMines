package com.orbitmines.bungeecord.handlers;

import com.orbitmines.api.database.*;
import com.orbitmines.api.database.tables.TableServerData;
import com.orbitmines.bungeecord.OrbitMinesBungee;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class AnnouncementHandler {

    private OrbitMinesBungee bungee;

    private List<Title> titles;

    public AnnouncementHandler(OrbitMinesBungee bungee) {
        this.bungee = bungee;

        titles = new ArrayList<>();

        if (Database.get().contains(Table.SERVER_DATA, new Column[]{ TableServerData.SERVER, TableServerData.TYPE }, new Where(TableServerData.SERVER, "BUNGEE"), new Where(TableServerData.TYPE, "TITLES"))) {
            fromDatabase();
        } else {
            titles.add(new Title("§8§lOrbit§7§lMines", "§7Subtitle"));
            Database.get().insert(Table.SERVER_DATA, TableServerData.SERVER_DATA.values("BUNGEE", "TITLES", serialize()));
        }
    }

    public List<Title> getTitles() {
        return titles;
    }

    public Title getTitle(int index) {
        return titles.get(index);
    }

    public void addTitle() {
        titles.add(new Title("§8§lOrbit§7§lMines", ""));

        update();
    }

    public void removeTitle(int index) {
        titles.remove(index);

        if (titles.size() == 0)
            titles.add(new Title("§8§lOrbit§7§lMines", "§7Subtitle"));

        update();
    }

    public void setTitle(int index, String title) {
        titles.get(index).setTitle(ChatColor.translateAlternateColorCodes('&', title).replaceAll("\\|", "").replaceAll("~", ""));

        update();
    }

    public void setSubTitle(int index, String subTitle) {
        titles.get(index).setSubTitle(ChatColor.translateAlternateColorCodes('&', subTitle).replaceAll("\\|", "").replaceAll("~", ""));

        update();
    }

    private String serialize() {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < titles.size(); i++) {
            if (i != 0)
                stringBuilder.append("~");

            Title title = titles.get(i);
            stringBuilder.append(title.getTitle()).append("|").append(title.getSubTitle());
        }

        return stringBuilder.toString().replaceAll("§", "&");
    }

    private void update() {
        Database.get().update(Table.SERVER_DATA, new Set(TableServerData.DATA, serialize()), new Where(TableServerData.SERVER, "BUNGEE"), new Where(TableServerData.TYPE, "TITLES"));
    }

    private void fromDatabase() {
        String data = Database.get().getString(Table.SERVER_DATA, TableServerData.DATA, new Where(TableServerData.SERVER, "BUNGEE"), new Where(TableServerData.TYPE, "TITLES"));
        String[] titles = ChatColor.translateAlternateColorCodes('&', data).split("~");

        for (String titleData : titles) {
            String[] title = titleData.split("\\|");
            this.titles.add(new Title(title[0], title.length != 1 ? title[1] : ""));
        }
    }

    public void send(ProxiedPlayer player) {
        if (titles.size() == 0)
            return;

        send(player, 0);
    }

    private void send(ProxiedPlayer player, int index) {
        net.md_5.bungee.api.Title title = ProxyServer.getInstance().createTitle();
        title.fadeIn(0);
        title.stay(120);
        title.fadeOut(index == titles.size() -1 ? 30 : 0);

        Title t = titles.get(index);

        title.title(new TextComponent(t.title));
        title.subTitle(new TextComponent(t.subTitle));

        title.send(player);

        if (index == titles.size() -1)
            return;

        bungee.getProxy().getScheduler().schedule(bungee, () -> send(player, index +1), 3, TimeUnit.SECONDS);
    }

    public class Title {

        private String title;
        private String subTitle;

        public Title(String title, String subTitle) {
            this.title = title;
            this.subTitle = subTitle;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }

        public void setSubTitle(String subTitle) {
            this.subTitle = subTitle;
        }

        public String getSubTitle() {
            return subTitle;
        }
    }
}
