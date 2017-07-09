package br.com.dusty.dkits.gamer;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class GamerRegistry {
	
	private static final HashMap<UUID, PrimitiveGamer> PRIMITIVE_GAMER_BY_UUID = new HashMap<>();
	private static final HashMap<Player, Gamer> GAMER_BY_PLAYER = new HashMap<>();
	
	static Gamer getGamerByPlayer(Player player){
		Gamer gamer = null;
		
		if(!GAMER_BY_PLAYER.containsKey(player)){
			gamer = new Gamer(player);
			
			GAMER_BY_PLAYER.put(player, gamer);
		}else{
			gamer = GAMER_BY_PLAYER.get(player);
		}
		
		return gamer;
	}
}
