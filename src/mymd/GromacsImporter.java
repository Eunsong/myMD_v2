package mymd;

import mymd.*;
import mymd.datatype.*;
import mymd.gromacs.LoadGromacsSystem;
import mymd.bond.*;
import mymd.angle.*;
import mymd.dihedral.*;
import mymd.constraint.*;
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
         *         type inputs containing initial trajectory and basic topologies 
         */
        public static MdSystem<LJParticle> buildLJParticleSystem
             (String jobName, String prmFile, String confFile, String topFile){

            mymd.gromacs.LoadGromacsSystem loader =
            new mymd.gromacs.LoadGromacsSystem( confFile, topFile );
            loader.build();
            int size = loader.getSize();

            // read simulatin parameters from input prm file
            MdParameter prm = new MdParameter();
            prm.importFromFile(prmFile);
            String LJInputType = prm.getLJInputType();

            // build particle info
            List<LJParticleType> types = new ArrayList<LJParticleType>();
            List<LJParticle> particles = new ArrayList<LJParticle>(size);
            
            List<MoleculeType> molecules = new ArrayList<MoleculeType>();

            for ( int i = 0; i < size; i++){

                // build particle type 
                String typeName = loader.getParticleType(i);
                int typeNumber = loader.getParticleTypeNumber(i);
                double mass = loader.getParticleMass(i);
                double C6 = loader.getParticleC6(i);
                double C12 = loader.getParticleC12(i);
                if ( LJInputType.equals("sigma-epsilon") ){
                    double sigma = C6;
                    double epsilon = C12;
                    C6 = 4.0*epsilon*Math.pow(sigma,6);
                    C12 = 4.0*epsilon*Math.pow(sigma,12);
                }
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

                String moleName = loader.getParticleMoleculeName(i);
                int moleNumber = loader.getParticleMoleculeNumber(i);
                int moleTypeNumber = loader.getParticleMoleculeTypeNumber(i);
                int numberOfParticles = loader.getParticleMoleculeNAtoms(i);
                int nrexcl = loader.getParticleMoleculeNrexcl(i);
                boolean[][] exclusions = loader.getParticleExclusions(i);

                MoleculeType moleType = new MoleculeType.Builder(moleName).
                exclusions(exclusions).numberOfParticles(numberOfParticles).
                nrexcl(nrexcl).build();
                if ( !molecules.contains(moleType) ){
                    molecules.add(moleType);
                }
                else {
                    index = molecules.indexOf(moleType);
                    moleType = molecules.get(index);
                }
                
                LJParticle particle = new LJParticle.Builder(i, ljType).
                name(loader.getParticleName(i)).id(loader.getParticleId(i)).
                moleculeNumber(moleNumber).moleculeType(moleType).
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
            Angles<MdSystem<LJParticle>> angles = new Angles<MdSystem<LJParticle>>();
            for ( int n = 0; n < loader.getAngleSize(); n++){
                int i = loader.getAnglei(n);
                int j = loader.getAnglej(n);
                int k = loader.getAnglek(n);
                double k0 = loader.getAnglek0(n);
                double theta0 = loader.getAngleTheta0(n);
                int func = loader.getAngleFunc(n);
                if ( func == 1 ){
                    Angle<MdSystem<LJParticle>> angle = 
                    new HarmonicAngle<MdSystem<LJParticle>>(i, j, k, k0, theta0);
                    angles.add(angle);
                }
                else throw new IllegalArgumentException("angle type other than 1 is not supported yet");
            }

            Dihedrals<MdSystem<LJParticle>> dihedrals = new Dihedrals<MdSystem<LJParticle>>();
            for ( int n = 0; n < loader.getDihedralSize(); n++){
                int i = loader.getDihedrali(n);
                int j = loader.getDihedralj(n);
                int k = loader.getDihedralk(n);
                int l = loader.getDihedrall(n);
                int func = loader.getDihedralFunc(n);

                if ( func == 1 ){
                    double k0 = loader.getDihedralk0(n);
                    int ni = loader.getDihedraln(n);
                    double phi0 = loader.getDihedralPhi0(n);
                    Dihedral<MdSystem<LJParticle>> dihedral = 
                    new StandardDihedral<MdSystem<LJParticle>>(i, j, k, l, k0, ni, phi0);
                    dihedrals.add(dihedral);
                }
                else if ( func == 3 ){

                    double[] C = loader.getDihedralC(n);
                    Dihedral<MdSystem<LJParticle>> dihedral = 
                    new RBDihedral<MdSystem<LJParticle>>(i, j, k, l, C);
                    dihedrals.add(dihedral);

                }
                else throw new IllegalArgumentException("dihedral type other than 1 or 3 are not supported yet");
            }


            Constraints<MdSystem<LJParticle>> constraints = new Constraints<MdSystem<LJParticle>>();
            for ( int n = 0; n < loader.getConstraintSize(); n++){
                int i = loader.getConstrainti(n);
                int j = loader.getConstraintj(n);
                double d0 = loader.getConstraintd0(n);
                Constraint<MdSystem<LJParticle>> constraint = 
                new SimpleShake<MdSystem<LJParticle>>(i, j, d0);
                constraints.add(constraint);
            }


    
            Topology<MdSystem<LJParticle>> top = 
            new Topology.Builder<MdSystem<LJParticle>>().bonds(bonds).angles(angles).dihedrals(dihedrals).constraints(constraints).build(); 
    
            return new MdSystem.Builder<LJParticle>(jobName).particles(particles).
            parameters(prm).topology(top).initialTrajectory(trj).verbose().build();

        }



}
