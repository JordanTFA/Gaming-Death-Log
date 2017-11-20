package csgodc;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

public class GUI {
	
	JFrame frame;
	JPanel panel;
	JLabel icon;
	
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
					Main.setCurrentMode("csgo");
					loadMode();
				}
			});
			
			
			JMenuItem lol = new JMenuItem("League of Legends");
			jm_mode.add(lol);
			
			lol.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					Main.setCurrentMode("lol");
					loadMode();
				}
			});
			
			
			JMenuItem ow = new JMenuItem("Overwatch");
			jm_mode.add(ow);
			ow.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					Main.setCurrentMode("ow");
					loadMode();
				}
			});
			
			
			JMenuItem pubg = new JMenuItem("PLAYERUNKNOWN'S BATTLEGROUNDS");
			jm_mode.add(pubg);
			
			pubg.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					Main.setCurrentMode("pubg");
					loadMode();
				}
			});
			
		JMenu cfg = new JMenu("Configure");
		jmb.add(cfg);
		
		Main.setCurrentMode("noMode");
		loadMode();
		
	}
	
	public void loadMode(){
	
		ArrayList<Mode> theModes = Mode.getTheModes();
		
		Mode currentMode = null;
		
		for(Mode m : theModes){
			if(m.id==Main.getCurrentMode()){	
				currentMode = m;				
			}
		}
		
		Color BGColour = currentMode.BGColour;
		
		/*Image imgLogo = currentMode.img;
		ImageIcon imgIcon= new ImageIcon(imgLogo);
		icon = new JLabel(imgIcon);
		icon.setBounds(5, 5, 30, 30);
		panel.add(icon);*/
		
		frame.setTitle(currentMode.name + " Death Log");
		panel.setBackground(BGColour);
		

		
		createButtons(currentMode.log);

	}
	
	public void createButtons(HashMap<String, Double> l){

				
		for(String c : l.keySet()){
			
			System.out.println(c + " " + l.get(c));
			
			JButton b = new JButton(c);
			panel.add(b);
		}
	}

}
