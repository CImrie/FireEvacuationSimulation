package simulation;

import myMath.Vector;

public class Exit {
	private int size;
	private Vector position;
	
	
	public Exit(){
		
	}
	
	public Exit(int size, int xCoord, int yCoord){
		this.size = size;
		this.position = new Vector(xCoord, yCoord);
	}
	
	//getters and setters
	public int getSize(){
		return this.size;
	}
	
	public Vector getPosition(){
		return this.position;
	}
}
