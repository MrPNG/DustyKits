package br.com.dusty.dkits.kit

import br.com.dusty.dkits.ability.ThorAbility
import br.com.dusty.dkits.util.color
import br.com.dusty.dkits.util.rename
import br.com.dusty.dkits.util.setDescription
import br.com.dusty.dkits.util.text.Text
import br.com.dusty.dkits.util.text.TextColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object ThorKit: Kit() {

	init {
		name = "Thor"

		icon = ItemStack(Material.WOOD_AXE)
		icon.rename(Text.of(name).color(TextColor.GOLD).toString())
		icon.setDescription(description, true)

		weapon = ItemStack(Material.GOLD_SWORD)
		armor = arrayOf(null, ItemStack(Material.LEATHER_CHESTPLATE).color(0xBBBBBB), null, null)
		items = arrayOf(ItemStack(Material.WOOD_AXE).rename("Machado do Thor"))

		ability = ThorAbility

		isDummy = false
		isBroadcast = true

		loadData()
	}
}
