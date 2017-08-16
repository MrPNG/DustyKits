package br.com.dusty.dkits.listener.gameplay

import br.com.dusty.dkits.gamer.Gamer
import br.com.dusty.dkits.util.TaskUtils
import br.com.dusty.dkits.util.protocol.ProtocolUtils
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent

import java.lang.reflect.Field

class PlayerDeathListener: Listener {

	private var class_PacketPlayInClientCommand: Class<*>? = null
	private var field_PacketPlayInClientCommand_a: Field? = null

	private var enum_EnumClientCommand_values: Array<Any>? = null

	init {
		try {
			class_PacketPlayInClientCommand = Class.forName(ProtocolUtils.NMS_PACKAGE + ProtocolUtils.NMS_VERSION + ".PacketPlayInClientCommand")
			field_PacketPlayInClientCommand_a = ProtocolUtils.getAccessibleField(class_PacketPlayInClientCommand, "a")

			val enum_EnumClientCommand = Class.forName(ProtocolUtils.NMS_PACKAGE + ProtocolUtils.NMS_VERSION + ".PacketPlayInClientCommand\$EnumClientCommand")
			enum_EnumClientCommand_values = enum_EnumClientCommand.getEnumConstants()
		} catch (e: ClassNotFoundException) {
			e.printStackTrace()
		} catch (e: NoSuchFieldException) {
			e.printStackTrace()
		}

	}

	@EventHandler
	fun onPlayerDeath(event: PlayerDeathEvent) {
		val player = event.entity

		val gamer = Gamer.of(player)

		try {
			val object_PacketPlayInClientCommand = class_PacketPlayInClientCommand!!.newInstance()
			field_PacketPlayInClientCommand_a!!.set(object_PacketPlayInClientCommand,
			                                        enum_EnumClientCommand_values!![0])

			TaskUtils.sync({
				               ProtocolUtils.sendPacket(object_PacketPlayInClientCommand, player)

				               //TODO: If player was on MiniHG, send to Lobby.
				               gamer.sendToWarp(gamer.warp)
			               })
		} catch (e: InstantiationException) {
			e.printStackTrace()
		} catch (e: IllegalAccessException) {
			e.printStackTrace()
		}

	}
}
