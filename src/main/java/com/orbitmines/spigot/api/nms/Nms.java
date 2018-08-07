package com.orbitmines.spigot.api.nms;

import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.api.nms.actionbar.ActionBarNms;
import com.orbitmines.spigot.api.nms.actionbar.ActionBarNms_1_13_R1;
import com.orbitmines.spigot.api.nms.anvilgui.AnvilNms;
import com.orbitmines.spigot.api.nms.anvilgui.AnvilNms_1_13_R1;
import com.orbitmines.spigot.api.nms.armorstand.ArmorStandNms;
import com.orbitmines.spigot.api.nms.armorstand.ArmorStandNms_1_13_R1;
import com.orbitmines.spigot.api.nms.bednpc.BedNpcNms;
import com.orbitmines.spigot.api.nms.bednpc.BedNpcNms_1_13_R1;
import com.orbitmines.spigot.api.nms.entity.EntityNms;
import com.orbitmines.spigot.api.nms.entity.EntityNms_1_13_R1;
import com.orbitmines.spigot.api.nms.firework.FireworkNms;
import com.orbitmines.spigot.api.nms.firework.FireworkNms_1_13_R1;
import com.orbitmines.spigot.api.nms.itemstack.ItemStackNms;
import com.orbitmines.spigot.api.nms.itemstack.ItemStackNms_1_13_R1;
import com.orbitmines.spigot.api.nms.npc.NpcNms;
import com.orbitmines.spigot.api.nms.npc.NpcNms_1_13_R1;
import com.orbitmines.spigot.api.nms.npc.bat.BatNpc;
import com.orbitmines.spigot.api.nms.npc.bat.BatNpc_1_13_R1;
import com.orbitmines.spigot.api.nms.npc.blaze.BlazeNpc;
import com.orbitmines.spigot.api.nms.npc.blaze.BlazeNpc_1_13_R1;
import com.orbitmines.spigot.api.nms.npc.cave_spider.CaveSpiderNpc;
import com.orbitmines.spigot.api.nms.npc.cave_spider.CaveSpiderNpc_1_13_R1;
import com.orbitmines.spigot.api.nms.npc.chicken.ChickenNpc;
import com.orbitmines.spigot.api.nms.npc.chicken.ChickenNpc_1_13_R1;
import com.orbitmines.spigot.api.nms.npc.cod.CodNpc;
import com.orbitmines.spigot.api.nms.npc.cod.CodNpc_1_13_R1;
import com.orbitmines.spigot.api.nms.npc.cow.CowNpc;
import com.orbitmines.spigot.api.nms.npc.cow.CowNpc_1_13_R1;
import com.orbitmines.spigot.api.nms.npc.creeper.CreeperNpc;
import com.orbitmines.spigot.api.nms.npc.creeper.CreeperNpc_1_13_R1;
import com.orbitmines.spigot.api.nms.npc.dolphin.DolphinNpc;
import com.orbitmines.spigot.api.nms.npc.dolphin.DolphinNpc_1_13_R1;
import com.orbitmines.spigot.api.nms.npc.donkey.DonkeyNpc;
import com.orbitmines.spigot.api.nms.npc.donkey.DonkeyNpc_1_13_R1;
import com.orbitmines.spigot.api.nms.npc.drowned.DrownedNpc;
import com.orbitmines.spigot.api.nms.npc.drowned.DrownedNpc_1_13_R1;
import com.orbitmines.spigot.api.nms.npc.elder_guardian.ElderGuardianNpc;
import com.orbitmines.spigot.api.nms.npc.elder_guardian.ElderGuardianNpc_1_13_R1;
import com.orbitmines.spigot.api.nms.npc.ender_dragon.EnderDragonNpc;
import com.orbitmines.spigot.api.nms.npc.ender_dragon.EnderDragonNpc_1_13_R1;
import com.orbitmines.spigot.api.nms.npc.enderman.EndermanNpc;
import com.orbitmines.spigot.api.nms.npc.enderman.EndermanNpc_1_13_R1;
import com.orbitmines.spigot.api.nms.npc.endermite.EndermiteNpc;
import com.orbitmines.spigot.api.nms.npc.endermite.EndermiteNpc_1_13_R1;
import com.orbitmines.spigot.api.nms.npc.evoker.EvokerNpc;
import com.orbitmines.spigot.api.nms.npc.evoker.EvokerNpc_1_13_R1;
import com.orbitmines.spigot.api.nms.npc.ghast.GhastNpc;
import com.orbitmines.spigot.api.nms.npc.ghast.GhastNpc_1_13_R1;
import com.orbitmines.spigot.api.nms.npc.giant.GiantNpc;
import com.orbitmines.spigot.api.nms.npc.giant.GiantNpc_1_13_R1;
import com.orbitmines.spigot.api.nms.npc.guardian.GuardianNpc;
import com.orbitmines.spigot.api.nms.npc.guardian.GuardianNpc_1_13_R1;
import com.orbitmines.spigot.api.nms.npc.horse.HorseNpc;
import com.orbitmines.spigot.api.nms.npc.horse.HorseNpc_1_13_R1;
import com.orbitmines.spigot.api.nms.npc.husk.HuskNpc;
import com.orbitmines.spigot.api.nms.npc.husk.HuskNpc_1_13_R1;
import com.orbitmines.spigot.api.nms.npc.illusioner.IllusionerNpc;
import com.orbitmines.spigot.api.nms.npc.illusioner.IllusionerNpc_1_13_R1;
import com.orbitmines.spigot.api.nms.npc.iron_golem.IronGolemNpc;
import com.orbitmines.spigot.api.nms.npc.iron_golem.IronGolemNpc_1_13_R1;
import com.orbitmines.spigot.api.nms.npc.llama.LlamaNpc;
import com.orbitmines.spigot.api.nms.npc.llama.LlamaNpc_1_13_R1;
import com.orbitmines.spigot.api.nms.npc.magma_cube.MagmaCubeNpc;
import com.orbitmines.spigot.api.nms.npc.magma_cube.MagmaCubeNpc_1_13_R1;
import com.orbitmines.spigot.api.nms.npc.mule.MuleNpc;
import com.orbitmines.spigot.api.nms.npc.mule.MuleNpc_1_13_R1;
import com.orbitmines.spigot.api.nms.npc.mushroom_cow.MushroomCowNpc;
import com.orbitmines.spigot.api.nms.npc.mushroom_cow.MushroomCowNpc_1_13_R1;
import com.orbitmines.spigot.api.nms.npc.ocelot.OcelotNpc;
import com.orbitmines.spigot.api.nms.npc.ocelot.OcelotNpc_1_13_R1;
import com.orbitmines.spigot.api.nms.npc.parrot.ParrotNpc;
import com.orbitmines.spigot.api.nms.npc.parrot.ParrotNpc_1_13_R1;
import com.orbitmines.spigot.api.nms.npc.phantom.PhantomNpc;
import com.orbitmines.spigot.api.nms.npc.phantom.PhantomNpc_1_13_R1;
import com.orbitmines.spigot.api.nms.npc.pig.PigNpc;
import com.orbitmines.spigot.api.nms.npc.pig.PigNpc_1_13_R1;
import com.orbitmines.spigot.api.nms.npc.pig_zombie.PigZombieNpc;
import com.orbitmines.spigot.api.nms.npc.pig_zombie.PigZombieNpc_1_13_R1;
import com.orbitmines.spigot.api.nms.npc.polar_bear.PolarBearNpc;
import com.orbitmines.spigot.api.nms.npc.polar_bear.PolarBearNpc_1_13_R1;
import com.orbitmines.spigot.api.nms.npc.pufferfish.PufferFishNpc;
import com.orbitmines.spigot.api.nms.npc.pufferfish.PufferFishNpc_1_13_R1;
import com.orbitmines.spigot.api.nms.npc.rabbit.RabbitNpc;
import com.orbitmines.spigot.api.nms.npc.rabbit.RabbitNpc_1_13_R1;
import com.orbitmines.spigot.api.nms.npc.salmon.SalmonNpc;
import com.orbitmines.spigot.api.nms.npc.salmon.SalmonNpc_1_13_R1;
import com.orbitmines.spigot.api.nms.npc.sheep.SheepNpc;
import com.orbitmines.spigot.api.nms.npc.sheep.SheepNpc_1_13_R1;
import com.orbitmines.spigot.api.nms.npc.shulker.ShulkerNpc;
import com.orbitmines.spigot.api.nms.npc.shulker.ShulkerNpc_1_13_R1;
import com.orbitmines.spigot.api.nms.npc.silverfish.SilverfishNpc;
import com.orbitmines.spigot.api.nms.npc.silverfish.SilverfishNpc_1_13_R1;
import com.orbitmines.spigot.api.nms.npc.skeleton.SkeletonNpc;
import com.orbitmines.spigot.api.nms.npc.skeleton.SkeletonNpc_1_13_R1;
import com.orbitmines.spigot.api.nms.npc.skeleton_horse.SkeletonHorseNpc;
import com.orbitmines.spigot.api.nms.npc.skeleton_horse.SkeletonHorseNpc_1_13_R1;
import com.orbitmines.spigot.api.nms.npc.slime.SlimeNpc;
import com.orbitmines.spigot.api.nms.npc.slime.SlimeNpc_1_13_R1;
import com.orbitmines.spigot.api.nms.npc.snowman.SnowmanNpc;
import com.orbitmines.spigot.api.nms.npc.snowman.SnowmanNpc_1_13_R1;
import com.orbitmines.spigot.api.nms.npc.spider.SpiderNpc;
import com.orbitmines.spigot.api.nms.npc.spider.SpiderNpc_1_13_R1;
import com.orbitmines.spigot.api.nms.npc.squid.SquidNpc;
import com.orbitmines.spigot.api.nms.npc.squid.SquidNpc_1_13_R1;
import com.orbitmines.spigot.api.nms.npc.stray.StrayNpc;
import com.orbitmines.spigot.api.nms.npc.stray.StrayNpc_1_13_R1;
import com.orbitmines.spigot.api.nms.npc.tropical_fish.TropicalFishNpc;
import com.orbitmines.spigot.api.nms.npc.tropical_fish.TropicalFishNpc_1_13_R1;
import com.orbitmines.spigot.api.nms.npc.turtle.TurtleNpc;
import com.orbitmines.spigot.api.nms.npc.turtle.TurtleNpc_1_13_R1;
import com.orbitmines.spigot.api.nms.npc.vex.VexNpc;
import com.orbitmines.spigot.api.nms.npc.vex.VexNpc_1_13_R1;
import com.orbitmines.spigot.api.nms.npc.villager.VillagerNpc;
import com.orbitmines.spigot.api.nms.npc.villager.VillagerNpc_1_13_R1;
import com.orbitmines.spigot.api.nms.npc.vindicator.VindicatorNpc;
import com.orbitmines.spigot.api.nms.npc.vindicator.VindicatorNpc_1_13_R1;
import com.orbitmines.spigot.api.nms.npc.witch.WitchNpc;
import com.orbitmines.spigot.api.nms.npc.witch.WitchNpc_1_13_R1;
import com.orbitmines.spigot.api.nms.npc.wither.WitherNpc;
import com.orbitmines.spigot.api.nms.npc.wither.WitherNpc_1_13_R1;
import com.orbitmines.spigot.api.nms.npc.wither_skeleton.WitherSkeletonNpc;
import com.orbitmines.spigot.api.nms.npc.wither_skeleton.WitherSkeletonNpc_1_13_R1;
import com.orbitmines.spigot.api.nms.npc.wolf.WolfNpc;
import com.orbitmines.spigot.api.nms.npc.wolf.WolfNpc_1_13_R1;
import com.orbitmines.spigot.api.nms.npc.zombie.ZombieNpc;
import com.orbitmines.spigot.api.nms.npc.zombie.ZombieNpc_1_13_R1;
import com.orbitmines.spigot.api.nms.npc.zombie_horse.ZombieHorseNpc;
import com.orbitmines.spigot.api.nms.npc.zombie_horse.ZombieHorseNpc_1_13_R1;
import com.orbitmines.spigot.api.nms.npc.zombie_villager.ZombieVillagerNpc;
import com.orbitmines.spigot.api.nms.npc.zombie_villager.ZombieVillagerNpc_1_13_R1;
import com.orbitmines.spigot.api.nms.tablist.TabListNms;
import com.orbitmines.spigot.api.nms.tablist.TabListNms_1_13_R1;
import com.orbitmines.spigot.api.nms.world.WorldNms;
import com.orbitmines.spigot.api.nms.world.WorldNms_1_13_R1;
import com.orbitmines.spigot.api.utils.ConsoleUtils;
import org.bukkit.entity.Player;

