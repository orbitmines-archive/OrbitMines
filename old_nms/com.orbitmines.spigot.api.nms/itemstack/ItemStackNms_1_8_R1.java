package com.orbitmines.spigot.api.nms.itemstack;

import com.orbitmines.api.utils.uuid.UUIDUtils;
import com.orbitmines.spigot.api.handlers.itembuilders.WrittenBookBuilder;
import net.minecraft.server.v1_8_R1.NBTTagByte;
import net.minecraft.server.v1_8_R1.NBTTagCompound;
import net.minecraft.server.v1_8_R1.NBTTagList;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R1.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

/**
 * Created by Fadi on 30-4-2016.
 */
public class ItemStackNms_1_8_R1 implements ItemStackNms {

    public ItemStack setUnbreakable(ItemStack item) {
        net.minecraft.server.v1_8_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);

        NBTTagCompound tag = nmsStack.hasTag() ? nmsStack.getTag() : new NBTTagCompound();

        tag.set("Unbreakable", new NBTTagByte((byte) 1));
        nmsStack.setTag(tag);

        return CraftItemStack.asCraftMirror(nmsStack);
    }

    public ItemStack setEggId(ItemStack item, EntityType entityType) {
        item.setDurability(entityType.getTypeId());
        return item;
    }

    @Override
    public ItemStack setCustomSkullTexture(ItemStack item, String name, String value) {
        //TODO NOT TESTED
        net.minecraft.server.v1_8_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);

        NBTTagCompound tag = nmsStack.hasTag() ? nmsStack.getTag() : new NBTTagCompound();

        NBTTagCompound skullOwner = new NBTTagCompound();
        skullOwner.setString("Id", UUIDUtils.trim(UUID.randomUUID()));
        skullOwner.setString("Name", name);

        NBTTagCompound properties = new NBTTagCompound();
        NBTTagCompound texture = new NBTTagCompound();
        texture.setString("Value", value);

        NBTTagList list = new NBTTagList();
        list.add(texture);

        properties.set("textures", list);

        skullOwner.set("Properties", properties);
        tag.set("SkullOwner", skullOwner);

        nmsStack.setTag(tag);

        return CraftItemStack.asCraftMirror(nmsStack);
    }

    @Override
    public void openBook(Player player, WrittenBookBuilder builder) {
        ((CraftPlayer) player).getHandle().openBook(CraftItemStack.asNMSCopy(builder.build()));
    }
}
