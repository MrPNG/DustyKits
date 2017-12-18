package br.com.dusty.dkits.util

import br.com.dusty.dkits.gamer.Gamer
import br.com.dusty.dkits.gamer.GamerRegistry
import br.com.dusty.dkits.util.protocol.EnumProtocolVersion
import br.com.dusty.dkits.util.text.Text
import br.com.dusty.dkits.util.text.TextColor
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.scoreboard.DisplaySlot
import org.bukkit.scoreboard.Scoreboard

object Scoreboards {

	val LABELS = arrayOf(Text.of("XP: ").color(TextColor.GOLD).toString(),
	                     Text.of("Créditos: ").color(TextColor.GOLD).toString(),
	                     Text.of("------------").color(TextColor.YELLOW).toString(),
	                     Text.of("Kills: ").color(TextColor.RED).toString(),
	                     Text.of("Deaths: ").color(TextColor.RED).toString(),
	                     Text.of("Killstreak: ").color(TextColor.RED).toString(),
	                     Text.of("------------ ").color(TextColor.YELLOW).toString(),
	                     Text.of("Kit: ").color(TextColor.AQUA).toString(),
	                     Text.of("Combate: ").color(TextColor.AQUA).toString())
//	                     Text.of("Players: ").color(TextColor.AQUA).toString())

	val LABELS_OLD = arrayOf(ChatColor.GOLD.toString() + "XP: ",
	                         ChatColor.GOLD.toString() + "$: ",
	                         ChatColor.YELLOW.toString() + "=-=-=-=-=-=-",
	                         ChatColor.RED.toString() + "Kills: ",
	                         ChatColor.RED.toString() + "Deaths: ",
	                         ChatColor.RED.toString() + "KS: ",
	                         ChatColor.YELLOW.toString() + "-=-=-=-=-=-=",
	                         ChatColor.AQUA.toString() + "Kit: ",
	                         ChatColor.AQUA.toString() + "Combate: ")
//	                         ChatColor.AQUA.toString() + "Players: ")

	fun create(player: Player) {
		val scoreboard = Bukkit.getScoreboardManager().newScoreboard
		player.scoreboard = scoreboard

		val objective = scoreboard.registerNewObjective(player.name, "dummy")
		objective.displayName = player.displayName
		objective.displaySlot = DisplaySlot.SIDEBAR
	}

	fun update(gamer: Gamer) {
		val player = gamer.player

		val scoreboard = player.scoreboard ?: return

		clear(scoreboard)

		val objective = scoreboard.getObjective(player.name) ?: return

		if (gamer.protocolVersion.isGreaterThanOrEquals(EnumProtocolVersion.RELEASE_1_8)) {
			val values = arrayOf(Text.of(Math.round(gamer.xp).toInt()).color(TextColor.YELLOW).toString(),
			                     Text.of(Math.round(gamer.money).toInt()).color(TextColor.YELLOW).toString(),
			                     "",
			                     Text.of(gamer.kills).color(TextColor.YELLOW).toString(),
			                     Text.of(gamer.deaths).color(TextColor.YELLOW).toString(),
			                     Text.of(gamer.killStreak).color(TextColor.YELLOW).toString(),
			                     "",
			                     Text.of(gamer.kit.name).color(TextColor.YELLOW).toString(),
			                     Text.of(if (gamer.isCombatTagged()) "Sim" else "Não").color(TextColor.YELLOW).toString())
//			                     Text.of(GamerRegistry.onlineGamers().filter { it.mode != EnumMode.ADMIN }.size).color(TextColor.YELLOW).toString())

			LABELS.indices.filterNot { it == 7 && !gamer.kit.isBroadcast }.forEach { objective.getScore(LABELS[it] + values[it])?.run { score = LABELS.size - it } }
		} else {
			val values = arrayOf(ChatColor.YELLOW.toString() + Math.round(gamer.xp),
			                     ChatColor.YELLOW.toString() + Math.round(gamer.money),
			                     "",
			                     ChatColor.YELLOW.toString() + gamer.kills,
			                     ChatColor.YELLOW.toString() + gamer.deaths,
			                     ChatColor.YELLOW.toString() + gamer.killStreak,
			                     "",
			                     ChatColor.YELLOW.toString() + gamer.kit.name,
			                     ChatColor.YELLOW.toString() + if (gamer.isCombatTagged()) "Sim" else "Não")
//			                     ChatColor.YELLOW.toString() + GamerRegistry.onlineGamers().filter { it.mode != EnumMode.ADMIN }.size)

			LABELS_OLD.indices.filterNot { it == 7 && !gamer.kit.isBroadcast }.forEach { objective.getScore(LABELS_OLD[it] + values[it])?.run { score = LABELS_OLD.size - it } }
		}
	}

	fun update() {
		GamerRegistry.onlineGamers().forEach({ update(it) })
	}

	fun clear(scoreboard: Scoreboard) {
		scoreboard.entries.forEach({ scoreboard.resetScores(it) })
	}
}
