package br.com.dusty.dkits.warp

import br.com.dusty.dkits.kit.Kit
import br.com.dusty.dkits.util.ItemStackUtils
import br.com.dusty.dkits.util.text.Text
import br.com.dusty.dkits.util.text.TextColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object LobbyWarp: Warp() {

	init {
		name = "Lobby"
		icon = ItemStack(Material.MAP)

		ItemStackUtils.rename(icon, Text.of(name).color(TextColor.GOLD).toString())
		ItemStackUtils.setDescription(icon, description)

		entryKit = LobbyKit()

		data.spreadRange = 4f

		loadData()
	}

	private class LobbyKit: Kit() {

		init {
			//TODO: Rules book
			items = arrayOf(ItemStackUtils.rename(ItemStack(Material.EMPTY_MAP),
			                                                 Text.of("Warps").color(TextColor.GOLD).toString()),
			                           null,
			                           null,
			                           null,
			                           null,
			                           null,
			                           null,
			                           null,
			                           ItemStackUtils.rename(ItemStack(Material.WRITTEN_BOOK),
			                                                 Text.of("Regras").color(TextColor.GOLD).toString()))
		}
	}
}
