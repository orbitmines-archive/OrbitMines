package com.orbitmines.spigot.api.nms.itemstack;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.spigot.api.handlers.itembuilders.WrittenBookBuilder;
import net.minecraft.server.v1_13_R2.*;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

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
    public ItemStack setMetaData(ItemStack item, String tagName, String key, String value) {
        net.minecraft.server.v1_13_R2.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);

        NBTTagCompound tag = nmsStack.hasTag() ? nmsStack.getTag() : new NBTTagCompound();

        NBTTagCompound orbitmines = tag.hasKey(tagName) ? tag.getCompound(tagName) : new NBTTagCompound();
        orbitmines.setString(key, value);

        tag.set(tagName, orbitmines);

        nmsStack.setTag(tag);

        return CraftItemStack.asCraftMirror(nmsStack);
    }

    @Override
    public String getMetaData(ItemStack item, String tagName, String key) {
        net.minecraft.server.v1_13_R2.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);

        NBTTagCompound tag = nmsStack.hasTag() ? nmsStack.getTag() : new NBTTagCompound();

        if (!tag.hasKey(tagName))
            return null;

        NBTTagCompound orbitmines = tag.getCompound(tagName);

        return orbitmines.hasKey(key) ? orbitmines.getString(key) : null;
    }

    @Override
    public HashMap<String, String> getMetaData(ItemStack item, String tagName) {
        net.minecraft.server.v1_13_R2.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);

        NBTTagCompound tag = nmsStack.hasTag() ? nmsStack.getTag() : new NBTTagCompound();

        if (!tag.hasKey(tagName))
            return null;

        NBTTagCompound orbitmines = tag.getCompound(tagName);

        HashMap<String, String> metaData = new HashMap<>();

        for (String key : orbitmines.getKeys()) {
            metaData.put(key, orbitmines.getString(key));
        }

        return metaData;
    }

    @Override
    public ItemStack setAttackDamage(ItemStack item, int damage) {
        net.minecraft.server.v1_13_R2.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);

        NBTTagCompound tag = nmsStack.hasTag() ? nmsStack.getTag() : new NBTTagCompound();

        NBTTagList attributes = new NBTTagList();
        NBTTagCompound damageAttribute = new NBTTagCompound();
        damageAttribute.set("AttributeName", new NBTTagString("generic.attackDamage"));
        damageAttribute.set("Name", new NBTTagString("generic.attackDamage"));
        damageAttribute.set("Amount", new NBTTagInt(damage));
        damageAttribute.set("Operation", new NBTTagInt(0));//0 -> value, 1 -> * 100%
        damageAttribute.set("UUIDLeast", new NBTTagInt(894654));
        damageAttribute.set("UUIDMost", new NBTTagInt(2872));
        damageAttribute.set("Slot", new NBTTagString("mainhand"));
        attributes.add(damageAttribute);

        tag.set("AttributeModifiers", attributes);

        nmsStack.setTag(tag);

        return CraftItemStack.asCraftMirror(nmsStack);
    }
}
