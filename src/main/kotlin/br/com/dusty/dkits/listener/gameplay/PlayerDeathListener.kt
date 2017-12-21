package br.com.dusty.dkits.listener.gameplay

import br.com.dusty.dkits.util.Tasks
import br.com.dusty.dkits.util.gamer.gamer
import br.com.dusty.dkits.warp.Warp
import br.com.dusty.dkits.warp.Warps
import org.bukkit.Material.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent

object PlayerDeathListener: Listener {

	val ALLOWED_DROPS = arrayOf(RED_MUSHROOM, BROWN_MUSHROOM, BOWL, MUSHROOM_SOUP, WOOD_SWORD, STONE_SWORD)

	@EventHandler
	fun onPlayerDeath(event: PlayerDeathEvent) {
		val player = event.entity
		val gamer = player.gamer()

		if (gamer.warp.overrides(event)) return

		event.deathMessage = null
		event.drops.removeIf { it.type !in ALLOWED_DROPS }

		if (gamer.isCombatTagged()) {
			val combatPartner = gamer.combatPartner!!

			if (gamer.warp.type != Warp.EnumWarpType.EVENT) combatPartner.kill(gamer)

			if (combatPartner.warp.durabilityBehavior == Warp.EnumDurabilityBehavior.REGEN_ON_KILL) combatPartner.player.inventory.apply {
				itemInHand?.durability = 0
				armorContents.forEach { it?.durability = 0 }
			}
		}

		gamer.resetKillStreak()

		Tasks.sync(Runnable {
			player.spigot().respawn()

			gamer.sendToWarp(if (gamer.warp.type == Warp.EnumWarpType.EVENT) Warps.LOBBY else gamer.warp, true, false)
		})
	}
}
