package mymd.datatype;

public interface Particle{

	public int getNumber();
	
	public String getName();
	
	public String getType();

	public int getTypeNumber();

	public String getResidueName();

	public int getResidueNumber();
	
	public String getMoleculeType();
	
	public int getMoleculeNumber();
	
	public double getMass();
		
	public double getCharge();
	
	public boolean isShell();

	public boolean isCharged();

}
