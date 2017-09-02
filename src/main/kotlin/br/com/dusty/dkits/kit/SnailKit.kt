package br.com.dusty.dkits.kit

import br.com.dusty.dkits.util.color
import br.com.dusty.dkits.util.rename
import br.com.dusty.dkits.util.setDescription
import br.com.dusty.dkits.util.text.Text
import br.com.dusty.dkits.util.text.TextColor
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object SnailKit: Kit() {

	init {
		name = "Snail"

		icon.rename(Text.of(name).color(TextColor.GOLD).toString())
		icon.setDescription(description)

		weapon = ItemStack(Material.STONE_SWORD)
		armor = arrayOf(null, ItemStack(Material.LEATHER_CHESTPLATE).color(Color.fromRGB(0xCCD0E1)), null, null)

		isDummy = false
		isBroadcast = true

		loadData()
	}
}