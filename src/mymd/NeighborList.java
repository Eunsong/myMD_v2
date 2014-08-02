package mymd;

/**
 * NeighborList interface 
 *
 * @author Eunsong Choi (eunsong.choi@gmail.com)
 * @version 1.0
 */
public interface NeighborList<E extends MdVector, T extends MdSystem<?, ?>>{


	public int[] getArray(int i);

	public int getSize();

	public void update(T sys, E[] positions);

}
