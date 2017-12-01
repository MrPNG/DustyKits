package br.com.dusty.dkits.util.inventory

import br.com.dusty.dkits.util.enchant
import br.com.dusty.dkits.util.rename
import br.com.dusty.dkits.util.text.Text
import br.com.dusty.dkits.util.text.TextColor
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

fun Inventory.addItemStacks(itemStacks: Array<ItemStack?>) {
	var i = 0

	while (this.getItem(i) != null) i++

	for (itemStack in itemStacks) this.setItem(i++, itemStack)
}

/**
 * Aplica um [ItemStack][] contendo, nessa ordem, 'helmet', 'chestplate', 'leggings' e 'boots' como armadura de um [Player].
 *
 * @param player
 * @param itemStacks
 */
fun Player.setArmor(itemStacks: Array<ItemStack?>) {
	val playerInventory = this.inventory

	playerInventory.helmet = itemStacks[0]
	playerInventory.chestplate = itemStacks[1]
	playerInventory.leggings = itemStacks[2]
	playerInventory.boots = itemStacks[3]
}

fun Inventory.fill(backButton: Boolean): Inventory {
	for (i in 0 until this.size) this.setItem(i, Inventories.BACKGROUND)

	if (backButton) this.setItem(0, Inventories.BUTTON_BACK)

	return this
}

fun Player.openInventory(player: Player) {
	openInventory(player.inventory)
	sendMessage(Text.positivePrefix().basic("Você está ").positive("vendo").basic(" o inventário de ").positive(player.name).toString())
}

fun Player.fillSoups(): Inventory {
	(0 .. 35).filter { inventory.getItem(it) == null }.forEach { inventory.setItem(it, Inventories.SOUP) }

	return inventory
}

fun Player.fillRecraft(): Inventory {
	inventory.setItem(14, Inventories.RED_MUSHROOMS)
	inventory.setItem(15, Inventories.BROWN_MUSHROOMS)
	inventory.setItem(16, Inventories.BOWLS)

	return inventory
}

object Inventories {

	val SOUP = ItemStack(Material.MUSHROOM_SOUP)

	val RED_MUSHROOMS = ItemStack(Material.RED_MUSHROOM, 64)
	val BROWN_MUSHROOMS = ItemStack(Material.BROWN_MUSHROOM, 64)
	val BOWLS = ItemStack(Material.BOWL, 64)

	val SOUPS_TITLE = Text.of("Sopas").color(TextColor.GOLD).toString()
	val RECRAFT_TITLE = Text.of("Recraft").color(TextColor.GOLD).toString()

	val ARMOR_FULL_IRON: Array<ItemStack?> = arrayOf(ItemStack(Material.IRON_HELMET), ItemStack(Material.IRON_CHESTPLATE), ItemStack(Material.IRON_LEGGINGS), ItemStack(Material.IRON_BOOTS))

	val DIAMOND_SWORD = ItemStack(Material.DIAMOND_SWORD)
	val DIAMOND_SWORD_SHARPNESS = ItemStack(Material.DIAMOND_SWORD).enchant(1, Enchantment.DAMAGE_ALL)

	val BACKGROUND = ItemStack(Material.STAINED_GLASS_PANE, 1, 1.toShort(), 7.toByte()).rename(" ")

	val BUTTON_BACK = ItemStack(Material.CARPET, 1, 1.toShort(), 14.toByte()).rename(Text.negativeOf("Voltar").toString())

	fun soups(player: Player): Inventory {
		val inventory = Bukkit.createInventory(player, 54, SOUPS_TITLE)

		for (i in 0 .. 53) inventory.setItem(i, SOUP)

		return inventory
	}

	fun recraft(player: Player): Inventory {
		val inventory = Bukkit.createInventory(player, 54, RECRAFT_TITLE)

		for (i in 0 .. 17) {
			inventory.setItem(i * 3, RED_MUSHROOMS)
			inventory.setItem(i * 3 + 1, BROWN_MUSHROOMS)
			inventory.setItem(i * 3 + 2, BOWLS)
		}

		return inventory
	}
}
