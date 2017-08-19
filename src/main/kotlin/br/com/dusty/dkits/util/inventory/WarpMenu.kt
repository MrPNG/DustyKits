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
import java.util.*

object WarpMenu {

	val TITLE_MAIN = Text.of("Warps").color(TextColor.GOLD).toString()
	val TITLE_GAME = Text.of("Warps de Jogo").color(TextColor.GOLD).toString()
	val TITLE_EVENT = Text.of("Warps de Evento").color(TextColor.GOLD).toString()

	val BUTTON_GAME = ItemStack(Material.DIAMOND_SWORD).rename(TITLE_GAME)
	val BUTTON_EVENT = ItemStack(Material.CAKE).rename(TITLE_EVENT)

	fun menuWarpMain(player: Player): Inventory {
		val inventory = Bukkit.createInventory(player, 27, TITLE_MAIN)

		inventory.fill(false)

		inventory.setItem(11, BUTTON_GAME)
		inventory.setItem(15, BUTTON_EVENT)

		return inventory
	}

	fun menuWarpGame(player: Player): Inventory {
		val inventory = Bukkit.createInventory(player, 27, TITLE_GAME)

		inventory.fill(true)

		val warps = ArrayList<Warp>()

		for (warp in Warps.WARPS)
			if (warp.warpType === Warp.EnumWarpType.GAME && warp.data.isEnabled)
				warps.add(warp)

		if (warps.size > 0)
			when (warps.size) {
				1    -> inventory.setItem(13, warps[0].icon)
				2    -> for (i in 0 .. 1)
					inventory.setItem(12 + i * 2, warps[i].icon)
				3    -> for (i in 0 .. 2)
					inventory.setItem(11 + i * 2, warps[i].icon)
				4    -> for (i in 0 .. 3)
					inventory.setItem(10 + i * 2, warps[i].icon)
				5    -> for (i in 0 .. 4)
					inventory.setItem(13 + i, warps[i].icon)
				else -> for (i in 0 .. 4)
					inventory.setItem(10 + i, warps[i].icon)
			}

		return inventory
	}

	fun menuWarpEvent(player: Player): Inventory {
		val inventory = Bukkit.createInventory(player, 27, TITLE_EVENT)

		inventory.fill(true)

		val warps = Warps.WARPS.filterTo(ArrayList<Warp>()) { it.warpType == Warp.EnumWarpType.EVENT && it.data.isEnabled }

		if (warps.size > 0)
			when (warps.size) {
				1    -> inventory.setItem(13, warps[0].icon)
				2    -> for (i in 0 .. 1)
					inventory.setItem(12 + i * 2, warps[i].icon)
				3    -> for (i in 0 .. 2)
					inventory.setItem(11 + i * 2, warps[i].icon)
				4    -> for (i in 0 .. 3)
					inventory.setItem(10 + i * 2, warps[i].icon)
				5    -> for (i in 0 .. 4)
					inventory.setItem(13 + i, warps[i].icon)
				else -> for (i in 0 .. 4)
					inventory.setItem(10 + i, warps[i].icon)
			}

		return inventory
	}
}
