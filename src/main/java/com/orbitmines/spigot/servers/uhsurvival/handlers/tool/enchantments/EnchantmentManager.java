package com.orbitmines.spigot.servers.uhsurvival.handlers.tool.enchantments;

import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.api.utils.MathUtils;
import com.orbitmines.spigot.servers.uhsurvival.UHSurvival;
import com.orbitmines.spigot.servers.uhsurvival.handlers.tool.Tool;
import com.orbitmines.spigot.servers.uhsurvival.utils.enums.Action;
import com.orbitmines.spigot.servers.uhsurvival.utils.enums.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by Robin on 2/27/2018.
 */
public class EnchantmentManager {

    private OrbitMines om;
    private HashMap<Enchantment, EnchantmentOutput> outputs;

    public EnchantmentManager(UHSurvival uhSurvival) {
        this.om = uhSurvival.getOrbitMines();
        this.outputs = new HashMap<>();
        new Enchantments(uhSurvival, this);
    }

    /* ENCHANTMENT METHODS (output) */
    public void output(HashMap<Enchantment, Integer> enchantments, Action action, Event event, boolean shoot){
        for(Enchantment ench : enchantments.keySet()){
            if(ench.getAction() == action){
                EnchantmentOutput output = outputs.get(ench);
                if (output != null) {
                    if (MathUtils.randomize(0, 100, output.getChance())) {
                        if (output.isShootable() && shoot) {
                            output.shootArrow(om, ((EntityShootBowEvent) event).getProjectile(), enchantments.get(ench));
                        } else {
                            output.output(event, enchantments.get(ench));
                        }
                    }
                }
            }
        }
    }

    public void registerOutput(EnchantmentOutput output){
        this.outputs.put(output.enchantment, output);
    }

    /* GETTERS (getEnchantments) */
    /*public HashMap<Enchantment, Integer> getEnchantments(Entity entity){
        HashMap<Enchantment, Integer> enchantments = new HashMap<>();
        for(MetadataValue metadataValue : entity.getMetadata("customEnchantment")){
            String[] ench = metadataValue.asString().split(" ");
            Enchantment en = Enchantment.getEnchantment(ench[0]);
            if(en != null){
                int level = MathUtils.getInteger(ench[1]);
                if(level != -1){
                    enchantments.put(en, level);
                }
            }
        }
        return enchantments;
    }*/

    /* STATIC METHODS (enchant) */
    public static boolean enchant(Tool tool, int level) {
        for (Enchantment enchantment : Enchantment.values()) {
            if(MathUtils.randomize(0, 100, enchantment.getChance())) {
                if (Arrays.asList(enchantment.getTypes()).contains(tool.getType())) {
                    if (!tool.isEnchanted(enchantment.getCounterEnchantment())) {
                        int levelPerLevel = 30 / enchantment.getMaxLevel();
                        int levels = level / levelPerLevel;
                        tool.addEnchantment(enchantment, levels);
                    }
                }
            }
        }
        if(!tool.isEnchanted()){
            enchant(tool, level);
            return false;
        } else {
            tool.updateTool();
            return true;
        }
    }

    /* CLASS (EnchantmentOutput) */
    public static abstract class EnchantmentOutput {

        private Enchantment enchantment;
        private boolean shootable;
        private int chance;

        public EnchantmentOutput(Enchantment enchantment, boolean shootable, int chance) {
            this.enchantment = enchantment;
            this.shootable = shootable;
            this.chance = chance;
        }

        public abstract void output(Event event, int level);

        void shootArrow(OrbitMines om, Entity e, int level) {
            e.setMetadata("customEnchantment", new FixedMetadataValue(om, enchantment.getName() + " " + level));
        }

        boolean isShootable() {
            return shootable;
        }

        int getChance() {
            return chance;
        }
    }

}