/*
* OrbitMines - @author Fadi Shawki - 26-6-2017
*/
public class Nms {

    private OrbitMines orbitMines;
    private String version;

    /* NPCs */
    private NpcNms npcNms;

    private BatNpc batNpc;
    private BlazeNpc blazeNpc;
    private CaveSpiderNpc caveSpiderNpc;
    private ChickenNpc chickenNpc;
    private CodNpc codNpc;
    private CowNpc cowNpc;
    private CreeperNpc creeperNpc;
    private DolphinNpc dolphinNpc;
    private DonkeyNpc donkeyNpc;
    private DrownedNpc drownedNpc;
    private ElderGuardianNpc elderGuardianNpc;
    private EnderDragonNpc enderDragonNpc;
    private EndermanNpc endermanNpc;
    private EndermiteNpc endermiteNpc;
    private EvokerNpc evokerNpc;
    private GhastNpc ghastNpc;
    private GiantNpc giantNpc;
    private GuardianNpc guardianNpc;
    private HorseNpc horseNpc;
    private HuskNpc huskNpc;
    private IllusionerNpc illusionerNpc;
    private IronGolemNpc ironGolemNpc;
    private LlamaNpc llamaNpc;
    private MagmaCubeNpc magmaCubeNpc;
    private MuleNpc muleNpc;
    private MushroomCowNpc mushroomCowNpc;
    private OcelotNpc ocelotNpc;
    private ParrotNpc parrotNpc;
    private PhantomNpc phantomNpc;
    private PigNpc pigNpc;
    private PigZombieNpc pigZombieNpc;
    private PolarBearNpc polarBearNpc;
    private PufferFishNpc pufferFishNpc;
    private RabbitNpc rabbitNpc;
    private SalmonNpc salmonNpc;
    private SheepNpc sheepNpc;
    private ShulkerNpc shulkerNpc;
    private SilverfishNpc silverfishNpc;
    private SkeletonNpc skeletonNpc;
    private SkeletonHorseNpc skeletonHorseNpc;
    private SlimeNpc slimeNpc;
    private SnowmanNpc snowmanNpc;
    private SpiderNpc spiderNpc;
    private SquidNpc squidNpc;
    private StrayNpc strayNpc;
    private TropicalFishNpc tropicalFishNpc;
    private TurtleNpc turtleNpc;
    private VexNpc vexNpc;
    private VillagerNpc villagerNpc;
    private VindicatorNpc vindicatorNpc;
    private WitchNpc witchNpc;
    private WitherNpc witherNpc;
    private WitherSkeletonNpc witherSkeletonNpc;
    private WolfNpc wolfNpc;
    private ZombieNpc zombieNpc;
    private ZombieHorseNpc zombieHorseNpc;
    private ZombieVillagerNpc zombieVillagerNpc;


