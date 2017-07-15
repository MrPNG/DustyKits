package br.com.dusty.dkits.util.gamer;

import br.com.dusty.dkits.gamer.Gamer;
import br.com.dusty.dkits.util.ScoreboardUtils;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

public class GamerUtils {
	
	public static void clear(Gamer gamer) {
		Player player = gamer.getPlayer();
		
		player.setHealth(20);
		player.setFoodLevel(20);
		player.setExp(0);
		player.setLevel(0);
		
		player.getInventory().clear();
		
		for(PotionEffect potionEffect : player.getActivePotionEffects())
			player.removePotionEffect(potionEffect.getType());
	}
	
	public static void fly(Gamer gamer, boolean fly) {
		Player player = gamer.getPlayer();
		
		player.setAllowFlight(fly);
		player.setFlying(fly);
	}
}
