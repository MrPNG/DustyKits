package br.com.dusty.dkits.kit

import br.com.dusty.dkits.ability.GladiatorAbility
import br.com.dusty.dkits.util.color
import br.com.dusty.dkits.util.description
import br.com.dusty.dkits.util.rename
import br.com.dusty.dkits.util.text.Text
import br.com.dusty.dkits.util.text.TextColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object GladiatorKit: Kit() {

	init {
		name = "Gladiator"

		icon = ItemStack(Material.IRON_FENCE)
		icon.rename(Text.of(name).color(TextColor.GOLD).toString())
		icon.description(description, true)

		weapon = ItemStack(Material.STONE_SWORD)
		armor = arrayOf(null, null, ItemStack(Material.LEATHER_CHESTPLATE).color(0x727272), null)
		items = arrayOf(ItemStack(Material.IRON_FENCE).rename("1v1"))

		ability = GladiatorAbility

		isDummy = false
		isBroadcast = true

		loadData()
	}
}
