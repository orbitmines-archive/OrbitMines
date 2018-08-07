package com.orbitmines.spigot.api.nms;

import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.api.nms.actionbar.*;
import com.orbitmines.spigot.api.nms.anvilgui.*;
import com.orbitmines.spigot.api.nms.armorstand.*;
import com.orbitmines.spigot.api.nms.entity.*;
import com.orbitmines.spigot.api.nms.firework.*;
import com.orbitmines.spigot.api.nms.itemstack.*;
import com.orbitmines.spigot.api.nms.npc.*;
import com.orbitmines.spigot.api.nms.npc.chicken.*;
import com.orbitmines.spigot.api.nms.particles.*;
import com.orbitmines.spigot.api.nms.tablist.*;
import com.orbitmines.spigot.api.nms.title.*;
import com.orbitmines.spigot.api.nms.world.*;
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

//    private BatNpc batNpc;
//    private BlazeNpc blazeNpc;
//    private CaveSpiderNpc caveSpiderNpc;
    private ChickenNpc chickenNpc;
//    private CowNpc cowNpc;
//    private CreeperNpc creeperNpc;
//    private EndermanNpc endermanNpc;
//    private EndermiteNpc endermiteNpc;
//    private EvokerNpc evokerNpc;
//    private GhastNpc ghastNpc;
//    private GuardianNpc guardianNpc;
//    private HorseNpc horseNpc;
//    private IronGolemNpc ironGolemNpc;
//    private LlamaNpc llamaNpc;
//    private MagmaCubeNpc magmaCubeNpc;
//    private MuleNpc muleNpc;
//    private MushroomCowNpc mushroomCowNpc;
//    private OcelotNpc ocelotNpc;
//    private PigNpc pigNpc;
//    private PigZombieNpc pigZombieNpc;
//    private PolarBearNpc polarBearNpc;
//    private RabbitNpc rabbitNpc;
//    private SheepNpc sheepNpc;
//    private SilverfishNpc silverfishNpc;
//    private SkeletonNpc skeletonNpc;
//    private SkeletonHorseNpc skeletonHorseNpc;
//    private SlimeNpc slimeNpc;
//    private SnowmanNpc snowmanNpc;
//    private SpiderNpc spiderNpc;
//    private SquidNpc squidNpc;
//    private StrayNpc strayNpc;
//    private VexNpc vexNpc;
//    private VillagerNpc villagerNpc;
//    private VindicatorNpc vindicatorNpc;
//    private WitchNpc witchNpc;
//    private WitherSkeletonNpc witherSkeletonNpc;
//    private WitherNpc witherNpc;
//    private WolfNpc wolfNpc;
//    private ZombieNpc zombieNpc;
//    private ZombieHorseNpc zombieHorseNpc;
//    private ZombieHuskNpc zombieHuskNpc;

    /* Entity */
    private EntityNms entityNms;
    /* Title */
    private TitleNms titleNms;
    /* ActionBar */
    private ActionBarNms actionBarNms;
    /* ItemStack */
    private ItemStackNms itemStackNms;
    /* ArmorStand */
    private ArmorStandNms armorStandNms;
    /* Firework */
    private FireworkNms fireworkNms;
    /* Particles */
    private ParticleNms particleNms;
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

            case "v1_8_R1": // 1.8 - 1.8.1
                /* NPCs */
                npcNms = new NpcNms_1_8_R1();
                npc().setClassFields();

//                batNpc = new BatNpc_1_8_R1();
//                blazeNpc = new BlazeNpc_1_8_R1();
//                caveSpiderNpc = new CaveSpiderNpc_1_8_R1();
                chickenNpc = new ChickenNpc_v1_8_R1(npcNms);
