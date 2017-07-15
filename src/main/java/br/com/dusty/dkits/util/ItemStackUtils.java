package br.com.dusty.dkits.util;

import br.com.dusty.dkits.util.text.Text;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.material.Dye;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

/**
 * Criação/personalização de {@link org.bukkit.inventory.ItemStack}
 */
public class ItemStackUtils {
	
	public static ItemStack rename(ItemStack itemStack, String name) {
		if(itemStack != null){
			ItemMeta itemMeta = itemStack.getItemMeta();
			if(itemMeta != null)
				itemMeta.setDisplayName(name);
			
			itemStack.setItemMeta(itemMeta);
		}
		
		return itemStack;
	}
	
	public static ItemStack enchant(ItemStack itemStack, int level, Enchantment... enchantments) {
		if(itemStack != null){
			for(Enchantment enchantment : enchantments)
				itemStack.addUnsafeEnchantment(enchantment, level);
		}
		
		return itemStack;
	}
	
	public static ItemStack color(ItemStack itemStack, Color color) {
		if(itemStack != null){
			if(itemStack.getType().name().startsWith("LEATHER_")){
				LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) itemStack.getItemMeta();
				leatherArmorMeta.setColor(color);
				
				itemStack.setItemMeta(leatherArmorMeta);
			}
		}
		
		return itemStack;
	}
	
	public static ItemStack dye(DyeColor c) {
		Dye dye = new Dye();
		dye.setColor(c);
		
		return dye.toItemStack(1);
	}
	
	public static ItemStack potions(int amount, boolean extended, boolean upgraded, PotionType potionType, boolean splash) {
		ItemStack itemStack = new ItemStack(Material.POTION, amount);
		
		PotionData potionData = new PotionData(potionType, extended, upgraded);
		
		PotionMeta potionMeta = (PotionMeta) itemStack.getItemMeta();
		potionMeta.setBasePotionData(potionData);
		
		itemStack.setItemMeta(potionMeta);
		
		return itemStack;
	}
	
	public static ItemStack setDescription(ItemStack itemStack, String description) {
		ItemMeta itemMeta = itemStack.getItemMeta();
		itemMeta.setLore(StringUtils.fancySplit(description, 32));
		
		itemStack.setItemMeta(itemMeta);
		
		return itemStack;
	}
	
	/**
	 * Retorna o <b>'displayName'</b> não-formatado de uma {@link ItemStack}, se houver, ou o <b>name()</b> de seu {@link org.bukkit.Material}, caso contrário.
	 *
	 * @param itemStack
	 * @return <b>'displayName'</b> não-formatado de uma {@link ItemStack}, se houver, ou o <b>name()</b> de seu {@link org.bukkit.Material}, caso contrário.
	 * Pode, ainda, retornar <b>null</b> se a {@link ItemStack} for 'null'.
	 */
	public static String getUnformattedDisplayName(ItemStack itemStack) {
		String displayName = null;
		
		if(itemStack != null){
			ItemMeta itemMeta = itemStack.getItemMeta();
			if(itemMeta != null && itemMeta.hasDisplayName())
				displayName = itemMeta.getDisplayName();
			else
				displayName = itemStack.getType().name();
		}
		
		return Text.clearFormatting(displayName);
	}
}
