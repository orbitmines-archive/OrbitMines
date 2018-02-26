package com.orbitmines.spigot.api.handlers.chat;

import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.api.handlers.OMPlayer;

import java.util.Collection;
import java.util.List;

/*
* OrbitMines - @author Fadi Shawki - 29-7-2017
*/
public class Title {

    private final OrbitMines orbitMines;

    private String title;
    private String subTitle;
    private int fadeIn;
    private int stay;
    private int fadeOut;

    public Title(String title, String subTitle, int fadeIn, int stay, int fadeOut) {
        orbitMines = OrbitMines.getInstance();

        this.title = title;
        this.subTitle = subTitle;
        this.fadeIn = fadeIn;
        this.stay = stay;
        this.fadeOut = fadeOut;
    }

    public void send(OMPlayer player) {
        if (player.isLoggedIn())
            orbitMines.getNms().title().send(player.getPlayer(), this);
    }

    public void send(OMPlayer... players) {
        for (OMPlayer player : players) {
            send(player);
        }
    }

    public void send(List<OMPlayer> players) {
        for (OMPlayer player : players) {
            send(player);
        }
    }

    public void send(Collection<? extends OMPlayer> players) {
        for (OMPlayer player : players) {
            send(player);
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public int getFadeIn() {
        return fadeIn;
    }

    public void setFadeIn(int fadeIn) {
        this.fadeIn = fadeIn;
    }

    public int getStay() {
        return stay;
    }

    public void setStay(int stay) {
        this.stay = stay;
    }

    public int getFadeOut() {
        return fadeOut;
    }

    public void setFadeOut(int fadeOut) {
        this.fadeOut = fadeOut;
    }

    public Title copy() {
        return new Title(title, subTitle, fadeIn, stay, fadeOut);
    }
}
