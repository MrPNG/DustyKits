package br.com.dusty.dkits.listener.quit

import br.com.dusty.dkits.gamer.EnumRank
import br.com.dusty.dkits.gamer.GamerRegistry
import br.com.dusty.dkits.util.ScoreboardUtils
import br.com.dusty.dkits.util.TaskUtils
import br.com.dusty.dkits.util.text.Text
import br.com.dusty.dkits.util.web.WebAPI
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

object PlayerQuitListener: Listener {

	private val QUIT_MESSAGE_PREFIX = Text.basicOf("[").negative("-").basic("] ").toString()

	@EventHandler
	fun onPlayerQuit(event: PlayerQuitEvent) {
		val player = event.player

		val gamer = GamerRegistry.unregister(player)

		if (gamer.isCombatTagged) {
			val combatPartner = gamer.combatPartner

			if (combatPartner != null) {
				combatPartner.addKill()
				combatPartner.addKillStreak()
				combatPartner.addKillMoney()
				combatPartner.addKillXp()

				gamer.addDeath()
				gamer.resetKillStreak()
				gamer.removeDeathMoney()
				gamer.removeDeathXp()
			}

			Bukkit.broadcastMessage(Text.negativeOf(player.name)
					                        .basic(" deslogou em ")
					                        .negative("combate")
					                        .basic("!")
					                        .toString())
		}

		TaskUtils.async(Runnable { WebAPI.saveProfiles(gamer) })

		if (gamer.rank.isLowerThan(EnumRank.MOD))
			event.quitMessage = QUIT_MESSAGE_PREFIX + player.name
		else
			event.quitMessage = null

		ScoreboardUtils.updateAll()
	}
}
