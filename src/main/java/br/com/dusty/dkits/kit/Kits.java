package br.com.dusty.dkits.kit;

import java.util.ArrayList;

public class Kits {
	
	public static final NoneKit NONE = new NoneKit();
	public static final PvpKit PVP = new PvpKit();
	
	public static final ArrayList<Kit> KITS = new ArrayList<>();
	
	public static void registerAll() {
		//Usage: WARPS.add(FOO_KIT);
		
		KITS.add(NONE);
		KITS.add(PVP);
	}
	
	public static Kit byName(String name) {
		for(Kit kit : KITS)
			if(kit.NAME.toLowerCase().startsWith(name.toLowerCase()))
				return kit;
		
		return null;
	}
}
