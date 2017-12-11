package br.com.dusty.dkits.warp

import br.com.dusty.dkits.gamer.gamer
import br.com.dusty.dkits.kit.Kit
import br.com.dusty.dkits.util.cosmetic.Colors
import br.com.dusty.dkits.util.entity.spawnFirework
import br.com.dusty.dkits.util.rename
import br.com.dusty.dkits.util.setDescription
import br.com.dusty.dkits.util.text.Text
import br.com.dusty.dkits.util.text.TextColor
import org.bukkit.FireworkEffect
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.inventory.ItemStack

object VolcanoWarp: Warp() {

	val ITEMSTACKS = arrayOf(ItemStack(Material.STONE_SWORD),
	                         ItemStack(Material.CHAINMAIL_CHESTPLATE),
	                         ItemStack(Material.CHAINMAIL_LEGGINGS),
	                         ItemStack(Material.IRON_SWORD),
	                         ItemStack(Material.IRON_LEGGINGS),
	                         ItemStack(Material.IRON_BOOTS),
	                         ItemStack(Material.IRON_CHESTPLATE))

	init {
		name = "Volcano"
		icon = ItemStack(Material.OBSIDIAN)

		icon.rename(Text.of(name).color(TextColor.GOLD).toString())
		icon.setDescription(description)

		entryKit = Kit(weapon = ItemStack(Material.WOOD_SWORD), armor = arrayOf(null, ItemStack(Material.LEATHER_CHESTPLATE), null, null), isDummy = false)

		durabilityBehavior = EnumDurabilityBehavior.REGEN_ON_KILL

		loadData()
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	fun onPlayerDeath(event: PlayerDeathEvent) {
		val gamer = event.entity.gamer()

		if (gamer.warp == this && gamer.isCombatTagged()) {
			val killer = gamer.combatPartner!!
			val player = killer.player
			val inventory = player.inventory

			if (when (killer.warpKills) {
				2    -> {
					inventory.setItem(inventory.indexOfFirst { it.type == Material.WOOD_SWORD }, ITEMSTACKS[0])
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
					inventory.setItem(inventory.indexOfFirst { it.type == Material.STONE_SWORD }, ITEMSTACKS[3])
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

				player.location.spawnFirework(killer.warpKills, FireworkEffect.builder().withColor(Colors.random()).build())
				player.spawnParticle(Particle.CRIT_MAGIC, player.location, 1)
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	fun onEntityDamage(event: EntityDamageEvent) {
		if (event.cause == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION && event.entity is Player && (event.entity as Player).gamer().warp == this) event.isCancelled = true
	}
}
