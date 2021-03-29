package net.jbstudios.scriptable_items;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.jbstudios.kitpvp.Manager;
import net.jbstudios.scriptable_items.gun.Gun;

public class ScriptableItems implements Runnable {

	private static List<ItemAdapter> itemAdapters = new ArrayList<ItemAdapter>();
	
	private int taskId = 0;
	
	public ScriptableItems() {
		new Gun();
		
		taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Manager.getManager(), this, 1L, 1L);
	}
	
	public void run() {
		for (ItemAdapter itemAdapter: itemAdapters) {
			itemAdapter.update();
		}
		for (ItemAdapter itemAdapter: itemAdapters) {
			itemAdapter.lateUpdate();
		}
	}
	
	public void close() {
		Bukkit.getScheduler().cancelTask(taskId);
		for (ItemAdapter itemAdapter: itemAdapters) {
			itemAdapter.close();
		}
	}
	
	public static void addItemAdapter(ItemAdapter itemAdapter) {
		itemAdapters.add(itemAdapter);
	}
	
	public static List<ItemAdapter> getItemAdapters() {
		return itemAdapters;
	}
	
	public static void removePlayer(Player player) {
		for (ItemAdapter itemAdapter: itemAdapters) {
			itemAdapter.removePlayer(player);
		}
	}
	
}
