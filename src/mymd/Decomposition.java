package mymd;


/**
 * A public interface providing an API for partitionning classes
 * implementing partition method, e.g. Domain-Decomposition, etc.
 *
 * @author Eunsong Choi (eunsong.choi@gmail.com)
 * @version 1.0
 */
public interface Decomposition<E extends MdVector>{

	/**
	 * getPartitions method returns partitioned particles result 
	 * stored in a member variable of a Decomposition instance
	 *
	 * @return List of Lits containing integers indicating 
	 *         particle identification numbers. For example, 
	 *         The first element of the list is a list of particle
	 *         id numbers that are members of the first partition
	 */
	public List<List<Integer>> getPartitions();


	/**
	 * getPartition method returns particles in partition i
	 * stored in a member variable of a Decomposition instance
	 *
	 * @return List containing integers indicating 
	 *         particle identification numbers in partition i. 
	 */
	public List<Integer> getPartition(int i);

	
	/**
	 * getNP method returns the number of partitions
	 * typically equal to the number of nodes or processors
	 *
	 * @return number of partitions 
	 */
	public int getNP();
	
	/**
	 * partiion method reads particle positions from sys and partition
	 * particles based on their coordinates and store the result in
	 * an instance of the class which calls the method
	 *
	 * @param sys MdSystem instance containing current particle locations
	 * @param np Number of partitions (typically equal to the number of nodes)
	 */
	public void partiion(MdSystem<E> sys);

}
