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

	private val class_PacketPlayInClientCommand: Class<*> = Class.forName(Protocols.NMS_PACKAGE + Protocols.NMS_VERSION + ".PacketPlayInClientCommand")
	private val field_PacketPlayInClientCommand_a: Field

	private val enum_EnumClientCommand_values: Array<*>

	init {
		field_PacketPlayInClientCommand_a = Protocols.getAccessibleField(class_PacketPlayInClientCommand, "a")

		val enum_EnumClientCommand = Class.forName(Protocols.NMS_PACKAGE + Protocols.NMS_VERSION + ".PacketPlayInClientCommand\$EnumClientCommand")
		enum_EnumClientCommand_values = enum_EnumClientCommand.enumConstants
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	fun onPlayerDeath(event: PlayerDeathEvent) { //TODO: Handle death
		event.deathMessage = null
		event.drops.removeIf { it.type !in ALLOWED_DROPS}

		val player = event.entity
		val gamer = player.gamer()

		if (player.killer != null && player.killer != player && gamer.warp.type != Warp.EnumWarpType.EVENT) player.killer.gamer().kill(gamer)

		gamer.resetKillStreak()

		val object_PacketPlayInClientCommand = class_PacketPlayInClientCommand.newInstance()
		field_PacketPlayInClientCommand_a.set(object_PacketPlayInClientCommand, enum_EnumClientCommand_values[0])

		Tasks.sync(Runnable {
			Protocols.sendPacket(object_PacketPlayInClientCommand, player)

			//TODO: If player was on MiniHG, send to Lobby.
			gamer.sendToWarp(if (gamer.warp.type == Warp.EnumWarpType.EVENT) Warps.LOBBY else gamer.warp, false)
		})
	}
}
