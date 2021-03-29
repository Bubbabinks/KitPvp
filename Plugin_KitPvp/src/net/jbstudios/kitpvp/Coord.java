package net.jbstudios.kitpvp;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class Coord extends Location {

	public Coord(Location location) {
		super(location.getWorld(), location.getBlockX()+0.5, location.getBlockY(), location.getBlockZ()+0.5);
		setPitch(location.getPitch());
		setYaw(location.getYaw());
	}
	
	public Coord(String location) {
		super(null, 0d, 0d, 0d);
		String[] locations = location.substring(1, location.length()-1).split(",");
		setWorld(Bukkit.getWorld(locations[0]));
		setX(Double.parseDouble(locations[1]));
		setY(Double.parseDouble(locations[2]));
		setZ(Double.parseDouble(locations[3]));
		setPitch(Float.parseFloat(locations[4]));
		setYaw(Float.parseFloat(locations[5]));
		while (getWorld() == null) {
			setWorld(Bukkit.getWorld(location.substring(1, location.length()-1).split(",")[0]));
		}
	}
	
	public String toString() {
		return "["+getWorld().getName()+","+getX()+","+getY()+","+getZ()+","+getPitch()+","+getYaw()+"]";
	}
	
}
