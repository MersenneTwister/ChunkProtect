package net.blockarray.cp.wrappers;

import java.sql.SQLException;

import net.blockarray.cp.CP;
import net.blockarray.cp.util.Permissions;
import net.blockarray.cp.util.Util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class PlayerData
{
	private Player player;

	public PlayerData(Player player)
	{
		this.player = player;
	}

	public Player getPlayer()
	{
		return player;
	}

	public String chunksOwned() throws SQLException
	{
		return CP.getNetwork().getPlayerData(player.getName()).getString("totalChunks");
	}

	public void sendMessage(String message)
	{
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
	}

	public int maxChunks()
	{
		int maxChunks = 0;

		if (player.hasPermission(Permissions.PERM_ADMIN))
		{
			maxChunks = 999999;
		}
		else
		{
			maxChunks = Util.getMaxChunks(player);
		}
		return maxChunks;
	}

	public boolean canCreateProtection()
	{
		try
		{
			if (Integer.valueOf(chunksOwned()) < maxChunks())
				return true;
			return false;
		}
		catch (NumberFormatException | SQLException e)
		{
			Bukkit.getServer().broadcastMessage(e.getMessage());
			return false;
		}
	}
}
