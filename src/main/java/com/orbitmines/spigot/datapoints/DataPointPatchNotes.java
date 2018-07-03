package com.orbitmines.spigot.datapoints;

import com.orbitmines.spigot.api.datapoints.DataPointLoader;
import com.orbitmines.spigot.api.datapoints.DataPointSign;
import com.orbitmines.spigot.api.handlers.itembuilders.ItemBuilder;
import com.orbitmines.spigot.api.handlers.npc.FloatingItem;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */
public class DataPointPatchNotes extends DataPointSign {

    private List<FloatingItem> items;

    public DataPointPatchNotes() {
        super("PATCH_NOTES", Type.IRON_PLATE, Material.WOOL, DyeColor.BROWN.getWoolData());

        items = new ArrayList<>();
    }

    @Override
    public boolean buildAt(DataPointLoader loader, Location location, String[] data) {
        FloatingItem item = new FloatingItem(new ItemBuilder(Material.BOOK_AND_QUILL), location);

        item.addLine(() -> "§c§lPATCH NOTES", false);

        item.create();

        items.add(item);

        return true;
    }

    @Override
    public boolean setup() {
        return true;
    }

    public List<FloatingItem> getItems() {
        return items;
    }
}
