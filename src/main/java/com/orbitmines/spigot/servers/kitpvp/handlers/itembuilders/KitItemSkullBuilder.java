package com.orbitmines.spigot.servers.kitpvp.handlers.itembuilders;

import com.orbitmines.api.utils.RandomUtils;
import com.orbitmines.spigot.api.handlers.itembuilders.ItemBuilder;
import com.orbitmines.spigot.api.handlers.itembuilders.PlayerSkullBuilder;
import com.orbitmines.spigot.api.handlers.scoreboard.ScoreboardString;
import com.orbitmines.spigot.api.nms.itemstack.ItemStackNms;
import com.orbitmines.spigot.api.utils.ItemUtils;
import com.orbitmines.spigot.servers.kitpvp.handlers.KitPvPKit;
import com.orbitmines.spigot.servers.kitpvp.handlers.actives.Active;
import com.orbitmines.spigot.servers.kitpvp.handlers.passives.Passive;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class KitItemSkullBuilder extends PlayerSkullBuilder implements KitItem {

    private KitPvPKit.Level kit;

    private Map<Active, Integer> actives;
    private Map<Passive, Integer> passives;

    private Set<Active> newActives;
    private Set<Active> removedActives;
    private Set<Passive> newPassives;
    private Set<Passive> removedPassives;

    public KitItemSkullBuilder(KitPvPKit.Level kit, ScoreboardString playerName) {
        this(kit, playerName, 1);
        this.kit = kit;
    }

    public KitItemSkullBuilder(KitPvPKit.Level kit, ScoreboardString playerName, int amount) {
        this(kit, playerName, amount, "");
    }

    public KitItemSkullBuilder(KitPvPKit.Level kit, ScoreboardString playerName, int amount, String displayName) {
        this(kit, playerName, amount, displayName, new String[]{});
    }

    public KitItemSkullBuilder(KitPvPKit.Level kit, ScoreboardString playerName, int amount, String displayName, String... lore) {
        super(playerName, amount, displayName, lore);
        this.kit = kit;

        this.actives = new HashMap<>();
        this.passives = new HashMap<>();

        this.unbreakable(true);
    }

    public KitItemSkullBuilder(KitItemSkullBuilder builder) {
        super(builder.getPlayerName(), builder.getAmount(), builder.getDisplayName(), builder.getLore());
        this.kit = builder.kit;

        this.actives = new HashMap<>();
        this.passives = new HashMap<>();

        this.unbreakable(true);
    }

    @Override
    public Map<Passive, Integer> getPassives() {
        return passives;
    }

    @Override
    public Map<Active, Integer> getActives() {
        return actives;
    }

    @Override
    public KitItemSkullBuilder addPassive(Passive passive, Integer integer) {
        this.passives.put(passive, integer);
        return this;
    }

    @Override
    public KitItemSkullBuilder addActive(Active active, Integer integer) {
        this.actives.put(active, integer);
        return this;
    }

    @Override
    public KitItemSkullBuilder applyRemovedActive(Set removedActives) {
        this.removedActives = removedActives;
        return this;
    }

    @Override
    public KitItemSkullBuilder applyNewActives(Set newActives) {
        this.newActives = newActives;
        return this;
    }

    @Override
    public KitItemSkullBuilder applyRemovedPassive(Set removedPassives) {
        this.removedActives = removedPassives;
        return this;
    }

    @Override
    public KitItemSkullBuilder applyNewPassive(Set newPassives) {
        this.newPassives = newPassives;
        return this;
    }

    @Override
    public ItemStack build() {
        if (this.displayName == null) {
            if (actives.size() == 0) {
                this.displayName = kit.getHandler().getDisplayName() + "'s " + ItemUtils.getName(material);
            } else {
                Active active = RandomUtils.randomFrom(this.actives.keySet());
                this.displayName = active.getColor().getChatColor() + "§l" + active.getName();
            }
        }

        if (this.passives.size() != 0 || this.actives.size() != 0)
            glow();

        ItemStack item = super.build();

        ItemStackNms nms = kit.getHandler().getKitPvP().getOrbitMines().getNms().customItem();

        /* Apply passives */
        {
            List<Passive> ordered = new ArrayList<>(this.passives.keySet());
            ordered.sort(Comparator.comparing(Enum::ordinal));
            for (Passive passive : ordered) {
                int level = this.passives.get(passive);

                item = passive.apply(nms, item, level);

                ItemMeta meta = item.getItemMeta();
                List<String> lore = meta.getLore() != null ? meta.getLore() : new ArrayList<>();

                if (passive.hasBreakLine())
                    lore.add("");

                lore.add(passive.getDisplayName(level));
                lore.addAll(Arrays.asList(passive.getDescription(level)));
                if (passive.isStackable())
                    lore.add("  §2§o[stackable]");

                meta.setLore(lore);
                item.setItemMeta(meta);
            }
        }
        /* Apply actives */
        {
            List<Active> ordered = new ArrayList<>(this.actives.keySet());
            ordered.sort(Comparator.comparing(Enum::ordinal));
            for (Active active : ordered) {
                int level = this.actives.get(active);

                item = active.apply(nms, item, level);

                ItemMeta meta = item.getItemMeta();
                List<String> lore = meta.getLore() != null ? meta.getLore() : new ArrayList<>();

                lore.add(active.getDisplayName(level));
                lore.addAll(Arrays.asList(active.getDescription(level)));

                meta.setLore(lore);
                item.setItemMeta(meta);
            }
        }

        return item;
    }

    @Override
    protected ItemStack modify(ItemStack itemStack) {
        return super.modify(itemStack);
    }

    @Override
    public ItemBuilder clone() {
        return new KitItemSkullBuilder(this);
    }
}