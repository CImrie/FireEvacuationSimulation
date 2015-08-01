package mySwing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JPanel;

import myMath.Vector;
import simulation.Evacuee;
import simulation.Exit;
import simulation.Room;


public class RoomPanel extends JPanel {
	private Room room;
	private Exit exit;
	
	public RoomPanel(Room room){
		this.room = room;
		this.exit = this.room.getExit();
		this.setPreferredSize(new Dimension(this.room.getWidth(), this.room.getHeight()));
	}
	
	//override paint component method
	public void paintComponent(Graphics g){
		//draw exit
		g.setColor(Color.RED);
		Exit exit = this.exit;
		Vector exitPos = exit.getPosition();
		g.drawString("EXIT", (int) exitPos.getX(), (int) exitPos.getY());
		g.fillRect((int) exitPos.getX(), (int) exitPos.getY(), 2, exit.getSize());
		int noOfEvacuees = this.room.getNoOfEvacuees();
		ArrayList<Evacuee> evacuees = this.room.getEvacuees();
		for (int i = 0; i < noOfEvacuees; i++){
			//g.drawRect(0, 0, this.room.getWidth(), this.room.getHeight());
			//get evacuee x and y position:
			Evacuee currentEvacuee = evacuees.get(i);
			Vector currentPosition = currentEvacuee.getPosition();
			int x = (int) currentPosition.getX();
			int y = (int) currentPosition.getY();
			//get evacuee size
			int size = currentEvacuee.getSize();
			g.setColor(Color.black);
			//dont' draw if the evacuee has exited
			if (!currentEvacuee.hasExited()){
				g.drawOval(x-size/2, y-size/2, size, size);
				g.setColor(Color.gray);
				//draw exit vectors
				g.setColor(Color.BLUE);
				//g.drawLine((int) currentEvacuee.getPosition().getX(), (int) currentEvacuee.getPosition().getY(), (int) currentEvacuee.getExitVector().getX() , (int) currentEvacuee.getExitVector().getY()+200);
			}
			
			
			
		}
	}
	
}