//                cowNpc = new CowNpc_1_8_R1();
//                creeperNpc = new CreeperNpc_1_8_R1();
//                endermanNpc = new EndermanNpc_1_8_R1();
//                endermiteNpc = new EndermiteNpc_1_8_R1();
//                ghastNpc = new GhastNpc_1_8_R1();
//                guardianNpc = new GuardianNpc_1_8_R1();
//                horseNpc = new HorseNpc_1_8_R1();
//                ironGolemNpc = new IronGolemNpc_1_8_R1();
//                magmaCubeNpc = new MagmaCubeNpc_1_8_R1();
//                mushroomCowNpc = new MushroomCowNpc_1_8_R1();
//                ocelotNpc = new OcelotNpc_1_8_R1();
//                pigNpc = new PigNpc_1_8_R1();
//                pigZombieNpc = new PigZombieNpc_1_8_R1();
//                rabbitNpc = new RabbitNpc_1_8_R1();
//                sheepNpc = new SheepNpc_1_8_R1();
//                silverfishNpc = new SilverfishNpc_1_8_R1();
//                skeletonNpc = new SkeletonNpc_1_8_R1();
//                slimeNpc = new SlimeNpc_1_8_R1();
//                snowmanNpc = new SnowmanNpc_1_8_R1();
//                spiderNpc = new SpiderNpc_1_8_R1();
//                squidNpc = new SquidNpc_1_8_R1();
//                villagerNpc = new VillagerNpc_1_8_R1();
//                witchNpc = new WitchNpc_1_8_R1();
//                witherNpc = new WitherNpc_1_8_R1();
//                wolfNpc = new WolfNpc_1_8_R1();
//                zombieNpc = new ZombieNpc_1_8_R1();

                /* Entity */
                entityNms = new EntityNms_1_8_R1();
                /* Titles */
                titleNms = new TitleNms_1_8_R1();
                /* ActionBar */
                actionBarNms = new ActionBarNms_1_8_R1();
                /* ItemStack */
                itemStackNms = new ItemStackNms_1_8_R1();
                /* ArmorStand */
                armorStandNms = new ArmorStandNms_1_8_R1();
                /* Firework */
                fireworkNms = new FireworkNms_1_8_R1();
                /* Particles */
                particleNms = new ParticleNms_1_8_R1();
                /* TabList */
                tabListNms = new TabListNms_1_8_R1();
                /* World */
                worldNms = new WorldNms_1_8_R1();

                break;
            case "v1_8_R2": // 1.8.3
                /* NPCs */
                npcNms = new NpcNms_1_8_R2();
                npc().setClassFields();

//                batNpc = new BatNpc_1_8_R2();
//                blazeNpc = new BlazeNpc_1_8_R2();
//                caveSpiderNpc = new CaveSpiderNpc_1_8_R2();
                chickenNpc = new ChickenNpc_v1_8_R2(npcNms);
//                cowNpc = new CowNpc_1_8_R2();
//                creeperNpc = new CreeperNpc_1_8_R2();
//                endermanNpc = new EndermanNpc_1_8_R2();
//                endermiteNpc = new EndermiteNpc_1_8_R2();
//                ghastNpc = new GhastNpc_1_8_R2();
//                guardianNpc = new GuardianNpc_1_8_R2();
//                horseNpc = new HorseNpc_1_8_R2();
//                ironGolemNpc = new IronGolemNpc_1_8_R2();
//                magmaCubeNpc = new MagmaCubeNpc_1_8_R2();
//                mushroomCowNpc = new MushroomCowNpc_1_8_R2();
//                ocelotNpc = new OcelotNpc_1_8_R2();
//                pigNpc = new PigNpc_1_8_R2();
//                pigZombieNpc = new PigZombieNpc_1_8_R2();
//                rabbitNpc = new RabbitNpc_1_8_R2();
//                sheepNpc = new SheepNpc_1_8_R2();
//                silverfishNpc = new SilverfishNpc_1_8_R2();
//                skeletonNpc = new SkeletonNpc_1_8_R2();
//                slimeNpc = new SlimeNpc_1_8_R2();
//                snowmanNpc = new SnowmanNpc_1_8_R2();
//                spiderNpc = new SpiderNpc_1_8_R2();
//                squidNpc = new SquidNpc_1_8_R2();
//                villagerNpc = new VillagerNpc_1_8_R2();
//                witchNpc = new WitchNpc_1_8_R2();
//                witherNpc = new WitherNpc_1_8_R2();
//                wolfNpc = new WolfNpc_1_8_R2();
//                zombieNpc = new ZombieNpc_1_8_R2();

                /* Entity */
                entityNms = new EntityNms_1_8_R2();
                /* Titles */
                titleNms = new TitleNms_1_8_R2();
                /* ActionBar */
                actionBarNms = new ActionBarNms_1_8_R2();
                /* ItemStack */
                itemStackNms = new ItemStackNms_1_8_R2();
                /* ArmorStand */
                armorStandNms = new ArmorStandNms_1_8_R2();
                /* Firework */
                fireworkNms = new FireworkNms_1_8_R2();
                /* Particles */
                particleNms = new ParticleNms_1_8_R2();
                /* TabList */
                tabListNms = new TabListNms_1_8_R2();
                /* World */
                worldNms = new WorldNms_1_8_R2();

                break;
            case "v1_8_R3": // 1.8.7 - 1.8.9
                /* NPCs */
                npcNms = new NpcNms_1_8_R3();
                npc().setClassFields();

