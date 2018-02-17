package br.com.dusty.dkits.kit

import br.com.dusty.dkits.ability.NinjaAbility
import br.com.dusty.dkits.util.color
import br.com.dusty.dkits.util.description
import br.com.dusty.dkits.util.rename
import br.com.dusty.dkits.util.text.Text
import br.com.dusty.dkits.util.text.TextColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object OdinKit: Kit() {

	init {
		name = "Odin"

		icon = ItemStack(Material.DIAMOND_AXE)
		icon.rename(Text.of(name).color(TextColor.GOLD).toString())
		icon.description(description, true)

		weapon = ItemStack(Material.STONE_SWORD)
		armor = arrayOf(null, null, ItemStack(Material.LEATHER_CHESTPLATE).color(0x191919), null)
		items = arrayOf(ItemStack(Material.DIAMOND_AXE).rename("Machado do Odin"))

		ability = NinjaAbility

		isDummy = false
		isBroadcast = true

		loadData()
	}
}
