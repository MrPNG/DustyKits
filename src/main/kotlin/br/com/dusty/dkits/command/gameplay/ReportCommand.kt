package br.com.dusty.dkits.command.gameplay

import br.com.dusty.dkits.command.PlayerCustomCommand
import br.com.dusty.dkits.gamer.EnumRank
import br.com.dusty.dkits.gamer.GamerRegistry
import br.com.dusty.dkits.util.Tasks
import br.com.dusty.dkits.util.text.Text
import br.com.dusty.dkits.util.web.WebAPI
import com.google.common.collect.HashMultimap
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.entity.Player
import java.util.*

object ReportCommand: PlayerCustomCommand(EnumRank.DEFAULT, "dustyreport") {

	val COMPLETIONS = arrayListOf("ff", "forcefield", "killaura", "sneak", "fly", "flight", "speed", "velocity", "reach", "gc", "ghostclient", "spider")

	val REPORTS_BY_UUID = HashMultimap.create<UUID, UUID>()

	override fun execute(sender: Player, alias: String, args: Array<String>): Boolean {
		if (args.size < 2) {
			sender.sendMessage(Text.negativePrefix().basic("Uso: /report ").negative("<jogador> <motivo>").toString())
		} else {
			val player = Bukkit.getPlayerExact(args[0])

			when {
				player == null                                                  -> sender.sendMessage(Text.negativePrefix().negative("Não").basic(" há um jogador online com o nome \"").negative(
						args[0]).basic("\"!").toString())
				REPORTS_BY_UUID.containsEntry(sender.uniqueId,
				                              player.uniqueId)                  -> sender.sendMessage(Text.negativePrefix().basic("Você ").negative("já").basic(" reportou o jogador ").negative(
						player.name).basic("!").toString())
				else                                                            -> {
					val reason = args.copyOfRange(1, args.size).joinToString(separator = " ")

					sender.sendMessage(Text.positivePrefix().basic("Você ").positive("reportou").basic(" o jogador ").positive(player.name).basic(" por ").positive(reason).basic("!").toString())

					GamerRegistry.onlineGamers().forEach {
						if (it.rank.isHigherThanOrEquals(EnumRank.MOD)) {
							val staffPlayer = it.player

							staffPlayer.playSound(staffPlayer.location, Sound.CHICKEN_HURT, 1F, 1F)
							staffPlayer.sendMessage(Text.negativePrefix().basic("[").negative("Report").basic("]: \n").negative("  Jogador").basic(": " + player.name + "\n").negative("  Motivo").basic(
									": " + reason + "\n").negative("  Por").basic(": " + sender.name).toString())
						}
					}

					REPORTS_BY_UUID.put(sender.uniqueId, player.uniqueId)

					Tasks.sync(Runnable { REPORTS_BY_UUID.remove(sender.uniqueId, player.uniqueId) }, 1200L)

					Tasks.async(Runnable { println(WebAPI.report(player.name, sender.name, reason)) })
				}
			}
		}

		return false
	}

	override fun tabComplete(sender: Player, alias: String, args: Array<String>) = if (args.isEmpty()) Bukkit.getOnlinePlayers().filter { sender.canSee(it) }.map { it.name }.toMutableList()
	else COMPLETIONS.filter { it.startsWith(args.last(), true) }.toMutableList()
}
