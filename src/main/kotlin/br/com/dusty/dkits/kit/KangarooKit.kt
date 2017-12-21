package br.com.dusty.dkits.kit

import br.com.dusty.dkits.ability.KangarooAbility
import br.com.dusty.dkits.util.color
import br.com.dusty.dkits.util.description
import br.com.dusty.dkits.util.rename
import br.com.dusty.dkits.util.text.Text
import br.com.dusty.dkits.util.text.TextColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object KangarooKit: Kit() {

	init {
		name = "Kangaroo"

		icon = ItemStack(Material.FIREWORK)
		icon.rename(Text.of(name).color(TextColor.GOLD).toString())
		icon.description(description, true)

		weapon = ItemStack(Material.STONE_SWORD)
		armor = arrayOf(null, null, ItemStack(Material.LEATHER_CHESTPLATE).color(0x9A3B33), null)
		items = arrayOf(ItemStack(Material.FIREWORK).rename("Boost do Kangaroo"))

		ability = KangarooAbility

		isDummy = false
		isBroadcast = true

		loadData()
	}
}
