import mymd.*;
import mymd.nonbond.*;
import mymd.datatype.*;
import mymd.gromacs.LoadGromacsSystem;
import mymd.bond.*;
import mymd.thermostat.*;
import mymd.its.*;
import java.util.List;
import java.util.ArrayList;
import java.io.PrintStream;

import java.math.BigDecimal;
import java.math.MathContext;

import mpi.*;

public class MpiITSRun{
    public static void main(String[] args){

        String inputPrmFile = args[3];
        String inputConfFile = args[4];
        String inputTopFile = args[5];
        String outputTrajFile = args[6];
        String outputEnergyFile = args[7];
        String ITSout = args[8];

        MdSystem<LJParticle> system =  GromacsImporter.buildLJParticleSystem
        ("JOB_NAME", inputPrmFile, inputConfFile, inputTopFile);

        MdParameter prm = system.getParam();

        final double dt = prm.getDt();
        final int nsteps = prm.getNsteps();
        final int nstlist = prm.getNstlist();
        final int nstxout = prm.getNstxout();
        final int nstvout = prm.getNstvout();
        final int nstenergy = prm.getNstenergy();
        final int nstlog = 10; //prm.getNstlog();
        final double T0 = prm.getT0();
        final double TRef = prm.getRefT();
        final double tauT = prm.getTauT();
        final boolean convertHbonds = prm.convertHbonds();
        if ( convertHbonds ){
            system.convertHBondsToConstraints();
        }



        /************** ITS setup ****************/
        final int ksize = 12;
        final double Tstep = 25.0; // T increment
        final int ITSEnergyFreq = nstxout; // frequency of energy storing for ITS

        final double[] Temps = new double[ksize];
        final double[] p0 = new double[ksize];
    
        for ( int i = 0; i < ksize; i++){
            Temps[i] = T0 + i*Tstep;
            p0[i] = 1.0/ksize;
        }
        /****************************************/





final BigDecimal[] n0 = new BigDecimal[ksize];
MathContext mathset = new MathContext(5);
n0[0] = new BigDecimal("5.99880e-03", mathset);
n0[1] = new BigDecimal("3.64660e+209", mathset);
n0[2] = new BigDecimal("1.23850e+391", mathset);
n0[3] = new BigDecimal("2.59790e+548", mathset);
n0[4] = new BigDecimal("1.21530e+686", mathset);
n0[5] = new BigDecimal("2.85080e+807", mathset);
n0[6] = new BigDecimal("2.93170e+915", mathset);
n0[7] = new BigDecimal("1.33400e+1012", mathset);
n0[8] = new BigDecimal("1.18050e+1099", mathset);
n0[9] = new BigDecimal("4.27230e+1177", mathset);
n0[10] = new BigDecimal("1.70190e+1249", mathset);
n0[11] = new BigDecimal("2.87210e+1314", mathset);




        // gen valocity
        system.genRandomVelocities(T0);


        /** MPI preparation **/
        MPI.Init(args);
        final int rank = MPI.COMM_WORLD.Rank();
        final int np = MPI.COMM_WORLD.Size();
        

        Integrator<MdSystem<LJParticle>> integrator
        = new VelocityVerlet<MdSystem<LJParticle>>(dt);
        
        FastLJC<MdSystem<LJParticle>> nonbond 
        = new FastLJC<MdSystem<LJParticle>>(system);

        DomainDecomposition<MdSystem<LJParticle>> decomposition
        = new DomainDecomposition<MdSystem<LJParticle>>(system, np);

        Thermostat<MdSystem<LJParticle>> thermostat 
        = new BerendsenThermostat<MdSystem<LJParticle>>(TRef, tauT);


        // push initial positions to the new trajectory
        system.forwardPosition(integrator);

        // get partitions using new positions
        system.partition(decomposition);

        Domain domain = decomposition.getDomain(rank);

        DomainNeighborList<MdSystem<LJParticle>> nblist 
        = new DomainNeighborList<MdSystem<LJParticle>>(system, domain);

        int SUB_CAPACITY = domain.getCapacity();

        // head node
        if ( rank == 0 ){
    
            try {
                PrintStream ps = new PrintStream(outputTrajFile);
                PrintStream psEnergy = new PrintStream(outputEnergyFile);
                PrintStream psITS = new PrintStream(ITSout);

                ITS<MdSystem<LJParticle>> its = new ITS<MdSystem<LJParticle>>(T0, ksize, Temps, p0, n0);
                double Uorg = 0.0;

                for ( int tstep = 0; tstep < nsteps; tstep++){
                
                    if ( tstep%nstlog == 0 ) {
                        System.out.println(String.format(
                        "Computing t = %5.3f ps", tstep*dt));
                    }

                    // integrate forward (apply position constraints if applicable)
                    if ( tstep != 0 ){
                        system.forwardPosition(integrator);
                        system.applyPositionConstraint();
                    }



                    // (I) update domains and send them to slave nodes
                    if ( tstep%nstlist == 0 ){
                        // updates partitions using new positions
                        system.partition(decomposition);    

                        // send updated partition info to slave nodes
                        for ( int proc = 1; proc < np; proc++){
                            int[] partition = decomposition.exportPartition(proc);
                            MPI.COMM_WORLD.
                            Send( partition, 0, SUB_CAPACITY, MPI.INT, proc, 99); 
                        }
                        // update local neighbor list
                        nblist.update(system);
                    }


                    // (II) export new positions to slave nodes
                    for ( int proc = 1; proc < np; proc++){
                        Domain domainEach = decomposition.getDomain(proc);
                        double[] positionArray = system.exportNewPositions(domainEach);

                        MPI.COMM_WORLD.
                        Send( positionArray, 0, 3*SUB_CAPACITY, MPI.DOUBLE, proc, 99); 
                    }




                    // (ITS Step 0 ) compute original energy
                    {

                            double nonbondEnergy = 0.0;
                            nonbondEnergy = system.getNonBondEnergy(nonbond, nblist);
                            // receive partial nonbond energies from slave-nodes and add
                            for ( int proc = 1; proc < np; proc++){
                                double[] partialEnergy = new double[1];
    
                                MPI.COMM_WORLD.
                                Recv( partialEnergy, 0, 1, MPI.DOUBLE, proc, 99); 
                                nonbondEnergy += partialEnergy[0];
                            }                                               
                            double coulombEnergy = 0.0; // temporary.
                            double bond = system.getBondEnergy();
                            double angle = system.getAngleEnergy();
                            double dihedral = system.getDihedralEnergy();
                            Uorg = nonbondEnergy + bond + angle + dihedral;

                        if ( tstep % ITSEnergyFreq == 0 ){
                            its.printEnergy(psITS, system, Uorg);
                        }
                    }   



                    // update non-bonded forces
                    system.updateNonBondForce( nonbond, nblist);            

                    // update long-ranged forces                    

                    // update bonded forces
                    system.updateBondForce();
                    
                    // update angle forces
                    system.updateAngleForce();

                    // update dihedral forces
                    system.updateDihedralForce();

                    // (III) receive computed forces from slaves
                    for ( int proc = 1; proc < np; proc++){
                        double[] forceArray = new double[3*SUB_CAPACITY]; 

                        MPI.COMM_WORLD.
                        Recv( forceArray, 0, 3*SUB_CAPACITY, MPI.DOUBLE, proc, 99); 
                    
                        Domain domainEach = decomposition.getDomain(proc);
                        system.importNewForces(domainEach, forceArray);
                    }


                    // (ITS step 1 ) Apply biasing forces
                    its.applyBiasForce(system, Uorg );


                    // forward velocities
                    system.forwardVelocity(integrator);
                    // apply velocity constraints
                    system.correctConstraintVelocity();

                    // apply temperature coupling
                    thermostat.apply(system);


                    // print energy (using information in newTraj )
                    if ( tstep % nstenergy == 0 ){
                        double nonbondEnergy = 0.0;
                        nonbondEnergy = system.getNonBondEnergy(nonbond, nblist);
                        // receive partial nonbond energies from slave-nodes and add
                        for ( int proc = 1; proc < np; proc++){
                            double[] partialEnergy = new double[1];

                            MPI.COMM_WORLD.
                            Recv( partialEnergy, 0, 1, MPI.DOUBLE, proc, 99); 
                            nonbondEnergy += partialEnergy[0];
                        }                                               

                        double coulombEnergy = 0.0; // temporary.
                        mymd.MdIO.printEnergy
                        (system, nonbondEnergy, coulombEnergy, psEnergy);   
                    }   


                    // update current trajectories from new trajectories
                    system.update();
    
                    if ( tstep%nstxout == 0 ){
                        mymd.MdIO.writeGro(system, ps);
                    }
                
                }
                
                
                ps.close();
                psEnergy.close();
            }
                
            catch ( java.io.IOException ex){


            }


        }
        // slave nodes
        else{
        
            decomposition = null;
            String slaveName = String.format("slave-%d", rank);


            /*** change subSystem constructor so it accepts a MdSystem object
                 as an input parameter. SubSystem is needed only if MdSystem object
                 exists. **/
            // create a sub-system for slave node
            Trajectory subTraj = new Trajectory(system.getSize());
            subTraj.setBox( system.getBox());
            SubSystem<LJParticle> subsystem = new SubSystem.Builder<LJParticle>().
            name(slaveName).particles(system.getParticles()).
            parameters(system.getParam()).topology(system.getTopology()).
            subTrajectory(subTraj).build();

            // mother system is freed. no longer needed for slave nodes
            system = null;

            for ( int tstep = 0; tstep < nsteps; tstep++){

                // (I) receive updated partition info from head node
                if ( tstep%nstlist == 0 ){
                    int[] partition = new int[SUB_CAPACITY];
                    MPI.COMM_WORLD.
                    Recv( partition, 0, SUB_CAPACITY, MPI.INT, 0, 99); 
                    
                    // import received array to its local domain
                    domain.importArray( partition );
    
                }



                // (II) receives new positions
                double[] positionArray = new double[3*SUB_CAPACITY];
                MPI.COMM_WORLD.
                Recv( positionArray, 0, SUB_CAPACITY*3, MPI.DOUBLE, 0, 99);

                // import new positions into the subsystem
                subsystem.importNewPositions(domain, positionArray);

                if ( tstep%nstlist == 0 ){
                    // update local neighbor list
                    nblist.update(subsystem);

                }

                // (ITS 0 step)
                    double[] partialEnergy = new double[1];
                    partialEnergy[0] = subsystem.getNonBondEnergy(nonbond, nblist);

                    MPI.COMM_WORLD.
                    Send( partialEnergy, 0, 1, MPI.DOUBLE, 0, 99); 


                // compute non-bonded forces
                subsystem.updateNonBondForce( nonbond, nblist );

                // PME


                // (III) export forces and send them to head-node
                double[] forceArray = subsystem.exportNewForces(domain);
                MPI.COMM_WORLD.
                Send( forceArray, 0, SUB_CAPACITY*3, MPI.DOUBLE, 0, 99); 


                if ( tstep % nstenergy == 0 ){
                    partialEnergy = new double[1];
                    partialEnergy[0] = subsystem.getNonBondEnergy(nonbond, nblist);

                    MPI.COMM_WORLD.
                    Send( partialEnergy, 0, 1, MPI.DOUBLE, 0, 99); 
                } 
            
                // reset force components
                subsystem.update();

            }


        }







        MPI.Finalize();
        
    }
}
