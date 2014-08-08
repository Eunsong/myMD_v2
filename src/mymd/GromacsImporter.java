package mymd;

import mymd.*;
import mymd.datatype.*;
import mymd.gromacs.LoadGromacsSystem;
import java.util.List;
import java.util.ArrayList;

public class GromacsImporter{


        /** 
         * a static method to construct a MdSystem object for LJParticles
         * by importing gromacs type input files (gro file and top file)
         *
		 * @param prm prm file containing simulation run parameters 
         * @param confFile an initial configuration file in gromacs gro format
         * @param topFile gromacs top file containing system topology info
         * @return A new MdSystem<LJParticle> object constructed using gromacs
		 *		   type inputs containing initial trajectory and basic topologies 
         */
        public static MdSystem<LJParticle> buildLJParticleSystem
             (String jobName, String prmFile, String confFile, String topFile){
            mymd.gromacs.LoadGromacsSystem loader =
            new mymd.gromacs.LoadGromacsSystem( confFile, topFile );
			loader.build();
            int size = loader.getSize();
            List<LJParticle> particles = new ArrayList<LJParticle>(size);
            for ( int i = 0; i < size; i++){
                LJParticle particle = new LJParticle.Builder(i).
                name(loader.getParticleName(i)).type(loader.getParticleType(i)).
                residueName(loader.getParticleResidueName(i)).
                residueNumber(loader.getParticleResidueNumber(i)).
                typeNumber(loader.getParticleTypeNumber(i)).mass(loader.getParticleMass(i)).
                C6(loader.getParticleC6(i)).C12(loader.getParticleC12(i)).build();
                particles.add(particle);
            }
            Trajectory trj = new Trajectory(size);
	        trj.setBox( loader.getBoxArray()[0] );
    	    trj.setTime(0.0);
        	for ( int i = 0; i < size; i++){
            	double x,y,z;
	            x = loader.getPositionArray(i)[0];
    	        y = loader.getPositionArray(i)[1];
        	    z = loader.getPositionArray(i)[2];
            	trj.setPosition(i, new MdVector(x,y,z));
	        }
			MdParameter prm = new MdParameter();
			prm.importFromFile(prmFile);
			
			Topology top = new Topology();
		
			return new MdSystem.Builder<LJParticle>(jobName).particles(particles).
			parameters(prm).topology(top).initialTrajectory(trj).build();

        }



}
