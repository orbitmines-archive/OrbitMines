package com.orbitmines.spigot.api.nms;

import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.api.nms.actionbar.*;
import com.orbitmines.spigot.api.nms.anvilgui.*;
import com.orbitmines.spigot.api.nms.armorstand.*;
import com.orbitmines.spigot.api.nms.customitem.*;
import com.orbitmines.spigot.api.nms.entity.*;
import com.orbitmines.spigot.api.nms.firework.*;
import com.orbitmines.spigot.api.nms.npc.*;
import com.orbitmines.spigot.api.nms.npc.bat.*;
import com.orbitmines.spigot.api.nms.npc.blaze.*;
import com.orbitmines.spigot.api.nms.npc.cavespider.*;
import com.orbitmines.spigot.api.nms.npc.chicken.*;
import com.orbitmines.spigot.api.nms.npc.cow.*;
import com.orbitmines.spigot.api.nms.npc.creeper.*;
import com.orbitmines.spigot.api.nms.npc.enderman.*;
import com.orbitmines.spigot.api.nms.npc.endermite.*;
import com.orbitmines.spigot.api.nms.npc.evoker.EvokerNpc;
import com.orbitmines.spigot.api.nms.npc.evoker.EvokerNpc_1_11_R1;
import com.orbitmines.spigot.api.nms.npc.evoker.EvokerNpc_1_12_R1;
import com.orbitmines.spigot.api.nms.npc.ghast.*;
import com.orbitmines.spigot.api.nms.npc.guardian.*;
import com.orbitmines.spigot.api.nms.npc.horse.*;
import com.orbitmines.spigot.api.nms.npc.irongolem.*;
import com.orbitmines.spigot.api.nms.npc.llama.LlamaNpc;
import com.orbitmines.spigot.api.nms.npc.llama.LlamaNpc_1_11_R1;
import com.orbitmines.spigot.api.nms.npc.llama.LlamaNpc_1_12_R1;
import com.orbitmines.spigot.api.nms.npc.magmacube.*;
import com.orbitmines.spigot.api.nms.npc.mule.MuleNpc;
import com.orbitmines.spigot.api.nms.npc.mule.MuleNpc_1_11_R1;
import com.orbitmines.spigot.api.nms.npc.mule.MuleNpc_1_12_R1;
import com.orbitmines.spigot.api.nms.npc.mushroomcow.*;
import com.orbitmines.spigot.api.nms.npc.ocelot.*;
import com.orbitmines.spigot.api.nms.npc.pig.*;
import com.orbitmines.spigot.api.nms.npc.pigzombie.*;
import com.orbitmines.spigot.api.nms.npc.polarbear.PolarBearNpc;
import com.orbitmines.spigot.api.nms.npc.polarbear.PolarBearNpc_1_10_R1;
import com.orbitmines.spigot.api.nms.npc.polarbear.PolarBearNpc_1_11_R1;
import com.orbitmines.spigot.api.nms.npc.polarbear.PolarBearNpc_1_12_R1;
import com.orbitmines.spigot.api.nms.npc.rabbit.*;
import com.orbitmines.spigot.api.nms.npc.sheep.*;
import com.orbitmines.spigot.api.nms.npc.silverfish.*;
import com.orbitmines.spigot.api.nms.npc.skeleton.*;
import com.orbitmines.spigot.api.nms.npc.skeletonhorse.SkeletonHorseNpc;
import com.orbitmines.spigot.api.nms.npc.skeletonhorse.SkeletonHorseNpc_1_11_R1;
import com.orbitmines.spigot.api.nms.npc.skeletonhorse.SkeletonHorseNpc_1_12_R1;
import com.orbitmines.spigot.api.nms.npc.slime.*;
import com.orbitmines.spigot.api.nms.npc.snowman.*;
import com.orbitmines.spigot.api.nms.npc.spider.*;
import com.orbitmines.spigot.api.nms.npc.squid.*;
import com.orbitmines.spigot.api.nms.npc.stray.StrayNpc;
import com.orbitmines.spigot.api.nms.npc.stray.StrayNpc_1_11_R1;
import com.orbitmines.spigot.api.nms.npc.stray.StrayNpc_1_12_R1;
import com.orbitmines.spigot.api.nms.npc.vex.VexNpc;
import com.orbitmines.spigot.api.nms.npc.vex.VexNpc_1_11_R1;
import com.orbitmines.spigot.api.nms.npc.vex.VexNpc_1_12_R1;
import com.orbitmines.spigot.api.nms.npc.villager.*;
import com.orbitmines.spigot.api.nms.npc.vindicator.VindicatorNpc;
import com.orbitmines.spigot.api.nms.npc.vindicator.VindicatorNpc_1_11_R1;
import com.orbitmines.spigot.api.nms.npc.vindicator.VindicatorNpc_1_12_R1;
import com.orbitmines.spigot.api.nms.npc.witch.*;
import com.orbitmines.spigot.api.nms.npc.wither.*;
import com.orbitmines.spigot.api.nms.npc.witherskeleton.WitherSkeletonNpc;
import com.orbitmines.spigot.api.nms.npc.witherskeleton.WitherSkeletonNpc_1_11_R1;
import com.orbitmines.spigot.api.nms.npc.witherskeleton.WitherSkeletonNpc_1_12_R1;
import com.orbitmines.spigot.api.nms.npc.wolf.*;
import com.orbitmines.spigot.api.nms.npc.zombie.*;
import com.orbitmines.spigot.api.nms.npc.zombiehorse.ZombieHorseNpc;
import com.orbitmines.spigot.api.nms.npc.zombiehorse.ZombieHorseNpc_1_11_R1;
import com.orbitmines.spigot.api.nms.npc.zombiehorse.ZombieHorseNpc_1_12_R1;
import com.orbitmines.spigot.api.nms.npc.zombiehusk.ZombieHuskNpc;
import com.orbitmines.spigot.api.nms.npc.zombiehusk.ZombieHuskNpc_1_11_R1;
import com.orbitmines.spigot.api.nms.npc.zombiehusk.ZombieHuskNpc_1_12_R1;
import com.orbitmines.spigot.api.nms.particles.*;
import com.orbitmines.spigot.api.nms.pet.chicken.*;
import com.orbitmines.spigot.api.nms.pet.cow.*;
import com.orbitmines.spigot.api.nms.pet.creeper.*;
import com.orbitmines.spigot.api.nms.pet.magmacube.*;
import com.orbitmines.spigot.api.nms.pet.mushroomcow.*;
import com.orbitmines.spigot.api.nms.pet.ocelot.*;
import com.orbitmines.spigot.api.nms.pet.pig.*;
import com.orbitmines.spigot.api.nms.pet.sheep.*;
import com.orbitmines.spigot.api.nms.pet.silverfish.*;
import com.orbitmines.spigot.api.nms.pet.slime.*;
import com.orbitmines.spigot.api.nms.pet.spider.*;
import com.orbitmines.spigot.api.nms.pet.squid.*;
import com.orbitmines.spigot.api.nms.pet.wolf.*;
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

    private BatNpc batNpc;
    private BlazeNpc blazeNpc;
    private CaveSpiderNpc caveSpiderNpc;
    private ChickenNpc chickenNpc;
    private CowNpc cowNpc;
    private CreeperNpc creeperNpc;
    private EndermanNpc endermanNpc;
    private EndermiteNpc endermiteNpc;
    private EvokerNpc evokerNpc;
    private GhastNpc ghastNpc;
    private GuardianNpc guardianNpc;
    private HorseNpc horseNpc;
    private IronGolemNpc ironGolemNpc;
    private LlamaNpc llamaNpc;
    private MagmaCubeNpc magmaCubeNpc;
    private MuleNpc muleNpc;
    private MushroomCowNpc mushroomCowNpc;
    private OcelotNpc ocelotNpc;
    private PigNpc pigNpc;
    private PigZombieNpc pigZombieNpc;
    private PolarBearNpc polarBearNpc;
    private RabbitNpc rabbitNpc;
    private SheepNpc sheepNpc;
    private SilverfishNpc silverfishNpc;
    private SkeletonNpc skeletonNpc;
    private SkeletonHorseNpc skeletonHorseNpc;
    private SlimeNpc slimeNpc;
    private SnowmanNpc snowmanNpc;
    private SpiderNpc spiderNpc;
    private SquidNpc squidNpc;
    private StrayNpc strayNpc;
    private VexNpc vexNpc;
    private VillagerNpc villagerNpc;
    private VindicatorNpc vindicatorNpc;
    private WitchNpc witchNpc;
    private WitherSkeletonNpc witherSkeletonNpc;
    private WitherNpc witherNpc;
    private WolfNpc wolfNpc;
    private ZombieNpc zombieNpc;
    private ZombieHorseNpc zombieHorseNpc;
    private ZombieHuskNpc zombieHuskNpc;

    /* Entity */
    private EntityNms entityNms;
    /* Title */
    private TitleNms titleNms;
    /* ActionBar */
    private ActionBarNms actionBarNms;
    /* CustomItem */
    private CustomItemNms customItemNms;
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

    /* Pets */
