package br.com.dusty.dkits.warp

import br.com.dusty.dkits.kit.Kit
import br.com.dusty.dkits.util.rename
import br.com.dusty.dkits.util.setDescription
import br.com.dusty.dkits.util.text.Text
import br.com.dusty.dkits.util.text.TextColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object LobbyWarp: Warp() {

	init {
		name = "Lobby"
		icon = ItemStack(Material.MAP)

		icon.rename(Text.of(name).color(TextColor.GOLD).toString())
		icon.setDescription(description)

		entryKit = Kit(items = arrayOf(GAME_WARP_KIT.items[8],
		                               null,
		                               null,
		                               null,
		                               null,
		                               null,
		                               null,
		                               null,
		                               ItemStack(Material.WRITTEN_BOOK).rename(Text.of("Regras").color(TextColor.GOLD).toString())))

		data.spreadRange = 4.0

		loadData()
	}
}
