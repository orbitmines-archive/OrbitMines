package com.orbitmines.spigot.servers.kitpvp.handlers.itembuilders;

import com.orbitmines.spigot.api.handlers.itembuilders.ItemBuilder;
import com.orbitmines.spigot.api.handlers.scoreboard.ScoreboardString;
import com.orbitmines.spigot.servers.kitpvp.handlers.KitPvPKit;
import com.orbitmines.spigot.servers.kitpvp.handlers.Passive;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/*
* OrbitMines - @author Fadi Shawki - 29-7-2017
*/
public class KitPlayerSkullBuilder extends ItemBuilder implements KitItem {

    private final KitPvPKit.Level kit;
    private final Map<Passive, Integer> passives;

    public KitPlayerSkullBuilder(KitPvPKit.Level kit, ScoreboardString playerName) {
        this(kit, playerName, 1);
    }

    public KitPlayerSkullBuilder(KitPvPKit.Level kit, ScoreboardString playerName, int amount) {
        this(kit, playerName, amount, null);
    }

    public KitPlayerSkullBuilder(KitPvPKit.Level kit, ScoreboardString playerName, int amount, String displayName) {
        this(kit, playerName, amount, displayName, (List<String>) null);
    }

    public KitPlayerSkullBuilder(KitPvPKit.Level kit, ScoreboardString playerName, int amount, String displayName, String... lore) {
        this(kit, playerName, amount, displayName, new ArrayList<>(Arrays.asList(lore)));
    }

    public KitPlayerSkullBuilder(KitPvPKit.Level kit, ScoreboardString playerName, int amount, String displayName, List<String> lore) {
        super(Material.PLAYER_HEAD, amount, displayName, lore);

        this.kit = kit;
        this.passives = new HashMap<>();
    }

    @Override
    public KitPlayerSkullBuilder addPassive(Passive passive, Integer level) {
        this.passives.put(passive, level);
        return this;
    }

    @Override
    public ItemStack build() {
        return super.build();
    }

    @Override
    protected ItemStack modify(ItemStack itemStack) {
        return super.modify(itemStack);
    }
}
