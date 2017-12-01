package br.com.dusty.dkits.listener.gameplay

import br.com.dusty.dkits.gamer.gamer
import br.com.dusty.dkits.util.Tasks
import br.com.dusty.dkits.util.protocol.Protocols
import br.com.dusty.dkits.warp.Warp
import br.com.dusty.dkits.warp.Warps
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import java.lang.reflect.Field

object PlayerDeathListener: Listener {

	val ALLOWED_DROPS = arrayOf(Material.RED_MUSHROOM, Material.BROWN_MUSHROOM, Material.BOWL, Material.MUSHROOM_SOUP, Material.WOOD_SWORD, Material.STONE_SWORD)

	@EventHandler(priority = EventPriority.HIGH)
	fun onPlayerDeath(event: PlayerDeathEvent) {
		event.deathMessage = null
		event.drops.removeIf { it.type !in ALLOWED_DROPS }

		val player = event.entity
		val gamer = player.gamer()

		if (gamer.warp.type != Warp.EnumWarpType.EVENT && gamer.isCombatTagged()) gamer.combatPartner!!.kill(gamer)

		gamer.resetKillStreak()

		Tasks.sync(Runnable {
			player.spigot().respawn()

			gamer.removeCombatTag()
			gamer.sendToWarp(if (gamer.warp.type == Warp.EnumWarpType.EVENT) Warps.LOBBY else gamer.warp, false)
		})
	}
}