//    private BatPet batPet;
//    private BlazePet blazePet;
//    private CaveSpiderPet caveSpiderPet;
    private ChickenPet chickenPet;
    private CowPet cowPet;
    private CreeperPet creeperPet;
    //    private EndermanPet endermanPet;
//    private EndermitePet endermitePet;
//    private EvokerPet evokerPet;
//    private GhastPet ghastPet;
//    private GuardianPet guardianPet;
//    private HorsePet horsePet;
//    private IronGolemPet ironGolemPet;
//    private LlamaPet llamaPet;
    private MagmaCubePet magmaCubePet;
    //    private MulePet mulePet;
    private MushroomCowPet mushroomCowPet;
    private OcelotPet ocelotPet;
    private PigPet pigPet;
    //    private PigZombiePet pigZombiePet;
//    private PolarBearPet polarBearPet;
//    private RabbitPet rabbitPet;
    private SheepPet sheepPet;
    private SilverfishPet silverfishPet;
    //    private SkeletonPet skeletonPet;
//    private SkeletonHorsePet skeletonHorsePet;
    private SlimePet slimePet;
    //    private SnowmanPet snowmanPet;
    private SpiderPet spiderPet;
    private SquidPet squidPet;
    //    private StrayPet strayPet;
//    private VexPet vexPet;
//    private VillagerPet villagerPet;
//    private VindicatorPet vindicatorPet;
//    private WitchPet witchPet;
//    private WitherSkeletonPet witherSkeletonPet;
//    private WitherPet witherPet;
    private WolfPet wolfPet;
//    private ZombiePet zombiePet;
//    private ZombieHorsePet zombieHorsePet;
//    private ZombieHuskPet zombieHuskPet;

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

                batNpc = new BatNpc_1_8_R1();
                blazeNpc = new BlazeNpc_1_8_R1();
                caveSpiderNpc = new CaveSpiderNpc_1_8_R1();
                chickenNpc = new ChickenNpc_1_8_R1();
                cowNpc = new CowNpc_1_8_R1();
                creeperNpc = new CreeperNpc_1_8_R1();
                endermanNpc = new EndermanNpc_1_8_R1();
                endermiteNpc = new EndermiteNpc_1_8_R1();
                ghastNpc = new GhastNpc_1_8_R1();
                guardianNpc = new GuardianNpc_1_8_R1();
                horseNpc = new HorseNpc_1_8_R1();
                ironGolemNpc = new IronGolemNpc_1_8_R1();
                magmaCubeNpc = new MagmaCubeNpc_1_8_R1();
                mushroomCowNpc = new MushroomCowNpc_1_8_R1();
                ocelotNpc = new OcelotNpc_1_8_R1();
                pigNpc = new PigNpc_1_8_R1();
                pigZombieNpc = new PigZombieNpc_1_8_R1();
                rabbitNpc = new RabbitNpc_1_8_R1();
                sheepNpc = new SheepNpc_1_8_R1();
                silverfishNpc = new SilverfishNpc_1_8_R1();
                skeletonNpc = new SkeletonNpc_1_8_R1();
                slimeNpc = new SlimeNpc_1_8_R1();
                snowmanNpc = new SnowmanNpc_1_8_R1();
                spiderNpc = new SpiderNpc_1_8_R1();
                squidNpc = new SquidNpc_1_8_R1();
                villagerNpc = new VillagerNpc_1_8_R1();
                witchNpc = new WitchNpc_1_8_R1();
                witherNpc = new WitherNpc_1_8_R1();
                wolfNpc = new WolfNpc_1_8_R1();
                zombieNpc = new ZombieNpc_1_8_R1();

                /* Entity */
                entityNms = new EntityNms_1_8_R1();
                /* Titles */
                titleNms = new TitleNms_1_8_R1();
                /* ActionBar */
                actionBarNms = new ActionBarNms_1_8_R1();
                /* CustomItem */
                customItemNms = new CustomItemNms_1_8_R1();
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
                
                /* Pets */
