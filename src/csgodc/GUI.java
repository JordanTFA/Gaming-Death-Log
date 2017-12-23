package csgodc;
import static java.util.stream.Collectors.toMap;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
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

	// Main Window
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
		
		JMenu jm_cfg = new JMenu("Configure");
		jmb.add(jm_cfg);
		JMenuItem viewcfg = new JMenuItem("Preferences...");
		jm_cfg.add(viewcfg);
		
		JMenu jm_log = new JMenu("Log");
		jmb.add(jm_log);
		JMenuItem viewlog = new JMenuItem("View Log");
		jm_log.add(viewlog);
		
		// Create the log window
		createLog();
		
		// Make the log visible if it's selected from the menu bar
		viewlog.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				logframe.setVisible(true);
			}
		});
		
		ArrayList<Mode> theModes = Mode.getTheModes();
		
		//Set default
		setCurrentMode(theModes.get(0));
		
		// Load the current mode, this should be noMode by default
		loadMode();
		
		/*
		 * These are all menu bar items for each mode
		 * We remove the index 0 because this is the "no-mode" mode.
		 */
		
		theModes.remove(0);
		
		for(Mode m : theModes){
			JMenuItem menuitem = new JMenuItem(m.name);
			
			if(m.id!="noMode"){
				jm_mode.add(menuitem);
			}
			
			
			menuitem.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					setCurrentMode(m);
					loadMode();
				}
			});
			
		}
		
		viewcfg.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				createCfg();
			}
		});
	}
	
	// Create log content
	public static void createLog(){
		
	    Toolkit tk = Toolkit.getDefaultToolkit();
	    Dimension screenSize = tk.getScreenSize();
		
		logframe = new JFrame();
		logframe.setSize(LOG_WIDTH, LOG_HEIGHT);
		logframe.setVisible(true);
		logframe.setTitle("Log");
		logframe.setResizable(true);
		
		// Set the log window next to the main window
		logframe.setLocation((screenSize.width / 2) + (WIDTH/2),(screenSize.height / 2) -  (HEIGHT/2)); // TODO: This needs to be configured correctly
		
		JPanel logpanel = new JPanel();
		logpanel.setBackground(new Color(220,220,220));
		
		// Design the log using HTML
		log = new JLabel();
		if(getLogContent() != null){
			log.setText("<html><body>" + getLogContent() + "</body></html>");
		}

		logframe.add(logpanel);
		logpanel.add(log);
	}
	
	// Creates the configuration panel
	public static void createCfg(){
		
		JFrame cfgFrame = new JFrame("Configuration");
		cfgFrame.setSize(300, 300);
		cfgFrame.setVisible(true);
		cfgFrame.setResizable(false);
		cfgFrame.setLocationRelativeTo(null);
		
		JPanel cfgPanel = new JPanel();
		cfgFrame.add(cfgPanel);
		
		TreeMap<String,Double> cats = Log.generateCategories(currentMode.id);
		// Create a check-box for each category
		for(String s : cats.keySet()){
			JCheckBox cfgCheck = new JCheckBox(s);
			cfgPanel.add(cfgCheck);
		}
		
		// TODO: Add the ability to create a new category
		JButton addCat = new JButton("Add Category");
		cfgPanel.add(addCat);
		
		// TODO: Add the ability to remove a category
		JButton removeCat = new JButton("Remove Category");
		cfgPanel.add(removeCat);
		
		
	}
	
	// Update the log whenever it receives a new entry
	public static void updateLog(String cat, Double change){
		
		// TODO: Make the log scroll instead of going off-screen
		
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
		
		// Create new line
		setLogContent("<p>" + cat + " " + symbol + " " + fontColour + Math.abs(change.intValue()) + "</font></p>" + getLogContent());
		
		log.setText("<html><body>" + getLogContent() + "</body></html");
		
	}
	
	// Load a new mode onto the main window
	public static void loadMode(){
		
		//Clear the old mode screen
		panel.removeAll();
		
		Mode currentMode = getCurrentMode();
		
		// Update log with a mode change - the if statement is to prevent "noMode" being picked up. Probably
		// A better way to deal with this
		// TODO: Fix maybe?
		// TODO: Change the font colour to a variable rather than hard-code blue. Easy fix
		
		if(currentMode.name.length() > 0){
			
			String colour = "<font color=\'blue\'>";
			setLogContent("<p>" + colour + "Changed mode to " + currentMode.name + "</p>" + getLogContent());
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
	
	public static void createButtons(TreeMap<String, Double> l){
				
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
					Double change = getLastChange() * -1;
					
					for(String c: l.keySet()){
						if(c==cat){
							l.put(c, l.get(c) + change);
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
				
				Map<String, Double> m = sortMap(l);
				
				for(Entry<String, Double> entry : m.entrySet()){
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
	
	public static Map<String, Double> sortMap(TreeMap<String, Double> old){

		// I don't really know what this does but it works
		
		Map<String, Double> newMap = old.entrySet()
		        .stream()
		        .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
		        .collect(
		            toMap(e -> e.getKey(), e -> e.getValue(), (e1, e2) -> e2,
		                LinkedHashMap::new));
		

		return newMap;
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
