package csgodc;
import static java.util.stream.Collectors.toMap;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
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
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
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
	
	static int numberOfCategories;

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
		createLog();
		
		// Make the log visible if it's selected from the menu bar
		viewlog.addActionListener(e -> logframe.setVisible(true));
	
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
		
		setNumberOfCategories(0);
		viewcfg.addActionListener(e -> createCfg());

	}
	
	// Create log content
	public static void createLog(){
		
	    Toolkit tk = Toolkit.getDefaultToolkit();
	    Dimension screenSize = tk.getScreenSize();
		
		logframe = new JFrame();
		logframe.setSize(LOG_WIDTH, LOG_HEIGHT);
		logframe.setVisible(true);
		logframe.setTitle("Log");
		logframe.setResizable(false);
		
		// Set the log window next to the main window
		logframe.setLocation((screenSize.width / 2) + (WIDTH/2),(screenSize.height / 2) -  (HEIGHT/2) - 20); 
		
		// Align text left
		JPanel logpanel = new JPanel(/*new GridBagLayout()*/);
		//GridBagConstraints c = new GridBagConstraints();
		logpanel.setLayout(null);
		logpanel.setBackground(new Color(220,220,220));
			
		log = new JTextPane();
		//JPanel noWrapPanel = new JPanel( new BorderLayout() );
		//noWrapPanel.add( log );
		//JScrollPane scrollPane = new JScrollPane( noWrapPanel );
		//scrollPane.setViewportView(log); // creates a wrapped scroll pane using the text pane as a viewport.
		
		log = applyCSS(log,"body {line-height: 50px; font-family: Dialog; font-size:12; font-weight: bold}");
		
		log.setBounds(10, 10, LOG_WIDTH - 20, LOG_HEIGHT-100);
		log.setBackground(new Color(220,220,220));
		log.setEditable(false);
		log.setHighlighter(null);
		
		/*c.insets = new Insets(5,5,5,5);
		
		c.gridwidth = 5;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;*/
		
		logpanel.add(log);
		
	    // Currently having an issue with JScrollPanes, they don't seem to want to work
		//JScrollPane scrollpane = new JScrollPane(log,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		JButton clear = new JButton("Clear");
		clear.addActionListener(e -> {
			setLogContent("");
			log.setText(getLogContent());
		});
		
		/*c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 20;*/
		
		clear.setBounds(30, LOG_HEIGHT-70, 80, 30);
		
		logpanel.add(clear);
		
		JButton newgame = new JButton("New Game");
		newgame.addActionListener(e -> {
			setLogContent("<p>--------------------</p>" + getLogContent());
			log.setText("<html><body>" + getLogContent() + "</body></html");
		});
		
		newgame.setBounds(120, HEIGHT-70, 120, 30);
		//c.gridx = 2;
		//c.gridy = 20;
		
		logpanel.add(newgame);	
		logframe.add(logpanel);		
		logpanel.validate();
	}
	
	// Creates the configuration panel
	public static void createCfg(){
		
		int cfgWidth = 300;
		int cfgHeight = 350;
		
		
		if(getCurrentMode() != null){
		
			JFrame cfgFrame = new JFrame("Configuration");
			cfgFrame.setSize(cfgHeight, cfgWidth);
			cfgFrame.setVisible(true);
			cfgFrame.setResizable(true);
			cfgFrame.setLocationRelativeTo(null);
			
			JPanel cfgPanel = new JPanel();
			cfgFrame.getContentPane().setLayout(null);
			cfgPanel.setBounds(0, 0, cfgHeight, cfgWidth);
			cfgPanel.setLayout(null);
			cfgPanel.setBackground(new Color(230,230,250));
			cfgFrame.add(cfgPanel);
			
			
			JLabel lblCategory = new JLabel(getCurrentMode().name);
			//c.gridx = 1;
			//c.gridy = 0;
			lblCategory.setBounds(70, 5, 200, 15);
			cfgPanel.add(lblCategory);
		
			JTextField catToAdd = new JTextField("Click Here to Add a Category");
			catToAdd.setSize(3000, 5);
			catToAdd.setBounds(30,25,170, 25);
			
			catToAdd.addMouseListener(new MouseAdapter(){
				@Override
				public void mouseClicked(MouseEvent e){
					catToAdd.setText("");
				}
			});
			
			//c.gridwidth = 2;
			//c.fill = GridBagConstraints.HORIZONTAL;
			//c.gridx = 1;
			//c.gridy = 1;
			cfgPanel.add(catToAdd);
        
			TreeMap<String,Double> cats = Log.generateCategories(currentMode.id);
			
			JButton addCat = new JButton("Add");
			addCat.setBounds(220, 25, 60, 25);
		
			addCat.addActionListener(e ->{
				
				System.out.println(numberOfCategories);
				
				if(getNumberOfCategories() >= 18){
					System.out.println("No.");
				}else{
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
			
			//c.gridwidth = 1;
			//c.fill = GridBagConstraints.HORIZONTAL;
			//c.gridx = 4;
			//c.gridy = 1;
			
			cfgPanel.add(addCat);
			
			JComboBox<String> jcmb = new JComboBox<String>();
			
			jcmb.setBounds(15, 220, 130, 25);
			
			JLabel lblCats = new JLabel();
			JLabel lblCats2 = new JLabel();
			// Create a check-box for each category
			for(String s : cats.keySet()){
				
				if(getNumberOfCategories() < 9){
					lblCats.setText( lblCats.getText()  + "<p>" + s + "</p>");
				}else{
					lblCats2.setText( lblCats2.getText()  + "<p>" + s + "</p>");
				}
				jcmb.addItem(s);
				setNumberOfCategories(getNumberOfCategories() + 1);
				
			}
			
			lblCats.setText("<html><body>" + lblCats.getText() + "</body></html>");
			lblCats2.setText("<html><body>" + lblCats2.getText() + "</body></html>");
			lblCats.setBounds(30, 35, 150, 200);
			lblCats2.setBounds(180, 35, 150, 200);
			
			JButton removeCat = new JButton("Remove Category");
			removeCat.setBounds(155, 220, 140,25);
			removeCat.addActionListener(e ->{
					
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

			});	
			
			cfgPanel.add(lblCats);
			cfgPanel.add(lblCats2);
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
		
		msg = applyCSS(msg,"body {line-height: 50px; font-family: Dialog; font-size:12; font-weight: bold}");
		
		StyledDocument doc = msg.getStyledDocument();
		SimpleAttributeSet center = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		doc.setParagraphAttributes(0, doc.getLength(), center, false);
	    
	    msg.setText("Welcome to the app. Blah blah blah blah");
		
		frame.setTitle("Gaming Death Log");
		panel.setBackground(BGColour);
		msg.setBackground(panel.getBackground());
	
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
			panel.validate();
		
	}
	
	// Load a new mode onto the main window
	public static void loadMode(){
		
		//Clear the old mode screen
		createBackground();
		
		allCategories = currentMode.log;
		
		if(currentMode.name.length() > 0){
			
			String colour = "<font color=\'blue\'>";
			setLogContent("<p>" + colour + "Changed mode to " + currentMode.name + "</font></p>" + getLogContent());
			log.setText(getLogContent());
			
			createButtons();
		}
	}
	
	public static JTextPane applyCSS(JTextPane pane, String cssContent){
		
		pane.setContentType("text/html");
		
		StyleSheet styleSheet = new StyleSheet();
		HTMLDocument htmlDocument;
		HTMLEditorKit htmlEditorKit = new HTMLEditorKit();
		
		styleSheet.addRule(cssContent);	
		htmlEditorKit.setStyleSheet(styleSheet);
	    htmlDocument = (HTMLDocument) htmlEditorKit.createDefaultDocument();
	    pane.setEditorKit(htmlEditorKit);
	    pane.setDocument(htmlDocument);
		
		return pane;
	}
	
	public static void createButtons(){
		
		setNumberOfCategories(0);

				
		for(String c : allCategories.keySet()){
			
			button = new JButton(c);
			panel.add(button);
			
			button.addActionListener(e ->{
					
				System.out.println("Add one to " + c);
					
				allCategories.put(c, allCategories.get(c) + 1);
				System.out.println(allCategories.get(c));
					
				Log.updateFile(allCategories);
					
				updateLog(c, 1.0);			
			});	
		}

		JPanel utilButtons = new JPanel();
		utilButtons.setBounds(0, HEIGHT-200, WIDTH, HEIGHT + 200);
		utilButtons.setLayout(null);
		frame.add(utilButtons);
		
		JButton undo = new JButton("Undo");
		undo.addActionListener(e -> {
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
			Log.updateFile(allCategories);
				
			String fontColour = "<font color='purple'>";
			setLogContent("<p>" + fontColour + "Stats reset for " + currentMode.name + "</font></p>" + getLogContent());
			log.setText(getLogContent());
			
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
	
	public static int getNumberOfCategories() {
		return numberOfCategories;
	}

	public static void setNumberOfCategories(int numberOfCategories) {
		GUI.numberOfCategories = numberOfCategories;
	}

}