//                batPet = new BatPet_1_8_R1();
//                blazePet = new BlazePet_1_8_R1();
//                caveSpiderPet = new CaveSpiderPet_1_8_R1();
                chickenPet = new ChickenPet_1_8_R1();
                cowPet = new CowPet_1_8_R1();
                creeperPet = new CreeperPet_1_8_R1();
//                endermanPet = new EndermanPet_1_8_R1();
//                endermitePet = new EndermitePet_1_8_R1();
//                ghastPet = new GhastPet_1_8_R1();
//                guardianPet = new GuardianPet_1_8_R1();
//                horsePet = new HorsePet_1_8_R1();
//                ironGolemPet = new IronGolemPet_1_8_R1();
                magmaCubePet = new MagmaCubePet_1_8_R1();
                mushroomCowPet = new MushroomCowPet_1_8_R1();
                ocelotPet = new OcelotPet_1_8_R1();
                pigPet = new PigPet_1_8_R1();
//                pigZombiePet = new PigZombiePet_1_8_R1();
//                rabbitPet = new RabbitPet_1_8_R1();
                sheepPet = new SheepPet_1_8_R1();
                silverfishPet = new SilverfishPet_1_8_R1();
//                skeletonPet = new SkeletonPet_1_8_R1();
                slimePet = new SlimePet_1_8_R1();
//                snowmanPet = new SnowmanPet_1_8_R1();
                spiderPet = new SpiderPet_1_8_R1();
                squidPet = new SquidPet_1_8_R1();
//                villagerPet = new VillagerPet_1_8_R1();
//                witchPet = new WitchPet_1_8_R1();
//                witherPet = new WitherPet_1_8_R1();
                wolfPet = new WolfPet_1_8_R1();
//                zombiePet = new ZombiePet_1_8_R1();

                break;
            case "v1_8_R2": // 1.8.3
                /* NPCs */
                npcNms = new NpcNms_1_8_R2();
                npc().setClassFields();

                batNpc = new BatNpc_1_8_R2();
                blazeNpc = new BlazeNpc_1_8_R2();
                caveSpiderNpc = new CaveSpiderNpc_1_8_R2();
                chickenNpc = new ChickenNpc_1_8_R2();
                cowNpc = new CowNpc_1_8_R2();
                creeperNpc = new CreeperNpc_1_8_R2();
                endermanNpc = new EndermanNpc_1_8_R2();
                endermiteNpc = new EndermiteNpc_1_8_R2();
                ghastNpc = new GhastNpc_1_8_R2();
                guardianNpc = new GuardianNpc_1_8_R2();
                horseNpc = new HorseNpc_1_8_R2();
                ironGolemNpc = new IronGolemNpc_1_8_R2();
                magmaCubeNpc = new MagmaCubeNpc_1_8_R2();
                mushroomCowNpc = new MushroomCowNpc_1_8_R2();
                ocelotNpc = new OcelotNpc_1_8_R2();
                pigNpc = new PigNpc_1_8_R2();
                pigZombieNpc = new PigZombieNpc_1_8_R2();
                rabbitNpc = new RabbitNpc_1_8_R2();
                sheepNpc = new SheepNpc_1_8_R2();
                silverfishNpc = new SilverfishNpc_1_8_R2();
                skeletonNpc = new SkeletonNpc_1_8_R2();
                slimeNpc = new SlimeNpc_1_8_R2();
                snowmanNpc = new SnowmanNpc_1_8_R2();
                spiderNpc = new SpiderNpc_1_8_R2();
                squidNpc = new SquidNpc_1_8_R2();
                villagerNpc = new VillagerNpc_1_8_R2();
                witchNpc = new WitchNpc_1_8_R2();
                witherNpc = new WitherNpc_1_8_R2();
                wolfNpc = new WolfNpc_1_8_R2();
                zombieNpc = new ZombieNpc_1_8_R2();

                /* Entity */
                entityNms = new EntityNms_1_8_R2();
                /* Titles */
                titleNms = new TitleNms_1_8_R2();
                /* ActionBar */
                actionBarNms = new ActionBarNms_1_8_R2();
                /* CustomItem */
                customItemNms = new CustomItemNms_1_8_R2();
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
                
                /* Pets */
//                batPet = new BatPet_1_8_R2();
//                blazePet = new BlazePet_1_8_R2();
//                caveSpiderPet = new CaveSpiderPet_1_8_R2();
                chickenPet = new ChickenPet_1_8_R2();
                cowPet = new CowPet_1_8_R2();
                creeperPet = new CreeperPet_1_8_R2();
//                endermanPet = new EndermanPet_1_8_R2();
//                endermitePet = new EndermitePet_1_8_R2();
//                ghastPet = new GhastPet_1_8_R2();
//                guardianPet = new GuardianPet_1_8_R2();
//                horsePet = new HorsePet_1_8_R2();
//                ironGolemPet = new IronGolemPet_1_8_R2();
                magmaCubePet = new MagmaCubePet_1_8_R2();
                mushroomCowPet = new MushroomCowPet_1_8_R2();
                ocelotPet = new OcelotPet_1_8_R2();
                pigPet = new PigPet_1_8_R2();
//                pigZombiePet = new PigZombiePet_1_8_R2();
//                rabbitPet = new RabbitPet_1_8_R2();
                sheepPet = new SheepPet_1_8_R2();
                silverfishPet = new SilverfishPet_1_8_R2();
//                skeletonPet = new SkeletonPet_1_8_R2();
                slimePet = new SlimePet_1_8_R2();
//                snowmanPet = new SnowmanPet_1_8_R2();
                spiderPet = new SpiderPet_1_8_R2();
                squidPet = new SquidPet_1_8_R2();
