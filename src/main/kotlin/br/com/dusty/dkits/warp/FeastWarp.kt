package br.com.dusty.dkits.warp

import br.com.dusty.dkits.Main
import br.com.dusty.dkits.gamer.Gamer
import br.com.dusty.dkits.gamer.GamerRegistry
import br.com.dusty.dkits.gamer.gamer
import br.com.dusty.dkits.kit.Kit
import br.com.dusty.dkits.kit.Kits
import br.com.dusty.dkits.util.*
import br.com.dusty.dkits.util.cosmetic.Colors
import br.com.dusty.dkits.util.entity.spawnFirework
import br.com.dusty.dkits.util.inventory.*
import br.com.dusty.dkits.util.text.Text
import br.com.dusty.dkits.util.text.TextColor
import org.bukkit.Bukkit
import org.bukkit.FireworkEffect
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Chest
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.block.Action
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionType
import java.util.*

object FeastWarp: Warp() {

	val ALLOWED_ITEMS = arrayOf(Material.EXP_BOTTLE,
	                            Material.GLASS_BOTTLE,
	                            Material.POTION,
	                            Material.BOW,
	                            Material.ARROW,
	                            Material.IRON_HELMET,
	                            Material.IRON_CHESTPLATE,
	                            Material.IRON_LEGGINGS,
	                            Material.IRON_BOOTS,
	                            Material.DIAMOND_HELMET,
	                            Material.DIAMOND_CHESTPLATE,
	                            Material.DIAMOND_LEGGINGS,
	                            Material.DIAMOND_BOOTS,
	                            Material.DIAMOND_SWORD)

	val CHEST_REFIL_MESSAGE = Text.positivePrefix().basic("Os ").positive("baús").basic(" foram ").positive("reabastecidos").basic("!").toString()

	val CHEST_POSITIONS = arrayOf(arrayOf(-2.0, 0.0, -2.0),
	                              arrayOf(-2.0, 0.0, 0.0),
	                              arrayOf(-2.0, 0.0, 2.0),
	                              arrayOf(0.0, 0.0, 2.0),
	                              arrayOf(2.0, 0.0, 2.0),
	                              arrayOf(2.0, 0.0, 0.0),
	                              arrayOf(2.0, 0.0, -2.0),
	                              arrayOf(0.0, 0.0, -2.0),
	                              arrayOf(-5.0, 1.0, -5.0),
	                              arrayOf(-5.0, 1.0, 0.0),
	                              arrayOf(-5.0, 1.0, 5.0),
	                              arrayOf(0.0, 1.0, 5.0),
	                              arrayOf(5.0, 1.0, 5.0),
	                              arrayOf(5.0, 1.0, 0.0),
	                              arrayOf(5.0, 1.0, -5.0),
	                              arrayOf(0.0, 1.0, -5.0),
	                              arrayOf(-8.0, 1.0, -8.0),
	                              arrayOf(-8.0, 1.0, 0.0),
	                              arrayOf(-8.0, 1.0, 8.0),
	                              arrayOf(0.0, 1.0, 8.0),
	                              arrayOf(8.0, 1.0, 8.0),
	                              arrayOf(8.0, 1.0, 0.0),
	                              arrayOf(8.0, 1.0, -8.0),
	                              arrayOf(0.0, 1.0, -8.0),
	                              arrayOf(-11.0, 1.0, -11.0),
	                              arrayOf(-11.0, 1.0, 0.0),
	                              arrayOf(-11.0, 1.0, 11.0),
	                              arrayOf(0.0, 1.0, 11.0),
	                              arrayOf(11.0, 1.0, 11.0),
	                              arrayOf(11.0, 1.0, 0.0),
	                              arrayOf(11.0, 1.0, -11.0),
	                              arrayOf(0.0, 1.0, -11.0))

	val CHEST_LOCATIONS: List<Location>

	val CHEST_ITEMS = arrayOf(arrayOf(ItemStack(Material.DIAMOND_HELMET), 1, 0.1, 1),
	                          arrayOf(ItemStack(Material.DIAMOND_CHESTPLATE), 1, 0.1, 1),
	                          arrayOf(ItemStack(Material.DIAMOND_LEGGINGS), 1, 0.1, 1),
	                          arrayOf(ItemStack(Material.DIAMOND_BOOTS), 1, 0.1, 1),
	                          arrayOf(ItemStack(Material.DIAMOND_SWORD), 1, 0.1, 1),
	                          arrayOf(ItemStack(Material.BOW), 2, 0.25, 1),
	                          arrayOf(ItemStack(Material.ARROW), 2, 0.25, 16),
	                          arrayOf(ItemStack(Material.EXP_BOTTLE), 2, 0.5, 8),
	                          arrayOf(ItemStacks.potions(1, false, false, PotionType.SPEED, false), 1, 0.15, 1),
	                          arrayOf(ItemStacks.potions(1, true, false, PotionType.SPEED, false), 1, 0.1, 1),
	                          arrayOf(ItemStacks.potions(1, false, true, PotionType.SPEED, false), 1, 0.1, 1),
	                          arrayOf(ItemStacks.potions(1, false, false, PotionType.REGEN, false), 1, 0.15, 1),
	                          arrayOf(ItemStacks.potions(1, true, false, PotionType.REGEN, false), 1, 0.1, 1),
	                          arrayOf(ItemStacks.potions(1, false, true, PotionType.REGEN, false), 1, 0.1, 1))

