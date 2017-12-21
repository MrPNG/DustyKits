package br.com.dusty.dkits.kit

import br.com.dusty.dkits.util.color
import br.com.dusty.dkits.util.description
import br.com.dusty.dkits.util.rename
import br.com.dusty.dkits.util.text.Text
import br.com.dusty.dkits.util.text.TextColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object VikingKit: Kit() {

	init {
		name = "Viking"

		icon = ItemStack(Material.IRON_AXE)
		icon.rename(Text.of(name).color(TextColor.GOLD).toString())
		icon.description(description, true)

		weapon = ItemStack(Material.IRON_AXE)
		armor = arrayOf(null, null, ItemStack(Material.LEATHER_CHESTPLATE).color(0x3A2323), null)

		isDummy = false
		isBroadcast = true

		loadData()
	}
}