package br.com.dusty.dkits.util;

import br.com.dusty.dkits.Main;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

public class TaskUtils {
	
	public static BukkitTask sync(Runnable runnable) {
		return Bukkit.getScheduler().runTask(Main.INSTANCE, runnable);
	}
	
	public static BukkitTask sync(Runnable runnable, long delay) {
		return Bukkit.getScheduler().runTaskLater(Main.INSTANCE, runnable, delay);
	}
	
	public static BukkitTask sync(Runnable runnable, long delay, long period) {
		return Bukkit.getScheduler().runTaskTimer(Main.INSTANCE, runnable, delay, period);
	}
	
	public static BukkitTask async(Runnable runnable) {
		return Bukkit.getScheduler().runTaskAsynchronously(Main.INSTANCE, runnable);
	}
	
	public static BukkitTask async(Runnable runnable, long delay) {
		return Bukkit.getScheduler().runTaskLaterAsynchronously(Main.INSTANCE, runnable, delay);
	}
	
	public static BukkitTask async(Runnable runnable, long delay, long period) {
		return Bukkit.getScheduler().runTaskTimerAsynchronously(Main.INSTANCE, runnable, delay, period);
	}
}
