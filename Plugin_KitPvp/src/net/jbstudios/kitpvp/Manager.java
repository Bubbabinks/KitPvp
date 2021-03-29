package net.jbstudios.kitpvp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import net.jbstudios.kitpvp.GameData.PropKeys;
import net.jbstudios.scriptable_items.ScriptableItems;


public class Manager extends JavaPlugin {
	
	public static List<Player> editPlayers;
	
	private static Manager manager;
	
	private ScriptableItems scriptableItems;
	
	private ArrayList<GameData> gameDataList;
	
	public void onEnable() {
		super.onEnable();
		editPlayers = new ArrayList<Player>();
		gameDataList = new ArrayList<GameData>();
		manager = this;
		
		scriptableItems = new ScriptableItems();
		
		File file = getDataFolder();
		if (file.exists()) {
			for (File propertiesFile: file.listFiles()) {
				Properties properties = new Properties();
				try {
					properties.load(new FileInputStream(new File(propertiesFile+"/gameData.properties")));
					getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
						public void run() {
							gameDataList.add(new GameData(properties));
						}
					});
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		getCommand("KP").setExecutor(new Executor());
		getCommand("KP").setTabCompleter(new Completer());
		
		getLogger().info(ChatColor.GREEN+""+ChatColor.BOLD+"KITPVP: "+ChatColor.RESET+""+ChatColor.GREEN+"has been enabled!");
	}
	
	public void onDisable() {
		super.onDisable();
		File file = getDataFolder();
		if (!file.exists()) {
			file.mkdir();
		}
		
		for (File inFile: file.listFiles()) {
			fileDeletor(inFile);
		}
		
		scriptableItems.close();
		
		for (GameData gameData: gameDataList) {
			file = new File(getDataFolder()+"/"+gameData.getProperty(PropKeys.Name));
			file.mkdir();
			file = new File(getDataFolder()+"/"+gameData.getProperty(PropKeys.Name)+"/gameData.properties");
			try {
				gameData.getProperties().store(new FileOutputStream(file), "");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			gameData.disable();
		}
		
		getLogger().info(ChatColor.RED+""+ChatColor.BOLD+"KITPVP: "+ChatColor.RESET+""+ChatColor.RED+"has been disabled!");
	}
	
	public ArrayList<GameData> getGameDataList() {
		return gameDataList;
	}
	
	public static Manager getManager() {
		return manager;
	}
	
	public ScriptableItems getScriptableItems() {
		return scriptableItems;
	}
	
	public void removeKit(Kit kit) {
		for (GameData gameData: gameDataList) {
			gameData.removeKit(kit);
		}
	}
	
	public static void fileDeletor(File file) {
		if (file.isDirectory()) {
			for (File inFile: file.listFiles()) {
				fileDeletor(inFile);
			}
		}
		file.delete();
	}
	
}
