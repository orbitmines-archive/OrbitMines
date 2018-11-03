package com.orbitmines.spigot.api.nms.bednpc;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.mojang.authlib.GameProfile;
import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.api.Freezer;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.npc.BedNpc;
import net.minecraft.server.v1_13_R2.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.type.Bed;
import org.bukkit.craftbukkit.v1_13_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Field;
import java.util.UUID;

public class BedNpcNms_1_13_R2 implements BedNpcNms {

    private final OrbitMines orbitMines;

    public BedNpcNms_1_13_R2(OrbitMines orbitMines) {
        this.orbitMines = orbitMines;
    }

    @Override
    public int spawn(BedNpc bedNpc, boolean withArmor, boolean withItemsInHand) {
        Location location = getFixedLocation(bedNpc);

        /* Player Profile */
        EntityPlayer craftPlayer = ((CraftPlayer) bedNpc.getPlayer()).getHandle();
        GameProfile profile = getProfileFrom(craftPlayer.getProfile(), "");

        /* Next Entity ID */
        int entityId = bedNpc.getEntityId() != -1 ? bedNpc.getEntityId() : OrbitMines.getInstance().getNms().entity().nextEntityId();

        DataWatcher dataWatcher = clonedEntityHumanClass(bedNpc.getPlayer(), entityId);
//        dataWatcher.watch(10, craftPlayer.getDataWatcher().getByte(10));
        Location locUnder = getSolidBlockUnder(location);
        Location used = locUnder != null ? locUnder : location;
        used.setYaw(location.getYaw());
        used.setPitch(location.getPitch());

        PacketPlayOutNamedEntitySpawn packetPlayOutNamedEntitySpawn = new PacketPlayOutNamedEntitySpawn();

        try {
            Field a = packetPlayOutNamedEntitySpawn.getClass().getDeclaredField("a");
            a.setAccessible(true);
            a.set(packetPlayOutNamedEntitySpawn, entityId);
            Field b = packetPlayOutNamedEntitySpawn.getClass().getDeclaredField("b");
            b.setAccessible(true);
            b.set(packetPlayOutNamedEntitySpawn, profile.getId());
            Field c = packetPlayOutNamedEntitySpawn.getClass().getDeclaredField("c");
            c.setAccessible(true);
            c.setDouble(packetPlayOutNamedEntitySpawn, location.getX());
            Field d = packetPlayOutNamedEntitySpawn.getClass().getDeclaredField("d");
            d.setAccessible(true);
            d.setDouble(packetPlayOutNamedEntitySpawn, location.getY() + 2D);
            Field e = packetPlayOutNamedEntitySpawn.getClass().getDeclaredField("e");
            e.setAccessible(true);
            e.setDouble(packetPlayOutNamedEntitySpawn, location.getZ());
            Field f = packetPlayOutNamedEntitySpawn.getClass().getDeclaredField("f");
            f.setAccessible(true);
            f.setByte(packetPlayOutNamedEntitySpawn, (byte) (int) (location.getYaw() * 256.0F / 360.0F));
            Field g = packetPlayOutNamedEntitySpawn.getClass().getDeclaredField("g");
            g.setAccessible(true);
            g.setByte(packetPlayOutNamedEntitySpawn, (byte) (int) (location.getPitch() * 256.0F / 360.0F));

            Field h = packetPlayOutNamedEntitySpawn.getClass().getDeclaredField("h");
            h.setAccessible(true);
            h.set(packetPlayOutNamedEntitySpawn, dataWatcher);
        } catch (Exception e) {
            e.printStackTrace();
        }

        PacketPlayOutBed packetPlayOutBed = new PacketPlayOutBed();
        try {
            Field a = packetPlayOutBed.getClass().getDeclaredField("a");
            a.setAccessible(true);
            a.setInt(packetPlayOutBed, entityId);
            Field b = packetPlayOutBed.getClass().getDeclaredField("b");
            b.setAccessible(true);
            b.set(packetPlayOutBed, new BlockPosition(location.getBlockX(), getBedLocation(bedNpc).getBlockY(), location.getBlockZ()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        PacketPlayOutEntity.PacketPlayOutRelEntityMove packetPlayOutRelEntityMove = new PacketPlayOutEntity.PacketPlayOutRelEntityMove(entityId, (byte) 0, (byte) (-60.8), (byte) 0, false);

        PacketPlayOutPlayerInfo packetPlayOutPlayerInfoAdd = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER);

        try {
            Field b = packetPlayOutPlayerInfoAdd.getClass().getDeclaredField("b");
            b.setAccessible(true);
//TODO
//            List<PacketPlayOutPlayerInfo.PlayerInfoData> data = (List<PacketPlayOutPlayerInfo.PlayerInfoData>) b.get(packetPlayOutPlayerInfoAdd);
//            data.add(packetPlayOutPlayerInfoAdd.new PlayerInfoData(profile, 0, EnumGamemode.SURVIVAL, new ChatMessage("")));
        } catch (Exception e) {
            e.printStackTrace();
        }

        PacketPlayOutPlayerInfo packetPlayOutPlayerInfoRemove = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER);
        try {
            Field b = packetPlayOutPlayerInfoRemove.getClass().getDeclaredField("b");
            b.setAccessible(true);

//            List<PacketPlayOutPlayerInfo.PlayerInfoData> data = (List<PacketPlayOutPlayerInfo.PlayerInfoData>) b.get(packetPlayOutPlayerInfoRemove);
//            data.add(packetPlayOutPlayerInfoRemove.new PlayerInfoData(profile, 0, EnumGamemode.SURVIVAL, new ChatMessage("")));
        } catch (Exception e) {
            e.printStackTrace();
        }

        PlayerInventory inventory = bedNpc.getPlayer().getInventory();
        PacketPlayOutEntityEquipment helmetPacket = null;
        PacketPlayOutEntityEquipment chestplatePacket = null;
        PacketPlayOutEntityEquipment leggingsPacket = null;
        PacketPlayOutEntityEquipment bootsPacket = null;

        if (withArmor) {
            helmetPacket = getPacketPlayOutEntityEquipment(entityId, EnumItemSlot.HEAD, getNmsItemStack(inventory.getHelmet()));
            chestplatePacket = getPacketPlayOutEntityEquipment(entityId, EnumItemSlot.CHEST, getNmsItemStack(inventory.getChestplate()));
            leggingsPacket = getPacketPlayOutEntityEquipment(entityId, EnumItemSlot.LEGS, getNmsItemStack(inventory.getLeggings()));
            bootsPacket = getPacketPlayOutEntityEquipment(entityId, EnumItemSlot.FEET, getNmsItemStack(inventory.getBoots()));
        }

        PacketPlayOutEntityEquipment mainHandPacket = null;
        PacketPlayOutEntityEquipment offHandPacket = null;

        if (withItemsInHand) {
            mainHandPacket = getPacketPlayOutEntityEquipment(entityId, EnumItemSlot.MAINHAND, getNmsItemStack(inventory.getItemInMainHand()));
            offHandPacket = getPacketPlayOutEntityEquipment(entityId, EnumItemSlot.OFFHAND, getNmsItemStack(inventory.getItemInOffHand()));
        }

        Bed bedHead = (Bed) Material.RED_BED.createBlockData();
        bedHead.setPart(Bed.Part.HEAD);
        bedHead.setFacing(bedNpc.getDirection().getBlockFace());

        Bed bedFoot = (Bed) Material.RED_BED.createBlockData();
        bedFoot.setPart(Bed.Part.FOOT);
        bedFoot.setFacing(bedNpc.getDirection().getBlockFace());

        Location bedHeadLoc = getBedLocation(bedNpc);
        Location bedFootLoc = bedNpc.getDirection().getAsNewLocation(bedHeadLoc);

        for (Player watcher : bedNpc.getWatchers()) {
            watcher.sendBlockChange(bedHeadLoc, bedHead);
            watcher.sendBlockChange(bedFootLoc, bedFoot);

            PlayerConnection playerConnection = ((CraftPlayer) watcher).getHandle().playerConnection;

            playerConnection.sendPacket(packetPlayOutPlayerInfoAdd);
            playerConnection.sendPacket(packetPlayOutNamedEntitySpawn);
            playerConnection.sendPacket(packetPlayOutBed);
            playerConnection.sendPacket(packetPlayOutRelEntityMove);

            if (withArmor) {
                playerConnection.sendPacket(helmetPacket);
                playerConnection.sendPacket(chestplatePacket);
                playerConnection.sendPacket(leggingsPacket);
                playerConnection.sendPacket(bootsPacket);
            }

            if (withItemsInHand) {
                playerConnection.sendPacket(mainHandPacket);
                playerConnection.sendPacket(offHandPacket);
            }

//            if (watcher != bedNpc.getPlayer()) {
            watcher.hidePlayer(orbitMines, bedNpc.getPlayer());
//            }

            if (bedNpc.isFirstPerson() && watcher == bedNpc.getPlayer()) {
                OMPlayer.getPlayer(bedNpc.getPlayer()).freeze(Freezer.ARMORSTAND_RIDE, bedNpc.getDirection().getAsNewLocation(location.clone().subtract(0, 2, 0), 0.4));
            }
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player watcher : bedNpc.getWatchers()) {
                    ((CraftPlayer) watcher).getHandle().playerConnection.sendPacket(packetPlayOutPlayerInfoRemove);
                }
            }
        }.runTaskLater(OrbitMines.getInstance(), 20);

