package com.orbitmines.spigot.api.nms.itemstack;

import com.orbitmines.api.utils.uuid.UUIDUtils;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.NBTTagList;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SpawnEggMeta;

import java.util.UUID;

/**
 * Created by Fadi on 30-4-2016.
 */
public class ItemStackNms_1_12_R1 implements ItemStackNms {

    public ItemStack setUnbreakable(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        meta.setUnbreakable(true);
        item.setItemMeta(meta);

        return item;
    }

    public ItemStack setEggId(ItemStack item, EntityType entityType) {
        SpawnEggMeta meta = (SpawnEggMeta) item.getItemMeta();
        meta.setSpawnedType(entityType);
        item.setItemMeta(meta);

        return item;
    }

    @Override
    public ItemStack setCustomSkullTexture(ItemStack item, String name, String value) {
        net.minecraft.server.v1_12_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);

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