//                villagerPet = new VillagerPet_1_8_R2();
//                witchPet = new WitchPet_1_8_R2();
//                witherPet = new WitherPet_1_8_R2();
                wolfPet = new WolfPet_1_8_R2();
//                zombiePet = new ZombiePet_1_8_R2();

                break;
            case "v1_8_R3": // 1.8.7 - 1.8.9
                /* NPCs */
                npcNms = new NpcNms_1_8_R3();
                npc().setClassFields();

                batNpc = new BatNpc_1_8_R3();
                blazeNpc = new BlazeNpc_1_8_R3();
                caveSpiderNpc = new CaveSpiderNpc_1_8_R3();
                chickenNpc = new ChickenNpc_1_8_R3();
                cowNpc = new CowNpc_1_8_R3();
                creeperNpc = new CreeperNpc_1_8_R3();
                endermanNpc = new EndermanNpc_1_8_R3();
                endermiteNpc = new EndermiteNpc_1_8_R3();
                ghastNpc = new GhastNpc_1_8_R3();
                guardianNpc = new GuardianNpc_1_8_R3();
                horseNpc = new HorseNpc_1_8_R3();
                ironGolemNpc = new IronGolemNpc_1_8_R3();
                magmaCubeNpc = new MagmaCubeNpc_1_8_R3();
                mushroomCowNpc = new MushroomCowNpc_1_8_R3();
                ocelotNpc = new OcelotNpc_1_8_R3();
                pigNpc = new PigNpc_1_8_R3();
                pigZombieNpc = new PigZombieNpc_1_8_R3();
                rabbitNpc = new RabbitNpc_1_8_R3();
                sheepNpc = new SheepNpc_1_8_R3();
                silverfishNpc = new SilverfishNpc_1_8_R3();
                skeletonNpc = new SkeletonNpc_1_8_R3();
                slimeNpc = new SlimeNpc_1_8_R3();
                snowmanNpc = new SnowmanNpc_1_8_R3();
                spiderNpc = new SpiderNpc_1_8_R3();
                squidNpc = new SquidNpc_1_8_R3();
                villagerNpc = new VillagerNpc_1_8_R3();
                witchNpc = new WitchNpc_1_8_R3();
                witherNpc = new WitherNpc_1_8_R3();
                wolfNpc = new WolfNpc_1_8_R3();
                zombieNpc = new ZombieNpc_1_8_R3();

                /* Entity */
                entityNms = new EntityNms_1_8_R3();
                /* Titles */
                titleNms = new TitleNms_1_8_R3();
                /* ActionBar */
                actionBarNms = new ActionBarNms_1_8_R3();
                /* CustomItem */
                customItemNms = new CustomItemNms_1_8_R3();
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
                
                /* Pets */
//                batPet = new BatPet_1_8_R3();
//                blazePet = new BlazePet_1_8_R3();
//                caveSpiderPet = new CaveSpiderPet_1_8_R3();
                chickenPet = new ChickenPet_1_8_R3();
                cowPet = new CowPet_1_8_R3();
                creeperPet = new CreeperPet_1_8_R3();
//                endermanPet = new EndermanPet_1_8_R3();
//                endermitePet = new EndermitePet_1_8_R3();
//                ghastPet = new GhastPet_1_8_R3();
//                guardianPet = new GuardianPet_1_8_R3();
//                horsePet = new HorsePet_1_8_R3();
//                ironGolemPet = new IronGolemPet_1_8_R3();
                magmaCubePet = new MagmaCubePet_1_8_R3();
                mushroomCowPet = new MushroomCowPet_1_8_R3();
                ocelotPet = new OcelotPet_1_8_R3();
                pigPet = new PigPet_1_8_R3();
//                pigZombiePet = new PigZombiePet_1_8_R3();
//                rabbitPet = new RabbitPet_1_8_R3();
                sheepPet = new SheepPet_1_8_R3();
                silverfishPet = new SilverfishPet_1_8_R3();
//                skeletonPet = new SkeletonPet_1_8_R3();
                slimePet = new SlimePet_1_8_R3();
//                snowmanPet = new SnowmanPet_1_8_R3();
                spiderPet = new SpiderPet_1_8_R3();
                squidPet = new SquidPet_1_8_R3();
//                villagerPet = new VillagerPet_1_8_R3();
//                witchPet = new WitchPet_1_8_R3();
//                witherPet = new WitherPet_1_8_R3();
                wolfPet = new WolfPet_1_8_R3();
//                zombiePet = new ZombiePet_1_8_R3();

                break;
            case "v1_9_R1": // 1.9 - 1.9.2
                /* NPCs */
                npcNms = new NpcNms_1_9_R1();
                npc().setClassFields();

                batNpc = new BatNpc_1_9_R1();
                blazeNpc = new BlazeNpc_1_9_R1();
                caveSpiderNpc = new CaveSpiderNpc_1_9_R1();
                chickenNpc = new ChickenNpc_1_9_R1();
                cowNpc = new CowNpc_1_9_R1();
                creeperNpc = new CreeperNpc_1_9_R1();
                endermanNpc = new EndermanNpc_1_9_R1();
                endermiteNpc = new EndermiteNpc_1_9_R1();
                ghastNpc = new GhastNpc_1_9_R1();
                guardianNpc = new GuardianNpc_1_9_R1();
                horseNpc = new HorseNpc_1_9_R1();
                ironGolemNpc = new IronGolemNpc_1_9_R1();
                magmaCubeNpc = new MagmaCubeNpc_1_9_R1();
                mushroomCowNpc = new MushroomCowNpc_1_9_R1();
                ocelotNpc = new OcelotNpc_1_9_R1();
                pigNpc = new PigNpc_1_9_R1();
                pigZombieNpc = new PigZombieNpc_1_9_R1();
                rabbitNpc = new RabbitNpc_1_9_R1();
                sheepNpc = new SheepNpc_1_9_R1();
                silverfishNpc = new SilverfishNpc_1_9_R1();
                skeletonNpc = new SkeletonNpc_1_9_R1();
                slimeNpc = new SlimeNpc_1_9_R1();
                snowmanNpc = new SnowmanNpc_1_9_R1();
                spiderNpc = new SpiderNpc_1_9_R1();
                squidNpc = new SquidNpc_1_9_R1();
                villagerNpc = new VillagerNpc_1_9_R1();
                witchNpc = new WitchNpc_1_9_R1();
                witherNpc = new WitherNpc_1_9_R1();
                wolfNpc = new WolfNpc_1_9_R1();
                zombieNpc = new ZombieNpc_1_9_R1();

                /* Entity */
                entityNms = new EntityNms_1_9_R1();
                /* Titles */
                titleNms = new TitleNms_1_9_R1();
                /* ActionBar */
                actionBarNms = new ActionBarNms_1_9_R1();
                /* CustomItem */
                customItemNms = new CustomItemNms_1_9_R1();
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
                
                /* Pets */
