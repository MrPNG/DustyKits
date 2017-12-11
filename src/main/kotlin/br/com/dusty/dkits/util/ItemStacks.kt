package br.com.dusty.dkits.util

import org.bukkit.*
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.LeatherArmorMeta
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.inventory.meta.SkullMeta
import org.bukkit.material.Dye
import org.bukkit.potion.PotionData
import org.bukkit.potion.PotionType

fun ItemStack.hasName(name: String): Boolean {
	val itemMeta = this.itemMeta
	if (itemMeta != null && itemMeta.hasDisplayName()) return itemMeta.displayName.clearFormatting() == name

	return false
}

fun ItemStack.rename(name: String): ItemStack {
	val itemMeta = this.itemMeta
	if (itemMeta != null) itemMeta.displayName = name

	this.itemMeta = itemMeta

	return this
}

fun ItemStack.enchant(level: Int, vararg enchantments: Enchantment): ItemStack? {
	for (enchantment in enchantments) this.addUnsafeEnchantment(enchantment, level)

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

fun ItemStack.setDescription(description: List<String>): ItemStack {
	val itemMeta = this.itemMeta
	itemMeta.lore = description

	this.itemMeta = itemMeta

	return this
}

fun ItemStack.setDescription(description: String): ItemStack = setDescription(description.fancySplit(32))

/**
 * Retorna o **'displayName'** não-formatado de uma [ItemStack], se houver, ou o **name()** de seu [org.bukkit.Material], caso contrário.
 *
 * @param itemStack
 * @return **'displayName'** não-formatado de uma [ItemStack], se houver, ou o **name()** de seu [org.bukkit.Material], caso contrário.
 * Pode, ainda, retornar **null** se a [ItemStack] for 'null'.
 */
fun ItemStack.getUnformattedDisplayName(): String? {
	val itemMeta = this.itemMeta

	val displayName = if (itemMeta != null && itemMeta.hasDisplayName()) itemMeta.displayName
	else this.type.name

	return displayName.clearFormatting()
}

/**
 * Criação/personalização de [org.bukkit.inventory.ItemStack]
 */
object ItemStacks {

	fun dye(c: DyeColor): ItemStack {
		val dye = Dye()
		dye.color = c

		return dye.toItemStack(1)
	}

	fun potions(amount: Int, extended: Boolean, upgraded: Boolean, potionType: PotionType, splash: Boolean): ItemStack {
		val itemStack = ItemStack(Material.POTION, amount)

		val potionMeta = itemStack.itemMeta as PotionMeta
		potionMeta.basePotionData = PotionData(potionType, extended, upgraded)

		itemStack.itemMeta = potionMeta

		return itemStack
	}

	fun skull(player: Player): ItemStack = ItemStack(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal.toShort()).apply {
		val skullMeta = itemMeta as SkullMeta
		skullMeta.owningPlayer = player

		itemMeta = skullMeta
	}

	fun skull(name: String): ItemStack = ItemStack(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal.toShort()).apply {
		val skullMeta = itemMeta as SkullMeta
		skullMeta.owningPlayer = Bukkit.getOfflinePlayer(name)

		itemMeta = skullMeta
	}
}
