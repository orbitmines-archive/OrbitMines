package com.orbitmines.spigot.servers.kitpvp.datapoints;

import com.orbitmines.api.Color;
import com.orbitmines.api.Language;
import com.orbitmines.spigot.api.datapoints.DataPointLoader;
import com.orbitmines.spigot.api.datapoints.DataPointSign;
import com.orbitmines.spigot.api.handlers.itembuilders.ItemBuilder;
import com.orbitmines.spigot.api.handlers.itembuilders.PotionBuilder;
import com.orbitmines.spigot.api.handlers.itembuilders.PotionItemBuilder;
import com.orbitmines.spigot.servers.kitpvp.handlers.KitPvPKit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */
public class KitPvPDataPointLobbyKitInfo extends DataPointSign {

    private Map<Location, KitInfo> kitInfo;

    public KitPvPDataPointLobbyKitInfo() {
        super("KIT_INFO", Type.GOLD_PLATE, Material.MAGENTA_WOOL);

        kitInfo = new HashMap<>();
    }

    @Override
    public boolean buildAt(DataPointLoader loader, Location location, String[] data) {
        KitInfo kitInfo;
        if (data.length >= 1) {
            try {
                kitInfo = KitInfo.valueOf(data[0]);
            } catch(IllegalArgumentException ex) {
                failureMessage = "Invalid KitInfo.";
                return false;
            }
        } else {
            failureMessage = "KitInfo not given.";
            return false;
        }

        this.kitInfo.put(location.add(0.5, 0.5, 0.5), kitInfo);
        return true;
    }

    @Override
    public boolean setup() {
        return true;
    }

    public Map<Location, KitInfo> getKitInfo() {
        return kitInfo;
    }

    public enum KitInfo {

        CLASS("Class", Color.AQUA, 1, 0, new ItemBuilder(Material.BOOK)) {
            @Override
            public String getDescription(KitPvPKit.Level kit) {
                return kit.getHandler().getKitClass().getName();
            }
        },
        MAX_HEALTH("Health", Color.RED, 2, 0, new ItemBuilder(Material.BEETROOT)) {
            @Override
            public String getDescription(KitPvPKit.Level kit) {
                return String.format("%.1f", kit.getMaxHealth());
            }
        },
        HEALTH_REGEN("Health Regen", Color.FUCHSIA, 1, 8, new PotionItemBuilder(PotionItemBuilder.Type.NORMAL, new PotionBuilder(PotionEffectType.REGENERATION, 1)).addFlag(ItemFlag.HIDE_POTION_EFFECTS)) {
            @Override
            public String getDescription(KitPvPKit.Level kit) {
                return kit.getHealthRegen().getMessage().lang(Language.ENGLISH) + " (" + String.format("%.1f", kit.getHealthRegen().getMultiplier() * 100) + "%)";
            }
        },
        KNOCKBACK_RES("Knockback Evading", Color.ORANGE, 2, 8, new ItemBuilder(Material.SHIELD)) {
            @Override
            public String getDescription(KitPvPKit.Level kit) {
                return String.format("%.1f", kit.getKnockbackResistance() * 100) + "%";
            }
        };

        protected final String name;
        protected final Color color;
        private final int row;
        private final int slot;
        private final ItemBuilder icon;

        KitInfo(String name, Color color, int row, int slot, ItemBuilder icon) {
            this.name = name;
            this.color = color;
            this.row = row;
            this.slot = slot;
            this.icon = icon;
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

        public int getRow() {
            return row;
        }

        public int getSlot() {
            return slot;
        }

        public ItemBuilder getIcon() {
            return icon.clone();
        }

        public String getDescription(KitPvPKit.Level kit) {
            throw new IllegalStateException();
        }
    }
}
