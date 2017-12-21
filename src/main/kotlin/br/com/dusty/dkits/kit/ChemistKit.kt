package br.com.dusty.dkits.kit

import br.com.dusty.dkits.ability.ChemistAbility
import br.com.dusty.dkits.util.ItemStacks
import br.com.dusty.dkits.util.color
import br.com.dusty.dkits.util.description
import br.com.dusty.dkits.util.rename
import br.com.dusty.dkits.util.text.Text
import br.com.dusty.dkits.util.text.TextColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionType

object ChemistKit: Kit() {

	init {
		name = "Chemist"

		icon = ItemStacks.potions(1, false, false, PotionType.INSTANT_DAMAGE, false)
		icon.rename(Text.of(name).color(TextColor.GOLD).toString())
		icon.description(description, true)

		weapon = ItemStack(Material.STONE_SWORD)
		armor = arrayOf(null, null, ItemStack(Material.LEATHER_CHESTPLATE).color(0xB3AA96), null)
		items = arrayOf(ItemStacks.potions(3, false, false, PotionType.INSTANT_DAMAGE, true),
		                ItemStacks.potions(3, false, false, PotionType.POISON, true),
		                ItemStacks.potions(3, false, false, PotionType.WEAKNESS, true))

		ability = ChemistAbility

		isDummy = false
		isBroadcast = true

		loadData()
	}
}
