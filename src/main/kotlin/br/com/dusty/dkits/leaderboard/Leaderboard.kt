package br.com.dusty.dkits.leaderboard

import br.com.dusty.dkits.leaderboard.Leaderboards.EnumLeaderboardType.KILLS
import br.com.dusty.dkits.util.Tasks
import br.com.dusty.dkits.util.text.Text
import br.com.dusty.dkits.util.text.TextColor
import br.com.dusty.dkits.util.text.TextStyle
import br.com.dusty.dkits.util.web.WebAPI
import io.reactivex.Observable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import org.bukkit.Location
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.EntityType

class Leaderboard(val location: Location,
                  val type: Leaderboards.EnumLeaderboardType = KILLS,
                  val amount: Int = 10,
                  val descending: Boolean = true,
                  val entities: ArrayList<ArmorStand> = arrayListOf()) {

	init {
		if (entities.isEmpty()) {
			Tasks.async(Runnable {
				val pairs = WebAPI.leaderboard(this)

				Tasks.sync(Runnable {
					var index = amount

					val location = location.clone()

					pairs.reversed().forEach {
						entities.add((location.world.spawnEntity(location, EntityType.ARMOR_STAND) as ArmorStand).apply {
							customName = Text.of((index).toString() + ". ").color(TextColor.GOLD).styles(TextStyle.BOLD).append(it.first).color(TextColor.YELLOW).basic(" - ").append(it.second).color(
									TextColor.GOLD).styles(TextStyle.BOLD).toString()
							isCustomNameVisible = true
							isVisible = false
							setGravity(false)
						})

						index--

						location.y += 0.32
					}

					location.y += 0.32

					val title = if (descending) type.titleDesc else type.titleAsc

					entities.add((location.world.spawnEntity(location, EntityType.ARMOR_STAND) as ArmorStand).apply {
						customName = Text.of(title).color(TextColor.GOLD).styles(TextStyle.BOLD).toString()
						isCustomNameVisible = true
						isVisible = false
						setGravity(false)
					})

					Leaderboards.saveData()
				})
			})
		}

		Tasks.async(Runnable { update() }, 0L, 20L * 60L * 5L)
	}

	fun update() {
		if (entities.size == amount + 1) {
			val onNext = Consumer<Leaderboard> {
				val pairs = WebAPI.leaderboard(this)

				if (pairs.size == amount) {
					Tasks.sync(Runnable {
						val chunk = location.chunk
						if (!chunk.isLoaded) chunk.load()

						var index = amount

						val iterator = entities.iterator()

						pairs.reversed().forEach {
							iterator.next().customName = Text.of((index).toString() + ". ").color(TextColor.GOLD).styles(TextStyle.BOLD).append(it.first).color(TextColor.YELLOW).basic(" - ").append(
									it.second).color(TextColor.GOLD).styles(TextStyle.BOLD).toString()

							index--
						}
					})
				}
			}

			val onError = Consumer<Throwable> {
				println("Couldn't update leaderboard of type ${type.name} at (${location.x}, ${location.y}, ${location.z}): " + it.message?.substring(0, 100) + "...")
			}

			Observable.just(this).subscribeOn(Schedulers.io()).subscribe(onNext, onError)
		}
	}

	fun remove() {
		entities.forEach { it.remove() }
	}
}
