package csgodc;

public class Main {
	
	static String mode;

	public static void main(String[] args) {
		
		setMode("csgo");
		GUI gui = new GUI();

	}
	
	public static String getMode() {
		return mode;
	}

	public static void setMode(String mode) {
		Main.mode = mode;
	}

}
