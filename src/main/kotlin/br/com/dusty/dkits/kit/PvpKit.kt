package br.com.dusty.dkits.kit

import br.com.dusty.dkits.util.description
import br.com.dusty.dkits.util.rename
import br.com.dusty.dkits.util.text.Text
import br.com.dusty.dkits.util.text.TextColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object PvpKit: Kit() {

	init {
		name = "PvP"
		description = "O kit mais básico de todos :)"

		icon.rename(Text.of(name).color(TextColor.GOLD).toString())
		icon.description(description, true)

		weapon = ItemStack(Material.STONE_SWORD)
		armor = arrayOf(null, ItemStack(Material.LEATHER_CHESTPLATE), null, null)

		isDummy = false
		isBroadcast = true

		loadData()
	}
}
