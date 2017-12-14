package br.com.dusty.dkits.warp

import br.com.dusty.dkits.Main
import br.com.dusty.dkits.command.staff.LocationCommand
import br.com.dusty.dkits.gamer.Gamer
import br.com.dusty.dkits.gamer.GamerRegistry
import br.com.dusty.dkits.gamer.gamer
import br.com.dusty.dkits.kit.Kit
import br.com.dusty.dkits.kit.Kits
import br.com.dusty.dkits.util.*
import br.com.dusty.dkits.util.ItemStacks.potions
import br.com.dusty.dkits.util.cosmetic.Colors
import br.com.dusty.dkits.util.entity.spawnFirework
import br.com.dusty.dkits.util.inventory.*
import br.com.dusty.dkits.util.text.Text
import br.com.dusty.dkits.util.text.TextColor
import org.bukkit.Bukkit
import org.bukkit.FireworkEffect
import org.bukkit.Location
import org.bukkit.Material.*
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
import org.bukkit.potion.PotionType.*
import java.util.*

object FeastWarp: Warp() {

	val ALLOWED_ITEMS = arrayOf(RED_MUSHROOM,
	                            BROWN_MUSHROOM,
	                            MUSHROOM_SOUP,
	                            BOWL,
	                            EXP_BOTTLE,
	                            GLASS_BOTTLE,
	                            POTION,
	                            BOW,
	                            ARROW,
	                            IRON_HELMET,
	                            IRON_CHESTPLATE,
	                            IRON_LEGGINGS,
	                            IRON_BOOTS,
	                            DIAMOND_HELMET,
	                            DIAMOND_CHESTPLATE,
	                            DIAMOND_LEGGINGS,
	                            DIAMOND_BOOTS,
	                            DIAMOND_SWORD)

	val CHEST_REFIL_MESSAGE = Text.positivePrefix().basic("Os ").positive("baús").basic(" foram ").positive("reabastecidos").basic("!").toString()

	val CHEST_POSITIONS = arrayOf(doubleArrayOf(-2.0, 0.0, -2.0),
	                              doubleArrayOf(-2.0, 0.0, 0.0),
	                              doubleArrayOf(-2.0, 0.0, 2.0),
	                              doubleArrayOf(0.0, 0.0, 2.0),
	                              doubleArrayOf(2.0, 0.0, 2.0),
	                              doubleArrayOf(2.0, 0.0, 0.0),
	                              doubleArrayOf(2.0, 0.0, -2.0),
	                              doubleArrayOf(0.0, 0.0, -2.0),
	                              doubleArrayOf(-5.0, 1.0, -5.0),
	                              doubleArrayOf(-5.0, 1.0, 0.0),
	                              doubleArrayOf(-5.0, 1.0, 5.0),
	                              doubleArrayOf(0.0, 1.0, 5.0),
	                              doubleArrayOf(5.0, 1.0, 5.0),
	                              doubleArrayOf(5.0, 1.0, 0.0),
	                              doubleArrayOf(5.0, 1.0, -5.0),
	                              doubleArrayOf(0.0, 1.0, -5.0),
	                              doubleArrayOf(-8.0, 1.0, -8.0),
	                              doubleArrayOf(-8.0, 1.0, 0.0),
	                              doubleArrayOf(-8.0, 1.0, 8.0),
	                              doubleArrayOf(0.0, 1.0, 8.0),
	                              doubleArrayOf(8.0, 1.0, 8.0),
	                              doubleArrayOf(8.0, 1.0, 0.0),
	                              doubleArrayOf(8.0, 1.0, -8.0),
	                              doubleArrayOf(0.0, 1.0, -8.0),
	                              doubleArrayOf(-11.0, 1.0, -11.0),
	                              doubleArrayOf(-11.0, 1.0, 0.0),
	                              doubleArrayOf(-11.0, 1.0, 11.0),
	                              doubleArrayOf(0.0, 1.0, 11.0),
	                              doubleArrayOf(11.0, 1.0, 11.0),
	                              doubleArrayOf(11.0, 1.0, 0.0),
	                              doubleArrayOf(11.0, 1.0, -11.0),
	                              doubleArrayOf(0.0, 1.0, -11.0))

	val CHEST_LOCATIONS: List<Location>

