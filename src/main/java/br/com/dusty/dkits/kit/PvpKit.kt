package br.com.dusty.dkits.kit

import br.com.dusty.dkits.util.ItemStackUtils
import br.com.dusty.dkits.util.text.Text
import br.com.dusty.dkits.util.text.TextColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object PvpKit: Kit() {

	init {
		name = "PvP"

		ItemStackUtils.rename(icon, Text.of(name).color(TextColor.GOLD).toString())
		ItemStackUtils.setDescription(icon, description)

		weapon = ItemStack(Material.STONE_SWORD)
		armor = arrayOf(null, ItemStack(Material.LEATHER_CHESTPLATE), null, null)

		isDummy = false
		isBroadcast = true

		loadData()
	}
}
