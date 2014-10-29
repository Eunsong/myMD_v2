package mymd;

import mymd.datatype.*;

/**
 * NeighborList interface 
 *
 * @author Eunsong Choi (eunsong.choi@gmail.com)
 * @version 1.0
 */
public interface NeighborList<T extends MdSystem<?>>{


    public int get(int i);

    public int[] getArray(int i);


    /**
     * getSize() method returns the number of particles holding its neighbor list
     *
     * @return the number of sublists (i.e. number of particle i)
     */
    public int getSize();


    /**
     * getSize(int i) method returns the number of neighbors of ith particle.
     * When treversing through the sublist arrays, one should make sure that
     * the iteration does not go beyond the size of each sublist array using
     * returned value of this method.
     *
     * @param i particle i 
     * @return the number of neighboring particles of the particle i
     */
    public int getSize(int i);

    public void update(T sys);

    public void update(T sys, MdVector[] positions, MdVector box);

}
