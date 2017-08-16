package br.com.dusty.dkits.listener.gameplay

import br.com.dusty.dkits.gamer.Gamer
import br.com.dusty.dkits.util.TaskUtils
import br.com.dusty.dkits.util.protocol.ProtocolUtils
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import java.lang.reflect.Field

object PlayerDeathListener: Listener {

	private val class_PacketPlayInClientCommand: Class<*> = Class.forName(ProtocolUtils.NMS_PACKAGE + ProtocolUtils.NMS_VERSION + ".PacketPlayInClientCommand")
	private val field_PacketPlayInClientCommand_a: Field

	private val enum_EnumClientCommand_values: Array<*>

	init {
		field_PacketPlayInClientCommand_a = ProtocolUtils.getAccessibleField(class_PacketPlayInClientCommand, "a")

		val enum_EnumClientCommand = Class.forName(ProtocolUtils.NMS_PACKAGE + ProtocolUtils.NMS_VERSION + ".PacketPlayInClientCommand\$EnumClientCommand")
		enum_EnumClientCommand_values = enum_EnumClientCommand.enumConstants
	}

	@EventHandler
	fun onPlayerDeath(event: PlayerDeathEvent) {
		val player = event.entity

		val gamer = Gamer.of(player)

		val object_PacketPlayInClientCommand = class_PacketPlayInClientCommand.newInstance()
		field_PacketPlayInClientCommand_a.set(object_PacketPlayInClientCommand, enum_EnumClientCommand_values[0])

		TaskUtils.sync(Runnable {
			ProtocolUtils.sendPacket(object_PacketPlayInClientCommand, player)

			//TODO: If player was on MiniHG, send to Lobby.
			gamer.sendToWarp(gamer.warp)
			gamer.resetKillStreak()
		})
	}
}
