package br.com.dusty.dkits.util.inventory

import br.com.dusty.dkits.util.rename
import br.com.dusty.dkits.util.text.Text
import br.com.dusty.dkits.util.text.TextColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object ConfirmationMenu {

	val TITLE = Text.of("Confirmação").color(TextColor.GOLD).toString()

	val BUTTON_YES = ItemStack(Material.STAINED_GLASS_PANE, 1, 1.toShort(), 13.toByte()).rename(Text.of("Sim").color(TextColor.GOLD).toString())
	val BUTTON_NO = ItemStack(Material.STAINED_GLASS_PANE, 1, 1.toShort(), 14.toByte()).rename(Text.of("Não").color(TextColor.GOLD).toString())
}
