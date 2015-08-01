package myMath;

public class Vector {
	
	private double x;
	private double y;
	
	public Vector(){
		this.x = 0;
		this.y = 0;
	}
	
	public Vector(double xComp, double yComp){
		this.x = xComp;
		this.y = yComp;
	}
	
	//getters and setters
	public double getX(){
		return this.x;
	}
	
	public double getY(){
		return this.y;
	}
	
	public void setX(double x){
		this.x = x;
	}
	
	public void setY(double y){
		this.y = y;
	}
	
	//returns the positive magnitude of this vector
	public double getMagnitude(){
		//return magnitude which is the square root of the sum of squares of each component
		return Math.sqrt(Math.pow(this.getX(), 2) + Math.pow(this.getY(), 2));
	}
	
	public String toString(){
		String str = "{x: " + this.x + " , y: " + this.y + " }";
		return str;
	}
	
	//this is a class method and adds two vectors together, returning the result
	public static Vector addVector(Vector a, Vector b){
		return new Vector(a.getX()+b.getX(), a.getY()+b.getY());
	}
	
	public static Vector subtractVector(Vector a, Vector b){
		return new Vector(a.getX()-b.getX(), a.getY()-b.getY());
	}
	
	//uses distance formula to get the magnitude of a vector connecting two vectors
	public static double getDistanceBetweenVectors(Vector a, Vector b){
		double xDiffSquared = Math.pow(b.getX() - a.getX(), 2);
		double yDiffSquared = Math.pow(b.getY() - a.getY(), 2);
		return Math.sqrt(xDiffSquared + yDiffSquared );
	}
	
	//check if a circle intersects another circle
	public static boolean checkIntersect(Vector a, Vector b, int size){
		if (getDistanceBetweenVectors(a,b) <= size ){
			return true;
		}
		else return false;
	}
	
	//generate scalar multiple of vector
	public Vector multiplyByScalar(double scalar){
		//distance = speed x time
		double angle = Math.atan2(this.getY(),this.getX());
		double xIncrement = scalar * Math.cos(angle);
		double yIncrement = scalar * Math.sin(angle);
		return new Vector(xIncrement, yIncrement);
	}
	
	//returns a new vector that is the rotation of the given vector
	public static Vector rotateVector(Vector vector, double radians){
		double oldX = vector.getX();
		double oldY = vector.getY();
		/* follow the rotation matrix of 
		 * |x'| = |cosx  sinx||x|
		 * |y'| = |-sinx cosx||y|
		 */
		double newX = Math.cos(radians)*oldX + Math.sin(radians)*oldY;
		double newY = -1 * Math.sin(radians)*oldX + Math.cos(radians)*oldY;
		return new Vector(newX, newY);
	}
	
	//check x and y distances between two vectors
	private static double checkDist(double a, double b){
		return Math.abs(b-a);
	}
	
	public static double checkXDist(Vector a, Vector b){
		return checkDist(a.getX(), b.getX());
	}
	
	public static double checkYDist(Vector a, Vector b){
		return checkDist(a.getY(), b.getY());
	}
}