//                batNpc = new BatNpc_1_8_R3();
//                blazeNpc = new BlazeNpc_1_8_R3();
//                caveSpiderNpc = new CaveSpiderNpc_1_8_R3();
                chickenNpc = new ChickenNpc_v1_8_R3(npcNms);
//                cowNpc = new CowNpc_1_8_R3();
//                creeperNpc = new CreeperNpc_1_8_R3();
//                endermanNpc = new EndermanNpc_1_8_R3();
//                endermiteNpc = new EndermiteNpc_1_8_R3();
//                ghastNpc = new GhastNpc_1_8_R3();
//                guardianNpc = new GuardianNpc_1_8_R3();
//                horseNpc = new HorseNpc_1_8_R3();
//                ironGolemNpc = new IronGolemNpc_1_8_R3();
//                magmaCubeNpc = new MagmaCubeNpc_1_8_R3();
//                mushroomCowNpc = new MushroomCowNpc_1_8_R3();
//                ocelotNpc = new OcelotNpc_1_8_R3();
//                pigNpc = new PigNpc_1_8_R3();
//                pigZombieNpc = new PigZombieNpc_1_8_R3();
//                rabbitNpc = new RabbitNpc_1_8_R3();
//                sheepNpc = new SheepNpc_1_8_R3();
//                silverfishNpc = new SilverfishNpc_1_8_R3();
//                skeletonNpc = new SkeletonNpc_1_8_R3();
//                slimeNpc = new SlimeNpc_1_8_R3();
//                snowmanNpc = new SnowmanNpc_1_8_R3();
//                spiderNpc = new SpiderNpc_1_8_R3();
//                squidNpc = new SquidNpc_1_8_R3();
//                villagerNpc = new VillagerNpc_1_8_R3();
//                witchNpc = new WitchNpc_1_8_R3();
//                witherNpc = new WitherNpc_1_8_R3();
//                wolfNpc = new WolfNpc_1_8_R3();
//                zombieNpc = new ZombieNpc_1_8_R3();

                /* Entity */
                entityNms = new EntityNms_1_8_R3();
                /* Titles */
                titleNms = new TitleNms_1_8_R3();
                /* ActionBar */
                actionBarNms = new ActionBarNms_1_8_R3();
                /* ItemStack */
                itemStackNms = new ItemStackNms_1_8_R3();
                /* ArmorStand */
                armorStandNms = new ArmorStandNms_1_8_R3();
                /* Firework */
                fireworkNms = new FireworkNms_1_8_R3();
                /* Particles */
                particleNms = new ParticleNms_1_8_R3();
                /* TabList */
                tabListNms = new TabListNms_1_8_R3();
                /* World */
                worldNms = new WorldNms_1_8_R3();

                break;
            case "v1_9_R1": // 1.9 - 1.9.2
                /* NPCs */
                npcNms = new NpcNms_1_9_R1();
                npc().setClassFields();

//                batNpc = new BatNpc_1_9_R1();
//                blazeNpc = new BlazeNpc_1_9_R1();
//                caveSpiderNpc = new CaveSpiderNpc_1_9_R1();
                chickenNpc = new ChickenNpc_v1_9_R1(npcNms);
