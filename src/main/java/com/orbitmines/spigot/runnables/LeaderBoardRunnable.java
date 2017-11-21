package com.orbitmines.spigot.runnables;

import com.madblock.spigot.api.handlers.leaderboards.LeaderBoard;
import com.madblock.spigot.api.runnables.SpigotRunnable;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class LeaderBoardRunnable extends SpigotRunnable {

    public LeaderBoardRunnable() {
        super(TimeUnit.MINUTE, 10, new Time(TimeUnit.SECOND, 1));
    }

    @Override
    public void run() {
        for (LeaderBoard leaderBoard : LeaderBoard.getLeaderBoards()) {
            leaderBoard.update();
        }
    }
}
