package br.com.dusty.dkits.util

import br.com.dusty.dkits.util.text.Text
import org.bukkit.Color
import org.bukkit.DyeColor
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.LeatherArmorMeta
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.material.Dye
import org.bukkit.potion.PotionData
import org.bukkit.potion.PotionType

fun ItemStack.hasName(name: String): Boolean {
	val itemMeta = this.itemMeta
	if (itemMeta != null && itemMeta.hasDisplayName())
		return Text.clearFormatting(itemMeta.displayName) == name

	return false
}

fun ItemStack.rename(name: String): ItemStack? {
	val itemMeta = this.itemMeta
	if (itemMeta != null)
		itemMeta.displayName = name

	this.itemMeta = itemMeta

	return this
}

fun ItemStack.enchant(level: Int, vararg enchantments: Enchantment): ItemStack? {
	for (enchantment in enchantments)
		this.addUnsafeEnchantment(enchantment, level)

	return this
}

fun ItemStack.color(color: Color): ItemStack? {
	if (this.type.name.startsWith("LEATHER_")) {
		val leatherArmorMeta = this.itemMeta as LeatherArmorMeta
		leatherArmorMeta.color = color

		this.itemMeta = leatherArmorMeta
	}

	return this
}

fun ItemStack.setDescription(description: String): ItemStack {
	val itemMeta = this.itemMeta
	itemMeta.lore = description.fancySplit(32)

	this.itemMeta = itemMeta

	return this
}

/**
 * Retorna o **'displayName'** não-formatado de uma [ItemStack], se houver, ou o **name()** de seu [org.bukkit.Material], caso contrário.
 *
 * @param itemStack
 * @return **'displayName'** não-formatado de uma [ItemStack], se houver, ou o **name()** de seu [org.bukkit.Material], caso contrário.
 * Pode, ainda, retornar **null** se a [ItemStack] for 'null'.
 */
fun ItemStack.getUnformattedDisplayName(): String? {
	val itemMeta = this.itemMeta

	val displayName = if (itemMeta != null && itemMeta.hasDisplayName())
		itemMeta.displayName
	else
		this.type.name

	return Text.clearFormatting(displayName)
}

/**
 * Criação/personalização de [org.bukkit.inventory.ItemStack]
 */
object ItemStackUtils {

	fun dye(c: DyeColor): ItemStack {
		val dye = Dye()
		dye.color = c

		return dye.toItemStack(1)
	}

	fun potions(amount: Int, extended: Boolean, upgraded: Boolean, potionType: PotionType, splash: Boolean): ItemStack {
		val itemStack = ItemStack(Material.POTION, amount)

		val potionData = PotionData(potionType, extended, upgraded)

		val potionMeta = itemStack.itemMeta as PotionMeta
		potionMeta.basePotionData = potionData

		itemStack.itemMeta = potionMeta

		return itemStack
	}
}
