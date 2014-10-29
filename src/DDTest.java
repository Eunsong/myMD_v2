import mymd.*;
import mymd.nonbond.*;
import mymd.datatype.*;
import mymd.gromacs.LoadGromacsSystem;
import mymd.bond.*;
import java.util.List;
import java.util.ArrayList;
import java.io.PrintStream;
import mymd.DomainNeighborList;

public class DDTest{
	public static void main(String[] args){

        String inputPrmFile = args[0];
        String inputConfFile = args[1];
        String inputTopFile = args[2];

		MdSystem<LJParticle> system =  GromacsImporter.buildLJParticleSystem
		("JOB_NAME", inputPrmFile, inputConfFile, inputTopFile);

SubSystem<LJParticle> subsystem = new SubSystem.Builder<LJParticle>().
name("test").particles(system.getParticles()).parameters(system.getParam()).
topology(system.getTopology()).subTrajectory(null).build();


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


/*
        MdVector[] positions = system.getCurrTraj().getPositions();
        int size = system.getSize(); */
        double lbox = system.getCurrTraj().getBox().getZ();
		int nPartition = 4; 
        double headDomainLength = 0.8*(lbox/nPartition);
        double otherDomainLength = (lbox - headDomainLength)/(nPartition-1);
        double[] domainBegin = new double[nPartition];
        double[] domainEnd = new double[nPartition];
        double[] bufferBegin = new double[nPartition];
        double[] bufferEnd = new double[nPartition];

        // find domain and buffer boundary locations
        domainBegin[0] = 0.0;
        domainEnd[0] = headDomainLength;
        bufferBegin[0] = headDomainLength;
        bufferEnd[0] = headDomainLength + rlist;
        for ( int n = 1; n < nPartition; n++){
            domainBegin[n] = domainEnd[n-1];
            domainEnd[n] = domainBegin[n] + otherDomainLength;
            if ( n != nPartition -1 ){
                bufferBegin[n] = domainEnd[n];
                bufferEnd[n] = bufferBegin[n] + rlist;
            }
            else {
                bufferBegin[nPartition-1] = 0.0;
                bufferEnd[nPartition-1] = rlist;
            }
        }
        // PBC correction
        for ( int n = 0; n < nPartition; n++){
            if ( bufferEnd[n] > lbox ) {
                 bufferEnd[n] -= lbox;
            }
        }

		for ( int n = 0; n < nPartition; n++){
			System.out.println(String.format("Domain[%d] : %f - %f", n, domainBegin[n], domainEnd[n]));
			System.out.println(String.format("Buffer[%d] : %f - %f", n, bufferBegin[n], bufferEnd[n]));
		}
		System.out.println(domainBegin[2]+4.0);
		System.out.println((domainBegin[2]+4.0)%lbox);



        Integrator<MdSystem<LJParticle>> integrator
		= new VelocityVerlet<MdSystem<LJParticle>>(dt);
		SimpleNeighborList<MdSystem<LJParticle>> nblist 
		= new SimpleNeighborList<MdSystem<LJParticle>>(system.getSize(), rlist);
		
		FastLJForce<MdSystem<LJParticle>> myLJForce 
		= new FastLJForce<MdSystem<LJParticle>>(system);

system.forwardPosition(integrator);
DomainDecomposition<MdSystem<LJParticle>> dd = new DomainDecomposition<MdSystem<LJParticle>>(system, 4);
dd.partition(system);
Domain d = dd.getDomain(0);
DomainNeighborList<MdSystem<LJParticle>> nb = new DomainNeighborList<MdSystem<LJParticle>>(400, 1.4, d);
nb.update(system);


			for ( int i = 0; i < nsteps; i++){
				if ( i % 10 == 0 ) System.out.println(String.format("computing %5.1fps", i*dt));
				if ( i % nstlist == 0 ) nblist.update(system);
				system.forwardPosition(integrator);
				system.updateNonBondedForce(myLJForce, nblist);
				system.updateBondForce();
				system.forwardVelocity(integrator);
				system.update();
			}


		
	}
}
