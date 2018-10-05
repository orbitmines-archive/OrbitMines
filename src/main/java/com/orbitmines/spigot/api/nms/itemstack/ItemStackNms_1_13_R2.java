package com.orbitmines.spigot.api.nms.itemstack;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.spigot.api.handlers.itembuilders.WrittenBookBuilder;
import net.minecraft.server.v1_13_R2.EnumHand;
import net.minecraft.server.v1_13_R2.NBTTagCompound;
import net.minecraft.server.v1_13_R2.NBTTagList;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class ItemStackNms_1_13_R2 implements ItemStackNms {

    @Override
    public ItemStack setCustomSkullTexture(ItemStack item, String name, String value) {
        net.minecraft.server.v1_13_R2.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);

        NBTTagCompound tag = nmsStack.hasTag() ? nmsStack.getTag() : new NBTTagCompound();

        NBTTagCompound skullOwner = new NBTTagCompound();
        skullOwner.setString("Id", UUID.randomUUID().toString());
        skullOwner.setString("Name", "{\"text\":\"" + name + "\"}");

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
        ItemStack before = player.getInventory().getItemInMainHand();
        ItemStack book = builder.build();

        player.getInventory().setItemInMainHand(book);
        ((CraftPlayer) player).getHandle().a(CraftItemStack.asNMSCopy(book), EnumHand.MAIN_HAND);
        player.getInventory().setItemInMainHand(before);
    }

    @Override
    public ItemStack setMetaData(ItemStack item, String key, String value) {
        net.minecraft.server.v1_13_R2.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);

        NBTTagCompound tag = nmsStack.hasTag() ? nmsStack.getTag() : new NBTTagCompound();

        NBTTagCompound orbitmines = tag.hasKey("OrbitMines") ? tag.getCompound("OrbitMines") : new NBTTagCompound();
        orbitmines.setString(key, value);

        tag.set("OrbitMines", orbitmines);

        nmsStack.setTag(tag);

        return CraftItemStack.asCraftMirror(nmsStack);
    }

    @Override
    public String getMetaData(ItemStack item, String key) {
        net.minecraft.server.v1_13_R2.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);

        NBTTagCompound tag = nmsStack.hasTag() ? nmsStack.getTag() : new NBTTagCompound();

        if (!tag.hasKey("OrbitMines"))
            return null;

        NBTTagCompound orbitmines = tag.getCompound("OrbitMines");

        return orbitmines.hasKey(key) ? orbitmines.getString(key) : null;
    }

    @Override
    public Map<String, String> getMetaData(ItemStack item) {
        net.minecraft.server.v1_13_R2.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);

        NBTTagCompound tag = nmsStack.hasTag() ? nmsStack.getTag() : new NBTTagCompound();

        if (!tag.hasKey("OrbitMines"))
            return null;

        NBTTagCompound orbitmines = tag.getCompound("OrbitMines");

        Map<String, String> metaData = new HashMap<>();

        for (String key : orbitmines.getKeys()) {
            metaData.put(key, orbitmines.getString(key));
        }

        return metaData;
    }
}
