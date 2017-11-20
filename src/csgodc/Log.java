package csgodc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class Log {
	
	public static String path;
	public static FileReader fr;
	public static BufferedReader br;
	public static String defaultLog;

	public static HashMap<String, Double> generateCategories(String modeid){
			
		path = "src/csgodc/log/" + modeid + ".txt";
		HashMap<String, Double> theCategories = new HashMap<String, Double>();
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
			createFile();
			generateCategories(modeid);

		} catch(IOException ex){
			ex.printStackTrace();
		}
		
		return theCategories;
	}
	
	public static void createFile(){
		
		String mode = Main.getCurrentMode();
		path = "src/csgodc/log/" + mode + ".txt";
		
		fillDefault();
			
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
	
	public static void fillDefault(){
		
		String mode = Main.getCurrentMode();
		path = "src/csgodc/log/" + mode + ".txt";
		
		switch(mode){
			case "csgo": setDefaultLog("Outaimed,0\n"
					+ "Crept up upon,0\n"
					+ "Bad positioning,0\n"
					+ "Overwhelmed,0\n"
					+ "Got Traded,0\n"
					+ "I Traded,0\n"
					+ "Flashed,0\n"
					+ "Peaked an AWP,0\n"
					+ "Tried to cheese,0\n");
				break;
			case "lol": setDefaultLog("lol");
				break;
			case "ow": setDefaultLog("ow");
				break;
			case "pubg": setDefaultLog("pubg");
				break;		
		}
		
	}
	
	public static String getDefaultLog() {
		return defaultLog;
	}

	public static void setDefaultLog(String defaultLog) {
		Log.defaultLog = defaultLog;
	}
}
