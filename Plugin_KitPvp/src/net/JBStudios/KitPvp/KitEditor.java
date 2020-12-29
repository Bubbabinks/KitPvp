package net.JBStudios.KitPvp;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class KitEditor implements Listener {
	
	private Inventory inventory;
	private Kit kit;
	private Player player;
	
	public KitEditor(Kit kit, Player player) {
		this.kit = kit;
		this.player = player;
		Bukkit.getPluginManager().registerEvents(this, Manager.getManager());
		generateInventory();
		player.openInventory(inventory);
	}
	
	@EventHandler
	public void onInteract(InventoryClickEvent e) {
		if (e.getInventory() == inventory) {
			if (e.getCurrentItem() != null) {
				if (e.getCurrentItem().getType() == Material.BARRIER) {
					e.setCancelled(true);
				}else if (e.getCurrentItem().getType() == Material.NETHER_STAR) {
					if (e.getSlot() == 26) {
						e.setCancelled(true);
						player.closeInventory();
						kit.disable();
						Manager.getManager().removeKit(kit);
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onClose(InventoryCloseEvent e) {
		if (e.getInventory() == inventory) {
			kit.removeAllItems();
			
			if (inventory.getItem(5) != null) {
				kit.addItem(new Item(99, inventory.getItem(5)));
			}
			if (inventory.getItem(0) != null) {
				kit.addItem(new Item(100, inventory.getItem(0)));
			}
			if (inventory.getItem(1) != null) {
				kit.addItem(new Item(101, inventory.getItem(1)));
			}
			if (inventory.getItem(2) != null) {
				kit.addItem(new Item(102, inventory.getItem(2)));
			}
			if (inventory.getItem(3) != null) {
				kit.addItem(new Item(103, inventory.getItem(3)));
			}
			if (inventory.getItem(7) != null) {
				kit.addItem(new Item(104, inventory.getItem(7)));
			}
			if (inventory.getItem(8) != null) {
				kit.addItem(new Item(105, inventory.getItem(8)));
			}
			
			for (int i=18;i<54;i++) {
				if (inventory.getItem(i) != null) {
					if (i == 27) {
						ItemMeta meta = inventory.getItem(i).getItemMeta();
						meta.setDisplayName("Click To Leave");
						inventory.getItem(i).setItemMeta(meta);
					}else {
						kit.addItem(new Item(i-18, inventory.getItem(i)));
					}
				}
			}
			
			kit.refreshArmorStand();
			HandlerList.unregisterAll(this);
		}
	}
	
	private Inventory generateInventory() {
		inventory = Bukkit.createInventory(null, 54, "Kit Editor");
		for (int i=9;i<18;i++) {
			inventory.setItem(i, new ItemStack(Material.BARRIER));
		}
		inventory.setItem(4, new ItemStack(Material.BARRIER));
		inventory.setItem(6, new ItemStack(Material.BARRIER));
		
		for (Item item: kit.getItems()) {
			if (item.getSlot() < 36) {
				inventory.setItem(item.getSlot()+18, item);
			}
			if (item.getSlot() == 99) {
				inventory.setItem(5, item);
			}
			if (item.getSlot() == 100) {
				inventory.setItem(0, item);
			}
			if (item.getSlot() == 101) {
				inventory.setItem(1, item);
			}
			if (item.getSlot() == 102) {
				inventory.setItem(2, item);
			}
			if (item.getSlot() == 103) {
				inventory.setItem(3, item);
			}
			if (item.getSlot() == 104) {
				inventory.setItem(7, item);
			}
			if (item.getSlot() == 105) {
				inventory.setItem(8, item);
			}
		}
		
		ItemStack netherStar = new ItemStack(Material.NETHER_STAR);
		ItemMeta meta = netherStar.getItemMeta();
		meta.setDisplayName("Click To Delete Kit");
		netherStar.setItemMeta(meta);
		
		inventory.setItem(26, netherStar);
		
		return inventory;
	}
	
}
