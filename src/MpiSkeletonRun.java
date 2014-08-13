import mymd.*;
import mymd.nonbond.*;
import mymd.datatype.*;
import mymd.gromacs.LoadGromacsSystem;
import mymd.bond.*;
import java.util.List;
import java.util.ArrayList;
import java.io.PrintStream;

import mpi.*;

public class MpiSkeletonRun{
	public static void main(String[] args){

        String inputPrmFile = args[3];
        String inputConfFile = args[4];
        String inputTopFile = args[5];
        String outputTrajFile = args[6];
        String outputEnergyFile = args[7];

		MdSystem<LJParticle> system =  GromacsImporter.buildLJParticleSystem
		("JOB_NAME", inputPrmFile, inputConfFile, inputTopFile);

SubSystem<LJParticle> subsystem = new SubSystem.Builder<LJParticle>().
name("test").particles(system.getParticles()).parameters(system.getParam()).
topology(system.getTopology()).subTrajectory(null).build();



        /** MPI preparation **/
        MPI.Init(args);
        final int rank = MPI.COMM_WORLD.Rank();
        final int np = MPI.COMM_WORLD.Size();
		

		MdParameter prm = system.getParam();

        final double dt = prm.getDt();
		final double rlist = prm.getRlist();
        final int nsteps = prm.getNsteps();
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
		
		FastLJForce<MdSystem<LJParticle>> myLJForce 
		= new FastLJForce<MdSystem<LJParticle>>(system);

		try {
			PrintStream ps = new PrintStream(outputTrajFile);
	
			for ( int i = 0; i < nsteps; i++){
				if ( i % 10 == 0 ) System.out.println(String.format("computing %5.1fps", i*dt));
				if ( i % nstlist == 0 ) nblist.update(system);
				system.forwardPosition(integrator);
				system.updateNonBondedForce(myLJForce, nblist);
				system.updateBondForce();
				system.forwardVelocity(integrator);
				if ( i % nstxout == 0 ) mymd.MdIO.writeGro(system, ps);
				system.update();
			}
			ps.close();
		}
		catch ( java.io.IOException ex){
			
		}



        MPI.Finalize();
		
	}
}
