package listener;

import java.util.ArrayList;

import simulation.Evacuee;

public abstract class EvacueeListener {
	private ArrayList<Evacuee> evacuees = new ArrayList<Evacuee>();
	
	public EvacueeListener(){
		
	}
	
	public EvacueeListener(ArrayList<Evacuee> evacuees){
		this.evacuees = evacuees;
	}
	
	public void addEvacuee(Evacuee evacuee){
		this.evacuees.add(evacuee);
	}
	
	public void removeEvacuee(Evacuee evacuee){
		this.evacuees.remove(evacuee);
	}
	
	public ArrayList<Evacuee> getEvacuees(){
		return this.evacuees;
	}
}
