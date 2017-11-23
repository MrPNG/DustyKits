package br.com.dusty.dkits.util

import br.com.dusty.dkits.Main
import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitTask

object Tasks {

	fun sync(runnable: Runnable): BukkitTask = Bukkit.getScheduler().runTask(Main.INSTANCE, runnable)

	fun sync(runnable: Runnable, delay: Long): BukkitTask = Bukkit.getScheduler().runTaskLater(Main.INSTANCE, runnable, delay)

	fun sync(runnable: Runnable, delay: Long, period: Long): BukkitTask = Bukkit.getScheduler().runTaskTimer(Main.INSTANCE, runnable, delay, period)

	fun async(runnable: Runnable): BukkitTask = Bukkit.getScheduler().runTaskAsynchronously(Main.INSTANCE, runnable)

	fun async(runnable: Runnable, delay: Long): BukkitTask = Bukkit.getScheduler().runTaskLaterAsynchronously(Main.INSTANCE, runnable, delay)

	fun async(runnable: Runnable, delay: Long, period: Long): BukkitTask = Bukkit.getScheduler().runTaskTimerAsynchronously(Main.INSTANCE, runnable, delay, period)
}
