package br.com.dusty.dkits.util.protocol

import br.com.dusty.dkits.gamer.Gamer
import br.com.dusty.dkits.util.text.Text
import br.com.dusty.dkits.util.text.TextColor
import java.lang.reflect.Field

object HeaderFooters {

	val HEADER_MAIN = Text.of("Dusty").color(TextColor.RED).append(" - ").color(TextColor.WHITE).append("dusty.com.br").color(TextColor.GOLD).toString()

	val HEADER_BAR = Text.of(" -------------------------------- ").color(TextColor.RED).toString()

	var class_PacketPlayOutPlayerListHeaderFooter: Class<*> = Class.forName(Protocols.NMS_PACKAGE + Protocols.NMS_VERSION + ".PacketPlayOutPlayerListHeaderFooter")
	var field_PacketPlayOutPlayerListHeaderFooter_a: Field
	var field_PacketPlayOutPlayerListHeaderFooter_b: Field

	init {
		field_PacketPlayOutPlayerListHeaderFooter_a = Protocols.getAccessibleField(class_PacketPlayOutPlayerListHeaderFooter, "a")
		field_PacketPlayOutPlayerListHeaderFooter_b = Protocols.getAccessibleField(class_PacketPlayOutPlayerListHeaderFooter, "b")
	}

	fun update(gamer: Gamer) {
		val player = gamer.player

		val object_PacketPlayOutPlayerListHeaderFooter = class_PacketPlayOutPlayerListHeaderFooter.newInstance()
		field_PacketPlayOutPlayerListHeaderFooter_a.set(object_PacketPlayOutPlayerListHeaderFooter, Protocols.chatMessage(HEADER_MAIN + "\n" + HEADER_BAR))
		field_PacketPlayOutPlayerListHeaderFooter_b.set(object_PacketPlayOutPlayerListHeaderFooter,
		                                                Protocols.chatMessage(HEADER_BAR + "\n" + Text.of("Warp: ").color(TextColor.RED).append(gamer.warp.name).color(TextColor.YELLOW).toString()))

		Protocols.sendPacket(object_PacketPlayOutPlayerListHeaderFooter, player)
	}
}
