package com.orbitmines.spigot.servers.uhsurvival.handlers.profile.food;

import com.orbitmines.spigot.api.handlers.itembuilders.PotionBuilder;
import com.orbitmines.spigot.api.utils.MathUtils;
import com.orbitmines.spigot.servers.uhsurvival.UHSurvival;
import com.orbitmines.spigot.servers.uhsurvival.handlers.UHPlayer;
import com.orbitmines.spigot.servers.uhsurvival.utils.enums.FoodType;
import org.bukkit.Material;
import org.bukkit.potion.PotionEffectType;

/**
 * Created by Robin on 3/3/2018.
 */
class Foods {

    Foods(UHSurvival uhSurvival) {
        FoodManager food = uhSurvival.getFoodManager();
        food.addFoodType(new FoodSteak());
        food.addFoodType(new FoodChicken());
        food.addFoodType(new FoodPorkchop());
        food.addFoodType(new FoodMutton());
        food.addFoodType(new FoodRabbit());
        food.addFoodType(new FoodRawBeef());
        food.addFoodType(new FoodRawChicken());
        food.addFoodType(new FoodRawPorkchop());
        food.addFoodType(new FoodRawMutton());
        food.addFoodType(new FoodRawRabbit());
        food.addFoodType(new FoodFish());
        food.addFoodType(new FoodSalmon());
        food.addFoodType(new FoodClownFish());
        food.addFoodType(new FoodPufferFish());
        food.addFoodType(new FoodRawFish());
        food.addFoodType(new FoodRawSalmon());
        food.addFoodType(new FoodApple());
        food.addFoodType(new FoodChorusFruit());
        food.addFoodType(new FoodMelon());
        food.addFoodType(new FoodGoldenApple());
        food.addFoodType(new FoodEnchantedGoldenApple());
        food.addFoodType(new FoodPotato());
        food.addFoodType(new FoodPoisonousPotato());
        food.addFoodType(new FoodBeetRoot());
        food.addFoodType(new FoodCarrot());
        food.addFoodType(new FoodGoldenCarrot());
        food.addFoodType(new FoodCookie());
        food.addFoodType(new FoodPumpkinPie());
        food.addFoodType(new FoodRabbitSoup());
        food.addFoodType(new FoodMushroomStew());
        food.addFoodType(new FoodBeetroutSoup());
        food.addFoodType(new FoodBread());
        food.addFoodType(new FoodFlesh());
        food.addFoodType(new FoodSpiderEye());

    }

    public class FoodSteak extends FoodManager.Food {

        FoodSteak() {
            super(FoodType.MEAT, Material.COOKED_BEEF, (byte) 0, 8, 12.8F);
        }
    }

    public class FoodChicken extends FoodManager.Food {

        FoodChicken() {
            super(FoodType.MEAT, Material.COOKED_CHICKEN, (byte) 0, 6, 7.2F);
        }
    }

    public class FoodPorkchop extends FoodManager.Food {

        FoodPorkchop() {
            super(FoodType.MEAT, Material.GRILLED_PORK, (byte) 0, 8, 12.8F);
        }
    }

    public class FoodMutton extends FoodManager.Food {

        FoodMutton() {
            super(FoodType.MEAT, Material.COOKED_MUTTON, (byte) 0, 6, 9.6F);
        }
    }

    public class FoodRabbit extends FoodManager.Food {

        FoodRabbit() {
            super(FoodType.MEAT, Material.COOKED_RABBIT, (byte) 0, 5, 6F);
        }
    }

    public class FoodRawBeef extends FoodManager.Food {

        FoodRawBeef() {
            super(FoodType.MEAT, Material.RAW_BEEF, (byte) 0, 3, 1.8F);
        }
    }

    public class FoodRawChicken extends FoodManager.Food {

        FoodRawChicken() {
            super(FoodType.MEAT, Material.RAW_CHICKEN, (byte) 0, 2, 1.2F);
        }

