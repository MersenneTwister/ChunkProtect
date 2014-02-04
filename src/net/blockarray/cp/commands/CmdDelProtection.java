package net.blockarray.cp.commands;

import net.blockarray.cp.util.Permissions;
import net.blockarray.cp.util.Util;
import net.blockarray.cp.wrappers.ChunkData;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdDelProtection implements CommandExecutor
{
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if (!(sender instanceof Player))
			return false;
		if (!sender.hasPermission(Permissions.PERM_CLAIM))
		{
			Util.sendMessage("&cYou don't have permission!", sender.getName());
			return true;
		}

		Player player = (Player) sender;
		ChunkData cd = new ChunkData(player.getLocation().getChunk());

		if (!player.hasPermission(Permissions.PERM_ADMIN))
		{
			if (!cd.isProtected())
			{
				Util.sendMessage("&cError: That chunk isn't protected!", player.getName());
				return true;
			}
			if (!cd.getOwner().equalsIgnoreCase(player.getName()))
			{
				Util.sendMessage("&cError: That protection doesn't belong to you!", player.getName());
				return true;
			}
			Util.sendMessage("&aProtection Deleted.", cd.getOwner());
			cd.removeProtection();
		}
		else
		{
			if (!cd.isProtected())
			{
				Util.sendMessage("&cError: That chunk isn't protected!", player.getName());
				return true;
			}
			Util.sendMessage("&a" + player.getName() + " removed your protection. At the coords " + cd.getCoords(), cd.getOwner());
			Util.sendMessage("&aProtection Deleted.", player.getName());
			cd.removeProtection();
		}

		return false;
	}
}
