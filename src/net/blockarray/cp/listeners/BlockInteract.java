package net.blockarray.cp.listeners;

import java.sql.SQLException;

import net.blockarray.cp.CP;
import net.blockarray.cp.util.Permissions;
import net.blockarray.cp.util.Util;
import net.blockarray.cp.wrappers.ChunkData;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockInteract implements Listener
{
	@EventHandler(priority = EventPriority.HIGHEST)
	public void blockInteract(BlockBreakEvent event)
	{
		Player player = event.getPlayer();

		if (player.hasPermission(Permissions.PERM_ADMIN))
			return;

		String username = player.getName();

		Chunk chunk = event.getBlock().getChunk();

		if (!CP.getWGInstance().canBuild(player, event.getBlock().getLocation()))
			return;

		ChunkData cd = new ChunkData(chunk);
		try
		{
			if (cd.isProtected() && !cd.getOwner().equalsIgnoreCase(username) && !cd.getResidents().contains(username.toUpperCase()))
			{
				event.setCancelled(true);
				Util.sendMessage(ChatColor.RED + "That chunk is protected, contact the owner: &6" + cd.getOwner()
						+ "&c if you wish to join.", username);
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return;
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void blockPlace(BlockPlaceEvent event)
	{
		Player player = event.getPlayer();

		if (player.hasPermission(Permissions.PERM_ADMIN))
			return;

		String username = player.getName();

		Chunk chunk = event.getBlock().getChunk();
		if (!CP.getWGInstance().canBuild(player, event.getBlock().getLocation()))
			return;
		ChunkData cd = new ChunkData(chunk);
		try
		{
			if (cd.isProtected() && !cd.getOwner().equalsIgnoreCase(username) && !cd.getResidents().contains(username.toUpperCase()))
			{
				event.setCancelled(true);
				Util.sendMessage(
						ChatColor.RED + "That chunk is protected, contact the owner: &6" + cd.getOwner() + " if you wish to join.",
						username);
			}
			else
			{}
		}
		catch (SQLException e)
		{
			return;
		}
	}

}