    /* Entity */
    private EntityNms entityNms;
    /* ActionBar */
    private ActionBarNms actionBarNms;
    /* ItemStack */
    private ItemStackNms itemStackNms;
    /* ArmorStand */
    private ArmorStandNms armorStandNms;
    /* BedNpc */
    private BedNpcNms bedNpcNms;
    /* Firework */
    private FireworkNms fireworkNms;
    /* TabList */
    private TabListNms tabListNms;
    /* World */
    private WorldNms worldNms;

    public Nms() {
        orbitMines = OrbitMines.getInstance();
        orbitMines.setNms(this);

        String version = checkVersion();
        this.version = version;

        if (version == null) {
            ConsoleUtils.empty();
            ConsoleUtils.warn("Error while registering NMS!");
            ConsoleUtils.warn("Disabling plugin...");
            ConsoleUtils.empty();
            orbitMines.getServer().getPluginManager().disablePlugin(orbitMines);
        }
    }

    private String checkVersion() {
        String version;

        try {
            version = orbitMines.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
        } catch (ArrayIndexOutOfBoundsException ex) {
            return null;
        }

        switch (version) {

            case "v1_13_R1": // 1.12 - ?
                /* NPCs */
                npcNms = new NpcNms_1_13_R1();

                batNpc = new BatNpc_1_13_R1(npcNms);
                blazeNpc = new BlazeNpc_1_13_R1(npcNms);
                caveSpiderNpc = new CaveSpiderNpc_1_13_R1(npcNms);
                chickenNpc = new ChickenNpc_1_13_R1(npcNms);
                codNpc = new CodNpc_1_13_R1(npcNms);
                cowNpc = new CowNpc_1_13_R1(npcNms);
                creeperNpc = new CreeperNpc_1_13_R1(npcNms);
                dolphinNpc = new DolphinNpc_1_13_R1(npcNms);
                donkeyNpc = new DonkeyNpc_1_13_R1(npcNms);
                drownedNpc = new DrownedNpc_1_13_R1(npcNms);
                elderGuardianNpc = new ElderGuardianNpc_1_13_R1(npcNms);
                enderDragonNpc = new EnderDragonNpc_1_13_R1(npcNms);
                endermanNpc = new EndermanNpc_1_13_R1(npcNms);
                endermiteNpc = new EndermiteNpc_1_13_R1(npcNms);
                evokerNpc = new EvokerNpc_1_13_R1(npcNms);
                ghastNpc = new GhastNpc_1_13_R1(npcNms);
                giantNpc = new GiantNpc_1_13_R1(npcNms);
                guardianNpc = new GuardianNpc_1_13_R1(npcNms);
                horseNpc = new HorseNpc_1_13_R1(npcNms);
                huskNpc = new HuskNpc_1_13_R1(npcNms);
                illusionerNpc = new IllusionerNpc_1_13_R1(npcNms);
                ironGolemNpc = new IronGolemNpc_1_13_R1(npcNms);
                llamaNpc = new LlamaNpc_1_13_R1(npcNms);
                magmaCubeNpc = new MagmaCubeNpc_1_13_R1(npcNms);
                muleNpc = new MuleNpc_1_13_R1(npcNms);
                mushroomCowNpc = new MushroomCowNpc_1_13_R1(npcNms);
                ocelotNpc = new OcelotNpc_1_13_R1(npcNms);
                parrotNpc = new ParrotNpc_1_13_R1(npcNms);
                phantomNpc = new PhantomNpc_1_13_R1(npcNms);
                pigNpc = new PigNpc_1_13_R1(npcNms);
                pigZombieNpc = new PigZombieNpc_1_13_R1(npcNms);
                polarBearNpc = new PolarBearNpc_1_13_R1(npcNms);
                pufferFishNpc = new PufferFishNpc_1_13_R1(npcNms);
                rabbitNpc = new RabbitNpc_1_13_R1(npcNms);
                salmonNpc = new SalmonNpc_1_13_R1(npcNms);
                sheepNpc = new SheepNpc_1_13_R1(npcNms);
                shulkerNpc = new ShulkerNpc_1_13_R1(npcNms);
                silverfishNpc = new SilverfishNpc_1_13_R1(npcNms);
                skeletonNpc = new SkeletonNpc_1_13_R1(npcNms);
                skeletonHorseNpc = new SkeletonHorseNpc_1_13_R1(npcNms);
                slimeNpc = new SlimeNpc_1_13_R1(npcNms);
                snowmanNpc = new SnowmanNpc_1_13_R1(npcNms);
                spiderNpc = new SpiderNpc_1_13_R1(npcNms);
                squidNpc = new SquidNpc_1_13_R1(npcNms);
                strayNpc = new StrayNpc_1_13_R1(npcNms);
                tropicalFishNpc = new TropicalFishNpc_1_13_R1(npcNms);
                turtleNpc = new TurtleNpc_1_13_R1(npcNms);
                vexNpc = new VexNpc_1_13_R1(npcNms);
                villagerNpc = new VillagerNpc_1_13_R1(npcNms);
                vindicatorNpc = new VindicatorNpc_1_13_R1(npcNms);
                witchNpc = new WitchNpc_1_13_R1(npcNms);
                witherNpc = new WitherNpc_1_13_R1(npcNms);
                witherSkeletonNpc = new WitherSkeletonNpc_1_13_R1(npcNms);
                wolfNpc = new WolfNpc_1_13_R1(npcNms);
                zombieNpc = new ZombieNpc_1_13_R1(npcNms);
                zombieHorseNpc = new ZombieHorseNpc_1_13_R1(npcNms);
                zombieVillagerNpc = new ZombieVillagerNpc_1_13_R1(npcNms);

                /* Entity */
                entityNms = new EntityNms_1_13_R1();
                /* ActionBar */
                actionBarNms = new ActionBarNms_1_13_R1();
                /* ItemStack */
                itemStackNms = new ItemStackNms_1_13_R1();
                /* ArmorStand */
                armorStandNms = new ArmorStandNms_1_13_R1();
                /* BedNpc */
                bedNpcNms = new BedNpcNms_1_13_R1(orbitMines);
                /* Firework */
                fireworkNms = new FireworkNms_1_13_R1(orbitMines);
                /* TabList */
                tabListNms = new TabListNms_1_13_R1();
                /* World */
                worldNms = new WorldNms_1_13_R1();

                break;
            default:
                return null;
        }

        return version;
    }

