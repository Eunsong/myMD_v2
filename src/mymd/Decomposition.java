package mymd;


/**
 * A public interface providing an API for partitionning classes
 * implementing partition method, e.g. Domain-Decomposition, etc.
 *
 * @author Eunsong Choi (eunsong.choi@gmail.com)
 * @version 1.0
 */
public interface Decomposition<E extends MdSystem<?>>{

    /**
     * exportPartitions method returns partitioned particles result 
     * stored in a member variable of a Decomposition instance
     *
     * @return int array containing partitioned system information.
     *         Specific form of output array depends on the implementation.
     *         In any case, output array should contain sufficient information
     *         to update entire partitions.
     */
    /** UNNECCESSARY METHOD **/
    //public int[] exportPartitions();


    /**
     * getPartition method returns particles in partition i
     * stored in a member variable of a Decomposition instance
     *
     * @return int array containing paricle information in a partition i.
     *         Specific form of output array depends on the implementation.
     *         In any case, output array should contain sufficient information
     *         to update a partition i.
     */
    public int[] exportPartition(int i);

    
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
     */
    public void partition(E sys);


    /**
     * partiion method reads particle positions from sys and partition
     * particles based on their coordinates and store the result in
     * an instance of the class which calls the method
     *
     * @param trj Trajectory instance containing particle locations
     */
    public void partition(Trajectory trj);


}
