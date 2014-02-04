package net.blockarray.cp;

import java.util.ArrayList;
import java.util.logging.Level;

import net.blockarray.cp.Config.Values;
import net.blockarray.cp.commands.CmdAddRes;
import net.blockarray.cp.commands.CmdClaim;
import net.blockarray.cp.commands.CmdDelProtection;
import net.blockarray.cp.commands.CmdHelp;
import net.blockarray.cp.commands.CmdInfo;
import net.blockarray.cp.commands.CmdRemoveRes;
import net.blockarray.cp.listeners.BlockInteract;
import net.blockarray.cp.listeners.MobDamage;
import net.blockarray.cp.listeners.OnLogin;
import net.blockarray.cp.network.Network;
import net.blockarray.cp.wrappers.ChunkData;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class CP extends JavaPlugin
{
	private static Values values;
	private static Network network;
	private static WorldGuardPlugin WG;

	public static ArrayList<ChunkData> chunks = new ArrayList<ChunkData>();

	public void onEnable()
	{
		values = new Values(this);
		network = new Network();
		network.connect();
		network.init();

		Bukkit.getServer().getLogger().log(Level.INFO, "Loading chunks...");
		chunks = network.getProtectedChunks(values.getPROTECTED_WORLD());
		Bukkit.getServer().getLogger().log(Level.INFO, "Done");

		getCommand("cpclaim").setExecutor(new CmdClaim());
		getCommand("cpinfo").setExecutor(new CmdInfo());
		getCommand("cpadd").setExecutor(new CmdAddRes());
		getCommand("cpremove").setExecutor(new CmdRemoveRes());
		getCommand("cpdelete").setExecutor(new CmdDelProtection());
		getCommand("cphelp").setExecutor(new CmdHelp());

		WG = getWorldGuard();

		Bukkit.getServer().getPluginManager().registerEvents(new BlockInteract(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new OnLogin(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new MobDamage(), this);
	}

	public void onDisable()
	{}

	public static Values getValues()
	{
		return values;
	}

	public static Network getNetwork()
	{
		return network;
	}

	public static WorldGuardPlugin getWGInstance()
	{
		return WG;
	}

	private WorldGuardPlugin getWorldGuard()
	{
		Plugin plugin = getServer().getPluginManager().getPlugin("WorldGuard");

		// WorldGuard may not be loaded
		if (plugin == null || !(plugin instanceof WorldGuardPlugin))
		{
			return null;
		}

		return (WorldGuardPlugin) plugin;
	}

}
