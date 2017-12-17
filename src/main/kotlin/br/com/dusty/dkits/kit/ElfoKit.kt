package br.com.dusty.dkits.kit

import br.com.dusty.dkits.ability.ElfoAbility
import br.com.dusty.dkits.util.color
import br.com.dusty.dkits.util.description
import br.com.dusty.dkits.util.enchant
import br.com.dusty.dkits.util.rename
import br.com.dusty.dkits.util.text.Text
import br.com.dusty.dkits.util.text.TextColor
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

object ElfoKit: Kit() {

	init {
		name = "Elfo"

		icon = ItemStack(Material.BOW).enchant(Pair(Enchantment.ARROW_INFINITE, 1))
		icon.rename(Text.of(name).color(TextColor.GOLD).toString())
		icon.description(description, true)

		weapon = ItemStack(Material.GOLD_SWORD)
		armor = arrayOf(null, ItemStack(Material.LEATHER_CHESTPLATE).color(0xBA8B00), null, null)
		items = arrayOf(ItemStack(Material.BOW).enchant(Pair(Enchantment.ARROW_INFINITE, 1)).rename("Arco do Elfo"),
		                ItemStack(Material.ARROW).enchant(Pair(Enchantment.ARROW_INFINITE, 1)).rename("Flecha do Elfo"))

		ability = ElfoAbility

		isDummy = false
		isBroadcast = true

		loadData()
	}
}
