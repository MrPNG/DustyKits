package br.com.dusty.dkits.util.gamer

import br.com.dusty.dkits.gamer.Gamer
import org.bukkit.entity.Player

object TagUtils {

	fun applyTag(gamer: Gamer) {
		val player = gamer.player

		val displayName = gamer.rank.format(player.name)

		player.displayName = displayName
		player.playerListName = displayName
		//TODO: Tag above head
	}
}
