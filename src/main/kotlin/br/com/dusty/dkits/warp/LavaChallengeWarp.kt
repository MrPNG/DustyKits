package br.com.dusty.dkits.warp

import br.com.dusty.dkits.gamer.Gamer
import br.com.dusty.dkits.kit.Kit
import br.com.dusty.dkits.util.inventory.Inventories
import br.com.dusty.dkits.util.inventory.fillRecraft
import br.com.dusty.dkits.util.inventory.fillSoups
import br.com.dusty.dkits.util.rename
import br.com.dusty.dkits.util.setDescription
import br.com.dusty.dkits.util.text.Text
import br.com.dusty.dkits.util.text.TextColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object LavaChallengeWarp: Warp() {

	init {
		name = "Lava Challenge"
		icon = ItemStack(Material.LAVA_BUCKET)

		icon.rename(Text.of(name).color(TextColor.GOLD).toString())
		icon.setDescription(description)

		entryKit = LavaChallengeKit

		loadData()
	}

	override fun applyKit(gamer: Gamer, kit: Kit) {
		gamer.clear()

		val player = gamer.player

		player.fillRecraft()
		player.fillSoups()
	}

	object LavaChallengeKit: Kit() {

		init {
			isDummy = false
		}
	}
}
