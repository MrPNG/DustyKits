package br.com.dusty.dkits.warp;

import br.com.dusty.dkits.util.ItemStackUtils;
import br.com.dusty.dkits.util.text.Text;
import br.com.dusty.dkits.util.text.TextColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ArenaWarp extends Warp {
	
	{
		NAME = "Arena";
		ICON = new ItemStack(Material.STONE_SWORD);
		
		ItemStackUtils.rename(ICON, Text.of(NAME).color(TextColor.GOLD).toString());
		ItemStackUtils.setDescription(ICON, DESCRIPTION);
		
		ENTRY_KIT = GAME_WARP_KIT;
		
		DATA.setSpreadRange(4);
		
		loadData();
	}
}
