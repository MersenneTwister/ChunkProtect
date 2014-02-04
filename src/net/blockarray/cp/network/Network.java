package net.blockarray.cp.network;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import net.blockarray.cp.CP;
import net.blockarray.cp.wrappers.ChunkData;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class Network
{
	private static Connection c;
	private static String HOST;
	private static String DATABASE;
	private static String USERNAME;
	private static String PASSWORD;

	private String url;

	public Network()
	{
		HOST = CP.getValues().getHOST();
		DATABASE = CP.getValues().getDATABASE();
		USERNAME = CP.getValues().getUSERNAME();
		PASSWORD = CP.getValues().getPASSWORD();
		url = "jdbc:mysql://" + HOST + ":3306/";
	}

	public void init()
	{
		try
		{
			Statement s = c.createStatement();
			s.executeUpdate("CREATE TABLE IF NOT EXISTS users (username VARCHAR(256), totalChunks VARCHAR(256))");
			s.executeUpdate("CREATE TABLE IF NOT EXISTS chunks (owner VARCHAR(256), residents VARCHAR(256), x VARCHAR(256), y VARCHAR(256), z VARCHAR(256))");
		}
		catch (SQLException e)
		{
			System.err.println(e.getMessage());
		}
	}

	public void connect()
	{
		try
		{
			c = DriverManager.getConnection(url + DATABASE, USERNAME, PASSWORD);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	public Connection getConnection()
	{
		return c;
	}

	public Chunk getChunk(Location loc, Player player)
	{
		String query = "SELECT * FROM chunks WHERE x = ? AND z = ?";

		try
		{
			PreparedStatement ps = c.prepareStatement(query);
			ps.setInt(1, (int) loc.getX());
			ps.setInt(2, (int) loc.getZ());

			ResultSet res = ps.executeQuery();

			int x = Integer.valueOf(res.getString("x"));
			int z = Integer.valueOf(res.getString("z"));

			Chunk chunk = player.getWorld().getChunkAt(x, z);

			return chunk;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			Bukkit.getServer().broadcastMessage("Chunk data error " + e.getMessage());
			return null;
		}
	}

	public ArrayList<ChunkData> getProtectedChunks(World world)
	{
		ArrayList<ChunkData> chunks = new ArrayList<ChunkData>();

		String query = "SELECT * FROM chunks";
		try
		{
			PreparedStatement ps = c.prepareStatement(query);
			ResultSet res = ps.executeQuery();

			int x = res.getInt("x");
			int z = res.getInt("z");

			Chunk chunk = world.getChunkAt(x, z);

			while (res.next())
			{
				chunks.add(new ChunkData(chunk, res.getString("owner"), world));
			}
			return chunks;
		}
		catch (SQLException e)
		{
			return chunks;
		}
	}

	public ResultSet getChunkData(Chunk chunk)
	{
		String query = "SELECT * FROM chunks WHERE x = ? AND z = ?";

		try
		{
			PreparedStatement ps = c.prepareStatement(query);
			ps.setInt(1, chunk.getX());
			ps.setInt(2, chunk.getZ());

			ResultSet res = ps.executeQuery();
			return res;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			Bukkit.getServer().broadcastMessage("Chunk data error");
			return null;
		}
	}

	public boolean addResident(ChunkData cd, String addative)
	{
		try
		{
			PreparedStatement ps = c.prepareStatement("SELECT * FROM chunks WHERE owner = ? AND x = ? AND z = ?");
			ps.setString(1, cd.getOwner());
			ps.setInt(2, cd.getX());
			ps.setInt(3, cd.getZ());
			ResultSet res = ps.executeQuery();
			if (res.next())
			{
				String residents = res.getString("residents");
				residents += addative.toUpperCase() + ":";

				PreparedStatement insert = c.prepareStatement("UPDATE chunks SET residents = ? WHERE owner = ? AND x = ? AND z = ?");
				insert.setString(1, residents);
				insert.setString(2, cd.getOwner());
				insert.setInt(3, cd.getX());
				insert.setInt(4, cd.getZ());
				insert.executeUpdate();

				return true;
			}
			return false;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return false;
		}

	}

	public ResultSet getPlayerData(String username)
	{
		String query = "SELECT * FROM users WHERE username = ? AND totalChunks > -1";
		try
		{
			PreparedStatement ps = c.prepareStatement(query);
			ps.setString(1, username);
			ResultSet res = ps.executeQuery();
			res.next();

			return res;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public boolean createProtection(ChunkData cd, String owner)
	{
		try
		{
			String query = "INSERT IGNORE INTO chunks (owner,residents,x,y,z) VALUES(?,?,?,?,?)";

			PreparedStatement ps = c.prepareStatement(query);
			ps.setString(1, owner);
			ps.setString(2, owner.toUpperCase() + ":");
			ps.setInt(3, cd.getX());
			ps.setInt(4, 3);
			ps.setInt(5, cd.getZ());
			ps.executeUpdate();
			CP.chunks.add(cd);
			ResultSet pd = getPlayerData(owner);
			int totChunks = pd.getInt("totalChunks") + 1;

			PreparedStatement ps1 = c.prepareStatement("UPDATE users SET totalChunks = ? WHERE username = ?");
			ps1.setInt(1, totChunks);
			ps1.setString(2, owner);
			ps1.executeUpdate();

			return true;
		}
		catch (SQLException e)
		{
			Bukkit.getServer().broadcastMessage(e.getMessage());
			return false;
		}
	}

	public boolean removeProtection(ChunkData cd)
	{
		try
		{
			String query = "DELETE FROM chunks WHERE owner = ? AND x = ? AND z = ?";

			PreparedStatement ps = c.prepareStatement(query);
			ps.setString(1, cd.getOwner());
			ps.setInt(2, cd.getX());
			ps.setInt(3, cd.getZ());

			ps.executeUpdate();

			ResultSet pd = getPlayerData(cd.getOwner());
			int totChunks = pd.getInt("totalChunks") - 1;

			PreparedStatement ps1 = c.prepareStatement("UPDATE users SET totalChunks = ? WHERE username = ?");
			ps1.setInt(1, totChunks);
			ps1.setString(2, cd.getOwner());
			ps1.executeUpdate();
			ps1.close();

			CP.chunks.remove(cd);
			ps.close();

			return true;

		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return false;
		}
	}

	public boolean removeResident(ChunkData chunk, String remove)
	{
		try
		{
			ArrayList<String> currentResidents = chunk.getResidents();
			currentResidents.remove(remove.toUpperCase());

			String mysqlAdd = "";

			for (String s : currentResidents)
			{
				mysqlAdd += s + ":";
			}

			PreparedStatement ps = c.prepareStatement("UPDATE chunks SET residents = ? WHERE owner = ? AND x = ? AND z = ?");
			ps.setString(1, mysqlAdd);
			ps.setString(2, chunk.getOwner());
			ps.setInt(3, chunk.getX());
			ps.setInt(4, chunk.getZ());

			ps.executeUpdate();
			return true;

		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return false;
		}
	}

	public boolean createUserData(String username)
	{

		try
		{

			if (!getPlayerData(username).next())
			{
				String query = "INSERT INTO users SET username = ?,totalChunks = ?";
				PreparedStatement ps = c.prepareStatement(query);
				ps.setString(1, username);
				ps.setString(2, "0");
				ps.executeUpdate();
				return true;
			}
			else
			{
				return true;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
}
