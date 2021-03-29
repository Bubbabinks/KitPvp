package net.jbstudios.scriptable_items;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class ItemAdapter {
	
	public abstract void addPlayer(Player player);
	public abstract void removePlayer(Player player);
	public abstract String toString();
	public abstract ItemStack getItem();
	
	public void update() {}
	public void lateUpdate() {}
	
	public void close() {}
	
}
