package net.jbstudios.kitpvp;

import org.bukkit.inventory.ItemStack;

public class Item extends ItemStack {
	
	private int slot = 0;
	
	public Item(int slot, ItemStack itemStack) {
		super(itemStack);
		this.slot = slot;
	}
	
	public int getSlot() {
		return slot;
	}
	
	public void setSlot(int slot) {
		this.slot = slot;
	}
}