//                cowNpc = new CowNpc_1_9_R1();
//                creeperNpc = new CreeperNpc_1_9_R1();
//                endermanNpc = new EndermanNpc_1_9_R1();
//                endermiteNpc = new EndermiteNpc_1_9_R1();
//                ghastNpc = new GhastNpc_1_9_R1();
//                guardianNpc = new GuardianNpc_1_9_R1();
//                horseNpc = new HorseNpc_1_9_R1();
//                ironGolemNpc = new IronGolemNpc_1_9_R1();
//                magmaCubeNpc = new MagmaCubeNpc_1_9_R1();
//                mushroomCowNpc = new MushroomCowNpc_1_9_R1();
//                ocelotNpc = new OcelotNpc_1_9_R1();
//                pigNpc = new PigNpc_1_9_R1();
//                pigZombieNpc = new PigZombieNpc_1_9_R1();
//                rabbitNpc = new RabbitNpc_1_9_R1();
//                sheepNpc = new SheepNpc_1_9_R1();
//                silverfishNpc = new SilverfishNpc_1_9_R1();
//                skeletonNpc = new SkeletonNpc_1_9_R1();
//                slimeNpc = new SlimeNpc_1_9_R1();
//                snowmanNpc = new SnowmanNpc_1_9_R1();
//                spiderNpc = new SpiderNpc_1_9_R1();
//                squidNpc = new SquidNpc_1_9_R1();
//                villagerNpc = new VillagerNpc_1_9_R1();
//                witchNpc = new WitchNpc_1_9_R1();
//                witherNpc = new WitherNpc_1_9_R1();
//                wolfNpc = new WolfNpc_1_9_R1();
//                zombieNpc = new ZombieNpc_1_9_R1();

                /* Entity */
                entityNms = new EntityNms_1_9_R1();
                /* Titles */
                titleNms = new TitleNms_1_9_R1();
                /* ActionBar */
                actionBarNms = new ActionBarNms_1_9_R1();
                /* ItemStack */
                itemStackNms = new ItemStackNms_1_9_R1();
                /* ArmorStand */
                armorStandNms = new ArmorStandNms_1_9_R1();
                /* Firework */
                fireworkNms = new FireworkNms_1_9_R1();
                /* Particles */
                particleNms = new ParticleNms_1_9_R1();
                /* TabList */
                tabListNms = new TabListNms_1_9_R1();
                /* World */
                worldNms = new WorldNms_1_9_R1();

                break;
            case "v1_9_R2": // 1.9.4
                /* NPCs */
                npcNms = new NpcNms_1_9_R2();
                npc().setClassFields();

//                batNpc = new BatNpc_1_9_R2();
//                blazeNpc = new BlazeNpc_1_9_R2();
//                caveSpiderNpc = new CaveSpiderNpc_1_9_R2();
                chickenNpc = new ChickenNpc_v1_9_R2(npcNms);
//                cowNpc = new CowNpc_1_9_R2();
//                creeperNpc = new CreeperNpc_1_9_R2();
//                endermanNpc = new EndermanNpc_1_9_R2();
//                endermiteNpc = new EndermiteNpc_1_9_R2();
//                ghastNpc = new GhastNpc_1_9_R2();
//                guardianNpc = new GuardianNpc_1_9_R2();
//                horseNpc = new HorseNpc_1_9_R2();
//                ironGolemNpc = new IronGolemNpc_1_9_R2();
//                magmaCubeNpc = new MagmaCubeNpc_1_9_R2();
//                mushroomCowNpc = new MushroomCowNpc_1_9_R2();
//                ocelotNpc = new OcelotNpc_1_9_R2();
//                pigNpc = new PigNpc_1_9_R2();
//                pigZombieNpc = new PigZombieNpc_1_9_R2();
//                rabbitNpc = new RabbitNpc_1_9_R2();
//                sheepNpc = new SheepNpc_1_9_R2();
//                silverfishNpc = new SilverfishNpc_1_9_R2();
//                skeletonNpc = new SkeletonNpc_1_9_R2();
//                slimeNpc = new SlimeNpc_1_9_R2();
//                snowmanNpc = new SnowmanNpc_1_9_R2();
//                spiderNpc = new SpiderNpc_1_9_R2();
//                squidNpc = new SquidNpc_1_9_R2();
//                villagerNpc = new VillagerNpc_1_9_R2();
//                witchNpc = new WitchNpc_1_9_R2();
//                witherNpc = new WitherNpc_1_9_R2();
//                wolfNpc = new WolfNpc_1_9_R2();
//                zombieNpc = new ZombieNpc_1_9_R2();

                /* Entity */
                entityNms = new EntityNms_1_9_R2();
                /* Titles */
                titleNms = new TitleNms_1_9_R2();
                /* ActionBar */
                actionBarNms = new ActionBarNms_1_9_R2();
                /* ItemStack */
                itemStackNms = new ItemStackNms_1_9_R2();
                /* ArmorStand */
                armorStandNms = new ArmorStandNms_1_9_R2();
                /* Firework */
                fireworkNms = new FireworkNms_1_9_R2();
                /* Particles */
                particleNms = new ParticleNms_1_9_R2();
                /* TabList */
                tabListNms = new TabListNms_1_9_R2();
                /* World */
                worldNms = new WorldNms_1_9_R2();

                break;
            case "v1_10_R1": // 1.10 -  1.10.2
                /* NPCs */
                npcNms = new NpcNms_1_10_R1();
                npc().setClassFields();

