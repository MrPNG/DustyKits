package br.com.dusty.dkits.util.protocol

import br.com.dusty.dkits.gamer.Gamer
import br.com.dusty.dkits.util.text.Text
import br.com.dusty.dkits.util.text.TextColor
import br.com.dusty.dkits.util.text.TextStyle
import java.lang.reflect.Field

object HeaderFooters {

	val HEADER_MAIN = Text.of("\n                Dusty                \n").color(TextColor.GREEN).styles(TextStyle.BOLD).append(" www.dusty.com.br\n ").color(TextColor.GOLD).toString()

	var class_PacketPlayOutPlayerListHeaderFooter: Class<*> = Class.forName(Protocols.NMS_PACKAGE + Protocols.VERSION + ".PacketPlayOutPlayerListHeaderFooter")
	var field_PacketPlayOutPlayerListHeaderFooter_a: Field
	var field_PacketPlayOutPlayerListHeaderFooter_b: Field

	init {
		field_PacketPlayOutPlayerListHeaderFooter_a = Protocols.getAccessibleField(class_PacketPlayOutPlayerListHeaderFooter, "a")
		field_PacketPlayOutPlayerListHeaderFooter_b = Protocols.getAccessibleField(class_PacketPlayOutPlayerListHeaderFooter, "b")
	}

	fun update(gamer: Gamer) {
		val player = gamer.player

		val object_PacketPlayOutPlayerListHeaderFooter = class_PacketPlayOutPlayerListHeaderFooter.newInstance()
		field_PacketPlayOutPlayerListHeaderFooter_a.set(object_PacketPlayOutPlayerListHeaderFooter, Protocols.chatMessage(HEADER_MAIN))
		field_PacketPlayOutPlayerListHeaderFooter_b.set(object_PacketPlayOutPlayerListHeaderFooter,
		                                                Protocols.chatMessage(Text.of("\nWarp: ").color(TextColor.GREEN).styles(TextStyle.BOLD).append(gamer.warp.name).color(TextColor.GOLD).toString() + "\n"))

		Protocols.sendPacket(object_PacketPlayOutPlayerListHeaderFooter, player)
	}
}
