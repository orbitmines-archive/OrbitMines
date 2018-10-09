package com.orbitmines.spigot.servers.kitpvp.handlers.itembuilders;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.utils.RandomUtils;
import com.orbitmines.spigot.api.handlers.itembuilders.ItemBuilder;
import com.orbitmines.spigot.api.nms.itemstack.ItemStackNms;
import com.orbitmines.spigot.api.utils.ItemUtils;
import com.orbitmines.spigot.servers.kitpvp.handlers.KitPvPKit;
import com.orbitmines.spigot.servers.kitpvp.handlers.actives.Active;
import com.orbitmines.spigot.servers.kitpvp.handlers.passives.Passive;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class KitItemBuilder extends ItemBuilder implements KitItem {

    private final KitPvPKit.Level kit;
    private final Map<Passive, Integer> passives;
    private final Map<Active, Integer> actives;

    public KitItemBuilder(KitPvPKit.Level kit, Material material) {
        this(kit, material, 1);
    }

    public KitItemBuilder(KitPvPKit.Level kit, Material material, int amount) {
        this(kit, material, amount, null);
    }

    public KitItemBuilder(KitPvPKit.Level kit, Material material, int amount, String displayName) {
        this(kit, material, amount, displayName, (List<String>) null);
    }

    public KitItemBuilder(KitPvPKit.Level kit, Material material, int amount, String displayName, String... lore) {
        this(kit, material, amount, displayName, new ArrayList<>(Arrays.asList(lore)));
    }

    public KitItemBuilder(KitPvPKit.Level kit, KitItemBuilder itemBuilder) {
        super(itemBuilder);

        this.kit = kit;
        this.passives = new HashMap<>();
        this.actives = new HashMap<>();

        unbreakable(true);
    }

    public KitItemBuilder(KitPvPKit.Level kit, Material material, int amount, String displayName, List<String> lore) {
        super(material, amount, displayName, lore);

        this.kit = kit;
        this.passives = new HashMap<>();
        this.actives = new HashMap<>();

        unbreakable(true);
    }

    @Override
    public KitItemBuilder addPassive(Passive passive, Integer level) {
        this.passives.put(passive, level);
        return this;
    }

    @Override
    public KitItemBuilder addActive(Active active, Integer level) {
        this.actives.put(active, level);
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

                if (passive == Passive.ARROW_REGEN || passive == Passive.ATTACK_DAMAGE)
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
}
