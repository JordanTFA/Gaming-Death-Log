package csgodc;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

public class Log {
	
	public final static int LOG_WIDTH = 300;
	public final static int LOG_HEIGHT = 600;
	
	public final static int WIDTH = 400;
	public final static int HEIGHT = 600;
		
	static JFrame logframe;
	static String logContent = "";
	
	static JTextPane log;
	StyleSheet sh;
	
	static String lastCat = null;
	static Double lastChange = null;

	public Log(){
		
	}
	
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
		JPanel logpanel = new JPanel();
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
		
		logpanel.add(log);
		
	    // Currently having an issue with JScrollPanes, they don't seem to want to work
		//JScrollPane scrollpane = new JScrollPane(log,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		JButton clear = new JButton("Clear");
		clear.addActionListener(e -> {
			setLogContent("");
			log.setText(getLogContent());
		});
		
		clear.setBounds(30, LOG_HEIGHT-70, 80, 30);
		
		logpanel.add(clear);
		
		JButton newgame = new JButton("New Game");
		newgame.addActionListener(e -> {
			setLogContent("<p>--------------------</p>" + getLogContent());
			log.setText("<html><body>" + getLogContent() + "</body></html");
		});
		
		newgame.setBounds(120, HEIGHT-70, 120, 30);
		
		logpanel.add(newgame);	
		logframe.add(logpanel);		
		logpanel.validate();
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
		addToLog();
		
	}
	
	public static void addToLog(){
		log.setText("<html><body>" + getLogContent() + "</body></html");
	}
	
	public static void makeLogVisible(){
		logframe.setVisible(true);
	}
	
	public static void applyDefaultContent(){
		log.setText("<html><body><p>"
				
				+ "This is the log that will show all of the changes that you make. Select a category to get started.</p><p></p><p>"
				+ " You can click 'New Game' to separate entries."
				
				+ "</p></body></html");
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
	
	public static String getLogContent() {
		return logContent;
	}

	public static void setLogContent(String logContent) {
		Log.logContent = logContent;
	}
	
	public static String getLastCat() {
		return lastCat;
	}

	public static void setLastCat(String lastCat) {
		Log.lastCat = lastCat;
	}

	public static Double getLastChange() {
		return lastChange;
	}

	public static void setLastChange(Double lastChange) {
		Log.lastChange = lastChange;
	}
}
