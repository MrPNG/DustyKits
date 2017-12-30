package br.com.dusty.dkits.util

import br.com.dusty.dkits.Main
import br.com.dusty.dkits.util.protocol.Protocols
import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.PacketAdapter
import com.comphenix.protocol.events.PacketEvent
import com.comphenix.protocol.wrappers.EnumWrappers
import com.comphenix.protocol.wrappers.PlayerInfoData
import com.comphenix.protocol.wrappers.WrappedGameProfile
import org.bukkit.Bukkit

object Tags {

	val PLAYER_INFO_ACTIONS = arrayOf(EnumWrappers.PlayerInfoAction.ADD_PLAYER, EnumWrappers.PlayerInfoAction.UPDATE_DISPLAY_NAME)

	fun registerPacketListener() {
		Protocols.PROTOCOL_MANAGER!!.addPacketListener(object: PacketAdapter(Main.INSTANCE, PacketType.Play.Server.PLAYER_INFO) {

			override fun onPacketSending(event: PacketEvent?) {
				event?.run {
					if (packet.playerInfoAction.read(0) in PLAYER_INFO_ACTIONS) {
						val newPlayerInfoDataList = arrayListOf<PlayerInfoData>()

						packet.playerInfoDataLists.read(0).forEach {
							val player = Bukkit.getPlayer(it.profile.uuid)

							if (player != null) {
								val gameProfile = WrappedGameProfile(it.profile.uuid, player.displayName.run { if (length > 16) substring(0, 15) else this }).apply {
									properties.replaceValues("textures", it.profile.properties["textures"])
								}
								val latency = it.latency
								val gameMode = it.gameMode
								val displayName = it.displayName

								newPlayerInfoDataList.add(PlayerInfoData(gameProfile, latency, gameMode, displayName))
							}
						}

						packet.playerInfoDataLists.write(0, newPlayerInfoDataList)
					}
				}
			}
		})
	}
}
