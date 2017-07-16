package br.com.dusty.dkits.listener.gameplay;

import br.com.dusty.dkits.util.ItemStackUtils;
import br.com.dusty.dkits.util.block.SignUtils;
import br.com.dusty.dkits.warp.LobbyWarp;
import br.com.dusty.dkits.warp.Warps;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerInteractListener implements Listener {
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
			Block block = event.getClickedBlock();
			
			switch(block.getType()){
				case WALL_SIGN:
					Sign sign = (Sign) block.getState();
					
					if(SignUtils.isSpecialSign(sign))
						SignUtils.doStuff(sign, player);
					
					break;
			}
		}
		
		if(event.getItem() != null){
			ItemStack itemStack = event.getItem();
			
			switch(itemStack.getType()){
				case EMPTY_MAP:
					if(itemStack.equals(Warps.LOBBY.getEntryKit().getItems()[0])){ //TODO: Open warp menu
						event.setCancelled(true);
					}
					
					break;
			}
		}
	}
}
