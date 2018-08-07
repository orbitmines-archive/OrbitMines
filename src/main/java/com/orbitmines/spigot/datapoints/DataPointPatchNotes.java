package com.orbitmines.spigot.datapoints;

import com.orbitmines.api.Server;
import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.PatchNotes;
import com.orbitmines.spigot.api.datapoints.DataPointLoader;
import com.orbitmines.spigot.api.datapoints.DataPointSign;
import com.orbitmines.spigot.api.handlers.itembuilders.ItemBuilder;
import com.orbitmines.spigot.api.handlers.npc.FloatingItem;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */
public class DataPointPatchNotes extends DataPointSign {

    private OrbitMines orbitMines;

    private List<FloatingItem> items;

    public DataPointPatchNotes() {
        super("PATCH_NOTES", Type.IRON_PLATE, Material.BROWN_WOOL);

        orbitMines = OrbitMines.getInstance();
        items = new ArrayList<>();
    }

    @Override
    public boolean buildAt(DataPointLoader loader, Location location, String[] data) {
        new BukkitRunnable() {
            @Override
            public void run() {
                FloatingItem item = new FloatingItem(new ItemBuilder(Material.WRITABLE_BOOK), location.add(0.5, 0, 0.5));

                item.addLine(() -> "§c§lPATCH NOTES", false);

                Server server = orbitMines.getServerHandler().getServer();
                PatchNotes patchNotes = orbitMines.getPatchNotes();

                List<PatchNotes.Instance> latest = server == Server.HUB ? patchNotes.getLatest() : Collections.singletonList(patchNotes.getLatest(server));

                for (PatchNotes.Instance instance : latest) {
                    item.addLine(() -> (instance.isNew() ? "§c§lNew!§r " : "") + "§7" + instance.getVersion() + " \"" + instance.getName() + "\"" + (server == Server.HUB && instance.getServer() != Server.HUB ? " §7(" + instance.getServer().getDisplayName() + "§r§7)" : ""), false);
                }

//        if (server == Server.HUB)
//            item.setInteractAction(((event, omp) -> patchNotes.getHubInstance().open(omp)));
//        else
                item.setInteractAction((event, omp) -> patchNotes.getLatest(server).open(omp));

                item.create();

                items.add(item);
            }
        }.runTaskLater(orbitMines, 1);

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
