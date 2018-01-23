package csgodc;
import static java.util.stream.Collectors.toMap;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

public class GUI{
	
	static JFrame frame;
	static JPanel panel;
	static JLabel icon;
	static JButton button;
	
	static JFrame logframe;
	static String logContent = "";
	
	static JTextPane log;
	StyleSheet sh;
	
	public final static int WIDTH = 400;
	public final static int HEIGHT = 600;
	
	public final static int LOG_WIDTH = 300;
	public final static int LOG_HEIGHT = 600;
	
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
		
		// Load the Default Page
		createDefaultPage();
		
		for(Mode m : theModes){
			JMenuItem menuitem = new JMenuItem(m.name);
			
			jm_mode.add(menuitem);

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
		logframe.setLocation((screenSize.width / 2) + (WIDTH/2),(screenSize.height / 2) -  (HEIGHT/2) - 20); 
		
		// Align text left
		JPanel logpanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
		logpanel.setBackground(new Color(220,220,220));
		
		log = new JTextPane();
		
		log.setContentType("text/html");
		log.setBackground(new Color(220,220,220));
		log.setEditable(false);
		log.setHighlighter(null);
		
		StyleSheet styleSheet = new StyleSheet();
		HTMLDocument htmlDocument;
		HTMLEditorKit htmlEditorKit = new HTMLEditorKit();
		
		// All of this creates stylesheet (CSS) rules to apply to the JTextPane
		styleSheet.addRule("body {line-height: 50px;}");										// Line spacing of 50px
		styleSheet.addRule("body {font-family: Dialog; font-size:12; font-weight: bold}");		// Font: Dialog, size: 12px, bold
		htmlEditorKit.setStyleSheet(styleSheet);
	    htmlDocument = (HTMLDocument) htmlEditorKit.createDefaultDocument();
	    log.setEditorKit(htmlEditorKit);
	    log.setDocument(htmlDocument);
		
	    // Make the text wrap; I don't think this is required
	    
		//JPanel noWrapPanel = new JPanel( new BorderLayout() );
		//noWrapPanel.add( log );
		
	    // Currently having an issue with JScrollPanes, they don't seem to want to work
		JScrollPane scrollpane = new JScrollPane(log,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		logframe.add(logpanel);
		logframe.add(log);
	}
	
	// Creates the configuration panel
	public static void createCfg(){
		
		if(getCurrentMode() != null){
		
			JFrame cfgFrame = new JFrame("Configuration");
			cfgFrame.setSize(300, 300);
			cfgFrame.setVisible(true);
			cfgFrame.setResizable(false);
			cfgFrame.setLocationRelativeTo(null);
			
			JPanel cfgPanel = new JPanel();
			cfgPanel.setBackground(new Color(230,230,250));
			cfgFrame.add(cfgPanel);
			
			
			JLabel lblCategory = new JLabel(getCurrentMode().name);
			cfgPanel.add(lblCategory);
		
			JTextField catToAdd = new JTextField("Click Here to Add a Category");
			catToAdd.setSize(3000, 5);
			catToAdd.setBounds(0,0,3000, 5);
			cfgPanel.add(catToAdd);
			catToAdd.addMouseListener(new MouseAdapter(){
				@Override
				public void mouseClicked(MouseEvent e){
					catToAdd.setText("");
				}
			});
        
			TreeMap<String,Double> cats = Log.generateCategories(currentMode.id);
			
			JButton addCat = new JButton("Add");
			cfgPanel.add(addCat);
		
			addCat.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent evt){
				
					cats.put(catToAdd.getText(), 0.0);
					Log.updateFile(cats);
				
					allCategories = cats;
				
					cfgFrame.dispose();
					createCfg();
					
					String colour = "<font color=\'green\'>";
					setLogContent("<p>" + colour + "Added " + catToAdd.getText() + "</font></p>" + getLogContent());
					log.setText("<html><body>" + getLogContent() + "</body></html>");

				
					createBackground();
					createButtons();

				}
			
			});	
		
			JComboBox<String> jcmb = new JComboBox<String>();
		
			JLabel lblCats = new JLabel();
			cfgPanel.add(lblCats);
			// Create a check-box for each category
			for(String s : cats.keySet()){
				lblCats.setText( lblCats.getText()  + "<p>" + s + "</p>");
			
				jcmb.addItem(s);
			}
			
			lblCats.setText("<html><body>" + lblCats.getText() + "</body></html>");
			
