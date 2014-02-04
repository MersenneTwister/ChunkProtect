package net.blockarray.cp.listeners;

import java.sql.SQLException;

import net.blockarray.cp.util.Permissions;
import net.blockarray.cp.wrappers.ChunkData;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class MobDamage implements Listener
{
	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.NORMAL)
	public void onMobDamage(EntityDamageByEntityEvent event)
	{
		if (event.isCancelled())
			return;

		Entity damagee = event.getEntity();
		ChunkData cd = new ChunkData(damagee.getLocation().getChunk());

		if (event.getDamager() instanceof Arrow)
		{
			Arrow damager = (Arrow) event.getDamager();

			if (damager.getShooter() instanceof Player)
			{

				Player player = (Player) damager.getShooter();
				if (player.hasPermission(Permissions.PERM_ADMIN))
					return;
				try
				{
					if (cd.isProtected() && !cd.getOwner().equalsIgnoreCase(player.getName())
							&& !cd.getResidents().contains(player.getName().toUpperCase()))
					{
						event.setCancelled(true);
						return;
					}
				}
				catch (SQLException e)
				{
					e.printStackTrace();
				}
			}
		}

		if (event.getDamager() instanceof Player)
		{

			Player player = (Player) event.getDamager();

			if (player.hasPermission(Permissions.PERM_ADMIN))
				return;

			try
			{
				if (cd.isProtected() && !cd.getOwner().equalsIgnoreCase(player.getName())
						&& !cd.getResidents().contains(player.getName().toUpperCase()))
				{
					event.setCancelled(true);
				}
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
	}
}
