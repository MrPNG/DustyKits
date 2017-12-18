package br.com.dusty.dkits.util.protocol

import org.bukkit.entity.Player
import java.lang.reflect.Constructor
import java.lang.reflect.Field
import java.lang.reflect.Method

object Protocols {

	val CRAFTBUKKIT_PACKAGE = "org.bukkit.craftbukkit."
	val NMS_PACKAGE = "net.minecraft.server."
	val NMS_VERSION = "v1_12_R1"

	val PROTOCOL_SUPPORT_PACKAGE = "protocolsupport.api."

	var constructor_ChatMessage: Constructor<*>
	var method_CraftPlayer_getHandle: Method
	var field_playerConnection: Field
	var method_PlayerConnection_sendPacket: Method

	var method_ProtocolSupportAPI_getProtocolVersion: Method
	var method_ProtocolVersion_getId: Method

	init {
		val class_ChatMessage = Class.forName(Protocols.NMS_PACKAGE + Protocols.NMS_VERSION + ".ChatMessage")
		constructor_ChatMessage = class_ChatMessage.getDeclaredConstructor(String::class.java, Array<Any>::class.java)

		val class_Packet = Class.forName(Protocols.NMS_PACKAGE + Protocols.NMS_VERSION + ".Packet")

		val class_CraftPlayer = Class.forName(Protocols.CRAFTBUKKIT_PACKAGE + Protocols.NMS_VERSION + ".entity.CraftPlayer")
		method_CraftPlayer_getHandle = class_CraftPlayer.getDeclaredMethod("getHandle")

		val class_EntityPlayer = Class.forName(Protocols.NMS_PACKAGE + Protocols.NMS_VERSION + ".EntityPlayer")
		field_playerConnection = class_EntityPlayer.getDeclaredField("playerConnection")

		val class_PlayerConnection = Class.forName(Protocols.NMS_PACKAGE + Protocols.NMS_VERSION + ".PlayerConnection")
		method_PlayerConnection_sendPacket = class_PlayerConnection.getDeclaredMethod("sendPacket", class_Packet)

		val class_ProtocolSupportAPI = Class.forName(PROTOCOL_SUPPORT_PACKAGE + "ProtocolSupportAPI")
		method_ProtocolSupportAPI_getProtocolVersion = class_ProtocolSupportAPI.getDeclaredMethod("getProtocolVersion", Player::class.java)

		val class_ProtocolVersion = Class.forName(PROTOCOL_SUPPORT_PACKAGE + "ProtocolVersion")
		method_ProtocolVersion_getId = class_ProtocolVersion.getDeclaredMethod("getId")
	}

	fun getAccessibleField(clazz: Class<*>, name: String): Field {
		val field = clazz.getDeclaredField(name)
		field.isAccessible = true

		return field
	}

	fun chatMessage(s: String): Any = constructor_ChatMessage.newInstance(s, arrayOfNulls<Any>(0))

	fun protocolVersion(player: Player): Int? {
		val object_ProtocolVersion = method_ProtocolSupportAPI_getProtocolVersion.invoke(null, player)

		return method_ProtocolVersion_getId.invoke(object_ProtocolVersion) as Int
	}

	fun sendPacket(object_Packet: Any, vararg players: Player) {
		players.map { method_CraftPlayer_getHandle.invoke(it) }.map { field_playerConnection.get(it) }.forEach { method_PlayerConnection_sendPacket.invoke(it, object_Packet) }
	}
}