			JButton removeCat = new JButton("Remove Category");
			removeCat.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent evt){
					
					cats.remove(jcmb.getSelectedItem());
					Log.updateFile(cats);
					
					allCategories = cats;
					
					cfgFrame.dispose();
					createCfg();
				
					String colour = "<font color=\'red\'>";
					setLogContent("<p>" + colour + "Removed " + jcmb.getSelectedItem()  + "</p>" + getLogContent());
					log.setText("<html><body>" + getLogContent() + "</body></html");
					
					createBackground();
					createButtons();
				}
			
			});	
		
			cfgPanel.add(jcmb);
			cfgPanel.add(removeCat);
			
		}else{
			JOptionPane.showMessageDialog(null, "Select a Category to Configure!");
		}
		
		
	}
	
	// Update the log whenever it receives a new entry
	public static void updateLog(String cat, Double change){
		
		// TODO: Make the log scroll instead of going off-screen
		
		setLastCat(cat);
		setLastChange(change);
		
		String fontColour;
		char symbol;
		
		if(change<0){
			fontColour = "<font color='red'>";
			symbol = '-';
		}else{
			fontColour = "<font color='green'>";
			symbol = '+';
		}
				
		// Create new line
		setLogContent("<p>" + cat + " " + symbol + " " + fontColour + Math.abs(change.intValue()) + "</font></p>" + getLogContent());
		
		log.setText("<html><body>" + getLogContent() + "</body></html");
		
	}
	
	public static void createDefaultPage(){
		
		panel.removeAll();
		
		Color BGColour = Color.PINK;
		
		Image imgLogo = new ImageIcon("src//img//img_nomode.png").getImage();
		ImageIcon imgIcon= new ImageIcon(imgLogo);
		icon = new JLabel(imgIcon);
		icon.setBounds(5, 5, 300, 80);
		icon.setSize(300,80);
		panel.add(icon);
	
		frame.setTitle("Gaming Death Log");
		panel.setBackground(BGColour);
	
		frame.validate();
		
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
		
	}
	
	// Load a new mode onto the main window
	public static void loadMode(){
		
		//Clear the old mode screen
		panel.removeAll();
		createBackground();
		
		allCategories = currentMode.log;
		
		if(currentMode.name.length() > 0){
			
			String colour = "<font color=\'blue\'>";
			setLogContent("<p>" + colour + "Changed mode to " + currentMode.name + "</font></p>" + getLogContent());
			log.setText(getLogContent());
			
			createButtons();
		}
		
	}
	
	public static void createButtons(){
				
		for(String c : allCategories.keySet()){
			
			button = new JButton(c);
			panel.add(button);
			
			button.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent evt){
					
					System.out.println("Add one to " + c);
					
					allCategories.put(c, allCategories.get(c) + 1);
					System.out.println(allCategories.get(c));
					
					Log.updateFile(allCategories);
					
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
					
					for(String c: allCategories.keySet()){
						if(c==cat){
							allCategories.put(c, allCategories.get(c) + change);
						}
					}
					
					updateLog(cat,change);
					
					Log.updateFile(allCategories);
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
					
					for(String c : allCategories.keySet()){

						allCategories.put(c, 0.0);
					}
				Log.updateFile(allCategories);
				
				String fontColour = "<font color='purple'>";
				setLogContent("<p>" + fontColour + "Stats reset for " + currentMode.name + "</font></p>" + getLogContent());
				log.setText(getLogContent());
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
				
				Map<String, Double> m = sortMap(allCategories);
				
				for(Entry<String, Double> entry : m.entrySet()){
					String key = entry.getKey();
			        Double value = entry.getValue();
			        
			        theStats.setText(theStats.getText() + "<p>" + key + ":     \t" + value.intValue() + "</p>");
				}
				
				JLabel msg = new JLabel();
				statsPanel.add(msg);
				
				ArrayList<String> mostCommonDeaths = new ArrayList<String>();
				
				mostCommonDeaths = findMostCommonDeath(m);
				
				System.out.println(mostCommonDeaths);
				
				msg.setText("Your most common cause of death is " + "<>" + ". Perhaps you could mitigate this by " + "<>" );
				
				// Maybe create a new method to find the key with the largest value (can be multiple) and then iterate through that list
				// Will have to switch away from label as it doesn't wrap.
				
				
				theStats.setText(theStats.getText() + "</html>");
				
				
			}
		});
		
		panel.add(stats);
		
		frame.validate();
	}
	
	public static ArrayList<String> findMostCommonDeath(Map<String,Double> theMap){
		int mostCommon = 0;
		double highest = 0;
		
		for(Entry<String, Double> entry : theMap.entrySet()){
			if(highest < entry.getValue()){
				highest = entry.getValue();
			}
			
		}
		
		ArrayList<String> mostCommonDeaths = new ArrayList<String>();
		
		for(Entry<String,Double> entry : theMap.entrySet()){
			if(entry.getValue() == highest){
				mostCommonDeaths.add(entry.getKey());
			}	
		}
		
		return mostCommonDeaths;
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
