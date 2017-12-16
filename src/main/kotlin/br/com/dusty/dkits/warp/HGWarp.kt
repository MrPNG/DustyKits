package br.com.dusty.dkits.warp

import br.com.dusty.dkits.Main
import br.com.dusty.dkits.command.gameplay.WarpCommand
import br.com.dusty.dkits.command.staff.LocationCommand
import br.com.dusty.dkits.gamer.EnumMode
import br.com.dusty.dkits.gamer.EnumRank
import br.com.dusty.dkits.gamer.Gamer
import br.com.dusty.dkits.gamer.GamerRegistry
import br.com.dusty.dkits.kit.Kit
import br.com.dusty.dkits.kit.Kits
import br.com.dusty.dkits.util.*
import br.com.dusty.dkits.util.ItemStacks.potions
import br.com.dusty.dkits.util.cosmetic.Colors
import br.com.dusty.dkits.util.entity.spawnFirework
import br.com.dusty.dkits.util.gamer.gamer
import br.com.dusty.dkits.util.inventory.Inventories
import br.com.dusty.dkits.util.inventory.addItemStacks
import br.com.dusty.dkits.util.text.Text
import br.com.dusty.dkits.util.text.TextColor
import br.com.dusty.dkits.warp.HGWarp.HGState.*
import org.bukkit.*
import org.bukkit.Material.*
import org.bukkit.block.Chest
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.entity.EntityPickupItemEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ShapelessRecipe
import org.bukkit.potion.PotionType.*
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask
import java.util.*

object HGWarp: Warp() {

	val NAME = "Evento HG"

	val PREFIX = Text.basicOf("[").neutral("EventoHG").basic("] ").toString()

	val GAME_CANCELLED = Text.neutralPrefix().append(PREFIX).basic("Esta partida do ").neutral(NAME).basic(" foi ").neutral("cancelada").basic("!").toString()
	val GAME_DURATION_LIMIT = Text.neutralPrefix().append(PREFIX).basic("O tempo de duração do ").neutral(NAME).basic(" chegou ao ").neutral("limite").basic("!").toString()

	val ALREADY_HAVE_KIT = Text.negativePrefix().basic("Você ").negative("já").basic(" está ").negative("usando").basic(" um kit!").toString()

	val NOT_IN_PLAY_MODE = Text.negativePrefix().basic("Você deve estar no modo ").negative("PLAY").basic(" para entrar no ").negative(NAME).basic("!").toString()
	val GAME_IS_FULL = Text.negativePrefix().basic("O ").negative(name).basic(" está ").negative("lotado").basic("!").toString()

	val CHEST_POSITIONS = arrayOf(doubleArrayOf(-1.0, 0.0, -1.0),
	                              doubleArrayOf(1.0, 0.0, -1.0),
	                              doubleArrayOf(1.0, 0.0, 1.0),
	                              doubleArrayOf(-1.0, 0.0, 1.0),
	                              doubleArrayOf(-2.0, 0.0, -2.0),
	                              doubleArrayOf(-2.0, 0.0, 0.0),
	                              doubleArrayOf(-2.0, 0.0, 2.0),
	                              doubleArrayOf(0.0, 0.0, 2.0),
	                              doubleArrayOf(2.0, 0.0, 2.0),
	                              doubleArrayOf(2.0, 0.0, 0.0),
	                              doubleArrayOf(2.0, 0.0, -2.0),
	                              doubleArrayOf(0.0, 0.0, -2.0))

	lateinit var CHEST_LOCATIONS: List<Location>

