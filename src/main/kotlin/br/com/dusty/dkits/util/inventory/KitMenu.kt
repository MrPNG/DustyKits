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

		return Bukkit.createInventory(player, 54, TITLE_OWNED).apply {
			fill(false)
			fillKits(Kits.KITS.filter { (gamer.hasKit(it) && it.data.isEnabled && it in gamer.warp.enabledKits) || gamer.mode == EnumMode.ADMIN })

			setItem(8, BUTTON_OTHER)
		}
	}

	fun menuKitOther(player: Player): Inventory {
		val gamer = player.gamer()

		return Bukkit.createInventory(player, 54, TITLE_OTHER).apply {
			fill(false)
			fillKits(Kits.KITS.filter { !gamer.hasKit(it) && it.data.isEnabled && it in gamer.warp.enabledKits })

			setItem(0, BUTTON_OWNED)
		}
	}

	fun Inventory.fillKits(kits: List<Kit>) {
		var i = -1

		kits.forEach {
			i += if (i + 2 % 9 == 0) 3 else 1

			setItem(10 + i, it.icon)
		}
	}
}
