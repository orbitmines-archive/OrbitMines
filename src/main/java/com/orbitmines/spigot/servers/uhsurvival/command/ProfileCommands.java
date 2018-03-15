package com.orbitmines.spigot.servers.uhsurvival.command;

import com.orbitmines.api.StaffRank;
import com.orbitmines.spigot.api.handlers.GUI;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.cmd.Command;
import com.orbitmines.spigot.api.handlers.itembuilders.ItemBuilder;
import com.orbitmines.spigot.api.utils.MathUtils;
import com.orbitmines.spigot.servers.uhsurvival.handlers.UHPlayer;
import com.orbitmines.spigot.servers.uhsurvival.handlers.profile.PlayerProfile;
import com.orbitmines.spigot.servers.uhsurvival.utils.enums.FoodType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

/**
 * Created by Robin on 3/13/2018.
 */
public class ProfileCommands extends Command {
    @Override
    public String[] getAlias() {
        return new String[]{"/profile", "/pf"};
    }

    @Override
    public String getHelp(OMPlayer omp) {
        return "";
    }

    @Override
    public void dispatch(OMPlayer omp, String[] a) {
        if(a[1] != null) {
            UHPlayer uhPlayer = UHPlayer.getUHPlayer(omp.getUUID());
            String argument = a[1].toLowerCase();
            PlayerProfile profile;
            if(a[2] != null){
                OfflinePlayer p = Bukkit.getPlayer(a[2]);
                if(p != null){
                    profile = PlayerProfile.getProfile(p.getUniqueId());
                } else {
                    //TODO: SEND MESSAGE
                    return;
                }
            } else {
                profile = uhPlayer.getProfile();
            }
            switch(argument){
                case "view": // /profile view [<player>] (STAFF ONLY)
                    ProfileView profileView = new ProfileView(uhPlayer, profile);
                    if(profileView.onOpen(omp)){
                        profileView.open(omp);
                    }
                    break;
                case "set":
                    if(omp.isEligible(StaffRank.MODERATOR)) {
                        String food;
                        String amount;
                        if (profile == uhPlayer.getProfile()) {
                            food = a[2];
                            amount = a[3];
                        } else {
                            food = a[3];
                            amount = a[4];
                        }
                        FoodType t = FoodType.valueOf(food.toUpperCase());
                        if (t != null) {
                            int am = MathUtils.getInteger(amount);
                            if (am <= 0) {
                                profile.removeFood(t, am);
                            }
                        }
                    }
                    break;
                case "reset":
                    if(omp.isEligible(StaffRank.MODERATOR)) {
                        for (FoodType type : FoodType.values()) {
                            profile.removeFood(type, 10);
                        }
                    }
                    break;
            }
        }
    }

    private class ProfileView extends GUI {

        private UHPlayer uhPlayer;
        private PlayerProfile profile;

        private ProfileView(UHPlayer player, PlayerProfile profile){
            this.uhPlayer = player;
            this.profile = profile;
            setupItems();
        }

        private void setupItems(){
            {
                ItemStack item = new ItemBuilder(Material.SKULL_ITEM, 1, 3).addLore("   Banned: " + profile.isBanned() + "   ").build();
                SkullMeta meta = (SkullMeta) item.getItemMeta();
                meta.setOwningPlayer(Bukkit.getOfflinePlayer(profile.getID()));
                item.setItemMeta(meta);
                ItemInstance instance = new ItemInstance(item) {
                    @Override
                    public void onClick(InventoryClickEvent event, OMPlayer omp) {
                        event.setCancelled(true);
                    }
                };
                this.add(4, instance);
            }
            {
                int slot = 10;
                for(FoodType type : FoodType.values()) {
                    ItemBuilder item = type.getBuilder(uhPlayer.getLanguage()).addLore("  " + type.getColor().substring(2) + profile.getAmount(type) + "  ");
                    ItemInstance instance = new ItemInstance(item.build()) {
                        @Override
                        public void onClick(InventoryClickEvent event, OMPlayer omp) {
                            event.setCancelled(true);
                        }
                    };
                    if(type != FoodType.WATER) {
                        this.add(slot, instance);
                        slot++;
                    } else {
                        this.add(22, instance);
                    }
                }
            }
        }

        @Override
        protected boolean onOpen(OMPlayer omp) {
            return omp.getUUID().equals(uhPlayer.getUUID()) || omp.isEligible(StaffRank.MODERATOR);
        }
    }
}