	//Usage: arrayOf(itemStack, attempts, chances, maxAmount)
	val CHEST_ITEMS = arrayOf(arrayOf(ItemStack(IRON_HELMET), 1, 0.1, 1),
	                          arrayOf(ItemStack(IRON_CHESTPLATE), 1, 0.1, 1),
	                          arrayOf(ItemStack(IRON_LEGGINGS), 1, 0.1, 1),
	                          arrayOf(ItemStack(IRON_BOOTS), 1, 0.1, 1),
	                          arrayOf(ItemStack(IRON_SWORD), 1, 0.1, 1),
	                          arrayOf(ItemStack(BOW), 2, 0.25, 1),
	                          arrayOf(ItemStack(ARROW), 2, 0.25, 16),
	                          arrayOf(ItemStack(EXP_BOTTLE), 2, 0.5, 8),
	                          arrayOf(ItemStack(WATER_BUCKET), 1, 0.25, 1),
	                          arrayOf(ItemStack(LAVA_BUCKET), 1, 0.25, 1),
	                          arrayOf(Inventories.SOUP, 2, 0.5, 8),
	                          arrayOf(Inventories.RED_MUSHROOMS, 2, 0.5, 8),
	                          arrayOf(Inventories.BROWN_MUSHROOMS, 2, 0.5, 8),
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

	val playersLimit = 20.0
	val minimumPlayers = 2

	val invincibility = 60
	val feast = 120

	var state = CLOSED

	val interval = 20 * 60 * 20
	val preparation = 60 * 2
	val maxDuration = 1200

	var nextGame = 0L
	var start = 0L

	val moneyPrize = 5000
	val xpPrize = 2000

	var moneyPrizeNormalized = 0.0
	var xpPrizeNormalized = 0.0

	lateinit var world: World

	var spreadRadius = 0.0

	lateinit var spawnLocation: Location
	lateinit var feastLocation: Location

	var task: BukkitTask? = null

	init {
		name = NAME

		icon = ItemStack(CHEST)
		icon.rename(Text.of(name).color(TextColor.GOLD).toString())
		icon.description(description, true)

		type = EnumWarpType.BOTH

		aliases = arrayOf(name.replace(" ", "").toLowerCase(), "hg")

		overriddenEvents = arrayOf(InventoryClickEvent::class.java, PlayerDeathEvent::class.java, PlayerDropItemEvent::class.java, EntityPickupItemEvent::class.java)

		entryKit = SIMPLE_GAME_WARP_KIT

		durabilityBehavior = EnumDurabilityBehavior.BREAK

		data = HGData()

		loadData()

		LocationCommand.CUSTOM_EXECUTORS.add(this)
		WarpCommand.CUSTOM_EXECUTORS.add(this)

		val chocolateMilkRecipe = ShapelessRecipe(ItemStack(MUSHROOM_SOUP).rename("Chocolate Milk"))
		chocolateMilkRecipe.addIngredient(1, BOWL)
		chocolateMilkRecipe.addIngredient(1, INK_SACK, 3)

		val cactiJuiceRecipe = ShapelessRecipe(ItemStack(MUSHROOM_SOUP).rename("Cacti Juice"))
		cactiJuiceRecipe.addIngredient(1, BOWL)
		cactiJuiceRecipe.addIngredient(1, CACTUS)

		Bukkit.addRecipe(chocolateMilkRecipe)
		Bukkit.addRecipe(cactiJuiceRecipe)

		prepare()
	}

	fun schedule() {
		nextGame = interval * 50L

		task?.cancel()
		task = Tasks.sync(object: BukkitRunnable() {
			override fun run() {
				nextGame -= 50

				if (nextGame == 0L) {
					cancel()

					prepare()
				}
			}
		}, 0L, 20L)
	}

	fun prepare() {
		if (state == CLOSED) {
			state = PREPARING

			val data = data as HGData

			val worldData = data.worldsData[Main.RANDOM.nextInt(data.worldsData.size)]
			world = Worlds.load(worldData.name, false) ?: return

			spreadRadius = worldData.spreadRadius

			spawnLocation = worldData.spawn.toLocation(world)
			feastLocation = worldData.feast.toLocation(world)

			CHEST_LOCATIONS = CHEST_POSITIONS.map { feastLocation.clone().add(it[0], it[1], it[2]) }

			task?.cancel()
			task = Tasks.sync(object: BukkitRunnable() {
				var i = preparation

				override fun run() {
					when {
						i > 0 && i % 60 == 0          -> {
							val div = i / 60

							Bukkit.broadcastMessage(Text.neutralPrefix().append(PREFIX).basic("O ").neutral(NAME).basic(" iniciará em ").neutral(div.toString() + if (div == 1) " minuto" else " minutos").basic(
									", use o comando ").neutral("/hg").basic(" para participar!").toString())
						}
						i in (1 .. 60) && i % 15 == 0 -> Bukkit.broadcastMessage(Text.neutralPrefix().append(PREFIX).basic("O ").neutral(NAME).basic(" iniciará em ").neutral(i.toString() + " segundos").basic(
								", use o comando ").neutral("/hg").basic(" para participar!").toString())
						i == 10                       -> Bukkit.broadcastMessage(Text.neutralPrefix().append(PREFIX).basic("O ").neutral(NAME).basic(" iniciará em ").neutral("10 segundos").basic(
								", use o comando ").neutral("/hg").basic(" para participar!").toString())
						i in (1 .. 5)                 -> Bukkit.broadcastMessage(Text.neutralPrefix().append(PREFIX).basic("O ").neutral(NAME).basic(" iniciará em ").neutral(i.toString() + if (i == 1) " segundo" else " segundos").basic(
								", use o comando ").neutral("/hg").basic(" para participar!").toString())
						i == 0                        -> {
							cancel()

							val gamers = GamerRegistry.onlineGamers().filter { it.warp == Warps.HG }

							if (gamers.size < minimumPlayers) {
								Bukkit.broadcastMessage(Text.neutralPrefix().append(PREFIX).basic("Não há ").neutral("jogadores").basic(" suficientes para o ").neutral(NAME).basic("!").toString())

								close(gamers, true)
							} else {
								start(gamers)
							}
						}
					}

					i--
				}
			}, 0L, 20L)

			state = OPEN
		}
	}

	fun close(gamers: Collection<Gamer>, reschedule: Boolean) {
		state = FINISHING

		Bukkit.broadcastMessage(GAME_CANCELLED)

		gamers.forEach { it.sendToWarp(Warps.LOBBY, true, true) }

		Worlds.rollback(world.name)

		task?.cancel()

		if (reschedule) schedule()

		state = CLOSED
	}

	fun start(gamers: Collection<Gamer>) {
		state = STARTING

		val normalizationFactor = Math.sqrt(gamers.size / playersLimit)
		moneyPrizeNormalized = moneyPrize * normalizationFactor
		xpPrizeNormalized = xpPrize * normalizationFactor

		gamers.forEach {
			val player = it.player

			if (it.kit.isDummy) it.kit = Kits.PVP

			applyKit(it, it.kit)

			val location = spawnLocation.spread(spreadRadius)
			location.y = world.getHighestBlockYAt(location) + 1.0

			player.teleport(location)
			player.gameMode = GameMode.SURVIVAL

			it.invincibility = invincibility * 1000L
		}

		task?.cancel()
		task = Tasks.sync(object: BukkitRunnable() {
			var i = 0

			override fun run() {
				when {
					i < invincibility && i % 60 == 0                               -> {
						val div = (invincibility - i) / 60

						val message = Text.neutralPrefix().append(PREFIX).basic("A ").neutral("invencibilidade").basic(" acabará em ").neutral(div.toString() + if (div == 1) " minuto" else " minutos").basic(
								"!").toString()

						gamers.forEach {
							if (it.warp == Warps.HG) it.player.sendMessage(message)
						}
					}
					i in ((invincibility - 60) until invincibility) && i % 15 == 0 -> {
						val dif = (invincibility - i)

						val message = Text.neutralPrefix().append(PREFIX).basic("A ").neutral("invencibilidade").basic(" acabará em ").neutral(dif.toString() + " segundos").basic("!").toString()

						gamers.forEach {
							if (it.warp == Warps.HG) it.player.sendMessage(message)
						}
					}
					i == invincibility - 10                                        -> {
						val message = Text.neutralPrefix().append(PREFIX).basic("A ").neutral("invencibilidade").basic(" acabará em ").neutral("10 segundos").basic("!").toString()

						gamers.forEach {
							if (it.warp == Warps.HG) it.player.sendMessage(message)
						}
					}
					i in ((invincibility - 5) until invincibility)                 -> {
						val dif = (invincibility - i)

						val message = Text.neutralPrefix().append(PREFIX).basic("A ").neutral("invencibilidade").basic(" acabará em ").neutral(dif.toString() + if (dif == 1) " segundo" else " segundos").basic(
								"!").toString()

						gamers.forEach {
							if (it.warp == Warps.HG) it.player.sendMessage(message)
						}
					}
					i == invincibility                                             -> {
						val message = Text.neutralPrefix().append(PREFIX).basic("A ").neutral("invencibilidade").basic(" acabou!").toString()

						gamers.forEach {
							if (it.warp == Warps.HG) it.player.sendMessage(message)
						}
					}
					i in ((feast - 300) until feast) && i % 60 == 0                -> {
						val div = (feast - i) / 60

						val message = Text.neutralPrefix().append(PREFIX).basic("O ").neutral("feast").basic(" iniciará em ").neutral(div.toString() + if (div == 1) " minuto" else " minutos").basic(
								"!").toString()

						gamers.forEach {
							if (it.warp == Warps.HG) it.player.sendMessage(message)
						}
					}
					i in ((feast - 60) until feast) && i % 15 == 0                 -> {
						val dif = (feast - i)

						val message = Text.neutralPrefix().append(PREFIX).basic("O ").neutral("feast").basic(" iniciará em ").neutral(dif.toString() + " segundos").basic("!").toString()

						gamers.forEach {
							if (it.warp == Warps.HG) it.player.sendMessage(message)
						}
					}
					i == feast - 10                                                -> {
						val message = Text.neutralPrefix().append(PREFIX).basic("O ").neutral("feast").basic(" iniciará em ").neutral("10 segundos").basic("!").toString()

						gamers.forEach {
							if (it.warp == Warps.HG) it.player.sendMessage(message)
						}
					}
					i in ((feast - 5) until feast)                                 -> {
						val dif = (feast - i)

						val message = Text.neutralPrefix().append(PREFIX).basic("O ").neutral("feast").basic(" iniciará em ").neutral(dif.toString() + if (dif == 1) " segundo" else " segundos").basic(
								"!").toString()

						gamers.forEach {
							if (it.warp == Warps.HG) it.player.sendMessage(message)
						}
					}
					i == feast                                                     -> {
						feast()

						val message = Text.neutralPrefix().append(PREFIX).basic("O ").neutral("feast").basic(" começou!").toString()

						gamers.forEach {
							if (it.warp == Warps.HG) it.player.sendMessage(message)
						}
					}
					i == maxDuration                                               -> {
						cancel()

						gamers.forEach {
							if (it.warp == Warps.HG) it.player.sendMessage(GAME_DURATION_LIMIT)
						}

						end(GamerRegistry.onlineGamers().filter { it.warp == Warps.HG })
					}
				}

				i++
			}
		}, 0L, 20L)

		start = System.currentTimeMillis()

		Bukkit.broadcastMessage(Text.neutralPrefix().append(PREFIX).basic("O ").neutral(NAME).basic(" começou!").toString())

		state = ONGOING
	}

	fun end(gamers: Collection<Gamer>) {
		state = FINISHING

		val iterator = gamers.iterator()
		val size = gamers.size

		val moneyPrizeIndividual = moneyPrizeNormalized / size
		val xpPrizeIndividual = xpPrizeNormalized / size

		gamers.forEach {
			it.addMoney(moneyPrizeIndividual)
			it.addXp(xpPrizeIndividual)

			it.fly(true)

			Tasks.sync(object: BukkitRunnable() {
				var i = 10

				override fun run() {
					if (--i > 0) it.player.location.spawnFirework(1, FireworkEffect.builder().withColor(Colors.random()).with(FireworkEffect.Type.BALL).build())
					else cancel()
				}
			}, 0L, 40L)
		}

		task?.cancel()
		task = Tasks.sync(Runnable {
			gamers.forEach {
				if (it.player.isOnline) {
					it.fly(false)
					it.sendToWarp(Warps.LOBBY, true, true)
				}
			}

			Worlds.rollback(world.name)

			schedule()

			state = CLOSED
		}, 400L)

		val message: String

		if (size == 1) {
			val gamer = iterator.next()

			message = Text.neutralPrefix().append(PREFIX).basic("O jogador ").neutral(gamer.player.displayName.clearFormatting()).basic(" venceu o ").neutral(NAME).basic("!").toString()
		} else {
			var text = Text.neutralOf(iterator.next().player.displayName.clearFormatting())

			for (i in 1 .. size) text = text.basic(if (i == size) " e " else ", ").neutral(iterator.next().player.displayName.clearFormatting())

			message = Text.neutralPrefix().append(PREFIX).basic("Os jogadores ").append(text.toString()).basic(" venceram o ").neutral(NAME).basic("!").toString()
		}

		Bukkit.broadcastMessage(message)
	}

	fun feast() {
		feastLocation.block.type = ENCHANTMENT_TABLE

		CHEST_LOCATIONS.forEach {
			val block = it.block

			if (block.type != CHEST) block.type = CHEST

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

			(block.state as Chest).blockInventory.run {
				clear()
				addItemStacks(items.toTypedArray())
			}
		}
	}

	@EventHandler
	fun onInventoryClick(event: InventoryClickEvent) {
		val gamer = (event.whoClicked as Player).gamer()

		if (gamer.warp == this && gamer.kit.isDummy) event.isCancelled = true
	}

	@EventHandler
	fun onPlayerDeath(event: PlayerDeathEvent) {
		val player = event.entity
		val gamer = player.gamer()

		if (gamer.warp == this) {
			event.deathMessage = null

			if (gamer.isCombatTagged()) gamer.combatPartner!!.kill(gamer)

			gamer.resetKillStreak()

			Tasks.sync(Runnable {
				player.spigot().respawn()

				gamer.sendToWarp(Warps.LOBBY, true, true)
			})
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	fun onPlayerInteract(event: PlayerInteractEvent) {
		val player = event.player
		val gamer = player.gamer()

		if (gamer.warp == this && state == ONGOING && event.item !in gamer.kit.items) event.isCancelled = false
	}

	@EventHandler
	fun onPlayerDropItem(event: PlayerDropItemEvent) {
		val gamer = event.player.gamer()

		if (gamer.warp == this && event.itemDrop.itemStack in gamer.kit.items) event.isCancelled = true
	}

	@EventHandler
	fun onEntityPickupItem(event: EntityPickupItemEvent) {
		if (event.entity is Player) {
			val player = event.entity as Player
			val gamer = player.gamer()

			if (gamer.warp == this && state == ONGOING) event.isCancelled = false
		}
	}

	override fun isAllowed(kit: Kit, gamer: Gamer, announce: Boolean): Boolean = when {
		state == ONGOING           -> {
			if (announce) gamer.player.sendMessage(ALREADY_HAVE_KIT)

			false
		}
		!enabledKits.contains(kit) -> {
			if (announce) gamer.player.sendMessage(Text.negativePrefix().basic("Você ").negative("não pode").basic(" usar o kit ").negative(kit.name).basic(" nesta warp!").toString())

			false
		}
		!gamer.hasKit(kit)         -> {
			if (announce) gamer.player.sendMessage(Text.negativePrefix().basic("Você ").negative("não").basic(" possui o kit ").negative(kit.name).basic("!").toString())

			false
		}
		else                       -> true
	}

	override fun dispatchGamer(gamer: Gamer, newWarp: Warp) {
		gamer.player.gameMode = GameMode.ADVENTURE

		if (state == ONGOING) {
			val gamers = GamerRegistry.onlineGamers().filter { it.warp == Warps.HG }
			val size = gamers.size

			if (size == 1) {
				end(gamers)
			} else {
				val message = Text.neutralPrefix().append(PREFIX).basic("O jogador ").neutral(gamer.player.displayName.clearFormatting()).basic(if (newWarp == Warps.NONE) " saiu do " else " foi eliminado do ").neutral(
						NAME).basic(", agora há ").neutral(size).basic(" jogadores!").toString()

				gamers.forEach {
					it.player.sendMessage(message)
				}
			}
		}
	}

	override fun applyKit(gamer: Gamer, kit: Kit) {
		if (state == STARTING) {
			gamer.clear()
			gamer.player.inventory.run {
				if (kit == Kits.PVP) addItem(Inventories.WOOD_SWORD)

				addItemStacks(kit.items)
				addItem(Inventories.COMPASS)
			}
		} else if (kit.isDummy) {
			super.applyKit(gamer, kit)
		}
	}

	override fun execute(gamer: Gamer, alias: String, args: Array<String>) {
		val player = gamer.player

		when (alias) {
			in aliases -> {
				if (gamer.rank.isLowerThan(EnumRank.ADMIN) || args.isEmpty()) when (state) {
					CLOSED    -> {
						player.sendMessage(Text.neutralPrefix().append(PREFIX).basic("O próximo ").neutral(NAME).basic(" inicia em ").neutral(nextGame.formatPeriod()).basic("!").toString())
					}
					OPEN      -> {
						when {
							gamer.mode != EnumMode.PLAY                                                 -> player.sendMessage(NOT_IN_PLAY_MODE)
							gamer.warp == this                                                          -> gamer.sendToWarp(Warps.LOBBY, false, true)
							GamerRegistry.onlineGamers().filter { it.warp == this }.size < playersLimit -> gamer.sendToWarp(this, false, true)
							else                                                                        -> player.sendMessage(GAME_IS_FULL)
						}
					}
					ONGOING   -> player.sendMessage(Text.neutralPrefix().append(PREFIX).basic("O ").neutral(NAME).basic(" iniciou há ").neutral((System.currentTimeMillis() - start).formatPeriod()).basic(
							" e ainda há ").neutral(GamerRegistry.onlineGamers().filter { it.warp == Warps.HG }.size).basic(" jogadores vivos!").toString())
					FINISHING -> player.sendMessage(Text.neutralPrefix().append(PREFIX).basic("O ").neutral(NAME).basic(" está sendo ").neutral("finalizado").basic("!").toString())
				} else when (args[0]) {
					"force" -> {
						when (state) {
							CLOSED -> prepare()
							OPEN   -> start(GamerRegistry.onlineGamers().filter { it.warp == this })
							else   -> player.sendMessage(Text.negativePrefix().negative("Já").basic(" está acontecendo um ").negative(NAME).basic("!").toString())
						}
					}
					"close" -> {
						when (state) {
							OPEN, ONGOING -> close(GamerRegistry.onlineGamers().filter { it.warp == this }, false)
						}
					}
				}
			}
			"location" -> {
				val name = args[0].toLowerCase()

				when {
					!Worlds.exists(name) -> player.sendMessage(Text.negativePrefix().negative("Não").basic(" há um mundo com o nome \"").negative(name).basic("\"!").toString())
					else                 -> {
						val data = data as HGData

						val worldsData = data.worldsData.toMutableList()

						val worldData = worldsData.firstOrNull { it.name == name } ?: WorldData(name)
						worldsData.remove(worldData)

						when (args[1].toLowerCase()) {
							"spawn"        -> {
								worldData.spawn = player.location.normalize().toSimpleLocation()

								player.sendMessage(Text.positivePrefix().basic("Você ").positive("definiu").basic(" o ").positive("spawn").basic(" do mundo ").positive(name).basic("!").toString())
							}
							"feast"        -> {
								worldData.feast = player.location.normalize().toSimpleLocation()

								player.sendMessage(Text.positivePrefix().basic("Você ").positive("definiu").basic(" o local do ").positive("feast").basic(" do mundo ").positive(name).basic("!").toString())
							}
							"spreadradius" -> {
								val spreadRadius = args[2].toDoubleOrNull()

								if (spreadRadius == null) {
									player.sendMessage(Text.negativePrefix().basic("\"").negative(args[2]).basic("\" não é um número ").negative("válido").basic("!").toString())
								} else {
									worldData.spreadRadius = spreadRadius

									player.sendMessage(Text.positivePrefix().basic("Você ").positive("definiu").basic(" o ").positive("raio").basic(" de dispersão do mundo ").positive(name).basic(
											"!").toString())
								}
							}
							else           -> player.sendMessage(Text.negativePrefix().negative("Não").basic(" há uma localização com o nome \"").negative(args[1]).basic("\"!").toString())
						}

						worldsData.add(worldData)

						data.worldsData = worldsData.toTypedArray()

						saveData()
					}
				}
			}
		}
	}

	override fun finalize() {
		close(GamerRegistry.onlineGamers().filter { it.warp == this }, false)
	}

	enum class HGState {

		CLOSED,
		PREPARING,
		OPEN,
		STARTING,
		ONGOING,
		FINISHING
	}

	class WorldData(var name: String = "", var spawn: SimpleLocation = SimpleLocation(), var feast: SimpleLocation = SimpleLocation(), var spreadRadius: Double = 0.0)

	class HGData(var worldsData: Array<WorldData> = arrayOf()): Data()
}
