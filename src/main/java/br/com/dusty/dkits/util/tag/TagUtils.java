package br.com.dusty.dkits.util.tag;

import br.com.dusty.dkits.gamer.Gamer;
import org.bukkit.entity.Player;

public class TagUtils {
	
	public static void applyTag(Gamer gamer) {
		Player player = gamer.getPlayer();
		
		String displayName = gamer.getRank().format(player.getName());
		
		player.setDisplayName(displayName);
		player.setPlayerListName(displayName);
		//TODO: Tag above head
	}
}
