package com.orbitmines.spigot.api.handlers.itembuilders;

import com.orbitmines.spigot.api.handlers.scoreboard.ScoreboardString;
import com.orbitmines.spigot.api.utils.ReflectionUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
* OrbitMines - @author Fadi Shawki - 29-7-2017
*/
public class PlayerSkullBuilder extends ItemBuilder {

    private ScoreboardString playerName;

    public PlayerSkullBuilder(ScoreboardString playerName) {
        this(playerName, 1);
    }

    public PlayerSkullBuilder(ScoreboardString playerName, int amount) {
        this(playerName, amount, null);
    }

    public PlayerSkullBuilder(ScoreboardString playerName, int amount, String displayName) {
        this(playerName, amount, displayName, (List<String>) null);
    }

    public PlayerSkullBuilder(ScoreboardString playerName, int amount, String displayName, String... lore) {
        this(playerName, amount, displayName, new ArrayList<>(Arrays.asList(lore)));
    }

    public PlayerSkullBuilder(ScoreboardString playerName, int amount, String displayName, List<String> lore) {
        super(Material.SKULL_ITEM, amount, 3, displayName, lore);

        this.playerName = playerName;
    }

    public ScoreboardString getPlayerName() {
        return playerName;
    }

    public void setPlayerName(ScoreboardString playerName) {
        this.playerName = playerName;
    }

    @Override
    public ItemStack build() {
        ItemStack itemStack = new ItemStack(material, amount, durability);
        SkullMeta meta = (SkullMeta) itemStack.getItemMeta();
        meta.setDisplayName(displayName);
        meta.setLore((lore == null || lore.size() == 0) ? null : new ArrayList<>(lore));

        String value = playerName.getString();
        if (value.length() <= 16)
            meta.setOwner(playerName.getString());
        else
            meta.setOwningPlayer(Bukkit.getOfflinePlayer(ReflectionUtils.getGameProfile(value).getId()));

        for (ItemFlag itemFlag : itemFlags) {
            meta.addItemFlags(itemFlag);
        }
        itemStack.setItemMeta(meta);

        return modify(itemStack);
    }
}
