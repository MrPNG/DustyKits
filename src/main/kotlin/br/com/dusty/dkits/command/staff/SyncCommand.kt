package br.com.dusty.dkits.command.staff

import br.com.dusty.dkits.command.PlayerCustomCommand
import br.com.dusty.dkits.gamer.EnumRank
import br.com.dusty.dkits.gamer.GamerRegistry
import br.com.dusty.dkits.util.gamer.gamer
import br.com.dusty.dkits.util.Tasks
import br.com.dusty.dkits.util.text.Text
import br.com.dusty.dkits.util.web.WebAPI
import org.bukkit.Bukkit
import org.bukkit.entity.Player

object SyncCommand: PlayerCustomCommand(EnumRank.ADMIN, "sync") {

	override fun execute(sender: Player, alias: String, args: Array<String>): Boolean {
		if (args.isEmpty()) {
			Tasks.async(Runnable { Bukkit.getLogger().info("Web API: " + WebAPI.saveProfiles(*GamerRegistry.onlineGamers().toTypedArray())) })

			sender.sendMessage(Text.negativePrefix().basic("Os ").negative("perfis").basic(" de ").negative("todos").basic(" os jogadores serão ").negative("sincronizados").basic("!").toString())
		} else {
			val player = Bukkit.getPlayerExact(args[0])

			if (player == null) {
				sender.sendMessage(Text.negativePrefix().negative("Não").basic(" há um jogador online com o nome \"").negative(args[0]).basic("\"!").toString())
			} else {
				val gamer = player.gamer()

				Tasks.async(Runnable { Bukkit.getLogger().info("Web API: " + WebAPI.saveProfiles(gamer)) })

				sender.sendMessage(Text.negativePrefix().basic("O ").negative("perfil").basic(" do jogador ").negative(player.name).basic(" será ").negative("sincronizado").basic("!").toString())
			}
		}

		return false
	}
}
