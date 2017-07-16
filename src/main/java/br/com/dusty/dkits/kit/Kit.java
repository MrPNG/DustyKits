package br.com.dusty.dkits.kit;

import br.com.dusty.dkits.Main;
import br.com.dusty.dkits.gamer.EnumMode;
import br.com.dusty.dkits.gamer.Gamer;
import br.com.dusty.dkits.util.inventory.InventoryUtils;
import br.com.dusty.dkits.util.TaskUtils;
import br.com.dusty.dkits.util.gamer.GamerUtils;
import br.com.dusty.dkits.util.text.Text;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.*;

public class Kit {
	
	protected String NAME = "None";
	protected String DESCRIPTION = "";
	protected ItemStack ICON = new ItemStack(Material.STONE_SWORD);
	
	protected ItemStack WEAPON = null;
	protected ItemStack[] ARMOR = {null, null, null, null};
	protected ItemStack[] ITEMS = {};
	
	protected boolean BROADCAST = false;
	
	protected Data DATA = new Data();
	
	//TODO: protected Ability ABILITY = null;
	
	public void apply(Gamer gamer) {
		GamerUtils.clear(gamer);
		
		Player player = gamer.getPlayer();
		
		player.getInventory().setItem(0, WEAPON);
		
		InventoryUtils.setArmor(player, ARMOR);
		InventoryUtils.addItemStacks(player.getInventory(), ITEMS);
	}
	
	public void applyIfAllowed(Gamer gamer) {
		Player player = gamer.getPlayer();
		
		if(gamer.getMode() != EnumMode.ADMIN && gamer.getKit() != Kits.NONE){ //TODO: If not on MiniHG
			player.sendMessage(Text.neutralOf("Você ")
			                       .negative("já")
			                       .neutral(" está ")
			                       .negative("usando")
			                       .neutral(" um kit!")
			                       .toString());
		}else if(gamer.getMode() != EnumMode.ADMIN && !gamer.getWarp().getEnabledKits().contains(this)){
			player.sendMessage(Text.neutralOf("Você ")
			                       .negative("não pode")
			                       .neutral(" usar o kit ")
			                       .negative(NAME)
			                       .neutral(" nesta warp!")
			                       .toString());
		}else if(gamer.getMode() != EnumMode.ADMIN && !gamer.hasKit(this)){
			player.sendMessage(Text.neutralOf("Você ")
			                       .negative("não")
			                       .neutral(" tem o kit ")
			                       .negative(NAME)
			                       .neutral("!")
			                       .toString());
		}else{
			gamer.setKit(this);
			apply(gamer);
			
			player.sendMessage(Text.neutralOf("Agora você está ")
			                       .positive("usando")
			                       .neutral(" o kit ")
			                       .positive(NAME)
			                       .neutral("!")
			                       .toString());
		}
	}
	
	public boolean enabled(boolean enabled) {
		if(DATA.ENABLED == enabled)
			return false;
		
		DATA.ENABLED = enabled;
		
		TaskUtils.async(this::saveData);
		
		return true;
	}
	
	public void loadData() {
		File dir = new File(Main.ROOT, "kit");
		File file = new File(dir, NAME.toLowerCase() + ".json");
		
		if(file.exists())
			try{
				DATA = Main.GSON.fromJson(new FileReader(file), Kit.Data.class);
			}catch(FileNotFoundException e){
				e.printStackTrace();
			}
		else
			saveData();
	}
	
	public void saveData() {
		File dir = new File(Main.ROOT, "kit");
		File file = new File(dir, NAME.toLowerCase() + ".json");
		
		PrintWriter printWriter = null;
		
		try{
			dir.mkdirs();
			file.createNewFile();
			
			printWriter = new PrintWriter(file);
			printWriter.println(Main.GSON.toJson(DATA));
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			if(printWriter != null)
				printWriter.close();
		}
	}
	
	public String getName() {
		return NAME;
	}
	
	public String getDescription() {
		return DESCRIPTION;
	}
	
	public ItemStack getIcon() {
		return ICON;
	}
	
	public ItemStack getWeapon() {
		return WEAPON;
	}
	
	public ItemStack[] getArmor() {
		return ARMOR;
	}
	
	public ItemStack[] getItems() {
		return ITEMS;
	}
	
	public boolean isBroadcast() {
		return BROADCAST;
	}
	
	public Data getData() {
		return DATA;
	}
	
	public static class Data {
		
		protected int PRICE = -1;
		protected boolean ENABLED = false;
		
		public int getPrice() {
			return PRICE;
		}
		
		public void setPrice(int price) {
			this.PRICE = price;
		}
		
		public boolean isEnabled() {
			return ENABLED;
		}
	}
}
