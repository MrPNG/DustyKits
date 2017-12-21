package br.com.dusty.dkits.kit

import br.com.dusty.dkits.ability.FishermanAbility
import br.com.dusty.dkits.util.color
import br.com.dusty.dkits.util.description
import br.com.dusty.dkits.util.rename
import br.com.dusty.dkits.util.text.Text
import br.com.dusty.dkits.util.text.TextColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object FishermanKit: Kit() {

	init {
		name = "Fisherman"

		icon = ItemStack(Material.FISHING_ROD)
		icon.rename(Text.of(name).color(TextColor.GOLD).toString())
		icon.description(description, true)

		weapon = ItemStack(Material.STONE_SWORD)
		armor = arrayOf(null, null, ItemStack(Material.LEATHER_CHESTPLATE).color(0x7F99B8), null)
		items = arrayOf(ItemStack(Material.FISHING_ROD).rename("Vara de Pescar do Fisherman"))

		ability = FishermanAbility

		isDummy = false
		isBroadcast = true

		loadData()
	}
}
