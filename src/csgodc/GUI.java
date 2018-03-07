package csgodc;
import static java.util.stream.Collectors.toMap;

import java.awt.Color;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class GUI{
	
	static JFrame frame;
	static JPanel panel;
	static JLabel icon;
	static JButton button;
	
	public final static int WIDTH = 400;
	public final static int HEIGHT = 600;
	
	static String lastCat = null;
	static Double lastChange = null;
	
	static Mode currentMode;
	
	static TreeMap<String,Double> allCategories;

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
		panel.setBounds(0, 0, WIDTH, HEIGHT-100);
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
		Log.createLog();
		
		// Make the log visible if it's selected from the menu bar
		viewlog.addActionListener(e -> Log.makeLogVisible());
	
		ArrayList<Mode> theModes = Mode.getTheModes();
		
		// Load the Default Page
		createDefaultPage();
		
		for(Mode m : theModes){
			JMenuItem menuitem = new JMenuItem(m.name);
			
			jm_mode.add(menuitem);

			menuitem.addActionListener(e ->{
					setCurrentMode(m);
					loadMode();
			});
			
		}
		viewcfg.addActionListener(e -> {
			
			Config.setCurrentMode(currentMode);
			Config.createCfg();
			
		});

	}
	
	// Default Page, can perhaps add to this
	public static void createDefaultPage(){
		
		panel.removeAll();
		
		Color BGColour = Color.PINK;
		
		Image imgLogo = new ImageIcon("src//img//img_nomode.png").getImage();
		ImageIcon imgIcon= new ImageIcon(imgLogo);
		icon = new JLabel(imgIcon);
		icon.setBounds(5, 5, 300, 80);
		icon.setSize(300,80);
		panel.add(icon);
		
		// TODO: This. Fill out the text and make it centred
		JTextPane msg = new JTextPane();
		panel.add(msg);
		msg.setContentType("text/html");
		msg.setEditable(false);
		msg.setHighlighter(null);
		msg.setBackground(panel.getBackground());
		msg.setSize(WIDTH, HEIGHT);
		
		msg = Log.applyCSS(msg,"body {line-height: 50px; font-family: Dialog; font-size:12; font-weight: bold; text-align: center;}");
		
		StyledDocument doc = msg.getStyledDocument();
		SimpleAttributeSet center = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		doc.setParagraphAttributes(0, doc.getLength(), center, false);
	    
	    msg.setText("<html><body>"
	    		+ "<p>Welcome to the death log. You can use this app to track</p>"
	    		+ "<p>your deaths across multiple games, so you can learn to die less.</p>"
	    		+ "<p></p>"
	    		+ "<p>You moron.</p>"
	    		+ "</body></html>");
	    
	    createDefaultButtons();
		
		frame.setTitle("Gaming Death Log");
		panel.setBackground(BGColour);
		msg.setBackground(panel.getBackground());
	
		frame.validate();
		
	}
	
	public static void createDefaultButtons(){
		
		ImageIcon img;
		JButton btn;
		
		img = new ImageIcon("src//img//links//img_git.png");
		img = resizeImg(img);
		btn = new JButton(img);
		
		btn.addActionListener(	e-> openWebPage("https://github.com/JordanTFA/Gaming-Death-Log")	);
		panel.add(btn);
		
		img = new ImageIcon("src//img//links//img_twitter.png");
		img = resizeImg(img);
		btn = new JButton(img);
		
		btn.addActionListener(	e-> openWebPage("https://twitter.com/")	);
		panel.add(btn);
		
		img = new ImageIcon("src//img//links//img_discord.png");
		img = resizeImg(img);
		btn = new JButton(img);
				
		btn.addActionListener(	e-> openWebPage("https://discord.gg/q6fUT")	);
		panel.add(btn);
	}
	
	public static ImageIcon resizeImg(ImageIcon img){
		
		Image image = img.getImage(); // transform it 
		Image newImg = image.getScaledInstance(40, 40,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
		img = new ImageIcon(newImg);  // transform it back
		
		return img;
		
	}
	
	public static void createBackground(){
		
		//Clear the old mode screen
		panel.removeAll();
		
		Mode currentMode = getCurrentMode();
		
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
			panel.validate();
		
	}
	
	// Load a new mode onto the main window
	public static void loadMode(){
		
		//Clear the old mode screen
		createBackground();
		
		allCategories = currentMode.log;
		
		if(currentMode.name.length() > 0){
			
			String colour = "<font color=\'blue\'>";
			Log.setLogContent("<p>" + colour + "Changed mode to " + currentMode.name + "</font></p>" + Log.getLogContent());
			Log.addToLog();
			
			createButtons();
		}
	}
	
	public static void createButtons(){
			
		for(String c : getAllCategories().keySet()){
			
			button = new JButton(c);
			panel.add(button);
			
			button.addActionListener(e ->{
					
				System.out.println("Add one to " + c);
					
				allCategories.put(c, allCategories.get(c) + 1);
				System.out.println(allCategories.get(c));
					
				FileSystem.updateFile(allCategories);
					
				Log.updateLog(c, 1.0);			
			});	
		}

		JPanel utilButtons = new JPanel();
		utilButtons.setBounds(0, HEIGHT-200, WIDTH, HEIGHT + 200);
		utilButtons.setLayout(null);
		frame.add(utilButtons);
		
		JButton undo = new JButton("Undo");
		undo.addActionListener(e -> {
			if(Log.getLastCat() != null && Log.getLastChange() != null){
				
				String cat = Log.getLastCat();
				Double change = Log.getLastChange() * -1;
				
				for(String c: allCategories.keySet()){
					if(c==cat){
						allCategories.put(c, allCategories.get(c) + change);
					}
				}
				
				Log.updateLog(cat,change);
				
				FileSystem.updateFile(allCategories);
			}
			else{
				JOptionPane.showMessageDialog(null, "Nothing to Undo!");
			}			
		});

		undo.setBounds(40,HEIGHT-100, 80, 30);
		utilButtons.add(undo);
		
		
		JButton reset = new JButton("Reset Stats");
		reset.addActionListener(e ->{
				
			int dialogButton = JOptionPane.YES_NO_OPTION;
				
			int dialogResult = JOptionPane.showConfirmDialog (null, "This will reset all of your stats! Are you sure you want to reset?", "Warning", dialogButton);
			if(dialogResult == JOptionPane.YES_OPTION){
					
				for(String c : allCategories.keySet()){

					allCategories.put(c, 0.0);
				}
			FileSystem.updateFile(allCategories);
				
			String fontColour = "<font color='purple'>";
			Log.setLogContent("<p>" + fontColour + "Stats reset for " + currentMode.name + "</font></p>" + Log.getLogContent());
			Log.addToLog();
			
			}
		});

		reset.setBounds(130,HEIGHT-100, 100, 30);
		utilButtons.add(reset);

		JButton stats = new JButton("Show Stats");
		stats.addActionListener(e ->{
				
				Mode currentMode = getCurrentMode();
				
				JFrame showStats = new JFrame(currentMode.name + " Statistics");
				showStats.setSize(300, 300);
				showStats.setLocationRelativeTo(null);
				showStats.setVisible(true);
				
				JPanel statsPanel = new JPanel();			
				showStats.add(statsPanel);
				
				JLabel theStats = new JLabel("<html><body>");
				theStats.setSize(300, 300);
				statsPanel.add(theStats);
				
				Map<String, Double> m = sortMap(allCategories);
				
				for(Entry<String, Double> entry : m.entrySet()){
					String key = entry.getKey();
			        Double value = entry.getValue();
			        
			        theStats.setText(theStats.getText() + "<p>" + key + ":     \t" + value.intValue() + "</p>");
				}
				
				theStats.setText(theStats.getText() + "</html>");
				
				JTextArea msg = new JTextArea();
				statsPanel.add(msg);
				msg.setEditable(false);
				msg.setHighlighter(null);
				msg.setLineWrap(true); 

				msg.setWrapStyleWord(true); 
				
				ArrayList<String> mostCommonDeaths = new ArrayList<String>();
				
				mostCommonDeaths = findMostCommonDeath(m);
				
				// We remove the first one, so the grammars makes sense.
				String deaths = mostCommonDeaths.get(0);
				mostCommonDeaths.remove(0);
				
				// Append deaths to the current list
				for(String s : mostCommonDeaths){
					deaths += ", " + s ;
				}
				
				// Stick a full stop at the end of the message.
				deaths += ".";
				
				msg.setText("Your most common cause(s) of death:\n" + deaths);
				msg.setBackground(statsPanel.getBackground()); // Panel colour
				msg.setSize(280,100);
	
		});

		stats.setBounds(240,HEIGHT-100, 120, 30);
		utilButtons.add(stats);
		
		//utilButtons.validate();
		frame.validate();
	}
	
	public static ArrayList<String> findMostCommonDeath(Map<String,Double> theMap){

		double highest = 0;
		
		// Find the highest value
		for(Entry<String, Double> entry : theMap.entrySet()){
			if(highest < entry.getValue()){
				highest = entry.getValue();
			}
			
		}
		
		
		ArrayList<String> mostCommonDeaths = new ArrayList<String>();
		
		// If any other keys have the same value, add them as well
		for(Entry<String,Double> entry : theMap.entrySet()){
			if(entry.getValue() == highest){
				mostCommonDeaths.add(entry.getKey());
			}	
		}
		
		return mostCommonDeaths;
	}
	
	public static void openWebPage(String url){
		   try {         
		     java.awt.Desktop.getDesktop().browse(java.net.URI.create(url));
		   }
		   catch (java.io.IOException e) {
		       System.out.println(e.getMessage());
		   }
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
	
	public static Mode getCurrentMode() {
		return currentMode;
	}

	public static void setCurrentMode(Mode currentMode) {
		GUI.currentMode = currentMode;
	}
	
	public static TreeMap<String, Double> getAllCategories() {
		return allCategories;
	}

	public static void setAllCategories(TreeMap<String, Double> allCategories) {
		GUI.allCategories = allCategories;
	}

}
