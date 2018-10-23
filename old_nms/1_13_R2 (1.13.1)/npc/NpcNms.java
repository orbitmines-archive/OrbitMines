package com.orbitmines.spigot.api.nms.npc;

import org.bukkit.entity.Entity;

/**
 * Created by Fadi on 30-4-2016.
 */
public interface NpcNms {

    void setNoAI(Entity entity);

    float[] handleMovement(Entity rideable, float speed, float backMultiplier, float sideMultiplier, float walkHeight);

    void handleJump(Entity rideable, float jumpHeight);

}
