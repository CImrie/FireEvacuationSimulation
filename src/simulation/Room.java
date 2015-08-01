package simulation;

import java.util.ArrayList;

import listener.CollisionListener;
import listener.ExitListener;
import myMath.Vector;

import java.util.Random;

public class Room {
	private ArrayList<Evacuee> evacuees = new ArrayList<Evacuee>();
	private int width;
	private int height;
	private Exit exit;
	private int noOfEvacuees;
	private Evacuee modelEvacuee;
	private CollisionListener cL;
	private ExitListener eL;
	private Random randomgen = new Random();
	
	public Room(){
		System.out.println("Error. You can not have a room with no properties!");
	}
	
	public Room(int width, int height, int noOfEvacuees, Evacuee modelEvacuee, Exit exit){
		this.width = width;
		this.height = height;
		this.noOfEvacuees = noOfEvacuees;
		this.modelEvacuee = modelEvacuee;
		this.exit = exit;
		this.eL = new ExitListener(this.exit, this);
		this.generateEvacuees();
		
		
	}
	
	//generates and assigns a single exit to the room
	public void generateExit(int exitSize, int exitX, int exitY){
		this.exit = new Exit(exitSize, exitX, exitY);
	}
	
	//generates and assigns the necessary number of evacuees to the room, and then randomises position
	public void generateEvacuees(){
		//create new arraylist of the generated evacuees
		ArrayList<Evacuee> generatedEvacuees = new ArrayList<Evacuee>();
		//loop through and generate evacuees
		for (int i = 0; i < this.getNoOfEvacuees(); i++){
			generatedEvacuees.add(new Evacuee(
					this.modelEvacuee.getSize(),
					this.modelEvacuee.getAgility(),
					this.modelEvacuee.getPatience(),
					this.modelEvacuee.getSpeed())
			);
		}
		//assign the room evacuees to be equal to the generated evacuees (prevents duplication rather than just adding)
		this.setEvacuees(generatedEvacuees);
		//randomise position of each evacuee
		this.randomisePos();
		//randomise exit vectors
		this.generateExitVectors();
	}
	
	//randomly generate and assign positions to each evacuee
	public void randomisePos(){
		this.cL = new CollisionListener(this.evacuees, this);
		int noOfEvacuees = this.getNoOfEvacuees();
		for (int i = 0; i < noOfEvacuees; i++){
			Vector randomVector = this.generateRandomVector();
			//this.cL.checkCircleCollisionAgainstVector(randomVector)
			while (this.cL.checkFutureCollision(randomVector, this.getEvacuees().get(i)).getBool()){
				randomVector = this.generateRandomVector();
			}
			this.getEvacuees().get(i).setPosition(randomVector);
		}
	}
	
	//generate random vector within the confines of the room (minus the size of the evacuee)
	private Vector generateRandomVector(){
		double evacRadius = this.modelEvacuee.getSize()/2;
		double x = Math.abs(this.width*this.randomgen.nextGaussian() - evacRadius);
		double y = Math.abs(this.height*this.randomgen.nextGaussian() - evacRadius);
		return (new Vector(x, y));
	}
	
	//generate and assign exit vectors to each evacuee
	public void generateExitVectors(){
		int noOfEvacuees = this.getNoOfEvacuees();
		for (int i = 0; i < noOfEvacuees; i++){
			//current evacuee
			Evacuee evac = this.evacuees.get(i);
			//evacuees current position vector
			Vector currPos = evac.getPosition();
			Vector eVector = this.calculateRandomExitVector(currPos);
			evac.setExitVector(eVector);
		}
	}
	
	//calculate random exit vector
	private Vector calculateRandomExitVector(Vector currentPositionVector){
		Vector exitPos = this.exit.getPosition();
		//handle case where x = 0 or x = width of room (i.e. exit is on vertical wall)
		double xCoord = exitPos.getX();
		double yCoord = exitPos.getY();
		//exit position vector - current position vector
		Vector eVector = Vector.subtractVector(new Vector(xCoord, yCoord), currentPositionVector);
		//now to randomise y coordinate within exit
		
		//gaussian biased towards '0' but more fairly distributed than Math.random()
		//adding half exit size redistributes fairly around the centre
		eVector.setY(eVector.getY() + this.randomgen.nextGaussian()*this.exit.getSize() + this.exit.getSize()/2);
		//return exit vector
		return (eVector);
	}
	
