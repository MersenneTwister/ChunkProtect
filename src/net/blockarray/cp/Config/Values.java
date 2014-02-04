package net.blockarray.cp.Config;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;

public class Values
{

	private static String DATABASE;
	private static String USERNAME;
	private static String PASSWORD;
	private static String HOST;
	private static int MAX_CHUNKS_POSSIBLE;
	private String PROTECTED_WORLD;

	@SuppressWarnings("unused")
	private Plugin plugin;

	public Values(Plugin plugin)
	{
		this.plugin = plugin;

		plugin.getConfig().options().copyDefaults(true);
		plugin.getConfig().addDefault("mysql.host", "localhost");
		plugin.getConfig().addDefault("mysql.database", "chunkprotect");
		plugin.getConfig().addDefault("mysql.username", "root");
		plugin.getConfig().addDefault("mysql.password", "password");
		plugin.getConfig().addDefault("protected.world", "world");
		plugin.getConfig().addDefault("max.chunks.possible", 200);
		plugin.saveConfig();

		DATABASE = plugin.getConfig().getString("mysql.database");
		USERNAME = plugin.getConfig().getString("mysql.username");
		PASSWORD = plugin.getConfig().getString("mysql.password");
		HOST = plugin.getConfig().getString("mysql.host");
		MAX_CHUNKS_POSSIBLE = plugin.getConfig().getInt("max.chunks.possible");
		PROTECTED_WORLD = plugin.getConfig().getString("protected.world");
	}

	public String getDATABASE()
	{
		return DATABASE;
	}

	public String getUSERNAME()
	{
		return USERNAME;
	}

	public String getPASSWORD()
	{
		return PASSWORD;
	}

	public String getHOST()
	{
		return HOST;
	}

	public int getMAX_CHUNKS_POSSIBLE()
	{
		return MAX_CHUNKS_POSSIBLE;
	}

	public World getPROTECTED_WORLD()
	{
		return Bukkit.getWorld(PROTECTED_WORLD);
	}

}
