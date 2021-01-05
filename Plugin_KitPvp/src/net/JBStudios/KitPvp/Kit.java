package net.JBStudios.KitPvp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Kit implements Listener, Runnable {
	
	private Coord coord;
	private ArmorStand armorStand;
	private ArrayList<Item> items;
	
	private ArrayList<KitListener> kitListeners;
	private List<Integer> effects;
	
	private int taskId;
	
	public Kit(Coord coord) {
		effects = new ArrayList<Integer>();
		for (int i=0;i<PotionEffectType.values().length;i++) {
			effects.add(0);
		}
		items = new ArrayList<Item>();
		kitListeners = new ArrayList<KitListener>();
		armorStand = (ArmorStand)coord.getWorld().spawnEntity(coord, EntityType.ARMOR_STAND);
		armorStand.setArms(true);
		Bukkit.getPluginManager().registerEvents(this, Manager.getManager());
		taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Manager.getManager(), this, 0, 5);
		this.coord = coord;
	}
	
	public Kit(File file) {
		items = new ArrayList<Item>();
		kitListeners = new ArrayList<KitListener>();
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);
		coord = new Coord(config.getString("coord"));
		armorStand = (ArmorStand)coord.getWorld().spawnEntity(coord, EntityType.ARMOR_STAND);
		armorStand.setArms(true);
		Bukkit.getPluginManager().registerEvents(this, Manager.getManager());
		taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Manager.getManager(), this, 0, 5);
		int i = 0;
		while (config.getItemStack("item"+i) != null) {
			items.add(new Item(config.getInt("itemSlot"+i), config.getItemStack("item"+i)));
			i++;
		}
		effects = config.getIntegerList("effects");
		refreshArmorStand();
	}
	
	public void run() {
		boolean setInvisible = false;
		for (int i=0;i<effects.size();i++) {
			if (effects.get(i) > 0 && PotionEffectType.values()[i].equals(PotionEffectType.INVISIBILITY)) {
				setInvisible = true;
			}else if (effects.get(i) > 0) {
				armorStand.addPotionEffect(new PotionEffect(PotionEffectType.values()[i], 10, 0, true, false));
			}
		}
		armorStand.setInvisible(setInvisible);
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
	
	public void addKitListener(KitListener kitListener) {
		kitListeners.add(kitListener);
	}
	
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof Player && e.getEntity() instanceof ArmorStand) {
			ArmorStand armorStand = (ArmorStand) e.getEntity();
			Player player = (Player) e.getDamager();
			if (armorStand == this.armorStand) {
				e.setCancelled(true);
				for (KitListener kitListener: kitListeners) {
					kitListener.onKitSelected(player, this);
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
	
	public void saveKit(File file) {
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);
		for (int i=0;i<items.size();i++) {
			ItemStack item = new ItemStack(items.get(i));
			config.set("item"+i, item);
			config.set("itemSlot"+i, items.get(i).getSlot());
		}
		config.set("coord", coord.toString());
		config.set("effects", effects);
		try {
			config.save(file);
		} catch (IOException e) {}
	}
	
	private void openKitEditor(Player p) {
		new KitEditor(this, p);
	}
	
	public void disable() {
		HandlerList.unregisterAll(this);
		Bukkit.getScheduler().cancelTask(taskId);
		armorStand.remove();
	}
	
	public interface KitListener {
		public void onKitSelected(Player player, Kit kit);
	}
	
	public List<Integer> getKitEffects() {
		return effects;
	}
	
}
