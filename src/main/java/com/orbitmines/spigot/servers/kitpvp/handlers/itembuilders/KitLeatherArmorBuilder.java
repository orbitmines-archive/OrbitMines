package com.orbitmines.spigot.servers.kitpvp.handlers.itembuilders;

import com.orbitmines.api.utils.RandomUtils;
import com.orbitmines.spigot.api.handlers.itembuilders.ItemBuilder;
import com.orbitmines.spigot.api.handlers.itembuilders.LeatherArmorBuilder;
import com.orbitmines.spigot.api.nms.itemstack.ItemStackNms;
import com.orbitmines.spigot.api.utils.ItemUtils;
import com.orbitmines.spigot.servers.kitpvp.handlers.KitPvPKit;
import com.orbitmines.spigot.servers.kitpvp.handlers.actives.Active;
import com.orbitmines.spigot.servers.kitpvp.handlers.passives.Passive;
import org.bukkit.Color;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

/*
* OrbitMines - @author Fadi Shawki - 29-7-2017
*/
public class KitLeatherArmorBuilder extends LeatherArmorBuilder implements KitItem {

    private final KitPvPKit.Level kit;
    private final Map<Passive, Integer> passives;
    private final Map<Active, Integer> actives;

    private Set<Passive> newPassives;
    private Set<Passive> removedPassives;
    private Set<Active> newActives;
    private Set<Active> removedActives;

    public KitLeatherArmorBuilder(KitPvPKit.Level kit, Type type) {
        this(kit, type, null, 1);
    }

    public KitLeatherArmorBuilder(KitPvPKit.Level kit, Type type, Color color) {
        this(kit, type, color, 1);
    }

    public KitLeatherArmorBuilder(KitPvPKit.Level kit, Type type, Color color, int amount) {
        this(kit, type, color, amount, null);
    }

    public KitLeatherArmorBuilder(KitPvPKit.Level kit, Type type, Color color, int amount, String displayName) {
        this(kit, type, color, amount, displayName, (List<String>) null);
    }

    public KitLeatherArmorBuilder(KitPvPKit.Level kit, Type type, Color color, int amount, String displayName, String... lore) {
        this(kit, type, color, amount, displayName, new ArrayList<>(Arrays.asList(lore)));
    }

    public KitLeatherArmorBuilder(KitPvPKit.Level kit, Type type, Color color, int amount, String displayName, List<String> lore) {
        super(type, color, amount, displayName, lore);

        this.kit = kit;
        this.passives = new HashMap<>();
        this.actives = new HashMap<>();

        unbreakable(true);
    }

    public KitLeatherArmorBuilder(KitPvPKit.Level kit, KitLeatherArmorBuilder builder) {
        super(builder);

        this.kit = kit;
        this.passives = new HashMap<>(builder.passives);
        this.actives = new HashMap<>(builder.actives);

        unbreakable(true);
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
    public KitLeatherArmorBuilder addPassive(Passive passive, Integer level) {
        this.passives.put(passive, level);
        return this;
    }

    @Override
    public KitLeatherArmorBuilder addActive(Active active, Integer level) {
        this.actives.put(active, level);
        return this;
    }

    @Override
    public KitLeatherArmorBuilder applyNewPassive(Set newPassives) {
        this.newPassives = newPassives;
        return this;
    }

    @Override
    public KitLeatherArmorBuilder applyRemovedPassive(Set removedPassives) {
        this.removedPassives = removedPassives;
        return this;
    }

    @Override
    public KitLeatherArmorBuilder applyNewActives(Set newActives) {
        this.newActives = newActives;
        return this;
    }

    @Override
    public KitLeatherArmorBuilder applyRemovedActive(Set removedActives) {
        this.removedActives = removedActives;
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
        return new KitLeatherArmorBuilder(this.kit, this);
    }
}
