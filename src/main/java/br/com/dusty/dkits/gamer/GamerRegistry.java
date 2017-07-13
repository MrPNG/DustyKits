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
		Gamer gamer;
		
		if(!GAMER_BY_PLAYER.containsKey(player)){
			UUID uuid = player.getUniqueId();
			
			gamer = new Gamer(player, PRIMITIVE_GAMER_BY_UUID.get(uuid));
			
			PRIMITIVE_GAMER_BY_UUID.remove(uuid);
			GAMER_BY_PLAYER.put(player, gamer);
		}else{
			gamer = GAMER_BY_PLAYER.get(player);
		}
		
		return gamer;
	}
	
	public static void unregister(Gamer gamer) {
		GAMER_BY_PLAYER.remove(gamer.getPlayer());
	}
	
	public static Collection<Gamer> getOnlineGamers() {
		return GAMER_BY_PLAYER.values();
	}
	
	public static PrimitiveGamer fromJson(String json, UUID uuid) {
		if(json == null)
			return null;
		
		PrimitiveGamer primitiveGamer;
		
		if(json.equals("null")){
			primitiveGamer = new PrimitiveGamer();
			primitiveGamer.setUUID(uuid);
		}else{
			primitiveGamer = Main.GSON.fromJson(json, PrimitiveGamer.class);
		}
		
		PRIMITIVE_GAMER_BY_UUID.put(uuid, primitiveGamer);
		
		return primitiveGamer;
	}
	
	public static PrimitiveGamer getPrimitiveGamerbyUUID(UUID uuid) {
		return PRIMITIVE_GAMER_BY_UUID.get(uuid);
	}
}
