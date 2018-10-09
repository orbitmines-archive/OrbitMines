package com.orbitmines.spigot.api.handlers.itemhandlers;

import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.itembuilders.ItemBuilder;
import com.orbitmines.spigot.api.runnables.PlayerRunnable;
import com.orbitmines.spigot.api.runnables.SpigotRunnable;
import com.orbitmines.spigot.api.utils.PlayerUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public abstract class ItemHoverBlockTarget extends ItemHover {

    private final List<Material> materials;

    private PlayerRunnable targetRunnable;

    public ItemHoverBlockTarget(ItemBuilder itemBuilder, boolean offHandAllowed, Material... materials) {
        super(itemBuilder, offHandAllowed);

        this.materials = Arrays.asList(materials);

        targetRunnable = new PlayerRunnable(SpigotRunnable.TimeUnit.TICK, 1) {
            @Override
            public void run(OMPlayer omp) {
                if (omp.getCurrentHover() != ItemHoverBlockTarget.this)
                    return;

                boolean targeted = isTargeted(omp);

                if (targeted && !entered.contains(omp)) {
                    onTargetEnter(omp);
                    entered.add(omp);
                } else if (!targeted && entered.contains(omp)) {
                    onLeave(omp);
                    entered.remove(omp);
                }
            }
        };
    }

    public abstract void onTargetEnter(OMPlayer omp);

    @Override
    protected void onEnter(OMPlayer omp, ItemStack item, int slot) {
        if (isTargeted(omp))
            onTargetEnter(omp);
    }

    public List<Material> getMaterials() {
        return materials;
    }

    @Override
    public void unregister() {
        super.unregister();

        targetRunnable.cancel();
    }

    private boolean isTargeted(OMPlayer omp) {
        Block block = PlayerUtils.getTargetBlock(omp.getPlayer(), 5);

        return block != null && this.materials.contains(block.getType());
    }
}
