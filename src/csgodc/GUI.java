package csgodc;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class GUI {
	
	static JFrame frame;
	static JPanel panel;
	static JLabel icon;
	static JButton button;
	
	static JFrame logframe;
	static String logContent = "";
	static JLabel log;
	
	public final static int WIDTH = 400;
	public final static int HEIGHT = 600;
	
	public final static int LOG_WIDTH = 300;
	public final static int LOG_HEIGHT = 600;
	
	static String lastCat = null;
	static Double lastChange = null;
	
	static Mode currentMode;


	public static void buildGUI(){
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
		
		createLog();
		
		viewlog.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				logframe.setVisible(true);
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
	
	public static void createLog(){
		
	    Toolkit tk = Toolkit.getDefaultToolkit();
	    Dimension screenSize = tk.getScreenSize();
		
		logframe = new JFrame();
		logframe.setSize(LOG_WIDTH, LOG_HEIGHT);
		logframe.setVisible(false);
		logframe.setTitle("Log");
		logframe.setResizable(true);
		logframe.setLocation((screenSize.width / 2) + (WIDTH/2),(screenSize.height / 2) -  (HEIGHT/2)); // This needs to be configured correctly
		
		JPanel logpanel = new JPanel();
		logpanel.setBackground(new Color(220,220,220));
		
		log = new JLabel();
		if(getLogContent() != null){
			log.setText("<html><body>" + getLogContent() + "</body></html>");
		}

		logframe.add(logpanel);
		logpanel.add(log);
	}
	
	public static void updateLog(String cat, Double change){
		
		// TODO: Make the log scroll instead of going offscreen
		
		setLastCat(cat);
		setLastChange(change);
		
		String fontColour;
		char symbol;
		
		if(change<0){
			fontColour = "<font color=\"red\">";
			symbol = '-';
		}else{
			fontColour = "<font color=\"green\">";
			symbol = '+';
		}
		
		setLogContent("<p>" + cat + " " + symbol + " " + fontColour + Math.abs(change.intValue()) + "</font></p>" + getLogContent());
		
		log.setText("<html><body>" + getLogContent() + "</body></html");
		
		
	}
	
	public static void loadMode(){
		
		panel.removeAll();
	
		ArrayList<Mode> theModes = Mode.getTheModes();
		
		for(Mode m : theModes){
			if(m.id==Main.getCurrentMode()){	
				setCurrentMode(m);
			}
		}
		
		Mode currentMode = getCurrentMode();
		
		if(currentMode.name.length() > 0){
			setLogContent("<p>" + "<font color=\"blue\">Changed mode to " + currentMode.name + "</p>" + getLogContent());
			log.setText("<html><body>" + getLogContent() + "</body></html");
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
	
	public static void createButtons(HashMap<String, Double> l){
				
		for(String c : l.keySet()){
			
			button = new JButton(c);
			panel.add(button);
			
			button.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent evt){
					
					System.out.println("Add one to " + c);
					
					l.put(c, l.get(c) + 1);
					System.out.println(l.get(c));
					
					Log.updateFile(l);
					
					updateLog(c, 1.0);
				}
				
			});	
		}
		
		JButton undo = new JButton("Undo");
		undo.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt){
					
				if(getLastCat() != null && getLastChange() != null){
					
					String cat = getLastCat();
					Double change = (getLastChange()) * -1;
					
					for(String c: l.keySet()){
						if(c==cat){
							l.put(c,(l.get(c) + (change)));
						}
					}
					
					updateLog(cat,change);
					
					Log.updateFile(l);
				}
				else{
					JOptionPane.showMessageDialog(null, "Nothing to Undo!");
				}			
			}
			
		});	
		panel.add(undo);
		
		JButton reset = new JButton("Reset Stats");
		reset.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt){
				
				int dialogButton = JOptionPane.YES_NO_OPTION;
				
				int dialogResult = JOptionPane.showConfirmDialog (null, "This will reset all of your stats! Are you sure you want to reset?", "Warning", dialogButton);
				if(dialogResult == JOptionPane.YES_OPTION){
					
					for(String c : l.keySet()){

						l.put(c, 0.0);
					}
				Log.updateFile(l);
				
				setLogContent("<p>" + "<font color=\"purple\">Stats reset for " + currentMode.name + "</font></p>" + getLogContent());
				log.setText("<html><body>" + getLogContent() + "</body></html");
			}
		}});
		
		panel.add(reset);

		
		JButton stats = new JButton("Show Stats");
		stats.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt){
				
				Mode currentMode = getCurrentMode();
				
				JFrame showStats = new JFrame(currentMode.name + " Statistics");
				showStats.setSize(300, 300);
				showStats.setLocationRelativeTo(null);
				showStats.setVisible(true);
				
				JPanel statsPanel = new JPanel();			
				showStats.add(statsPanel);
				
				JLabel theStats = new JLabel("<html><body>");
				statsPanel.add(theStats);
				
				// TODO: Sort numerically
				for(Entry<String,Double> entry : l.entrySet()){
					String key = entry.getKey();
			        Double value = entry.getValue();
			        
			        theStats.setText(theStats.getText() + "<p>" + key + ":     \t" + value.intValue() + "</p>");
				}
				
				theStats.setText(theStats.getText() + "</html>");
				
				
			}
		});
		
		panel.add(stats);
		
		frame.validate();
	}
	
	public static String getLastCat() {
		return lastCat;
	}

	public static void setLastCat(String lastCat) {
		GUI.lastCat = lastCat;
	}

	public static Double getLastChange() {
		return lastChange;
	}

	public static void setLastChange(Double lastChange) {
		GUI.lastChange = lastChange;
	}
	
	public static String getLogContent() {
		return logContent;
	}

	public static void setLogContent(String logContent) {
		GUI.logContent = logContent;
	}
	
	public static Mode getCurrentMode() {
		return currentMode;
	}

	public static void setCurrentMode(Mode currentMode) {
		GUI.currentMode = currentMode;
	}

}
