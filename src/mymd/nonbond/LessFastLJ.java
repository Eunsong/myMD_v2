package mymd.nonbond;

import mymd.*;
import mymd.datatype.LJParticle;
import mymd.datatype.LJParticleType;
import mymd.datatype.MdVector;
import java.util.List;
import java.util.ArrayList;
import java.lang.ArrayIndexOutOfBoundsException;

/**
 * LessFastLJ class implements NonBond interface for computing Lennard-Jones 
 * forces and energies using lookup tables for a faster computation speed.
 * The difference from the FastLJ class is that LessFastLJ creates only four
 * lookup tables regardless of number of distinct particle types. This may
 * be useful for systems with many distinct particle types.
 *
 * @author Eunsong Choi (eunsong.choi@gmail.com)
 * @version 1.0
 */
public class LessFastLJ<T extends MdSystem<LJParticle>> implements NonBond<T>{

    private final double rc;
    private final int size;
    private final RnTable R6Table, R12Table, R8Table, R14Table;

    public LessFastLJ(T sys){
        rc = sys.getParam().getRvdw();
        size = sys.getSize();

        this.R6Table = new RnTable(6, rc);
        this.R12Table = new RnTable(12, rc);
        this.R8Table = new RnTable(8, rc);
        this.R14Table = new RnTable(14, rc);
    }


    private double getForceTable(double C6, double C12, double r){
            return (-6.0*C6*R8Table.get(r) + 12.0*C12*R14Table.get(r));
    }
    private double getEnergyTable(double C6, double C12, double r){
        return (-C6*R6Table.get(r) + C12*R12Table.get(r));
    }

    /**
     * computes a LJ force vector acting on the particle i due to j 
     * using the particle positions stored in the newTraj in sys
     *
     * @param sys 
     * @param i index number of particle i
     * @param j index number of particle j
     * @return MdVector object representing a force vector acting on
     *         the particle i due to the particle j
     */
    public MdVector get( T sys, int i, int j){
        Trajectory traj = sys.getNewTraj();
        MdVector box = traj.getBox();
        MdVector Ri = traj.getPosition(i);
        MdVector Rj = traj.getPosition(j);
        MdVector Rij = new MdVector();
        Rij.copy(Ri).sub(Rj);
        Rij.minImage(box);
 
        double r = Rij.norm();
        if ( r < rc ){
            double C6i = sys.getParticle(i).getC6();
            double C12i = sys.getParticle(i).getC12();
            double C6j = sys.getParticle(j).getC6();
            double C12j = sys.getParticle(j).getC12();
            double C6ij = combrule(C6i, C6j);
            double C12ij = combrule(C12i, C12j);
    
            return MdVector.times(Rij, getForceTable(C6ij, C12ij, r));
        }
        return new MdVector();
    }


    /**
     * computes LJ force vectors of all partifcles in sys due to 
     * their neighbors specified in the given nblist and add computed
     * forces to their new force component. 
     *
     * @param sys 
     * @param nblist a NeighborList object containing index of neighboring
     *               particles(partjcle j) of each particle(i). Note that
     *               this method assumes the size of nblist and trajectories
     *               in sys are the same and they are written in the same order
     */
    public void updateAllForces( T sys, NeighborList nblist ){

        Trajectory traj = sys.getNewTraj(); 
        MdVector box = traj.getBox();
        MdVector Rij = new MdVector();
        MdVector F = new MdVector();
        for ( int i = 0; i < nblist.getSize(); i++){
            int iActual = nblist.get(i);
            int[] sublist = nblist.getArray(i);
            MdVector Ri = traj.getPosition(iActual);
            double C6i = sys.getParticle(iActual).getC6();
            double C12i = sys.getParticle(iActual).getC12();

            for ( int j = 0; j < nblist.getSize(i); j++){ 
                int jActual = sublist[j];
                double C6j = sys.getParticle(jActual).getC6();
                double C12j = sys.getParticle(jActual).getC12();
                double C6ij = combrule(C6i, C6j);
                double C12ij = combrule(C12i, C12j);
                MdVector Rj = traj.getPosition(jActual);
                Rij.copy(Ri).sub(Rj); // Rij = Ri - Rj
                Rij.minImage(box);
                double r = Rij.norm();
                if ( r < rc ){
                    F.copy(Rij).timesSet( getForceTable(C6ij, C12ij, r) );
                    traj.addForce(iActual, F);
                    traj.addReactionForce(jActual, F);
                }
            }
        }
    }


    public double getTotalEnergy(T sys, NeighborList nblist){

        Trajectory traj = sys.getNewTraj(); 
        MdVector box = traj.getBox();
        MdVector Rij = new MdVector();
        double energy = 0.0;
        for ( int i = 0; i < nblist.getSize(); i++){
            int iActual = nblist.get(i);
            int[] sublist = nblist.getArray(i);
            MdVector Ri = traj.getPosition(iActual);
            double C6i = sys.getParticle(iActual).getC6();
            double C12i = sys.getParticle(iActual).getC12();

            for ( int j = 0; j < nblist.getSize(i); j++){ 
                int jActual = sublist[j];
                double C6j = sys.getParticle(jActual).getC6();
                double C12j = sys.getParticle(jActual).getC12();
                double C6ij = combrule(C6i, C6j);
                double C12ij = combrule(C12i, C12j);
                MdVector Rj = traj.getPosition(jActual);
                Rij.copy(Ri).sub(Rj); // Rij = Ri - Rj
                Rij.minImage(box);
                double r = Rij.norm();
                if ( r < rc ){
                    energy += getEnergyTable(C6ij, C12ij, r);
                }
            }
        }
        return energy;
    }

    private double combrule(double Ci, double Cj){
        return Math.sqrt(Ci*Cj);
    } 


}
