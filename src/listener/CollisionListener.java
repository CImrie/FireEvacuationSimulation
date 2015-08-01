package listener;

import java.util.ArrayList;

import myMath.Vector;
import simulation.BoolEvacPair;
import simulation.Evacuee;
import simulation.Room;

public class CollisionListener extends EvacueeListener{

	private ArrayList<Evacuee> evacuees;
	private Room room;
	
	public CollisionListener(ArrayList<Evacuee> evacuees, Room room){
		this.evacuees = evacuees;
		this.room = room;
	}
	
	public BoolEvacPair checkFutureCollision(Vector trialPosition, Evacuee evacuee){
		int limit = this.evacuees.size();
		for (int i = 0; i < limit; i++){
			Evacuee currEvac = this.evacuees.get(i);
			Vector currPos = currEvac.getPosition();
			double size = currEvac.getSize();
			boolean exitStatus = currEvac.hasExited();
			if (Vector.checkIntersect(currPos, trialPosition, (int) size) && currEvac != evacuee && exitStatus == false){
				return new BoolEvacPair(true, currEvac);
			}
			else if (this.checkWallCollision(trialPosition, evacuee)){
				return new BoolEvacPair(true, null);
			}
		}
		return new BoolEvacPair(false, new Evacuee());
	}
	
	public boolean checkWallCollision(Vector trialPosition, Evacuee evacuee){
		Room room = this.room;
		int width = room.getWidth();
		int height = room.getHeight();
		double evacRadius = evacuee.getSize()/2;
		double newX = trialPosition.getX();
		double newY = trialPosition.getY();
		
		boolean xCheck = !(newX >= (0 + evacRadius ) && trialPosition.getX() <= (width - evacRadius));
		boolean yCheck = !(newY >= (0 + evacRadius ) && trialPosition.getY() <= (height - evacRadius));
		if (xCheck || yCheck){
				return true;
		}
		return false;
	}
	
	
}
