package br.com.dusty.dkits.warp

import br.com.dusty.dkits.kit.Kit
import br.com.dusty.dkits.util.Inventories
import br.com.dusty.dkits.util.description
import br.com.dusty.dkits.util.rename
import br.com.dusty.dkits.util.text.Text
import br.com.dusty.dkits.util.text.TextColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object LobbyWarp: Warp() {

	init {
		name = "Lobby"

		icon = ItemStack(Material.MAP)
		icon.rename(Text.of(name).color(TextColor.GOLD).toString())
		icon.description(description, true)

		entryKit = Kit(items = arrayOf(GAME_WARP_KIT.items[8], null, null, null, Inventories.STORE, null, null, null, Inventories.RULEBOOK))

		data.spreadRange = 4.0

		loadData()
	}
}
