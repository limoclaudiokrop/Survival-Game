import java.awt.Color;

import javax.swing.JFrame;


//Class to set up the frame of game
public class SpaceFrame extends JFrame{
	SpaceFrame(){
		//Add panel and set title
		this.add(new myPanel());
		this.setTitle("Survival in Space");
		
		//set close operation and property of resizable
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.pack();
		this.setVisible(true);
		this.setLocationRelativeTo(null);
		
		//Set background color
		this.getContentPane().setBackground(Color.YELLOW); 
	}
}
