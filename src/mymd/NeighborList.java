package mymd;

/**
 * NeighborList interface 
 *
 * @author Eunsong Choi (eunsong.choi@gmail.com)
 * @version 1.0
 */
public interface NeighborList<E extends MdVector>{


	public int[] getArray(int i);

	public void update(MdSystem<?> sys, E[] positions);

}
