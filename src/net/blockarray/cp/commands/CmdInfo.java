package net.blockarray.cp.commands;

import java.sql.SQLException;

import net.blockarray.cp.util.Permissions;
import net.blockarray.cp.util.Util;
import net.blockarray.cp.wrappers.ChunkData;

import org.bukkit.Chunk;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdInfo implements CommandExecutor
{

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if (!sender.hasPermission(Permissions.PERM_INFO))
			return false;

		if (!(sender instanceof Player))
			return false;

		Player player = (Player) sender;
		Chunk chunk = player.getLocation().getChunk();

		ChunkData cd = new ChunkData(chunk);
		if (!cd.isProtected())
		{
			Util.sendMessage("&cThat chunk hasn't been claimed (yet)!", sender.getName());
			return true;
		}
		try
		{
			String owner = cd.getOwner();
			String residents = Util.getResidentsAsString(cd.getResidents());
			String coords = cd.getCoords();

			Util.sendMessage("&7Owner: &6" + owner, player.getName());
			Util.sendMessage("&7Residents: " + residents, player.getName());
			Util.sendMessage("&7Coords: &6" + coords, player.getName());
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}

		return false;
	}

}
