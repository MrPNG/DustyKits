package br.com.dusty.dkits.listener.mechanics

import br.com.dusty.dkits.EnumServerStatus
import br.com.dusty.dkits.Main
import br.com.dusty.dkits.gamer.EnumMode
import br.com.dusty.dkits.util.MOTDs
import br.com.dusty.dkits.util.gamer.gamer
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.server.ServerListPingEvent

object ServerListPingListener: Listener {

	@EventHandler
	fun onServerListPing(event: ServerListPingEvent) {
		event.removeAll { event.iterator().next().gamer().mode == EnumMode.ADMIN }
		event.maxPlayers = Main.data.slots

		when (Main.data.serverStatus) {
			EnumServerStatus.ONLINE      -> event.motd = MOTDs.randomMOTD()
			EnumServerStatus.OFFLINE     -> event.motd = MOTDs.offlineMOTD()
			EnumServerStatus.MAINTENANCE -> event.motd = MOTDs.maintenanceMOTD()
		}
	}
}
