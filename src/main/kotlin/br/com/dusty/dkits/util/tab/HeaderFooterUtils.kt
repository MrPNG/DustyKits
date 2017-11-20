package br.com.dusty.dkits.util.tab

import br.com.dusty.dkits.gamer.Gamer
import br.com.dusty.dkits.util.protocol.ProtocolUtils
import br.com.dusty.dkits.util.text.Text
import br.com.dusty.dkits.util.text.TextColor
import java.lang.reflect.Field

object HeaderFooterUtils {

	val HEADER_MAIN = Text.of("Dusty").color(TextColor.RED).append(" - ").color(TextColor.WHITE).append("dusty.com.br").color(TextColor.GOLD).toString()

	val HEADER_BAR = Text.of(" -------------------------------- ").color(TextColor.RED).toString()

	var class_PacketPlayOutPlayerListHeaderFooter: Class<*> = Class.forName(ProtocolUtils.NMS_PACKAGE + ProtocolUtils.NMS_VERSION + ".PacketPlayOutPlayerListHeaderFooter")
	var field_PacketPlayOutPlayerListHeaderFooter_a: Field
	var field_PacketPlayOutPlayerListHeaderFooter_b: Field

	init {
		field_PacketPlayOutPlayerListHeaderFooter_a = ProtocolUtils.getAccessibleField(class_PacketPlayOutPlayerListHeaderFooter, "a")
		field_PacketPlayOutPlayerListHeaderFooter_b = ProtocolUtils.getAccessibleField(class_PacketPlayOutPlayerListHeaderFooter, "b")
	}

	fun update(gamer: Gamer) {
		val player = gamer.player

		val object_PacketPlayOutPlayerListHeaderFooter = class_PacketPlayOutPlayerListHeaderFooter.newInstance()
		field_PacketPlayOutPlayerListHeaderFooter_a.set(object_PacketPlayOutPlayerListHeaderFooter, ProtocolUtils.chatMessage(HEADER_MAIN + "\n" + HEADER_BAR))
		field_PacketPlayOutPlayerListHeaderFooter_b.set(object_PacketPlayOutPlayerListHeaderFooter,
		                                                ProtocolUtils.chatMessage(HEADER_BAR + "\n" + Text.of("Warp: ").color(TextColor.RED).append(gamer.warp.name).color(TextColor.YELLOW).toString()))

		ProtocolUtils.sendPacket(object_PacketPlayOutPlayerListHeaderFooter, player)
	}
}
