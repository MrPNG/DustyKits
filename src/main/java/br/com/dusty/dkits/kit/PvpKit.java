package br.com.dusty.dkits.kit;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class PvpKit extends Kit {
	
	{
		NAME = "PvP";
		
		WEAPON = new ItemStack(Material.STONE_SWORD);
		ARMOR = new ItemStack[]{null, new ItemStack(Material.LEATHER_CHESTPLATE), null, null};
		
		BROADCAST = true;
		
		loadData();
	}
}
