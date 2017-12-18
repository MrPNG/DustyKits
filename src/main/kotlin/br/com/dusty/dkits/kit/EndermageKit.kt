package br.com.dusty.dkits.kit

import br.com.dusty.dkits.ability.EndermageAbility
import br.com.dusty.dkits.util.color
import br.com.dusty.dkits.util.description
import br.com.dusty.dkits.util.rename
import br.com.dusty.dkits.util.text.Text
import br.com.dusty.dkits.util.text.TextColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object EndermageKit: Kit() {

	init {
		name = "Endermage"

		icon = ItemStack(Material.ENDER_PORTAL_FRAME)
		icon.rename(Text.of(name).color(TextColor.GOLD).toString())
		icon.description(description, true)

		weapon = ItemStack(Material.STONE_SWORD)
		armor = arrayOf(null, ItemStack(Material.LEATHER_CHESTPLATE).color(0x7F3FB2), null, null)
		items = arrayOf(ItemStack(Material.ENDER_PORTAL_FRAME).rename("Portal do Endermage"))

		ability = EndermageAbility

		isDummy = false
		isBroadcast = true

		loadData()
	}
}
