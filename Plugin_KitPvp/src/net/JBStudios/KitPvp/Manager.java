package net.JBStudios.KitPvp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import net.JBStudios.KitPvp.GameData.PropKeys;


public class Manager extends JavaPlugin {
	
	private static Manager manager;
	
	private ArrayList<GameData> gameDataList;
	
	public void onEnable() {
		super.onEnable();
		
		gameDataList = new ArrayList<GameData>();
		manager = this;
		
		File file = getDataFolder();
		if (file.exists()) {
			file = new File(file.toString()+"/games");
			if (file.exists()) {
				for (File propertiesFile: file.listFiles()) {
					Properties properties = new Properties();
					try {
						properties.load(new FileInputStream(new File(propertiesFile+"/gameData.properties")));
						gameDataList.add(new GameData(properties));
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
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
		file = new File(file.toString()+"/games");
		if (!file.exists()) {
			file.mkdir();
		}else {
			for (File inFile: file.listFiles()) {
				fileDeletor(inFile);
			}
		}
		for (GameData gameData: gameDataList) {
			file = new File(getDataFolder()+"/games/"+gameData.getProperty(PropKeys.Name));
			file.mkdir();
			file = new File(getDataFolder()+"/games/"+gameData.getProperty(PropKeys.Name)+"/gameData.properties");
			try {
				gameData.getProperties().store(new FileOutputStream(file), "");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		getLogger().info(ChatColor.RED+""+ChatColor.BOLD+"KITPVP: "+ChatColor.RESET+""+ChatColor.RED+"has been disabled!");
	}
	
	public ArrayList<GameData> getGameDataList() {
		return gameDataList;
	}
	
	public static Manager getManager() {
		return manager;
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
