package br.com.dusty.dkits.warp;

import br.com.dusty.dkits.Main;
import br.com.dusty.dkits.gamer.Gamer;
import br.com.dusty.dkits.kit.Kit;
import br.com.dusty.dkits.kit.Kits;
import br.com.dusty.dkits.util.ItemStackUtils;
import br.com.dusty.dkits.util.LocationUtils;
import br.com.dusty.dkits.util.TaskUtils;
import br.com.dusty.dkits.util.text.Text;
import br.com.dusty.dkits.util.text.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.stream.Collectors;

public class Warp implements Listener {
	
	String NAME = "None";
	String DESCRIPTION = "";
	ItemStack ICON = new ItemStack(Material.STONE_SWORD);
	EnumWarpType TYPE = EnumWarpType.GAME;
	
	Location SPAWN;
	
	Kit ENTRY_KIT = Kits.NONE;
	HashSet<Kit> ENABLED_KITS = new HashSet<>();
	
	Data DATA = new Data();
	
	static Kit GAME_WARP_KIT = new GameWarpKit();
	static Kit EVENT_WARP_KIT = new EventWarpKit();
	
	public boolean enabled(boolean enabled) {
		if(DATA.ENABLED == enabled)
			return false;
		
		DATA.ENABLED = enabled;
		
		TaskUtils.async(this::saveData);
		
		return true;
	}
	
	public boolean enableKit(Kit kit, boolean enable) {
		boolean b;
		
		if(enable)
			b = ENABLED_KITS.add(kit);
		else
			b = ENABLED_KITS.remove(kit);
		
		if(DATA.LIST_ENABLED_KITS){
			DATA.KITS = ENABLED_KITS.stream()
			                        .map(Kit::getName)
			                        .collect(Collectors.toCollection(HashSet::new))
			                        .toArray(new String[0]);
		}else{
			ArrayList<Kit> disabledKits = (ArrayList<Kit>) Kits.KITS.clone();
			disabledKits.removeAll(ENABLED_KITS);
			
			DATA.KITS = disabledKits.stream()
			                        .map(Kit::getName)
			                        .collect(Collectors.toCollection(HashSet::new))
			                        .toArray(new String[0]);
		}
		
		TaskUtils.async(this::saveData);
		
		return b;
	}
	
	void loadData() {
		File dir = new File(Main.ROOT, "warp");
		File file = new File(dir, NAME.toLowerCase() + ".json");
		
		if(file.exists())
			try{
				DATA = Main.GSON.fromJson(new FileReader(file), Warp.Data.class);
			}catch(FileNotFoundException e){
				e.printStackTrace();
			}
		else
			saveData();
		
		if(DATA.LIST_ENABLED_KITS){
			for(String s : DATA.KITS){
				Kit kit = Kits.byName(s);
				
				if(kit != null && kit.getData().isEnabled())
					ENABLED_KITS.add(kit);
			}
		}else{
			for(Kit kit : Kits.KITS)
				if(kit.getData().isEnabled())
					ENABLED_KITS.add(kit);
			
			for(String s : DATA.KITS){
				Kit kit = Kits.byName(s);
				
				if(kit != null)
					ENABLED_KITS.remove(kit);
			}
		}
	}
	
	private void saveData() {
		File dir = new File(Main.ROOT, "warp");
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
	
	public EnumWarpType getType() {
		return TYPE;
	}
	
	public Location getSpawn() {
		return SPAWN == null ? SPAWN = new Location(Bukkit.getWorlds().get(0),
		                                            DATA.SPAWN[0],
		                                            DATA.SPAWN[1],
		                                            DATA.SPAWN[2]) : SPAWN;
	}
	
	public void setSpawn(Location location) {
		DATA.SPAWN[0] = (float) location.getX();
		DATA.SPAWN[1] = (float) location.getY();
		DATA.SPAWN[2] = (float) location.getZ();
	}
	
	public Kit getEntryKit() {
		return ENTRY_KIT;
	}
	
	public HashSet<Kit> getEnabledKits() {
		return ENABLED_KITS;
	}
	
	public Data getData() {
		return DATA;
	}
	
	public void receiveGamer(Gamer gamer) {
		Player player = gamer.getPlayer();
		
		player.teleport(LocationUtils.spread(getSpawn(), DATA.SPREAD_RANGE));
		player.sendMessage(Text.positivePrefix()
		                       .basic("Você foi ")
		                       .positive("teleportado")
		                       .basic(" para a warp ")
		                       .positive(NAME)
		                       .basic("!")
		                       .toString());
		
		gamer.setKit(ENTRY_KIT);
		ENTRY_KIT.apply(gamer);
	}
	
	public enum EnumWarpType {
		GAME,
		EVENT
	}
	
	public static class Data {
		
		private boolean ENABLED = false;
		
		private boolean LIST_ENABLED_KITS = true;
		private String[] KITS = {};
		
		private float[] SPAWN = {0, 0, 0};
		private float SPREAD_RANGE = 0;
		private float SPAWN_RADIUS = 0;
		
		public boolean isEnabled() {
			return ENABLED;
		}
		
		public boolean isListEnabledKits() {
			return LIST_ENABLED_KITS;
		}
		
		public void setListEnabledKits(boolean listEnabledKits) {
			this.LIST_ENABLED_KITS = listEnabledKits;
		}
		
		public String[] getKits() {
			return KITS;
		}
		
		public float[] getSpawn() {
			return SPAWN;
		}
		
		public float getSpreadRange() {
			return SPREAD_RANGE;
		}
		
		public void setSpreadRange(float spreadRange) {
			this.SPREAD_RANGE = spreadRange;
		}
		
		public float getSpawnRadius() {
			return SPAWN_RADIUS;
		}
		
		public void setSpawnRadius(float spawnRadius) {
			this.SPAWN_RADIUS = spawnRadius;
		}
	}
	
	private static class GameWarpKit extends Kit {
		
		{
			ITEMS = new ItemStack[]{ItemStackUtils.rename(new ItemStack(Material.CHEST),
			                                              Text.of("Kits").color(TextColor.GOLD).toString()),
			                        null,
			                        null,
			                        null,
			                        ItemStackUtils.rename(new ItemStack(Material.EMERALD),
			                                              Text.of("Shop").color(TextColor.GOLD).toString()),
			                        null,
			                        null,
			                        null,
			                        ItemStackUtils.rename(new ItemStack(Material.MAP),
			                                              Text.of("Warps").color(TextColor.GOLD).toString())};
		}
	}
	
	private static class EventWarpKit extends Kit {
		
		{
			ITEMS = new ItemStack[]{null,
			                        null,
			                        null,
			                        null,
			                        null,
			                        null,
			                        null,
			                        null,
			                        ItemStackUtils.rename(new ItemStack(Material.MAP),
			                                              Text.of("Warps").color(TextColor.GOLD).toString())};
		}
	}
}