	//updates position for each evacuee
	public void updateRoom(double timeStep){
		this.shuffleEvacuees();
		int noOfEvacuees = this.getNoOfEvacuees();
		for (int i = 0; i < noOfEvacuees; i++){
			//store current evacuee
			Evacuee currentEvac = this.evacuees.get(i);
			//check collision then update position if no collision
			//if evacuee has not exited, increment over the timestep (increment so that the number of intervals is sufficient to remove collision problems(movement is not greater than the size of an evacuee))
			if (!currentEvac.hasExited()){
				int numberOfIntervals = (int) (currentEvac.getSpeed()*timeStep) + 1;
				for(int j = 0; j < numberOfIntervals; j++){					
					currentEvac.generateIncrementVector(timeStep/numberOfIntervals);
					//create as random as possible simulation by randomising at each interval
					this.generateExitVectors();
					//check collision and who collided with (if applicable)
					BoolEvacPair colResult = this.cL.checkFutureCollision(Vector.addVector(currentEvac.getPosition(), currentEvac.getIncrementVector()), currentEvac);
					if (!colResult.getBool()){
						currentEvac.updatePosition();
					}
					else {
						//move max distance
						double distance = this.getDistanceBetweenTwoEvacuees(currentEvac, colResult.getEvac());
						//if a distance can still be moved, move it, otherwise if the distance between collided objects = 0 then randomly choose side/backward vector
						if (distance > 0){
							currentEvac.moveMaxDistanceAlongIdealIncrement(distance);
						}
						else if (this.randomgen.nextGaussian() < currentEvac.getAgility()){
							//check if can move to side
							BoolEvacPair colResult2 = this.cL.checkFutureCollision(Vector.addVector(currentEvac.getPosition(), Vector.rotateVector(currentEvac.getIncrementVector(), Math.PI/2)), currentEvac);
							BoolEvacPair colResult3 = this.cL.checkFutureCollision(Vector.addVector(currentEvac.getPosition(), Vector.rotateVector(currentEvac.getIncrementVector(), -Math.PI/2)), currentEvac);
							//random number to choose which side to go to
							double random = this.randomgen.nextGaussian();
							if (!colResult2.getBool() && random < 0.5){
								//move to side
								currentEvac.moveSide();
							}
							else if (!colResult3.getBool() && random > 0.5){
								//move to other side
								currentEvac.moveNegSide();
							}
							
						}
						else if (this.randomgen.nextGaussian() < currentEvac.getPatience()){
							//check if can move back
							BoolEvacPair colResult4 = this.cL.checkFutureCollision(Vector.addVector(currentEvac.getPosition(), Vector.rotateVector(currentEvac.getIncrementVector(), Math.PI)), currentEvac);
							if (!colResult4.getBool()){
								//move back
								this.evacuees.get(i).moveBack();
							}
						}
					}
				}
			}
			
		}
		//regenerate exit vectors based on new positions
		this.generateExitVectors();
		this.eL.checkExited();
	}
	
	//shuffle order of evacuees (so that movement is randomised)
	public void shuffleEvacuees(){
		ArrayList<Evacuee> temp = new ArrayList<Evacuee>();
		ArrayList<Evacuee> evacuees = this.evacuees;
		while (temp.size() < evacuees.size()){
			//System.out.println(this.evacuees.size());
			//randomly choose an evacuee
			int i = (int) (Math.random()*evacuees.size());
			//if evacuee is not already in the temp array list, add
			if (!temp.contains(evacuees.get(i))){
				temp.add(evacuees.get(i));
			}
			//else randomise again until the size of the first array list is the same as the size of the temp
		}
	}

	public double getDistanceBetweenTwoEvacuees(Evacuee e1, Evacuee e2){
		double distance = 0;
		//check neither evac is null (as null means it was a collision between an evacuee and the wall and this method is not necessary)
		if (e1 != null && e2 != null){
			distance = (int) Vector.getDistanceBetweenVectors(e1.getPosition(), e2.getPosition()) - (e1.getSize());
		}
		return distance;
	}
	
	//getters and setters
	public Exit getExit(){
		return this.exit;
	}
	
	public int getWidth(){
		return this.width;
	}
	
	public int getHeight(){
		return this.height;
	}
	
	public int getNoOfEvacuees(){
		return this.noOfEvacuees;
	}
	
	public ArrayList<Evacuee> getEvacuees(){
		return this.evacuees;
	}
	
	public ExitListener getExitListener(){
		return this.eL;
	}
	
	public Evacuee getModelEvacuee(){
		return this.modelEvacuee;
	}
	
	public void setEvacuees(ArrayList<Evacuee> evacuees){
		this.evacuees = evacuees;
	}
}
