package br.com.dusty.dkits.kit;

import br.com.dusty.dkits.util.ItemStackUtils;
import br.com.dusty.dkits.util.text.Text;
import br.com.dusty.dkits.util.text.TextColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class PvpKit extends Kit {
	
	{
		NAME = "PvP";
		
		ItemStackUtils.rename(ICON, Text.of(NAME).color(TextColor.GOLD).toString());
		ItemStackUtils.setDescription(ICON, DESCRIPTION);
		
		WEAPON = new ItemStack(Material.STONE_SWORD);
		ARMOR = new ItemStack[]{null, new ItemStack(Material.LEATHER_CHESTPLATE), null, null};
		
		DUMMY = false;
		BROADCAST = true;
		
		loadData();
	}
}
