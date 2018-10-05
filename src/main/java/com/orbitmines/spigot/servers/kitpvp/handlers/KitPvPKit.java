package com.orbitmines.spigot.servers.kitpvp.handlers;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.Color;
import com.orbitmines.spigot.api.handlers.itembuilders.ItemBuilder;
import com.orbitmines.spigot.api.handlers.kit.Kit;
import com.orbitmines.spigot.servers.kitpvp.Attributes;
import com.orbitmines.spigot.servers.kitpvp.KitClass;

import java.util.ArrayList;
import java.util.List;

public abstract class KitPvPKit {

    private static List<KitPvPKit> kits = new ArrayList<>();

    protected final long id;
    protected final String name;
    protected final Color color;
    protected final ItemBuilder icon;
    protected final KitClass kitClass;
    protected final Level[] levels;

    public KitPvPKit(long id, String name, Color color, ItemBuilder icon, KitClass kitClass) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.icon = icon;
        this.kitClass = kitClass;
        this.levels = registerLevels();

        kits.add(this);
    }

    protected abstract Level[] registerLevels();

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return color.getChatColor() + "§l" + name;
    }

    public Color getColor() {
        return color;
    }

    public ItemBuilder getIcon() {
        return icon.clone();
    }

    public KitClass getKitClass() {
        return kitClass;
    }

    public Level[] getLevels() {
        return levels;
    }

    public Level getLevel(int level) {
        try {
            return levels[level - 1];
        } catch(IndexOutOfBoundsException ex) {
            return null;
        }
    }

    public void give(KitPvPPlayer omp, int level) {
        Level lvl = getLevel(level);

        if (lvl != null)
            lvl.give(omp);
    }

    public static List<KitPvPKit> getKits() {
        return kits;
    }

    public static KitPvPKit getKit(long kitId) {
        for (KitPvPKit kit : kits) {
            if (kit.getId() == kitId)
                return kit;
        }
        return null;
    }

    public abstract class Level implements Attributes {

        protected final Kit kit;

        public Level() {
            this.kit = registerKit();
        }

        public abstract int getPrice();

        protected abstract Kit registerKit();

        public KitPvPKit getHandler() {
            return KitPvPKit.this;
        }

        public Kit getKit() {
            return kit;
        }

        public int getLevel() {
            for (int l = 0; l < levels.length; l++) {
                if (levels[l] == this)
                    return l + 1;
            }
            throw new ArrayIndexOutOfBoundsException();
        }

        public void give(KitPvPPlayer omp) {
            kit.setItems(omp);
        }
    }
}
