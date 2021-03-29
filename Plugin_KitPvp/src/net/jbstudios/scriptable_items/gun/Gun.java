package net.jbstudios.scriptable_items.gun;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.jbstudios.kitpvp.Manager;
import net.jbstudios.scriptable_items.ItemAdapter;
import net.jbstudios.scriptable_items.ScriptableItems;

public class Gun extends ItemAdapter implements Listener {
	
	private List<Player> players;
	
	private ItemStack item;
	
	public Gun() {
		players = new ArrayList<Player>();
		ScriptableItems.addItemAdapter(this);
		item = new ItemStack(Material.NETHERITE_HOE);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("M16");
		item.setItemMeta(meta);
		Bukkit.getPluginManager().registerEvents(this, Manager.getManager());
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		if (players.contains(e.getPlayer())) {
			if (e.getPlayer().getInventory().getItemInMainHand().getType().equals(item.getType())) {
				if (e.getAction().equals(Action.LEFT_CLICK_AIR) || e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
					e.getPlayer().sendMessage("Coming Soon! LOL");
				}
				e.setCancelled(true);
			}
		}
	}
	
	public void addPlayer(Player player) {
		players.add(player);
	}
	
	public void removePlayer(Player player) {
		players.remove(player);
	}
	
	@Override
	public void update() {
		super.update();
		
	}

	public String toString() {
		return "Gun";
	}
	
	public ItemStack getItem() {
		return item;
	}
	
}
