package net.blockarray.cp.commands;

import java.sql.SQLException;

import net.blockarray.cp.util.Permissions;
import net.blockarray.cp.util.Util;
import net.blockarray.cp.wrappers.ChunkData;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdRemoveRes implements CommandExecutor
{

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if (!(sender instanceof Player))
			return false;
		Player player = (Player) sender;

		if (!player.hasPermission(Permissions.PERM_CLAIM))
			return false;
		if (args.length < 1)
		{
			Util.sendMessage("&cInvalid Syntax. Usage: /cpremove <username>", sender.getName());
			return false;
		}

		ChunkData cd = new ChunkData(player.getLocation().getChunk());

		if (cd.isProtected() && cd.getOwner().equalsIgnoreCase(player.getName()) || player.hasPermission(Permissions.PERM_ADMIN))
		{

			if (args[0].equalsIgnoreCase(player.getName()))
			{
				Util.sendMessage("&cYou can't remove yourself, fool! If you want to remove this protection, use /cpdelete instead.",
						player.getName());
				return true;
			}

			try
			{
				if (!cd.getResidents().contains(args[0].toUpperCase()))
				{
					Util.sendMessage("&cError: That user is not a resident!", player.getName());
					return false;
				}
			}
			catch (SQLException e1)
			{
				e1.printStackTrace();
			}

			cd.removeResident(args[0]);
			Util.sendMessage("&aSucess, removed " + args[0] + " from the residencies.", player.getName());
			return true;
		}
		else
		{
			Util.sendMessage("&cError: Plot doesn't belong to you, or is not claimed!", player.getName());

			return false;
		}

	}
}