        return entityId;
    }

    @Override
    public void destroy(BedNpc bedNpc) {
        PacketPlayOutEntityDestroy packetPlayOutEntityDestroy = new PacketPlayOutEntityDestroy(bedNpc.getEntityId());

        for (Player watcher : bedNpc.getWatchers()) {
            ((CraftPlayer) watcher).getHandle().playerConnection.sendPacket(packetPlayOutEntityDestroy);

//            if (watcher != bedNpc.getPlayer())
            watcher.showPlayer(orbitMines, bedNpc.getPlayer());
        }
    }

    @Override
    public Location getFixedLocation(BedNpc bedNpc) {
        Location location = getSolidBlockUnder(bedNpc.getLocation());
        if (location == null)
            return bedNpc.getLocation();

        location.setYaw(location.getYaw());
        location.setPitch(location.getPitch());

        return location;
    }

    private PacketPlayOutEntityEquipment getPacketPlayOutEntityEquipment(int entityId, EnumItemSlot slot, ItemStack stack) {
        return new PacketPlayOutEntityEquipment(entityId, slot, stack);
    }

    private ItemStack getNmsItemStack(org.bukkit.inventory.ItemStack stack) {
        if (stack == null)
            return new ItemStack(Item.getById(0));

        ItemStack temp = new ItemStack(Item.getById(stack.getType().getId()), stack.getAmount());

        if (stack.getEnchantments().size() >= 1)
            temp.addEnchantment(IRegistry.ENCHANTMENT.get(MinecraftKey.a("protection")), 1);

        return temp;
    }

    private DataWatcher clonedEntityHumanClass(Player player, int entityId) {
        EntityHuman h = new EntityHuman(((CraftWorld) player.getWorld()).getHandle(), ((CraftPlayer) player).getProfile()) {
            @Override
            public void sendMessage(IChatBaseComponent arg0) {
                return;
            }

            @Override
            public boolean u() {
                return false;
            }

            @Override
            public BlockPosition getChunkCoordinates() {
                return null;
            }

            @Override
            public boolean isSpectator() {
                return false;
            }
        };
        h.f(entityId);
        return h.getDataWatcher();
    }

    private GameProfile getProfileFrom(GameProfile oldProf, String name) {
        GameProfile newProf = new GameProfile(UUID.randomUUID(), name);
        newProf.getProperties().putAll(oldProf.getProperties());
        return newProf;
    }

    private Location getBedLocation(BedNpc bedNpc) {
        Location location = bedNpc.getLocation().clone();
        location.setY(1);

        return location;
    }

    private Location getSolidBlockUnder(Location location) {
        if (location.getBlockY() < 0)
            return null;

        for (int y = location.getBlockY(); y >= 0; y--) {
            Material m = location.getWorld().getBlockAt(location.getBlockX(), y, location.getBlockZ()).getType();

            if (m.isSolid())
                return new Location(location.getWorld(), location.getX(), y + 1, location.getZ());
        }
        return null;
    }
}
