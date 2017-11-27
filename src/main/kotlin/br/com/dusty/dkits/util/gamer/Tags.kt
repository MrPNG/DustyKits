package br.com.dusty.dkits.util.gamer

import br.com.dusty.dkits.gamer.Gamer
import br.com.dusty.dkits.util.text.TextStyle

object Tags {

	fun applyTag(gamer: Gamer) {
		val player = gamer.player

		val displayName = gamer.rank.format(player.name) + TextStyle.RESET

		player.displayName = displayName
		player.playerListName = displayName
		//TODO: Tag above head
	}
}
