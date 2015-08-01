package simulation;

public class BoolEvacPair {
	private boolean bool;
	private Evacuee evac;
	
	public BoolEvacPair(){
		this.bool = false;
		this.evac = new Evacuee();
	}
	
	public BoolEvacPair(boolean bool, Evacuee evacuee){
		this.evac = evacuee;
		this.bool = bool;
	}
	
	public boolean getBool(){
		return this.bool;
	}
	
	public Evacuee getEvac(){
		return this.evac;
	}
}
