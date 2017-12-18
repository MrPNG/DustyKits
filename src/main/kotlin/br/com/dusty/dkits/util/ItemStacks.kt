package br.com.dusty.dkits.util

import br.com.dusty.dkits.util.text.Text
import br.com.dusty.dkits.util.text.TextColor
import org.bukkit.Color
import org.bukkit.DyeColor
import org.bukkit.Material.SKULL_ITEM
import org.bukkit.OfflinePlayer
import org.bukkit.SkullType
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.LeatherArmorMeta
import org.bukkit.inventory.meta.SkullMeta
import org.bukkit.material.Dye
import org.bukkit.potion.Potion
import org.bukkit.potion.PotionType

fun ItemStack.rename(name: String): ItemStack {
	val itemMeta = this.itemMeta
	if (itemMeta != null) itemMeta.displayName = name

	this.itemMeta = itemMeta

	return this
}

fun ItemStack.enchant(vararg enchantments: Pair<Enchantment, Int>) = apply { enchantments.forEach { addUnsafeEnchantment(it.first, it.second) } }

fun ItemStack.color(color: Int) = color(Color.fromBGR(color))

fun ItemStack.color(color: Color) = apply {
	if (type.name.startsWith("LEATHER_")) {
		val leatherArmorMeta = itemMeta as LeatherArmorMeta
		leatherArmorMeta.color = color

		itemMeta = leatherArmorMeta
	}
}

fun ItemStack.description(description: List<String>, forceColor: Boolean) = apply {
	val itemMeta = itemMeta
	itemMeta.lore = if (forceColor) description.map { Text.of(it.clearFormatting()).color(TextColor.YELLOW).toString() } else description

	this.itemMeta = itemMeta
}

fun ItemStack.description(description: String, forceColor: Boolean): ItemStack = description(description.fancySplit(32), forceColor)

/**
 * Retorna o **'displayName'** não-formatado de uma [ItemStack], se houver, ou o **name()** de seu [org.bukkit.Material], caso contrário.
 *
 * @param itemStack
 * @return **'displayName'** não-formatado de uma [ItemStack], se houver, ou o **name()** de seu [org.bukkit.Material], caso contrário.
 * Pode, ainda, retornar **null** se a [ItemStack] for 'null'.
 */
fun ItemStack.unformattedDisplayName(): String? {
	val itemMeta = this.itemMeta

	val displayName = if (itemMeta != null && itemMeta.hasDisplayName()) itemMeta.displayName
	else this.type.name

	return displayName.clearFormatting()
}

fun OfflinePlayer.skull() = ItemStack(SKULL_ITEM, 1, SkullType.PLAYER.ordinal.toShort()).also {
	val skullMeta = it.itemMeta as SkullMeta
	skullMeta.owner = name

	it.itemMeta = skullMeta
}

infix fun ItemStack.isSimilarTo(itemStack: ItemStack?) = isSimilar(itemStack)

/**
 * Criação/personalização de [org.bukkit.inventory.ItemStack]
 */
object ItemStacks {

	fun dye(color: DyeColor): ItemStack {
		val dye = Dye()
		dye.color = color

		return dye.toItemStack(1)
	}

	fun potions(amount: Int, extended: Boolean, upgraded: Boolean, potionType: PotionType, splash: Boolean) = Potion(potionType, if (upgraded) 2 else 1, splash, extended).toItemStack(amount)
}