//                batNpc = new BatNpc_1_10_R1();
//                blazeNpc = new BlazeNpc_1_10_R1();
//                caveSpiderNpc = new CaveSpiderNpc_1_10_R1();
                chickenNpc = new ChickenNpc_v1_10_R1(npcNms);
//                cowNpc = new CowNpc_1_10_R1();
//                creeperNpc = new CreeperNpc_1_10_R1();
//                endermanNpc = new EndermanNpc_1_10_R1();
//                endermiteNpc = new EndermiteNpc_1_10_R1();
//                ghastNpc = new GhastNpc_1_10_R1();
//                guardianNpc = new GuardianNpc_1_10_R1();
//                horseNpc = new HorseNpc_1_10_R1();
//                ironGolemNpc = new IronGolemNpc_1_10_R1();
//                magmaCubeNpc = new MagmaCubeNpc_1_10_R1();
//                mushroomCowNpc = new MushroomCowNpc_1_10_R1();
//                ocelotNpc = new OcelotNpc_1_10_R1();
//                pigNpc = new PigNpc_1_10_R1();
//                pigZombieNpc = new PigZombieNpc_1_10_R1();
//                polarBearNpc = new PolarBearNpc_1_10_R1();
//                rabbitNpc = new RabbitNpc_1_10_R1();
//                sheepNpc = new SheepNpc_1_10_R1();
//                silverfishNpc = new SilverfishNpc_1_10_R1();
//                skeletonNpc = new SkeletonNpc_1_10_R1();
//                slimeNpc = new SlimeNpc_1_10_R1();
//                snowmanNpc = new SnowmanNpc_1_10_R1();
//                spiderNpc = new SpiderNpc_1_10_R1();
//                squidNpc = new SquidNpc_1_10_R1();
//                villagerNpc = new VillagerNpc_1_10_R1();
//                witchNpc = new WitchNpc_1_10_R1();
//                witherNpc = new WitherNpc_1_10_R1();
//                wolfNpc = new WolfNpc_1_10_R1();
//                zombieNpc = new ZombieNpc_1_10_R1();

                /* Entity */
                entityNms = new EntityNms_1_10_R1();
                /* Titles */
                titleNms = new TitleNms_1_10_R1();
                /* ActionBar */
                actionBarNms = new ActionBarNms_1_10_R1();
                /* ItemStack */
                itemStackNms = new ItemStackNms_1_10_R1();
                /* ArmorStand */
                armorStandNms = new ArmorStandNms_1_10_R1();
                /* Firework */
                fireworkNms = new FireworkNms_1_10_R1();
                /* Particles */
                particleNms = new ParticleNms_1_10_R1();
                /* TabList */
                tabListNms = new TabListNms_1_10_R1();
                /* World */
                worldNms = new WorldNms_1_10_R1();

                break;
            case "v1_11_R1": // 1.11 - 1.11.2
                /* NPCs */
                npcNms = new NpcNms_1_11_R1();
                npc().setClassFields();

//                batNpc = new BatNpc_1_11_R1();
//                blazeNpc = new BlazeNpc_1_11_R1();
//                caveSpiderNpc = new CaveSpiderNpc_1_11_R1();
                chickenNpc = new ChickenNpc_v1_11_R1(npcNms);
