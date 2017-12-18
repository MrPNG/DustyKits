package br.com.dusty.dkits.warp

import br.com.dusty.dkits.gamer.Gamer
import br.com.dusty.dkits.util.description
import br.com.dusty.dkits.util.rename
import br.com.dusty.dkits.util.text.Text
import br.com.dusty.dkits.util.text.TextColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object GladiatorWarp: Warp() {

	init {
		name = "Gladiator"

		icon = ItemStack(Material.IRON_FENCE)
		icon.rename(Text.of(name).color(TextColor.GOLD).toString())
		icon.description(description, true)

		hasShop = true

		loadData()
	}

	override fun receiveGamer(gamer: Gamer, announce: Boolean) {
		val oneVsOne = Warps.ONE_VS_ONE

		gamer.sendToWarp(oneVsOne, true, false)

		val player = gamer.player

		player.sendMessage(Text.positivePrefix().basic("Você foi ").positive("teleportado").basic(" para a warp ").positive(oneVsOne.name).basic(", onde poderá jogar o modo ").positive("Gladiator").basic(
				" e outros!").toString())

//		if (gamer.protocolVersion.isGreaterThanOrEquals(EnumProtocolVersion.RELEASE_1_8)) player.sendTitle(Text.basicOf("Você está na warp ").positive(oneVsOne.name).basic("!").toString(),
//		                                                                                                   Text.basicOf("Aqui você pode escolher o modo ").positive("Gladiator").basic(" e outros!").toString(),
//		                                                                                                   10,
//		                                                                                                   80,
//		                                                                                                   10) //TODO: 1.8 switch
	}
}