//                batPet = new BatPet_1_9_R1();
//                blazePet = new BlazePet_1_9_R1();
//                caveSpiderPet = new CaveSpiderPet_1_9_R1();
                chickenPet = new ChickenPet_1_9_R1();
                cowPet = new CowPet_1_9_R1();
                creeperPet = new CreeperPet_1_9_R1();
//                endermanPet = new EndermanPet_1_9_R1();
//                endermitePet = new EndermitePet_1_9_R1();
//                ghastPet = new GhastPet_1_9_R1();
//                guardianPet = new GuardianPet_1_9_R1();
//                horsePet = new HorsePet_1_9_R1();
//                ironGolemPet = new IronGolemPet_1_9_R1();
                magmaCubePet = new MagmaCubePet_1_9_R1();
                mushroomCowPet = new MushroomCowPet_1_9_R1();
                ocelotPet = new OcelotPet_1_9_R1();
                pigPet = new PigPet_1_9_R1();
//                pigZombiePet = new PigZombiePet_1_9_R1();
//                rabbitPet = new RabbitPet_1_9_R1();
                sheepPet = new SheepPet_1_9_R1();
                silverfishPet = new SilverfishPet_1_9_R1();
//                skeletonPet = new SkeletonPet_1_9_R1();
                slimePet = new SlimePet_1_9_R1();
//                snowmanPet = new SnowmanPet_1_9_R1();
                spiderPet = new SpiderPet_1_9_R1();
                squidPet = new SquidPet_1_9_R1();
//                villagerPet = new VillagerPet_1_9_R1();
//                witchPet = new WitchPet_1_9_R1();
//                witherPet = new WitherPet_1_9_R1();
                wolfPet = new WolfPet_1_9_R1();
//                zombiePet = new ZombiePet_1_9_R1();

                break;
            case "v1_9_R2": // 1.9.4
                /* NPCs */
                npcNms = new NpcNms_1_9_R2();
                npc().setClassFields();

                batNpc = new BatNpc_1_9_R2();
                blazeNpc = new BlazeNpc_1_9_R2();
                caveSpiderNpc = new CaveSpiderNpc_1_9_R2();
                chickenNpc = new ChickenNpc_1_9_R2();
                cowNpc = new CowNpc_1_9_R2();
                creeperNpc = new CreeperNpc_1_9_R2();
                endermanNpc = new EndermanNpc_1_9_R2();
                endermiteNpc = new EndermiteNpc_1_9_R2();
                ghastNpc = new GhastNpc_1_9_R2();
                guardianNpc = new GuardianNpc_1_9_R2();
                horseNpc = new HorseNpc_1_9_R2();
                ironGolemNpc = new IronGolemNpc_1_9_R2();
                magmaCubeNpc = new MagmaCubeNpc_1_9_R2();
                mushroomCowNpc = new MushroomCowNpc_1_9_R2();
                ocelotNpc = new OcelotNpc_1_9_R2();
                pigNpc = new PigNpc_1_9_R2();
                pigZombieNpc = new PigZombieNpc_1_9_R2();
                rabbitNpc = new RabbitNpc_1_9_R2();
                sheepNpc = new SheepNpc_1_9_R2();
                silverfishNpc = new SilverfishNpc_1_9_R2();
                skeletonNpc = new SkeletonNpc_1_9_R2();
                slimeNpc = new SlimeNpc_1_9_R2();
                snowmanNpc = new SnowmanNpc_1_9_R2();
                spiderNpc = new SpiderNpc_1_9_R2();
                squidNpc = new SquidNpc_1_9_R2();
                villagerNpc = new VillagerNpc_1_9_R2();
                witchNpc = new WitchNpc_1_9_R2();
                witherNpc = new WitherNpc_1_9_R2();
                wolfNpc = new WolfNpc_1_9_R2();
                zombieNpc = new ZombieNpc_1_9_R2();

                /* Entity */
                entityNms = new EntityNms_1_9_R2();
                /* Titles */
                titleNms = new TitleNms_1_9_R2();
                /* ActionBar */
                actionBarNms = new ActionBarNms_1_9_R2();
                /* CustomItem */
                customItemNms = new CustomItemNms_1_9_R2();
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
                
                /* Pets */
//                batPet = new BatPet_1_9_R2();
//                blazePet = new BlazePet_1_9_R2();
//                caveSpiderPet = new CaveSpiderPet_1_9_R2();
                chickenPet = new ChickenPet_1_9_R2();
                cowPet = new CowPet_1_9_R2();
                creeperPet = new CreeperPet_1_9_R2();
//                endermanPet = new EndermanPet_1_9_R2();
//                endermitePet = new EndermitePet_1_9_R2();
//                ghastPet = new GhastPet_1_9_R2();
//                guardianPet = new GuardianPet_1_9_R2();
//                horsePet = new HorsePet_1_9_R2();
//                ironGolemPet = new IronGolemPet_1_9_R2();
                magmaCubePet = new MagmaCubePet_1_9_R2();
                mushroomCowPet = new MushroomCowPet_1_9_R2();
                ocelotPet = new OcelotPet_1_9_R2();
                pigPet = new PigPet_1_9_R2();
//                pigZombiePet = new PigZombiePet_1_9_R2();
//                rabbitPet = new RabbitPet_1_9_R2();
                sheepPet = new SheepPet_1_9_R2();
                silverfishPet = new SilverfishPet_1_9_R2();
//                skeletonPet = new SkeletonPet_1_9_R2();
                slimePet = new SlimePet_1_9_R2();
//                snowmanPet = new SnowmanPet_1_9_R2();
                spiderPet = new SpiderPet_1_9_R2();
                squidPet = new SquidPet_1_9_R2();
