package net.jbstudios.kitpvp;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SpawnMarker implements Listener {

	private Coord spawn;
	private GameData gameData;
	
	private ArmorStand armorStand;
	private Inventory inventory;
	private Player player;
	
	public SpawnMarker(Coord spawn, GameData gameData) {
		this.spawn = spawn;
		this.gameData = gameData;
		armorStand = (ArmorStand)spawn.getWorld().spawnEntity(spawn, EntityType.ARMOR_STAND);
		armorStand.setInvulnerable(true);
		armorStand.setGravity(false);
		inventory = Bukkit.createInventory(null, 9, "Spawn Marker");
		
		ItemStack removeItem = new ItemStack(Material.NETHER_STAR);
		ItemMeta meta = removeItem.getItemMeta();
		meta.setDisplayName("Click To Remove Spawn");
		removeItem.setItemMeta(meta);
		inventory.setItem(4, removeItem);
		
		Bukkit.getPluginManager().registerEvents(this, Manager.getManager());
	}
	
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof Player && e.getEntity() instanceof ArmorStand) {
			ArmorStand armorStand = (ArmorStand) e.getEntity();
			Player player = (Player) e.getDamager();
			if (armorStand == this.armorStand) {
				e.setCancelled(true);
				if (player.hasPermission("KP") || player.isOp()) {
					player.openInventory(inventory);
					this.player = player;
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
					if (this.player == null) {
						player.openInventory(inventory);
						this.player = player;
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onClose(InventoryCloseEvent e) {
		if (e.getInventory() == inventory) {
			player = null;
		}
	}
	
	@EventHandler
	public void onInteract(InventoryClickEvent e) {
		if (e.getInventory() == inventory) {
			e.setCancelled(true);
			if (e.getCurrentItem() != null) {
				if (e.getCurrentItem().getType() == Material.NETHER_STAR) {
					if (e.getSlot() == 4) {
						player.closeInventory();
						gameData.removeSpawn(spawn);
						disable();
					}
				}
			}
		}
	}
	
	public void disable() {
		armorStand.remove();
		HandlerList.unregisterAll(this);
	}
	
}
