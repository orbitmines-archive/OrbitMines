package com.orbitmines.spigot.servers.uhsurvival.handlers.item.tool.enchantments;

import com.orbitmines.spigot.api.utils.LocationUtils;
import com.orbitmines.spigot.api.utils.MathUtils;
import com.orbitmines.spigot.servers.uhsurvival.handlers.UHPlayer;
import com.orbitmines.spigot.servers.uhsurvival.handlers.item.tool.Tool;
import com.orbitmines.spigot.servers.uhsurvival.handlers.mob.Attacker;
import com.orbitmines.spigot.servers.uhsurvival.utils.Enchantment;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Enchantments {

    private static final int MAX_EXP = 30;

    private double chance;

    Enchantments(double chance){
        this.chance = chance;
    }

    public double getChance() {
        return chance;
    }

    /* ENCHANTMENT-TYPES */
    public static abstract class AttackEnchantment extends Enchantments {

        AttackEnchantment(double chance) {
            super(chance);
        }

        public abstract boolean output(Attacker attacker, Attacker defender, int level);
    }

    public static abstract class BreakEnchantment extends Enchantments {

        BreakEnchantment(double chance) {
            super(chance);
        }

        public abstract boolean output(UHPlayer player, Block block, int level);
    }

    /* ENCHANTMENTS */
    public static class EnchantmentLightning extends AttackEnchantment {

        public EnchantmentLightning(){
            super(50);
        }

        @Override
        public boolean output(Attacker attacker, Attacker defender, int level) {
            attacker.getLocation().getWorld().strikeLightning(defender.getLocation());
            return false;
        }
    }

    public static class EnchantmentExploding extends AttackEnchantment {

        public EnchantmentExploding() {
            super(25);
        }

        @Override
        public boolean output(Attacker attacker, Attacker defender, int level) {
            attacker.getLocation().getWorld().createExplosion(defender.getLocation(), (float) (level * 1.25));
            return false;
        }
    }

    public static class EnchantmentDrill extends BreakEnchantment {

        public EnchantmentDrill() {
            super(25);
        }

        @Override
        public boolean output(UHPlayer player, Block block, int level) {
            block.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, block.getLocation(), 1);
            //TODO: ADD SOUNDS
            for (int x = -1; x < 2; x++) {
                for (int y = -1; y < 2; y++) {
                    for (int z = -1; z < 2; z++) {
                        if (x == 0 || y == 0 || z == 0) {
                            Location location = new Location(block.getWorld(), block.getX() + x, block.getY() + y, block.getZ() + z);
                            Block b = location.getBlock();
                            if (!location.equals(block.getLocation())) {
                                if (!b.isLiquid() && b.getType().isSolid() && b.getType() != Material.BEDROCK) {
                                    Enchantments.breakBlock(b, player.getItemInMainHand());
                                }
                            }
                        }
                    }
                }
            }
            return false;
        }
    }

    public static class EnchantmentVeinMiner extends BreakEnchantment {

        private Material[] acceptedBlocks = new Material[]{Material.DIAMOND_ORE, Material.COAL_ORE, Material.REDSTONE_ORE, Material.EMERALD_ORE, Material.REDSTONE_ORE, Material.LAPIS_ORE, Material.IRON_ORE
        , Material.GOLD_ORE, Material.NETHER_QUARTZ_ORE};

        public EnchantmentVeinMiner() {
            super(20);
        }

        @Override
        public boolean output(UHPlayer player, Block block, int level) {
            int MAX_BLOCKS = level * 5;
            int current_broken_blocks = 0;
            List<Block> blocks = new ArrayList<>();
            if(Arrays.asList(acceptedBlocks).contains(block.getType())) {
                LocationUtils.addIdenticalBlocksTouching(block, blocks);
                for (Block block1 : blocks) {
                    if (current_broken_blocks < MAX_BLOCKS) {
                        Enchantments.breakBlock(block1, player.getItemInMainHand());
                        current_broken_blocks++;
                    } else {
                        break;
                    }
                }
            }
            return false;
        }
    }

    /* STATIC METHODS */
    private static void breakBlock(Block block, ItemStack item){
        for(ItemStack i : block.getDrops(item)){
            block.getWorld().dropItemNaturally(block.getLocation(), i);
        }
        block.setType(Material.AIR);
    }

    public static void enchant(Tool tool, int exp_level){
        System.out.println("ToolType:" + tool.getType().name());
        for(Enchantment enchantment : Enchantment.getEnchantments(tool.getType())){
            //TODO: MAKE IT FOR A DOUBLE!;
            if(MathUtils.randomize(0, 100, (int) enchantment.getChance())){
                tool.addEnchantment(enchantment, exp_level / (MAX_EXP / enchantment.getMaxLevel()));
            }
        }
    }
}
