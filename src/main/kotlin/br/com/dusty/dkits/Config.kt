package br.com.dusty.dkits

import com.google.gson.GsonBuilder
import org.bukkit.Bukkit
import java.io.File
import java.io.FileReader
import java.io.PrintWriter

object Config {

	val CONFIG_DIR = File(Bukkit.getWorldContainer(), "config").apply { if (!exists()) mkdirs() }

	val GSON = GsonBuilder().setPrettyPrinting().create()

	var data = Data()

	init {
		if (!CONFIG_DIR.exists()) CONFIG_DIR.mkdirs()
	}

	fun loadData() {
		val file = File(CONFIG_DIR, "config.json")

		if (file.exists()) data = GSON.fromJson(FileReader(file), data.javaClass)

		saveData()
	}

	fun saveData() {
		val file = File(CONFIG_DIR, "config.json")
		file.createNewFile()

		PrintWriter(file).use { it.println(GSON.toJson(data)) }
	}

	data class Data(var slots: Int = 120, var serverStatus: EnumServerStatus = EnumServerStatus.OFFLINE, var soups: Int = 0)

	enum class EnumServerStatus {

		ONLINE,
		OFFLINE,
		MAINTENANCE
	}
}
