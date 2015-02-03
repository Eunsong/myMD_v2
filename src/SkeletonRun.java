import mymd.*;
import mymd.nonbond.*;
import mymd.datatype.*;
import mymd.gromacs.LoadGromacsSystem;
import mymd.bond.*;
import java.util.List;
import java.util.ArrayList;
import java.io.PrintStream;

/**
 * Runs a generic MD simulation using the myMD module. This version works on
 * a single core. Use MpiSkeletonRun for a parallel computing version. 
 * Simulation settings should be specified
 * in an input prm file but standard GROMACS-like files can be used for force-fields.
 *
 * @author Eunsong Choi (eunsong.choi@gmail.com)
 * @version 1.0
 */
public class SkeletonRun{
    public static void main(String[] args){

        String inputPrmFile = args[0];
        String inputConfFile = args[1];
        String inputTopFile = args[2];
        String outputTrajFile = args[3];
        String outputEnergyFile = args[4];

        MdSystem<LJParticle> system =  GromacsImporter.buildLJParticleSystem
        ("JOB_NAME", inputPrmFile, inputConfFile, inputTopFile);

        MdParameter prm = system.getParam();

        final double dt = prm.getDt();
        final int nsteps = prm.getNsteps();
        final double rlist = prm.getRlist();
        final int nstlist = prm.getNstlist();
        final int nstxout = prm.getNstxout();
        final int nstvout = prm.getNstvout();
        final int nstenergy = prm.getNstenergy();
        final double T0 = prm.getT0();

        final boolean convertHbonds = prm.convertHbonds();

        Integrator<MdSystem<LJParticle>> integrator
        = new VelocityVerlet<MdSystem<LJParticle>>(dt);
        SimpleNeighborList<MdSystem<LJParticle>> nblist 
        = new SimpleNeighborList<MdSystem<LJParticle>>(system.getSize(), rlist);
        
        FastLJ<MdSystem<LJParticle>> nonbond 
        = new FastLJ<MdSystem<LJParticle>>(system);

        try {
            PrintStream ps = new PrintStream(outputTrajFile);
            PrintStream psEnergy = new PrintStream(outputEnergyFile);
    
            for ( int i = 0; i < nsteps; i++){
                if ( i % 10 == 0 ) System.out.println(String.format("computing %5.1fps", i*dt));
                if ( i % nstlist == 0 ) nblist.update(system);
                system.forwardPosition(integrator);
                system.updateNonBondForce(nonbond, nblist);
                system.updateBondForce();
                system.forwardVelocity(integrator);
                if ( i % nstxout == 0 ) mymd.MdIO.writeGro(system, ps);
                if ( i % nstenergy == 0 ) {
                    double nonbondEnergy;
                    nonbondEnergy = system.getNonBondEnergy(nonbond, nblist);
                    double coulombEnergy;
                    coulombEnergy = 0.0; //temporary
                    mymd.MdIO.printEnergy(system, nonbondEnergy, coulombEnergy, psEnergy);
                }
                system.update();
            }
            ps.close();
            psEnergy.close();
        }
        catch ( java.io.IOException ex){
            
        }
    }
}
