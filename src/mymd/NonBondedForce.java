package mymd;

import mymd.datatype.MdVector;

public interface NonBondedForce<T extends MdSystem<?>>{

//	public MdVector get( T sys, int i, int j);

	/**  
	 * Update all nonbonded forces in newTraj of sys using nblist
	 *
	 * @param sys
	 * @param nblist 
	 */
	public void updateAll( T sys, NeighborList nblist ); 

}