    public String getVersion() {
        return version;
    }

    public NpcNms npc() {
        return npcNms;
    }

    public EntityNms entity() {
        return entityNms;
    }

    public ActionBarNms actionBar() {
        return actionBarNms;
    }

    public AnvilNms anvilGui(Player player, AnvilNms.AnvilClickEventHandler handler) {
        return anvilGui(player, handler, null);
    }

    public AnvilNms anvilGui(Player player, AnvilNms.AnvilClickEventHandler handler, AnvilNms.AnvilCloseEvent closeEvent) {
        switch (version) {
            case "v1_13_R1": // 1.13 - ?
                return new AnvilNms_1_13_R1(player, handler, closeEvent);
        }
        return null;
    }

    public ItemStackNms customItem() {
        return itemStackNms;
    }

    public ArmorStandNms armorStand() {
        return armorStandNms;
    }

    public BedNpcNms bedNpc() {
        return bedNpcNms;
    }

    public FireworkNms firework() {
        return fireworkNms;
    }

    public TabListNms tabList() {
        return tabListNms;
    }

    public WorldNms world() {
        return worldNms;
    }

    /*
        Npcs
     */

    public BatNpc getBatNpc() {
        return batNpc;
    }

    public BlazeNpc getBlazeNpc() {
        return blazeNpc;
    }

