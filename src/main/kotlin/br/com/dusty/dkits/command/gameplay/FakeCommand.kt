package br.com.dusty.dkits.command.gameplay

import br.com.dusty.dkits.command.PlayerCustomCommand
import br.com.dusty.dkits.gamer.EnumRank
import br.com.dusty.dkits.gamer.GamerRegistry
import br.com.dusty.dkits.util.Tasks
import br.com.dusty.dkits.util.entity.gamer
import br.com.dusty.dkits.util.stdlib.clearFormatting
import br.com.dusty.dkits.util.text.Text
import br.com.dusty.dkits.util.web.MojangAPI
import io.reactivex.Observable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import org.bukkit.entity.Player

object FakeCommand: PlayerCustomCommand(EnumRank.YOUTUBER, "fake", "unfake") {

	override fun execute(sender: Player, alias: String, args: Array<String>): Boolean {
		val gamer = sender.gamer()

		when (alias) {
			"fake"   -> {
				if (args.isEmpty()) sender.sendMessage(Text.negativePrefix().basic("Uso: /fake ").negative("<nome>").toString())
				else {
					val name = args[0].clearFormatting()

					when {
						name.length > 14 -> sender.sendMessage(Text.negativePrefix().basic("O nick ").negative(name).basic(" é muito ").negative("longo").basic("!").toString())
						GamerRegistry.onlineGamers().any {
							it.player.name == name || it.displayName.equals(name, true)
						}                -> sender.sendMessage(Text.negativePrefix().negative("Já").basic(" existe um jogador com o nick ").negative(name).basic("!").toString())
						else             -> {
							val onNext = Consumer<String> {
								if (MojangAPI.profile(it) != null) sender.sendMessage(Text.negativePrefix().negative("Já").basic(" existe um jogador com o nick ").negative(it).basic("!").toString())
								else {
									gamer.run {
										displayName = it

										Tasks.sync(Runnable { refreshTag() })
									}

									sender.sendMessage(Text.positivePrefix().basic("Agora seu ").positive("nick").basic(" é ").positive(it).basic("!").toString())
								}
							}

							val onError = Consumer<Throwable> {
								sender.sendMessage(Text.negativePrefix().positive("Não").basic(" foi ").negative("possível").basic(" alterar o seu ").positive("nick").basic("!").toString())
							}

							Observable.just(name).subscribeOn(Schedulers.io()).subscribe(onNext, onError)
						}
					}
				}
			}
			"unfake" -> {
				val name = sender.name

				gamer.run {
					displayName = name

					refreshTag()
				}

				sender.sendMessage(Text.positivePrefix().basic("Agora seu ").positive("nick").basic(" é ").positive(name).basic("!").toString())
			}
		}

		return false
	}
}
