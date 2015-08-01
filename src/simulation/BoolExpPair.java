package simulation;

public class BoolExpPair {
	private boolean bool;
	private Experiment exp;
	
	public BoolExpPair(){
		this.bool = false;
		this.exp = null;
	}
	
	public BoolExpPair(boolean bool,  Experiment exp ){
		this.exp = exp;
		this.bool = bool;
	}
	
	public boolean getBool(){
		return this.bool;
	}
	
	public Experiment getExp(){
		return this.exp;
	}
	
	public void setBool(boolean bool){
		this.bool = bool;
	}
}
