package com.orbitmines.spigot.api.utils;

import com.orbitmines.api.utils.RandomUtils;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class ColorUtils {

    public static Color[] VALUES = {
            Color.AQUA,
            Color.BLACK,
            Color.BLUE,
            Color.FUCHSIA,
            Color.GRAY,
            Color.GREEN,
            Color.LIME,
            Color.MAROON,
            Color.NAVY,
            Color.ORANGE,
            Color.PURPLE,
            Color.RED,
            Color.SILVER,
            Color.TEAL,
            Color.WHITE,
            Color.YELLOW
    };

    public static Color random(){
        return RandomUtils.randomFrom(VALUES);
    }

    public static String color(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static Material getWoolMaterial(com.orbitmines.api.Color color) {
        return getWoolMaterial(toDyeColor(color));
    }

    public static Material getBannerMaterial(com.orbitmines.api.Color color) {
        return getBannerMaterial(toDyeColor(color));
    }

    public static Material getBedMaterial(com.orbitmines.api.Color color) {
        return getBedMaterial(toDyeColor(color));
    }

    public static Material getCarpetMaterial(com.orbitmines.api.Color color) {
        return getCarpetMaterial(toDyeColor(color));
    }

    public static Material getConcreteMaterial(com.orbitmines.api.Color color) {
        return getConcreteMaterial(toDyeColor(color));
    }

    public static Material getGlazedTerracottaMaterial(com.orbitmines.api.Color color) {
        return getGlazedTerracottaMaterial(toDyeColor(color));
    }

    public static Material getShulkerBoxMaterial(com.orbitmines.api.Color color) {
        return getShulkerBoxMaterial(toDyeColor(color));
    }

    public static Material getStainedGlassMaterial(com.orbitmines.api.Color color) {
        return getStainedGlassMaterial(toDyeColor(color));
    }

    public static Material getStainedGlassPaneMaterial(com.orbitmines.api.Color color) {
        return getStainedGlassPaneMaterial(toDyeColor(color));
    }

    public static Material getTerracottaMaterial(com.orbitmines.api.Color color) {
        return getTerracottaMaterial(toDyeColor(color));
    }

    public static Material getTulipMaterial(com.orbitmines.api.Color color) {
        return getTulipMaterial(toDyeColor(color));
    }

    public static Material getWallBannerMaterial(com.orbitmines.api.Color color) {
        return getWallBannerMaterial(toDyeColor(color));
    }

    public static Material getWoolMaterial(DyeColor dyeColor) {
        switch (dyeColor) {

            case WHITE:
                return Material.WHITE_WOOL;
            case ORANGE:
                return Material.ORANGE_WOOL;
            case MAGENTA:
                return Material.MAGENTA_WOOL;
            case LIGHT_BLUE:
                return Material.LIGHT_BLUE_WOOL;
            case YELLOW:
                return Material.YELLOW_WOOL;
            case LIME:
                return Material.LIME_WOOL;
            case PINK:
                return Material.PINK_WOOL;
            case GRAY:
                return Material.GRAY_WOOL;
            case LIGHT_GRAY:
                return Material.LIGHT_GRAY_WOOL;
            case CYAN:
                return Material.CYAN_WOOL;
            case PURPLE:
                return Material.PURPLE_WOOL;
            case BLUE:
                return Material.BLUE_WOOL;
            case BROWN:
                return Material.BROWN_WOOL;
            case GREEN:
                return Material.GREEN_WOOL;
            case RED:
                return Material.RED_WOOL;
            case BLACK:
                return Material.BLACK_WOOL;
        }
        throw new IllegalArgumentException();
    }

    public static Material getBannerMaterial(DyeColor dyeColor) {
        switch (dyeColor) {

            case WHITE:
                return Material.WHITE_BANNER;
            case ORANGE:
                return Material.ORANGE_BANNER;
            case MAGENTA:
                return Material.MAGENTA_BANNER;
            case LIGHT_BLUE:
                return Material.LIGHT_BLUE_BANNER;
            case YELLOW:
                return Material.YELLOW_BANNER;
            case LIME:
                return Material.LIME_BANNER;
            case PINK:
                return Material.PINK_BANNER;
            case GRAY:
                return Material.GRAY_BANNER;
            case LIGHT_GRAY:
                return Material.LIGHT_GRAY_BANNER;
            case CYAN:
                return Material.CYAN_BANNER;
            case PURPLE:
                return Material.PURPLE_BANNER;
            case BLUE:
                return Material.BLUE_BANNER;
            case BROWN:
                return Material.BROWN_BANNER;
            case GREEN:
                return Material.GREEN_BANNER;
            case RED:
                return Material.RED_BANNER;
            case BLACK:
                return Material.BLACK_BANNER;
        }
        throw new IllegalArgumentException();
    }

    public static Material getBedMaterial(DyeColor dyeColor) {
        switch (dyeColor) {

            case WHITE:
                return Material.WHITE_BED;
            case ORANGE:
                return Material.ORANGE_BED;
            case MAGENTA:
                return Material.MAGENTA_BED;
            case LIGHT_BLUE:
                return Material.LIGHT_BLUE_BED;
            case YELLOW:
                return Material.YELLOW_BED;
            case LIME:
                return Material.LIME_BED;
            case PINK:
                return Material.PINK_BED;
            case GRAY:
                return Material.GRAY_BED;
            case LIGHT_GRAY:
                return Material.LIGHT_GRAY_BED;
            case CYAN:
                return Material.CYAN_BED;
            case PURPLE:
                return Material.PURPLE_BED;
            case BLUE:
                return Material.BLUE_BED;
            case BROWN:
                return Material.BROWN_BED;
            case GREEN:
                return Material.GREEN_BED;
            case RED:
                return Material.RED_BED;
            case BLACK:
                return Material.BLACK_BED;
        }
        throw new IllegalArgumentException();
    }

    public static Material getCarpetMaterial(DyeColor dyeColor) {
        switch (dyeColor) {

            case WHITE:
                return Material.WHITE_CARPET;
            case ORANGE:
                return Material.ORANGE_CARPET;
            case MAGENTA:
                return Material.MAGENTA_CARPET;
            case LIGHT_BLUE:
                return Material.LIGHT_BLUE_CARPET;
            case YELLOW:
                return Material.YELLOW_CARPET;
            case LIME:
                return Material.LIME_CARPET;
            case PINK:
                return Material.PINK_CARPET;
            case GRAY:
                return Material.GRAY_CARPET;
            case LIGHT_GRAY:
                return Material.LIGHT_GRAY_CARPET;
            case CYAN:
                return Material.CYAN_CARPET;
            case PURPLE:
                return Material.PURPLE_CARPET;
            case BLUE:
                return Material.BLUE_CARPET;
            case BROWN:
                return Material.BROWN_CARPET;
            case GREEN:
                return Material.GREEN_CARPET;
            case RED:
                return Material.RED_CARPET;
            case BLACK:
                return Material.BLACK_CARPET;
        }
        throw new IllegalArgumentException();
    }

    public static Material getConcreteMaterial(DyeColor dyeColor) {
        switch (dyeColor) {

            case WHITE:
                return Material.WHITE_CONCRETE;
            case ORANGE:
                return Material.ORANGE_CONCRETE;
            case MAGENTA:
                return Material.MAGENTA_CONCRETE;
            case LIGHT_BLUE:
                return Material.LIGHT_BLUE_CONCRETE;
            case YELLOW:
                return Material.YELLOW_CONCRETE;
            case LIME:
                return Material.LIME_CONCRETE;
            case PINK:
                return Material.PINK_CONCRETE;
            case GRAY:
                return Material.GRAY_CONCRETE;
            case LIGHT_GRAY:
                return Material.LIGHT_GRAY_CONCRETE;
            case CYAN:
                return Material.CYAN_CONCRETE;
            case PURPLE:
                return Material.PURPLE_CONCRETE;
            case BLUE:
                return Material.BLUE_CONCRETE;
            case BROWN:
                return Material.BROWN_CONCRETE;
            case GREEN:
                return Material.GREEN_CONCRETE;
            case RED:
                return Material.RED_CONCRETE;
            case BLACK:
                return Material.BLACK_CONCRETE;
        }
        throw new IllegalArgumentException();
    }

    public static Material getConcretePowderMaterial(DyeColor dyeColor) {
        switch (dyeColor) {

            case WHITE:
                return Material.WHITE_CONCRETE_POWDER;
            case ORANGE:
                return Material.ORANGE_CONCRETE_POWDER;
            case MAGENTA:
                return Material.MAGENTA_CONCRETE_POWDER;
            case LIGHT_BLUE:
                return Material.LIGHT_BLUE_CONCRETE_POWDER;
            case YELLOW:
                return Material.YELLOW_CONCRETE_POWDER;
            case LIME:
                return Material.LIME_CONCRETE_POWDER;
            case PINK:
                return Material.PINK_CONCRETE_POWDER;
            case GRAY:
                return Material.GRAY_CONCRETE_POWDER;
            case LIGHT_GRAY:
                return Material.LIGHT_GRAY_CONCRETE_POWDER;
            case CYAN:
                return Material.CYAN_CONCRETE_POWDER;
            case PURPLE:
                return Material.PURPLE_CONCRETE_POWDER;
            case BLUE:
                return Material.BLUE_CONCRETE_POWDER;
            case BROWN:
                return Material.BROWN_CONCRETE_POWDER;
            case GREEN:
                return Material.GREEN_CONCRETE_POWDER;
            case RED:
                return Material.RED_CONCRETE_POWDER;
            case BLACK:
                return Material.BLACK_CONCRETE_POWDER;
        }
        throw new IllegalArgumentException();
    }

    public static Material getGlazedTerracottaMaterial(DyeColor dyeColor) {
        switch (dyeColor) {

            case WHITE:
                return Material.WHITE_GLAZED_TERRACOTTA;
            case ORANGE:
                return Material.ORANGE_GLAZED_TERRACOTTA;
            case MAGENTA:
                return Material.MAGENTA_GLAZED_TERRACOTTA;
            case LIGHT_BLUE:
                return Material.LIGHT_BLUE_GLAZED_TERRACOTTA;
            case YELLOW:
                return Material.YELLOW_GLAZED_TERRACOTTA;
            case LIME:
                return Material.LIME_GLAZED_TERRACOTTA;
            case PINK:
                return Material.PINK_GLAZED_TERRACOTTA;
            case GRAY:
                return Material.GRAY_GLAZED_TERRACOTTA;
            case LIGHT_GRAY:
                return Material.LIGHT_GRAY_GLAZED_TERRACOTTA;
            case CYAN:
                return Material.CYAN_GLAZED_TERRACOTTA;
            case PURPLE:
                return Material.PURPLE_GLAZED_TERRACOTTA;
            case BLUE:
                return Material.BLUE_GLAZED_TERRACOTTA;
            case BROWN:
                return Material.BROWN_GLAZED_TERRACOTTA;
            case GREEN:
                return Material.GREEN_GLAZED_TERRACOTTA;
            case RED:
                return Material.RED_GLAZED_TERRACOTTA;
            case BLACK:
                return Material.BLACK_GLAZED_TERRACOTTA;
        }
        throw new IllegalArgumentException();
    }

    public static Material getShulkerBoxMaterial(DyeColor dyeColor) {
        switch (dyeColor) {

            case WHITE:
                return Material.WHITE_SHULKER_BOX;
            case ORANGE:
                return Material.ORANGE_SHULKER_BOX;
            case MAGENTA:
                return Material.MAGENTA_SHULKER_BOX;
            case LIGHT_BLUE:
                return Material.LIGHT_BLUE_SHULKER_BOX;
            case YELLOW:
                return Material.YELLOW_SHULKER_BOX;
            case LIME:
                return Material.LIME_SHULKER_BOX;
            case PINK:
                return Material.PINK_SHULKER_BOX;
            case GRAY:
                return Material.GRAY_SHULKER_BOX;
            case LIGHT_GRAY:
                return Material.LIGHT_GRAY_SHULKER_BOX;
            case CYAN:
                return Material.CYAN_SHULKER_BOX;
            case PURPLE:
                return Material.PURPLE_SHULKER_BOX;
            case BLUE:
                return Material.BLUE_SHULKER_BOX;
            case BROWN:
                return Material.BROWN_SHULKER_BOX;
            case GREEN:
                return Material.GREEN_SHULKER_BOX;
            case RED:
                return Material.RED_SHULKER_BOX;
            case BLACK:
                return Material.BLACK_SHULKER_BOX;
        }
        throw new IllegalArgumentException();
    }

    public static Material getStainedGlassMaterial(DyeColor dyeColor) {
        switch (dyeColor) {

            case WHITE:
                return Material.WHITE_STAINED_GLASS;
            case ORANGE:
                return Material.ORANGE_STAINED_GLASS;
            case MAGENTA:
                return Material.MAGENTA_STAINED_GLASS;
            case LIGHT_BLUE:
                return Material.LIGHT_BLUE_STAINED_GLASS;
            case YELLOW:
                return Material.YELLOW_STAINED_GLASS;
            case LIME:
                return Material.LIME_STAINED_GLASS;
            case PINK:
                return Material.PINK_STAINED_GLASS;
            case GRAY:
                return Material.GRAY_STAINED_GLASS;
            case LIGHT_GRAY:
                return Material.LIGHT_GRAY_STAINED_GLASS;
            case CYAN:
                return Material.CYAN_STAINED_GLASS;
            case PURPLE:
                return Material.PURPLE_STAINED_GLASS;
            case BLUE:
                return Material.BLUE_STAINED_GLASS;
            case BROWN:
                return Material.BROWN_STAINED_GLASS;
            case GREEN:
                return Material.GREEN_STAINED_GLASS;
            case RED:
                return Material.RED_STAINED_GLASS;
            case BLACK:
                return Material.BLACK_STAINED_GLASS;
        }
        throw new IllegalArgumentException();
    }

    public static Material getStainedGlassPaneMaterial(DyeColor dyeColor) {
        switch (dyeColor) {

            case WHITE:
                return Material.WHITE_STAINED_GLASS_PANE;
            case ORANGE:
                return Material.ORANGE_STAINED_GLASS_PANE;
            case MAGENTA:
                return Material.MAGENTA_STAINED_GLASS_PANE;
            case LIGHT_BLUE:
                return Material.LIGHT_BLUE_STAINED_GLASS_PANE;
            case YELLOW:
                return Material.YELLOW_STAINED_GLASS_PANE;
            case LIME:
                return Material.LIME_STAINED_GLASS_PANE;
            case PINK:
                return Material.PINK_STAINED_GLASS_PANE;
            case GRAY:
                return Material.GRAY_STAINED_GLASS_PANE;
            case LIGHT_GRAY:
                return Material.LIGHT_GRAY_STAINED_GLASS_PANE;
            case CYAN:
                return Material.CYAN_STAINED_GLASS_PANE;
            case PURPLE:
                return Material.PURPLE_STAINED_GLASS_PANE;
            case BLUE:
                return Material.BLUE_STAINED_GLASS_PANE;
            case BROWN:
                return Material.BROWN_STAINED_GLASS_PANE;
            case GREEN:
                return Material.GREEN_STAINED_GLASS_PANE;
            case RED:
                return Material.RED_STAINED_GLASS_PANE;
            case BLACK:
                return Material.BLACK_STAINED_GLASS_PANE;
        }
        throw new IllegalArgumentException();
    }

    public static Material getTerracottaMaterial(DyeColor dyeColor) {
        switch (dyeColor) {

            case WHITE:
                return Material.WHITE_TERRACOTTA;
            case ORANGE:
                return Material.ORANGE_TERRACOTTA;
            case MAGENTA:
                return Material.MAGENTA_TERRACOTTA;
            case LIGHT_BLUE:
                return Material.LIGHT_BLUE_TERRACOTTA;
            case YELLOW:
                return Material.YELLOW_TERRACOTTA;
            case LIME:
                return Material.LIME_TERRACOTTA;
            case PINK:
                return Material.PINK_TERRACOTTA;
            case GRAY:
                return Material.GRAY_TERRACOTTA;
            case LIGHT_GRAY:
                return Material.LIGHT_GRAY_TERRACOTTA;
            case CYAN:
                return Material.CYAN_TERRACOTTA;
            case PURPLE:
                return Material.PURPLE_TERRACOTTA;
            case BLUE:
                return Material.BLUE_TERRACOTTA;
            case BROWN:
                return Material.BROWN_TERRACOTTA;
            case GREEN:
                return Material.GREEN_TERRACOTTA;
            case RED:
                return Material.RED_TERRACOTTA;
            case BLACK:
                return Material.BLACK_TERRACOTTA;
        }
        throw new IllegalArgumentException();
    }

    public static Material getTulipMaterial(DyeColor dyeColor) {
        switch (dyeColor) {

            case WHITE:
                return Material.WHITE_TULIP;
            case ORANGE:
                return Material.ORANGE_TULIP;
            case PINK:
                return Material.PINK_TULIP;
            case RED:
                return Material.RED_TULIP;
        }
        throw new IllegalArgumentException();
    }

    public static Material getWallBannerMaterial(DyeColor dyeColor) {
        switch (dyeColor) {

            case WHITE:
                return Material.WHITE_WALL_BANNER;
            case ORANGE:
                return Material.ORANGE_WALL_BANNER;
            case MAGENTA:
                return Material.MAGENTA_WALL_BANNER;
            case LIGHT_BLUE:
                return Material.LIGHT_BLUE_WALL_BANNER;
            case YELLOW:
                return Material.YELLOW_WALL_BANNER;
            case LIME:
                return Material.LIME_WALL_BANNER;
            case PINK:
                return Material.PINK_WALL_BANNER;
            case GRAY:
                return Material.GRAY_WALL_BANNER;
            case LIGHT_GRAY:
                return Material.LIGHT_GRAY_WALL_BANNER;
            case CYAN:
                return Material.CYAN_WALL_BANNER;
            case PURPLE:
                return Material.PURPLE_WALL_BANNER;
            case BLUE:
                return Material.BLUE_WALL_BANNER;
            case BROWN:
                return Material.BROWN_WALL_BANNER;
            case GREEN:
                return Material.GREEN_WALL_BANNER;
            case RED:
                return Material.RED_WALL_BANNER;
            case BLACK:
                return Material.BLACK_WALL_BANNER;
        }
        throw new IllegalArgumentException();
    }
    
    public static Color toBukkitColor(com.orbitmines.api.Color color) {
        switch (color) {

            case AQUA:
                return Color.AQUA;
            case BLACK:
                return Color.BLACK;
            case BLUE:
                return Color.BLUE;
            case FUCHSIA:
                return Color.FUCHSIA;
            case GRAY:
                return Color.GRAY;
            case GREEN:
                return Color.GREEN;
            case LIME:
                return Color.LIME;
            case MAROON:
                return Color.MAROON;
            case NAVY:
                return Color.NAVY;
            case ORANGE:
                return Color.ORANGE;
            case PURPLE:
                return Color.PURPLE;
            case RED:
                return Color.RED;
            case SILVER:
                return Color.SILVER;
            case TEAL:
                return Color.TEAL;
            case WHITE:
                return Color.WHITE;
            case YELLOW:
                return Color.YELLOW;
        }
        throw new IllegalArgumentException();
    }

    public static DyeColor toDyeColor(com.orbitmines.api.Color color) {
        switch (color) {

            case AQUA:
                return DyeColor.LIGHT_BLUE;
            case BLACK:
                return DyeColor.BLACK;
            case BLUE:
                return DyeColor.BLUE;
            case FUCHSIA:
                return DyeColor.PINK;
            case GRAY:
                return DyeColor.GRAY;
            case GREEN:
                return DyeColor.GREEN;
            case LIME:
                return DyeColor.LIME;
            case MAROON:
                return DyeColor.RED;
            case NAVY:
                return DyeColor.BLUE;
            case ORANGE:
                return DyeColor.ORANGE;
            case PURPLE:
                return DyeColor.PURPLE;
            case RED:
                return DyeColor.RED;
            case SILVER:
                return DyeColor.LIGHT_GRAY;
            case TEAL:
                return DyeColor.CYAN;
            case WHITE:
                return DyeColor.WHITE;
            case YELLOW:
                return DyeColor.YELLOW;
        }
        throw new IllegalStateException();
    }

    public static ChatColor toChatColor(com.orbitmines.api.Color color) {
        switch (color) {

            case AQUA:
                return ChatColor.AQUA;
            case BLACK:
                return ChatColor.BLACK;
            case BLUE:
                return ChatColor.BLUE;
            case FUCHSIA:
                return ChatColor.LIGHT_PURPLE;
            case GRAY:
                return ChatColor.DARK_GRAY;
            case GREEN:
                return ChatColor.DARK_GREEN;
            case LIME:
                return ChatColor.GREEN;
            case MAROON:
                return ChatColor.DARK_RED;
            case NAVY:
                return ChatColor.DARK_BLUE;
            case ORANGE:
                return ChatColor.GOLD;
            case PURPLE:
                return ChatColor.DARK_PURPLE;
            case RED:
                return ChatColor.RED;
            case SILVER:
                return ChatColor.GRAY;
            case TEAL:
                return ChatColor.DARK_AQUA;
            case WHITE:
                return ChatColor.WHITE;
            case YELLOW:
                return ChatColor.YELLOW;
        }
        throw new IllegalArgumentException();
    }
}
