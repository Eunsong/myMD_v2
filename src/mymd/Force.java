package mymd;

import mymd.datatype.MdVector;

public interface Force<T extends MdSystem<?>>{

	public MdVector get( T sys, int i, int j);

	/**  
	 * Update all forces in newTraj of sys using nblist
	 *
	 * @param sys
	 * @param nblist 
	 */
	public void update( T sys, NeighborList nblist ); 

}
