package mymd.datatype;

public interface Particle{

    public int getNumber();
    
    public int getId();
    
    public String getName();

    public ParticleType getType();

    public int getTypeNumber();

    public String getResidueName();

    public int getResidueNumber();
    
    public MoleculeType getMoleculeType();
    
    public int getMoleculeNumber();
    
    public double getMass();
        
    public double getCharge();
    
    public boolean isShell();

    public boolean isCharged();

}
