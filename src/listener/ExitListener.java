package listener;

import java.util.ArrayList;

import myMath.Vector;
import simulation.*;

public class ExitListener extends EvacueeListener{
	private Exit exit;
	private Room room;
	private ArrayList<Evacuee> survivors = new ArrayList<Evacuee>();
	
	public ExitListener(Exit exit, Room room){
		this.exit = exit;
		this.room = room;
	}
	
	//checks if any Evacuees have exited the room, and then commands them to exit
	public void checkExited(){
		//store values outside loop
		ArrayList<Evacuee> evacuees = this.room.getEvacuees();
		Vector exitPos = this.exit.getPosition();
		for (int i = 0; i < this.room.getNoOfEvacuees(); i++){
			//current evacuee
			Evacuee currentEvac = evacuees.get(i);
			Vector currentEvacPos = currentEvac.getPosition();
			//checks for vertical exit
			if ( (int) exitPos.getX() == 0 || (int) exitPos.getX() == this.room.getWidth()){
				//add 0.5 to boundary so that it is always reachable by evacuee, otherwise would be truncated to too low a value
				if((int) Vector.checkXDist(currentEvacPos, exitPos) <= (currentEvac.getSize()/2 +.5) && !currentEvac.hasExited()){
					//check within the range of y values:
					if ((int) currentEvacPos.getY() >= exitPos.getY() && (int) currentEvacPos.getY() <= (exitPos.getY() + this.exit.getSize() )){
						evacuees.get(i).exitRoom();
						this.survivors.add(currentEvac);
					}
				}
			}
		}
	}
	
	public ArrayList<Evacuee> getSurvivors(){
		return this.survivors;
	}
}
