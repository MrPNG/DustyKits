package br.com.dusty.dkits.kit

import br.com.dusty.dkits.ability.StomperAbility
import br.com.dusty.dkits.util.color
import br.com.dusty.dkits.util.description
import br.com.dusty.dkits.util.rename
import br.com.dusty.dkits.util.text.Text
import br.com.dusty.dkits.util.text.TextColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object StomperKit: Kit() {

	init {
		name = "Stomper"

		icon = ItemStack(Material.IRON_BOOTS)
		icon.rename(Text.of(name).color(TextColor.GOLD).toString())
		icon.description(description, true)

		weapon = ItemStack(Material.STONE_SWORD)
		armor = arrayOf(null, null, ItemStack(Material.LEATHER_CHESTPLATE).color(0x881100), null)

		ability = StomperAbility

		isDummy = false
		isBroadcast = true

		loadData()
	}
}
