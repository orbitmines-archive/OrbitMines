package com.orbitmines.spigot.api.handlers.scoreboard;

import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import java.util.*;

/*
* OrbitMines - @author Fadi Shawki - 29-7-2017
*/
public class ScoreboardTeam {

    private String name;
    private List<Player> players;
    private String prefix;
    private String suffix;
    private boolean allowFriendlyFire;
    private boolean canSeeFriendlyInvisibles;
    private Map<Team.Option, Team.OptionStatus> options;

    public ScoreboardTeam(String name, Player... players) {
        this(name, new ArrayList<>(Arrays.asList(players)));
    }

    public ScoreboardTeam(String name, List<Player> players) {
        this.name = name;
        this.players = players;
        this.allowFriendlyFire = true;
        this.canSeeFriendlyInvisibles = false;
        this.options = new HashMap<>();
        this.options.put(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.ALWAYS);
        this.options.put(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
    }

    public String getName() {
        return name;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public void setAllowFriendlyFire(boolean allowFriendlyFire) {
        this.allowFriendlyFire = allowFriendlyFire;
    }

    public void setCanSeeFriendlyInvisibles(boolean canSeeFriendlyInvisibles) {
        this.canSeeFriendlyInvisibles = canSeeFriendlyInvisibles;
    }

    public Map<Team.Option, Team.OptionStatus> getOptions() {
        return options;
    }

    public void update(org.bukkit.scoreboard.Scoreboard scoreboard) {
        Team t = scoreboard.getTeam(name);
        if (t == null)
            t = scoreboard.registerNewTeam(name);

        for (Player player : players) {
            t.addEntry(player.getName());
        }

        if (prefix != null)
            t.setPrefix(prefix);
        if (suffix != null)
            t.setSuffix(suffix);

        t.setAllowFriendlyFire(allowFriendlyFire);
        t.setCanSeeFriendlyInvisibles(canSeeFriendlyInvisibles);
        for (Team.Option option : options.keySet()) {
            t.setOption(option, options.get(option));
        }
    }
}
