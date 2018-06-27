package com.orbitmines.spigot.api.nms.itemstack;

import com.orbitmines.api.utils.uuid.UUIDUtils;
import net.minecraft.server.v1_10_R1.NBTTagByte;
import net.minecraft.server.v1_10_R1.NBTTagCompound;
import net.minecraft.server.v1_10_R1.NBTTagList;
import org.bukkit.craftbukkit.v1_10_R1.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

/**
 * Created by Fadi on 30-4-2016.
 */
public class ItemStackNms_1_10_R1 implements ItemStackNms {

    public ItemStack setUnbreakable(ItemStack item) {
        net.minecraft.server.v1_10_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);

        NBTTagCompound tag = nmsStack.hasTag() ? nmsStack.getTag() : new NBTTagCompound();

        tag.set("Unbreakable", new NBTTagByte((byte) 1));
        nmsStack.setTag(tag);

        return CraftItemStack.asCraftMirror(nmsStack);
    }

    public ItemStack setEggId(ItemStack item, EntityType entityType) {
        net.minecraft.server.v1_10_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);

        NBTTagCompound tag = nmsStack.hasTag() ? nmsStack.getTag() : new NBTTagCompound();

        NBTTagCompound eggId = new NBTTagCompound();
        eggId.setString("id", entityType.getName());
        tag.set("EntityTag", eggId);
        nmsStack.setTag(tag);

        return CraftItemStack.asCraftMirror(nmsStack);
    }

    @Override
    public ItemStack setCustomSkullTexture(ItemStack item, String name, String value) {
        //TODO NOT TESTED
        net.minecraft.server.v1_10_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);

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