        @Override
        public void consume(UHPlayer uhPlayer) {
            if(MathUtils.randomize(0, 100, 30)) {
                uhPlayer.addPotionEffect(new PotionBuilder(PotionEffectType.HUNGER, 30 * 20, 0));
                uhPlayer.addPotionEffect(new PotionBuilder(PotionEffectType.POISON, 5 * 20, 0));
            }
        }
    }

    public class FoodRawPorkchop extends FoodManager.Food {

        FoodRawPorkchop() {
            super(FoodType.MEAT, Material.PORK, (byte) 0, 3, 1.8F);
        }
    }

    public class FoodRawMutton extends FoodManager.Food {

        FoodRawMutton() {
            super(FoodType.MEAT, Material.MUTTON, (byte) 0, 2, 1.2F);
        }
    }

    public class FoodRawRabbit extends FoodManager.Food {

        FoodRawRabbit() {
            super(FoodType.MEAT, Material.RABBIT, (byte) 0, 3, 1.8F);
        }
    }

    public class FoodFish extends FoodManager.Food {

        FoodFish() {
            super(FoodType.FISH, Material.COOKED_FISH, (byte) 0, 5, 6F);
        }
    }

    public class FoodSalmon extends FoodManager.Food {

        FoodSalmon() {
            super(FoodType.FISH, Material.COOKED_FISH, (byte) 1, 6, 9.6F);
        }
    }

    public class FoodClownFish extends FoodManager.Food {

        FoodClownFish() {
            super(FoodType.FISH, Material.RAW_FISH, (byte) 2, 1, 0.2F);
        }
    }

    public class FoodPufferFish extends FoodManager.Food {

        FoodPufferFish() {
            super(FoodType.FISH, Material.RAW_FISH, (byte) 3, 1, 0.2F);
        }

        @Override
        public void consume(UHPlayer uhPlayer) {
            uhPlayer.addPotionEffect(new PotionBuilder(PotionEffectType.HUNGER, 15 * 20, 2));
            uhPlayer.addPotionEffect(new PotionBuilder(PotionEffectType.CONFUSION, 15 * 20, 1));
            uhPlayer.addPotionEffect(new PotionBuilder(PotionEffectType.POISON, 60 * 20, 3));
        }
    }

    public class FoodRawFish extends FoodManager.Food {

        FoodRawFish() {
            super(FoodType.FISH, Material.RAW_FISH, (byte) 0, 2, 0.4F);
        }
    }

    public class FoodRawSalmon extends FoodManager.Food {

        FoodRawSalmon() {
            super(FoodType.FISH, Material.RAW_FISH, (byte) 1, 2, 0.4F);
        }
    }

    public class FoodApple extends FoodManager.Food {

        FoodApple() {
            super(FoodType.FRUIT, Material.APPLE, (byte) 0, 4, 2.4F);
        }
    }

    public class FoodChorusFruit extends FoodManager.Food {

        FoodChorusFruit() {
            super(FoodType.FRUIT, Material.CHORUS_FRUIT, (byte) 0, 4, 2.4F);
        }
    }

    public class FoodMelon extends FoodManager.Food {

        FoodMelon() {
            super(FoodType.FRUIT, Material.MELON, (byte) 0, 2, 1.2F);
        }

    }

    public class FoodGoldenApple extends FoodManager.Food {

        FoodGoldenApple() {
            super(FoodType.FRUIT, Material.GOLDEN_APPLE, (byte) 0, 4, 9.6F);
        }

        @Override
        public void consume(UHPlayer uhPlayer) {
            uhPlayer.addPotionEffect(new PotionBuilder(PotionEffectType.REGENERATION, 100, 1));
            uhPlayer.addPotionEffect(new PotionBuilder(PotionEffectType.ABSORPTION, 5 * 60 * 20, 0));
        }
    }

    public class FoodEnchantedGoldenApple extends FoodManager.Food {

        FoodEnchantedGoldenApple() {
            super(FoodType.FRUIT, Material.GOLDEN_APPLE, (byte) 1, 4, 9.6F);
        }

