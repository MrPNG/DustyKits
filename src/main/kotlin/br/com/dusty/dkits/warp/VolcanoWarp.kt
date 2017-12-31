package br.com.dusty.dkits.warp

import br.com.dusty.dkits.kit.Kit
import br.com.dusty.dkits.store.EnumAdvantage.*
import br.com.dusty.dkits.util.graphics.Colors
import br.com.dusty.dkits.util.description
import br.com.dusty.dkits.util.entity.gamer
import br.com.dusty.dkits.util.entity.spawnFirework
import br.com.dusty.dkits.util.rename
import br.com.dusty.dkits.util.text.Text
import br.com.dusty.dkits.util.text.TextColor
import org.bukkit.FireworkEffect
import org.bukkit.Material.*
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object VolcanoWarp: Warp() {

	val ITEMSTACKS = arrayOf(ItemStack(STONE_SWORD),
	                         ItemStack(CHAINMAIL_CHESTPLATE),
	                         ItemStack(CHAINMAIL_LEGGINGS),
	                         ItemStack(IRON_SWORD),
	                         ItemStack(IRON_LEGGINGS),
	                         ItemStack(IRON_BOOTS),
	                         ItemStack(IRON_CHESTPLATE))

	init {
		name = "Volcano"

		icon = ItemStack(OBSIDIAN)
		icon.rename(Text.of(name).color(TextColor.GOLD).toString())
		icon.description(description, true)

		entryKit = Kit(weapon = ItemStack(WOOD_SWORD), armor = arrayOf(null, null, ItemStack(LEATHER_CHESTPLATE), null), isDummy = false)

		loadData()
	}

	@EventHandler(priority = EventPriority.LOW)
	fun onPlayerDeath(event: PlayerDeathEvent) {
		val dead = event.entity.gamer()

		if (dead.warp == this && dead.isCombatTagged()) {
			val gamer = dead.combatPartner!!
			val player = gamer.player
			val inventory = player.inventory

			if (gamer.hasAdvantage(VOLCANO_FIRE_PROTECTION)) player.addPotionEffect(PotionEffect(PotionEffectType.FIRE_RESISTANCE, 100, 1))

			val normalizationFactor = when {
				gamer.hasAdvantage(VOLCANO_25) -> 4.0 / 3.0
				gamer.hasAdvantage(VOLCANO_10) -> 10.0 / 9.0
				else                           -> 1.0
			}

			val normalizedKills = Math.floor(gamer.warpKills * normalizationFactor).toInt()

			if (when (normalizedKills) {
				2    -> {
					inventory.setItem(inventory.indexOfFirst { it.type == WOOD_SWORD }, ITEMSTACKS[0])
					true
				}
				6    -> {
					inventory.chestplate = ITEMSTACKS[1]
					true
				}
				10   -> {
					inventory.leggings = ITEMSTACKS[2]
					true
				}
				15   -> {
					inventory.setItem(inventory.indexOfFirst { it.type == STONE_SWORD }, ITEMSTACKS[3])
					true
				}
				20   -> {
					inventory.leggings = ITEMSTACKS[4]
					true
				}
				25   -> {
					inventory.boots = ITEMSTACKS[5]
					true
				}
				30   -> {
					inventory.chestplate = ITEMSTACKS[6]
					true
				}
				else -> false
			}) {
				player.sendMessage(Text.positivePrefix().basic("Você ").positive("avançou").basic(" de ").positive("nível").basic(" na warp ").positive(this.name).basic("!").toString())

				player.location.spawnFirework(normalizedKills, FireworkEffect.builder().withColor(Colors.random()).build())
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	fun onEntityDamage(event: EntityDamageEvent) {
		if (event.cause == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION && event.entity is Player && (event.entity as Player).gamer().warp == this) event.isCancelled = true
	}
}
