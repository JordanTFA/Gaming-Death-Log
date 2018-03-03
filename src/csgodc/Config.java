package csgodc;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import csgodc.TextPrompt.Show;

public class Config {
	
	static Mode mode;
	
	final static int CFG_WIDTH = 300;
	final static int CFG_HEIGHT = 350;
	
	static int numberOfCategories;
	static TreeMap<String,Double> allCategories;
	
	static Mode currentMode;

	public Config(Mode mode){
		
		Config.mode = mode;
		
	}
	
	public static void createCfg(){
				
		if(GUI.getCurrentMode() != null){
		
			JFrame cfgFrame = new JFrame("Configuration");
			cfgFrame.setSize(CFG_HEIGHT, CFG_WIDTH);
			cfgFrame.setVisible(true);
			cfgFrame.setResizable(true);
			cfgFrame.setLocationRelativeTo(null);
			
			JPanel cfgPanel = new JPanel();
			cfgFrame.getContentPane().setLayout(null);
			cfgPanel.setBounds(0, 0, CFG_HEIGHT, CFG_WIDTH);
			cfgPanel.setLayout(null);
			cfgPanel.setBackground(new Color(230,230,250));
			cfgFrame.add(cfgPanel);
			
			
			JLabel lblCategory = new JLabel(GUI.getCurrentMode().name);
			lblCategory.setBounds(70, 5, 230, 15);
			cfgPanel.add(lblCategory);
		
			JTextField catToAdd = new JTextField();	
			TextPrompt tp = new TextPrompt("Click Here to Add a Category", catToAdd);
			tp.setShow(Show.FOCUS_LOST);
			tp.changeAlpha(0.5f);
			catToAdd.setSize(3000, 5);
			catToAdd.setBounds(30,25,170, 25);
			
			catToAdd.addMouseListener(new MouseAdapter(){
				@Override
				public void mouseClicked(MouseEvent e){
					catToAdd.setText("");
				}
			});
			
			cfgPanel.add(catToAdd);

			TreeMap<String,Double> cats = FileSystem.generateCategories(getCurrentMode().id);
			
			JButton addCat = new JButton("Add");
			addCat.setBounds(220, 25, 60, 25);
		
			addCat.addActionListener(e ->{
				
				if(getNumberOfCategories() >= 18){
					JOptionPane.showMessageDialog(null, "Reached max number of categorie! (18)");
				}else{
					
					String content = catToAdd.getText();
					
					if(content.length() > 30){
						JOptionPane.showMessageDialog(null, "Too Long!");
					}else{
						cats.put(content, 0.0);
						FileSystem.updateFile(cats);
						
						allCategories = cats;
						
						cfgFrame.dispose();
						createCfg();
							
						String colour = "<font color=\'green\'>";
						Log.setLogContent("<p>" + colour + "Added " + catToAdd.getText() + "</font></p>" + Log.getLogContent());
						Log.addToLog();
					}

					GUI.createBackground();
					GUI.createButtons();
				}
			});	
			
			cfgPanel.add(addCat);
			
			JComboBox<String> jcmb = new JComboBox<String>();
			
			jcmb.setBounds(15, 220, 130, 25);
			
			JLabel lblCats = new JLabel();
			lblCats.setForeground(Color.BLUE);
			JLabel lblCats2 = new JLabel();
			lblCats2.setForeground(Color.RED);		

			int counter = 0;
			
			// Create a label for each category
			for(String s : cats.keySet()){
				
				if(counter < 9){
					lblCats.setText( lblCats.getText()  + "<p>" + s + "</p>");
					
				}else{
					lblCats2.setText( lblCats2.getText()  + "<p>" + s + "</p>");
				}
				jcmb.addItem(s);
				setNumberOfCategories(cats.size());
				
				counter++;
				
			}
			
			lblCats.setText("<html><body>" + lblCats.getText() + "</body></html>");
			lblCats2.setText("<html><body>" + lblCats2.getText() + "</body></html>");
			lblCats.setBounds(30, 35, 150, 200);
			lblCats2.setBounds(180, 35, 150, 200);
			
			JButton removeCat = new JButton("Remove Category");
			removeCat.setBounds(155, 220, 140,25);
			removeCat.addActionListener(e ->{
					
				cats.remove(jcmb.getSelectedItem());
				FileSystem.updateFile(cats);
					
				allCategories = cats;
					
				cfgFrame.dispose();
				createCfg();
				
				String colour = "<font color=\'red\'>";
				Log.setLogContent("<p>" + colour + "Removed " + jcmb.getSelectedItem()  + "</p>" + Log.getLogContent());
				Log.addToLog();
					
				GUI.createBackground();
				GUI.createButtons();

			});	
			
			cfgPanel.add(lblCats);
			cfgPanel.add(lblCats2);
			cfgPanel.add(jcmb);
			cfgPanel.add(removeCat);
			
		}else{
			JOptionPane.showMessageDialog(null, "Select a Category to Configure!");
		}
	}
	
	public static int getNumberOfCategories() {
		return numberOfCategories;
	}

	public static void setNumberOfCategories(int numberOfCategories) {
		GUI.numberOfCategories = numberOfCategories;
	}
	
	public static Mode getCurrentMode() {
		return currentMode;
	}

	public static void setCurrentMode(Mode currentMode) {
		Config.currentMode = currentMode;
	}

}
