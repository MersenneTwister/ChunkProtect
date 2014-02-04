package net.blockarray.cp.util;

import java.util.ArrayList;

import net.blockarray.cp.CP;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Util
{
	public static void sendMessage(String message, String username)
	{
		Player player = Bukkit.getPlayer(username);
		if (player == null)
			return;
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
	}

	public static int getMaxChunks(Player player)
	{
		int max = 0;
		for (int i = 0; i < CP.getValues().getMAX_CHUNKS_POSSIBLE(); i++)
		{
			if (player.hasPermission("cp.max." + i))
			{
				max = i;
			}
		}
		return max;
	}

	public static String getResidentsAsString(ArrayList<String> residents)
	{

		String message = "";

		for (int i = 0; i < residents.size(); i++)
			if (i >= residents.size())
				message += "&6" + residents.get(i) + "&8";
			else
				message += "&6" + residents.get(i) + "&8, ";
		return message;
	}
}
