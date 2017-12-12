package br.com.dusty.dkits.warp

import br.com.dusty.dkits.command.gameplay.WarpCommand
import br.com.dusty.dkits.command.staff.LocationCommand
import br.com.dusty.dkits.gamer.Gamer
import br.com.dusty.dkits.util.*
import br.com.dusty.dkits.util.text.Text
import br.com.dusty.dkits.util.text.TextColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object HGWarp: Warp() {

	val PREFIX = Text.basicOf("[").neutral("EventoHG").basic("] ").toString()

	var state = HGState.CLOSED

	var nextGame = System.currentTimeMillis() + 1000 * 60 * 60 * 24
	var endOfGame = System.currentTimeMillis() + 1000 * 60 * 60 * 24

	init {
		name = "Evento HG"

		icon = ItemStack(Material.CHEST)
		icon.rename(Text.of(name).color(TextColor.GOLD).toString())
		icon.setDescription(description, true)

		type = EnumWarpType.EVENT

		aliases = arrayOf(name.replace(" ", "").toLowerCase(), "hg")

		data = HGData()

		loadData()

		LocationCommand.CUSTOM_EXECUTORS.add(this)
		WarpCommand.CUSTOM_EXECUTORS.add(this)
	}

	override fun execute(gamer: Gamer, alias: String, args: Array<String>) {
		val player = gamer.player

		when (alias) {
			in aliases -> {
				when (state) {
					HGWarp.HGState.CLOSED  -> {
						player.sendMessage(Text.neutralPrefix().append(PREFIX).basic("O próximo ").neutral("jogo").basic(" inicia em ").neutral((nextGame - System.currentTimeMillis()).periodString()).basic(
								"!").toString())
					}
					HGWarp.HGState.OPEN    -> TODO()
					HGWarp.HGState.ONGOING -> TODO()
				}
			}
			"location" -> {
				val name = args[0].toLowerCase()

				when {
					!Worlds.exists(name) -> player.sendMessage(Text.negativePrefix().negative("Não").basic(" há um mundo com o nome \"").negative(name).basic("\"!").toString())
					else                 -> {
						val data = data as HGData

						val worlds = data.worlds.toMutableList()

						val world = worlds.firstOrNull { it.name == name } ?: HGWorld()
						worlds.remove(world)

						when (args[1].toLowerCase()) {
							"spawn"        -> {
								world.spawn = player.location.normalize().toSimpleLocation()

								player.sendMessage(Text.positivePrefix().basic("Você ").positive("definiu").basic(" o ").positive("spawn").basic(" do mundo ").positive(name).basic("!").toString())
							}
							"feast"        -> {
								world.feast = player.location.normalize().toSimpleLocation()

								player.sendMessage(Text.positivePrefix().basic("Você ").positive("definiu").basic(" o local do ").positive("feast").basic(" do mundo ").positive(name).basic("!").toString())
							}
							"spreadradius" -> {
								val spreadRadius = args[1].toDoubleOrNull()

								if (spreadRadius == null) {
									player.sendMessage(Text.negativePrefix().basic("\"").negative(args[1]).basic("\" não é um número ").negative("válido").basic("!").toString())
								} else {
									world.spreadRadius = spreadRadius

									player.sendMessage(Text.positivePrefix().basic("Você ").positive("definiu").basic(" o ").positive("raio").basic(" de dispersão do mundo ").positive(name).basic(
											"!").toString())
								}
							}
							else           -> player.sendMessage(Text.negativePrefix().negative("Não").basic(" há uma localização com o nome \"").negative(args[1]).basic("\"!").toString())
						}

						worlds.add(world)

						data.worlds = worlds.toTypedArray()

						saveData()
					}
				}
			}
		}
	}

	enum class HGState {

		CLOSED,
		OPEN,
		ONGOING
	}

	class HGWorld(var name: String = "", var spawn: SimpleLocation = SimpleLocation(), var feast: SimpleLocation = SimpleLocation(), var spreadRadius: Double = 0.0)

	class HGData(var worlds: Array<HGWorld> = arrayOf()): Data()
}
