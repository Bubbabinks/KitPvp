package net.JBStudios.KitPvp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import net.JBStudios.KitPvp.GameData.PropKeys;

public class Completer implements TabCompleter {

	private Manager manager;
	private ArrayList<GameData> gameDataList;
	
	private ArrayList<String> args0;
	private ArrayList<String> args2;
	
	public Completer() {
		args0 = new ArrayList<String>();
		manager = Manager.getManager();
		gameDataList = manager.getGameDataList();
		args0.add("create");
		args0.add("remove");
		args0.add("list");
		args0.add("modify");
		
		args2 = new ArrayList<String>();
		args2.add("list");
		args2.add("setLobby");
		args2.add("addKit");
		args2.add("addSpawn");
		args2.add("showSpawns");
	}
	
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		Collection<String> collection = new ArrayList<String>();
		
		if (args.length == 1) {
			StringUtil.copyPartialMatches(args[0], args0, collection);
		}
		if (args.length == 2) {
			if (args[0].equalsIgnoreCase("remove")) {
				ArrayList<String> names = new ArrayList<String>();
				for (GameData gameData: gameDataList) {
					names.add(gameData.getProperty(PropKeys.Name));
				}
				StringUtil.copyPartialMatches(args[1], names, collection);
			}else if (args[0].equalsIgnoreCase("modify")) {
				ArrayList<String> names = new ArrayList<String>();
				for (GameData gameData: gameDataList) {
					names.add(gameData.getProperty(PropKeys.Name));
				}
				StringUtil.copyPartialMatches(args[1], names, collection);
			}
		}if (args.length == 3) {
			if (args[0].equalsIgnoreCase("modify")) {
				StringUtil.copyPartialMatches(args[2], args2, collection);
			}
		}
		
		return (List<String>) collection;
	}
}
