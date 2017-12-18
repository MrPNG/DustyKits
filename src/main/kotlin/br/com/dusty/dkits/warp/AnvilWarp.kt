package br.com.dusty.dkits.warp

import br.com.dusty.dkits.kit.Kit
import br.com.dusty.dkits.util.description
import br.com.dusty.dkits.util.rename
import br.com.dusty.dkits.util.text.Text
import br.com.dusty.dkits.util.text.TextColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object AnvilWarp: Warp() {

	init {
		name = "Anvil"

		icon = ItemStack(Material.ANVIL)
		icon.rename(Text.of(name).color(TextColor.GOLD).toString())
		icon.description(description, true)

		type = EnumWarpType.EVENT

		entryKit = Kit()

		loadData()
	}
}
