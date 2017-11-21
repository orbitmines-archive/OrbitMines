package com.orbitmines.spigot.api.nms.bednpc;

import com.mojang.authlib.GameProfile;
import com.orbitmines.spigot.api.Freezer;
import com.orbitmines.spigot.api.utils.LocationUtils;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Field;
import java.util.List;
import java.util.UUID;

/*
* OrbitMines - @author Fadi Shawki - 26-6-2017
*/
public class BedNpcNms_1_8_R3 implements BedNpcNms {

    @Override
    public int spawn(BedNpc bedNpc, boolean withArmor, boolean withItemsInHand) {
        Location location = getFixedLocation(bedNpc);

        /* Player Profile */
        EntityPlayer craftPlayer = ((CraftPlayer) bedNpc.getPlayer()).getHandle();
        GameProfile profile = getProfileFrom(craftPlayer.getProfile(), "");

        /* Next Entity ID */
        int entityId = bedNpc.getEntityId() != -1 ? bedNpc.getEntityId() : MadBlock.getInstance().getNms().entity().nextEntityId();

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
            c.setInt(packetPlayOutNamedEntitySpawn, MathHelper.floor(location.getX() * 32.0D));
            Field d = packetPlayOutNamedEntitySpawn.getClass().getDeclaredField("d");
            d.setAccessible(true);
            d.setInt(packetPlayOutNamedEntitySpawn, MathHelper.floor((location.getY() + 2) * 32.0D));
            Field e = packetPlayOutNamedEntitySpawn.getClass().getDeclaredField("e");
            e.setAccessible(true);
            e.setInt(packetPlayOutNamedEntitySpawn, MathHelper.floor(location.getZ() * 32.0D));
            Field f = packetPlayOutNamedEntitySpawn.getClass().getDeclaredField("f");
            f.setAccessible(true);
            f.setByte(packetPlayOutNamedEntitySpawn, (byte) (int) (location.getYaw() * 256.0F / 360.0F));
            Field g = packetPlayOutNamedEntitySpawn.getClass().getDeclaredField("g");
            g.setAccessible(true);
            g.setByte(packetPlayOutNamedEntitySpawn, (byte) (int) (location.getPitch() * 256.0F / 360.0F));
            Field i = packetPlayOutNamedEntitySpawn.getClass().getDeclaredField("i");
            i.setAccessible(true);
            i.set(packetPlayOutNamedEntitySpawn, dataWatcher);
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
            @SuppressWarnings("unchecked")
            List<PacketPlayOutPlayerInfo.PlayerInfoData> data = (List<PacketPlayOutPlayerInfo.PlayerInfoData>) b.get(packetPlayOutPlayerInfoAdd);
            data.add(packetPlayOutPlayerInfoAdd.new PlayerInfoData(profile, 0, WorldSettings.EnumGamemode.SURVIVAL, new ChatMessage("")));
        } catch (Exception e) {
            e.printStackTrace();
        }

        PacketPlayOutPlayerInfo packetPlayOutPlayerInfoRemove = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER);
        try {
            Field b = packetPlayOutPlayerInfoRemove.getClass().getDeclaredField("b");
            b.setAccessible(true);
            @SuppressWarnings("unchecked")
            List<PacketPlayOutPlayerInfo.PlayerInfoData> data = (List<PacketPlayOutPlayerInfo.PlayerInfoData>) b.get(packetPlayOutPlayerInfoRemove);
            data.add(packetPlayOutPlayerInfoRemove.new PlayerInfoData(profile, 0, WorldSettings.EnumGamemode.SURVIVAL, new ChatMessage("")));
        } catch (Exception e) {
            e.printStackTrace();
        }

        PlayerInventory inventory = bedNpc.getPlayer().getInventory();
        PacketPlayOutEntityEquipment helmetPacket = null;
        PacketPlayOutEntityEquipment chestplatePacket = null;
        PacketPlayOutEntityEquipment leggingsPacket = null;
        PacketPlayOutEntityEquipment bootsPacket = null;

        if (withArmor) {
            helmetPacket = getPacketPlayOutEntityEquipment(entityId, 4, getNmsItemStack(inventory.getHelmet()));
            chestplatePacket = getPacketPlayOutEntityEquipment(entityId, 3, getNmsItemStack(inventory.getChestplate()));
            leggingsPacket = getPacketPlayOutEntityEquipment(entityId, 2, getNmsItemStack(inventory.getLeggings()));
            bootsPacket = getPacketPlayOutEntityEquipment(entityId, 1, getNmsItemStack(inventory.getBoots()));
        }

        PacketPlayOutEntityEquipment mainHandPacket = null;

        if (withItemsInHand) {
            mainHandPacket = getPacketPlayOutEntityEquipment(entityId, 0, getNmsItemStack(inventory.getItemInHand()));
        }

        for (Player watcher : bedNpc.getWatchers()) {
            watcher.sendBlockChange(getBedLocation(bedNpc), Material.BED_BLOCK, (byte) bedNpc.getDirection().ordinal());

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
            }

//            if (watcher != bedNpc.getPlayer()) {
            watcher.hidePlayer(bedNpc.getPlayer());
//            }

            if (bedNpc.isFirstPerson() && watcher == bedNpc.getPlayer()) {
                OMPlayer.getPlayer(bedNpc.getPlayer()).freeze(Freezer.ARMORSTAND_RIDE, bedNpc.getDirection().getAsNewLocation(LocationUtils.copy(location).subtract(0, 2, 0), 0.4));
            }
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player watcher : bedNpc.getWatchers()) {
                    ((CraftPlayer) watcher).getHandle().playerConnection.sendPacket(packetPlayOutPlayerInfoRemove);
                }
            }
        }.runTaskLater(MadBlock.getInstance(), 20);

        return entityId;
    }

    @Override
    public void destroy(BedNpc bedNpc) {
        PacketPlayOutEntityDestroy packetPlayOutEntityDestroy = new PacketPlayOutEntityDestroy(bedNpc.getEntityId());

        for (Player watcher : bedNpc.getWatchers()) {
            ((CraftPlayer) watcher).getHandle().playerConnection.sendPacket(packetPlayOutEntityDestroy);

//            if (watcher != bedNpc.getPlayer())
                watcher.showPlayer(bedNpc.getPlayer());
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

    private PacketPlayOutEntityEquipment getPacketPlayOutEntityEquipment(int entityId, int slot, ItemStack stack) {
        return new PacketPlayOutEntityEquipment(entityId, slot, stack);
    }

    private ItemStack getNmsItemStack(org.bukkit.inventory.ItemStack stack) {
        if (stack == null)
            return new ItemStack(Item.getById(0));

        ItemStack temp = new ItemStack(Item.getById(stack.getTypeId()), stack.getAmount());
        temp.setData((int) stack.getData().getData());
        if (stack.getEnchantments().size() >= 1)
            temp.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);

        return temp;
    }

    private DataWatcher clonedEntityHumanClass(Player player, int entityId) {
        EntityHuman h = new EntityHuman(((CraftWorld) player.getWorld()).getHandle(), ((CraftPlayer) player).getProfile()) {
            public void sendMessage(IChatBaseComponent arg0) {
                return;
            }

            public boolean a(int arg0, String arg1) {
                return false;
            }

            public BlockPosition getChunkCoordinates() {
                return null;
            }

            public boolean isSpectator() {
                return false;
            }
        };
        h.d(entityId);
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