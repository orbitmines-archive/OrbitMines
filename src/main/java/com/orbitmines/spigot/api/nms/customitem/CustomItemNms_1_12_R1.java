package com.orbitmines.spigot.api.nms.customitem;

import com.orbitmines.spigot.api.utils.ItemUtils;
import com.orbitmines.spigot.api.Mob;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Created by Fadi on 30-4-2016.
 */
public class CustomItemNms_1_12_R1 implements CustomItemNms {

    public CustomItemNms_1_12_R1() {
        ItemUtils.FLAG_ENCHANTMENTS = ItemFlag.HIDE_ENCHANTS.ordinal();
        ItemUtils.FLAG_ATTRIBUTES_MODIFIERS = ItemFlag.HIDE_ATTRIBUTES.ordinal();
        ItemUtils.FLAG_UNBREAKABLE = ItemFlag.HIDE_UNBREAKABLE.ordinal();
        ItemUtils.FLAG_CAN_DESTROY = ItemFlag.HIDE_DESTROYS.ordinal();
        ItemUtils.FLAG_CAN_PLACE_ON = ItemFlag.HIDE_PLACED_ON.ordinal();
        ItemUtils.FLAG_POTIONS = ItemFlag.HIDE_POTION_EFFECTS.ordinal();
    }

    @Override
    public ItemStack hideFlags(ItemStack item, int... hideFlags) {
        ItemMeta meta = item.getItemMeta();

        ItemFlag[] values = ItemFlag.values();
        for (int flag : hideFlags) {
            meta.addItemFlags(values[flag]);
        }

        item.setItemMeta(meta);

        return item;
    }

    @Override
    public ItemStack setUnbreakable(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        meta.setUnbreakable(true);
        item.setItemMeta(meta);

        return item;
    }

    @Override
    public ItemStack setEggId(ItemStack item, Mob mob) {
        if (item == null)
            return null;

        net.minecraft.server.v1_12_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag = nmsStack.hasTag() ? nmsStack.getTag() : new NBTTagCompound();
        NBTTagCompound eggId = new NBTTagCompound();
        eggId.setString("id", mob.getRawName());
        tag.set("EntityTag", eggId);
        nmsStack.setTag(tag);

        return CraftItemStack.asCraftMirror(nmsStack);
    }
}
