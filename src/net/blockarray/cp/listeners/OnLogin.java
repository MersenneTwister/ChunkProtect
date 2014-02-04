package net.blockarray.cp.listeners;

import net.blockarray.cp.CP;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class OnLogin implements Listener
{
	@EventHandler(priority=EventPriority.HIGHEST)
	public void onLogin(PlayerJoinEvent event)
	{
		CP.getNetwork().createUserData(event.getPlayer().getName());
	}
}