	//Usage: arrayOf(itemStack, attempts, chances, maxAmount)
	val CHEST_ITEMS = arrayOf(arrayOf(ItemStack(DIAMOND_HELMET), 1, 0.1, 1),
	                          arrayOf(ItemStack(DIAMOND_CHESTPLATE), 1, 0.1, 1),
	                          arrayOf(ItemStack(DIAMOND_LEGGINGS), 1, 0.1, 1),
	                          arrayOf(ItemStack(DIAMOND_BOOTS), 1, 0.1, 1),
	                          arrayOf(ItemStack(DIAMOND_SWORD), 1, 0.1, 1),
	                          arrayOf(ItemStack(BOW), 2, 0.25, 1),
	                          arrayOf(ItemStack(ARROW), 2, 0.25, 16),
	                          arrayOf(ItemStack(EXP_BOTTLE), 2, 0.5, 8),
	                          arrayOf(potions(1, false, false, SPEED, false), 1, 0.075, 1),
	                          arrayOf(potions(1, true, false, SPEED, false), 1, 0.05, 1),
	                          arrayOf(potions(1, false, true, SPEED, false), 1, 0.05, 1),
	                          arrayOf(potions(1, false, false, REGEN, false), 1, 0.075, 1),
	                          arrayOf(potions(1, true, false, REGEN, false), 1, 0.05, 1),
	                          arrayOf(potions(1, false, true, REGEN, false), 1, 0.05, 1),
	                          arrayOf(potions(1, false, false, STRENGTH, false), 1, 0.075, 1),
	                          arrayOf(potions(1, true, false, STRENGTH, false), 1, 0.05, 1),
	                          arrayOf(potions(1, false, true, STRENGTH, false), 1, 0.05, 1),
	                          arrayOf(potions(1, false, false, WEAKNESS, true), 1, 0.075, 1),
	                          arrayOf(potions(1, true, false, WEAKNESS, true), 1, 0.05, 1),
	                          arrayOf(potions(1, false, false, POISON, true), 1, 0.075, 1),
	                          arrayOf(potions(1, true, false, POISON, true), 1, 0.05, 1),
	                          arrayOf(potions(1, false, true, POISON, true), 1, 0.05, 1))

	var enchantmentTable = Locations.GENERIC
		get() {
			if (field == Locations.GENERIC && data is FeastData) field = (data as FeastData).enchantmentTable.toLocation(Bukkit.getWorlds()[0])

			return field
		}
		set(location) {
			if (data is FeastData) {
				field = location

				(data as FeastData).enchantmentTable = location.toSimpleLocation()

				saveData()
			}
		}

	init {
		name = "Feast"
		icon = ItemStack(CHEST)

		icon.rename(Text.of(name).color(TextColor.GOLD).toString())
		icon.setDescription(description, true)

		entryKit = SIMPLE_GAME_WARP_KIT

		hasShop = true

		durabilityBehavior = EnumDurabilityBehavior.BREAK

		data = FeastData()

		loadData()

		LocationCommand.CUSTOM_EXECUTORS.add(this)

		Tasks.sync(Runnable {
			fillChests()
			GamerRegistry.onlineGamers().filter { it.warp == this }.forEach {
				it.player.sendMessage(CHEST_REFIL_MESSAGE)
			}
		}, 0L, 6000L)

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
			event.action == Action.RIGHT_CLICK_BLOCK && event.clickedBlock.type == CHEST -> player.openInventory((event.clickedBlock.state as Chest).blockInventory)
			event.item == null || event.item.type in ALLOWED_ITEMS                       -> event.isCancelled = false
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	fun onInventoryClick(event: InventoryClickEvent) {
		val player = event.whoClicked as Player
		val gamer = player.gamer()

		if (gamer.warp == this && !gamer.kit.isDummy) event.isCancelled = false
	}

	fun fillChests() {
		CHEST_LOCATIONS.filter { it.block.type == CHEST }.forEach {
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
				fillSoups(true)
				setArmor(Inventories.ARMOR_FULL_IRON)
			}
		}
	}

	override fun execute(gamer: Gamer, alias: String, args: Array<String>) {
		val player = gamer.player

		when (alias) {
			"location" -> when (args[0]) {
				"enchantmentTable" -> {
					enchantmentTable = player.location.normalize()

					player.sendMessage(Text.positivePrefix().basic("Você ").positive("definiu").basic(" o local da ").positive("mesa de encantamentos").basic(" da warp ").positive(name).basic(
							"!").toString())
				}
			}
		}
	}

	data class FeastData(var enchantmentTable: SimpleLocation = SimpleLocation()): Data()
}
