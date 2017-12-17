package br.com.dusty.dkits.kit

import br.com.dusty.dkits.ability.SnailAbility
import br.com.dusty.dkits.util.ItemStacks
import br.com.dusty.dkits.util.color
import br.com.dusty.dkits.util.description
import br.com.dusty.dkits.util.rename
import br.com.dusty.dkits.util.text.Text
import br.com.dusty.dkits.util.text.TextColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionType

object SnailKit: Kit() {

	init {
		name = "Snail"

		icon = ItemStacks.potions(1, false, false, PotionType.SLOWNESS, false)
		icon.rename(Text.of(name).color(TextColor.GOLD).toString())
		icon.description(description, true)

		weapon = ItemStack(Material.STONE_SWORD)
		armor = arrayOf(null, ItemStack(Material.LEATHER_CHESTPLATE).color(0x91A38D), null, null)

		ability = SnailAbility

		isDummy = false
		isBroadcast = true

		loadData()
	}
}