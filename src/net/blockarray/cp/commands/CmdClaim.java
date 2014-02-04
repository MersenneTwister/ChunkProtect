package net.blockarray.cp.commands;

import java.sql.SQLException;

import net.blockarray.cp.CP;
import net.blockarray.cp.util.Permissions;
import net.blockarray.cp.util.Util;
import net.blockarray.cp.wrappers.ChunkData;
import net.blockarray.cp.wrappers.PlayerData;

import org.bukkit.Chunk;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdClaim extends Permissions implements CommandExecutor
{

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if (!sender.hasPermission(PERM_CLAIM))
		{
			Util.sendMessage("&cYou don't have permission to claim chunks!", sender.getName());
			return true;
		}
		if (!(sender instanceof Player))
			return false;
		
		
		Player player = (Player)sender;

		if(!CP.getWGInstance().canBuild(player, player.getLocation()))
		{
			Util.sendMessage("&cError: This region is protected by WorldGuard.", player.getName());
			return false;
		}
		
		PlayerData pd = new PlayerData((Player) sender);
		Chunk chunk = pd.getPlayer().getLocation().getChunk();

		ChunkData cd = new ChunkData(chunk);

		try
		{

			if (!pd.canCreateProtection())
			{
				pd.sendMessage("&cYou can't create anymore protections!");
				return false;
			}
			else
			{
				if (cd.isProtected())
				{
					pd.sendMessage("&cThat chunk is already protected! Contact " + cd.getOwner() + " if you wish to build there.");
					return false;
				}
				else
				{
					if (cd.addProtection(pd.getPlayer().getName()))
					{
						int chunksLeft = pd.maxChunks() - Integer.valueOf(pd.chunksOwned());
						pd.sendMessage("&aSuccess, chunk protected! You can protect " + chunksLeft + " more chunk(s)!");
						return true;
					}
					return true;
				}
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return false;
		}
	}
}
