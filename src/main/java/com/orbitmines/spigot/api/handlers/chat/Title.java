package com.orbitmines.spigot.api.handlers.chat;

import com.orbitmines.api.Message;
import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.api.handlers.OMPlayer;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/*
* OrbitMines - @author Fadi Shawki - 29-7-2017
*/
public class Title {

    private final OrbitMines plugin;

    private Message title;
    private Message subTitle;
    private int fadeIn;
    private int stay;
    private int fadeOut;

    public Title(String title, String subTitle, int fadeIn, int stay, int fadeOut) {
        this(new Message(title), new Message(subTitle), fadeIn, stay, fadeOut);
    }

    public Title(Message title, Message subTitle, int fadeIn, int stay, int fadeOut) {
        plugin = OrbitMines.getInstance();

        this.title = title;
        this.subTitle = subTitle;
        this.fadeIn = fadeIn;
        this.stay = stay;
        this.fadeOut = fadeOut;
    }

    public void send(OMPlayer player) {
        send(Collections.singletonList(player));
    }

    public void send(OMPlayer... players) {
        send(Arrays.asList(players));
    }

    public void send(Collection<? extends OMPlayer> players) {
        for (OMPlayer omp : players) {
            omp.getPlayer().sendTitle(omp.lang(title), omp.lang(subTitle), fadeIn, stay, fadeOut);
        }
    }

    public Message getTitle() {
        return title;
    }

    public void setTitle(Message title) {
        this.title = title;
    }

    public Message getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(Message subTitle) {
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
