package net.JBStudios.KitPvp;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class Coord extends Location {

	public Coord(Location location) {
		super(location.getWorld(), location.getBlockX()+0.5, location.getBlockY(), location.getBlockZ()+0.5);
		setPitch(location.getPitch());
		setYaw(location.getYaw());
	}
	
	public Coord(String location) {
		super(Bukkit.getWorld(location.substring(1, location.length()-1).split(",")[0]),
				Double.parseDouble(location.substring(1, location.length()-1).split(",")[1]),
				Double.parseDouble(location.substring(1, location.length()-1).split(",")[2]),
				Double.parseDouble(location.substring(1, location.length()-1).split(",")[3]));
		setPitch(Float.parseFloat(location.substring(1, location.length()-1).split(",")[4]));
		setYaw(Float.parseFloat(location.substring(1, location.length()-1).split(",")[5]));
		while (getWorld() == null) {
			setWorld(Bukkit.getWorld(location.substring(1, location.length()-1).split(",")[0]));
		}
	}
	
	public String toString() {
		return "["+getWorld().getName()+","+getX()+","+getY()+","+getZ()+","+getPitch()+","+getYaw()+"]";
	}
	
}
