package csgodc;
import java.awt.Color;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

public class GUI {
	
	public final int WIDTH = 400;
	public final int HEIGHT = 600;

	public GUI(){
		
		Main.setMode("csgo");//Main.getMode();
		Color BGColour = Color.BLACK;
		
		switch(Main.getMode()){
			case "csgo": BGColour = Color.BLACK;
				break;
			case "lol": BGColour = Color.BLUE;
				break;
			case "ow": BGColour = Color.WHITE;
				break;
			case "pubg": BGColour = Color.ORANGE;
				break;
		}
		
		JFrame frame = new JFrame();
		frame.setSize(WIDTH, HEIGHT);
		frame.setTitle("Death Log");
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		JPanel panel = new JPanel();
		panel.setBackground(BGColour);
		frame.add(panel);
		
		JMenuBar jmb = new JMenuBar();
		frame.setJMenuBar(jmb);
		
		JMenu jm_mode = new JMenu("Mode");
		jmb.add(jm_mode);
		
			JMenuItem csgo = new JMenuItem("CounterStrike: Global Offensive");
			jm_mode.add(csgo);
			JMenuItem lol = new JMenuItem("League of Legends");
			jm_mode.add(lol);
			JMenuItem ow = new JMenuItem("Overwatch");
			jm_mode.add(ow);
			JMenuItem pubg = new JMenuItem("PLAYERUNKNOWN'S BATTLEGROUNDS");
			jm_mode.add(pubg);
			
		JMenu cfg = new JMenu("Configure");
		jmb.add(cfg);
		
		ArrayList<String> theCategories = Log.getCategories();	
	}
}
