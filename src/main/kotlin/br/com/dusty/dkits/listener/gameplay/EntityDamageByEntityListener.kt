package br.com.dusty.dkits.listener.gameplay

import br.com.dusty.dkits.Main
import br.com.dusty.dkits.gamer.EnumMode
import br.com.dusty.dkits.util.gamer.gamer
import com.sk89q.worldguard.protection.flags.DefaultFlag
import org.bukkit.Material.*
import org.bukkit.entity.Arrow
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent

object EntityDamageByEntityListener: Listener {

	@EventHandler(priority = EventPriority.HIGH)
	fun onEntityDamage(event: EntityDamageByEntityEvent) {
		var damagerPlayer: Player? = null

		var damage = event.damage

		when (event.damager) {
			is Arrow  -> {
				val arrow = event.damager as Arrow

				if (arrow.shooter is Player) damagerPlayer = arrow.shooter as Player
			}
			is Player -> {
				damagerPlayer = event.damager as Player

				if (damagerPlayer.itemInHand != null) when (damagerPlayer.itemInHand.type) {
					WOOD_SWORD, GOLD_SWORD, STONE_SWORD, IRON_SWORD, DIAMOND_SWORD -> damage *= 0.75
				}
			}
		}

		event.damage = damage

		if (damagerPlayer != null) {
			val damager = damagerPlayer.gamer()

			if (damager.warp.overrides(event)) return

			if (event.entity is Player && !event.isCancelled) {
				val player = (event.entity as Player)
				val gamer = player.gamer()

				if (damager.isInvincible || damager.mode == EnumMode.SPECTATE || !player.canSee(damagerPlayer) || Main.REGION_MANAGER!!.getApplicableRegions(damager.player.location).allows(
						DefaultFlag.INVINCIBILITY)) event.isCancelled = true
				else {
					if (gamer.combatTag < 10000L) gamer.combatTag = 10000L
					if (damager.combatTag < 10000L) damager.combatTag = 10000L

					gamer.combatPartner = damager
					damager.combatPartner = gamer
				}
			}
		}
	}
}
