package br.com.dusty.dkits.command.staff

import br.com.dusty.dkits.command.PlayerCustomCommand
import br.com.dusty.dkits.gamer.EnumMode
import br.com.dusty.dkits.gamer.EnumRank
import br.com.dusty.dkits.gamer.gamer
import br.com.dusty.dkits.util.text.Text
import br.com.dusty.dkits.warp.Warps
import org.bukkit.Location
import org.bukkit.entity.Player

object LocationCommand: PlayerCustomCommand(EnumRank.ADMIN, "location") {

	override fun execute(sender: Player, alias: String, args: Array<String>): Boolean {
		val gamer = sender.gamer()

		if (args.size < 2) {
			sender.sendMessage(Text.negativePrefix().basic("Uso: /location ").negative("<set/goto> <nomeDaWarp>").basic(" [custom]").toString())
		} else {
			when (args[0]) {
				"set"  -> {
					val warp = Warps[args[1]]

					//TODO: HG lobby and spawn per map; 1v1 individual player spawn in arena
					when (warp) {
						Warps.NONE -> sender.sendMessage(Text.negativePrefix().negative("Não").basic(" há uma warp chamada ").negative("\"" + args[1] + "\"").basic("!").toString())
						else       -> {
							warp.spawn = Location(sender.location.world, Math.floor(sender.location.x) + 0.5, Math.floor(sender.location.y) + 0.5, Math.floor(sender.location.z) + 0.5)

							sender.sendMessage(Text.positivePrefix().basic("Você ").positive("definiu").basic(" o spawn da warp ").positive(warp.name).basic("!").toString())
						}
					}
				}
				"goto" -> {
					if (gamer.mode != EnumMode.ADMIN) {
						sender.sendMessage(Text.negativePrefix().basic("Você ").negative("não").basic(" está no modo ").negative("ADMIN").basic("!").toString())
					} else {
						val warp = Warps[args[1]]

						//TODO: HG lobby and spawn per map; 1v1 individual player spawn in arena
						when (warp) {
							Warps.NONE -> sender.sendMessage(Text.negativePrefix().negative("Não").basic(" há uma warp chamada ").negative("\"" + args[1] + "\"").basic("!").toString())
							else       -> {
								sender.teleport(warp.spawn)

								sender.sendMessage(Text.positivePrefix().basic("Você foi ").positive("teleportado").basic(" para a warp ").positive(warp.name).basic("!").toString())
							}
						}
					}
				}
			}
		}

		return false
	}
}
