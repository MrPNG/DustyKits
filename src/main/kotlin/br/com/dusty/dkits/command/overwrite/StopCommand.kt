package br.com.dusty.dkits.command.overwrite

import br.com.dusty.dkits.command.CustomCommand
import br.com.dusty.dkits.gamer.EnumRank
import br.com.dusty.dkits.gamer.GamerRegistry
import br.com.dusty.dkits.util.text.Text
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender

object StopCommand: CustomCommand(EnumRank.ADMIN, "stop") {

	private val KICK_SHUTDOWN = Text.negativeOf("O servidor está reiniciando!\n\nVolte em alguns segundos...").toString()

	override fun execute(sender: CommandSender, alias: String, args: Array<String>): Boolean {
		if (testPermission(sender)) {
			val gamers = GamerRegistry.onlineGamers()

			gamers.forEach { gamer -> gamer.player.kickPlayer(KICK_SHUTDOWN) }

			//TODO: Reactivate Web API
//			WebAPI.saveProfiles(*gamers.toTypedArray())

			Bukkit.shutdown()
		}

		return false
	}
}
