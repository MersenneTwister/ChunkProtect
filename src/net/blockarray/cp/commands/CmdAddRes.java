package net.blockarray.cp.commands;

import java.sql.SQLException;

import net.blockarray.cp.util.Permissions;
import net.blockarray.cp.util.Util;
import net.blockarray.cp.wrappers.ChunkData;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdAddRes implements CommandExecutor
{

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if (!sender.hasPermission(Permissions.PERM_ADD_RES))
			return false;

		if (args.length < 1)
		{
			Util.sendMessage("&cInvalid Syntax. Usage: /cpadd <username>", sender.getName());
			return false;
		}

		Player player = (Player) sender;

		ChunkData cd = new ChunkData(player.getLocation().getChunk());
		
		
		if (cd.isProtected() && cd.getOwner().equalsIgnoreCase(player.getName()) || player.hasPermission(Permissions.PERM_ADMIN))
		{
			try
			{
				if (cd.getResidents().contains(args[0]))
				{
					Util.sendMessage("&cError: That user is already added!", player.getName());
					return false;
				}
			}
			catch (SQLException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			try
			{
				cd.addResident(args[0]);
				Util.sendMessage("&aSucess, added " + args[0] + " to residents.", player.getName());
				return true;
			}
			catch (SQLException e)
			{
				e.printStackTrace();
				return false;
			}
		}
		else
		{
			Util.sendMessage("&cError: Plot doesn't belong to you, or is not claimed!", player.getName());

			return false;
		}
	}
}