//                cowNpc = new CowNpc_1_11_R1();
//                creeperNpc = new CreeperNpc_1_11_R1();
//                endermanNpc = new EndermanNpc_1_11_R1();
//                endermiteNpc = new EndermiteNpc_1_11_R1();
//                evokerNpc = new EvokerNpc_1_11_R1();
//                ghastNpc = new GhastNpc_1_11_R1();
//                guardianNpc = new GuardianNpc_1_11_R1();
//                horseNpc = new HorseNpc_1_11_R1();
//                ironGolemNpc = new IronGolemNpc_1_11_R1();
//                llamaNpc = new LlamaNpc_1_11_R1();
//                magmaCubeNpc = new MagmaCubeNpc_1_11_R1();
//                muleNpc = new MuleNpc_1_11_R1();
//                mushroomCowNpc = new MushroomCowNpc_1_11_R1();
//                ocelotNpc = new OcelotNpc_1_11_R1();
//                pigNpc = new PigNpc_1_11_R1();
//                pigZombieNpc = new PigZombieNpc_1_11_R1();
//                polarBearNpc = new PolarBearNpc_1_11_R1();
//                rabbitNpc = new RabbitNpc_1_11_R1();
//                sheepNpc = new SheepNpc_1_11_R1();
//                silverfishNpc = new SilverfishNpc_1_11_R1();
//                skeletonNpc = new SkeletonNpc_1_11_R1();
//                skeletonHorseNpc = new SkeletonHorseNpc_1_11_R1();
//                slimeNpc = new SlimeNpc_1_11_R1();
//                snowmanNpc = new SnowmanNpc_1_11_R1();
//                spiderNpc = new SpiderNpc_1_11_R1();
//                squidNpc = new SquidNpc_1_11_R1();
//                strayNpc = new StrayNpc_1_11_R1();
//                vexNpc = new VexNpc_1_11_R1();
//                villagerNpc = new VillagerNpc_1_11_R1();
//                vindicatorNpc = new VindicatorNpc_1_11_R1();
//                witchNpc = new WitchNpc_1_11_R1();
//                witherNpc = new WitherNpc_1_11_R1();
//                witherSkeletonNpc = new WitherSkeletonNpc_1_11_R1();
//                wolfNpc = new WolfNpc_1_11_R1();
//                zombieNpc = new ZombieNpc_1_11_R1();
//                zombieHorseNpc = new ZombieHorseNpc_1_11_R1();
//                zombieHuskNpc = new ZombieHuskNpc_1_11_R1();

                /* Entity */
                entityNms = new EntityNms_1_11_R1();
                /* Titles */
                titleNms = new TitleNms_1_11_R1();
                /* ActionBar */
                actionBarNms = new ActionBarNms_1_11_R1();
                /* ItemStack */
                itemStackNms = new ItemStackNms_1_11_R1();
                /* ArmorStand */
                armorStandNms = new ArmorStandNms_1_11_R1();
                /* Firework */
                fireworkNms = new FireworkNms_1_11_R1();
                /* Particles */
                particleNms = new ParticleNms_1_11_R1();
                /* TabList */
                tabListNms = new TabListNms_1_11_R1();
                /* World */
                worldNms = new WorldNms_1_11_R1();

                break;
            case "v1_12_R1": // 1.12 - ?
                /* NPCs */
                npcNms = new NpcNms_1_12_R1();
                npc().setClassFields();

//                batNpc = new BatNpc_1_12_R1();
//                blazeNpc = new BlazeNpc_1_12_R1();
//                caveSpiderNpc = new CaveSpiderNpc_1_12_R1();
                chickenNpc = new ChickenNpc_v1_12_R1(npcNms);