        @Override
        public void consume(UHPlayer uhPlayer) {
            uhPlayer.addPotionEffect(new PotionBuilder(PotionEffectType.REGENERATION, 20 * 20, 1));
            uhPlayer.addPotionEffect(new PotionBuilder(PotionEffectType.ABSORPTION, 2 * 60 * 20, 3));
            uhPlayer.addPotionEffect(new PotionBuilder(PotionEffectType.DAMAGE_RESISTANCE, 5 * 60 * 20, 0));
            uhPlayer.addPotionEffect(new PotionBuilder(PotionEffectType.FIRE_RESISTANCE, 5 * 60 * 20, 0));

        }
    }

    public class FoodPotato extends FoodManager.Food {

        FoodPotato() {
            super(FoodType.VEGETABLES, Material.POTATO_ITEM, (byte) 0, 1, 0.6F);
        }
    }

    public class FoodPoisonousPotato extends FoodManager.Food {

        FoodPoisonousPotato() {
            super(FoodType.VEGETABLES, Material.POISONOUS_POTATO, (byte) 0, 2, 1.2F);
        }

        @Override
        public void consume(UHPlayer uhPlayer) {
            if(MathUtils.randomize(0, 100, 60)){
                uhPlayer.addPotionEffect(new PotionBuilder(PotionEffectType.POISON, 80, 1));
            }
        }
    }

    public class FoodBeetRoot extends FoodManager.Food {

        FoodBeetRoot() {
            super(FoodType.VEGETABLES, Material.BEETROOT, (byte) 0, 1, 1.2F);
        }
    }

    public class FoodCarrot extends FoodManager.Food {

        FoodCarrot() {
            super(FoodType.VEGETABLES, Material.CARROT_ITEM, (byte) 0, 3, 3.6F);
        }
    }

    public class FoodGoldenCarrot extends FoodManager.Food {

        FoodGoldenCarrot() {
            super(FoodType.VEGETABLES, Material.GOLDEN_CARROT, (byte) 0, 6, 14.4F);
        }
    }

    public class FoodCookie extends FoodManager.Food {

        FoodCookie() {
            super(FoodType.SWEET, Material.COOKIE, (byte) 0, 2, 0.4F);
        }
    }

    public class FoodPumpkinPie extends FoodManager.Food {

        FoodPumpkinPie(){
            super(FoodType.SWEET, Material.PUMPKIN_PIE, (byte) 0, 8, 4.8F);
        }
    }

    public class FoodRabbitSoup extends FoodManager.Food {

        FoodRabbitSoup(){
            super(FoodType.SOUPS, Material.RABBIT_STEW, (byte) 0, 10, 12F);
        }
    }

    public class FoodMushroomStew extends FoodManager.Food {

        FoodMushroomStew(){
            super(FoodType.SOUPS, Material.MUSHROOM_SOUP, (byte) 0, 6, 7.2F);
        }
    }

    public class FoodBeetroutSoup extends FoodManager.Food{

        FoodBeetroutSoup(){
            super(FoodType.SOUPS, Material.BEETROOT_SOUP, (byte) 0, 6, 7.2F);
        }
    }

    public class FoodBread extends FoodManager.Food {

        FoodBread(){
            super(FoodType.MALICIOUS, Material.BREAD, (byte) 0, 5, 6F);
        }
    }

    public class FoodFlesh extends FoodManager.Food {

        FoodFlesh(){
            super(FoodType.MALICIOUS, Material.ROTTEN_FLESH, (byte) 0, 4, 0.8F);
        }

        @Override
        public void consume(UHPlayer uhPlayer) {
            if(MathUtils.randomize(0, 100, 80)){
                uhPlayer.addPotionEffect(new PotionBuilder(PotionEffectType.HUNGER, 30 * 20, 0));
            }
        }
    }

    public class FoodSpiderEye extends FoodManager.Food {

        FoodSpiderEye(){
            super(FoodType.MALICIOUS, Material.SPIDER_EYE, (byte) 0, 2, 3.2F);
        }

        @Override
        public void consume(UHPlayer uhPlayer) {
            uhPlayer.addPotionEffect(new PotionBuilder(PotionEffectType.POISON, 4 * 20, 0));
        }
    }
}
