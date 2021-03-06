package csgodc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.TreeMap;

public class FileSystem {
	
	public static String path;
	public static FileReader fr;
	public static BufferedReader br;
	public static String defaultLog;

	public static TreeMap<String, Double> generateCategories(String modeid){
			
		path = "src/csgodc/log/" + modeid + ".txt";
		TreeMap<String, Double> theCategories = new TreeMap<String, Double>();
		String line = null;
		
		try {

			fr = new FileReader(path);
			br = new BufferedReader(fr);
			
            while((line = br.readLine()) != null) {
            	String array[] = line.split(",");
            	theCategories.put(array[0],Double.parseDouble(array[1]));
            }   

            // Always close files.
            br.close();
			
			
		} catch (FileNotFoundException e) {
			System.out.println("Unable to open file '" + path + "'. Creating new file...");
			createFile(modeid);
			generateCategories(modeid);

		} catch(IOException ex){
			ex.printStackTrace();
		}
		
		return theCategories;
	}
	
	public static void createFile(String mode){

		path = "src/csgodc/log/" + mode + ".txt";
		
		fillDefault(mode);
			
		try{
			
			File file = new File(path);
			file.createNewFile();
			
			if (!file.exists()) {
				
	            file.createNewFile();
	        } else {
	        	
	            FileOutputStream writer = new FileOutputStream(path);
	            writer.write((getDefaultLog()).getBytes());
	            writer.close();
	        }
		 }catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	public static void fillDefault(String mode){
		path = "src/csgodc/log/" + mode + ".txt";
		
		switch(mode){
			case "csgo": setDefaultLog("Bad positioning,0\n"
					+ "Crept up Upon,0\n"
					+ "Flashed,0\n"
					+ "Got Traded,0\n"
					+ "I Traded,0\n"
					+ "Outaimed,0\n"
					+ "Overwhelmed,0\n"	
					+ "Peaked an AWP,0\n"
					+ "Tried to Cheese,0");
				break;
			case "lol": setDefaultLog("Crept up upon,0\n"
					+ "Facechecked Bush,0\n"
					+ "Ganked (Blame jg),0\n"
					+ "Got Baited,0\n"
					+ "Held Summoner Spells,0\n"
					+ "Invaded,0\n"
					+ "Overextended,0\n"	
					+ "Took Bad Fight,0\n" 
					+ "Team Fight (Favourable),0\n"
					+ "Team Fight (Unfavourable),0"
					);

				break;
			case "ow": setDefaultLog("Backed into Wall,0\n"
					+ "Crept up Upon,0\n"
					+ "Didn't notice sniper,0\n"
					+ "Dived on,0\n"
					+ "I dived (Favourable),0\n"
					+ "I dived (Unfavourable),0\n"
					+ "Picked off/Sniped,0\n"
					+ "Separated from team,0\n"
					+ "Team Fight (Favourable),0\n"
					+ "Team Fight (Unfavourable),0\n"
					+ "Dived on,0"
					);
				break;
			case "pubg": setDefaultLog("Car Accident,0\n"
					+ "Couldn't find vehicle,0\n"
					+ "Enemy had better weapon,0\n"
					+ "Glitch,0\n"
					+ "Lost track of time,0\n"
					+ "Poor Engagement,0\n"
					+ "Red Zone,0\n"
					+ "Took a bad fight,0\n"
					+ "Unaware of enemy,0\n"
					+ "Unlucky Circles,0"
					);
				break;		
		}
	}
	
	public static void updateFile(TreeMap<String, Double> l){
		
		path = "src/csgodc/log/" + GUI.getCurrentMode().id + ".txt";
		
		try {

			FileOutputStream fow = new FileOutputStream(path);
			
			for(String s : l.keySet()){
				
				fow.write((s + "," + l.get(s) + "\n").getBytes());	
			}
			
			fow.close();
			
		} catch (IOException e) {
			System.out.println("Couldn't find file...");

			e.printStackTrace();
		}
	}
	
	public static String getDefaultLog() {
		return defaultLog;
	}

	public static void setDefaultLog(String defaultLog) {
		FileSystem.defaultLog = defaultLog;
	}
}
