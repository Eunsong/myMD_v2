package mymd;

import mymd.datatype.*;

/**
 * SimpleNeighborList class implements update() method in AbstractNeighborList
 * class using a simple brute force calculation. This class is intended for
 * testing purpose, and is inefficient for any practical simulations.
 *
 * @author Eunsong Choi (eunsong.choi@gmail.com)
 * @version 1.0
 */
public class SimpleNeighborList<T extends MdSystem<?>> 
     extends AbstractNeighborList<T> implements NeighborList<T>{


    public SimpleNeighborList(int numberOfParticles, double rc){
        super(numberOfParticles, rc);
    }

    /**
     * update(T sys) method uses new positions in sys and invokes
     * update(T sys, MdVector[] positions, MdVector box) method 
     *
     * @param sys
     */
    public void update(T sys){
        MdVector[] positions = sys.getNewTraj().getPositions();
        MdVector box = sys.getBox();
        update(sys, positions, box);
    }


    /**
     * update(T sys, MdVector[] positions, MdVector box) method finds neighbor 
     * lists using brute force distance calculation through all O(n^2) pairs
     *
     * @param sys MdSystem object containing system backbone information
     * @param positions MdVector array containing position of particles
     * @param box system box dimension
     */
    public void update(T sys, MdVector[] positions, MdVector box){

        int size = super.getSize();
        double rc = super.rc;
        double rc2 = rc*rc;
        MdVector Rij = new MdVector();
        for ( int i = 0; i < size; i++){
            super.nblist.get(i).reset();
            super.nblist.get(i).seti(i);
            for ( int j = i+1; j < size; j++){
                MdVector Ri = positions[i];
                MdVector Rj = positions[j];
                Rij.copy(Ri).sub(Rj).done();
                Rij.minImage(box);
                double rsq = Rij.normsq();
                if ( rsq < rc2 ){
                    super.nblist.get(i).add(j);
                }               
            }
        }
    }



}
