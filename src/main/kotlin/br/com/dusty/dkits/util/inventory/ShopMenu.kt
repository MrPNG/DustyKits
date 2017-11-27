package br.com.dusty.dkits.util.inventory

import br.com.dusty.dkits.gamer.gamer
import br.com.dusty.dkits.kit.Kit
import br.com.dusty.dkits.kit.Kits
import br.com.dusty.dkits.util.rename
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

	val ARMOR = HashMap<ItemStack, Int>()

	init {
		ARMOR.put(ItemStack(Material.CHAINMAIL_CHESTPLATE), 3500)
		ARMOR.put(ItemStack(Material.CHAINMAIL_LEGGINGS), 3000)
		ARMOR.put(ItemStack(Material.IRON_CHESTPLATE), 6000)
		ARMOR.put(ItemStack(Material.IRON_BOOTS), 5000)
		ARMOR.put(ItemStack(Material.DIAMOND_HELMET), 5500)
	}

	fun menuShopMain(player: Player): Inventory {
		val inventory = Bukkit.createInventory(player, 27, TITLE_MAIN)

		inventory.fill(false)

		inventory.setItem(11, BUTTON_KITS)
		inventory.setItem(15, BUTTON_ARMOR)

		return inventory
	}

	fun menuShopKit(player: Player): Inventory {
		val inventory = Bukkit.createInventory(player, 27, TITLE_KITS)

		inventory.fill(true)
		inventory.fillKits(Kits.KITS.filter { it.data.isEnabled && it.data.price != -1 && player.gamer().hasKit(it) })

		return inventory
	}

	fun menuShopArmor(player: Player): Inventory {
		val inventory = Bukkit.createInventory(player, 27, TITLE_ARMOR)

		inventory.fill(true)
		inventory.fillArmor(ARMOR.keys.toTypedArray())

		return inventory
	}

	fun Inventory.fillKits(kits: List<Kit>) {
		if (kits.isNotEmpty()) when (kits.size) {
			1    -> setItem(13, kits[0].icon)
			2    -> for (i in 0 .. 1) setItem(12 + i * 2, kits[i].icon)
			3    -> for (i in 0 .. 2) setItem(11 + i * 2, kits[i].icon)
			4    -> for (i in 0 .. 3) setItem(10 + i * 2, kits[i].icon)
			5    -> for (i in 0 .. 4) setItem(13 + i, kits[i].icon)
			else -> for (i in 0 .. 4) setItem(10 + i, kits[i].icon)
		}
		else setItem(13, ALL_KITS)
	}

	fun Inventory.fillArmor(items: Array<ItemStack>) {
		if (items.isNotEmpty()) when (items.size) {
			1    -> setItem(13, items[0])
			2    -> for (i in 0 .. 1) setItem(12 + i * 2, items[i])
			3    -> for (i in 0 .. 2) setItem(11 + i * 2, items[i])
			4    -> for (i in 0 .. 3) setItem(10 + i * 2, items[i])
			5    -> for (i in 0 .. 4) setItem(13 + i, items[i])
			else -> for (i in 0 .. 4) setItem(10 + i, items[i])
		}
	}
}
