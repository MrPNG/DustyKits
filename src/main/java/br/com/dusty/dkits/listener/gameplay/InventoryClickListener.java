package br.com.dusty.dkits.listener.gameplay;

import br.com.dusty.dkits.gamer.Gamer;
import br.com.dusty.dkits.util.inventory.InventoryUtils;
import br.com.dusty.dkits.util.inventory.WarpMenu;
import br.com.dusty.dkits.warp.Warp;
import br.com.dusty.dkits.warp.Warps;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryClickListener implements Listener {
	
	private static final String TITLE_CONTAINER_INVENTORY = "container.inventory";
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		
		Gamer gamer = Gamer.of(player);
		
		if(gamer.getKit().isDummy())
			event.setCancelled(true);
		
		Inventory inventory = event.getClickedInventory();
		
		ItemStack itemStack = event.getCurrentItem();
		
		if(!inventory.getTitle().equals(TITLE_CONTAINER_INVENTORY)){
			
			//Warp menu - main
			if(inventory.getTitle().equals(WarpMenu.TITLE_MAIN)){
				if(itemStack.equals(WarpMenu.BUTTON_GAME))
					player.openInventory(WarpMenu.menuWarpGame(player));
				else if(itemStack.equals(WarpMenu.BUTTON_EVENT))
					player.openInventory(WarpMenu.menuWarpEvent(player));
				
				//Warp menu - game/event
			}else if(inventory.getTitle().equals(WarpMenu.TITLE_GAME) || inventory.getTitle().equals(WarpMenu.TITLE_EVENT)){
				if(itemStack.equals(InventoryUtils.BUTTON_BACK))
					player.openInventory(WarpMenu.menuWarpMain(player));
				else if(!itemStack.equals(InventoryUtils.BACKGROUND))
					for(Warp warp : Warps.WARPS)
						if(warp.getIcon().equals(itemStack)){
							gamer.sendToWarp(warp);
							return;
						}
			}
		}
	}
}
