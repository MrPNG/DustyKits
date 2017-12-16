package br.com.dusty.dkits.kit

import br.com.dusty.dkits.ability.PoseidonAbility
import br.com.dusty.dkits.util.color
import br.com.dusty.dkits.util.description
import br.com.dusty.dkits.util.rename
import br.com.dusty.dkits.util.text.Text
import br.com.dusty.dkits.util.text.TextColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object PoseidonKit: Kit() {

	init {
		name = "Poseidon"

		icon = ItemStack(Material.WATER_BUCKET)
		icon.rename(Text.of(name).color(TextColor.GOLD).toString())
		icon.description(description, true)

		weapon = ItemStack(Material.STONE_SWORD)
		armor = arrayOf(null, ItemStack(Material.LEATHER_CHESTPLATE).color(0x3356CC), null, null)

		ability = PoseidonAbility

		isDummy = false
		isBroadcast = true

		loadData()
	}
}
