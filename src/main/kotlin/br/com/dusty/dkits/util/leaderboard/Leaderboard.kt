package br.com.dusty.dkits.util.leaderboard

import br.com.dusty.dkits.util.Tasks
import br.com.dusty.dkits.util.leaderboard.Leaderboards.EnumLeaderboardType.KILLS
import br.com.dusty.dkits.util.text.Text
import br.com.dusty.dkits.util.text.TextColor
import br.com.dusty.dkits.util.text.TextStyle
import br.com.dusty.dkits.util.web.WebAPI
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

		Tasks.async(Runnable { update() }, 0L, 20L * 3L * 10L)
	}

	fun update() {
		if (entities.isNotEmpty()) {
			val pairs = WebAPI.leaderboard(this)

			Tasks.sync(Runnable {
				var index = amount

				val iterator = entities.iterator()

				pairs.reversed().forEach {
					iterator.next().customName = Text.of((index).toString() + ". ").color(TextColor.GOLD).styles(TextStyle.BOLD).append(it.first).color(TextColor.YELLOW).basic(" - ").append(it.second).color(
							TextColor.GOLD).styles(TextStyle.BOLD).toString()

					index--
				}
			})
		}
	}

	fun remove() {
		entities.forEach { it.remove() }
	}
}
