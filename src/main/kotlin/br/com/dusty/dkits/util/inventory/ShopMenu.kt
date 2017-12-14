package br.com.dusty.dkits.util.inventory

import br.com.dusty.dkits.gamer.gamer
import br.com.dusty.dkits.kit.Kit
import br.com.dusty.dkits.kit.Kits
import br.com.dusty.dkits.util.rename
import br.com.dusty.dkits.util.setDescription
import br.com.dusty.dkits.util.text.Text
import br.com.dusty.dkits.util.text.TextColor
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

object ShopMenu {

	val TITLE_MAIN = Text.of("Shop").color(TextColor.GOLD).toString()
	val TITLE_KITS = Text.of("Comprar Kits").color(TextColor.GOLD).toString()
	val TITLE_ARMOR = Text.of("Comprar Armaduras").color(TextColor.GOLD).toString()

	val BUTTON_KITS = ItemStack(Material.CHEST).rename(TITLE_KITS)
	val BUTTON_ARMOR = ItemStack(Material.IRON_CHESTPLATE).rename(TITLE_ARMOR)

	val ALL_KITS = ItemStack(Material.YELLOW_FLOWER).rename(Text.of("Você já possui todos os kits!").color(TextColor.GOLD).toString())

	val ARMOR = linkedMapOf(ItemStack(Material.CHAINMAIL_CHESTPLATE).setDescription("Preço: " + 3000 + " créditos", true) to 3000,
	                        ItemStack(Material.CHAINMAIL_LEGGINGS).setDescription("Preço: " + 4500 + " créditos", true) to 4500,
	                        ItemStack(Material.IRON_CHESTPLATE).setDescription("Preço: " + 6000 + " créditos", true) to 6000,
	                        ItemStack(Material.IRON_BOOTS).setDescription("Preço: " + 3000 + " créditos", true) to 3000,
	                        ItemStack(Material.DIAMOND_HELMET).setDescription("Preço: " + 6000 + " créditos", true) to 6000)

	fun menuShopMain(player: Player): Inventory = Bukkit.createInventory(player, 27, TITLE_MAIN).apply {
		fillBackground(false)

		setItem(11, BUTTON_KITS)
		setItem(15, BUTTON_ARMOR)
	}

	fun menuShopKit(player: Player): Inventory = Bukkit.createInventory(player, 27, TITLE_KITS).apply {
		fillBackground(true)
		fillKits(Kits.KITS.filter { it.data.isEnabled && it.data.price != -1 && player.gamer().hasKit(it) })

		setItem(0, Inventories.BUTTON_BACK)
	}

	fun menuShopArmor(player: Player): Inventory = Bukkit.createInventory(player, 27, TITLE_ARMOR).apply {
		fillBackground(true)
		fillArmor(ARMOR.keys.toTypedArray())

		setItem(0, Inventories.BUTTON_BACK)
	}

	fun Inventory.fillKits(kits: List<Kit>) {
		if (kits.isNotEmpty()) when (kits.size) {
			1    -> setItem(13, kits[0].icon.setDescription("Preço: " + kits[0].data.price + " créditos", true))
			2    -> for (i in 0 .. 1) setItem(12 + i * 2, kits[i].icon.setDescription("Preço: " + kits[i].data.price + " créditos", true))
			3    -> for (i in 0 .. 2) setItem(11 + i * 2, kits[i].icon.setDescription("Preço: " + kits[i].data.price + " créditos", true))
			4    -> for (i in 0 .. 3) setItem(10 + i * 2, kits[i].icon.setDescription("Preço: " + kits[i].data.price + " créditos", true))
			5    -> for (i in 0 .. 4) setItem(13 + i, kits[i].icon.setDescription("Preço: " + kits[i].data.price + " créditos", true))
			else -> for (i in 0 .. 4) setItem(10 + i, kits[i].icon.setDescription("Preço: " + kits[i].data.price + " créditos", true))
		}
		else setItem(13, ALL_KITS)
	}

	fun Inventory.fillArmor(items: Array<ItemStack>) {
		if (items.isNotEmpty()) when (items.size) {
			1    -> setItem(13, items[0])
			2    -> for (i in 0 .. 1) setItem(12 + i * 2, items[i])
			3    -> for (i in 0 .. 2) setItem(11 + i * 2, items[i])
			4    -> for (i in 0 .. 3) setItem(10 + i * 2, items[i])
			5    -> for (i in 0 .. 4) setItem(11 + i, items[i])
			else -> for (i in 0 until items.size) setItem(10 + i, items[i])
		}
	}
}
