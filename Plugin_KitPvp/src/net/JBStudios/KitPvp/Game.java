package net.JBStudios.KitPvp;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;

import net.JBStudios.KitPvp.GameData.PropKeys;

public class Game implements Listener {

	private GameData gameData;
	
	private ArrayList<Player> players;
	
	public Game(GameData gameData) {
		this.gameData = gameData;
		players = new ArrayList<Player>();
		Bukkit.getPluginManager().registerEvents(this, Manager.getManager());
	}
	
	public void addPlayer(Player player, Kit kit) {
		if (gameData.getProperty(PropKeys.Lobby) == null) {
			player.sendMessage(ChatColor.BLUE+""+ChatColor.BOLD+gameData.getProperty(PropKeys.Name)+ChatColor.RESET+": "+ChatColor.RED+"Game is missing a lobby!");
			return;
		}
		if (gameData.getProperty(PropKeys.WorldSpawn) == null) {
			player.sendMessage(ChatColor.BLUE+""+ChatColor.BOLD+gameData.getProperty(PropKeys.Name)+ChatColor.RESET+": "+ChatColor.RED+"Game is missing a world spawn be sure to set it with the /kp command!");
			return;
		}
		if (gameData.getSpawns().size() < 2) {
			player.sendMessage(ChatColor.BLUE+""+ChatColor.BOLD+gameData.getProperty(PropKeys.Name)+ChatColor.RESET+": "+ChatColor.RED+"Game needs more spawns!");
			return;
		}
		
		PlayerInventory inventory = player.getInventory();
		inventory.clear();
		for (Item item: kit.getItems()) {
			if (item.getSlot() > 26 && item.getSlot() < 36) {
				inventory.setItem(item.getSlot()-27, item);
			}else if (item.getSlot() > -1 && item.getSlot() < 27) {
				inventory.setItem(item.getSlot()+9, item);
			}else if (item.getSlot() == 99) {
				inventory.setItemInOffHand(item);
			}else if (item.getSlot() == 100) {
				inventory.setHelmet(item);
			}else if (item.getSlot() == 101) {
				inventory.setChestplate(item);
			}else if (item.getSlot() == 102) {
				inventory.setLeggings(item);
			}else if (item.getSlot() == 103) {
				inventory.setBoots(item);
			}
		}
		
		player.teleport(gameData.getSpawns().get((int)(Math.random()*((double)gameData.getSpawns().size()))));
		players.add(player);
	}
	
	@EventHandler
	public void onPlayerDropItem(PlayerDropItemEvent e) {
		if (players.contains(e.getPlayer())) {
			if (e.getItemDrop().getItemStack().getItemMeta().getDisplayName().equalsIgnoreCase("Drop To Leave")) {
				playerLeaveGame(e.getPlayer());
			}
			e.getItemDrop().remove();
		}
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		if (players.contains(e.getEntity())) {
			Player player = e.getEntity();
			e.getDrops().clear();
			Bukkit.getScheduler().scheduleSyncDelayedTask(Manager.getManager(), new Runnable() {
				public void run() {
					player.spigot().respawn();
					playerLeaveGame(player);
				}
			});
		}
	}
	
	private void playerLeaveGame(Player player) {
		player.getInventory().clear();
		for (PotionEffect effect: player.getActivePotionEffects()) {
			player.removePotionEffect(effect.getType());
		}
		player.setHealth(20);
		player.setFoodLevel(20);
		player.teleport(new Coord(gameData.getProperty(PropKeys.Lobby)));
		players.remove(player);
	}
	
	public void disable() {
		for (Player player: players) {
			player.getInventory().clear();
			for (PotionEffect effect: player.getActivePotionEffects()) {
				player.removePotionEffect(effect.getType());
			}
			player.teleport(new Coord(gameData.getProperty(PropKeys.WorldSpawn)));
			
		}
		players.clear();
		HandlerList.unregisterAll(this);
	}
	
}
