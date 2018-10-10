package com.orbitmines.spigot.servers.kitpvp.handlers.gui;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.Color;
import com.orbitmines.api.utils.NumberUtils;
import com.orbitmines.spigot.api.handlers.GUI;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.itembuilders.ItemBuilder;
import com.orbitmines.spigot.api.handlers.itembuilders.PlayerSkullBuilder;
import com.orbitmines.spigot.api.handlers.itembuilders.PotionBuilder;
import com.orbitmines.spigot.api.handlers.itembuilders.PotionItemBuilder;
import com.orbitmines.spigot.api.handlers.kit.Kit;
import com.orbitmines.spigot.api.utils.ItemUtils;
import com.orbitmines.spigot.servers.kitpvp.KitPvP;
import com.orbitmines.spigot.servers.kitpvp.datapoints.KitPvPDataPointLobbyKitInfo;
import com.orbitmines.spigot.servers.kitpvp.handlers.KitPvPKit;
import com.orbitmines.spigot.servers.kitpvp.handlers.KitPvPPlayer;
import com.orbitmines.spigot.servers.kitpvp.handlers.itembuilders.KitItem;
import com.orbitmines.spigot.servers.kitpvp.handlers.passives.Passive;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.*;

public class KitInfoGUI extends GUI {

    private final KitPvP kitPvP;
    private final KitPvPKit handler;
    private final KitPvPKit.Level kit;
    private final int level;

    public KitInfoGUI(KitPvP kitPvP, KitPvPKit kit, int level) {
        newInventory(54, "§0§l" + kit.getName() + " (Level " + level + ")");

        this.kitPvP = kitPvP;
        this.handler = kit;
        this.kit = kit.getLevel(level);
        this.level = level;
    }