    public CaveSpiderNpc getCaveSpiderNpc() {
        return caveSpiderNpc;
    }

    public ChickenNpc getChickenNpc() {
        return chickenNpc;
    }

    public CodNpc getCodNpc() {
        return codNpc;
    }

    public CowNpc getCowNpc() {
        return cowNpc;
    }

    public CreeperNpc getCreeperNpc() {
        return creeperNpc;
    }

    public DolphinNpc getDolphinNpc() {
        return dolphinNpc;
    }

    public DonkeyNpc getDonkeyNpc() {
        return donkeyNpc;
    }

    public DrownedNpc getDrownedNpc() {
        return drownedNpc;
    }

    public ElderGuardianNpc getElderGuardianNpc() {
        return elderGuardianNpc;
    }

    public EnderDragonNpc getEnderDragonNpc() {
        return enderDragonNpc;
    }

    public EndermanNpc getEndermanNpc() {
        return endermanNpc;
    }

    public EndermiteNpc getEndermiteNpc() {
        return endermiteNpc;
    }

    public EvokerNpc getEvokerNpc() {
        return evokerNpc;
    }

    public GhastNpc getGhastNpc() {
        return ghastNpc;
    }

    public GiantNpc getGiantNpc() {
        return giantNpc;
    }

    public GuardianNpc getGuardianNpc() {
        return guardianNpc;
    }

