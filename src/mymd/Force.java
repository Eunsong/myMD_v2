package mymd;

public interface Force<E extends MdVector, T extends MdSystem<E>>{

	public <E> E get( T sys, int i, int j, double r);

	/**  
	 * Update all forces in currTraj of sys using nblist
	 *
	 * @param sys
	 * @param nblist 
	 */
	public <E> void update( T sys, NeighborList nblist ); 

}