    @Override
    protected boolean onOpen(OMPlayer player) {
        KitPvPPlayer omp = (KitPvPPlayer) player;

        if (omp.getSelectedKit() != null || omp.isSpectator()) {
            omp.sendMessage("Kit", Color.RED, "Je kan de §c§lKit Selector§7 niet gebruiken in de arena!", "You can't use the §c§lKit Selector§7 in the arena!");
            return false;
        }

        add(5, 0, new ItemInstance(new PlayerSkullBuilder(() -> "Red Arrow Left", 1, omp.lang("§c« Terug naar Kit Selector", "§c« Back to Kit Selector")).setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjg0ZjU5NzEzMWJiZTI1ZGMwNThhZjg4OGNiMjk4MzFmNzk1OTliYzY3Yzk1YzgwMjkyNWNlNGFmYmEzMzJmYyJ9fX0=").build()) {
            @Override
            public void onClick(InventoryClickEvent event, OMPlayer omp) {
                new KitSelectorGUI(kitPvP).open(omp);
            }
        });

        int unlockedLevel = omp.getKitData(handler).getUnlockedLevel();
        if (unlockedLevel >= level) {
            /* Unlocked */
            add(5, 8, new EmptyItemInstance(new ItemBuilder(Material.LIME_TERRACOTTA, level, "§a§l" + omp.lang("ONTGRENDELD", "UNLOCKED")).build()));
        } else if (unlockedLevel == level - 1) {
            /* Purchase */
            add(5, 8, new ItemInstance(new ItemBuilder(Material.RED_TERRACOTTA, level, "§4§l" + omp.lang("VERGRENDELD", "LOCKED"), "§7Price: §6§l" + NumberUtils.locale(kit.getPrice()), "", omp.lang("§aKlik hier om te kopen.", "§aClick here to buy.")).build()) {
                @Override
                public void onClick(InventoryClickEvent event, OMPlayer player) {
                    KitPvPPlayer omp = (KitPvPPlayer) player;

                    if (omp.getCoins() >= kit.getPrice()) {
                        omp.removeCoins(kit.getPrice());
                        omp.getKitData(handler).setUnlockedLevel(level);

                        omp.playSound(Sound.ENTITY_PLAYER_LEVELUP);

                        reopen(omp);
                    } else {
                        omp.playSound(Sound.ENTITY_ENDERMAN_SCREAM);
                        omp.sendMessage("Shop", Color.RED, "Je hebt niet genoeg §6§lCoins§7.", "You don't have enough §6§lCoins§7.");
                    }
                }
            });
        } else {
            /* Can't purchase */
            add(5, 8, new EmptyItemInstance(new ItemBuilder(Material.GRAY_TERRACOTTA, level, "§8§l" + omp.lang("NIET BESCHIKBAAR", "UNAVAILABLE"), "§8Price: §8§l" + NumberUtils.locale(kit.getPrice())).build()));
        }

        /* Level Buttons */
        {
            KitPvPKit.Level[] levels = handler.getLevels();
            int size = levels.length;

            /* In order to center  */
            int slot = 4 - (size % 2 == 0 ? size / 2 : (size - 1) / 2);

            for (int i = 0; i < size; i++) {
                /* Skip middle slot whenever a the amount of types is even to center types correctly */
                if (slot == 4 && size % 2 == 0) {
                    clear(3, slot);
                    slot++;
                }

                if (i < size) {
                    KitPvPKit.Level kit = levels[i];
                    int level = kit.getLevel();

                    ItemBuilder item;

                    if (omp.getKitData(handler).getUnlockedLevel() >= level) {
                        /* Unlocked */
                        item = new ItemBuilder(Material.LIME_DYE, level);

                        item.setDisplayName("§a§lLvl " + level + " §a§l" + omp.lang("ONTGRENDELD", "UNLOCKED"));
                    } else {
                        item = new ItemBuilder(Material.GRAY_DYE, level);

                        /* Locked */
                        item.setDisplayName("§a§lLvl " + level + " §4§l" + omp.lang("VERGRENDELD", "LOCKED"));
                    }

                    item.addLore("");

                    if (level == this.level) {
                        item.glow();
                        item.addLore(omp.lang("§cAl geselecteerd.", "§cAlready selected."));
                    } else{
                        item.addLore(omp.lang("§aKlik hier om te selecteren.", "§aClick here to select."));
                    }

                    add(5, slot, new ItemInstance(item.build()) {
                        @Override
                        public void onClick(InventoryClickEvent event, OMPlayer omp) {
                            new KitInfoGUI(kitPvP, handler, level).open(omp);
                        }
                    });
                } else {
                    clear(5, slot);
                }

                slot++;
            }
        }
        /* Point Effects */
        {
            List<PotionEffect> potionEffects = kit.getKit().getPotionEffects();
            int size = potionEffects.size();

            /* In order to center  */
            int slot = 4 - (size % 2 == 0 ? size / 2 : (size - 1) / 2);

            for (int i = 0; i < size; i++) {
                /* Skip middle slot whenever a the amount of types is even to center types correctly */
                if (slot == 4 && size % 2 == 0) {
                    clear(3, slot);
                    slot++;
                }

                if (i < size) {
                    PotionEffect effect = potionEffects.get(i);

                    PotionItemBuilder item = new PotionItemBuilder(PotionItemBuilder.Type.NORMAL, new PotionBuilder(effect.getType(), effect.getAmplifier()), true, effect.getAmplifier() + 1, (ItemUtils.POSITIVE_EFFECTS.contains(effect.getType()) ? "§a§l" : "§c§l") + ItemUtils.getName(effect.getType()) + " " + NumberUtils.toRoman(effect.getAmplifier() + 1));

                    //TODO NEW WHEN CHANGES IS LEVELS

                    add(4, slot, new EmptyItemInstance(item.build()));
                } else {
                    clear(4, slot);
                }

                slot++;
            }
        }

        /* Info */
        for (KitPvPDataPointLobbyKitInfo.KitInfo info : KitPvPDataPointLobbyKitInfo.KitInfo.values()) {
            add(info.getRow(), info.getSlot(), new EmptyItemInstance(info.getIcon().setDisplayName(info.getDisplayName()).addLore("§7§o" + info.getDescription(kit)).build()));
        }

        /* Contents */
        Kit kit = this.kit.getKit();

        setItem(0, 4, kit.getHelmet(), Type.HELMET);
        setItem(1, 4, kit.getChestplate(), Type.CHESTPLATE);
        setItem(2, 4, kit.getLeggings(), Type.LEGGINGS);
        setItem(3, 4, kit.getBoots(), Type.BOOTS);

        int count = kit.contentItems();

        int row = 0;
        int slot = 2;
        for (int i = 0; i < count; i++) {
            setItem(row, slot, kit.getItemNotNull(i), Type.CONTENT);

            if (slot == 2) {
                slot = 6;
            } else {
                slot = 2;
                row++;
            }
        }

        return true;
    }

