package mymd.bond;

import mymd.MdSystem;

public interface Bond<T extends MdSystem<?>>{

    
    public int geti();
    
    public int getj();
    
    public double getk0();

    public double getb0();

    public void updateForce(T sys);

    public double getEnergy(T sys);

}
