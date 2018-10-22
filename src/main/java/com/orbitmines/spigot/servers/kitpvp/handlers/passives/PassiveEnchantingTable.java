package com.orbitmines.spigot.servers.kitpvp.handlers.passives;

import com.orbitmines.spigot.api.handlers.itembuilders.ItemBuilder;
import com.orbitmines.spigot.api.handlers.kit.Kit;
import com.orbitmines.spigot.api.utils.MathUtils;
import com.orbitmines.spigot.servers.kitpvp.handlers.KitPvPPlayer;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PassiveEnchantingTable implements Passive.Handler<PlayerDeathEvent> {

    private final Enchantment[] attack_enchantments = {Enchantment.DAMAGE_ALL, Enchantment.FIRE_ASPECT};

    private final Enchantment[] protect_enchantments = {Enchantment.PROTECTION_ENVIRONMENTAL, Enchantment.THORNS};


    @Override
    public void trigger(PlayerDeathEvent event, int level) {

        Player entity = event.getEntity();

        Player kill = entity.getKiller();

        KitPvPPlayer killer = KitPvPPlayer.getPlayer(kill);

        int slot = enchantedArmor(level) ? MathUtils.clamp(MathUtils.randomInteger(5), 0, 4) : 0;

        Kit kit = killer.getSelectedKit().getKit();

        ItemBuilder item = getRandomBuilder(kit, slot);

        int tries = 0;

        while (true) {

            if (tries <= 10) {
                Enchantment enchantment = getRandomEnchant(slot == 0 ? attack_enchantments : protect_enchantments);
                int l = item.getEnchantments().getOrDefault(enchantment, 0) + 1;

                if (enchantment.getMaxLevel() > l) {
                    item.getEnchantments().put(enchantment, l);
                    killer.getSelectedKit().give(killer);
                    break;
                }
                tries++;

            } else {
                return;
            }
        }
    }

    private ItemBuilder getRandomBuilder(Kit kit, int slot) {
        switch (slot) {
            case 0:
                return kit.getItems(Material.ENCHANTED_BOOK).get(0).clone();
            case 1:
                return kit.getHelmet();
            case 2:
                return kit.getChestplate();
            case 3:
                return kit.getLeggings();
            case 4:
                return kit.getBoots();
        }
        return null;
    }

    private boolean enchantedArmor(int level) {
        switch (level) {
            case 1:
                return false;
            case 2:
                return true;
        }
        return false;
    }

    private Enchantment getRandomEnchant(Enchantment[] enchantments) {
        int index = MathUtils.randomInteger(enchantments.length);
        return enchantments[index];
    }

}
