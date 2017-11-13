package csgodc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Log {
	
	public static String path;
	public static FileReader fr;
	public static BufferedReader br;
	public static String defaultLog;

	public static ArrayList<String> getCategories(){
			
		path = "src/csgodc/log/" + Main.getMode() + ".txt";
		ArrayList<String> theCategories = new ArrayList<String>();
		String line = null;
		
		try {

			fr = new FileReader(path);
			br = new BufferedReader(fr);
			
            while((line = br.readLine()) != null) {
                System.out.println(line);
            }   

            // Always close files.
            br.close();
			
			
		} catch (FileNotFoundException e) {
			System.out.println("Unable to open file '" + path + "'. Creating new file...");
			createFile();

		} catch(IOException ex){
			ex.printStackTrace();
		}
		
		return null;
	}
	
	public static void createFile(){
		
		String mode = Main.getMode();
		path = "src/csgodc/log/" + mode + ".txt";
		
		switch(mode){
			case "csgo": setDefaultLog("csgo");
				break;
			case "lol": setDefaultLog("lol");
				break;
			case "ow": setDefaultLog("OW");
				break;
				
		}
			
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
	
	public static String getDefaultLog() {
		return defaultLog;
	}

	public static void setDefaultLog(String defaultLog) {
		Log.defaultLog = defaultLog;
	}
}