    public HorseNpc getHorseNpc() {
        return horseNpc;
    }

    public HuskNpc getHuskNpc() {
        return huskNpc;
    }

    public IllusionerNpc getIllusionerNpc() {
        return illusionerNpc;
    }

    public IronGolemNpc getIronGolemNpc() {
        return ironGolemNpc;
    }

    public LlamaNpc getLlamaNpc() {
        return llamaNpc;
    }

    public MagmaCubeNpc getMagmaCubeNpc() {
        return magmaCubeNpc;
    }

    public MuleNpc getMuleNpc() {
        return muleNpc;
    }

    public MushroomCowNpc getMushroomCowNpc() {
        return mushroomCowNpc;
    }

    public OcelotNpc getOcelotNpc() {
        return ocelotNpc;
    }

    public ParrotNpc getParrotNpc() {
        return parrotNpc;
    }

    public PhantomNpc getPhantomNpc() {
        return phantomNpc;
    }

    public PigNpc getPigNpc() {
        return pigNpc;
    }

    public PigZombieNpc getPigZombieNpc() {
        return pigZombieNpc;
    }

    public PolarBearNpc getPolarBearNpc() {
        return polarBearNpc;
    }

    public PufferFishNpc getPufferFishNpc() {
        return pufferFishNpc;
    }

