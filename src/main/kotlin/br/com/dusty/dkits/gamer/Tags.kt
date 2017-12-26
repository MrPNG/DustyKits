package br.com.dusty.dkits.gamer

import br.com.dusty.dkits.Main
import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.PacketAdapter
import com.comphenix.protocol.events.PacketEvent
import com.comphenix.protocol.wrappers.PlayerInfoData
import com.comphenix.protocol.wrappers.WrappedChatComponent
import com.comphenix.protocol.wrappers.WrappedGameProfile
import org.bukkit.Bukkit

object Tags {

	init {
		Main.PROTOCOL_MANAGER!!.addPacketListener(object: PacketAdapter(Main.INSTANCE, PacketType.Play.Server.PLAYER_INFO) {

			override fun onPacketSending(event: PacketEvent?) {
				event?.run {
					val newPlayerInfoDataList = arrayListOf<PlayerInfoData>()

					packet.playerInfoDataLists.read(0).forEach {
						val player = Bukkit.getPlayer(it.profile.id)

						if (player != null) {
							val gameProfile = WrappedGameProfile(it.profile.uuid, player.displayName).apply { properties.replaceValues("textures", it.profile.properties["textures"]) }
							val latency = it.latency
							val gameMode = it.gameMode
							val displayName = WrappedChatComponent.fromText(player.displayName)

							newPlayerInfoDataList.add(PlayerInfoData(gameProfile, latency, gameMode, displayName))
						}
					}

					packet.playerInfoDataLists.write(0, newPlayerInfoDataList)
				}
			}
		})
	}
}
