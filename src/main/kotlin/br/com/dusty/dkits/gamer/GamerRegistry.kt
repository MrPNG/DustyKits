package br.com.dusty.dkits.gamer

import br.com.dusty.dkits.Main
import org.bukkit.entity.Player
import java.util.*

object GamerRegistry {

	val PRIMITIVE_GAMER_BY_UUID = HashMap<UUID, PrimitiveGamer>()
	val GAMER_BY_PLAYER = linkedMapOf<UUID, Gamer>()

	fun onlineGamers() = GAMER_BY_PLAYER.values

	fun gamer(player: Player): Gamer {
		var gamer = GAMER_BY_PLAYER[player.uniqueId]

		if (gamer == null) {
			val uuid = player.uniqueId

			gamer = Gamer(player, PRIMITIVE_GAMER_BY_UUID[uuid]!!)

			PRIMITIVE_GAMER_BY_UUID.remove(uuid)
			GAMER_BY_PLAYER.put(player.uniqueId, gamer)
		}

		return gamer
	}

	fun primitiveGamerFromJson(json: String?, uuid: UUID) = when (json) {
		null             -> null
		"{\"status\":2}" -> PrimitiveGamer(uuid.toString())
		else             -> Main.GSON.fromJson(json, PrimitiveGamer::class.java)
	}

	fun tempPrimitiveGamer(uuid: UUID): PrimitiveGamer = primitiveGamerFromJson("{status:2}", uuid)!!

	fun unregister(uuid: UUID): Gamer? {
		PRIMITIVE_GAMER_BY_UUID.remove(uuid)

		return GAMER_BY_PLAYER.remove(uuid)
	}
}
