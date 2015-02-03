package mymd.nonbond;

import mymd.*;
import mymd.datatype.MdVector;

public interface NonBond<T extends MdSystem<?>>{

    //public MdVector getForce( T sys, int i, int j);
    public void updateAllForces(T sys, NeighborList nblist);

    public double getTotalEnergy(T sys, NeighborList nblist);


}
