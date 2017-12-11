package br.com.dusty.dkits.warp

import br.com.dusty.dkits.util.rename
import br.com.dusty.dkits.util.setDescription
import br.com.dusty.dkits.util.text.Text
import br.com.dusty.dkits.util.text.TextColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object FpsWarp: Warp() {

	init {
		name = "FPS"
		icon = ItemStack(Material.GLASS)

		icon.rename(Text.of(name).color(TextColor.GOLD).toString())
		icon.setDescription(description)

		hasShop = true

		loadData()
	}
}
