package br.com.dusty.dkits.warp

import br.com.dusty.dkits.Main
import br.com.dusty.dkits.gamer.Gamer
import br.com.dusty.dkits.gamer.GamerRegistry
import br.com.dusty.dkits.gamer.gamer
import br.com.dusty.dkits.kit.Kit
import br.com.dusty.dkits.kit.Kits
import br.com.dusty.dkits.util.*
import br.com.dusty.dkits.util.inventory.*
import br.com.dusty.dkits.util.text.Text
import br.com.dusty.dkits.util.text.TextColor
import org.bukkit.*
import org.bukkit.block.Chest
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.block.Action
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionType
import java.util.*

object FeastWarp: Warp() {

	val CHEST_REFIL_MESSAGE = Text.positivePrefix().basic("Os ").positive("baús").basic(" foram ").positive(" reabastecidos ").basic("!").toString()

	val CHEST_POSITIONS = arrayOf(arrayOf(-2.0, 0.0, -2.0),
	                              arrayOf(-2.0, 0.0, 0.0),
	                              arrayOf(-2.0, 0.0, 2.0),
	                              arrayOf(0.0, 0.0, 2.0),
	                              arrayOf(2.0, 0.0, 2.0),
	                              arrayOf(2.0, 0.0, 0.0),
	                              arrayOf(2.0, 0.0, -2.0),
	                              arrayOf(0.0, 0.0, -2.0),
	                              arrayOf(-5.0, 0.0, -5.0),
	                              arrayOf(-5.0, 0.0, 0.0),
	                              arrayOf(-5.0, 0.0, 5.0),
	                              arrayOf(0.0, 0.0, 5.0),
	                              arrayOf(5.0, 0.0, 5.0),
	                              arrayOf(5.0, 0.0, 0.0),
	                              arrayOf(5.0, 0.0, -5.0),
	                              arrayOf(0.0, 0.0, -5.0),
	                              arrayOf(-8.0, 0.0, -8.0),
	                              arrayOf(-8.0, 0.0, 0.0),
	                              arrayOf(-8.0, 0.0, 8.0),
	                              arrayOf(0.0, 0.0, 8.0),
	                              arrayOf(8.0, 0.0, 8.0),
	                              arrayOf(8.0, 0.0, 0.0),
	                              arrayOf(8.0, 0.0, -8.0),
	                              arrayOf(0.0, 0.0, -8.0),
	                              arrayOf(-11.0, 0.0, -11.0),
	                              arrayOf(-11.0, 0.0, 0.0),
	                              arrayOf(-11.0, 0.0, 11.0),
	                              arrayOf(0.0, 0.0, 11.0),
	                              arrayOf(11.0, 0.0, 11.0),
	                              arrayOf(11.0, 0.0, 0.0),
	                              arrayOf(11.0, 0.0, -11.0),
	                              arrayOf(0.0, 0.0, -11.0))

	val CHEST_LOCATIONS: List<Location>

	val CHEST_ITEMS = arrayOf(arrayOf(ItemStack(Material.DIAMOND_HELMET), 1, 0.1, 1),
	                          arrayOf(ItemStack(Material.DIAMOND_CHESTPLATE), 1, 0.1, 1),
	                          arrayOf(ItemStack(Material.DIAMOND_LEGGINGS), 1, 0.1, 1),
	                          arrayOf(ItemStack(Material.DIAMOND_BOOTS), 1, 0.1, 1),
	                          arrayOf(ItemStack(Material.DIAMOND_SWORD), 1, 0.1, 1),
	                          arrayOf(ItemStack(Material.BOW), 2, 0.25, 1),
	                          arrayOf(ItemStack(Material.ARROW), 2, 0.25, 16),
	                          arrayOf(ItemStack(Material.EXP_BOTTLE), 2, 0.5, 8),
	                          arrayOf(ItemStacks.potions(1, false, false, PotionType.SPEED, false), 1, 0.25, 1),
	                          arrayOf(ItemStacks.potions(1, true, false, PotionType.SPEED, false), 1, 0.2, 1),
	                          arrayOf(ItemStacks.potions(1, false, true, PotionType.SPEED, false), 1, 0.15, 1),
	                          arrayOf(ItemStacks.potions(1, true, true, PotionType.SPEED, false), 1, 0.1, 1),
	                          arrayOf(ItemStacks.potions(1, false, false, PotionType.REGEN, false), 1, 0.25, 1),
	                          arrayOf(ItemStacks.potions(1, true, false, PotionType.SPEED, false), 1, 0.2, 1),
	                          arrayOf(ItemStacks.potions(1, false, true, PotionType.SPEED, false), 1, 0.15, 1),
	                          arrayOf(ItemStacks.potions(1, true, true, PotionType.SPEED, false), 1, 0.1, 1))

