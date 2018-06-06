package com.orbitmines.spigot.api.nms.itemstack;

import com.orbitmines.api.utils.uuid.UUIDUtils;
import net.minecraft.server.v1_8_R3.NBTTagByte;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagList;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

/**
 * Created by Fadi on 30-4-2016.
 */
public class ItemStackNms_1_8_R3 implements ItemStackNms {

    public ItemStack setUnbreakable(ItemStack item) {
        net.minecraft.server.v1_8_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);

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
        net.minecraft.server.v1_8_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);

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
}
