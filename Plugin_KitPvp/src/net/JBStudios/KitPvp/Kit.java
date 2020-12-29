package net.JBStudios.KitPvp;

import java.util.ArrayList;
import java.util.Properties;

import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.EntityEquipment;

public class Kit implements Listener {
	
	private Coord coord;
	private ArmorStand armorStand;
	private ArrayList<Item> items;
	
	public Kit(Coord coord) {
		items = new ArrayList<Item>();
		armorStand = (ArmorStand)coord.getWorld().spawnEntity(coord, EntityType.ARMOR_STAND);
		armorStand.setArms(true);
		Bukkit.getPluginManager().registerEvents(this, Manager.getManager());
		this.coord = coord;
	}
	
	public Kit(Properties properties) {
		items = new ArrayList<Item>();
		coord = new Coord(properties.getProperty("coord"));
		armorStand = (ArmorStand)coord.getWorld().spawnEntity(coord, EntityType.ARMOR_STAND);
		armorStand.setArms(true);
		Bukkit.getPluginManager().registerEvents(this, Manager.getManager());
		int i = 0;
		while (properties.getProperty("item"+i) != null) {
			items.add(new Item(properties.getProperty("item"+i)));
			i++;
		}
		refreshArmorStand();
	}
	
	public void refreshArmorStand() {
		EntityEquipment equipment = armorStand.getEquipment();
		equipment.clear();
		for (Item item: items) {
			if (item.getSlot() == 100) {
				equipment.setHelmet(item);
			}
			if (item.getSlot() == 101) {
				equipment.setChestplate(item);
			}
			if (item.getSlot() == 102) {
				equipment.setLeggings(item);
			}
			if (item.getSlot() == 103) {
				equipment.setBoots(item);
			}
			if (item.getSlot() == 104) {
				equipment.setItemInMainHand(item);
			}
			if (item.getSlot() == 105) {
				equipment.setItemInOffHand(item);
			}
		}
	}
	
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof Player && e.getEntity() instanceof ArmorStand) {
			ArmorStand armorStand = (ArmorStand) e.getEntity();
			Player player = (Player) e.getDamager();
			if (armorStand == this.armorStand) {
				e.setCancelled(true);
				if (player.hasPermission("KP") || player.isOp()) {
					openKitEditor(player);
				}
			}
		}
	}
	
	@EventHandler
	public void onInteract(PlayerInteractAtEntityEvent e) {
		if (e.getRightClicked() instanceof ArmorStand) {
			ArmorStand armorStand = (ArmorStand) e.getRightClicked();
			Player player = e.getPlayer();
			if (armorStand == this.armorStand) {
				e.setCancelled(true);
				if (player.hasPermission("KP") || player.isOp()) {
					openKitEditor(player);
				}
			}
		}
	}
	
	public void removeAllItems() {
		items = new ArrayList<Item>();
	}
	
	public void addItem(Item item) {
		items.add(item);
	}
	
	public ArrayList<Item> getItems() {
		return items;
	}
	
	public Properties generateProperties() {
		Properties properties = new Properties();
		properties.setProperty("coord", coord.toString());
		for (int i=0;i<items.size();i++) {
			properties.setProperty("item"+i, items.get(i).toString());
		}
		return properties;
	}
	
	private void openKitEditor(Player p) {
		new KitEditor(this, p);
	}
	
	public void disable() {
		armorStand.remove();
		HandlerList.unregisterAll(this);
	}
	
}
