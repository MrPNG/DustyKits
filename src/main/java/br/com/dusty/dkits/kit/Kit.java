package br.com.dusty.dkits.kit;

import br.com.dusty.dkits.gamer.Gamer;
import br.com.dusty.dkits.util.InventoryUtils;
import br.com.dusty.dkits.util.gamer.GamerUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Kit {
	
	public String NAME = "None";
	public String DESCRIPTION = "";
	public ItemStack ICON = new ItemStack(Material.STONE_SWORD);
	
	public ItemStack WEAPON = null;
	public ItemStack[] ARMOR = {null, null, null, null};
	public ItemStack[] ITEMS = {};
	
	public Data DATA;
	
	//TODO: public static Ability ABILITY = null;
	
	public void apply(Gamer gamer) {
		GamerUtils.clear(gamer);
		
		Player player = gamer.getPlayer();
		
		player.getInventory().setItem(0, WEAPON);
		
		InventoryUtils.setArmor(player, ARMOR);
		InventoryUtils.addItemStacks(player.getInventory(), ITEMS);
	}
	
	public static class Data {
		
		public int PRICE = -1;
		public boolean ENABLED = false;
	}
}
