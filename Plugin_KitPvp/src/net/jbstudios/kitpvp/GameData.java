package net.jbstudios.kitpvp;

import java.io.File;
import java.util.ArrayList;
import java.util.Properties;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;

import net.jbstudios.kitpvp.Kit.KitListener;

public class GameData implements Listener {
	
	public enum PropKeys {
		Lobby("lobby"),
		Name("name"),
		WorldSpawn("worldSpawn");
		
		String value;
		
		private PropKeys(String value) {
			this.value = value;
		}
		
		public String toString() {
			return value;
		}
	}
	
	private ArrayList<Kit> kits;
	private ArrayList<Coord> spawns;
	private ArrayList<SpawnMarker> spawnMarkers;
	
	private Game game;
	
	private boolean spawnMarkersEnabled;
	
	private Properties properties;
	
	public GameData(String name) {
		properties = new Properties();
		kits = new ArrayList<Kit>();
		spawns = new ArrayList<Coord>();
		spawnMarkers = new ArrayList<SpawnMarker>();
		game = new Game(this);
		spawnMarkersEnabled = false;
		setProperty(PropKeys.Name, name);
		Bukkit.getPluginManager().registerEvents(this, Manager.getManager());
	}
	
	public GameData(Properties properties) {
		this.properties = properties;
		kits = new ArrayList<Kit>();
		spawns = new ArrayList<Coord>();
		spawnMarkers = new ArrayList<SpawnMarker>();
		game = new Game(this);
		spawnMarkersEnabled = false;
		for (File file: new File(Manager.getManager().getDataFolder().toString()+"/"+getProperty(PropKeys.Name)).listFiles()) {
			if (file.getName().contains("kit")) {
				Kit kit = new Kit(file);
				kit.addKitListener(new KitListener() {
					public void onKitSelected(Player player, Kit kit) {
						game.addPlayer(player, kit);
					}
				});
				kits.add(kit);
			}
		}
		int i=0;
		while (properties.getProperty("spawn"+i) != null) {
			spawns.add(new Coord(properties.getProperty("spawn"+i)));
			properties.remove("spawn"+i);
			i++;
		}
		Bukkit.getPluginManager().registerEvents(this, Manager.getManager());
	}
	
	@EventHandler
	public void onJoinGame(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		if (spawns.size() > 0) {
			if (spawns.get(0).getWorld().equals(player.getWorld())) {
				for (PotionEffect effect: player.getActivePotionEffects()) {
					player.removePotionEffect(effect.getType());
				}
				player.getInventory().clear();
				player.teleport(new Coord(getProperty(PropKeys.WorldSpawn)));
			}
		}
	}
	
	public void addSpawn(Coord coord) {
		spawns.add(coord);
		if (spawnMarkersEnabled) {
			spawnMarkers.add(new SpawnMarker(coord, this));
		}
	}
	
	public void removeSpawn(Coord coord) {
		spawns.remove(coord);
	}
	
	public ArrayList<Coord> getSpawns() {
		return spawns;
	}
	
	public void toggleSpawnMarkers() {
		if (spawnMarkersEnabled) {
			for (int i=0;i<spawnMarkers.size();i++) {
				spawnMarkers.get(i).disable();
			}
			for (int i=0;i<spawnMarkers.size();i++) {
				spawnMarkers.remove(i);
			}
			spawnMarkersEnabled = false;
		}else {
			for (Coord spawn: spawns) {
				spawnMarkers.add(new SpawnMarker(spawn, this));
			}
			spawnMarkersEnabled = true;
		}
	}
	
	public Properties getProperties() {
		for (int i=0;i<spawns.size();i++) {
			properties.setProperty("spawn"+i, spawns.get(i).toString());
		}
		return properties;
	}
	
	public String getProperty(PropKeys key) {
		return properties.getProperty(key+"");
	}
	
	public void setProperty(PropKeys key, String value) {
		properties.setProperty(key+"", value);
	}
	
	public void addKit(Location location) {
		float yaw = 180f;
		if (location.getYaw() < 45f || location.getYaw() > 315) {
			yaw = 0f;
		}
		if (location.getYaw() < 135f && location.getYaw() > 45f) {
			yaw = 90f;
		}
		if (location.getYaw() > 255f && location.getYaw() < 315) {
			yaw = -90;
		}
		location.setYaw(yaw);
		location.setPitch(0f);
		Coord coord = new Coord(location);
		
		Kit kit = new Kit(coord);
		kit.addKitListener(new KitListener() {
			public void onKitSelected(Player player, Kit kit) {
				game.addPlayer(player, kit);
			}
		});
		
		kits.add(kit);
	}
	
	public void removeKit(Kit kit) {
		kits.remove(kit);
	}
	
	public void disable() {
		for (int i=0;i<kits.size();i++) {
			kits.get(i).disable();
			kits.get(i).saveKit(new File(Manager.getManager().getDataFolder().toString()+"/"+getProperty(PropKeys.Name)+"/kit"+i+".yml"));
		}
		for (int i=0;i<spawnMarkers.size();i++) {
			spawnMarkers.get(i).disable();
		}
		for (int i=0;i<spawnMarkers.size();i++) {
			spawnMarkers.remove(i);
		}
		game.disable();
		HandlerList.unregisterAll(this);
	}
	
}
