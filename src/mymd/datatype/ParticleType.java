package mymd.datatype;

public class ParticleType{

	protected final String name;
	protected final int number; // type identification number
	protected final double mass;
	protected final double charge;

	
	protected ParticleType(String name, int number, double mass, double charge){
		this.name = name;
		this.number = number;
		this.mass = mass;
		this.charge = charge;
	}
	

	public String getName(){
		return this.name;
	}
	public int getNumber(){
		return this.number;
	}
	public double getMass(){
		return this.mass;
	}
	public double getCharge(){
		return this.charge;
	}

	// Two particle types are equal if they have the same name 
	@Override public boolean equals(Object o){
		if ( !(o instanceof ParticleType)) return false;
		ParticleType cp = (ParticleType)o;
		return cp.name.equals(this.name);	
	}

}
