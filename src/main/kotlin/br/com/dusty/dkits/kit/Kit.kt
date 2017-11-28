package br.com.dusty.dkits.kit

import br.com.dusty.dkits.Main
import br.com.dusty.dkits.ability.Ability
import br.com.dusty.dkits.gamer.EnumMode
import br.com.dusty.dkits.gamer.Gamer
import br.com.dusty.dkits.util.Tasks
import br.com.dusty.dkits.util.inventory.addItemStacks
import br.com.dusty.dkits.util.inventory.setArmor
import br.com.dusty.dkits.util.text.Text
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import java.io.*

open class Kit {

	var name = "None"
	var description = ""
	var icon = ItemStack(Material.STONE_SWORD)

	var weapon: ItemStack? = null
	var armor = arrayOf<ItemStack?>(null, null, null, null)
	var items = arrayOf<ItemStack?>()

	var ability = Ability()

	var isDummy = true
	var isBroadcast = false

	var data = Data()

	//TODO: If not on MiniHG
	fun isAllowed(gamer: Gamer, announce: Boolean): Boolean = when {
		gamer.mode != EnumMode.ADMIN && !gamer.kit.isDummy                     -> {
			if (announce) gamer.player.sendMessage(Text.negativePrefix().basic("Você ").negative("já").basic(" está ").negative("usando").basic(" um kit!").toString())

			false
		}
		gamer.mode != EnumMode.ADMIN && !gamer.warp.enabledKits.contains(this) -> {
			if (announce) gamer.player.sendMessage(Text.negativePrefix().basic("Você ").negative("não pode").basic(" usar o kit ").negative(name).basic(" nesta warp!").toString())

			false
		}
		gamer.mode != EnumMode.ADMIN && !gamer.hasKit(this)                    -> {
			if (announce) gamer.player.sendMessage(Text.negativePrefix().basic("Você ").negative("não").basic(" possui o kit ").negative(name).basic("!").toString())

			false
		}
		else                                                                   -> true
	}

	fun setEnabled(enabled: Boolean): Boolean {
		if (data.isEnabled == enabled) return false

		data.isEnabled = enabled

		Tasks.async(Runnable { this.saveData() })

		return true
	}

	fun setPrice(price: Int): Boolean {
		if (data.price == price) return false

		data.price = price

		Tasks.async(Runnable { this.saveData() })

		return true
	}

	fun loadData() {
		val dir = File(Main.CONFIG_DIR, "kit")
		val file = File(dir, name.toLowerCase() + ".json")

		if (file.exists()) try {
			data = Main.GSON.fromJson(FileReader(file), Kit.Data::class.java)
		} catch (e: FileNotFoundException) {
			e.printStackTrace()
		}
		else saveData()
	}

	fun saveData() {
		val dir = File(Main.CONFIG_DIR, "kit")
		val file = File(dir, name.toLowerCase() + ".json")

		var printWriter: PrintWriter? = null

		try {
			dir.mkdirs()
			file.createNewFile()

			printWriter = PrintWriter(file)
			printWriter.println(Main.GSON.toJson(data))
		} catch (e: IOException) {
			e.printStackTrace()
		} finally {
			if (printWriter != null) printWriter.close()
		}
	}

	data class Data(var price: Int = -1, var isEnabled: Boolean = false)
}
