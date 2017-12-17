package br.com.dusty.dkits.kit

import br.com.dusty.dkits.ability.TurtleAbility
import br.com.dusty.dkits.util.color
import br.com.dusty.dkits.util.description
import br.com.dusty.dkits.util.rename
import br.com.dusty.dkits.util.text.Text
import br.com.dusty.dkits.util.text.TextColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object TurtleKit: Kit() {

	init {
		name = "Turtle"

		icon = ItemStack(Material.DIAMOND_CHESTPLATE)
		icon.rename(Text.of(name).color(TextColor.GOLD).toString())
		icon.description(description, true)

		weapon = ItemStack(Material.STONE_SWORD)
		armor = arrayOf(null, ItemStack(Material.LEATHER_CHESTPLATE).color(0x658732), null, null)

		ability = TurtleAbility

		isDummy = false
		isBroadcast = true

		loadData()
	}
}