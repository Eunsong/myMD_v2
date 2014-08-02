package mymd.datatype;

public class LJParticle implements Particle{

	private final int number;
	private final String name;
	private final String typeName;
	private final int typeNumber;
	private final String residueName;
	private final int residueNumber;

	private final double mass;
	private final double charge;
	private final int chargeGroup;
	private final boolean isShell;

	private final double C6;
	private final double C12;



	public void setNumber(int number){
		this.number = number;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public void setTypeName(String name){
		this.typeName = name;
	}

	public void setTypeNumber(int number){
		this.typeNumber = number;
	}

	public void setResidueName(String name){
		this.residueName = name;
	}

	public void setResidueNumber(int number){
		this.residueNumber = number;
	}
	
	public double setMass(double mass){
		this.mass = mass;
	}
		
	public double setCharge(double charge){
		this.charge = charge;
	}
	
	public boolean isShell(boolean isShell){
		this.isShell = isShell;
	}



	public int getNumber(){
		return this.number;
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getTypeName(){
		return this.typeName;
	}

	public int getTypeNumber(){
		return this.typeNumber;
	}

	public int getResidueName(){
		return this.residueName;
	}

	public int getResidueNumber(){
		return this.residueNumber;
	}
	
	public double getMass(){
		return this.mass;
	}
		
	public double getCharge(){
		return this.charge;
	}
	
	public boolean isShell(){
		return this.isShell;
	}

	public void setC6(double C6){
		this.C6 = C6;
	}
	public double getC6(){
		return this.C6;
	}

	public void setC12(double C12){
		this.C12 = C12;
	}
	public double getC12(){
		return this.C12;
	}

}
