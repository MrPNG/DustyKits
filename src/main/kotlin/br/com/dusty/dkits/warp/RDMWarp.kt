package br.com.dusty.dkits.warp

import br.com.dusty.dkits.kit.Kit
import br.com.dusty.dkits.util.description
import br.com.dusty.dkits.util.enchant
import br.com.dusty.dkits.util.inventory.Inventories
import br.com.dusty.dkits.util.rename
import br.com.dusty.dkits.util.text.Text
import br.com.dusty.dkits.util.text.TextColor
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

object RDMWarp: Warp() {

	init {
		name = "Rei da Mesa"

		icon = ItemStack(Material.DIAMOND_SWORD).enchant(Pair(Enchantment.DAMAGE_ALL, 1))
		icon.rename(Text.of(name).color(TextColor.GOLD).toString())
		icon.description(description, true)

		type = EnumWarpType.EVENT

		entryKit = Kit(weapon = Inventories.DIAMOND_SWORD_SHARPNESS,
		               armor = Inventories.ARMOR_FULL_IRON,
		               items = arrayOf(Inventories.RED_MUSHROOMS, Inventories.BROWN_MUSHROOMS, Inventories.BOWLS),
		               isDummy = false)

		loadData()
	}
}
