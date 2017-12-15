package br.com.dusty.dkits.kit

import br.com.dusty.dkits.util.color
import br.com.dusty.dkits.util.enchant
import br.com.dusty.dkits.util.rename
import br.com.dusty.dkits.util.setDescription
import br.com.dusty.dkits.util.text.Text
import br.com.dusty.dkits.util.text.TextColor
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

object GrandpaKit: Kit() {

	init {
		name = "Grandpa"

		icon = ItemStack(Material.STICK).enchant(Pair(Enchantment.KNOCKBACK, 2), Pair(Enchantment.PROTECTION_FALL, 99))
		icon.rename(Text.of(name).color(TextColor.GOLD).toString())
		icon.setDescription(description, true)

		weapon = ItemStack(Material.GOLD_SWORD)
		armor = arrayOf(null, ItemStack(Material.LEATHER_CHESTPLATE).color(0x928C85), null, null)
		items = arrayOf(ItemStack(Material.STICK).enchant(Pair(Enchantment.KNOCKBACK, 2), Pair(Enchantment.PROTECTION_FALL, 99)).rename("Bengala"))

		isDummy = false
		isBroadcast = true

		loadData()
	}
}
