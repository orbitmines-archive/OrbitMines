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
import java.util.Arrays;
import java.util.List;

public class KitPvPKit {

    private static List<KitPvPKit> kits = new ArrayList<>();

    protected final long id;
    protected final String name;
    protected final Color color;
    protected final ItemBuilder icon;
    protected final KitClass kitClass;
    protected final Level[] levels;

    public KitPvPKit(long id, String name, Color color, ItemBuilder icon, KitClass kitClass, Level... levels) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.icon = icon;
        this.kitClass = kitClass;
        this.levels = levels;

        kits.add(this);
    }

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

    public class Level {

        protected final int price;
        protected final Kit kit;
        protected final Attributes attributes;

        public Level(Kit kit, Attributes attributes) {
            this(0, kit, attributes);
        }

        public Level(int price, Kit kit, Attributes attributes) {
            this.price = price;
            this.kit = kit;
            this.attributes = attributes;
        }

        public KitPvPKit getHandler() {
            return KitPvPKit.this;
        }

        public int getPrice() {
            return price;
        }

        public Kit getKit() {
            return kit;
        }

        public Attributes getAttributes() {
            return attributes;
        }

        public int getLevel() {
            return Arrays.binarySearch(levels, this) + 1;
        }

        public void give(KitPvPPlayer omp) {
            kit.setItems(omp);
        }
    }
}