//                villagerPet = new VillagerPet_1_9_R2();
//                witchPet = new WitchPet_1_9_R2();
//                witherPet = new WitherPet_1_9_R2();
                wolfPet = new WolfPet_1_9_R2();
//                zombiePet = new ZombiePet_1_9_R2();

                break;
            case "v1_10_R1": // 1.10 -  1.10.2
                /* NPCs */
                npcNms = new NpcNms_1_10_R1();
                npc().setClassFields();

                batNpc = new BatNpc_1_10_R1();
                blazeNpc = new BlazeNpc_1_10_R1();
                caveSpiderNpc = new CaveSpiderNpc_1_10_R1();
                chickenNpc = new ChickenNpc_1_10_R1();
                cowNpc = new CowNpc_1_10_R1();
                creeperNpc = new CreeperNpc_1_10_R1();
                endermanNpc = new EndermanNpc_1_10_R1();
                endermiteNpc = new EndermiteNpc_1_10_R1();
                ghastNpc = new GhastNpc_1_10_R1();
                guardianNpc = new GuardianNpc_1_10_R1();
                horseNpc = new HorseNpc_1_10_R1();
                ironGolemNpc = new IronGolemNpc_1_10_R1();
                magmaCubeNpc = new MagmaCubeNpc_1_10_R1();
                mushroomCowNpc = new MushroomCowNpc_1_10_R1();
                ocelotNpc = new OcelotNpc_1_10_R1();
                pigNpc = new PigNpc_1_10_R1();
                pigZombieNpc = new PigZombieNpc_1_10_R1();
                polarBearNpc = new PolarBearNpc_1_10_R1();
                rabbitNpc = new RabbitNpc_1_10_R1();
                sheepNpc = new SheepNpc_1_10_R1();
                silverfishNpc = new SilverfishNpc_1_10_R1();
                skeletonNpc = new SkeletonNpc_1_10_R1();
                slimeNpc = new SlimeNpc_1_10_R1();
                snowmanNpc = new SnowmanNpc_1_10_R1();
                spiderNpc = new SpiderNpc_1_10_R1();
                squidNpc = new SquidNpc_1_10_R1();
                villagerNpc = new VillagerNpc_1_10_R1();
                witchNpc = new WitchNpc_1_10_R1();
                witherNpc = new WitherNpc_1_10_R1();
                wolfNpc = new WolfNpc_1_10_R1();
                zombieNpc = new ZombieNpc_1_10_R1();

                /* Entity */
                entityNms = new EntityNms_1_10_R1();
                /* Titles */
                titleNms = new TitleNms_1_10_R1();
                /* ActionBar */
                actionBarNms = new ActionBarNms_1_10_R1();
                /* CustomItem */
                customItemNms = new CustomItemNms_1_10_R1();
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
                
                /* Pets */
//                batPet = new BatPet_1_10_R1();
//                blazePet = new BlazePet_1_10_R1();
//                caveSpiderPet = new CaveSpiderPet_1_10_R1();
                chickenPet = new ChickenPet_1_10_R1();
                cowPet = new CowPet_1_10_R1();
                creeperPet = new CreeperPet_1_10_R1();
//                endermanPet = new EndermanPet_1_10_R1();
//                endermitePet = new EndermitePet_1_10_R1();
//                ghastPet = new GhastPet_1_10_R1();
//                guardianPet = new GuardianPet_1_10_R1();
//                horsePet = new HorsePet_1_10_R1();
//                ironGolemPet = new IronGolemPet_1_10_R1();
                magmaCubePet = new MagmaCubePet_1_10_R1();
                mushroomCowPet = new MushroomCowPet_1_10_R1();
                ocelotPet = new OcelotPet_1_10_R1();
                pigPet = new PigPet_1_10_R1();
//                pigZombiePet = new PigZombiePet_1_10_R1();
//                rabbitPet = new RabbitPet_1_10_R1();
                sheepPet = new SheepPet_1_10_R1();
                silverfishPet = new SilverfishPet_1_10_R1();
//                skeletonPet = new SkeletonPet_1_10_R1();
                slimePet = new SlimePet_1_10_R1();
//                snowmanPet = new SnowmanPet_1_10_R1();
                spiderPet = new SpiderPet_1_10_R1();
                squidPet = new SquidPet_1_10_R1();
//                villagerPet = new VillagerPet_1_10_R1();
//                witchPet = new WitchPet_1_10_R1();
//                witherPet = new WitherPet_1_10_R1();
                wolfPet = new WolfPet_1_10_R1();
//                zombiePet = new ZombiePet_1_10_R1();

                break;
            case "v1_11_R1": // 1.11 - 1.11.2
                /* NPCs */
                npcNms = new NpcNms_1_11_R1();
                npc().setClassFields();

                batNpc = new BatNpc_1_11_R1();
                blazeNpc = new BlazeNpc_1_11_R1();
                caveSpiderNpc = new CaveSpiderNpc_1_11_R1();
                chickenNpc = new ChickenNpc_1_11_R1();
                cowNpc = new CowNpc_1_11_R1();
                creeperNpc = new CreeperNpc_1_11_R1();
                endermanNpc = new EndermanNpc_1_11_R1();
                endermiteNpc = new EndermiteNpc_1_11_R1();
                evokerNpc = new EvokerNpc_1_11_R1();
                ghastNpc = new GhastNpc_1_11_R1();
                guardianNpc = new GuardianNpc_1_11_R1();
                horseNpc = new HorseNpc_1_11_R1();
                ironGolemNpc = new IronGolemNpc_1_11_R1();
                llamaNpc = new LlamaNpc_1_11_R1();
                magmaCubeNpc = new MagmaCubeNpc_1_11_R1();
                muleNpc = new MuleNpc_1_11_R1();
                mushroomCowNpc = new MushroomCowNpc_1_11_R1();
                ocelotNpc = new OcelotNpc_1_11_R1();
                pigNpc = new PigNpc_1_11_R1();
                pigZombieNpc = new PigZombieNpc_1_11_R1();
                polarBearNpc = new PolarBearNpc_1_11_R1();
                rabbitNpc = new RabbitNpc_1_11_R1();
                sheepNpc = new SheepNpc_1_11_R1();
                silverfishNpc = new SilverfishNpc_1_11_R1();
                skeletonNpc = new SkeletonNpc_1_11_R1();
                skeletonHorseNpc = new SkeletonHorseNpc_1_11_R1();
                slimeNpc = new SlimeNpc_1_11_R1();
                snowmanNpc = new SnowmanNpc_1_11_R1();
                spiderNpc = new SpiderNpc_1_11_R1();
                squidNpc = new SquidNpc_1_11_R1();
                strayNpc = new StrayNpc_1_11_R1();
                vexNpc = new VexNpc_1_11_R1();
                villagerNpc = new VillagerNpc_1_11_R1();
                vindicatorNpc = new VindicatorNpc_1_11_R1();
                witchNpc = new WitchNpc_1_11_R1();
                witherNpc = new WitherNpc_1_11_R1();
                witherSkeletonNpc = new WitherSkeletonNpc_1_11_R1();
                wolfNpc = new WolfNpc_1_11_R1();
                zombieNpc = new ZombieNpc_1_11_R1();
                zombieHorseNpc = new ZombieHorseNpc_1_11_R1();
                zombieHuskNpc = new ZombieHuskNpc_1_11_R1();

                /* Entity */
                entityNms = new EntityNms_1_11_R1();
                /* Titles */
                titleNms = new TitleNms_1_11_R1();
                /* ActionBar */
                actionBarNms = new ActionBarNms_1_11_R1();
                /* CustomItem */
                customItemNms = new CustomItemNms_1_11_R1();
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
                
                /* Pets */
