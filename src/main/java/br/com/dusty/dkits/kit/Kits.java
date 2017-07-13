package br.com.dusty.dkits.kit;

import br.com.dusty.dkits.Main;

import java.io.*;
import java.util.ArrayList;

public class Kits {
	
	public static final NoneKit NONE_KIT = new NoneKit();
	public static final PvpKit PVP_KIT = new PvpKit();
	
	public static final ArrayList<Kit> KITS = new ArrayList<>();
	
	public static void registerAll() {
		//Usage: KITS.add(FOO_KIT);
		
		KITS.add(NONE_KIT);
		KITS.add(PVP_KIT);
		
		KITS.forEach(Kits::loadData);
	}
	
	private static void loadData(Kit kit) {
		File file = new File(Main.ROOT + "/kit", kit.NAME.toLowerCase() + ".json");
		
		if(file.exists()){
			try{
				kit.DATA = Main.GSON.fromJson(new FileReader(file), Kit.Data.class);
			}catch(FileNotFoundException e){
				e.printStackTrace();
			}
		}else{
			kit.DATA = new Kit.Data();
			saveData(kit);
			return;
		}
	}
	
	private static void saveData(Kit kit) {
		File dir = new File(Main.ROOT, "kit");
		File file = new File(dir, kit.NAME.toLowerCase() + ".json");
		
		PrintWriter printWriter = null;
		
		try{
			if(!dir.exists())
				dir.mkdirs();
			
			if(!file.exists())
				file.createNewFile();
			
			printWriter = new PrintWriter(file);
			printWriter.println(Main.GSON.toJson(kit.DATA));
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			if(printWriter != null)
				printWriter.close();
		}
		
		return;
	}
}
