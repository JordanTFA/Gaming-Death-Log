package csgodc;
import java.awt.Color;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class GUI {
	
	public final int WIDTH = 400;
	public final int HEIGHT = 600;

	public GUI(){
		
		JFrame frame = new JFrame();
		frame.setSize(WIDTH, HEIGHT);
		frame.setTitle("CS:GO Death Counter");
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.BLACK);
		frame.add(panel);
		
		ArrayList<String> theCategories = Log.getCategories();	
	}
}