//                batPet = new BatPet_1_11_R1();
//                blazePet = new BlazePet_1_11_R1();
//                caveSpiderPet = new CaveSpiderPet_1_11_R1();
                chickenPet = new ChickenPet_1_11_R1();
                cowPet = new CowPet_1_11_R1();
                creeperPet = new CreeperPet_1_11_R1();
//                endermanPet = new EndermanPet_1_11_R1();
//                endermitePet = new EndermitePet_1_11_R1();
//                ghastPet = new GhastPet_1_11_R1();
//                guardianPet = new GuardianPet_1_11_R1();
//                horsePet = new HorsePet_1_11_R1();
//                ironGolemPet = new IronGolemPet_1_11_R1();
                magmaCubePet = new MagmaCubePet_1_11_R1();
                mushroomCowPet = new MushroomCowPet_1_11_R1();
                ocelotPet = new OcelotPet_1_11_R1();
                pigPet = new PigPet_1_11_R1();
//                pigZombiePet = new PigZombiePet_1_11_R1();
//                rabbitPet = new RabbitPet_1_11_R1();
                sheepPet = new SheepPet_1_11_R1();
                silverfishPet = new SilverfishPet_1_11_R1();
//                skeletonPet = new SkeletonPet_1_11_R1();
                slimePet = new SlimePet_1_11_R1();
//                snowmanPet = new SnowmanPet_1_11_R1();
                spiderPet = new SpiderPet_1_11_R1();
                squidPet = new SquidPet_1_11_R1();
//                villagerPet = new VillagerPet_1_11_R1();
//                witchPet = new WitchPet_1_11_R1();
//                witherPet = new WitherPet_1_11_R1();
                wolfPet = new WolfPet_1_11_R1();
//                zombiePet = new ZombiePet_1_11_R1();

                break;
            case "v1_12_R1": // 1.12 - ?
                /* NPCs */
                npcNms = new NpcNms_1_12_R1();
                npc().setClassFields();

                batNpc = new BatNpc_1_12_R1();
                blazeNpc = new BlazeNpc_1_12_R1();
                caveSpiderNpc = new CaveSpiderNpc_1_12_R1();
                chickenNpc = new ChickenNpc_1_12_R1();
                cowNpc = new CowNpc_1_12_R1();
                creeperNpc = new CreeperNpc_1_12_R1();
                endermanNpc = new EndermanNpc_1_12_R1();
                endermiteNpc = new EndermiteNpc_1_12_R1();
                evokerNpc = new EvokerNpc_1_12_R1();
                ghastNpc = new GhastNpc_1_12_R1();
                guardianNpc = new GuardianNpc_1_12_R1();
                horseNpc = new HorseNpc_1_12_R1();
                ironGolemNpc = new IronGolemNpc_1_12_R1();
                llamaNpc = new LlamaNpc_1_12_R1();
                magmaCubeNpc = new MagmaCubeNpc_1_12_R1();
                muleNpc = new MuleNpc_1_12_R1();
                mushroomCowNpc = new MushroomCowNpc_1_12_R1();
                ocelotNpc = new OcelotNpc_1_12_R1();
                pigNpc = new PigNpc_1_12_R1();
                pigZombieNpc = new PigZombieNpc_1_12_R1();
                polarBearNpc = new PolarBearNpc_1_12_R1();
                rabbitNpc = new RabbitNpc_1_12_R1();
                sheepNpc = new SheepNpc_1_12_R1();
                silverfishNpc = new SilverfishNpc_1_12_R1();
                skeletonNpc = new SkeletonNpc_1_12_R1();
                skeletonHorseNpc = new SkeletonHorseNpc_1_12_R1();
                slimeNpc = new SlimeNpc_1_12_R1();
                snowmanNpc = new SnowmanNpc_1_12_R1();
                spiderNpc = new SpiderNpc_1_12_R1();
                squidNpc = new SquidNpc_1_12_R1();
                strayNpc = new StrayNpc_1_12_R1();
                vexNpc = new VexNpc_1_12_R1();
                villagerNpc = new VillagerNpc_1_12_R1();
                vindicatorNpc = new VindicatorNpc_1_12_R1();
                witchNpc = new WitchNpc_1_12_R1();
                witherNpc = new WitherNpc_1_12_R1();
                witherSkeletonNpc = new WitherSkeletonNpc_1_12_R1();
                wolfNpc = new WolfNpc_1_12_R1();
                zombieNpc = new ZombieNpc_1_12_R1();
                zombieHorseNpc = new ZombieHorseNpc_1_12_R1();
                zombieHuskNpc = new ZombieHuskNpc_1_12_R1();

                /* Entity */
                entityNms = new EntityNms_1_12_R1();
                /* Titles */
                titleNms = new TitleNms_1_12_R1();
                /* ActionBar */
                actionBarNms = new ActionBarNms_1_12_R1();
                /* CustomItem */
                customItemNms = new CustomItemNms_1_12_R1();
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
                
                /* Pets */
