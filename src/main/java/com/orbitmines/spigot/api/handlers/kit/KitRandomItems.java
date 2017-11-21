package com.orbitmines.spigot.api.handlers.kit;

import com.madblock.api.utils.RandomUtils;
import com.madblock.spigot.api.handlers.OMPlayer;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/*
* OrbitMines - @author Fadi Shawki - 29-7-2017
*/
public class KitRandomItems extends Kit {

    private List<List<ItemStack>> randomItems;

    public KitRandomItems(String name) {
        super(name);

        this.randomItems = new ArrayList<>();
        for (int i = 0; i < 36; i++) {
            this.randomItems.add(new ArrayList<>());
        }
    }

    public List<List<ItemStack>> getRandomItems() {
        return randomItems;
    }

    public void setRandomItems(List<List<ItemStack>> randomItems) {
        this.randomItems = randomItems;
    }

    public void setRandomItem(int index, List<ItemStack> content) {
        this.randomItems.set(index, content);
    }

    public List<ItemStack> getRandomItem(int index) {
        return this.randomItems.get(index);
    }

    @Override
    public void setItems(OMPlayer omp) {
        super.setItems(mbp);

        randomize(mbp);
    }

    @Override
    public void addItems(OMPlayer omp) {
        super.addItems(mbp);

        randomize(mbp);
    }

    @Override
    public void replaceItems(OMPlayer omp) {
        super.replaceItems(mbp);

        randomize(mbp);
    }

    private void randomize(OMPlayer omp) {
        int index = 0;
        for (List<ItemStack> items : getRandomItems()) {
            if (items != null && items.size() > 0)
                mbp.getPlayer().getInventory().setItem(index, RandomUtils.randomFrom(items));

            index++;
        }
    }
}
