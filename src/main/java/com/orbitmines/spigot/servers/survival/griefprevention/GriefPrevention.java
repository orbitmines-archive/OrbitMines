/*
    GriefPrevention Server Plugin for Minecraft
    Copyright (C) 2012 Ryan Hamshire

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

import org.bukkit.plugin.java.JavaPlugin;

public class GriefPrevention extends JavaPlugin {
//	//for convenience, a reference to the instance of this plugin
//	public static GriefPrevention instance;
//
//	//this handles data storage, like player and region data
//	public DataStore dataStore;
//
//	//configuration variables, loaded/saved from a config.yml
//
//	//claim mode for each world
//	public ConcurrentHashMap<World, ClaimsMode> config_claims_worldModes;
//
//	public boolean config_claims_preventGlobalMonsterEggs = true;                   //whether monster eggs can be placed regardless of trust.
//	public boolean config_claims_preventTheft = true;                               //whether containers and crafting blocks are protectable
//	public boolean config_claims_protectHorses = true;                              //whether horses on a claim should be protected by that claim's rules
//	public boolean config_claims_preventButtonsSwitches = true;                     //whether buttons and switches are protectable
//	public boolean config_claims_lockWoodenDoors = true;                            //whether wooden doors should be locked by default (require /accesstrust)
//	public boolean config_claims_lockTrapDoors = true;                              //whether trap doors should be locked by default (require /accesstrust)
//	public boolean config_claims_lockFenceGates = true;                             //whether fence gates should be locked by default (require /accesstrust)
//	public boolean config_claims_enderPearlsRequireAccessTrust = false;             //whether teleporting into a claim with a pearl requires access trust
//	public boolean config_claims_portalsRequirePermission = true;                   //whether nether portals require permission to generate.  defaults to off for performance reasons
//	public boolean config_claims_villagerTradingRequiresTrust = true;               //whether trading with a claimed villager requires permission
//
//	public int config_claims_initialBlocks = 250;                                   //the number of claim blocks a new player starts with
//	public double config_claims_abandonReturnRatio = 1.0;                           //the portion of claim blocks returned to a player when a claim is abandoned
//	public int config_claims_maxDepth = 0;                                          //limit on how deep claims can go
//
//	public int config_claims_claimsExtendIntoGroundDistance = 300;                  //how far below the shoveled block a new claim will reach
//	public int config_claims_minWidth = 3;                                          //minimum width for non-admin claims
//	public int config_claims_minArea = 9;                                           //minimum area for non-admin claims
//
//	public boolean config_claims_survivalAutoNatureRestoration = false;             //whether survival claims will be automatically restored to nature when auto-deleted
//
//	public Material config_claims_investigationTool;                //which material will be used to investigate claims with a right click
//	public Material config_claims_modificationTool;                    //which material will be used to create/resize claims with a right click
//
//	public MaterialCollection config_mods_accessTrustIds;            //list of block IDs which should require /accesstrust for player interaction
//	public MaterialCollection config_mods_containerTrustIds;        //list of block IDs which should require /containertrust for player interaction
//
//	public HashMap<String, Integer> config_seaLevelOverride;        //override for sea level, because bukkit doesn't report the right value for all situations
//
//	//handles slash commands
//	@SuppressWarnings("deprecation")
//	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
//
//		Player player = null;
//		if (sender instanceof Player) {
//			player = (Player) sender;
//		}
//
//		//claim
//		if (cmd.getName().equalsIgnoreCase("claim") && player != null) {
//			if (!GriefPrevention.instance.claimsEnabledForWorld(player.getWorld())) {
//				GriefPrevention.sendMessage(player, TextMode.Err, Messages.ClaimsDisabledWorld);
//				return true;
//			}
//
//			PlayerData playerData = this.dataStore.getPlayerData(player.getUniqueId());
//
//			//if he's at the claim count per player limit already and doesn't have permission to bypass, display an error message
//			if (GriefPrevention.instance.config_claims_maxClaimsPerPlayer > 0 &&
//					!player.hasPermission("griefprevention.overrideclaimcountlimit") &&
//					playerData.getClaims().size() >= GriefPrevention.instance.config_claims_maxClaimsPerPlayer) {
//				GriefPrevention.sendMessage(player, TextMode.Err, Messages.ClaimCreationFailedOverClaimCountLimit);
//				return true;
//			}
//
//			//default is chest claim radius, unless -1
//			int radius = GriefPrevention.instance.config_claims_automaticClaimsForNewPlayersRadius;
//			if (radius < 0) radius = (int) Math.ceil(Math.sqrt(GriefPrevention.instance.config_claims_minArea) / 2);
//
//			//if player has any claims, respect claim minimum size setting
//			if (playerData.getClaims().size() > 0) {
//				//if player has exactly one land claim, this requires the claim modification tool to be in hand (or creative mode player)
//				if (playerData.getClaims().size() == 1 && player.getGameMode() != GameMode.CREATIVE && player.getItemInHand().getType() != GriefPrevention.instance.config_claims_modificationTool) {
//					GriefPrevention.sendMessage(player, TextMode.Err, Messages.MustHoldModificationToolForThat);
//					return true;
//				}
//
//				radius = (int) Math.ceil(Math.sqrt(GriefPrevention.instance.config_claims_minArea) / 2);
//			}
//
//			//allow for specifying the radius
//			if (args.length > 0) {
//				if (playerData.getClaims().size() < 2 && player.getGameMode() != GameMode.CREATIVE && player.getItemInHand().getType() != GriefPrevention.instance.config_claims_modificationTool) {
//					GriefPrevention.sendMessage(player, TextMode.Err, Messages.RadiusRequiresGoldenShovel);
//					return true;
//				}
//
//				int specifiedRadius;
//				try {
//					specifiedRadius = Integer.parseInt(args[0]);
//				} catch (NumberFormatException e) {
//					return false;
//				}
//
//				if (specifiedRadius < radius) {
//					GriefPrevention.sendMessage(player, TextMode.Err, Messages.MinimumRadius, String.valueOf(radius));
//					return true;
//				} else {
//					radius = specifiedRadius;
//				}
//			}
//
//			if (radius < 0) radius = 0;
//
//			Location lc = player.getLocation().add(-radius, 0, -radius);
//			Location gc = player.getLocation().add(radius, 0, radius);
//
//			//player must have sufficient unused claim blocks
//			int area = Math.abs((gc.getBlockX() - lc.getBlockX() + 1) * (gc.getBlockZ() - lc.getBlockZ() + 1));
//			int remaining = playerData.getRemainingClaimBlocks();
//			if (remaining < area) {
//				GriefPrevention.sendMessage(player, TextMode.Err, Messages.CreateClaimInsufficientBlocks, String.valueOf(area - remaining));
//				GriefPrevention.instance.dataStore.tryAdvertiseAdminAlternatives(player);
//				return true;
//			}
//
//			CreateClaimResult result = this.dataStore.createClaim(lc.getWorld(),
//					lc.getBlockX(), gc.getBlockX(),
//					lc.getBlockY() - GriefPrevention.instance.config_claims_claimsExtendIntoGroundDistance - 1,
//					gc.getWorld().getHighestBlockYAt(gc) - GriefPrevention.instance.config_claims_claimsExtendIntoGroundDistance - 1,
//					lc.getBlockZ(), gc.getBlockZ(),
//					player.getUniqueId(), null, null, player);
//			if (!result.succeeded) {
//				if (result.claim != null) {
//					GriefPrevention.sendMessage(player, TextMode.Err, Messages.CreateClaimFailOverlapShort);
//
//					Visualization visualization = Visualization.FromClaim(result.claim, player.getEyeLocation().getBlockY(), VisualizationType.ErrorClaim, player.getLocation());
//					Visualization.Apply(player, visualization);
//				} else {
//					GriefPrevention.sendMessage(player, TextMode.Err, Messages.CreateClaimFailOverlapRegion);
//				}
//			} else {
//				GriefPrevention.sendMessage(player, TextMode.Success, Messages.CreateClaimSuccess);
//
//				//link to a video demo of land claiming, based on world type
//				if (GriefPrevention.instance.creativeRulesApply(player.getLocation())) {
//					GriefPrevention.sendMessage(player, TextMode.Instr, Messages.CreativeBasicsVideo2, DataStore.CREATIVE_VIDEO_URL);
//				} else if (GriefPrevention.instance.claimsEnabledForWorld(player.getLocation().getWorld())) {
//					GriefPrevention.sendMessage(player, TextMode.Instr, Messages.SurvivalBasicsVideo2, DataStore.SURVIVAL_VIDEO_URL);
//				}
//				Visualization visualization = Visualization.FromClaim(result.claim, player.getEyeLocation().getBlockY(), VisualizationType.Claim, player.getLocation());
//				Visualization.Apply(player, visualization);
//				playerData.claimResizing = null;
//				playerData.lastShovelLocation = null;
//
//				this.autoExtendClaim(result.claim);
//			}
//
//			return true;
//		}
//
//		//extendclaim
//		if (cmd.getName().equalsIgnoreCase("extendclaim") && player != null) {
//			if (args.length < 1) {
//				//link to a video demo of land claiming, based on world type
//				if (GriefPrevention.instance.creativeRulesApply(player.getLocation())) {
//					GriefPrevention.sendMessage(player, TextMode.Instr, Messages.CreativeBasicsVideo2, DataStore.CREATIVE_VIDEO_URL);
//				} else if (GriefPrevention.instance.claimsEnabledForWorld(player.getLocation().getWorld())) {
//					GriefPrevention.sendMessage(player, TextMode.Instr, Messages.SurvivalBasicsVideo2, DataStore.SURVIVAL_VIDEO_URL);
//				}
//				return false;
//			}
//
//			int amount;
//			try {
//				amount = Integer.parseInt(args[0]);
//			} catch (NumberFormatException e) {
//				//link to a video demo of land claiming, based on world type
//				if (GriefPrevention.instance.creativeRulesApply(player.getLocation())) {
//					GriefPrevention.sendMessage(player, TextMode.Instr, Messages.CreativeBasicsVideo2, DataStore.CREATIVE_VIDEO_URL);
//				} else if (GriefPrevention.instance.claimsEnabledForWorld(player.getLocation().getWorld())) {
//					GriefPrevention.sendMessage(player, TextMode.Instr, Messages.SurvivalBasicsVideo2, DataStore.SURVIVAL_VIDEO_URL);
//				}
//				return false;
//			}
//
//			//requires claim modification tool in hand
//			if (player.getGameMode() != GameMode.CREATIVE && player.getItemInHand().getType() != GriefPrevention.instance.config_claims_modificationTool) {
//				GriefPrevention.sendMessage(player, TextMode.Err, Messages.MustHoldModificationToolForThat);
//				return true;
//			}
//
//			//must be standing in a land claim
//			PlayerData playerData = this.dataStore.getPlayerData(player.getUniqueId());
//			Claim claim = this.dataStore.getClaimAt(player.getLocation(), true, playerData.lastClaim);
//			if (claim == null) {
//				GriefPrevention.sendMessage(player, TextMode.Err, Messages.StandInClaimToResize);
//				return true;
//			}
//
//			//must have permission to edit the land claim you're in
//			String errorMessage = claim.allowEdit(player);
//			if (errorMessage != null) {
//				GriefPrevention.sendMessage(player, TextMode.Err, Messages.NotYourClaim);
//				return true;
//			}
//
//			//determine new corner coordinates
//			org.bukkit.util.Vector direction = player.getLocation().getDirection();
//			if (direction.getY() > .75) {
//				GriefPrevention.sendMessage(player, TextMode.Info, Messages.ClaimsExtendToSky);
//				return true;
//			}
//
//			if (direction.getY() < -.75) {
//				GriefPrevention.sendMessage(player, TextMode.Info, Messages.ClaimsAutoExtendDownward);
//				return true;
//			}
//
//			Location lc = claim.getLesserBoundaryCorner();
//			Location gc = claim.getGreaterBoundaryCorner();
//			int newx1 = lc.getBlockX();
//			int newx2 = gc.getBlockX();
//			int newy1 = lc.getBlockY();
//			int newy2 = gc.getBlockY();
//			int newz1 = lc.getBlockZ();
//			int newz2 = gc.getBlockZ();
//
//			//if changing Z only
//			if (Math.abs(direction.getX()) < .3) {
//				if (direction.getZ() > 0) {
//					newz2 += amount;  //north
//				} else {
//					newz1 -= amount;  //south
//				}
//			}
//
//			//if changing X only
//			else if (Math.abs(direction.getZ()) < .3) {
//				if (direction.getX() > 0) {
//					newx2 += amount;  //east
//				} else {
//					newx1 -= amount;  //west
//				}
//			}
//
//			//diagonals
//			else {
//				if (direction.getX() > 0) {
//					newx2 += amount;
//				} else {
//					newx1 -= amount;
//				}
//
//				if (direction.getZ() > 0) {
//					newz2 += amount;
//				} else {
//					newz1 -= amount;
//				}
//			}
//
//			//attempt resize
//			playerData.claimResizing = claim;
//			this.dataStore.resizeClaimWithChecks(player, playerData, newx1, newx2, newy1, newy2, newz1, newz2);
//			playerData.claimResizing = null;
//
//			return true;
//		}
//
//		//abandonclaim
//		if (cmd.getName().equalsIgnoreCase("abandonclaim") && player != null) {
//			return this.abandonClaimHandler(player, false);
//		}
//
//		//abandontoplevelclaim
//		if (cmd.getName().equalsIgnoreCase("abandontoplevelclaim") && player != null) {
//			return this.abandonClaimHandler(player, true);
//		}
//
//		//ignoreclaims
//		if (cmd.getName().equalsIgnoreCase("ignoreclaims") && player != null) {
//			PlayerData playerData = this.dataStore.getPlayerData(player.getUniqueId());
//
//			playerData.ignoreClaims = !playerData.ignoreClaims;
//
//			//toggle ignore claims mode on or off
//			if (!playerData.ignoreClaims) {
//				GriefPrevention.sendMessage(player, TextMode.Success, Messages.RespectingClaims);
//			} else {
//				GriefPrevention.sendMessage(player, TextMode.Success, Messages.IgnoringClaims);
//			}
//
//			return true;
//		}
//
//		//abandonallclaims
//		else if (cmd.getName().equalsIgnoreCase("abandonallclaims") && player != null) {
//			if (args.length != 0) return false;
//
//			//count claims
//			PlayerData playerData = this.dataStore.getPlayerData(player.getUniqueId());
//			int originalClaimCount = playerData.getClaims().size();
//
//			//check count
//			if (originalClaimCount == 0) {
//				GriefPrevention.sendMessage(player, TextMode.Err, Messages.YouHaveNoClaims);
//				return true;
//			}
//
//			//adjust claim blocks
//			for (Claim claim : playerData.getClaims()) {
//				playerData.setAccruedClaimBlocks(playerData.getAccruedClaimBlocks() - (int) Math.ceil((claim.getArea() * (1 - this.config_claims_abandonReturnRatio))));
//			}
//
//			//delete them
//			this.dataStore.deleteClaimsForPlayer(player.getUniqueId(), false);
//
//			//inform the player
//			int remainingBlocks = playerData.getRemainingClaimBlocks();
//			GriefPrevention.sendMessage(player, TextMode.Success, Messages.SuccessfulAbandon, String.valueOf(remainingBlocks));
//
//			//revert any current visualization
//			Visualization.Revert(player);
//
//			return true;
//		}
//
//		//restore nature
//		else if (cmd.getName().equalsIgnoreCase("restorenature") && player != null) {
//			//change shovel mode
//			PlayerData playerData = this.dataStore.getPlayerData(player.getUniqueId());
//			playerData.shovelMode = ShovelMode.RestoreNature;
//			GriefPrevention.sendMessage(player, TextMode.Instr, Messages.RestoreNatureActivate);
//			return true;
//		}
//
//		//restore nature aggressive mode
//		else if (cmd.getName().equalsIgnoreCase("restorenatureaggressive") && player != null) {
//			//change shovel mode
//			PlayerData playerData = this.dataStore.getPlayerData(player.getUniqueId());
//			playerData.shovelMode = ShovelMode.RestoreNatureAggressive;
//			GriefPrevention.sendMessage(player, TextMode.Warn, Messages.RestoreNatureAggressiveActivate);
//			return true;
//		}
//
//		//restore nature fill mode
//		else if (cmd.getName().equalsIgnoreCase("restorenaturefill") && player != null) {
//			//change shovel mode
//			PlayerData playerData = this.dataStore.getPlayerData(player.getUniqueId());
//			playerData.shovelMode = ShovelMode.RestoreNatureFill;
//
//			//set radius based on arguments
//			playerData.fillRadius = 2;
//			if (args.length > 0) {
//				try {
//					playerData.fillRadius = Integer.parseInt(args[0]);
//				} catch (Exception exception) {
//				}
//			}
//
//			if (playerData.fillRadius < 0) playerData.fillRadius = 2;
//
//			GriefPrevention.sendMessage(player, TextMode.Success, Messages.FillModeActive, String.valueOf(playerData.fillRadius));
//			return true;
//		}
//
//		//trust <player>
//		else if (cmd.getName().equalsIgnoreCase("trust") && player != null) {
//			//requires exactly one parameter, the other player's name
//			if (args.length != 1) return false;
//
//			//most trust commands use this helper method, it keeps them consistent
//			this.handleTrustCommand(player, ClaimPermission.Build, args[0]);
//
//			return true;
//		}
//
//		//transferclaim <player>
//		else if (cmd.getName().equalsIgnoreCase("transferclaim") && player != null) {
//			//which claim is the user in?
//			Claim claim = this.dataStore.getClaimAt(player.getLocation(), true, null);
//			if (claim == null) {
//				GriefPrevention.sendMessage(player, TextMode.Instr, Messages.TransferClaimMissing);
//				return true;
//			}
//
//			//check additional permission for admin claims
//			if (claim.isAdminClaim() && !player.hasPermission("griefprevention.adminclaims")) {
//				GriefPrevention.sendMessage(player, TextMode.Err, Messages.TransferClaimPermission);
//				return true;
//			}
//
//			UUID newOwnerID = null;  //no argument = make an admin claim
//			String ownerName = "admin";
//
//			if (args.length > 0) {
//				OfflinePlayer targetPlayer = this.resolvePlayerByName(args[0]);
//				if (targetPlayer == null) {
//					GriefPrevention.sendMessage(player, TextMode.Err, Messages.PlayerNotFound2);
//					return true;
//				}
//				newOwnerID = targetPlayer.getUniqueId();
//				ownerName = targetPlayer.getName();
//			}
//
//			//change ownerhsip
//			try {
//				this.dataStore.changeClaimOwner(claim, newOwnerID);
//			} catch (NoTransferException e) {
//				GriefPrevention.sendMessage(player, TextMode.Instr, Messages.TransferTopLevel);
//				return true;
//			}
//
//			//confirm
//			GriefPrevention.sendMessage(player, TextMode.Success, Messages.TransferSuccess);
//			ConsoleUtils.msg(player.getName() + " transferred a claim at " + GriefPrevention.getfriendlyLocationString(claim.getLesserBoundaryCorner()) + " to " + ownerName + ".", CustomLogEntryTypes.AdminActivity);
//
//			return true;
//		}
//
//		//trustlist
//		else if (cmd.getName().equalsIgnoreCase("trustlist") && player != null) {
//			Claim claim = this.dataStore.getClaimAt(player.getLocation(), true, null);
//
//			//if no claim here, error message
//			if (claim == null) {
//				GriefPrevention.sendMessage(player, TextMode.Err, Messages.TrustListNoClaim);
//				return true;
//			}
//
//			//if no permission to manage permissions, error message
//			String errorMessage = claim.allowGrantPermission(player);
//			if (errorMessage != null) {
//				GriefPrevention.sendMessage(player, TextMode.Err, errorMessage);
//				return true;
//			}
//
//			//otherwise build a list of explicit permissions by permission level
//			//and send that to the player
//			ArrayList<String> builders = new ArrayList<String>();
//			ArrayList<String> containers = new ArrayList<String>();
//			ArrayList<String> accessors = new ArrayList<String>();
//			ArrayList<String> managers = new ArrayList<String>();
//			claim.getPermissions(builders, containers, accessors, managers);
//
//			GriefPrevention.sendMessage(player, TextMode.Info, Messages.TrustListHeader);
//
//			StringBuilder permissions = new StringBuilder();
//			permissions.append(ChatColor.GOLD + ">");
//
//			if (managers.size() > 0) {
//				for (int i = 0; i < managers.size(); i++)
//					permissions.append(this.trustEntryToPlayerName(managers.get(i)) + " ");
//			}
//
//			player.sendMessage(permissions.toString());
//			permissions = new StringBuilder();
//			permissions.append(ChatColor.YELLOW + ">");
//
//			if (builders.size() > 0) {
//				for (int i = 0; i < builders.size(); i++)
//					permissions.append(this.trustEntryToPlayerName(builders.get(i)) + " ");
//			}
//
//			player.sendMessage(permissions.toString());
//			permissions = new StringBuilder();
//			permissions.append(ChatColor.GREEN + ">");
//
//			if (containers.size() > 0) {
//				for (int i = 0; i < containers.size(); i++)
//					permissions.append(this.trustEntryToPlayerName(containers.get(i)) + " ");
//			}
//
//			player.sendMessage(permissions.toString());
//			permissions = new StringBuilder();
//			permissions.append(ChatColor.BLUE + ">");
//
//			if (accessors.size() > 0) {
//				for (int i = 0; i < accessors.size(); i++)
//					permissions.append(this.trustEntryToPlayerName(accessors.get(i)) + " ");
//			}
//
//			player.sendMessage(permissions.toString());
//
//			player.sendMessage(
//					ChatColor.GOLD + this.dataStore.getMessage(Messages.Manage) + " " +
//							ChatColor.YELLOW + this.dataStore.getMessage(Messages.Build) + " " +
//							ChatColor.GREEN + this.dataStore.getMessage(Messages.Containers) + " " +
//							ChatColor.BLUE + this.dataStore.getMessage(Messages.Access));
//
//			return true;
//		}
//
//		//untrust <player> or untrust [<group>]
//		else if (cmd.getName().equalsIgnoreCase("untrust") && player != null) {
//			//requires exactly one parameter, the other player's name
//			if (args.length != 1) return false;
//
//			//determine which claim the player is standing in
//			Claim claim = this.dataStore.getClaimAt(player.getLocation(), true /*ignore height*/, null);
//
//			//bracket any permissions
//			if (args[0].contains(".") && !args[0].startsWith("[") && !args[0].endsWith("]")) {
//				args[0] = "[" + args[0] + "]";
//			}
//
//			//determine whether a single player or clearing permissions entirely
//			boolean clearPermissions = false;
//			OfflinePlayer otherPlayer = null;
//			if (args[0].equals("all")) {
//				if (claim == null || claim.allowEdit(player) == null) {
//					clearPermissions = true;
//				} else {
//					GriefPrevention.sendMessage(player, TextMode.Err, Messages.ClearPermsOwnerOnly);
//					return true;
//				}
//			} else {
//				//validate player argument or group argument
//				if (!args[0].startsWith("[") || !args[0].endsWith("]")) {
//					otherPlayer = this.resolvePlayerByName(args[0]);
//					if (!clearPermissions && otherPlayer == null && !args[0].equals("public")) {
//						GriefPrevention.sendMessage(player, TextMode.Err, Messages.PlayerNotFound2);
//						return true;
//					}
//
//					//correct to proper casing
//					if (otherPlayer != null)
//						args[0] = otherPlayer.getName();
//				}
//			}
//
//			//if no claim here, apply changes to all his claims
//			if (claim == null) {
//				PlayerData playerData = this.dataStore.getPlayerData(player.getUniqueId());
//				for (int i = 0; i < playerData.getClaims().size(); i++) {
//					claim = playerData.getClaims().get(i);
//
//					//if untrusting "all" drop all permissions
//					if (clearPermissions) {
//						claim.clearPermissions();
//					}
//
//					//otherwise drop individual permissions
//					else {
//						String idToDrop = args[0];
//						if (otherPlayer != null) {
//							idToDrop = otherPlayer.getUniqueId().toString();
//						}
//						claim.dropPermission(idToDrop);
//						claim.managers.remove(idToDrop);
//					}
//
//					//save changes
//					this.dataStore.saveClaim(claim);
//				}
//
//				//beautify for output
//				if (args[0].equals("public")) {
//					args[0] = "the public";
//				}
//
//				//confirmation message
//				if (!clearPermissions) {
//					GriefPrevention.sendMessage(player, TextMode.Success, Messages.UntrustIndividualAllClaims, args[0]);
//				} else {
//					GriefPrevention.sendMessage(player, TextMode.Success, Messages.UntrustEveryoneAllClaims);
//				}
//			}
//
//			//otherwise, apply changes to only this claim
//			else if (claim.allowGrantPermission(player) != null) {
//				GriefPrevention.sendMessage(player, TextMode.Err, Messages.NoPermissionTrust, claim.getOwnerName());
//				return true;
//			} else {
//				//if clearing all
//				if (clearPermissions) {
//					//requires owner
//					if (claim.allowEdit(player) != null) {
//						GriefPrevention.sendMessage(player, TextMode.Err, Messages.UntrustAllOwnerOnly);
//						return true;
//					}
//
//					claim.clearPermissions();
//					GriefPrevention.sendMessage(player, TextMode.Success, Messages.ClearPermissionsOneClaim);
//				}
//
//				//otherwise individual permission drop
//				else {
//					String idToDrop = args[0];
//					if (otherPlayer != null) {
//						idToDrop = otherPlayer.getUniqueId().toString();
//					}
//					boolean targetIsManager = claim.managers.contains(idToDrop);
//					if (targetIsManager && claim.allowEdit(player) != null)  //only claim owners can untrust managers
//					{
//						GriefPrevention.sendMessage(player, TextMode.Err, Messages.ManagersDontUntrustManagers, claim.getOwnerName());
//						return true;
//					} else {
//						claim.dropPermission(idToDrop);
//						claim.managers.remove(idToDrop);
//
//						//beautify for output
//						if (args[0].equals("public")) {
//							args[0] = "the public";
//						}
//
//						GriefPrevention.sendMessage(player, TextMode.Success, Messages.UntrustIndividualSingleClaim, args[0]);
//					}
//				}
//
//				//save changes
//				this.dataStore.saveClaim(claim);
//			}
//
//			return true;
//		}
//
//		//accesstrust <player>
//		else if (cmd.getName().equalsIgnoreCase("accesstrust") && player != null) {
//			//requires exactly one parameter, the other player's name
//			if (args.length != 1) return false;
//
//			this.handleTrustCommand(player, ClaimPermission.Access, args[0]);
//
//			return true;
//		}
//
//		//containertrust <player>
//		else if (cmd.getName().equalsIgnoreCase("containertrust") && player != null) {
//			//requires exactly one parameter, the other player's name
//			if (args.length != 1) return false;
//
//			this.handleTrustCommand(player, ClaimPermission.Inventory, args[0]);
//
//			return true;
//		}
//
//		//permissiontrust <player>
//		else if (cmd.getName().equalsIgnoreCase("permissiontrust") && player != null) {
//			//requires exactly one parameter, the other player's name
//			if (args.length != 1) return false;
//
//			this.handleTrustCommand(player, null, args[0]);  //null indicates permissiontrust to the helper method
//
//			return true;
//		}
//
//		//adminclaims
//		else if (cmd.getName().equalsIgnoreCase("adminclaims") && player != null) {
//			PlayerData playerData = this.dataStore.getPlayerData(player.getUniqueId());
//			playerData.shovelMode = ShovelMode.Admin;
//			GriefPrevention.sendMessage(player, TextMode.Success, Messages.AdminClaimsMode);
//
//			return true;
//		}
//
//		//basicclaims
//		else if (cmd.getName().equalsIgnoreCase("basicclaims") && player != null) {
//			PlayerData playerData = this.dataStore.getPlayerData(player.getUniqueId());
//			playerData.shovelMode = ShovelMode.Basic;
//			playerData.claimSubdividing = null;
//			GriefPrevention.sendMessage(player, TextMode.Success, Messages.BasicClaimsMode);
//
//			return true;
//		}
//
//		//subdivideclaims
//		else if (cmd.getName().equalsIgnoreCase("subdivideclaims") && player != null) {
//			PlayerData playerData = this.dataStore.getPlayerData(player.getUniqueId());
//			playerData.shovelMode = ShovelMode.Subdivide;
//			playerData.claimSubdividing = null;
//			GriefPrevention.sendMessage(player, TextMode.Instr, Messages.SubdivisionMode);
//			GriefPrevention.sendMessage(player, TextMode.Instr, Messages.SubdivisionVideo2, DataStore.SUBDIVISION_VIDEO_URL);
//
//			return true;
//		}
//
//		//deleteclaim
//		else if (cmd.getName().equalsIgnoreCase("deleteclaim") && player != null) {
//			//determine which claim the player is standing in
//			Claim claim = this.dataStore.getClaimAt(player.getLocation(), true /*ignore height*/, null);
//
//			if (claim == null) {
//				GriefPrevention.sendMessage(player, TextMode.Err, Messages.DeleteClaimMissing);
//			} else {
//				//deleting an admin claim additionally requires the adminclaims permission
//				if (!claim.isAdminClaim() || player.hasPermission("griefprevention.adminclaims")) {
//					PlayerData playerData = this.dataStore.getPlayerData(player.getUniqueId());
//					if (claim.children.size() > 0 && !playerData.warnedAboutMajorDeletion) {
//						GriefPrevention.sendMessage(player, TextMode.Warn, Messages.DeletionSubdivisionWarning);
//						playerData.warnedAboutMajorDeletion = true;
//					} else {
//						claim.removeSurfaceFluids(null);
//						this.dataStore.deleteClaim(claim, true, true);
//
//						//if in a creative mode world, /restorenature the claim
//						if (GriefPrevention.instance.creativeRulesApply(claim.getLesserBoundaryCorner()) || GriefPrevention.instance.config_claims_survivalAutoNatureRestoration) {
//							GriefPrevention.instance.restoreClaim(claim, 0);
//						}
//
//						GriefPrevention.sendMessage(player, TextMode.Success, Messages.DeleteSuccess);
//						ConsoleUtils.msg(player.getName() + " deleted " + claim.getOwnerName() + "'s claim at " + GriefPrevention.getfriendlyLocationString(claim.getLesserBoundaryCorner()), CustomLogEntryTypes.AdminActivity);
//
//						//revert any current visualization
//						Visualization.Revert(player);
//
//						playerData.warnedAboutMajorDeletion = false;
//					}
//				} else {
//					GriefPrevention.sendMessage(player, TextMode.Err, Messages.CantDeleteAdminClaim);
//				}
//			}
//
//			return true;
//		} else if (cmd.getName().equalsIgnoreCase("claimexplosions") && player != null) {
//			//determine which claim the player is standing in
//			Claim claim = this.dataStore.getClaimAt(player.getLocation(), true /*ignore height*/, null);
//
//			if (claim == null) {
//				GriefPrevention.sendMessage(player, TextMode.Err, Messages.DeleteClaimMissing);
//			} else {
//				String noBuildReason = claim.allowBuild(player, Material.STONE);
//				if (noBuildReason != null) {
//					GriefPrevention.sendMessage(player, TextMode.Err, noBuildReason);
//					return true;
//				}
//
//				if (claim.areExplosivesAllowed) {
//					claim.areExplosivesAllowed = false;
//					GriefPrevention.sendMessage(player, TextMode.Success, Messages.ExplosivesDisabled);
//				} else {
//					claim.areExplosivesAllowed = true;
//					GriefPrevention.sendMessage(player, TextMode.Success, Messages.ExplosivesEnabled);
//				}
//			}
//
//			return true;
//		}
//
//		//deleteallclaims <player>
//		else if (cmd.getName().equalsIgnoreCase("deleteallclaims")) {
//			//requires exactly one parameter, the other player's name
//			if (args.length != 1) return false;
//
//			//try to find that player
//			OfflinePlayer otherPlayer = this.resolvePlayerByName(args[0]);
//			if (otherPlayer == null) {
//				GriefPrevention.sendMessage(player, TextMode.Err, Messages.PlayerNotFound2);
//				return true;
//			}
//
//			//delete all that player's claims
//			this.dataStore.deleteClaimsForPlayer(otherPlayer.getUniqueId(), true);
//
//			GriefPrevention.sendMessage(player, TextMode.Success, Messages.DeleteAllSuccess, otherPlayer.getName());
//			if (player != null) {
//				ConsoleUtils.msg(player.getName() + " deleted all claims belonging to " + otherPlayer.getName() + ".", CustomLogEntryTypes.AdminActivity);
//
//				//revert any current visualization
//				Visualization.Revert(player);
//			}
//
//			return true;
//		} else if (cmd.getName().equalsIgnoreCase("deleteclaimsinworld")) {
//			//must be executed at the console
//			if (player != null) {
//				GriefPrevention.sendMessage(player, TextMode.Err, Messages.ConsoleOnlyCommand);
//				return true;
//			}
//
//			//requires exactly one parameter, the world name
//			if (args.length != 1) return false;
//
//			//try to find the specified world
//			World world = Bukkit.getServer().getWorld(args[0]);
//			if (world == null) {
//				GriefPrevention.sendMessage(player, TextMode.Err, Messages.WorldNotFound);
//				return true;
//			}
//
//			//delete all claims in that world
//			this.dataStore.deleteClaimsInWorld(world, true);
//			ConsoleUtils.msg("Deleted all claims in world: " + world.getName() + ".", CustomLogEntryTypes.AdminActivity);
//			return true;
//		} else if (cmd.getName().equalsIgnoreCase("deleteclaimsinworld")) {
//			//must be executed at the console
//			if (player != null) {
//				GriefPrevention.sendMessage(player, TextMode.Err, Messages.ConsoleOnlyCommand);
//				return true;
//			}
//
//			//requires exactly one parameter, the world name
//			if (args.length != 1)
//			    return false;
//
//			//try to find the specified world
//			World world = Bukkit.getServer().getWorld(args[0]);
//			if (world == null) {
//				GriefPrevention.sendMessage(player, TextMode.Err, Messages.WorldNotFound);
//				return true;
//			}
//
//			//delete all USER claims in that world
//			this.dataStore.deleteClaimsInWorld(world, false);
//			ConsoleUtils.msg("Deleted all user claims in world: " + world.getName() + ".", CustomLogEntryTypes.AdminActivity);
//			return true;
//		}
//
//		//claimslist or claimslist <player>
//		else if (cmd.getName().equalsIgnoreCase("claimslist")) {
//			//at most one parameter
//			if (args.length > 1) return false;
//
//			//player whose claims will be listed
//			OfflinePlayer otherPlayer;
//
//			//if another player isn't specified, assume current player
//			if (args.length < 1) {
//				if (player != null)
//					otherPlayer = player;
//				else
//					return false;
//			}
//
//			//otherwise if no permission to delve into another player's claims data
//			else if (player != null && !player.hasPermission("griefprevention.claimslistother")) {
//				GriefPrevention.sendMessage(player, TextMode.Err, Messages.ClaimsListNoPermission);
//				return true;
//			}
//
//			//otherwise try to find the specified player
//			else {
//				otherPlayer = this.resolvePlayerByName(args[0]);
//				if (otherPlayer == null) {
//					GriefPrevention.sendMessage(player, TextMode.Err, Messages.PlayerNotFound2);
//					return true;
//				}
//			}
//
//			//load the target player's data
//			PlayerData playerData = this.dataStore.getPlayerData(otherPlayer.getUniqueId());
//			Vector<Claim> claims = playerData.getClaims();
//			GriefPrevention.sendMessage(player, TextMode.Instr, Messages.StartBlockMath,
//					String.valueOf(playerData.getAccruedClaimBlocks()),
//					String.valueOf((playerData.getBonusClaimBlocks() + this.dataStore.getGroupBonusBlocks(otherPlayer.getUniqueId()))),
//					String.valueOf((playerData.getAccruedClaimBlocks() + playerData.getBonusClaimBlocks() + this.dataStore.getGroupBonusBlocks(otherPlayer.getUniqueId()))));
//			if (claims.size() > 0) {
//				GriefPrevention.sendMessage(player, TextMode.Instr, Messages.ClaimsListHeader);
//				for (int i = 0; i < playerData.getClaims().size(); i++) {
//					Claim claim = playerData.getClaims().get(i);
//					GriefPrevention.sendMessage(player, TextMode.Instr, getfriendlyLocationString(claim.getLesserBoundaryCorner()) + this.dataStore.getMessage(Messages.ContinueBlockMath, String.valueOf(claim.getArea())));
//				}
//
//				GriefPrevention.sendMessage(player, TextMode.Instr, Messages.EndBlockMath, String.valueOf(playerData.getRemainingClaimBlocks()));
//			}
//
//			//drop the data we just loaded, if the player isn't online
//			if (!otherPlayer.isOnline())
//				this.dataStore.clearCachedPlayerData(otherPlayer.getUniqueId());
//
//			return true;
//		}
//
//		//adminclaimslist
//		else if (cmd.getName().equalsIgnoreCase("adminclaimslist")) {
//			//find admin claims
//			Vector<Claim> claims = new Vector<>();
//			for (Claim claim : this.dataStore.claims) {
//				if (claim.ownerID == null)  //admin claim
//				{
//					claims.add(claim);
//				}
//			}
//			if (claims.size() > 0) {
//				GriefPrevention.sendMessage(player, TextMode.Instr, Messages.ClaimsListHeader);
//				for (int i = 0; i < claims.size(); i++) {
//					Claim claim = claims.get(i);
//					GriefPrevention.sendMessage(player, TextMode.Instr, getfriendlyLocationString(claim.getLesserBoundaryCorner()));
//				}
//			}
//
//			return true;
//		}
//
//		//unlockItems
//		else if (cmd.getName().equalsIgnoreCase("unlockdrops") && player != null) {
//			PlayerData playerData;
//
//			if (player.hasPermission("griefprevention.unlockothersdrops") && args.length == 1) {
//				Player otherPlayer = Bukkit.getPlayer(args[0]);
//				if (otherPlayer == null) {
//					GriefPrevention.sendMessage(player, TextMode.Err, Messages.PlayerNotFound2);
//					return true;
//				}
//
//				playerData = this.dataStore.getPlayerData(otherPlayer.getUniqueId());
//				GriefPrevention.sendMessage(player, TextMode.Success, Messages.DropUnlockOthersConfirmation, otherPlayer.getName());
//			} else {
//				playerData = this.dataStore.getPlayerData(player.getUniqueId());
//				GriefPrevention.sendMessage(player, TextMode.Success, Messages.DropUnlockConfirmation);
//			}
//
//			playerData.dropsAreUnlocked = true;
//
//			return true;
//		}
//
//		//deletealladminclaims
//		else if (player != null && cmd.getName().equalsIgnoreCase("deletealladminclaims")) {
//			if (!player.hasPermission("griefprevention.deleteclaims")) {
//				GriefPrevention.sendMessage(player, TextMode.Err, Messages.NoDeletePermission);
//				return true;
//			}
//
//			//delete all admin claims
//			this.dataStore.deleteClaimsForPlayer(null, true);  //null for owner id indicates an administrative claim
//
//			GriefPrevention.sendMessage(player, TextMode.Success, Messages.AllAdminDeleted);
//			if (player != null) {
//				ConsoleUtils.msg(player.getName() + " deleted all administrative claims.", CustomLogEntryTypes.AdminActivity);
//
//				//revert any current visualization
//				Visualization.Revert(player);
//			}
//
//			return true;
//		}
//
//		//adjustbonusclaimblocks <player> <amount> or [<permission>] amount
//		else if (cmd.getName().equalsIgnoreCase("adjustbonusclaimblocks")) {
//			//requires exactly two parameters, the other player or group's name and the adjustment
//			if (args.length != 2) return false;
//
//			//parse the adjustment amount
//			int adjustment;
//			try {
//				adjustment = Integer.parseInt(args[1]);
//			} catch (NumberFormatException numberFormatException) {
//				return false;  //causes usage to be displayed
//			}
//
//			//if granting blocks to all players with a specific permission
//			if (args[0].startsWith("[") && args[0].endsWith("]")) {
//				String permissionIdentifier = args[0].substring(1, args[0].length() - 1);
//				int newTotal = this.dataStore.adjustGroupBonusBlocks(permissionIdentifier, adjustment);
//
//				GriefPrevention.sendMessage(player, TextMode.Success, Messages.AdjustGroupBlocksSuccess, permissionIdentifier, String.valueOf(adjustment), String.valueOf(newTotal));
//				if (player != null)
//					ConsoleUtils.msg(player.getName() + " adjusted " + permissionIdentifier + "'s bonus claim blocks by " + adjustment + ".");
//
//				return true;
//			}
//
//			//otherwise, find the specified player
//			OfflinePlayer targetPlayer;
//			try {
//				UUID playerID = UUID.fromString(args[0]);
//				targetPlayer = this.getServer().getOfflinePlayer(playerID);
//
//			} catch (IllegalArgumentException e) {
//				targetPlayer = this.resolvePlayerByName(args[0]);
//			}
//
//			if (targetPlayer == null) {
//				GriefPrevention.sendMessage(player, TextMode.Err, Messages.PlayerNotFound2);
//				return true;
//			}
//
//			//give blocks to player
//			PlayerData playerData = this.dataStore.getPlayerData(targetPlayer.getUniqueId());
//			playerData.setBonusClaimBlocks(playerData.getBonusClaimBlocks() + adjustment);
//			this.dataStore.savePlayerData(targetPlayer.getUniqueId(), playerData);
//
//			GriefPrevention.sendMessage(player, TextMode.Success, Messages.AdjustBlocksSuccess, targetPlayer.getName(), String.valueOf(adjustment), String.valueOf(playerData.getBonusClaimBlocks()));
//			if (player != null)
//				ConsoleUtils.msg(player.getName() + " adjusted " + targetPlayer.getName() + "'s bonus claim blocks by " + adjustment + ".", CustomLogEntryTypes.AdminActivity);
//
//			return true;
//		}
//
//		//adjustbonusclaimblocksall <amount>
//		else if (cmd.getName().equalsIgnoreCase("adjustbonusclaimblocksall")) {
//			//requires exactly one parameter, the amount of adjustment
//			if (args.length != 1) return false;
//
//			//parse the adjustment amount
//			int adjustment;
//			try {
//				adjustment = Integer.parseInt(args[0]);
//			} catch (NumberFormatException numberFormatException) {
//				return false;  //causes usage to be displayed
//			}
//
//			//for each online player
//			@SuppressWarnings("unchecked")
//			Collection<Player> players = (Collection<Player>) this.getServer().getOnlinePlayers();
//			StringBuilder builder = new StringBuilder();
//			for (Player onlinePlayer : players) {
//				UUID playerID = onlinePlayer.getUniqueId();
//				PlayerData playerData = this.dataStore.getPlayerData(playerID);
//				playerData.setBonusClaimBlocks(playerData.getBonusClaimBlocks() + adjustment);
//				this.dataStore.savePlayerData(playerID, playerData);
//				builder.append(onlinePlayer.getName() + " ");
//			}
//
//			GriefPrevention.sendMessage(player, TextMode.Success, Messages.AdjustBlocksAllSuccess, String.valueOf(adjustment));
//			ConsoleUtils.msg("Adjusted all " + players.size() + "players' bonus claim blocks by " + adjustment + ".  " + builder.toString(), CustomLogEntryTypes.AdminActivity);
//
//			return true;
//		}
//
//		//trapped
//		else if (cmd.getName().equalsIgnoreCase("trapped") && player != null) {
//			//FEATURE: empower players who get "stuck" in an area where they don't have permission to build to save themselves
//
//			PlayerData playerData = this.dataStore.getPlayerData(player.getUniqueId());
//			Claim claim = this.dataStore.getClaimAt(player.getLocation(), false, playerData.lastClaim);
//
//			//if another /trapped is pending, ignore this slash command
//			if (playerData.pendingTrapped) {
//				return true;
//			}
//
//			//if the player isn't in a claim or has permission to build, tell him to man up
//			if (claim == null || claim.allowBuild(player, Material.AIR) == null) {
//				GriefPrevention.sendMessage(player, TextMode.Err, Messages.NotTrappedHere);
//				return true;
//			}
//
//			//rescue destination may be set by GPFlags or other plugin, ask to find out
//			SaveTrappedPlayerEvent event = new SaveTrappedPlayerEvent(claim);
//			Bukkit.getPluginManager().callEvent(event);
//
//			//if the player is in the nether or end, he's screwed (there's no way to programmatically find a safe place for him)
//			if (player.getWorld().getEnvironment() != Environment.NORMAL) {
//				GriefPrevention.sendMessage(player, TextMode.Err, Messages.TrappedWontWorkHere);
//				return true;
//			}
//
//			//send instructions
//			GriefPrevention.sendMessage(player, TextMode.Instr, Messages.RescuePending);
//
//			//create a task to rescue this player in a little while
//			PlayerRescueTask task = new PlayerRescueTask(player, player.getLocation(), event.getDestination());
//			this.getServer().getScheduler().scheduleSyncDelayedTask(this, task, 200L);  //20L ~ 1 second
//
//			return true;
//		}
//
//		//givepet
//		else if (cmd.getName().equalsIgnoreCase("givepet") && player != null) {
//			//requires one parameter
//			if (args.length < 1) return false;
//
//			PlayerData playerData = this.dataStore.getPlayerData(player.getUniqueId());
//
//			//special case: cancellation
//			if (args[0].equalsIgnoreCase("cancel")) {
//				playerData.petGiveawayRecipient = null;
//				GriefPrevention.sendMessage(player, TextMode.Success, Messages.PetTransferCancellation);
//				return true;
//			}
//
//			//find the specified player
//			OfflinePlayer targetPlayer = this.resolvePlayerByName(args[0]);
//			if (targetPlayer == null) {
//				GriefPrevention.sendMessage(player, TextMode.Err, Messages.PlayerNotFound2);
//				return true;
//			}
//
//			//remember the player's ID for later pet transfer
//			playerData.petGiveawayRecipient = targetPlayer;
//
//			//send instructions
//			GriefPrevention.sendMessage(player, TextMode.Instr, Messages.ReadyToTransferPet);
//
//			return true;
//		}
//
//		return false;
//	}
//
//	//helper method keeps the trust commands consistent and eliminates duplicate code
//	private void handleTrustCommand(Player player, ClaimPermission permissionLevel, String recipientName) {
//		//determine which claim the player is standing in
//		Claim claim = this.dataStore.getClaimAt(player.getLocation(), true /*ignore height*/, null);
//
//		//validate player or group argument
//		String permission = null;
//		OfflinePlayer otherPlayer;
//		UUID recipientID = null;
//
//		if (recipientName.startsWith("[") && recipientName.endsWith("]")) {
//			permission = recipientName.substring(1, recipientName.length() - 1);
//			if (permission == null || permission.isEmpty()) {
//				GriefPrevention.sendMessage(player, TextMode.Err, Messages.InvalidPermissionID);
//				return;
//			}
//		} else if (recipientName.contains(".")) {
//			permission = recipientName;
//		} else {
//			otherPlayer = this.resolvePlayerByName(recipientName);
//			if (otherPlayer == null && !recipientName.equals("public") && !recipientName.equals("all")) {
//				GriefPrevention.sendMessage(player, TextMode.Err, Messages.PlayerNotFound2);
//				return;
//			}
//
//			if (otherPlayer != null) {
//				recipientName = otherPlayer.getName();
//				recipientID = otherPlayer.getUniqueId();
//			} else {
//				recipientName = "public";
//			}
//		}
//
//		//determine which claims should be modified
//		ArrayList<Claim> targetClaims = new ArrayList<>();
//		if (claim == null) {
//			PlayerData playerData = this.dataStore.getPlayerData(player.getUniqueId());
//			for (int i = 0; i < playerData.getClaims().size(); i++) {
//				targetClaims.add(playerData.getClaims().get(i));
//			}
//		} else {
//			//check permission here
//			if (claim.allowGrantPermission(player) != null) {
//				GriefPrevention.sendMessage(player, TextMode.Err, Messages.NoPermissionTrust, claim.getOwnerName());
//				return;
//			}
//
//			//see if the player has the level of permission he's trying to grant
//			String errorMessage;
//
//			//permission level null indicates granting permission trust
//			if (permissionLevel == null) {
//				errorMessage = claim.allowEdit(player);
//				if (errorMessage != null) {
//					errorMessage = "Only " + claim.getOwnerName() + " can grant /PermissionTrust here.";
//				}
//			}
//
//			//otherwise just use the ClaimPermission enum values
//			else {
//				switch (permissionLevel) {
//					case Access:
//						errorMessage = claim.allowAccess(player);
//						break;
//					case Inventory:
//						errorMessage = claim.allowContainers(player);
//						break;
//					default:
//						errorMessage = claim.allowBuild(player, Material.AIR);
//				}
//			}
//
//			//error message for trying to grant a permission the player doesn't have
//			if (errorMessage != null) {
//				GriefPrevention.sendMessage(player, TextMode.Err, Messages.CantGrantThatPermission);
//				return;
//			}
//
//			targetClaims.add(claim);
//		}
//
//		//if we didn't determine which claims to modify, tell the player to be specific
//		if (targetClaims.size() == 0) {
//			GriefPrevention.sendMessage(player, TextMode.Err, Messages.GrantPermissionNoClaim);
//			return;
//		}
//
//		//apply changes
//		for (int i = 0; i < targetClaims.size(); i++) {
//			Claim currentClaim = targetClaims.get(i);
//			String identifierToAdd = recipientName;
//			if (permission != null) {
//				identifierToAdd = "[" + permission + "]";
//			} else if (recipientID != null) {
//				identifierToAdd = recipientID.toString();
//			}
//
//			if (permissionLevel == null) {
//				if (!currentClaim.managers.contains(identifierToAdd)) {
//					currentClaim.managers.add(identifierToAdd);
//				}
//			} else {
//				currentClaim.setPermission(identifierToAdd, permissionLevel);
//			}
//			this.dataStore.saveClaim(currentClaim);
//		}
//
//		//notify player
//		if (recipientName.equals("public")) recipientName = this.dataStore.getMessage(Messages.CollectivePublic);
//		String permissionDescription;
//		if (permissionLevel == null) {
//			permissionDescription = this.dataStore.getMessage(Messages.PermissionsPermission);
//		} else if (permissionLevel == ClaimPermission.Build) {
//			permissionDescription = this.dataStore.getMessage(Messages.BuildPermission);
//		} else if (permissionLevel == ClaimPermission.Access) {
//			permissionDescription = this.dataStore.getMessage(Messages.AccessPermission);
//		} else //ClaimPermission.Inventory
//		{
//			permissionDescription = this.dataStore.getMessage(Messages.ContainersPermission);
//		}
//
//		String location;
//		if (claim == null) {
//			location = this.dataStore.getMessage(Messages.LocationAllClaims);
//		} else {
//			location = this.dataStore.getMessage(Messages.LocationCurrentClaim);
//		}
//
//		GriefPrevention.sendMessage(player, TextMode.Success, Messages.GrantPermissionConfirmation, recipientName, permissionDescription, location);
//	}
//
//	public int getSeaLevel(World world) {
//		Integer overrideValue = this.config_seaLevelOverride.get(world.getName());
//
//		if (overrideValue == null || overrideValue == -1) {
//			return world.getSeaLevel();
//		} else {
//			return overrideValue;
//		}
//	}
//
//	public ItemStack getItemInHand(Player player, EquipmentSlot hand) {
//		if (hand == EquipmentSlot.OFF_HAND)
//		    return player.getInventory().getItemInOffHand();
//
//		return player.getInventory().getItemInMainHand();
//	}
}
