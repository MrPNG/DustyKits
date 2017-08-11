@file:JvmName("TaskUtils")

package br.com.dusty.dkits.util

import br.com.dusty.dkits.Main
import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitTask

object TaskUtils {

    @JvmStatic fun sync(runnable: Runnable): BukkitTask {
        return Bukkit.getScheduler().runTask(Main.INSTANCE, runnable)
    }

    @JvmStatic fun sync(runnable: Runnable, delay: Long): BukkitTask {
        return Bukkit.getScheduler().runTaskLater(Main.INSTANCE, runnable, delay)
    }

    @JvmStatic fun sync(runnable: Runnable, delay: Long, period: Long): BukkitTask {
        return Bukkit.getScheduler().runTaskTimer(Main.INSTANCE, runnable, delay, period)
    }

    @JvmStatic fun async(runnable: Runnable): BukkitTask {
        return Bukkit.getScheduler().runTaskAsynchronously(Main.INSTANCE, runnable)
    }

    @JvmStatic fun async(runnable: Runnable, delay: Long): BukkitTask {
        return Bukkit.getScheduler().runTaskLaterAsynchronously(Main.INSTANCE, runnable, delay)
    }

    @JvmStatic fun async(runnable: Runnable, delay: Long, period: Long): BukkitTask {
        return Bukkit.getScheduler().runTaskTimerAsynchronously(Main.INSTANCE, runnable, delay, period)
    }
}
