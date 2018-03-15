package com.orbitmines.spigot.servers.uhsurvival.command;

import com.orbitmines.api.StaffRank;
import com.orbitmines.spigot.api.handlers.GUI;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.cmd.StaffCommand;
import com.orbitmines.spigot.api.utils.MathUtils;
import com.orbitmines.spigot.servers.uhsurvival.handlers.UHPlayer;
import com.orbitmines.spigot.servers.uhsurvival.handlers.dungeon.DungeonManager;
import com.orbitmines.spigot.servers.uhsurvival.handlers.dungeon.loottable.LootItem;
import com.orbitmines.spigot.servers.uhsurvival.handlers.dungeon.loottable.LootTable;
import com.orbitmines.spigot.servers.uhsurvival.handlers.dungeon.loottable.LootTableManager;
import com.orbitmines.spigot.servers.uhsurvival.utils.enums.Enchantment;
import com.orbitmines.spigot.servers.uhsurvival.utils.enums.ToolType;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Robin on 3/11/2018.
 */
public class LootTableCommand extends StaffCommand {

    private LootTableManager lootTableManager;

    public LootTableCommand(StaffRank rank) {
        super(rank);
        lootTableManager = DungeonManager.getLootTableManager();
    }

    @Override
    public String[] getAlias() {
        return new String[]{"/loot", "/loottable"};
    }

    @Override
    public String getHelp(OMPlayer omp) {
        return null;
    }

    @Override
    public void onDispatch(OMPlayer omp, String[] a) {
        UHPlayer player = UHPlayer.getUHPlayer(omp.getUUID());
        if (a[1] != null) {
            String name = a[2];
            switch (a[1].toLowerCase()) {
                case "create":
                    if(name != null){
                        lootTableManager.createLootTable(omp.getWorld(), name);
                    }
                    break;
                case "delete":
                    if(name != null) {
                        lootTableManager.removeLootTable(name);
                    }
                    break;
                case "setItem":
                    if(a[2] != null) {
                        LootItem item = LootItem.toLoot(player.getItemInMainHand());
                        if (item != null) {
                            switch (a[2]) {
                                case "level":
                                    if(ToolType.Type.getType(item.getItem().getMaterial()) != null) {
                                        item.setLevel(MathUtils.getInteger(a[3]) == -1 ? 1 : MathUtils.getInteger(a[3]));
                                    }
                                    break;
                                case "max":
                                    item.setAmount(MathUtils.getInteger(a[3]) == -1 ? 1 : MathUtils.getInteger(a[3]));
                                    break;
                                case "enchant":
                                    if(ToolType.Type.getType(item.getItem().getMaterial()) != null){
                                        Enchantment ench = Enchantment.getEnchantment(a[3]);
                                        if(ench != null){
                                            int level = MathUtils.getInteger(a[4]);
                                            if(level > 0 && level <= ench.getMaxLevel()){
                                                item.addEnchantment(ench, level);
                                            }
                                        }
                                    }
                                    break;
                                case "chance":
                                    double chance = MathUtils.getInteger(a[3]);
                                    if(chance >= 1 && chance <= 100){
                                        item.setChance(chance);
                                    }
                                    break;
                            }
                            player.getInventory().setItemInMainHand(item.toItem());
                        }
                    }
                    break;
                case "add":
                    if(name != null){
                        LootTable lootTable = lootTableManager.getLootTable(name);
                        if(lootTable != null) {
                            if (a[3] != null && a[3].equalsIgnoreCase("all")) {
                                for (ItemStack itemStack : player.getInventory()) {
                                    if (lootTableManager.isLootItem(itemStack)) {
                                        lootTable.addLootItem(LootItem.toLoot(itemStack));
                                    }
                                }
                            } else {
                                ItemStack item = player.getItemInMainHand();
                                if(lootTableManager.isLootItem(item)){
                                    lootTable.addLootItem(LootItem.toLoot(item));
                                }
                            }
                        }
                    }
                    break;
                case "list":
                    if(name != null) {
                        int page = MathUtils.getInteger(name) == -1 ? 0 : MathUtils.getInteger(name);
                        for(int i = page * 8; i < 8 * (page + 1); i++){
                            LootTable lootTable = (LootTable) lootTableManager.getLootTables().toArray()[i];
                            if(lootTable != null) {
                                player.sendMessage(i+"." + " Name: " + lootTable.getName() + " Content: " + lootTable.getLootItems().size());
                            }
                        }
                    }
                    break;
                case "content":
                    if(name != null){
                        LootTable lootTable = lootTableManager.getLootTable(name);
                        if(lootTable != null){
                            new InventoryView(lootTable).onOpen(player);
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }

    public class InventoryView extends GUI {

        public InventoryView(LootTable lootTable){
            int slot = 0;
            for(LootItem lootItem : lootTable.getLootItems()){
                ItemInstance instance = new ItemInstance(lootItem.toItem()) {
                    @Override
                    public void onClick(InventoryClickEvent event, OMPlayer omp) {
                        ClickType type = event.getClick();
                        if(type == ClickType.RIGHT || type == ClickType.SHIFT_RIGHT){
                            lootTable.getLootItems().remove(LootItem.toLoot(this.getItemStack()));
                            omp.getInventory().addItem(this.getItemStack());
                            reopen(omp);
                        }
                    }
                };
                this.add(slot, instance);
                slot++;
            }
        }

        @Override
        protected boolean onOpen(OMPlayer omp) {
            return true;
        }
    }
}
