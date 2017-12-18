package br.com.dusty.dkits.warp

import br.com.dusty.dkits.gamer.Gamer
import br.com.dusty.dkits.util.description
import br.com.dusty.dkits.util.rename
import br.com.dusty.dkits.util.text.Text
import br.com.dusty.dkits.util.text.TextColor
import org.bukkit.Material
import org.bukkit.SkullType
import org.bukkit.inventory.ItemStack

object FakeCXC: Warp() {

	init {
		name = "ClanVsClan"

		icon = ItemStack(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal.toShort())
		icon.rename(Text.of(name).color(TextColor.GOLD).toString())
		icon.description(description, true)

		type = EnumWarpType.BOTH

		hasShop = true

		loadData()
	}

	override fun receiveGamer(gamer: Gamer, announce: Boolean) {
		val lobby = Warps.LOBBY

		gamer.sendToWarp(lobby, true, false)

		val player = gamer.player

		player.sendMessage(Text.positivePrefix().basic("Você foi ").positive("teleportado").basic(" para a warp ").positive(lobby.name).basic(" pois o ").positive("Evento " + name).basic(" ainda não foi lançado!").toString())

//		if (gamer.protocolVersion.isGreaterThanOrEquals(EnumProtocolVersion.RELEASE_1_8)) player.sendTitle(Text.basicOf("Você está na warp ").positive(lobby.name).basic("!").toString(),
//		                                                                                                   Text.basicOf("O ").positive("Evento " + name).basic(" ainda não foi lançado!").toString(),
//		                                                                                                   10,
//		                                                                                                   80,
//		                                                                                                   10) //TODO: 1.8 switch
	}
}
