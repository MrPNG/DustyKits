package br.com.dusty.dkits.listener.gameplay

import br.com.dusty.dkits.gamer.EnumMode
import br.com.dusty.dkits.gamer.gamer
import br.com.dusty.dkits.warp.Warp
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent

object EntityDamageByEntityListener: Listener {

	@EventHandler(priority = EventPriority.HIGH)
	fun onEntityDamage(event: EntityDamageByEntityEvent) {
		if (event.damager is Player) {
			val damagerPlayer = (event.damager as Player)
			val damager = damagerPlayer.gamer()

			if (damager.warp.durabilityBehavior == Warp.EnumDurabilityBehavior.REGEN) damagerPlayer.inventory.itemInMainHand?.durability = 0

			if (event.entity is Player && !event.isCancelled) {
				val player = (event.entity as Player)
				val gamer = player.gamer()

				if (gamer.warp.durabilityBehavior == Warp.EnumDurabilityBehavior.REGEN) player.inventory.armorContents.forEach { it?.durability = 0 }

				if (!player.canSee(damagerPlayer) || damager.mode == EnumMode.SPECTATE) {
					event.isCancelled = true

					return
				}

				gamer.combatTag = 10000L
				gamer.combatPartner = damager

				damager.combatTag = 10000L
				damager.combatPartner = gamer
			}
		}
	}
}
