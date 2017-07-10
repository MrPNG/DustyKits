package br.com.dusty.dkits.gamer;

import br.com.dusty.dkits.Main;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public class GamerRegistry {
	
	private static final HashMap<UUID, PrimitiveGamer> PRIMITIVE_GAMER_BY_UUID = new HashMap<>();
	private static final HashMap<Player, Gamer> GAMER_BY_PLAYER = new HashMap<>();
	
	static Gamer getGamerByPlayer(Player player) {
		Gamer gamer = null;
		
		if(!GAMER_BY_PLAYER.containsKey(player)){
			gamer = new Gamer(player);
			
			GAMER_BY_PLAYER.put(player, gamer);
		}else{
			gamer = GAMER_BY_PLAYER.get(player);
		}
		
		return gamer;
	}
	
	public static void unregister(Gamer gamer){
		GAMER_BY_PLAYER.remove(gamer.getPlayer());
	}
	
	public static Collection<Gamer> getOnlineGamers(){
		return GAMER_BY_PLAYER.values();
	}
	
	private static Gamer fromPrimitive(Player player, UUID uuid) {
		PrimitiveGamer primitiveGamer = PRIMITIVE_GAMER_BY_UUID.get(uuid);
		
		Gamer gamer = new Gamer(player);
		gamer.setKills(primitiveGamer.getKills());
		gamer.setDeaths(primitiveGamer.getDeaths());
		gamer.setKillStreak(primitiveGamer.getKillStreak());
		gamer.setMaxKillStreak(primitiveGamer.getMaxKillStreak());
		gamer.setXp(primitiveGamer.getXp());
		gamer.setMoney(primitiveGamer.getMoney());
		gamer.setHgWins(primitiveGamer.getHgWins());
		gamer.setHgLoses(primitiveGamer.getHgLoses());
		
		return gamer;
	}
	
	private static PrimitiveGamer fromJson(String json) {
		PrimitiveGamer primitiveGamer = Main.GSON.fromJson(json, PrimitiveGamer.class);
		PRIMITIVE_GAMER_BY_UUID.put(primitiveGamer.getUUID(), primitiveGamer);
		
		return primitiveGamer;
	}
}