    public RabbitNpc getRabbitNpc() {
        return rabbitNpc;
    }

    public SalmonNpc getSalmonNpc() {
        return salmonNpc;
    }

    public SheepNpc getSheepNpc() {
        return sheepNpc;
    }

    public ShulkerNpc getShulkerNpc() {
        return shulkerNpc;
    }

    public SilverfishNpc getSilverfishNpc() {
        return silverfishNpc;
    }

    public SkeletonNpc getSkeletonNpc() {
        return skeletonNpc;
    }

    public SkeletonHorseNpc getSkeletonHorseNpc() {
        return skeletonHorseNpc;
    }

    public SlimeNpc getSlimeNpc() {
        return slimeNpc;
    }

    public SnowmanNpc getSnowmanNpc() {
        return snowmanNpc;
    }

    public SpiderNpc getSpiderNpc() {
        return spiderNpc;
    }

    public SquidNpc getSquidNpc() {
        return squidNpc;
    }

    public StrayNpc getStrayNpc() {
        return strayNpc;
    }

    public TropicalFishNpc getTropicalFishNpc() {
        return tropicalFishNpc;
    }

    public TurtleNpc getTurtleNpc() {
        return turtleNpc;
    }

    public VexNpc getVexNpc() {
        return vexNpc;
    }

    public VillagerNpc getVillagerNpc() {
        return villagerNpc;
    }

    public VindicatorNpc getVindicatorNpc() {
        return vindicatorNpc;
    }

    public WitchNpc getWitchNpc() {
        return witchNpc;
    }

    public WitherNpc getWitherNpc() {
        return witherNpc;
    }

    public WitherSkeletonNpc getWitherSkeletonNpc() {
        return witherSkeletonNpc;
    }

    public WolfNpc getWolfNpc() {
        return wolfNpc;
    }

    public ZombieNpc getZombieNpc() {
        return zombieNpc;
    }

    public ZombieHorseNpc getZombieHorseNpc() {
        return zombieHorseNpc;
    }

    public ZombieVillagerNpc getZombieVillagerNpc() {
        return zombieVillagerNpc;
    }
}
