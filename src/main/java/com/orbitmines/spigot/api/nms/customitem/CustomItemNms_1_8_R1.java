package com.orbitmines.spigot.api.nms.customitem;

import com.orbitmines.spigot.api.Mob;
import net.minecraft.server.v1_8_R1.NBTTagByte;
import net.minecraft.server.v1_8_R1.NBTTagCompound;
import org.bukkit.craftbukkit.v1_8_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Fadi on 30-4-2016.
 */
public class CustomItemNms_1_8_R1 implements CustomItemNms {

    @Override
    public ItemStack hideFlags(ItemStack item, int... hideFlags) {
        try {
            net.minecraft.server.v1_8_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);

            NBTTagCompound tag = new NBTTagCompound();

            if (nmsStack.hasTag()) tag = nmsStack.getTag();

            int hide = 0;
            for (int i : hideFlags) {
                hide = hide + i;
            }

            if (hide != 0) {
                tag.set("HideFlags", new NBTTagByte((byte) hide));
                nmsStack.setTag(tag);
            }

            return CraftItemStack.asCraftMirror(nmsStack);
        } catch (NullPointerException ex) {
            return item;
        }
    }

    @Override
    public ItemStack setUnbreakable(ItemStack item) {
        try {
            net.minecraft.server.v1_8_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);

            NBTTagCompound tag = new NBTTagCompound();
            if (nmsStack.hasTag()) tag = nmsStack.getTag();
            tag.set("Unbreakable", new NBTTagByte((byte) 1));
            nmsStack.setTag(tag);

            return CraftItemStack.asCraftMirror(nmsStack);
        } catch (NullPointerException ex) {
            return item;
        }
    }

    /* Fix for 1.9+ */
    @Override
    public ItemStack setEggId(ItemStack item, Mob mob){
        return item;
    }
}
