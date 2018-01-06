package br.com.dusty.dkits.listener.mechanics

import br.com.dusty.dkits.command.Commands
import br.com.dusty.dkits.gamer.EnumMode
import br.com.dusty.dkits.gamer.GamerRegistry
import br.com.dusty.dkits.util.entity.gamer
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerChatTabCompleteEvent

object PlayerChatTabCompleteListener: Listener {

	@EventHandler
	fun onPlayerChatTabComplete(event: PlayerChatTabCompleteEvent) {
		val player = event.player
		val gamer = player.gamer()

		if (gamer.warp.overrides(event)) return

		val tabCompletions = event.tabCompletions

		if (event.chatMessage.startsWith("/")) {
			if (gamer.mode != EnumMode.ADMIN) tabCompletions.run {
				clear()
				addAll(Commands.CUSTOM_COMMANDS.asSequence().filter { it.rank.isLowerThanOrEquals(gamer.rank) }.map { it.label })
			}
		} else GamerRegistry.onlineGamers().filter { it.displayName != it.player.name }.forEach {
			tabCompletions.run {
				remove(it.player.name)
				add(it.displayName)
			}
		}
	}
}
