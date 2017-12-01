package csgodc;

import java.awt.Color;
import java.awt.Image;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.ImageIcon;

public class Mode {
	
	public static ArrayList<Mode> theModes = new ArrayList<Mode>();

	String id;
	String name;
	Color BGColour;
	Image img;
	HashMap<String, Double> log;
	
	public Mode(String theid, String thename,  Color theBGColour, Image theimage, HashMap<String, Double> thelog){
		id = theid;
		name = thename;
		BGColour = theBGColour;
		img = theimage;
		log = thelog;	
	}
	
	public static ArrayList<Mode> generateModes(){
		
		HashMap<String,Double> h = new HashMap<String, Double>();
		h = Log.generateCategories("noMode");
		
		Mode noMode = new Mode("noMode", "", Color.PINK, new ImageIcon("src//img//img_pubg.png").getImage(), h);
		
		h = Log.generateCategories("csgo");
		Mode csgo = new Mode("csgo", "Counter-Strike: Global Offensive", Color.GRAY, new ImageIcon("src//img//img_csgo.png").getImage() , h);
		
		h = Log.generateCategories("lol");
		Mode lol = new Mode("lol", "League of Legends", Color.CYAN, new ImageIcon("src//img//img_lol.png").getImage(), h);
		
		h = Log.generateCategories("ow");
		Mode ow = new Mode("ow", "Overwatch", Color.WHITE, new ImageIcon("src//img//img_ow.png").getImage(), h);

		h = Log.generateCategories("pubg");
		Mode pubg = new Mode("pubg", "PLAYERUNKNOWN'S BATTLEGROUNDS", Color.ORANGE, new ImageIcon("src//img//img_pubg.png").getImage(),h);
		
		theModes.add(noMode);
		theModes.add(csgo);
		theModes.add(lol);
		theModes.add(ow);
		theModes.add(pubg);
		
		return theModes;
	}
	
	public static ArrayList<Mode> getTheModes() {
		return theModes;
	}

	public static void setTheModes(ArrayList<Mode> theModes) {
		Mode.theModes = theModes;
	}

}
