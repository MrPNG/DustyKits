package br.com.dusty.dkits.listener.gameplay;

import br.com.dusty.dkits.util.block.SignUtils;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractListener implements Listener {
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		
		if(event.getClickedBlock() != null){
			Block block = event.getClickedBlock();
			
			switch(block.getType()){
				case WALL_SIGN:
					Sign sign = (Sign) block.getState();
					
					if(SignUtils.isSpecialSign(sign))
						SignUtils.doStuff(sign, player);
					
					break;
			}
		}
	}
}
