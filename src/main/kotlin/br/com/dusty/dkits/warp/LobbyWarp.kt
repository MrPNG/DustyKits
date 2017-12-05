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

		entryKit = LobbyKit()

		data.spreadRange = 4F

		loadData()
	}

	private class LobbyKit: Kit() {

		init {
			//TODO: Rules book
			items = arrayOf(GameWarpKit.items[8], null, null, null, null, null, null, null, ItemStack(Material.WRITTEN_BOOK).rename(Text.of("Regras").color(TextColor.GOLD).toString()))
		}
	}
}
