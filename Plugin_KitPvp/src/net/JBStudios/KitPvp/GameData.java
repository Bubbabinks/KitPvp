package net.JBStudios.KitPvp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.Listener;

public class GameData implements Listener {
	
	public enum PropKeys {
		Lobby("lobby"),
		Name("name");
		
		String value;
		
		private PropKeys(String value) {
			this.value = value;
		}
		
		public String toString() {
			return value;
		}
	}
	
	private ArrayList<Kit> kits;
	
	private Properties properties;
	
	public GameData(String name) {
		properties = new Properties();
		kits = new ArrayList<Kit>();
		setProperty(PropKeys.Name, name);
	}
	
	public GameData(Properties properties) {
		this.properties = properties;
		kits = new ArrayList<Kit>();
		for (File file: new File(Manager.getManager().getDataFolder().toString()+"/"+getProperty(PropKeys.Name)).listFiles()) {
			if (file.getName().contains("kit")) {
				Properties p = new Properties();
				try {
					p.load(new FileInputStream(file));
				} catch (FileNotFoundException e) {} catch (IOException e) {}
				Bukkit.getScheduler().scheduleSyncDelayedTask(Manager.getManager(), new Runnable() {
					public void run() {
						kits.add(new Kit(p));
					}
				}, 1L);
			}
		}
	}
	
	public Properties getProperties() {
		return properties;
	}
	
	public String getProperty(PropKeys key) {
		return properties.getProperty(key+"");
	}
	
	public void setProperty(PropKeys key, String value) {
		properties.setProperty(key+"", value);
	}
	
	public void addKit(Location location) {
		if (Math.abs(location.getYaw()) <= 45) {
			location.setYaw(0f);
		}else if (location.getYaw() < 135f && location.getYaw() > 45f) {
			location.setYaw(90f);
		}else if (location.getYaw() > -135f && location.getYaw() < -45f) {
			location.setYaw(-90f);
		}else {
			location.setYaw(180f);
		}
		location.setPitch(0f);
		Coord coord = new Coord(location);
		
		kits.add(new Kit(coord));
	}
	
	public void disable() {
		for (int i=0;i<kits.size();i++) {
			kits.get(i).disable();
			Properties properties = kits.get(i).generateProperties();
			try {
				properties.store(new FileOutputStream(new File(Manager.getManager().getDataFolder().toString()+"/"+getProperty(PropKeys.Name)+"/kit"+i+".properties")), "");
			} catch (FileNotFoundException e) {} catch (IOException e) {}
		}
	}
	
}
