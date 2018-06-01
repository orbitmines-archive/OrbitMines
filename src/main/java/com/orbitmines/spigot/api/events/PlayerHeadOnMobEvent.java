package com.orbitmines.spigot.api.events;

import com.orbitmines.api.VipRank;
import com.orbitmines.api.database.Column;
import com.orbitmines.api.database.Database;
import com.orbitmines.api.database.Table;
import com.orbitmines.api.database.Where;
import com.orbitmines.api.database.tables.TablePlayers;
import com.orbitmines.api.utils.RandomUtils;
import com.orbitmines.api.utils.uuid.UUIDUtils;
import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.api.handlers.itembuilders.PlayerSkullBuilder;
import com.orbitmines.spigot.api.handlers.npc.MobNpc;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class PlayerHeadOnMobEvent implements Listener {

    private final OrbitMines orbitMines;

    public PlayerHeadOnMobEvent() {
        orbitMines = OrbitMines.getInstance();
    }

    @EventHandler
    public void onSpawn(EntitySpawnEvent event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof Zombie) && !(entity instanceof Skeleton))
            return;

        double chance = RandomUtils.RANDOM.nextDouble();

        VipRank vipRank;

        if (chance <= 0.01)
            vipRank = VipRank.IRON;
        else if (chance <= 0.03)
            vipRank = VipRank.GOLD;
        else if (chance <= 0.06)
            vipRank = VipRank.DIAMOND;
        else if (chance <= 0.10)
            vipRank = VipRank.EMERALD;
        else
            return;

        new BukkitRunnable() {
            @Override
            public void run() {
                if (MobNpc.getNpc(entity) == null)
                    addHead((LivingEntity) entity, vipRank);
            }
        }.runTaskLater(orbitMines, 1);
    }

    private void addHead(LivingEntity entity, VipRank vipRank) {
        List<Map<Column, String>> entries = Database.get().getEntries(Table.PLAYERS, TablePlayers.UUID, new Where(TablePlayers.VIPRANK, vipRank.toString()));

        if (entries.size() == 0)
            return;

        String name = UUIDUtils.getName(UUID.fromString(RandomUtils.randomFrom(entries).get(TablePlayers.UUID)));
        if (name == null)
            return;

        EntityEquipment ee = entity.getEquipment();
        ee.setHelmet(new PlayerSkullBuilder(() -> name).build());
        ee.setHelmetDropChance(0.001F);
    }
}
