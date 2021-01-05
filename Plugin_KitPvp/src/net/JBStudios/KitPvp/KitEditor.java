package net.JBStudios.KitPvp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
import org.bukkit.potion.PotionEffectType;

public class KitEditor implements Listener {
	
	private Inventory inventory;
	private Kit kit;
	private Player player;
	
	private int page = 0;
	
	private Material[] materials = new Material[] {
			Material.SUGAR,
			Material.ANVIL,
			Material.GOLDEN_PICKAXE,
			Material.WOODEN_PICKAXE,
			Material.NETHERITE_SWORD,
			Material.GOLDEN_APPLE,
			Material.TNT,
			Material.RABBIT_FOOT,
			Material.CRIMSON_HYPHAE,
			Material.GHAST_TEAR,
			Material.NETHERITE_CHESTPLATE,
			Material.LAVA_BUCKET,
			Material.WATER_BUCKET,
			Material.GLASS,
			Material.COAL_BLOCK,
			Material.GLOWSTONE,
			Material.ROTTEN_FLESH,
			Material.WOODEN_SWORD,
			Material.SPIDER_EYE,
			Material.WITHER_SKELETON_SKULL,
			Material.GOLD_NUGGET,
			Material.REDSTONE_BLOCK,
			Material.GOLDEN_CARROT,
			Material.SPECTRAL_ARROW,
			Material.ELYTRA,
			Material.LAPIS_BLOCK,
			Material.STONE,
			Material.FEATHER,
			Material.CONDUIT,
			Material.DOLPHIN_SPAWN_EGG,
			Material.BLACK_BANNER,
			Material.EMERALD_BLOCK
	};
	
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
					if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("Click To Delete Kit")) {
						kit.disable();
						Manager.getManager().removeKit(kit);
						player.closeInventory();
						e.setCancelled(true);
					}
				}else if (e.getCurrentItem().getType() == Material.SPECTRAL_ARROW && e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("Next Page")) {
					e.setCancelled(true);
					Bukkit.getScheduler().scheduleSyncDelayedTask(Manager.getManager(), new Runnable() {
						public void run() {
							if (page == 0) {
								kit.removeAllItems();
								
								inventory.setItem(26, null);
								
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
										kit.addItem(new Item(i-18, inventory.getItem(i)));
									}
								}
								
								ItemStack netherStar = new ItemStack(Material.NETHER_STAR);
								ItemMeta meta = netherStar.getItemMeta();
								meta.setDisplayName("Drop To Leave");
								netherStar.setItemMeta(meta);
								kit.addItem(new Item(8, netherStar));
								
								kit.refreshArmorStand();
								
								inventory.clear();
								
								effectsPage();
								page++;
							}else if (page == 1) {
								inventory.clear();
								customScriptKitsPage();
								page++;
							}else if (page == 2) {
								inventory.clear();
								inventoryPage();
								page = 0;
							}
						}
					});
				}else {
					List<Material> ALM = Arrays.asList(materials);
					if (ALM.contains(e.getCurrentItem().getType()) && page == 1) {
						int step = ALM.indexOf(e.getCurrentItem().getType());
						List<Integer> effects = kit.getKitEffects();
						if (effects.get(step) > 3) {
							effects.set(step, 0);
						}else {
							effects.set(step, effects.get(step)+1);
						}
						e.setCancelled(true);
						Bukkit.getScheduler().scheduleSyncDelayedTask(Manager.getManager(), new Runnable() {
							public void run() {
								inventory.clear();
								effectsPage();
							}
						});
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onClose(InventoryCloseEvent e) {
		if (e.getInventory() == inventory) {
			if (page == 0) {
				kit.removeAllItems();
				
				inventory.setItem(26, null);
				
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
						kit.addItem(new Item(i-18, inventory.getItem(i)));
					}
				}
				
				ItemStack netherStar = new ItemStack(Material.NETHER_STAR);
				ItemMeta meta = netherStar.getItemMeta();
				meta.setDisplayName("Drop To Leave");
				netherStar.setItemMeta(meta);
				kit.addItem(new Item(8, netherStar));
				
				kit.refreshArmorStand();
			}
			HandlerList.unregisterAll(this);
		}
	}
	
	private void generateInventory() {
		inventory = Bukkit.createInventory(null, 54, "Kit Editor");
		inventoryPage();
	}
	
	private void inventoryPage() {
		for (int i=9;i<17;i++) {
			inventory.setItem(i, new ItemStack(Material.BARRIER));
		}
		ItemStack nextPageItem = new ItemStack(Material.SPECTRAL_ARROW);
		ItemMeta meta = nextPageItem.getItemMeta();
		meta.setDisplayName("Next Page");
		nextPageItem.setItemMeta(meta);
		inventory.setItem(17, nextPageItem);
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
		meta = netherStar.getItemMeta();
		meta.setDisplayName("Click To Delete Kit");
		netherStar.setItemMeta(meta);
		
		inventory.setItem(26, netherStar);
	}
	
	private void effectsPage() {
		ItemStack nextPageItem = new ItemStack(Material.SPECTRAL_ARROW);
		ItemMeta meta = nextPageItem.getItemMeta();
		meta.setDisplayName("Next Page");
		nextPageItem.setItemMeta(meta);
		inventory.setItem(8, nextPageItem);
		
		PotionEffectType[] effects = PotionEffectType.values();
		
		int step = 0;
		for (int y=1;y<6;y++) {
			for (int x=1;x<8;x++) {
				int i = y*9+x;
				if (step < effects.length) {
					ItemStack item = new ItemStack(materials[step]);
					meta = item.getItemMeta();
					meta.setDisplayName(effects[step].getName());
					List<String> list = new ArrayList<String>();
					list.add("Level: "+kit.getKitEffects().get(step));
					meta.setLore(list);
					item.setItemMeta(meta);
					inventory.setItem(i, item);
				}
				step++;
			}
		}
		
	}
	
	private void customScriptKitsPage() {
		ItemStack nextPageItem = new ItemStack(Material.SPECTRAL_ARROW);
		ItemMeta meta = nextPageItem.getItemMeta();
		meta.setDisplayName("Next Page");
		nextPageItem.setItemMeta(meta);
		inventory.setItem(8, nextPageItem);
		
		ItemStack comingSoon = new ItemStack(Material.BARRIER);
		meta = comingSoon.getItemMeta();
		meta.setDisplayName("Coming Soon");
		List<String> lore = new ArrayList<String>();
		lore.add("There will be scriptable kits here eventually!");
		meta.setLore(lore);
		comingSoon.setItemMeta(meta);
		inventory.setItem(31, comingSoon);
	}
	
}
