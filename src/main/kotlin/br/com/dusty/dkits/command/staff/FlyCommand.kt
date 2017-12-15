package br.com.dusty.dkits.command.staff

import br.com.dusty.dkits.command.PlayerCustomCommand
import br.com.dusty.dkits.gamer.EnumMode
import br.com.dusty.dkits.gamer.EnumRank
import br.com.dusty.dkits.util.gamer.gamer
import br.com.dusty.dkits.util.text.Text
import org.bukkit.Bukkit
import org.bukkit.entity.Player

object FlyCommand: PlayerCustomCommand(EnumRank.MODPLUS, "fly") {

	override fun execute(sender: Player, alias: String, args: Array<String>): Boolean {
		val gamer = sender.gamer()

		when {
			gamer.mode != EnumMode.ADMIN -> sender.sendMessage(Text.negativePrefix().basic("Você ").negative("não").basic(" está no modo ").negative("ADMIN").basic("!").toString())
			args.isEmpty()               -> {
				val fly = !sender.allowFlight

				gamer.fly(fly)

				if (fly) sender.sendMessage(Text.positivePrefix().basic("Agora você ").positive("está").basic(" voando!").toString())
				else sender.sendMessage(Text.negativePrefix().basic("Agora você ").negative("não").basic(" está mais voando!").toString())
			}
			else                         -> {
				val player = Bukkit.getPlayerExact(args[0])

				if (player == null) {
					sender.sendMessage(Text.negativePrefix().negative("Não").basic(" há um jogador online com o nome \"").negative(args[0]).basic("\"!").toString())
				} else {
					val fly = !player.allowFlight

					player.gamer().fly(fly)

					if (fly) sender.sendMessage(Text.positivePrefix().basic("Agora o jogador ").positive(player.name).basic(" está voando!").toString())
					else sender.sendMessage(Text.negativePrefix().basic("Agora o jogador ").negative(player.name).basic(" não está mais voando!").toString())
				}
			}
		}

		return false
	}
}
