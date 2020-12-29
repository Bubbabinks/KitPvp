package net.JBStudios.KitPvp;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;

public class Item extends ItemStack {
	
	private int slot = 0;
	
	public Item(int slot, ItemStack itemStack) {
		super(itemStack);
		this.slot = slot;
	}
	
	public Item(String save) {
		super();
		ArrayList<String> saves = new ArrayList<String>();
		while (save.indexOf('+') != -1) {
			saves.add(save.substring(0,save.indexOf('+')));
			save = save.substring(save.indexOf('+')+1,save.length());
		}
		saves.add(save);
		for (String s: saves) {
			String key = s.split("-")[0];
			String value = s.split("-")[1];
			if (key.equalsIgnoreCase("material")) {
				setType(Material.getMaterial(value));
			}else if (key.equalsIgnoreCase("name")) {
				ItemMeta itemMeta = getItemMeta();
				itemMeta.setDisplayName(value);
				setItemMeta(itemMeta);
			}else if (key.equalsIgnoreCase("lore")) {
				ItemMeta itemMeta = getItemMeta();
				itemMeta.getLore().add(value);
				setItemMeta(itemMeta);
			}else if (key.equalsIgnoreCase("amount")) {
				setAmount(Integer.parseInt(value));
			}else if (key.equalsIgnoreCase("slot")) {
				slot = Integer.parseInt(value);
			}else if (key.equalsIgnoreCase("enchantment")) {
				String enchantName = value.split(",")[0];
				int level = Integer.parseInt(value.split(",")[1]);
				addEnchantment(Enchantment.getByKey(NamespacedKey.minecraft(enchantName)), level);
			}else if (key.equalsIgnoreCase("potionEffect")) {
				PotionMeta meta = (PotionMeta)getItemMeta();
				meta.setBasePotionData(new PotionData(PotionType.valueOf(value)));
				setItemMeta(meta);
			}else if (key.equalsIgnoreCase("potionExtended")) {
				PotionMeta meta = (PotionMeta)getItemMeta();
				PotionData data = meta.getBasePotionData();
				meta.setBasePotionData(new PotionData(data.getType(), Boolean.parseBoolean(value), false));
				setItemMeta(meta);
			}else if (key.equalsIgnoreCase("potionUpgraded")) {
				PotionMeta meta = (PotionMeta)getItemMeta();
				PotionData data = meta.getBasePotionData();
				meta.setBasePotionData(new PotionData(data.getType(), data.isExtended(), Boolean.parseBoolean(value)));
				setItemMeta(meta);
			}
		}
	}
	
	public int getSlot() {
		return slot;
	}
	
	public void setSlot(int slot) {
		this.slot = slot;
	}
	
	public String toString() {
		String save = "";
		Map<Enchantment, Integer> enchantments = getEnchantments();
		int amount = getAmount();
		
		save += "material-"+getType()+"+";
		try {
			String name = getItemMeta().getDisplayName();
			if (name != null && name != "") {
				save += "name-"+name+"+";
			}
		}catch (Exception e) {}
		try {
			List<String> lore = getItemMeta().getLore();
			if (lore != null) {
				for (String l: lore) {
					save += "lore-"+l+"+";
				}
			}
		}catch (Exception e) {}
		save += "amount-"+amount+"+";
		save += "slot-"+slot+"+";
		for (Enchantment enchant: enchantments.keySet()) {
			save += "enchantment-"+enchant.getKey().getKey()+","+enchantments.get(enchant)+"+";
		}
		if (getType() == Material.POTION || getType() == Material.SPLASH_POTION || getType() == Material.LINGERING_POTION) {
			PotionMeta meta = (PotionMeta) getItemMeta();
			save += "potionEffect-"+meta.getBasePotionData().getType().name()+"+";
			save += "potionExtended-"+meta.getBasePotionData().isExtended()+"+";
			save += "potionUpgraded-"+meta.getBasePotionData().isUpgraded()+"+";
		}
		save = save.substring(0, save.length()-1);
		return save;
	}
}
