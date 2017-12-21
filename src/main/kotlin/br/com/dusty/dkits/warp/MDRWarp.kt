package br.com.dusty.dkits.warp

import br.com.dusty.dkits.util.description
import br.com.dusty.dkits.util.rename
import br.com.dusty.dkits.util.text.Text
import br.com.dusty.dkits.util.text.TextColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object MDRWarp: Warp() {

	init {
		name = "Mãe da Rua"

		icon = ItemStack(Material.REDSTONE)
		icon.rename(Text.of(name).color(TextColor.GOLD).toString())
		icon.description(description, true)

		type = EnumWarpType.EVENT

		entryKit = EMPTY_NOT_DUMMY_KIT

		loadData()
	}
}
