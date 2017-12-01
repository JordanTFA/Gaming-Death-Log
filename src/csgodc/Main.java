package csgodc;

public class Main {
	
	static String currentMode;

	public static void main(String[] args) {
			
		setCurrentMode("noMode");
		Mode.generateModes();
		GUI.buildGUI();
	}
	
	public static String getCurrentMode() {
		return currentMode;
	}

	public static void setCurrentMode(String mode) {
		Main.currentMode = mode;
	}
}
