package com.orbitmines.spigot.servers.survival.runnables;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.spigot.api.handlers.achievements.StoredProgressAchievement;
import com.orbitmines.spigot.api.runnables.SpigotRunnable;
import com.orbitmines.spigot.servers.survival.Survival;
import com.orbitmines.spigot.servers.survival.handlers.SurvivalAchievements;
import com.orbitmines.spigot.servers.survival.handlers.SurvivalPlayer;
import com.orbitmines.spigot.servers.survival.handlers.claim.Claim;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ClaimAchievementRunnable extends SpigotRunnable {

    private final Survival survival;

    private final Map<String, Integer> map;

    public ClaimAchievementRunnable(Survival survival) {
        super(TimeUnit.SECOND, 10);

        this.survival = survival;

        this.map = new HashMap<>();
    }

    @Override
    public void run() {
        map.clear();

        for (SurvivalPlayer omp : SurvivalPlayer.getSurvivalPlayers()) {
            Claim claim = survival.getClaimHandler().getClaimAt(omp.getLocation(), true, omp.getLastClaim());

            if (claim == null)
                continue;

            omp.setLastClaim(claim);

            String uuid = claim.getOwner().toString();

            if (!map.containsKey(uuid))
                map.put(uuid, 1);
            else
                map.put(uuid, map.get(uuid) + 1);
        }

        for (String uuid : map.keySet()) {
            SurvivalPlayer owner = SurvivalPlayer.getPlayer(UUID.fromString(uuid));

            if (owner == null)
                continue;

            StoredProgressAchievement handler = (StoredProgressAchievement) SurvivalAchievements.CROWDED.getHandler();
            handler.setHighest(owner, map.get(uuid), true);
        }
    }
}
