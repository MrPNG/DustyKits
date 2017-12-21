package br.com.dusty.dkits.kit

import br.com.dusty.dkits.ability.BubbleAbility
import br.com.dusty.dkits.util.color
import br.com.dusty.dkits.util.description
import br.com.dusty.dkits.util.rename
import br.com.dusty.dkits.util.text.Text
import br.com.dusty.dkits.util.text.TextColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object BubbleKit: Kit() {

	init {
		name = "Bubble"

		icon = ItemStack(Material.SLIME_BALL)
		icon.rename(Text.of(name).color(TextColor.GOLD).toString())
		icon.description(description, true)

		weapon = ItemStack(Material.STONE_SWORD)
		armor = arrayOf(null, null, ItemStack(Material.LEATHER_CHESTPLATE).color(0x62A391), null)
		items = arrayOf(ItemStack(Material.SLIME_BALL).rename("Bolha"))

		ability = BubbleAbility

		isDummy = false
		isBroadcast = true

		loadData()
	}
}
