package mymd;

import mymd.datatype.*;
import java.io.PrintStream;


/**
 * MdIO class provides static methods that can be used for file I/O 
 * while running simulations.
 *
 * @author Eunsong Choi(eunsong.choi@gmail.com)
 * @version 1.0
 */
public class MdIO{

    public static <T extends MdSystem<?>> void writeGro(T sys, PrintStream ps){
        String jobName = sys.getName();
        double time = sys.getTime();
        int N = sys.getSize();
        ps.println(String.format("Generated from mymd package : %s t=%5.5f", jobName, time ));
        ps.println(String.format(" %d", N));
        MdVector[] positions = sys.getCurrTraj().getPositions();
        MdVector[] velocities = sys.getCurrTraj().getVelocities();
        MdVector box = sys.getBox();
        
        for ( int i = 0; i < N; i++ ) {
            Particle particle = sys.getParticle(i);
            ps.println( String.format("%5d%-5s%5s%5d%8.3f%8.3f%8.3f%8.4f%8.4f%8.4f",
                    particle.getResidueNumber()+1, particle.getResidueName(),
                    particle.getName(), particle.getNumber() + 1, 
                    positions[i].getX(), positions[i].getY(), positions[i].getZ(),
                    velocities[i].getX(), velocities[i].getY(), velocities[i].getZ() ));
        }

        ps.println( String.format( "%4.5f  %4.5f  %4.5f", 
                        box.getX(), box.getY(), box.getZ() ));

    }


    /**
     * printEnergy method prints out energy components computed from 
     * new Trajectory(should be invoked after updating all forces and velocities
     * and before calling update() method in MdSystem. Currently, this method
     * assumes all bonded type potentials are computed from the head node. 
     * Non-bonded and Coulomb interactions should be computed prior to calling  
     * this method, and results should be passed as input parameters. This way
     * we can make use of slave nodes for computing nonbonded potentials. 
     *
     * @param sys 
     * @param nonbondEnergy total non-bonded potential energy computed in advance
     * @param coulombEnergy total coulomb potential energy computed in advance
     * @param ps PrintStream object where output energies should be written
     */
    public static <T extends MdSystem<?>> void printEnergy(T sys, 
                  double nonbondEnergy, double coulombEnergy, PrintStream ps){

        double time = sys.getNewTraj().getTime();
        double bondEnergy = sys.getBondEnergy();
        double angleEnergy = sys.getAngleEnergy();
        double dihedralEnergy = sys.getDihedralEnergy();
        double kineticEnergy = sys.getKineticEnergy();
    
        ps.print( printFormattedEnergy(time, nonbondEnergy, coulombEnergy, 
                  bondEnergy, angleEnergy, dihedralEnergy, kineticEnergy));

    }

    private static String printFormattedEnergy
        (double time, double nonbond, double coulomb, double bond, double angle, 
         double dihedral, double kinetic){

        double potential = nonbond + coulomb + bond + angle + dihedral;
        double total = potential + kinetic;
        return String.format("%6.2f %1.6e %1.6e %1.6e %1.6e %1.6e %1.6e %1.6e %1.6e\n", 
               time, nonbond, coulomb, bond, angle, dihedral, kinetic, potential, total);
    }


}
