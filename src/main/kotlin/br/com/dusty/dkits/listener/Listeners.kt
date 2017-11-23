package br.com.dusty.dkits.listener

import br.com.dusty.dkits.Main
import br.com.dusty.dkits.listener.gameplay.EntityDamageByEntityListener
import br.com.dusty.dkits.listener.gameplay.PlayerDeathListener
import br.com.dusty.dkits.listener.gameplay.PlayerInteractListener
import br.com.dusty.dkits.listener.login.AsyncPlayerPreLoginListener
import br.com.dusty.dkits.listener.login.PlayerJoinListener
import br.com.dusty.dkits.listener.login.PlayerLoginListener
import br.com.dusty.dkits.listener.mechanics.*
import br.com.dusty.dkits.listener.quit.PlayerQuitListener
import org.bukkit.Bukkit
import org.bukkit.event.Listener
import java.util.*

object Listeners {

	/**
	 * [ArrayList] que contém todos os [Listener] a serem/já registrados pelo plugin.
	 */
	private val LISTENERS = ArrayList<Listener>()

	/**
	 * Registra todos os [Listener] da [ArrayList] LISTENERS.
	 */
	fun registerAll() {
		//Usage: LISTENERS.add(FooListener);

		//Login
		LISTENERS.add(AsyncPlayerPreLoginListener)
		LISTENERS.add(PlayerJoinListener)
		LISTENERS.add(PlayerLoginListener)

		//Gameplay
		LISTENERS.add(EntityDamageByEntityListener)
		LISTENERS.add(PlayerDeathListener)
		LISTENERS.add(PlayerInteractListener)
		LISTENERS.add(br.com.dusty.dkits.listener.gameplay.PlayerMoveListener)

		//Mechanincs
		LISTENERS.add(AsyncPlayerChatListener)
		LISTENERS.add(EntityDamageListener)
		LISTENERS.add(EntityPickupItemListener)
		LISTENERS.add(FoodLevelChangeListener)
		LISTENERS.add(InventoryClickListener)
		LISTENERS.add(ItemSpawnListener)
		LISTENERS.add(LeavesDecayListener)
		LISTENERS.add(PlayerCommandPreprocessListener)
		LISTENERS.add(PlayerDropItemListener)
		LISTENERS.add(PlayerInteractEntityListener)
		LISTENERS.add(br.com.dusty.dkits.listener.mechanics.PlayerMoveListener)
		LISTENERS.add(ServerListPingListener)
		LISTENERS.add(SignChangeListener)
		LISTENERS.add(WeatherChangeListener)

		//Quit
		LISTENERS.add(PlayerQuitListener)

		val pluginManager = Bukkit.getPluginManager()
		LISTENERS.forEach { listener -> pluginManager.registerEvents(listener, Main.INSTANCE) }
	}
}
