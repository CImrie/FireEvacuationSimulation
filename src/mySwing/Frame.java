package mySwing;
import javax.swing.JFrame;

public class Frame extends JFrame {
	public Frame(int X, int Y, String title) {
		//set size and visibility
		this.setSize(X, Y);
		this.setVisible(true);
		this.setTitle(title);
		
		//allow closing using 'X' button
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void close(){
		this.setVisible(false);
		dispose();
		pack();
	}
	
}