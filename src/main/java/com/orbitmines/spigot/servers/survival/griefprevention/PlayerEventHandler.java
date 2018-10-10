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

import org.bukkit.event.Listener;

class PlayerEventHandler implements Listener {

//
//	static Block getTargetBlock(Player player, int maxDistance) throws IllegalStateException {
//		Location eye = player.getEyeLocation();
//		Material eyeMaterial = eye.getBlock().getType();
//		boolean passThroughWater = (eyeMaterial == Material.WATER || eyeMaterial == Material.STATIONARY_WATER);
//		BlockIterator iterator = new BlockIterator(player.getLocation(), player.getEyeHeight(), maxDistance);
//		Block result = player.getLocation().getBlock().getRelative(BlockFace.UP);
//		while (iterator.hasNext()) {
//			result = iterator.next();
//			Material graphType = result.getType();
//			if (graphType != Material.AIR &&
//					(!passThroughWater || graphType != Material.STATIONARY_WATER) &&
//					(!passThroughWater || graphType != Material.WATER) &&
//					graphType != Material.LONG_GRASS &&
//					graphType != Material.SNOW) return result;
//		}
//
//		return result;
//	}
}
