import mymd.*;
import mymd.datatype.*;
import mymd.gromacs.LoadGromacsSystem;
import java.util.List;
import java.util.ArrayList;
import java.io.PrintStream;

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
		
		FastLJForce<MdSystem<LJParticle>> myLJForce 
		= new FastLJForce<MdSystem<LJParticle>>(system);

		try {
			PrintStream ps = new PrintStream(outputTrajFile);
	
			for ( int i = 0; i < nsteps; i++){
				if ( i % nstlist == 0 ) nblist.update(system);
				integrator.forwardPosition(system);
				myLJForce.update(system, nblist);
				integrator.forwardVelocity(system);
				if ( i % nstxout == 0 ) mymd.MdIO.writeGro(system, ps);
				system.update();
			}
			ps.close();
		}
		catch ( java.io.IOException ex){
			
		}


		
	}
}
