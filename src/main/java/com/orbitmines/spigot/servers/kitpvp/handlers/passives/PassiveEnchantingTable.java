package com.orbitmines.spigot.servers.kitpvp.handlers.passives;

import com.orbitmines.api.utils.RandomUtils;
import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.api.handlers.npc.FloatingItem;
import com.orbitmines.spigot.api.nms.itemstack.ItemStackNms;
import com.orbitmines.spigot.api.utils.ItemUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;

public class PassiveEnchantingTable implements Passive.Handler<PlayerDeathEvent> {

    private final ItemStackNms nms;

    public PassiveEnchantingTable() {
        nms = OrbitMines.getInstance().getNms().customItem();
    }

    @Override
    public void trigger(PlayerDeathEvent event, int level) {
        Player entity = event.getEntity();
        Player killer = entity.getKiller();

        /* There's a chance of the lightning hitting, otherwise move on */
        if (Math.random() >= getChance(level))
            return;

        Slot slot = Slot.random(entity);

        ItemStack itemStack = slot.getFor(killer);
        Enchantment enchantment = slot.randomEnchantment(killer);

        if (enchantment == null)
            return;

        int newLevel = itemStack.getEnchantmentLevel(enchantment) + 1;

        /* Since 1.13 sharpness enchantment no longer applies on items it cannot enchant, so we do it ourselves. */
        if (enchantment == Enchantment.DAMAGE_ALL && !enchantment.canEnchantItem(itemStack))
            itemStack = Passive.ATTACK_DAMAGE.apply(nms, itemStack, newLevel);

        itemStack.addUnsafeEnchantment(enchantment, newLevel);

        slot.setFor(killer, itemStack);

        /* Build Item Hologram */
        ItemStack fItemstack = itemStack;
        FloatingItem hologram = new FloatingItem(null, entity.getLocation()) {
            @Override
            public ItemStack build() {
                return new ItemStack(fItemstack);
            }
        };
        hologram.addLine(() -> Passive.ENCHANTING_TABLE.getColor().getChatColor() + "§l" + Passive.ENCHANTING_TABLE.getName(), false);
        hologram.addLine(() -> "§e§o+ " + ChatColor.stripColor(ItemUtils.getName(enchantment, newLevel)), false);
        hologram.create(killer);

        new BukkitRunnable() {
            @Override
            public void run() {
                hologram.destroy();
            }
        }.runTaskLater(OrbitMines.getInstance(), 60L);
    }

    public double getChance(int level) {
        switch (level) {
            case 1:
                return 0.4D;
            case 2:
                return 0.5D;
            case 3:
                return 0.6D;
            default:
                throw new ArrayIndexOutOfBoundsException();
        }
    }

    private enum Slot {

        WEAPON(new Enchantment[] {
                Enchantment.DAMAGE_ALL,
                Enchantment.KNOCKBACK,
                Enchantment.FIRE_ASPECT,
                Enchantment.SWEEPING_EDGE
        }) {
            @Override
            public ItemStack getFor(Player player) {
                return player.getInventory().getItemInMainHand();
            }

            @Override
            public void setFor(Player player, ItemStack itemStack) {
                player.getInventory().setItemInMainHand(itemStack);
            }
        },
        BOW(new Enchantment[] {
                Enchantment.ARROW_DAMAGE,
                Enchantment.ARROW_KNOCKBACK,
                Enchantment.ARROW_FIRE,
                Enchantment.ARROW_INFINITE
        }) {
            @Override
            public ItemStack getFor(Player player) {
                int slot = getSlot(player);
                if (slot == -1)
                    return null;

                return player.getInventory().getItem(slot);
            }

            @Override
            public void setFor(Player player, ItemStack itemStack) {
                player.getInventory().setItem(getSlot(player), itemStack);
            }

            private int getSlot(Player player) {
                return player.getInventory().first(Material.BOW);
            }
        },

