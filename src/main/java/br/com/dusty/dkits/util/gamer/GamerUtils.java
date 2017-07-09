package br.com.dusty.dkits.util.gamer;

import br.com.dusty.dkits.gamer.Gamer;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

public class GamerUtils {
	
	public static void clear(Gamer gamer) {
		//TODO: Remove kit
		
		Player player = gamer.getPlayer();
		
		player.getInventory().clear();
		for(PotionEffect potionEffect : player.getActivePotionEffects())
			player.removePotionEffect(potionEffect.getType());
	}
	
	public static void flight(Gamer gamer, boolean flight) {
		Player player = gamer.getPlayer();
		
		player.setAllowFlight(flight);
		player.setFlying(flight);
	}
}
