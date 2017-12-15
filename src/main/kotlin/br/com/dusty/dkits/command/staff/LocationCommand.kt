package br.com.dusty.dkits.command.staff

import br.com.dusty.dkits.command.PlayerCustomCommand
import br.com.dusty.dkits.gamer.EnumMode
import br.com.dusty.dkits.gamer.EnumRank
import br.com.dusty.dkits.util.gamer.gamer
import br.com.dusty.dkits.util.normalize
import br.com.dusty.dkits.util.text.Text
import br.com.dusty.dkits.warp.Warps
import org.bukkit.entity.Player

object LocationCommand: PlayerCustomCommand(EnumRank.ADMIN, "location") {

	override fun execute(sender: Player, alias: String, args: Array<String>): Boolean {
		val gamer = sender.gamer()

		if (args.size < 2) {
			sender.sendMessage(Text.negativePrefix().basic("Uso: /location ").negative("<set>/<goto> <nomeDaWarp>").basic(" [custom]").toString())
		} else {
			when (args[0].toLowerCase()) {
				"set"  -> {
					val warp = Warps[args[1]]

					when {
						warp == Warps.NONE                        -> sender.sendMessage(Text.negativePrefix().negative("Não").basic(" há uma warp chamada ").negative("\"" + args[1] + "\"").basic(
								"!").toString())
						warp in CUSTOM_EXECUTORS && args.size > 2 -> warp.execute(gamer, alias, args.copyOfRange(2, args.size))
						else                                      -> {
							warp.spawn = sender.location.normalize()

							sender.sendMessage(Text.positivePrefix().basic("Você ").positive("definiu").basic(" o spawn da warp ").positive(warp.name).basic("!").toString())
						}
					}
				}
				"goto" -> {
					if (gamer.mode != EnumMode.ADMIN) {
						sender.sendMessage(Text.negativePrefix().basic("Você ").negative("não").basic(" está no modo ").negative("ADMIN").basic("!").toString())
					} else {
						val warp = Warps[args[1]]

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
