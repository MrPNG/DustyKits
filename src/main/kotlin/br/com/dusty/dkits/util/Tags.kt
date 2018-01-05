package br.com.dusty.dkits.util

import br.com.dusty.dkits.gamer.Gamer
import br.com.dusty.dkits.util.protocol.Protocols
import br.com.dusty.dkits.util.text.TextStyle
import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.PacketContainer
import com.comphenix.protocol.wrappers.*

object Tags {

	fun updateNameAboveHead(gamer: Gamer, otherGamers: Collection<Gamer>) {
		gamer.player.run {
			val tag = gamer.tag.format(gamer.displayName) + TextStyle.RESET

			val ping = Protocols.ping(this)
			val gameMode = EnumWrappers.NativeGameMode.fromBukkit(gameMode)

			val gameProfileOld = WrappedGameProfile.fromPlayer(this)

			val packetPlayOutPlayerInfoRemove = PacketContainer(PacketType.Play.Server.PLAYER_INFO).apply {
				playerInfoAction.write(0, EnumWrappers.PlayerInfoAction.REMOVE_PLAYER)
				playerInfoDataLists.write(0, arrayListOf(PlayerInfoData(gameProfileOld, ping, gameMode, WrappedChatComponent.fromText(name))))
			}

			val gameProfileNew = WrappedGameProfile(uniqueId, tag).apply { properties.replaceValues("textures", gameProfileOld.properties["textures"]) }

			val packetPlayOutPlayerInfoAdd = PacketContainer(PacketType.Play.Server.PLAYER_INFO).apply {
				playerInfoAction.write(0, EnumWrappers.PlayerInfoAction.ADD_PLAYER)
				playerInfoDataLists.write(0, arrayListOf(PlayerInfoData(gameProfileNew, ping, gameMode, WrappedChatComponent.fromText(tag))))
			}

			val packetPlayOutEntityDestroy = PacketContainer(PacketType.Play.Server.ENTITY_DESTROY).apply {
				integerArrays.write(0, intArrayOf(entityId))
			}

			val packetPlayOutNamedEntitySpawn = PacketContainer(PacketType.Play.Server.NAMED_ENTITY_SPAWN).apply {
				integers.write(0, entityId)
				uuiDs.write(0, uniqueId)
				integers.write(1, (location.x * 32.0).toInt())
				integers.write(2, (location.y * 32.0).toInt())
				integers.write(3, (location.z * 32.0).toInt())
				bytes.write(0, (location.yaw * 256.0 / 360.0).toByte())
				bytes.write(1, (location.pitch * 256.0 / 360.0).toByte())
				integers.write(4, 0)
				dataWatcherModifier.write(0, WrappedDataWatcher.getEntityWatcher(this@run))
			}

			with(Protocols.PROTOCOL_MANAGER!!) {
				otherGamers.filter { it.player != this@run && it.player.canSee(this@run) }.forEach {
					val otherPlayer = it.player

					sendServerPacket(otherPlayer, packetPlayOutPlayerInfoRemove)
					sendServerPacket(otherPlayer, packetPlayOutPlayerInfoAdd)
					sendServerPacket(otherPlayer, packetPlayOutEntityDestroy)
					sendServerPacket(otherPlayer, packetPlayOutNamedEntitySpawn)
				}
			}
		}
	}
}
