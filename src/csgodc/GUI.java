package csgodc;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.ImageIcon;
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
	JButton button;
	
	public final int WIDTH = 400;
	public final int HEIGHT = 600;
	
	public final int LOG_WIDTH = 300;
	public final int LOG_HEIGHT = 600;

	public GUI(){
		
		frame = new JFrame();
		frame.setSize(WIDTH, HEIGHT);
		frame.setTitle("Death Log");
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		panel = new JPanel();
		frame.add(panel);
		
		JMenuBar jmb = new JMenuBar();
		frame.setJMenuBar(jmb);
		
		JMenu jm_mode = new JMenu("Mode");
		jmb.add(jm_mode);
		
		JMenu cfg = new JMenu("Configure");
		jmb.add(cfg);
		
		JMenu jm_log = new JMenu("Log");
		jmb.add(jm_log);
		JMenuItem viewlog = new JMenuItem("View Log");
		jm_log.add(viewlog);
		
		viewlog.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				createLog();
			}
		});
		
		loadMode();
		
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
	
	}
	
	public void createLog(){
		
	    Toolkit tk = Toolkit.getDefaultToolkit();
	    Dimension screenSize = tk.getScreenSize();
		
		JFrame logframe = new JFrame();
		logframe.setSize(LOG_WIDTH, LOG_HEIGHT);
		logframe.setVisible(true);
		logframe.setTitle("Log");
		logframe.setResizable(true);
		logframe.setLocation((screenSize.width / 2) + (WIDTH/2),(screenSize.height / 2) -  (HEIGHT/2)); // This needs to be configured correctly
		
		JPanel logpanel = new JPanel();
		logpanel.setBackground(Color.GRAY);
		
		logframe.add(logpanel);
	}
	
	public void loadMode(){
		
		panel.removeAll();
	
		ArrayList<Mode> theModes = Mode.getTheModes();
		
		Mode currentMode = null;
		
		for(Mode m : theModes){
			if(m.id==Main.getCurrentMode()){	
				currentMode = m;
			}
		}
		
		Color BGColour = currentMode.BGColour;
		
		Image imgLogo = currentMode.img;
		ImageIcon imgIcon= new ImageIcon(imgLogo);
		icon = new JLabel(imgIcon);
		icon.setBounds(5, 5, 300, 80);
		icon.setSize(300,80);
		panel.add(icon);
		
		frame.setTitle(currentMode.name + " Death Log");
		panel.setBackground(BGColour);
		
		frame.validate();
	
		createButtons(currentMode.log);

	}
	
	public void createButtons(HashMap<String, Double> l){
				
		for(String c : l.keySet()){
			
			// System.out.println(c + " " + l.get(c));
			
			button = new JButton(c);
			panel.add(button);
			
			button.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent evt){
					
					System.out.println("Add one to " + c);
					
					//Log.addEntry(c,l);
					l.put(c, l.get(c) + 1);
					System.out.println(l.get(c));
					
					Log.updateEntry(l);
					
				}
				
			});	
		}
		
		/*JButton undo = new JButton("Undo");
		undo.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt){
				
			}
			
		});	
		panel.add(undo);*/
		
		//TODO: Need to find last change
		
		frame.validate();
	}

}
