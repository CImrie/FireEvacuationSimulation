package simulation;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import mySwing.Frame;
import mySwing.RoomPanel;

public class Experiment {
	private Room room;
	private int noOfEvacAtStart;
	private boolean hasFinished = false;
	//note... timestep and clock are in seconds (arbitrary, but chosen!)
	private double clock;
	private Timer t;
	private Timer t2;
	private boolean activateGraphics = false;
	private double timeStep;
	private int expNo;
	
	//default constructor
	public Experiment(){
		
	}
	
	public Experiment(int roomWidth, int roomHeight, int noOfEvacuees, Evacuee modelEvacuee, Exit exit, boolean activateGraphics, double timeStep, int experimentNo){
		this.room = new Room(roomWidth, roomHeight, noOfEvacuees, modelEvacuee, exit);
		this.noOfEvacAtStart = noOfEvacuees;
		this.activateGraphics = activateGraphics;
		this.timeStep = timeStep;
		this.expNo = experimentNo;
		this.runExperiment();
	}	
	
	public void runExperiment(){
		//if graphics are activated, run gui
		if (this.activateGraphics){
			//create gui
			RoomPanel p = new RoomPanel(this.getRoom());
			final Frame f = new Frame(this.room.getWidth(), this.room.getHeight(), "Fire! (Evacuation Simulation)" + " Exp #: " +  this.expNo);
			
			f.add(p);
			p.setPreferredSize(new Dimension(this.room.getWidth(), this.room.getHeight()));
			f.pack();
			
			this.t = new Timer(1, new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (hasFinished()){
						//close frame (as it shows nothing useful)
						f.close();
						t.stop();
	
					}
					else {
						updateExperiment(timeStep);
						f.repaint();
					}
				}});
	
			t.start();
		}
		else {
			this.t2 = new Timer(1, new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (hasFinished()){
						t2.stop();
					}
					else {
						updateExperiment(timeStep);
					}
				}});
			t2.start();
		}
	}
	
	public void updateExperiment(double timeStep){
		if (this.room.getExitListener().getSurvivors().size() == this.noOfEvacAtStart){
			//get clock update from the last evacuee to leave (evacuee handles incremental addition to clock)
			if (this.room.getExitListener().getSurvivors().size() > 1){
				this.clock = this.room.getExitListener().getSurvivors().get(this.room.getExitListener().getSurvivors().size()-1).getClock();
			}
			else {
				this.clock = 0;
			}
			this.hasFinished = true;
		}
		else {
			this.room.updateRoom(timeStep);
		}
		
	}
	
	public boolean hasFinished(){
		return this.hasFinished;
	}
	
	public double getTime(){
		return this.clock;
	}
	
	public Room getRoom(){
		return this.room;
	}
}
