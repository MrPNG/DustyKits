package br.com.dusty.dkits.kit

import br.com.dusty.dkits.ability.ChickenmanAbility
import br.com.dusty.dkits.util.color
import br.com.dusty.dkits.util.description
import br.com.dusty.dkits.util.rename
import br.com.dusty.dkits.util.text.Text
import br.com.dusty.dkits.util.text.TextColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object ChickenmanKit: Kit() {

	init {
		name = "Chickenman"

		icon = ItemStack(Material.COOKED_CHICKEN)
		icon.rename(Text.of(name).color(TextColor.GOLD).toString())
		icon.description(description, true)

		weapon = ItemStack(Material.WOOD_SWORD)
		armor = arrayOf(null, null, ItemStack(Material.LEATHER_CHESTPLATE).color(0xAA842D), null)
		items = arrayOf(ItemStack(Material.COOKED_CHICKEN).rename("Frango Radioativo"))

		ability = ChickenmanAbility

		isDummy = false
		isBroadcast = true

		loadData()
	}
}
