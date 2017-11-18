package csgodc;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

public class GUI {
	
	JFrame frame;
	JPanel panel;
	
	public final int WIDTH = 400;
	public final int HEIGHT = 600;

	public GUI(){
		
		frame = new JFrame();
		frame.setSize(WIDTH, HEIGHT);
		frame.setTitle("Death Log");
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		panel = new JPanel();
		frame.add(panel);
		
		JMenuBar jmb = new JMenuBar();
		frame.setJMenuBar(jmb);
		
		JMenu jm_mode = new JMenu("Mode");
		jmb.add(jm_mode);
		
			JMenuItem csgo = new JMenuItem("CounterStrike: Global Offensive");
			jm_mode.add(csgo);
			
			csgo.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					loadMode("csgo");
				}
			});
			
			
			JMenuItem lol = new JMenuItem("League of Legends");
			jm_mode.add(lol);
			
			lol.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					loadMode("lol");
				}
			});
			
			
			JMenuItem ow = new JMenuItem("Overwatch");
			jm_mode.add(ow);
			ow.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					loadMode("ow");
				}
			});
			
			
			JMenuItem pubg = new JMenuItem("PLAYERUNKNOWN'S BATTLEGROUNDS");
			jm_mode.add(pubg);
			
			pubg.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					loadMode("pubg");
				}
			});
			
		JMenu cfg = new JMenu("Configure");
		jmb.add(cfg);
		
		loadMode("noMode");
		
	}
	
	public void loadMode(String mode){
		
		frame.setTitle(mode + " Death Log");
		
		Color BGColour = Color.BLACK;
		
		switch(mode){
		case "noMode" : BGColour = Color.PINK;
			break;
		case "csgo": BGColour = Color.BLACK;
			break;
		case "lol": BGColour = Color.BLUE;
			break;
		case "ow": BGColour = Color.WHITE;
			break;
		case "pubg": BGColour = Color.ORANGE;
			break;
		}
		
		panel.setBackground(BGColour);
		
		ArrayList<String> theCategories = Log.getCategories();	
	}
	


}
