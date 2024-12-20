/*
    GriefPrevention Server Plugin for Minecraft
    Copyright (C) 2011 Ryan Hamshire

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.orbitmines.spigot.servers.survival.griefprevention;
//holds all of GriefPrevention's player-tied data
public class PlayerData {
//    //the player's ID
//    public UUID playerID;
//
//    //the player's claims
//    private Vector<Claim> claims = null;
//
//    //how many claim blocks the player has earned via play time
//    private Integer accruedClaimBlocks = null;
//
//    //temporary holding area to avoid opening data files too early
//    private int newlyAccruedClaimBlocks = 0;
//
//    //how many claim blocks the player has been gifted by admins, or purchased via economy integration
//    private Integer bonusClaimBlocks = null;
//
//    //what "mode" the shovel is in determines what it will do when it's used
//    public ShovelMode shovelMode = ShovelMode.Basic;
//
//    //radius for restore nature fill mode
//    int fillRadius = 0;
//
//    //last place the player used the shovel, useful in creating and resizing claims,
//    //because the player must use the shovel twice in those instances
//    public Location lastShovelLocation = null;
//
//    //the claim this player is currently resizing
//    public Claim claimResizing = null;
//
//    //the claim this player is currently subdividing
//    public Claim claimSubdividing = null;
//
//    //whether or not the player has a pending /trapped rescue
//    public boolean pendingTrapped = false;
//
//    //safety confirmation for deleting multi-subdivision claims
//    public boolean warnedAboutMajorDeletion = false;
//
//    //whether or not this player has received a message about unlocking death drops since his last death
//    boolean receivedDropUnlockAdvertisement = false;
//
//    //whether or not this player's dropped items (on death) are unlocked for other players to pick up
//    boolean dropsAreUnlocked = false;
//
//    //player which a pet will be given to when it's right-clicked
//    OfflinePlayer petGiveawayRecipient = null;
//
//    //don't load data from secondary storage until it's needed
//    public int getAccruedClaimBlocks() {
//        if (this.accruedClaimBlocks == null) this.loadDataFromSecondaryStorage();
//
//        //update claim blocks with any he has accrued during his current play session
//        if (this.newlyAccruedClaimBlocks > 0) {
//            int accruedLimit = this.getAccruedClaimBlocksLimit();
//
//            //if over the limit before adding blocks, leave it as-is, because the limit may have changed AFTER he accrued the blocks
//            if (this.accruedClaimBlocks < accruedLimit) {
//                //move any in the holding area
//                int newTotal = this.accruedClaimBlocks + this.newlyAccruedClaimBlocks;
//
//                //respect limits
//                this.accruedClaimBlocks = Math.min(newTotal, accruedLimit);
//            }
//
//            this.newlyAccruedClaimBlocks = 0;
//            return this.accruedClaimBlocks;
//        }
//
//        return accruedClaimBlocks;
//    }
//
//    public void setAccruedClaimBlocks(Integer accruedClaimBlocks) {
//        this.accruedClaimBlocks = accruedClaimBlocks;
//        this.newlyAccruedClaimBlocks = 0;
//    }
//
//    public int getBonusClaimBlocks() {
//        if (this.bonusClaimBlocks == null) this.loadDataFromSecondaryStorage();
//        return bonusClaimBlocks;
//    }
//
//    public void setBonusClaimBlocks(Integer bonusClaimBlocks) {
//        this.bonusClaimBlocks = bonusClaimBlocks;
//    }
//
//    private void loadDataFromSecondaryStorage() {
//        //reach out to secondary storage to get any data there
//        PlayerData storageData = GriefPrevention.instance.dataStore.getPlayerDataFromStorage(this.playerID);
//
//        if (this.accruedClaimBlocks == null) {
//            if (storageData.accruedClaimBlocks != null) {
//                this.accruedClaimBlocks = storageData.accruedClaimBlocks;
//
//                //ensure at least minimum accrued are accrued (in case of settings changes to increase initial amount)
//                if (this.accruedClaimBlocks < GriefPrevention.instance.config_claims_initialBlocks) {
//                    this.accruedClaimBlocks = GriefPrevention.instance.config_claims_initialBlocks;
//                }
//
//            } else {
//                this.accruedClaimBlocks = GriefPrevention.instance.config_claims_initialBlocks;
//            }
//        }
//
//        if (this.bonusClaimBlocks == null) {
//            if (storageData.bonusClaimBlocks != null) {
//                this.bonusClaimBlocks = storageData.bonusClaimBlocks;
//            } else {
//                this.bonusClaimBlocks = 0;
//            }
//        }
//    }
//
//    public Vector<Claim> getClaims() {
//        if (this.claims == null) {
//            this.claims = new Vector<Claim>();
//
//            //find all the claims belonging to this player and note them for future reference
//            DataStore dataStore = GriefPrevention.instance.dataStore;
//            int totalClaimsArea = 0;
//            for (int i = 0; i < dataStore.claims.size(); i++) {
//                Claim claim = dataStore.claims.get(i);
//                if (!claim.inDataStore) {
//                    dataStore.claims.remove(i--);
//                    continue;
//                }
//                if (playerID.equals(claim.ownerID)) {
//                    this.claims.add(claim);
//                    totalClaimsArea += claim.getArea();
//                }
//            }
//
//            //ensure player has claim blocks for his claims, and at least the minimum accrued
//            this.loadDataFromSecondaryStorage();
//
//            //if total claimed area is more than total blocks available
//            int totalBlocks = this.accruedClaimBlocks + this.getBonusClaimBlocks() + GriefPrevention.instance.dataStore.getGroupBonusBlocks(this.playerID);
//            if (totalBlocks < totalClaimsArea) {
//                OfflinePlayer player = GriefPrevention.instance.getServer().getOfflinePlayer(this.playerID);
//                ConsoleUtils.msg(player.getName() + " has more claimed land than blocks available.  Adding blocks to fix.", CustomLogEntryTypes.Debug, true);
//                ConsoleUtils.msg(player.getName() + " Accrued blocks: " + this.getAccruedClaimBlocks() + " Bonus blocks: " + this.getBonusClaimBlocks(), CustomLogEntryTypes.Debug, true);
//                ConsoleUtils.msg("Total blocks: " + totalBlocks + " Total claimed area: " + totalClaimsArea, CustomLogEntryTypes.Debug, true);
//                for (Claim claim : this.claims) {
//                    if (!claim.inDataStore) continue;
//                    ConsoleUtils.msg(
//                            GriefPrevention.getfriendlyLocationString(claim.getLesserBoundaryCorner()) + " // "
//                                    + GriefPrevention.getfriendlyLocationString(claim.getGreaterBoundaryCorner()) + " = "
//                                    + claim.getArea()
//                            , CustomLogEntryTypes.Debug, true);
//                }
//
//                //try to fix it by adding to accrued blocks
//                this.accruedClaimBlocks = totalClaimsArea; //Set accrued blocks to equal total claims
//                int accruedLimit = this.getAccruedClaimBlocksLimit();
//                this.accruedClaimBlocks = Math.min(accruedLimit, this.accruedClaimBlocks); //set accrued blocks to maximum limit, if it's smaller
//                ConsoleUtils.msg("New accrued blocks: " + this.accruedClaimBlocks, CustomLogEntryTypes.Debug, true);
//
//                //Recalculate total blocks (accrued + bonus + permission group bonus)
//                totalBlocks = this.accruedClaimBlocks + this.getBonusClaimBlocks() + GriefPrevention.instance.dataStore.getGroupBonusBlocks(this.playerID);
//                ConsoleUtils.msg("New total blocks: " + totalBlocks, CustomLogEntryTypes.Debug, true);
//
//                //if that didn't fix it, then make up the difference with bonus blocks
//                if (totalBlocks < totalClaimsArea) {
//                    int bonusBlocksToAdd = totalClaimsArea - totalBlocks;
//                    this.bonusClaimBlocks += bonusBlocksToAdd;
//                    ConsoleUtils.msg("Accrued blocks weren't enough. Adding " + bonusBlocksToAdd + " bonus blocks.", CustomLogEntryTypes.Debug, true);
//                }
//                ConsoleUtils.msg(player.getName() + " Accrued blocks: " + this.getAccruedClaimBlocks() + " Bonus blocks: " + this.getBonusClaimBlocks() + " Group Bonus Blocks: " + GriefPrevention.instance.dataStore.getGroupBonusBlocks(this.playerID), CustomLogEntryTypes.Debug, true);
//                //Recalculate total blocks (accrued + bonus + permission group bonus)
//                totalBlocks = this.accruedClaimBlocks + this.getBonusClaimBlocks() + GriefPrevention.instance.dataStore.getGroupBonusBlocks(this.playerID);
//                ConsoleUtils.msg("Total blocks: " + totalBlocks + " Total claimed area: " + totalClaimsArea, CustomLogEntryTypes.Debug, true);
//                ConsoleUtils.msg("Remaining claim blocks to use: " + this.getRemainingClaimBlocks() + " (should be 0)", CustomLogEntryTypes.Debug, true);
//            }
//        }
//
//        for (int i = 0; i < this.claims.size(); i++) {
//            if (!claims.get(i).inDataStore) {
//                claims.remove(i--);
//            }
//        }
//
//        return claims;
//    }
//
//    //Limit can be changed by addons
//    public int getAccruedClaimBlocksLimit() {
//        if (this.AccruedClaimBlocksLimit < 0)
//            return GriefPrevention.instance.config_claims_maxAccruedBlocks_default;
//        return this.AccruedClaimBlocksLimit;
//    }
//
//    public void setAccruedClaimBlocksLimit(int limit) {
//        this.AccruedClaimBlocksLimit = limit;
//    }
//
//    public void accrueBlocks(int howMany) {
//        this.newlyAccruedClaimBlocks += howMany;
//    }
}