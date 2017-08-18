package br.com.dusty.dkits.util.inventory

import br.com.dusty.dkits.util.ItemStackUtils
import br.com.dusty.dkits.util.text.Text
import br.com.dusty.dkits.util.text.TextColor
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.PlayerInventory

object InventoryUtils {

	val SOUP = ItemStack(Material.MUSHROOM_SOUP)

	val RED_MUSHROOMS = ItemStack(Material.RED_MUSHROOM, 64)
	val BROWN_MUSHROOMS = ItemStack(Material.BROWN_MUSHROOM, 64)
	val BOWLS = ItemStack(Material.BOWL, 64)

	private val SOUPS_TITLE = Text.of("Sopas").color(TextColor.GOLD).toString()
	private val RECRAFT_TITLE = Text.of("Recraft").color(TextColor.GOLD).toString()

	private val ARMOR_FULL_IRON = arrayOf(ItemStack(Material.IRON_HELMET),
	                                      ItemStack(Material.IRON_CHESTPLATE),
	                                      ItemStack(Material.IRON_LEGGINGS),
	                                      ItemStack(Material.IRON_BOOTS))

	val BACKGROUND = ItemStackUtils.rename(ItemStack(Material.STAINED_GLASS_PANE,
	                                                 1,
	                                                 1.toShort(),
	                                                 7.toByte()), " ")

	val BUTTON_BACK = ItemStackUtils.rename(ItemStack(Material.CARPET, 1, 1.toShort(), 14.toByte()),
	                                        Text.negativeOf("Voltar").toString())

	fun soups(player: Player): Inventory {
		val inventory = Bukkit.createInventory(player, 54, SOUPS_TITLE)

		for (i in 0 .. 53)
			inventory.setItem(i, SOUP)

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

	fun addItemStacks(inventory: Inventory, itemStacks: Array<ItemStack>) {
		var i = 0

		while (inventory.getItem(i) != null)
			i++

		for (itemStack in itemStacks)
			inventory.setItem(i++, itemStack)
	}

	/**
	 * Aplica um [ItemStack][] contendo, nessa ordem, 'helmet', 'chestplate', 'leggings' e 'boots' como armadura de um [Player].
	 *
	 * @param player
	 * @param itemStacks
	 */
	fun setArmor(player: Player, itemStacks: Array<ItemStack>) {
		val playerInventory = player.inventory

		playerInventory.helmet = itemStacks[0]
		playerInventory.chestplate = itemStacks[1]
		playerInventory.leggings = itemStacks[2]
		playerInventory.boots = itemStacks[3]
	}

	fun basic(inventory: Inventory, backButton: Boolean): Inventory {
		for (i in 0 .. inventory.size - 1)
			inventory.setItem(i, BACKGROUND)

		if (backButton)
			inventory.setItem(0, BUTTON_BACK)

		return inventory
	}
}
