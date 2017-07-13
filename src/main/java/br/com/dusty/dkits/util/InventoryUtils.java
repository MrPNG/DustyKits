package br.com.dusty.dkits.util;

import br.com.dusty.dkits.util.text.Text;
import br.com.dusty.dkits.util.text.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class InventoryUtils {
	
	public static final ItemStack SOUP = new ItemStack(Material.MUSHROOM_SOUP);
	
	public static final ItemStack RED_MUSHROOMS = new ItemStack(Material.RED_MUSHROOM, 64);
	public static final ItemStack BROWN_MUSHROOMS = new ItemStack(Material.BROWN_MUSHROOM, 64);
	public static final ItemStack BOWLS = new ItemStack(Material.BOWL, 64);
	
	private static final String SOUPS_TITLE = Text.of("Sopas").color(TextColor.GOLD).toString();
	private static final String RECRAFT_TITLE = Text.of("Recraft").color(TextColor.GOLD).toString();
	
	private static final ItemStack[] ARMOR_FULLIRON = new ItemStack[]{new ItemStack(Material.IRON_HELMET),
	                                                                  new ItemStack(Material.IRON_CHESTPLATE),
	                                                                  new ItemStack(Material.IRON_LEGGINGS),
	                                                                  new ItemStack(Material.IRON_BOOTS)};
	
	public static Inventory soups(Player player) {
		Inventory inventory = Bukkit.createInventory(player, 54, SOUPS_TITLE);
		
		for(int i = 0; i < 54; i++){
			inventory.setItem(i, SOUP);
		}
		
		return inventory;
	}
	
	public static Inventory recraft(Player player) {
		Inventory inventory = Bukkit.createInventory(player, 54, RECRAFT_TITLE);
		
		for(int i = 0; i < 18; i++){
			inventory.setItem(i * 3, RED_MUSHROOMS);
		}
		
		for(int i = 0; i < 18; i++){
			inventory.setItem((i * 3) + 1, BROWN_MUSHROOMS);
		}
		
		for(int i = 0; i < 18; i++){
			inventory.setItem((i * 3) + 2, BOWLS);
		}
		
		return inventory;
	}
	
	public static void addItemStacks(Inventory inventory, ItemStack[] itemStacks) {
		int i = 0;
		
		while(inventory.getItem(i) != null)
			i++;
		
		for(ItemStack itemStack : itemStacks)
			inventory.setItem(i++, itemStack);
	}
	
	/**
	 * Aplica um {@link ItemStack}[] contendo, nessa ordem, 'helmet', 'chestplate', 'leggings' e 'boots' como armadura de um {@link Player}.
	 *
	 * @param player
	 * @param itemStacks
	 */
	public static void setArmor(Player player, ItemStack[] itemStacks) {
		PlayerInventory playerInventory = player.getInventory();
		playerInventory.setHelmet(itemStacks[0]);
		playerInventory.setChestplate(itemStacks[1]);
		playerInventory.setLeggings(itemStacks[2]);
		playerInventory.setBoots(itemStacks[3]);
	}
}
