package csgodc;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;

public class Mode {
	
	public static ArrayList<Mode> theModes = new ArrayList<Mode>();
	private String id;
	private String name;
	private Color BGColour;
	private HashMap<String, Double> log;
	
	public Mode(String theid, String thename,  Color theBGColour, HashMap<String, Double> thelog){
		id = theid;
		name = thename;
		BGColour = theBGColour;
		// log = thelog;
	
	}
	
	public static void generateModes(){
		
		//Mode noMode = new Mode("dc", "Death Counter", Color.BLACK, "");
		
		HashMap<String,Double> h = new HashMap<String, Double>();
		h.put("Outaimed",0.0);
		Mode csgo = new Mode("csgo", "Counter-Strike: Global Offensive", Color.BLACK, h /*"Outaimed,0\n"
					+ "Crept up upon,0\n"
					+ "Bad positioning,0\n"
					+ "Overwhelmed,0\n"
					+ "Got Traded,0\n"
					+ "I Traded,0\n"
					+ "Flashed,0\n"
					+ "Peaked an AWP,0\n"
					+ "Tried to cheese,0\n"*/);
		
		//Mode lol = new Mode("lol", "League of Legends", Color.BLUE, "Overextended,0\n");
		
		//Mode ow = new Mode("ow", "Overwatch", Color.WHITE, "Not with Team,0\n");
		
		//Mode pubg = new Mode("pubg", "PLAYERUNKNOWN'S BATTLEGROUNDS", Color.ORANGE, "Unaware of Enemy,0\n");
		
	}

}
