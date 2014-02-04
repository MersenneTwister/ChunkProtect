package net.blockarray.cp.wrappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import net.blockarray.cp.CP;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;

public class ChunkData
{
	private Chunk chunk;
	private String owner = "";
	private int x, z;

	public ChunkData(Chunk chunk)
	{
		this.chunk = chunk;
		if (isProtected())
		{
			try
			{
				ResultSet res = CP.getNetwork().getChunkData(this.chunk);
				res.next();
				owner = res.getString("owner");
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		}

		this.x = chunk.getX();
		this.z = chunk.getZ();
	}

	public ChunkData(Chunk chunk, String owner, World world)
	{
		this.chunk = chunk;
		this.owner = owner;

		this.x = chunk.getX();
		this.z = chunk.getZ();
	}

	public boolean isProtected()
	{
		ResultSet res = CP.getNetwork().getChunkData(chunk);
		try
		{
			if (res.next())
				return true;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}

	public Chunk getChunk()
	{
		return chunk;
	}

	public String getOwner()
	{
		return owner;
	}

	public void setOwner(String owner)
	{
		this.owner = owner;
	}

	public ArrayList<String> getResidents() throws SQLException
	{
		ResultSet res = CP.getNetwork().getChunkData(chunk);
		res.next();
		String chunkResidents = res.getString("residents");
		ArrayList<String> residents = new ArrayList<String>();

		for (String s : chunkResidents.split(":"))
		{
			residents.add(s);
		}
		return residents;
	}

	public void addResident(String addative) throws SQLException
	{
		CP.getNetwork().addResident(this, addative);
	}

	public boolean addProtection(String username)
	{
		owner = username;
		return CP.getNetwork().createProtection(this, username);
	}

	public int getX()
	{
		return x;
	}

	public int getZ()
	{
		return z;
	}

	public String getCoords()
	{
		return "&7X: &6" + ((x * 16) + 8) + "&7, Z: &6" + ((z * 16) + 8);
	}

	public Location getLocation()
	{
		return new Location(chunk.getWorld(), x, 50, z);
	}

	public void removeResident(String string)
	{
		CP.getNetwork().removeResident(this, string);
	}

	public void removeProtection()
	{
		CP.getNetwork().removeProtection(this);
	}
}
