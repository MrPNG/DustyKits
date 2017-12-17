package br.com.dusty.dkits.kit

import br.com.dusty.dkits.ability.ThorAbility
import br.com.dusty.dkits.util.color
import br.com.dusty.dkits.util.description
import br.com.dusty.dkits.util.rename
import br.com.dusty.dkits.util.text.Text
import br.com.dusty.dkits.util.text.TextColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object ThorKit: Kit() {

	init {
		name = "Thor"

		icon = ItemStack(Material.WOOD_AXE)
		icon.rename(Text.of(name).color(TextColor.GOLD).toString())
		icon.description(description, true)

		weapon = ItemStack(Material.STONE_SWORD)
		armor = arrayOf(null, ItemStack(Material.LEATHER_CHESTPLATE).color(0x704E22), null, null)
		items = arrayOf(ItemStack(Material.WOOD_AXE).rename("Machado do Thor"))

		ability = ThorAbility

		isDummy = false
		isBroadcast = true

		loadData()
	}
}
