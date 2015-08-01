package simulation;

import myMath.Vector;

public class Evacuee {
	private int size;
	private double agility;
	private double patience;
	private double speed;
	private Vector currentExitVector;
	private Vector currentPositionVector;
	private Vector incrementVector;
	private boolean hasExited;
	private double clock;

	
	public Evacuee(){
		
	}
	
	public Evacuee(int size, double agility, double patience, double speed){
		this.size = size;
		this.agility = agility;
		this.patience = patience;
		this.speed = speed;
		this.currentPositionVector = new Vector();
		this.hasExited = false;
	}
	
	//getters and setters
	public int getSize(){
		return this.size;
	}
	
	public double getAgility(){
		return this.agility;
	}
	
	public double getPatience(){
		return this.patience;
	}
	
	public double getSpeed(){
		return this.speed;
	}
	
	public void setPosition(Vector position){
		this.currentPositionVector = position;
	}
	
	public Vector getPosition(){
		return this.currentPositionVector;
	}
	
	public Vector getExitVector(){
		return this.currentExitVector;
	}
	
	public boolean hasExited(){
		return this.hasExited;
	}
	
	public void setExitVector(Vector exitVector){
		this.currentExitVector = exitVector;
	}
	
	//generates (the ideal) vector that adds to the position in order to move towards an exit or target
	public void generateIncrementVector(double timeStep){
		//distance = speed x time
		this.clock += timeStep;
		double magnitude = this.getSpeed() * timeStep;
		this.incrementVector = this.getExitVector().multiplyByScalar(magnitude);
	}
	
	public Vector getIncrementVector(){
		return this.incrementVector;
	}
	
	//updates the position according to current exit vector and given speed (with time interval)
	public void updatePosition(){
		this.setPosition(Vector.addVector(this.currentPositionVector, this.incrementVector));
	}
	
	public void moveMaxDistanceAlongIdealIncrement(double distance){
		//calculate new increment vector for this distance
		Vector increment = this.getIncrementVector().multiplyByScalar(distance);
		this.setPosition(Vector.addVector(this.currentPositionVector, increment));
	}
	
	//if blocked, try moving to side
	//(rotates exit vector +- 90 degrees)
	public void moveSide(){
		//rotate increment vector that is calculated
		Vector increment = Vector.rotateVector(this.getIncrementVector(), Math.PI / 2);
		this.setPosition(Vector.addVector(this.currentPositionVector, increment ));
	}
	
	//if blocked, try moving to side
	//(rotates exit vector +- 90 degrees)
	public void moveNegSide(){
		//rotate increment vector that is calculated
		Vector increment = Vector.rotateVector(this.getIncrementVector(), -Math.PI / 2);
		this.setPosition(Vector.addVector(this.currentPositionVector, increment ));
	}
	
	//if blocked, try moving backwards
	//(rotates exit vector 180 degrees)
	public void moveBack(){
		//rotate increment vector that is calculated
		Vector increment = Vector.rotateVector(this.getIncrementVector(), Math.PI);
		this.setPosition(Vector.addVector(this.currentPositionVector, increment ));
	}
	
	public double getClock(){
		return this.clock;
	}
	
	//exits the room
	public void exitRoom(){
		this.hasExited = true;
	}
}