	var enchantmentTable = Locations.GENERIC
		get() {
			if (field == Locations.GENERIC && data is FeastData) {
				val data = (data as FeastData)

				field = Location(Bukkit.getWorlds()[0], data.enchantmentTable[0].toDouble(), data.enchantmentTable[1].toDouble(), data.enchantmentTable[2].toDouble())
			}

			return field
		}
		set(location) {
			field = location

			if (data is FeastData) {
				(data as FeastData).run {
					enchantmentTable[0] = location.x.toFloat()
					enchantmentTable[1] = location.y.toFloat()
					enchantmentTable[2] = location.z.toFloat()
				}
			}

			saveData()
		}

	init {
		name = "Feast"
		icon = ItemStack(Material.CHEST)

		icon.rename(Text.of(name).color(TextColor.GOLD).toString())
		icon.setDescription(description)

		entryKit = SimpleGameWarpKit

		hasShop = true

		durabilityBehavior = EnumDurabilityBehavior.BREAK

		data = FeastData()

		loadData()

		Tasks.sync(Runnable {
			fillChests()
			GamerRegistry.onlineGamers().filter { it.warp == this }.forEach {
				it.player.sendMessage(CHEST_REFIL_MESSAGE)
			}
		}, 0L, 1200L)

		CHEST_LOCATIONS = CHEST_POSITIONS.map { enchantmentTable.clone().add(it[0], it[1], it[2]) }
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	fun onEntityDamage(event: EntityDamageEvent) {
		if (event.cause == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION && event.entity is Player && (event.entity as Player).gamer().warp == this) event.isCancelled = true
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	fun onPlayerDropItem(event: PlayerDropItemEvent) {
		if (event.player.gamer().warp == this) {
			val item = event.itemDrop

			if (item.itemStack.type in ALLOWED_ITEMS) {
				event.isCancelled = false

				Tasks.sync(Runnable { item.remove() })
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	fun onPlayerInteract(event: PlayerInteractEvent) {
		val player = event.player
		val gamer = player.gamer()

		if (gamer.warp == this) when {
			event.action == Action.RIGHT_CLICK_BLOCK && event.clickedBlock.type == Material.CHEST -> player.openInventory((event.clickedBlock.state as Chest).blockInventory)
			event.item != null && event.item.type in ALLOWED_ITEMS                                -> event.isCancelled = false
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	fun onInventoryClick(event: InventoryClickEvent) {
		val player = event.whoClicked as Player
		val gamer = player.gamer()

		if (gamer.warp == this && !gamer.kit.isDummy) event.isCancelled = false
	}

	fun fillChests() {
		CHEST_LOCATIONS.filter { it.block.type == Material.CHEST }.forEach {
			val items = ArrayList<ItemStack?>(27)

			CHEST_ITEMS.forEach {
				for (i in 1 .. (it[1] as Int)) if ((it[2] as Double).chances()) {
					val itemStack = (it[0] as ItemStack)
					itemStack.amount = Main.RANDOM.nextInt(it[3] as Int) + 1

					items.add(itemStack)
				}
			}

			for (i in 1 .. (27 - items.size)) items.add(null)

			Collections.shuffle(items, Main.RANDOM)

			(it.block.state as Chest).blockInventory.run {
				clear()
				addItemStacks(items.toTypedArray())
			}

			it.spawnFirework(1, FireworkEffect.builder().withColor(Colors.random()).with(FireworkEffect.Type.BALL).build())
		}
	}

	override fun applyKit(gamer: Gamer, kit: Kit) {
		gamer.clear()
		gamer.player.run {
			inventory.addItemStacks(kit.items)

			if (!kit.isDummy) {
				inventory.setItem(0, if (kit == Kits.PVP) Inventories.DIAMOND_SWORD_SHARPNESS else Inventories.DIAMOND_SWORD)
				fillRecraft()
				fillSoups()
				setArmor(Inventories.ARMOR_FULL_IRON)
			}
		}
	}

	data class FeastData(var enchantmentTable: Array<Float> = arrayOf(0F, 0F, 0F)): Data()
}
