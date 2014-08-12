package mymd;

import mymd.*;
import mymd.datatype.*;
import mymd.gromacs.LoadGromacsSystem;
import mymd.bond.*;
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

			// build particle info
			List<LJParticleType> types = new ArrayList<LJParticleType>();
            List<LJParticle> particles = new ArrayList<LJParticle>(size);
            for ( int i = 0; i < size; i++){

				// build particle type 
				String typeName = loader.getParticleType(i);
				int typeNumber = loader.getParticleTypeNumber(i);
				double mass = loader.getParticleMass(i);
				double C6 = loader.getParticleC6(i);
				double C12 = loader.getParticleC12(i);
				double charge = loader.getParticleCharge(i);
				LJParticleType ljType = new LJParticleType.Builder(typeName).
				number(typeNumber).mass(mass).charge(charge).C6(C6).C12(C12).build();

				int index = types.indexOf(ljType);
				if ( index == -1 ){
					types.add(ljType);
				}
				else {
					ljType = types.get(index);
				}

                LJParticle particle = new LJParticle.Builder(i, ljType).
                name(loader.getParticleName(i)).
                residueName(loader.getParticleResidueName(i)).
                residueNumber(loader.getParticleResidueNumber(i)).mass(mass).
                C6(C6).C12(C12).build();
                particles.add(particle);
            }

			// build initial trajectory
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

			// read simulatin parameters from input prm file
			MdParameter prm = new MdParameter();
			prm.importFromFile(prmFile);

			// build Topology object
			Bonds<MdSystem<LJParticle>> bonds = new Bonds<MdSystem<LJParticle>>();
			for ( int k = 0; k < loader.getBondSize(); k++){
				int i = loader.getBondi(k);
				int j = loader.getBondj(k);
				double k0 = loader.getBondk0(k);
				double b0 = loader.getBondb0(k);
				int func = loader.getBondFunc(k);
				if ( func == 1 ){
					Bond<MdSystem<LJParticle>> bond = 
					new HarmonicBond<MdSystem<LJParticle>>(i, j, k0, b0);
					bonds.add(bond);
				}
				else throw new IllegalArgumentException("bond type other than 1 is not supported yet");
			}

			Topology<MdSystem<LJParticle>> top = 
			new Topology.Builder<MdSystem<LJParticle>>().bonds(bonds).build();	
	
			return new MdSystem.Builder<LJParticle>(jobName).particles(particles).
			parameters(prm).topology(top).initialTrajectory(trj).verbose().build();

        }



}
