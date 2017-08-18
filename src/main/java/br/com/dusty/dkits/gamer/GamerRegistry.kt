package br.com.dusty.dkits.gamer

import br.com.dusty.dkits.Main
import org.bukkit.entity.Player
import java.util.*

object GamerRegistry {

	private val PRIMITIVE_GAMER_BY_UUID = HashMap<UUID, PrimitiveGamer>()
	private val GAMER_BY_PLAYER = HashMap<Player, Gamer>()

	internal fun getGamerByPlayer(player: Player): Gamer {
		val gamer: Gamer

		if (!GAMER_BY_PLAYER.containsKey(player)) {
			val uuid = player.uniqueId

			gamer = Gamer(player, PRIMITIVE_GAMER_BY_UUID[uuid]!!)

			PRIMITIVE_GAMER_BY_UUID.remove(uuid)
			GAMER_BY_PLAYER.put(player, gamer)
		} else {
			gamer = GAMER_BY_PLAYER[player]!!
		}

		return gamer
	}

	fun unregister(player: Player): Gamer {
		return GAMER_BY_PLAYER.remove(player)!!
	}

	val onlineGamers: Collection<Gamer>
		get() = GAMER_BY_PLAYER.values

	fun fromJson(json: String?, uuid: UUID): PrimitiveGamer? {
		if (json == null)
			return null

		val primitiveGamer: PrimitiveGamer

		if (json == "null") {
			primitiveGamer = PrimitiveGamer()
			primitiveGamer.setUniqueId(uuid)
		} else {
			primitiveGamer = Main.GSON.fromJson(json, PrimitiveGamer::class.java)
		}

		PRIMITIVE_GAMER_BY_UUID.put(uuid, primitiveGamer)

		return primitiveGamer
	}

	fun getPrimitiveGamerByUniqueId(uuid: UUID): PrimitiveGamer? {
		return PRIMITIVE_GAMER_BY_UUID[uuid]
	}

	fun unregister(uuid: UUID): PrimitiveGamer {
		return PRIMITIVE_GAMER_BY_UUID.remove(uuid)!!
	}
}
