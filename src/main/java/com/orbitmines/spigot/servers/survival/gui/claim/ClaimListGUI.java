package com.orbitmines.spigot.servers.survival.gui.claim;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.CachedPlayer;
import com.orbitmines.spigot.api.handlers.GUI;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.itembuilders.ItemBuilder;
import com.orbitmines.spigot.api.handlers.itembuilders.PlayerSkullBuilder;
import com.orbitmines.spigot.servers.survival.Survival;
import com.orbitmines.spigot.servers.survival.handlers.SurvivalPlayer;
import com.orbitmines.spigot.servers.survival.handlers.claim.Claim;
import org.bukkit.Sound;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;

public class ClaimListGUI extends GUI {

    private final int CLAIMS_PER_PAGE = 18;
    private final int NEW_PER_PAGE = CLAIMS_PER_PAGE / 9;

    private Survival survival;
    private CachedPlayer owner;
    private int page;

    public ClaimListGUI(Survival survival, CachedPlayer owner) {
        this(survival, owner, 0);
    }

    public ClaimListGUI(Survival survival, CachedPlayer owner, int page) {
        this.survival = survival;
        this.owner = owner;
        this.page = page;

        newInventory(36, "§0§l" + owner.getPlayerName() + "'s Claims");
    }

    @Override
    protected boolean onOpen(OMPlayer player) {
        SurvivalPlayer omp = (SurvivalPlayer) player;

        List<Claim> claims = survival.getClaimHandler().getClaims(owner.getUUID());

        int slot = 9;

        for (Claim claim : getClaimsForPage(claims)) {
            if (claim != null) {
                ItemBuilder item = ClaimGUI.getClaimIcon(survival, omp, claim);

                if (claim.isOwner(omp.getUUID()) || survival.canEditOtherClaims(omp))
                    add(slot, new ItemInstance(item.addLore("").addLore(omp.lang("§aKlik hier om te beheren.", "§aClick here to manage.")).build()) {
                        @Override
                        public void onClick(InventoryClickEvent event, OMPlayer player) {
                            new ClaimGUI(survival, claim).open(omp);
                        }
                    });
                else
                    add(slot, new EmptyItemInstance(item.build()));
            } else {
                clear(slot);
            }

            slot++;
        }

        if (page != 0)
            add(3, 0, new ItemInstance(new PlayerSkullBuilder(() -> "Lime Arrow Left", 1, omp.lang("§7« Meer Claims", "§7« More Claims")).setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjUzNDc0MjNlZTU1ZGFhNzkyMzY2OGZjYTg1ODE5ODVmZjUzODlhNDU0MzUzMjFlZmFkNTM3YWYyM2QifX19").build()) {
                @Override
                public void onClick(InventoryClickEvent event, OMPlayer omp) {
                    page--;
                    omp.playSound(Sound.UI_BUTTON_CLICK);
                    reopen(omp);
                }
            });
        else
            clear(3, 0);

        if (canHaveMorePages(claims))
            add(3, 8, new ItemInstance(new PlayerSkullBuilder(() -> "Lime Arrow Right", 1, omp.lang("§7Meer Claims »", "§7More Claims »")).setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGVmMzU2YWQyYWE3YjE2NzhhZWNiODgyOTBlNWZhNWEzNDI3ZTVlNDU2ZmY0MmZiNTE1NjkwYzY3NTE3YjgifX19").build()) {
                @Override
                public void onClick(InventoryClickEvent event, OMPlayer omp) {
                    page++;
                    omp.playSound(Sound.UI_BUTTON_CLICK);
                    reopen(omp);
                }
            });
        else
            clear(3, 8);

        return true;
    }

    private Claim[] getClaimsForPage(List<Claim> claims) {
        Claim[] pageClaims = new Claim[CLAIMS_PER_PAGE];

        for (int i = 0; i < CLAIMS_PER_PAGE; i++) {
            if (claims.size() > i)
                pageClaims[i] = claims.get(i);
        }

        if (page != 0) {
            for (int i = 0; i < page; i++) {
                for (int j = 0; j < CLAIMS_PER_PAGE; j++) {
                    int check = -1;
                    if ((j + 1) % 9 == 0)
                        check = (j + 1) / 9;

                    if (check != -1) {
                        int next = CLAIMS_PER_PAGE + check + (NEW_PER_PAGE * i) - 1;
                        pageClaims[j] = claims.size() > next ? claims.get(next) : null;
                    } else {
                        pageClaims[j] = pageClaims[j + 1];
                    }
                }
            }
        }

        return pageClaims;
    }

    private boolean canHaveMorePages(List<Claim> claims) {
        int claimAmount = claims.size();

        if (claimAmount <= CLAIMS_PER_PAGE)
            return false;

        int maxPage = claimAmount % NEW_PER_PAGE;
        maxPage = maxPage != 0 ? (claimAmount - CLAIMS_PER_PAGE + (NEW_PER_PAGE - maxPage)) / NEW_PER_PAGE : (claimAmount - CLAIMS_PER_PAGE) / NEW_PER_PAGE;

        return maxPage > page;
    }
}
