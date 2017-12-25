package br.com.dusty.dkits.util.leaderboard

import br.com.dusty.dkits.Main
import br.com.dusty.dkits.util.SimpleLocation
import br.com.dusty.dkits.util.toSimpleLocation
import com.google.gson.reflect.TypeToken
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.EntityType
import java.io.File
import java.io.FileReader
import java.io.PrintWriter
import java.util.*

object Leaderboards {

	val LEADERBOARDS_TYPE_TOKEN = object: TypeToken<Collection<LeaderboardData>>() {}.type

	val leaderboards = arrayListOf<Leaderboard>()
	val leaderboardsData = arrayListOf<LeaderboardData>()

	fun registerAll() {
		loadData()

		leaderboardsData.forEach {
			val location = it.location.toLocation(Main.WORLD)

			val chunk = location.chunk
			chunk.load()

			val type = enumValueOf(it.type) as EnumLeaderboardType
			val amount = it.amount
			val descending = it.descending

			val entities = it.uuids.map { uuid ->
				(chunk.entities.firstOrNull { it.type == EntityType.ARMOR_STAND && it.uniqueId.toString() == uuid } ?: Main.WORLD.spawnEntity(location, EntityType.ARMOR_STAND)) as ArmorStand
			} as ArrayList<ArmorStand>

			leaderboards.add(Leaderboard(location, type, amount, descending, entities))
		}

		saveData()
	}

	fun loadData() {
		val file = File(Main.CONFIG_DIR, "leaderboards.json")

		if (file.exists()) leaderboardsData.addAll(Main.GSON.fromJson(FileReader(file), LEADERBOARDS_TYPE_TOKEN))

		saveData()
	}

	fun saveData() {
		val file = File(Main.CONFIG_DIR, "leaderboards.json")

		if (!file.exists()) file.createNewFile()

		PrintWriter(file).use {
			it.println(Main.GSON.toJson(leaderboards.map {
				LeaderboardData(it.location.toSimpleLocation(), it.type.name, it.amount, it.descending, it.entities.map { it.uniqueId.toString() }.toTypedArray())
			}))
		}
	}

	data class LeaderboardData(val location: SimpleLocation, val type: String, val amount: Int, val descending: Boolean, val uuids: Array<String>) {

		override fun equals(other: Any?): Boolean {
			if (this === other) return true
			if (javaClass != other?.javaClass) return false

			other as LeaderboardData

			if (location != other.location || !Arrays.equals(uuids, other.uuids)) return false

			return true
		}

		override fun hashCode() = Arrays.hashCode(uuids) * 31 + location.hashCode()
	}

	enum class EnumLeaderboardType(val titleDesc: String, val titleAsc: String) {

		KILLS("Top Kills", "Menos Kills"),
		DEATHS("Top Mortes", "Menos Mortes"),
		MONEY("Top Créditos", "Menos Créditos"),
		KILLSTREAK("Top KillStreak", "Menor KillStreak"),
		XP("Top XP", "Menos XP"),
		HGWINS("Top Vitórias no EventoHG", "Menos Vitórias no EventoHG"),
		HGLOSSES("Top Derrotas no EventoHG", "Menos Derrotas no EventoHG"),
		ONEVSONEWINS("Top Vitórias no 1v1", "Menos Vitórias no 1v1"),
		ONEVSONELOSSES("Top Derrotas no 1v1", "Menos Derrotas no 1v1"),
		GLADIATORWINS("Top Vitórias no Gladiator", "Menos Vitórias no Gladiator"),
		GLADIATORLOSSES("Top Derrotas no Gladiator", "Menos Derrotas no Gladiator")
	}
}
