package br.com.dusty.dkits.store

import br.com.dusty.dkits.gamer.EnumRank
import br.com.dusty.dkits.gamer.Gamer
import br.com.dusty.dkits.gamer.PrimitiveGamer
import br.com.dusty.dkits.kit.Kits
import br.com.dusty.dkits.util.text.Text
import br.com.dusty.dkits.util.web.HttpClients
import com.google.common.collect.HashBiMap
import org.bukkit.Bukkit

object Store {

	val KIT_BY_ID = HashBiMap.create(linkedMapOf(2 to Kits.ELFO, 3 to Kits.GRANDPA, 4 to Kits.STOMPER, 5 to Kits.THOR, 6 to Kits.FISHERMAN, 7 to Kits.ENDERMAGE,
//	                                                8 to Kits.SPIDER,
                                                 9 to Kits.RING, 10 to Kits.SNAIL,
//	                                                11 to Kits.GLADIATOR
                                                 12 to Kits.NINJA,
//	                                                13 to Kits.BUBBLE,
//	                                                14 to Kits.PRISAO,
                                                 15 to Kits.CHICKENMAN,
//	                                                16 to Kits.ODIN,
//                                                  17 to Kits.SPECIALIST,
//                                                  18 to Kits.JUMPER,
                                                 19 to Kits.VIKING,
//                                                  20 to Kits.DESARMADOR,
//                                                  21 to Kits.SNAIC,
                                                 22 to Kits.POSEIDON, 23 to Kits.CHEMIST, 24 to Kits.KANGAROO
//                                                  25 to Kits.MAGMA
	                                            ))

	val ID_BY_KIT = KIT_BY_ID.inverse()

	fun parsePurchases(json: String?) = when (json) {
		null, "[]" -> Purchases()
		else       -> HttpClients.GSON.fromJson(json, PurchasesData::class.java)?.compras ?: Purchases()
	}

	class PurchasesData {

		val compras = Purchases()

		override fun toString(): String {
			return "PurchasesData(compras=$compras)"
		}
	}

	class Purchases {

		val kit = arrayOf<KitPurchases>()
		val vip = arrayOf<RankPurchases>()
		val vantagem = arrayOf<AdvantagePurchases>()

		fun loadRank(gamer: Gamer) {
			EnumRank.values().forEach {
				if (gamer.player.hasPermission("dkits.rank." + it.name.toLowerCase())) gamer.rank = it
			}

			if (gamer.rank == EnumRank.NONE) vip.filter { it.datafinal > System.currentTimeMillis() }.forEach {
				var rank = EnumRank.DEFAULT

				when (it.vip) {
					1001, 1002 -> rank = EnumRank.PRO
					1003, 1004 -> rank = EnumRank.MVP
				}

				if (gamer.rank.isLowerThan(rank)) gamer.rank = rank
			}
		}

		fun loadKits(gamer: Gamer) {
			Kits.KITS.forEach {
				if (gamer.player.hasPermission("dkits.kit." + it.name.toLowerCase())) gamer.kits.add(it)
			}

			kit.forEach {
				val kit = KIT_BY_ID[it.kit]

				if (kit != null) gamer.kits.add(kit)
			}
		}

		fun loadAvantages(gamer: Gamer) {
			when {
				gamer.rank.isHigherThanOrEquals(EnumRank.MVP) -> {
					gamer.advantages.add(EnumAdvantage.SLOT)
					gamer.advantages.add(EnumAdvantage.HG_RESPAWN)
				}
				gamer.rank.isHigherThanOrEquals(EnumRank.PRO) -> gamer.advantages.add(EnumAdvantage.DOUBLE)
			}

			vantagem.filter { it.datafinal > System.currentTimeMillis() }.forEach {
				when (it.vantagem) {
					1005 -> gamer.advantages.add(EnumAdvantage.SLOT)
					1006 -> {
						val oldPrimitiveGamer = gamer.primitiveGamer

						val primitiveGamer = PrimitiveGamer(oldPrimitiveGamer.uuid)
						primitiveGamer.id = oldPrimitiveGamer.id
						primitiveGamer.clan = oldPrimitiveGamer.clan

						gamer.primitiveGamer = primitiveGamer

						gamer.player.kickPlayer(Text.basicOf("Entre ").positive("novamente").basic(" no servidor para que o \'").positive("reset").basic("\' do seu perfil seja ").positive("concluído").basic(
								"!").toString())
					}
					1007 -> gamer.advantages.add(EnumAdvantage.DOUBLE)
					1008 -> {
						gamer.primitiveGamer.money += 1000.0

						gamer.updateScoreboard()
					}
					1009 -> {

					}
					1010 -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "unban " + gamer.player.name)
					1011 -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "unmute " + gamer.player.name)
				}
			}
		}
	}

	class KitPurchases(val id: Int = 0, val kit: Int = 0)

	class RankPurchases(val id: Int = 0, val vip: Int = 0, val datafinal: Long = 0L)

	class AdvantagePurchases(val id: Int = 0, val vantagem: Int = 0, val datafinal: Long = 0L)

	class PseudoPurchase(val id: Int = 0, val uuid: String = "", val tipo: Int = 0, val item: Int = 0, val datafinal: Long = -1L)
}
