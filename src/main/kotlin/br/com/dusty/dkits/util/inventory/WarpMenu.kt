package br.com.dusty.dkits.util.inventory

import br.com.dusty.dkits.util.rename
import br.com.dusty.dkits.util.text.Text
import br.com.dusty.dkits.util.text.TextColor
import br.com.dusty.dkits.warp.Warp
import br.com.dusty.dkits.warp.Warps
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

object WarpMenu {

	val TITLE_MAIN = Text.of("Warps").color(TextColor.GOLD).toString()
	val TITLE_GAME = Text.of("Warps de Jogo").color(TextColor.GOLD).toString()
	val TITLE_EVENT = Text.of("Warps de Evento").color(TextColor.GOLD).toString()

	val BUTTON_GAME = ItemStack(Material.DIAMOND_SWORD).rename(TITLE_GAME)
	val BUTTON_EVENT = ItemStack(Material.CAKE).rename(TITLE_EVENT)

	fun menuWarpMain(player: Player): Inventory = Bukkit.createInventory(player, 27, TITLE_MAIN).apply {
		fillBackground(false)

		setItem(11, BUTTON_GAME)
		setItem(15, BUTTON_EVENT)
	}

	fun menuWarpGame(player: Player): Inventory {
		val warps = Warps.WARPS.filter { it.type == Warp.EnumWarpType.GAME && it.data.isEnabled }

		return Bukkit.createInventory(player, 27 + Math.floor(warps.size / 7.0).toInt() * 9, TITLE_GAME).apply {
			fillBackground(true)
			fillWarps(warps)

			setItem(0, Inventories.BUTTON_BACK)
		}
	}

	fun menuWarpEvent(player: Player): Inventory {
		val warps = Warps.WARPS.filter { (it.type == Warp.EnumWarpType.EVENT || it.type == Warp.EnumWarpType.MINIGAME) && it.data.isEnabled }

		return Bukkit.createInventory(player, 27 + Math.floor(warps.size / 7.0).toInt() * 9, TITLE_EVENT).apply {
			fillBackground(true)
			fillWarps(warps)

			setItem(0, Inventories.BUTTON_BACK)
		}
	}

	fun Inventory.fillWarps(warps: List<Warp>) {
		if (warps.isNotEmpty()) when (warps.size) {
			1    -> setItem(13, warps[0].icon)
			2    -> for (i in 0 .. 1) setItem(12 + i * 2, warps[i].icon)
			3    -> for (i in 0 .. 2) setItem(11 + i * 2, warps[i].icon)
			4    -> for (i in 0 .. 3) setItem(10 + i * 2, warps[i].icon)
			5    -> for (i in 0 .. 4) setItem(11 + i, warps[i].icon)
			else -> {
				var i = -1

				warps.forEach {
					i += if (i + 2 % 9 == 0) 3 else 1

					setItem(10 + i, it.icon)
				}
			}
		}
	}
}