//                batPet = new BatPet_1_12_R1();
//                blazePet = new BlazePet_1_12_R1();
//                caveSpiderPet = new CaveSpiderPet_1_12_R1();
                chickenPet = new ChickenPet_1_12_R1();
                cowPet = new CowPet_1_12_R1();
                creeperPet = new CreeperPet_1_12_R1();
//                endermanPet = new EndermanPet_1_12_R1();
//                endermitePet = new EndermitePet_1_12_R1();
//                ghastPet = new GhastPet_1_12_R1();
//                guardianPet = new GuardianPet_1_12_R1();
//                horsePet = new HorsePet_1_12_R1();
//                ironGolemPet = new IronGolemPet_1_12_R1();
                magmaCubePet = new MagmaCubePet_1_12_R1();
                mushroomCowPet = new MushroomCowPet_1_12_R1();
                ocelotPet = new OcelotPet_1_12_R1();
                pigPet = new PigPet_1_12_R1();
//                pigZombiePet = new PigZombiePet_1_12_R1();
//                rabbitPet = new RabbitPet_1_12_R1();
                sheepPet = new SheepPet_1_12_R1();
                silverfishPet = new SilverfishPet_1_12_R1();
//                skeletonPet = new SkeletonPet_1_12_R1();
                slimePet = new SlimePet_1_12_R1();
//                snowmanPet = new SnowmanPet_1_12_R1();
                spiderPet = new SpiderPet_1_12_R1();
                squidPet = new SquidPet_1_12_R1();
//                villagerPet = new VillagerPet_1_12_R1();
//                witchPet = new WitchPet_1_12_R1();
//                witherPet = new WitherPet_1_12_R1();
                wolfPet = new WolfPet_1_12_R1();
//                zombiePet = new ZombiePet_1_12_R1();

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

    public CustomItemNms customItem() {
        return customItemNms;
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

    public CowNpc getCowNpc() {
        return cowNpc;
    }

    public CreeperNpc getCreeperNpc() {
        return creeperNpc;
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

    public GuardianNpc getGuardianNpc() {
        return guardianNpc;
    }

    public HorseNpc getHorseNpc() {
        return horseNpc;
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

    public PigNpc getPigNpc() {
        return pigNpc;
    }

    public PigZombieNpc getPigZombieNpc() {
        return pigZombieNpc;
    }

    public PolarBearNpc getPolarBearNpc() {
        return polarBearNpc;
    }

    public RabbitNpc getRabbitNpc() {
        return rabbitNpc;
    }

    public SheepNpc getSheepNpc() {
        return sheepNpc;
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

    public ZombieHuskNpc getZombieHuskNpc() {
        return zombieHuskNpc;
    }

    /*
        Pets
     */

//    public BatPet getBatPet() {
//        return batPet;
//    }

//    public BlazePet getBlazePet() {
//        return blazePet;
//    }

//    public CaveSpiderPet getCaveSpiderPet() {
//        return caveSpiderPet;
//    }

    public ChickenPet getChickenPet() {
        return chickenPet;
    }

    public CowPet getCowPet() {
        return cowPet;
    }

    public CreeperPet getCreeperPet() {
        return creeperPet;
    }

//    public EndermanPet getEndermanPet() {
//        return endermanPet;
//    }

//    public EndermitePet getEndermitePet() {
//        return endermitePet;
//    }

//    public EvokerPet getEvokerPet() {
//        return evokerPet;
//    }

//    public GhastPet getGhastPet() {
//        return ghastPet;
//    }

//    public GuardianPet getGuardianPet() {
//        return guardianPet;
//    }

//    public HorsePet getHorsePet() {
//        return horsePet;
//    }

//    public IronGolemPet getIronGolemPet() {
//        return ironGolemPet;
//    }

//    public LlamaPet getLlamaPet() {
//        return llamaPet;
//    }

    public MagmaCubePet getMagmaCubePet() {
        return magmaCubePet;
    }

//    public MulePet getMulePet() {
//        return mulePet;
//    }

    public MushroomCowPet getMushroomCowPet() {
        return mushroomCowPet;
    }

    public OcelotPet getOcelotPet() {
        return ocelotPet;
    }

    public PigPet getPigPet() {
        return pigPet;
    }

//    public PigZombiePet getPigZombiePet() {
//        return pigZombiePet;
//    }

//    public PolarBearPet getPolarBearPet() {
//        return polarBearPet;
//    }

//    public RabbitPet getRabbitPet() {
//        return rabbitPet;
//    }

    public SheepPet getSheepPet() {
        return sheepPet;
    }

    public SilverfishPet getSilverfishPet() {
        return silverfishPet;
    }

//    public SkeletonPet getSkeletonPet() {
//        return skeletonPet;
//    }

//    public SkeletonHorsePet getSkeletonHorsePet() {
//        return skeletonHorsePet;
//    }

    public SlimePet getSlimePet() {
        return slimePet;
    }

//    public SnowmanPet getSnowmanPet() {
//        return snowmanPet;
//    }

    public SpiderPet getSpiderPet() {
        return spiderPet;
    }

    public SquidPet getSquidPet() {
        return squidPet;
    }

//    public StrayPet getStrayPet() {
//        return strayPet;
//    }

//    public VexPet getVexPet() {
//        return vexPet;
//    }

//    public VillagerPet getVillagerPet() {
//        return villagerPet;
//    }

//    public VindicatorPet getVindicatorPet() {
//        return vindicatorPet;
//    }

//    public WitchPet getWitchPet() {
//        return witchPet;
//    }

//    public WitherPet getWitherPet() {
//        return witherPet;
//    }

//    public WitherSkeletonPet getWitherSkeletonPet() {
//        return witherSkeletonPet;
//    }

    public WolfPet getWolfPet() {
        return wolfPet;
    }

//    public ZombiePet getZombiePet() {
//        return zombiePet;
//    }

//    public ZombieHorsePet getZombieHorsePet() {
//        return zombieHorsePet;
//    }

//    public ZombieHuskPet getZombieHuskPet() {
//        return zombieHuskPet;
//    }
}
