package br.com.dusty.dkits.gamer

import br.com.dusty.dkits.Main
import org.bukkit.entity.Player
import java.util.*

object GamerRegistry {

	val PRIMITIVE_GAMER_BY_UUID = HashMap<UUID, PrimitiveGamer>()
	val GAMER_BY_PLAYER = HashMap<Player, Gamer>()

	fun onlineGamers() = GAMER_BY_PLAYER.values

	fun gamer(player: Player): Gamer {
		var gamer = GAMER_BY_PLAYER[player]

		if (gamer == null) {
			val uuid = player.uniqueId

			gamer = Gamer(player, PRIMITIVE_GAMER_BY_UUID[uuid]!!)

			PRIMITIVE_GAMER_BY_UUID.remove(uuid)
			GAMER_BY_PLAYER.put(player, gamer)
		}

		return gamer
	}

	fun primitiveGamerFromJson(json: String?, uuid: UUID): PrimitiveGamer? {
		if (json == null) return null

		val primitiveGamer: PrimitiveGamer = if (json == "null") PrimitiveGamer(uuid.toString()) else Main.GSON.fromJson(json, PrimitiveGamer::class.java)

		PRIMITIVE_GAMER_BY_UUID.put(uuid, primitiveGamer)

		return primitiveGamer
	}

	fun tempPrimitiveGamer(uuid: UUID): PrimitiveGamer = primitiveGamerFromJson("null", uuid)!!
}
