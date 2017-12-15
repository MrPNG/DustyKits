package br.com.dusty.dkits.kit

import br.com.dusty.dkits.ability.StomperAbility
import br.com.dusty.dkits.util.color
import br.com.dusty.dkits.util.rename
import br.com.dusty.dkits.util.setDescription
import br.com.dusty.dkits.util.text.Text
import br.com.dusty.dkits.util.text.TextColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object StomperKit: Kit() {

	init {
		name = "Stomper"

		icon = ItemStack(Material.IRON_BOOTS)
		icon.rename(Text.of(name).color(TextColor.GOLD).toString())
		icon.setDescription(description, true)

		weapon = ItemStack(Material.GOLD_SWORD)
		armor = arrayOf(null, ItemStack(Material.LEATHER_CHESTPLATE).color(0xBBBBBB), null, null)

		ability = StomperAbility

		isDummy = false
		isBroadcast = true

		loadData()
	}
}
