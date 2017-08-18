package br.com.dusty.dkits.warp

import br.com.dusty.dkits.util.ItemStackUtils
import br.com.dusty.dkits.util.text.Text
import br.com.dusty.dkits.util.text.TextColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object ArenaWarp: Warp() {

	init {
		name = "Arena"
		icon = ItemStack(Material.STONE_SWORD)

		ItemStackUtils.rename(icon, Text.of(name).color(TextColor.GOLD).toString())
		ItemStackUtils.setDescription(icon, description)

		entryKit = Warp.GAME_WARP_KIT

		data.spreadRange = 4f

		loadData()
	}
}
