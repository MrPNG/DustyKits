package br.com.dusty.dkits.util.inventory

import br.com.dusty.dkits.util.enchant
import br.com.dusty.dkits.util.inventory.Inventories.BACKGROUND
import br.com.dusty.dkits.util.inventory.Inventories.BOWLS
import br.com.dusty.dkits.util.inventory.Inventories.BROWN_MUSHROOMS
import br.com.dusty.dkits.util.inventory.Inventories.BUTTON_BACK
import br.com.dusty.dkits.util.inventory.Inventories.RED_MUSHROOMS
import br.com.dusty.dkits.util.inventory.Inventories.SOUP
import br.com.dusty.dkits.util.rename
import br.com.dusty.dkits.util.text.Text
import br.com.dusty.dkits.util.text.TextColor
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Material.*
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.BookMeta

fun Inventory.addItemStacks(itemStacks: Array<ItemStack?>) {
	var i = 0

	while (this.getItem(i) != null) i++

	for (itemStack in itemStacks) this.setItem(i++, itemStack)
}

/**
 * Aplica um [ItemStack][] contendo, nessa ordem, 'helmet', 'chestplate', 'leggings' e 'boots' como armadura de um [Player].
 *
 * @param player
 * @param itemStacks
 */
fun Player.setArmor(itemStacks: Array<ItemStack?>) {
	inventory.apply {
		helmet = itemStacks[0]
		chestplate = itemStacks[1]
		leggings = itemStacks[2]
		boots = itemStacks[3]
	}
}

fun Inventory.fillBackground(backButton: Boolean): Inventory {
	for (i in 0 until this.size) this.setItem(i, BACKGROUND)

	if (backButton) this.setItem(0, BUTTON_BACK)

	return this
}

fun Player.openInventory(player: Player) {
	openInventory(player.inventory)
	sendMessage(Text.positivePrefix().basic("Você está ").positive("vendo").basic(" o inventário de ").positive(player.name).toString())
}

fun Player.fillSoups(fullInventory: Boolean) = inventory.apply { (0 .. (if (fullInventory) 35 else 8)).filter { getItem(it) == null }.forEach { setItem(it, SOUP) } }

fun Player.fillRecraft(): Inventory = inventory.apply {
	setItem(13, RED_MUSHROOMS)
	setItem(14, BROWN_MUSHROOMS)
	setItem(15, BOWLS)
}

object Inventories {

	val SOUP = ItemStack(MUSHROOM_SOUP)
	val BOWL = ItemStack(Material.BOWL)

	val RED_MUSHROOMS = ItemStack(RED_MUSHROOM, 64)
	val BROWN_MUSHROOMS = ItemStack(BROWN_MUSHROOM, 64)
	val BOWLS = ItemStack(Material.BOWL, 64)

	val COCOA_BEAN = ItemStack(Material.INK_SACK, 64, 3.toShort())

	val COMPASS = ItemStack(Material.COMPASS)

	val SOUPS_TITLE = Text.of("Sopas").color(TextColor.GOLD).toString()
	val RECRAFT_TITLE = Text.of("Recraft").color(TextColor.GOLD).toString()

	val NO_ARMOR = arrayOfNulls<ItemStack>(4)
	val ARMOR_FULL_IRON: Array<ItemStack?> = arrayOf(ItemStack(IRON_HELMET), ItemStack(IRON_CHESTPLATE), ItemStack(IRON_LEGGINGS), ItemStack(IRON_BOOTS))

	val WOOD_SWORD = ItemStack(Material.WOOD_SWORD)
	val DIAMOND_SWORD = ItemStack(Material.DIAMOND_SWORD)
	val DIAMOND_SWORD_SHARPNESS = ItemStack(Material.DIAMOND_SWORD).enchant(Pair(Enchantment.DAMAGE_ALL, 1))

	val DIAMOND_AXE = ItemStack(Material.DIAMOND_AXE)

	val BACKGROUND = ItemStack(STAINED_GLASS_PANE, 1, 1.toShort(), 7.toByte()).rename(" ")

	val BUTTON_BACK = ItemStack(CARPET, 1, 1.toShort(), 14.toByte()).rename(Text.negativeOf("Voltar").toString())

	val STORE = ItemStack(Material.GOLD_INGOT).rename(Text.of("Loja").color(TextColor.GOLD).toString())
	val RULEBOOK = ItemStack(Material.WRITTEN_BOOK).rename(Text.of("Livro de Regras").color(TextColor.GOLD).toString()).apply {
		val bookMeta = itemMeta as BookMeta

		bookMeta.title = "Livro de Regras"
		bookMeta.addPage("Olá, seja bem-vindo ao Dusty!\n\nLeia atentamente as regras, todas elas são todas muito importantes!\n\n1. É proibido o uso de qualquer hack client;\n\n2. É proibido ameaçar um jogador de qualquer maneira;",
		                 "3. São proibidas quaisquer ofensas a outros jogadores e membros da staff;\n\n4. É proibido divulgar links ou outros servidores;\n",
		                 "5. É proibido abusar de bugs, sendo que, se você encontrar algum, deve reportar a um membro da staff;\n\n6. É proibido dar \'chargeback\' em qualquer produto comprado na loja (isso acarretará ban imediato).",
		                 "O descumprimento de quaisquer regras neste livro acarretará as devidas punições ao jogador, incluindo, mas não limitado a:\n\n- \'mutes\', temporários e permanentes;\n- \'bans\', temporários e permanentes;\n- \'kicks\'")
		bookMeta.author = "BigGamerBR"

		itemMeta = bookMeta
	}

	fun soups(player: Player) = Bukkit.createInventory(player, 54, SOUPS_TITLE).apply { for (i in 0 .. 53) setItem(i, SOUP) }

	fun recraft(player: Player) = Bukkit.createInventory(player, 54, RECRAFT_TITLE).apply {
		for (i in 0 .. 17) {
			setItem(i * 3, RED_MUSHROOMS)
			setItem(i * 3 + 1, BROWN_MUSHROOMS)
			setItem(i * 3 + 2, BOWLS)
		}
	}
}
