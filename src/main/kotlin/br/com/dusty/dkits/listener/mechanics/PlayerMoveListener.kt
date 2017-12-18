package br.com.dusty.dkits.listener.mechanics

import br.com.dusty.dkits.Main
import br.com.dusty.dkits.gamer.EnumMode
import br.com.dusty.dkits.gamer.Gamer
import br.com.dusty.dkits.util.gamer.gamer
import br.com.dusty.dkits.util.isWalk
import br.com.dusty.dkits.util.text.Text
import org.bukkit.Location
import org.bukkit.Material.SPONGE
import org.bukkit.block.BlockFace
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import kotlin.math.sign

object PlayerMoveListener: Listener {

	@EventHandler
	fun onPlayerMove(event: PlayerMoveEvent) {
		val player = event.player
		val gamer = player.gamer()

		if (gamer.warp.overrides(event)) return

		if (gamer.warpTask != null) {
			gamer.warpTask!!.cancel()
			gamer.warpTask = null

			player.sendMessage(Text.negativePrefix().basic("Você se ").negative("moveu").basic(", teleporte ").negative("cancelado").basic("!").toString())
		}

		val from = event.from
		val to = event.to

		if (gamer.mode != EnumMode.ADMIN) {
			when {
				gamer.isFrozen() && event.isWalk()                  -> event.to = Location(from.world, from.x, to.y, from.z, to.yaw, to.pitch)
				toInvincibleLocation(from, to)                      -> {
					val velocity = player.velocity.clone()

					val signX = velocity.x.sign
					val signY = velocity.y.sign
					val signZ = velocity.z.sign

					velocity.x = -2.0 * (if (signX != 0.0) signX else if (Main.RANDOM.nextBoolean()) 1.0 else -1.0)
					velocity.y = -2.0 * signY
					velocity.z = -2.0 * (if (signZ != 0.0) signZ else if (Main.RANDOM.nextBoolean()) 1.0 else -1.0)

					player.velocity = velocity
				}
				to.block.getRelative(BlockFace.DOWN).type == SPONGE -> boost(gamer, to)
			}
		}
	}

//	fun toInvincibleLocation(from: Location, to: Location) = !Main.REGION_MANAGER!!.getApplicableRegions(from).allows(DefaultFlag.INVINCIBILITY) && Main.REGION_MANAGER!!.getApplicableRegions(
//			to).allows(DefaultFlag.INVINCIBILITY) //TODO: 1.8 switch
	fun toInvincibleLocation(from: Location, to: Location) = false

	fun boost(gamer: Gamer, location: Location) {
		val player = gamer.player

		var under = 0
		val locationUnder = location.clone()
		while (locationUnder.add(0.0, -1.0, 0.0).block.type == SPONGE) under++

		var east = 0
		/*val locationEast = location.clone()
		locationEast.x++
		while (locationEast.clone().add(0.0, -1.0, 0.0).block.type == SPONGE) east++*/

		var south = 0
		/*val locationSouth = location.clone()
		locationSouth.z++
		while (locationSouth.clone().add(0.0, -1.0, 0.0).block.type == SPONGE) south++*/

		var west = 0
		/*val locationWest = location.clone()
		locationWest.x--
		while (locationWest.clone().add(0.0, -1.0, 0.0).block.type == SPONGE) west++*/

		var north = 0
		/*val locationNorth = location.clone()
		locationNorth.z--
		while (locationNorth.clone().add(0.0, -1.0, 0.0).block.type == SPONGE) north++*/

		val velocity = player.velocity.clone()
		velocity.x = 0.75 * (east - west)
		velocity.y = 0.75 * under
		velocity.z = 0.75 * (south - north)

		player.velocity = velocity

		gamer.isNoFall = true
	}
}