	var enchantmentTable = Locations.GENERIC
		get() {
			if (field == Locations.GENERIC) field = Location(Bukkit.getWorlds()[0],
			                                                 (data as FeastData).enchantmentTable[0].toDouble(),
			                                                 (data as FeastData).enchantmentTable[1].toDouble(),
			                                                 (data as FeastData).enchantmentTable[2].toDouble())

			return field
		}
		set(location) {
			field = location

			(data as FeastData).enchantmentTable[0] = location.x.toFloat()
			(data as FeastData).enchantmentTable[1] = location.y.toFloat()
			(data as FeastData).enchantmentTable[2] = location.z.toFloat()

			saveData()
		}

	init {
		CHEST_LOCATIONS = CHEST_POSITIONS.map { enchantmentTable.clone().add(it[0], it[1], it[2]) }

		name = "Feast"
		icon = ItemStack(Material.CHEST)

		icon.rename(Text.of(name).color(TextColor.GOLD).toString())
		icon.setDescription(description)

		entryKit = SimpleGameWarpKit

		hasShop = true

		data = FeastData()

		loadData()

		Tasks.sync(Runnable {
			fillChests()
			GamerRegistry.onlineGamers().filter { it.warp == this }.forEach {
				it.player.sendMessage(CHEST_REFIL_MESSAGE)
			}
		}, 0L, 6000L)
	}

	@EventHandler
	fun onPlayerInteract(event: PlayerInteractEvent) {
		val player = event.player

		if (event.action == Action.RIGHT_CLICK_BLOCK && event.clickedBlock.type == Material.CHEST && player.gamer().warp == this) player.openInventory((event.clickedBlock as Chest).blockInventory)
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	fun onInventoryClick(event: InventoryClickEvent) {
		val player = event.whoClicked as Player
		val gamer = player.gamer()

		if (gamer.warp == this && !gamer.kit.isDummy) event.isCancelled = false
	}

	fun fillChests() {
		CHEST_LOCATIONS.filter { it.block.type == Material.CHEST }.forEach {
			val items = ArrayList<ItemStack>(27)

			CHEST_ITEMS.forEach {
				for (i in 1 .. (it[1] as Int)) if (Maths.probability(it[2] as Double)) {
					val itemStack = (it[0] as ItemStack)
					itemStack.amount = Main.RANDOM.nextInt(it[3] as Int) + 1

					items.add(itemStack)
				}
			}

			Collections.shuffle(items, Main.RANDOM)

			(it.block.state as Chest).blockInventory.addItemStacks(items.toTypedArray())

			it.spawnFirework(1, FireworkEffect.builder().withColor(Colors.random()).with(FireworkEffect.Type.BALL_LARGE).build())
			it.playEffect(Effect.MOBSPAWNER_FLAMES, 0, 2)
		}
	}

	override fun applyKit(gamer: Gamer, kit: Kit) {
		gamer.clear()

		val player = gamer.player

		player.inventory.setItem(0, if (kit == Kits.PVP) Inventories.DIAMOND_SWORD_SHARPNESS else Inventories.DIAMOND_SWORD)
		player.inventory.addItemStacks(kit.items)
		player.fillRecraft()
		player.fillSoups()
		player.setArmor(Inventories.ARMOR_FULL_IRON)
	}

	data class FeastData(var enchantmentTable: Array<Float> = arrayOf(0F, 0F, 0F)): Data()
}