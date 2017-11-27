package br.com.dusty.dkits.command.staff

import br.com.dusty.dkits.command.PlayerCustomCommand
import br.com.dusty.dkits.gamer.EnumRank
import br.com.dusty.dkits.gamer.GamerRegistry
import br.com.dusty.dkits.util.text.Text
import br.com.dusty.dkits.util.web.WebAPI
import org.bukkit.entity.Player
import java.util.*

object StopCommand: PlayerCustomCommand(EnumRank.ADMIN, "stop") {

	private val KICK_SHUTDOWN = Text.negativeOf("O servidor está reiniciando!\n\nVolte em alguns segundos...").toString()

	override fun execute(sender: Player, alias: String, args: Array<String>): Boolean {
		val gamers = HashSet(GamerRegistry.onlineGamers())

		gamers.forEach { gamer -> gamer.player.kickPlayer(KICK_SHUTDOWN) }

		WebAPI.saveProfiles(*gamers.toTypedArray())

		return false
	}
}
