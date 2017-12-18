package br.com.dusty.dkits.command.staff

import br.com.dusty.dkits.command.PlayerCustomCommand
import br.com.dusty.dkits.gamer.EnumRank
import br.com.dusty.dkits.store.Store
import br.com.dusty.dkits.util.gamer.gamer
import br.com.dusty.dkits.util.text.Text
import br.com.dusty.dkits.util.web.WebAPI
import io.reactivex.Observable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import org.bukkit.Bukkit
import org.bukkit.entity.Player

object VipCommand: PlayerCustomCommand(EnumRank.ADMIN, "vip") {

	override fun execute(sender: Player, alias: String, args: Array<String>): Boolean {
		if (args.size < 3) {
			sender.sendMessage(Text.negativePrefix().negative("Uso:").basic(" /vip ").negative("<rank> <nomeDoJogador> <tempoEmDias>").toString())
		} else {
			val rank = EnumRank[args[0].toUpperCase()]

			if (rank == EnumRank.NONE) {
				sender.sendMessage(Text.negativePrefix().negative("Uso:").basic(" /vip ").negative("<mvp>/<pro> <nomeDoJogador> <tempoEmDias>").toString())
			} else {
				val player = Bukkit.getPlayerExact(args[1])

				if (player == null) {
					sender.sendMessage(Text.negativePrefix().negative("Não").basic(" há um jogador online chamado ").negative("\"" + args[2] + "\"").basic("!").toString())
				} else {
					val days = args[2].toIntOrNull() ?: 0
					val time = days * (1000 * 60 * 60 * 24).toLong()

					val gamer = player.gamer()
					gamer.rank = rank

					val onNext = Consumer<Player> {
						WebAPI.addPurchase(Store.PseudoPurchase(0, player.uniqueId.toString(), 2, if (rank == EnumRank.PRO) 1001 else 1003, time))

						sender.sendMessage(Text.positivePrefix().basic("Você deu um ").positive(rank.name).basic(" para o jogador ").positive(player.name).basic(" por ").positive(days).basic(" dias!").toString())
						player.sendMessage(Text.positivePrefix().basic("Você ganhou um ").positive(rank.name).basic(" por ").positive(days).basic(" dias!").toString())
					}

					val onError = Consumer<Throwable> {
						sender.sendMessage(Text.negativePrefix().negative("Não").basic(" foi possível dar um ").negative(rank.name).basic(" para o jogador ").negative(player.name).basic("!").toString())
					}

					Observable.just(player).subscribeOn(Schedulers.io()).subscribe(onNext, onError)
				}
			}
		}

		return false
	}
}
