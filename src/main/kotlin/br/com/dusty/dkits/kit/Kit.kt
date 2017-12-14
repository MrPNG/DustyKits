package br.com.dusty.dkits.kit

import br.com.dusty.dkits.Main
import br.com.dusty.dkits.ability.Ability
import br.com.dusty.dkits.util.Tasks
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import java.io.File
import java.io.FileReader
import java.io.PrintWriter

open class Kit(var name: String = "None",
               var description: String = "",
               var icon: ItemStack = ItemStack(Material.STONE_SWORD),
               var weapon: ItemStack? = null,
               var armor: Array<ItemStack?> = arrayOf(null, null, null, null),
               var items: Array<ItemStack?> = arrayOf(),
               var ability: Ability = Ability(),
               var isDummy: Boolean = true,
               var isBroadcast: Boolean = false,
               var data: Data = Data()) {

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

		if (file.exists()) data = Main.GSON.fromJson(FileReader(file), Kit.Data::class.java)
		else saveData()
	}

	fun saveData() {
		val dir = File(Main.CONFIG_DIR, "kit")
		val file = File(dir, name.toLowerCase() + ".json")

		dir.mkdirs()
		file.createNewFile()

		PrintWriter(file).use { println(Main.GSON.toJson(data)) }
	}

	override fun equals(other: Any?) = when {
		this === other                -> true
		javaClass == other?.javaClass -> true
		else                          -> false
	}

	override fun toString(): String {
		return "Kit(name='$name')"
	}

	data class Data(var price: Int = -1, var isEnabled: Boolean = false)
}