//                cowNpc = new CowNpc_1_12_R1();
//                creeperNpc = new CreeperNpc_1_12_R1();
//                endermanNpc = new EndermanNpc_1_12_R1();
//                endermiteNpc = new EndermiteNpc_1_12_R1();
//                evokerNpc = new EvokerNpc_1_12_R1();
//                ghastNpc = new GhastNpc_1_12_R1();
//                guardianNpc = new GuardianNpc_1_12_R1();
//                horseNpc = new HorseNpc_1_12_R1();
//                ironGolemNpc = new IronGolemNpc_1_12_R1();
//                llamaNpc = new LlamaNpc_1_12_R1();
//                magmaCubeNpc = new MagmaCubeNpc_1_12_R1();
//                muleNpc = new MuleNpc_1_12_R1();
//                mushroomCowNpc = new MushroomCowNpc_1_12_R1();
//                ocelotNpc = new OcelotNpc_1_12_R1();
//                pigNpc = new PigNpc_1_12_R1();
//                pigZombieNpc = new PigZombieNpc_1_12_R1();
//                polarBearNpc = new PolarBearNpc_1_12_R1();
//                rabbitNpc = new RabbitNpc_1_12_R1();
//                sheepNpc = new SheepNpc_1_12_R1();
//                silverfishNpc = new SilverfishNpc_1_12_R1();
//                skeletonNpc = new SkeletonNpc_1_12_R1();
//                skeletonHorseNpc = new SkeletonHorseNpc_1_12_R1();
//                slimeNpc = new SlimeNpc_1_12_R1();
//                snowmanNpc = new SnowmanNpc_1_12_R1();
//                spiderNpc = new SpiderNpc_1_12_R1();
//                squidNpc = new SquidNpc_1_12_R1();
//                strayNpc = new StrayNpc_1_12_R1();
//                vexNpc = new VexNpc_1_12_R1();
//                villagerNpc = new VillagerNpc_1_12_R1();
//                vindicatorNpc = new VindicatorNpc_1_12_R1();
//                witchNpc = new WitchNpc_1_12_R1();
//                witherNpc = new WitherNpc_1_12_R1();
//                witherSkeletonNpc = new WitherSkeletonNpc_1_12_R1();
//                wolfNpc = new WolfNpc_1_12_R1();
//                zombieNpc = new ZombieNpc_1_12_R1();
//                zombieHorseNpc = new ZombieHorseNpc_1_12_R1();
//                zombieHuskNpc = new ZombieHuskNpc_1_12_R1();

                /* Entity */
                entityNms = new EntityNms_1_12_R1();
                /* Titles */
                titleNms = new TitleNms_1_12_R1();
                /* ActionBar */
                actionBarNms = new ActionBarNms_1_12_R1();
                /* ItemStack */
                itemStackNms = new ItemStackNms_1_12_R1();
                /* ArmorStand */
                armorStandNms = new ArmorStandNms_1_12_R1();
                /* Firework */
                fireworkNms = new FireworkNms_1_12_R1();
                /* Particles */
                particleNms = new ParticleNms_1_12_R1();
                /* TabList */
                tabListNms = new TabListNms_1_12_R1();
                /* World */
                worldNms = new WorldNms_1_12_R1();

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

    public TitleNms title() {
        return titleNms;
    }

    public ActionBarNms actionBar() {
        return actionBarNms;
    }

    public AnvilNms anvilGui(Player player, AnvilNms.AnvilClickEventHandler handler) {
        return anvilGui(player, handler, null);
    }

    public AnvilNms anvilGui(Player player, AnvilNms.AnvilClickEventHandler handler, AnvilNms.AnvilCloseEvent closeEvent) {
        switch (version) {
            case "v1_8_R1": // 1.8 - 1.8.1
                return new AnvilNms_1_8_R1(player, handler, closeEvent);
            case "v1_8_R2": // 1.8.3
                return new AnvilNms_1_8_R2(player, handler, closeEvent);
            case "v1_8_R3": // 1.8.7 - 1.8.9
                return new AnvilNms_1_8_R3(player, handler, closeEvent);
            case "v1_9_R1": // 1.9 - 1.9.2
                return new AnvilNms_1_9_R1(player, handler, closeEvent);
            case "v1_9_R2": // 1.9.4
                return new AnvilNms_1_9_R2(player, handler, closeEvent);
            case "v1_10_R1": // 1.10 - 1.10.2
                return new AnvilNms_1_10_R1(player, handler, closeEvent);
            case "v1_11_R1": // 1.11 - 1.11.2
                return new AnvilNms_1_11_R1(player, handler, closeEvent);
            case "v1_12_R1": // 1.12 - ?
                return new AnvilNms_1_12_R1(player, handler, closeEvent);
        }
        return null;
    }

    public ItemStackNms customItem() {
        return itemStackNms;
    }

    public ArmorStandNms armorStand() {
        return armorStandNms;
    }

    public FireworkNms firework() {
        return fireworkNms;
    }

    public ParticleNms particle() {
        return particleNms;
    }

    public TabListNms tabList() {
        return tabListNms;
    }

    /*
        Npcs
     */

