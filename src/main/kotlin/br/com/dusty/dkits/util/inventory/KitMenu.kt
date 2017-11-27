package br.com.dusty.dkits.util.inventory

import br.com.dusty.dkits.gamer.EnumMode
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

object KitMenu {

	val TITLE_OWNED = Text.of("Seus Kits").color(TextColor.GOLD).toString()
	val TITLE_OTHER = Text.of("Outros Kits").color(TextColor.GOLD).toString()

	val BUTTON_OWNED = ItemStack(Material.CHEST).rename(TITLE_OWNED)
	val BUTTON_OTHER = ItemStack(Material.GOLD_INGOT).rename(TITLE_OTHER)

	fun menuKitOwned(player: Player): Inventory {
		val gamer = player.gamer()

		val inventory = Bukkit.createInventory(player, 54, TITLE_OWNED)

		inventory.fill(false)
		inventory.fillKits(Kits.KITS.filter { (gamer.hasKit(it) && it.data.isEnabled && it in gamer.warp.enabledKits) || gamer.mode == EnumMode.ADMIN })

		inventory.setItem(8, BUTTON_OTHER)

		return inventory
	}

	fun menuKitOther(player: Player): Inventory {
		val gamer = player.gamer()

		val inventory = Bukkit.createInventory(player, 54, TITLE_OTHER)

		inventory.fill(false)
		inventory.fillKits(Kits.KITS.filter { !gamer.hasKit(it) && it.data.isEnabled && it in gamer.warp.enabledKits })

		inventory.setItem(0, BUTTON_OWNED)

		return inventory
	}

	fun Inventory.fillKits(kits: List<Kit>) {
		for (i in 0 until kits.size) setItem(10 + i, kits[i].icon)
	}
}
