package br.com.dusty.dkits.kit

import br.com.dusty.dkits.ability.RingAbility
import br.com.dusty.dkits.util.color
import br.com.dusty.dkits.util.description
import br.com.dusty.dkits.util.enchant
import br.com.dusty.dkits.util.rename
import br.com.dusty.dkits.util.text.Text
import br.com.dusty.dkits.util.text.TextColor
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

object RingKit: Kit() {

	init {
		name = "Ring"

		icon = ItemStack(Material.GOLD_BOOTS).enchant(Pair(Enchantment.DURABILITY, 1))
		icon.rename(Text.of(name).color(TextColor.GOLD).toString())
		icon.description(description, true)

		weapon = ItemStack(Material.STONE_SWORD)
		armor = arrayOf(null, ItemStack(Material.LEATHER_CHESTPLATE).color(0xFFE100), null, null)
		items = arrayOf(ItemStack(Material.GOLD_BOOTS).enchant(Pair(Enchantment.DURABILITY, 1)).rename("Botas de Invisibilidade"))

		ability = RingAbility

		isDummy = false
		isBroadcast = true

		loadData()
	}
}