    private void setItem(int row, int slot, ItemBuilder itemBuilder, Type type) {
        if (itemBuilder == null)
            return;

        if (this.level == 1) {
            add(row, slot, new EmptyItemInstance(itemBuilder.build()));
            return;
        }

        ItemBuilder item = itemBuilder.clone();

        /*
            Show added to new level
         */
        KitPvPKit.Level prevLevel = handler.getLevel(this.level - 1);
        Kit kit = prevLevel.getKit();

        ItemBuilder prevItem;
        switch (type) {

            case HELMET:
                prevItem = kit.getHelmet();
                break;
            case CHESTPLATE:
                prevItem = kit.getChestplate();
                break;
            case LEGGINGS:
                prevItem = kit.getLeggings();
                break;
            case BOOTS:
                prevItem = kit.getBoots();
                break;
            case CONTENT:
                prevItem = kit.getItem(this.kit.getKit().index(itemBuilder));
                break;
            default:
                throw new NullPointerException();
        }
        ItemStack exampleItem = item.build();

        if (prevItem == null) {
            /* Completely new item */
            item.setDisplayName("§a§l+NEW! " + exampleItem.getItemMeta().getDisplayName());
        } else {
            /* Clone it */
            prevItem = prevItem.clone();
            ItemStack examplePrevItem = prevItem.build();

            if (item.getMaterial() != prevItem.getMaterial()) {
                item.setDisplayName("§a§l+NEW! " + exampleItem.getItemMeta().getDisplayName());
                item.addLore("§c§l§m" + ChatColor.stripColor(examplePrevItem.getItemMeta().getDisplayName()));
            } else if (item.getAmount() != prevItem.getAmount()) {
                int diff = item.getAmount() - prevItem.getAmount();

                if (diff > 0)
                    item.addLore("§a§l+" + diff + "x " + exampleItem.getItemMeta().getDisplayName());
                else
                    item.addLore("§c§l" + diff + "x " + exampleItem.getItemMeta().getDisplayName());
            }

            KitItem kitItem = (KitItem) item;
            KitItem kitItemPrev = (KitItem) prevItem;

            /* Passives */
            Map<Passive, Integer> passives = new HashMap<>(kitItem.getPassives());
            Map<Passive, Integer> passivesPrev = new HashMap<>(kitItemPrev.getPassives());

            Set<Passive> newPassives = new HashSet<>();
            Set<Passive> removedPassives = new HashSet<>();

            for (Passive passive : passives.keySet()) {
                if (!passivesPrev.containsKey(passive)) {
                    newPassives.add(passive);
                } else if (!passivesPrev.get(passive).equals(passives.get(passive))) {
                    newPassives.add(passive);
                    item.addLore("§c§m" + ChatColor.stripColor(passive.getDisplayName(passivesPrev.get(passive))));
                }
            }
            for (Passive passive : passivesPrev.keySet()) {
                if (!passives.containsKey(passive))
                    removedPassives.add(passive);
            }

            kitItem.applyNewPassive(newPassives);
            kitItem.applyRemovedPassive(removedPassives);

            /* Enchantments */
            item.addFlag(ItemFlag.HIDE_ENCHANTS);
            for (Enchantment enchantment : item.getEnchantments().keySet()) {
                String prefix = "";
                if (!prevItem.getEnchantments().containsKey(enchantment)) {
                    prefix = "§a§l+NEW! ";
                } else if (!prevItem.getEnchantments().get(enchantment).equals(item.getEnchantments().get(enchantment))) {
                    prefix = "§a§l+NEW! ";
                    item.addLore("§c§m" + ChatColor.stripColor(ItemUtils.getName(enchantment, prevItem.getEnchantments().get(enchantment))));
                }

                item.addLore(prefix + ItemUtils.getName(enchantment, item.getEnchantments().get(enchantment)));
            }
        }

        add(row, slot, new EmptyItemInstance(item.build()));
    }

    private enum Type {

        HELMET,
        CHESTPLATE,
        LEGGINGS,
        BOOTS,
        CONTENT;

    }
}