//    public BatNpc getBatNpc() {
//        return batNpc;
//    }
//
//    public BlazeNpc getBlazeNpc() {
//        return blazeNpc;
//    }
//
//    public CaveSpiderNpc getCaveSpiderNpc() {
//        return caveSpiderNpc;
//    }

    public ChickenNpc getChickenNpc() {
        return chickenNpc;
    }
//
//    public CowNpc getCowNpc() {
//        return cowNpc;
//    }
//
//    public CreeperNpc getCreeperNpc() {
//        return creeperNpc;
//    }
//
//    public EndermanNpc getEndermanNpc() {
//        return endermanNpc;
//    }
//
//    public EndermiteNpc getEndermiteNpc() {
//        return endermiteNpc;
//    }
//
//    public EvokerNpc getEvokerNpc() {
//        return evokerNpc;
//    }
//
//    public GhastNpc getGhastNpc() {
//        return ghastNpc;
//    }
//
//    public GuardianNpc getGuardianNpc() {
//        return guardianNpc;
//    }
//
//    public HorseNpc getHorseNpc() {
//        return horseNpc;
//    }
//
//    public IronGolemNpc getIronGolemNpc() {
//        return ironGolemNpc;
//    }
//
//    public LlamaNpc getLlamaNpc() {
//        return llamaNpc;
//    }
//
//    public MagmaCubeNpc getMagmaCubeNpc() {
//        return magmaCubeNpc;
//    }
//
//    public MuleNpc getMuleNpc() {
//        return muleNpc;
//    }
//
//    public MushroomCowNpc getMushroomCowNpc() {
//        return mushroomCowNpc;
//    }
//
//    public OcelotNpc getOcelotNpc() {
//        return ocelotNpc;
//    }
//
//    public PigNpc getPigNpc() {
//        return pigNpc;
//    }
//
//    public PigZombieNpc getPigZombieNpc() {
//        return pigZombieNpc;
//    }
//
//    public PolarBearNpc getPolarBearNpc() {
//        return polarBearNpc;
//    }
//
//    public RabbitNpc getRabbitNpc() {
//        return rabbitNpc;
//    }
//
//    public SheepNpc getSheepNpc() {
//        return sheepNpc;
//    }
//
//    public SilverfishNpc getSilverfishNpc() {
//        return silverfishNpc;
//    }
//
//    public SkeletonNpc getSkeletonNpc() {
//        return skeletonNpc;
//    }
//
//    public SkeletonHorseNpc getSkeletonHorseNpc() {
//        return skeletonHorseNpc;
//    }
//
//    public SlimeNpc getSlimeNpc() {
//        return slimeNpc;
//    }
//
//    public SnowmanNpc getSnowmanNpc() {
//        return snowmanNpc;
//    }
//
//    public SpiderNpc getSpiderNpc() {
//        return spiderNpc;
//    }
//
//    public SquidNpc getSquidNpc() {
//        return squidNpc;
//    }
//
//    public StrayNpc getStrayNpc() {
//        return strayNpc;
//    }
//
//    public VexNpc getVexNpc() {
//        return vexNpc;
//    }
//
//    public VillagerNpc getVillagerNpc() {
//        return villagerNpc;
//    }
//
//    public VindicatorNpc getVindicatorNpc() {
//        return vindicatorNpc;
//    }
//
//    public WitchNpc getWitchNpc() {
//        return witchNpc;
//    }
//
//    public WitherNpc getWitherNpc() {
//        return witherNpc;
//    }
//
//    public WitherSkeletonNpc getWitherSkeletonNpc() {
//        return witherSkeletonNpc;
//    }
//
//    public WolfNpc getWolfNpc() {
//        return wolfNpc;
//    }
//
//    public ZombieNpc getZombieNpc() {
//        return zombieNpc;
//    }
//
//    public ZombieHorseNpc getZombieHorseNpc() {
//        return zombieHorseNpc;
//    }
//
//    public ZombieHuskNpc getZombieHuskNpc() {
//        return zombieHuskNpc;
//    }
}
