package br.com.dusty.dkits.warp

import br.com.dusty.dkits.gamer.GamerRegistry
import br.com.dusty.dkits.util.Tasks
import br.com.dusty.dkits.util.rename
import br.com.dusty.dkits.util.setDescription
import br.com.dusty.dkits.util.text.Text
import br.com.dusty.dkits.util.text.TextColor
import net.minecraft.server.v1_12_R1.DataWatcher
import net.minecraft.server.v1_12_R1.DataWatcherObject
import net.minecraft.server.v1_12_R1.DataWatcherRegistry
import net.minecraft.server.v1_12_R1.PacketPlayOutEntityMetadata
import org.bukkit.Material
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer
import org.bukkit.inventory.ItemStack

object ArenaWarp: Warp() {

	init {
		name = "Arena"
		icon = ItemStack(Material.STONE_SWORD)

		icon.rename(Text.of(name).color(TextColor.GOLD).toString())
		icon.setDescription(description)

		entryKit = GameWarpKit

		hasShop = true

		loadData()
	}
}
