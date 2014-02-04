package net.blockarray.cp.commands;

import net.blockarray.cp.util.Util;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CmdHelp implements CommandExecutor
{

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{

		Util.sendMessage("&7**********&6Commands&7**********", sender.getName());
		sender.sendMessage("");
		Util.sendMessage("&6/cpclaim&7: Claims the chunk you're standing on.", sender.getName());
		Util.sendMessage("&6/cpdelete&7: Deletes the protection of the chunk you're standing on.", sender.getName());

		sender.sendMessage("");

		Util.sendMessage("&6/cpadd&7: Add a user to the protection of the chunk you're standing on.", sender.getName());
		Util.sendMessage("&6/cpremove&7: Remove a user from the protection you're standing on.", sender.getName());

		sender.sendMessage("");

		Util.sendMessage("&6/cphelp&7: Runs this command.", sender.getName());

		return false;
	}

}
