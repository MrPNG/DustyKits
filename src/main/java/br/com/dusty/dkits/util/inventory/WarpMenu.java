package br.com.dusty.dkits.util.inventory;

import br.com.dusty.dkits.util.ItemStackUtils;
import br.com.dusty.dkits.util.text.Text;
import br.com.dusty.dkits.util.text.TextColor;
import br.com.dusty.dkits.warp.Warp;
import br.com.dusty.dkits.warp.Warps;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class WarpMenu {
	
	public static final String TITLE_MAIN = Text.of("Warps").color(TextColor.GOLD).toString();
	public static final String TITLE_GAME = Text.of("Warps de Jogo").color(TextColor.GOLD).toString();
	public static final String TITLE_EVENT = Text.of("Warps de Evento").color(TextColor.GOLD).toString();
	
	public static final ItemStack BUTTON_GAME = ItemStackUtils.rename(new ItemStack(Material.DIAMOND_SWORD), TITLE_GAME);
	public static final ItemStack BUTTON_EVENT = ItemStackUtils.rename(new ItemStack(Material.CAKE), TITLE_EVENT);
	
	public static Inventory menuWarpMain(Player player) {
		Inventory inventory = Bukkit.createInventory(player, 27, TITLE_MAIN);
		
		InventoryUtils.basic(inventory, false);
		
		inventory.setItem(11, BUTTON_GAME);
		inventory.setItem(15, BUTTON_EVENT);
		
		return inventory;
	}
	
	public static Inventory menuWarpGame(Player player) {
		Inventory inventory = Bukkit.createInventory(player, 27, TITLE_GAME);
		
		InventoryUtils.basic(inventory, true);
		
		ArrayList<Warp> warps = new ArrayList<>();
		
		for(Warp warp : Warps.INSTANCE.getWARPS())
			if(warp.getWarpType() == Warp.EnumWarpType.GAME && warp.getData().isEnabled())
				warps.add(warp);
		
		if(warps.size() > 0)
			switch(warps.size()){
				case 1:
					inventory.setItem(13, warps.get(0).getIcon());
					
					break;
				case 2:
					for(int i = 0; i < 2; i++)
						inventory.setItem(12 + (i * 2), warps.get(i).getIcon());
					
					break;
				case 3:
					for(int i = 0; i < 3; i++)
						inventory.setItem(11 + (i * 2), warps.get(i).getIcon());
					
					break;
				case 4:
					for(int i = 0; i < 4; i++)
						inventory.setItem(10 + (i * 2), warps.get(i).getIcon());
					
					break;
				case 5:
					for(int i = 0; i < 5; i++)
						inventory.setItem(13 + i, warps.get(i).getIcon());
					
					break;
				default:
					for(int i = 0; i < 5; i++)
						inventory.setItem(10 + i, warps.get(i).getIcon());
			}
		
		return inventory;
	}
	
	public static Inventory menuWarpEvent(Player player) {
		Inventory inventory = Bukkit.createInventory(player, 27, TITLE_EVENT);
		
		InventoryUtils.basic(inventory, true);
		
		ArrayList<Warp> warps = new ArrayList<>();
		
		for(Warp warp : Warps.INSTANCE.getWARPS())
			if(warp.getWarpType() == Warp.EnumWarpType.EVENT && warp.getData().isEnabled())
				warps.add(warp);
		
		if(warps.size() > 0)
			switch(warps.size()){
				case 1:
					inventory.setItem(13, warps.get(0).getIcon());
					
					break;
				case 2:
					for(int i = 0; i < 2; i++)
						inventory.setItem(12 + (i * 2), warps.get(i).getIcon());
					
					break;
				case 3:
					for(int i = 0; i < 3; i++)
						inventory.setItem(11 + (i * 2), warps.get(i).getIcon());
					
					break;
				case 4:
					for(int i = 0; i < 4; i++)
						inventory.setItem(10 + (i * 2), warps.get(i).getIcon());
					
					break;
				case 5:
					for(int i = 0; i < 5; i++)
						inventory.setItem(13 + i, warps.get(i).getIcon());
					
					break;
				default:
					for(int i = 0; i < 5; i++)
						inventory.setItem(10 + i, warps.get(i).getIcon());
			}
		
		return inventory;
	}
}
