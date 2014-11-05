import mymd.*;
import mymd.nonbond.*;
import mymd.datatype.*;
import mymd.gromacs.LoadGromacsSystem;
import mymd.bond.*;
import mymd.thermostat.*;
import java.util.List;
import java.util.ArrayList;
import java.io.PrintStream;
import mpi.*;


/**
 * Runs a generic MD simulation using the myMD module. This code
 * runs a simulation parallely on a distributed momory platform(e.g. cluster computer)
 * using MPJ-Express library. Simulation settings should be specified
 * in an input prm file but standard GROMACS-like files can be used for force-fields.
 *
 * @author Eunsong Choi (eunsong.choi@gmail.com)
 * @version 1.0
 */
public class MpiSkeletonRun{
    public static void main(String[] args){

        String inputPrmFile = args[3];
        String inputConfFile = args[4];
        String inputTopFile = args[5];
        String outputTrajFile = args[6];
        String outputEnergyFile = args[7];

        MdSystem<LJParticle> system =  GromacsImporter.buildLJParticleSystem
        ("JOB_NAME", inputPrmFile, inputConfFile, inputTopFile);

        MdParameter prm = system.getParam();

        final double dt = prm.getDt();
        final int nsteps = prm.getNsteps();
        final int nstlist = prm.getNstlist();
        final int nstxout = prm.getNstxout();
        final int nstvout = prm.getNstvout();
        final int nstenergy = prm.getNstenergy();
        final int nstlog = 100; //prm.getNstlog();
        final double T0 = prm.getT0();
        final double TRef = prm.getRefT();
        final double tauT = prm.getTauT();
        final boolean convertHbonds = prm.convertHbonds();
        if ( convertHbonds ){
            system.convertHBondsToConstraints();
        }
    
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

                // compute non-bonded forces
                subsystem.updateNonBondForce( nonbond, nblist );

                // PME

                // (III) export forces and send them to head-node
                double[] forceArray = subsystem.exportNewForces(domain);
                MPI.COMM_WORLD.
                Send( forceArray, 0, SUB_CAPACITY*3, MPI.DOUBLE, 0, 99); 

                if ( tstep % nstenergy == 0 ){
                    double[] partialEnergy = new double[1];
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