        HELMET(new Enchantment[] {
                Enchantment.PROTECTION_ENVIRONMENTAL,
                Enchantment.PROTECTION_FIRE,
                Enchantment.PROTECTION_EXPLOSIONS,
                Enchantment.PROTECTION_PROJECTILE,

                Enchantment.OXYGEN
        }) {
            @Override
            public ItemStack getFor(Player player) {
                return player.getInventory().getHelmet();
            }

            @Override
            public void setFor(Player player, ItemStack itemStack) {
                player.getInventory().setHelmet(itemStack);
            }
        },
        CHESTPLATE(new Enchantment[] {
                Enchantment.PROTECTION_ENVIRONMENTAL,
                Enchantment.PROTECTION_FIRE,
                Enchantment.PROTECTION_EXPLOSIONS,
                Enchantment.PROTECTION_PROJECTILE
        }) {
            @Override
            public ItemStack getFor(Player player) {
                return player.getInventory().getChestplate();
            }

            @Override
            public void setFor(Player player, ItemStack itemStack) {
                player.getInventory().setChestplate(itemStack);
            }
        },
        LEGGINGS(new Enchantment[] {
                Enchantment.PROTECTION_ENVIRONMENTAL,
                Enchantment.PROTECTION_FIRE,
                Enchantment.PROTECTION_EXPLOSIONS,
                Enchantment.PROTECTION_PROJECTILE
        }) {
            @Override
            public ItemStack getFor(Player player) {
                return player.getInventory().getLeggings();
            }

            @Override
            public void setFor(Player player, ItemStack itemStack) {
                player.getInventory().setLeggings(itemStack);
            }
        },
        BOOTS(new Enchantment[] {
                Enchantment.PROTECTION_ENVIRONMENTAL,
                Enchantment.PROTECTION_FIRE,
                Enchantment.PROTECTION_EXPLOSIONS,
                Enchantment.PROTECTION_PROJECTILE,

                Enchantment.PROTECTION_FALL,
                Enchantment.DEPTH_STRIDER,
                Enchantment.FROST_WALKER
        }) {
            @Override
            public ItemStack getFor(Player player) {
                return player.getInventory().getBoots();
            }

            @Override
            public void setFor(Player player, ItemStack itemStack) {
                player.getInventory().setBoots(itemStack);
            }
        };

        private final Enchantment[] enchantments;

        Slot(Enchantment[] enchantments) {
            this.enchantments = enchantments;
        }

        public Enchantment randomEnchantment(Player player) {
            ItemStack itemStack = getFor(player);

            Set<Enchantment> enchantments = new HashSet<>();
            for (Enchantment enchantment : this.enchantments) {
                if (itemStack.getEnchantmentLevel(enchantment) < getMaxLevel(enchantment))
                    enchantments.add(enchantment);
            }

            return RandomUtils.randomFrom(enchantments);
        }

        public ItemStack getFor(Player player) {
            throw new IllegalStateException();
        }

        public void setFor(Player player, ItemStack itemStack) {
            throw new IllegalStateException();
        }

        private int getMaxLevel(Enchantment enchantment) {
            if (enchantment == Enchantment.DAMAGE_ALL ||
                enchantment == Enchantment.PROTECTION_ENVIRONMENTAL
            )
                return 10;
            else if (enchantment == Enchantment.ARROW_DAMAGE)
                return 8;
            else if (enchantment == Enchantment.KNOCKBACK ||
                     enchantment == Enchantment.SWEEPING_EDGE ||
                     enchantment == Enchantment.ARROW_KNOCKBACK ||
                     enchantment == Enchantment.PROTECTION_FIRE ||
                     enchantment == Enchantment.PROTECTION_EXPLOSIONS ||
                     enchantment == Enchantment.PROTECTION_PROJECTILE ||
                     enchantment == Enchantment.PROTECTION_FALL
            )
                return 5;
            else if (enchantment == Enchantment.FIRE_ASPECT)
                return 3;
            else
                return enchantment.getMaxLevel();
        }

        public static Slot random(Player player) {
            Set<Slot> slots = new HashSet<>();
            for (Slot slot : values()) {
                if (slot.getFor(player) != null && slot.randomEnchantment(player) != null)
                    slots.add(slot);
            }

            return RandomUtils.randomFrom(slots);
        }
    }
}
