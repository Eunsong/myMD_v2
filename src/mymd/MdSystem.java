package mymd;

import mymd.datatype.*;
import mymd.nonbond.*;
import mymd.bond.*;
import mymd.angle.*;
import mymd.dihedral.*;
import mymd.constraint.*;
import mymd.gromacs.LoadGromacsSystem;
import java.util.List;


/**
 * MdSystem is a backbone class for simulation codes using mymd package.
 * A MdSystem object contains all the neccessary information in running 
 * simulations including project name, number of particles(size), simulation
 * set-up parameters, system topology, and position, velocity, and force 
 * of each particle. 
 * 
 * @author Eunsong Choi (eunsong.choi@gmail.com)
 * @version 1.0
 */

public class MdSystem<T extends Particle>{

	// boltzmann constant in units of kJ/mol/K
	public static final double kb = 0.0083144621;

	private final String name;
	private final int size;
	private final List<T> particles;
	private Trajectory currTraj, newTraj, pastTraj;
  	private final MdParameter parameters;
	private final Topology<MdSystem<T>> topology;
	private final double dt; 
	private final boolean verbose;


	/**
	 * public constructor is not provided. Instead, builder pattern, similar to
	 * that is introduced in Joshua Bloch's "Effective Java" and modified by
	 * Eamonn McManus in the link below, is used. Slight modification was made to
	 * use generic type parameter.
	 *
	 * @param init abstract builder class that needs to be subclassed
	 * @see https://weblogs.java.net/blog/emcmanus/archive/
	 *			  2010/10/25/using-builder-pattern-subclasses
	 */
	protected MdSystem(Init<T, ?> init){
		this.name = init.name;
		this.particles = init.particles;
		this.parameters = init.parameters;
		this.topology = init.topology;
		this.size = init.size;
		this.currTraj = init.initialTrajectory;
		this.dt = this.parameters.getDt();
		if ( this.currTraj != null ) {
			this.newTraj = new Trajectory(size);
			this.pastTraj = new Trajectory(size);
			this.newTraj.setTime(currTraj.getTime() + this.dt);
		}	
		this.verbose = init.verbose;
	}

	public String getName(){
		return this.name;
	}
	
	public List<T> getParticles(){
		return this.particles;
	}
	public T getParticle(int i){
		return this.particles.get(i);
	}

	public MdVector getBox(){
		return this.currTraj.getBox();
	}
	public int getSize(){
		return this.size;
	}

	public Trajectory getCurrTraj(){
		return this.currTraj;
	}
	public Trajectory getNewTraj(){
		return this.newTraj;
	}
	public Trajectory getPastTraj(){
		return this.pastTraj;
	}
	
	public MdParameter getParam(){
		return this.parameters;
	}
	public Topology<MdSystem<T>> getTopology(){
		return this.topology;
	}

	public double getTime(){
		return this.currTraj.getTime();
	}
	
	public boolean verbose(){
		return this.verbose;
	}
	
	public void forwardPosition(Integrator<MdSystem<T>> it){
		it.forwardPosition(this);
	}

	public void forwardVelocity(Integrator<MdSystem<T>> it){
		it.forwardVelocity(this);
	}



	/**
	 * generates random velocities of temperature T to all particles in the
	 * current trajectory.
	 *
	 * @param T target temperature in Kelvin
	 */
	public void genRandomVelocities(double T){
		Trajectory trj = currTraj;
		for ( int i = 0; i < size; i++){
			if ( !particles.get(i).isShell() ){
				double massi = particles.get(i).getMass();
				double vi = Math.sqrt( (3.0*kb*T)/massi);
				currTraj.genVelocity(i, vi);
			}
		}
	}


	public void update(){
		Trajectory tmp = this.pastTraj;
		this.pastTraj = this.currTraj;
		this.currTraj = this.newTraj;
		this.newTraj = tmp;
		this.newTraj.resetForces();
		this.newTraj.setTime( currTraj.getTime() + dt );
	}



	/******** methods updating various forces(to newTraj) ********/
	public void updateNonBondForce
	(NonBond<MdSystem<T>> nonbond, NeighborList nblist){
		nonbond.updateAllForces(this, nblist);
	}
	public void updateBondForce(){
		updateBondForce(topology.getBonds());
	}
	public void updateBondForce(Bonds<MdSystem<T>> bonds){
		bonds.updateAllForces(this);
	}
	public void updateAngleForce(){
		updateAngleForce(topology.getAngles());
	}
	public void updateAngleForce(Angles<MdSystem<T>> angles){
		angles.updateAllForces(this);
	}
	public void updateDihedralForce(){
		updateDihedralForce(topology.getDihedrals());
	}
	public void updateDihedralForce(Dihedrals<MdSystem<T>> dihedrals){
		dihedrals.updateAllForces(this);
	}


	/******** methods computing various energies(from newTraj) *******/

	/**
	 * getNonBondEnergy method returns nonbonded potential energy
	 * of nonbond pairs listed in the specified nblist. When using parallel 
	 * computation, each node should compute their responsible part and
	 * send the result to the head node.
	 */
	public double getNonBondEnergy
	(NonBond<MdSystem<T>> nonbond, NeighborList nblist){
		return nonbond.getTotalEnergy(this, nblist);	 
    }
	public double getBondEnergy(){
		return getBondEnergy(topology.getBonds());
	}

	public double getBondEnergy(Bonds<MdSystem<T>> bonds){
		return bonds.getTotalEnergy(this);
	}
	public double getAngleEnergy(){
		return getAngleEnergy(topology.getAngles());
	}
	public double getAngleEnergy(Angles<MdSystem<T>> angles){
		return angles.getTotalEnergy(this);
	}

	public double getDihedralEnergy(){
		return getDihedralEnergy(topology.getDihedrals());
	}
	public double getDihedralEnergy(Dihedrals<MdSystem<T>> dihedrals){
		return dihedrals.getTotalEnergy(this);
	}


	public double getKineticEnergy(){
		Trajectory trj = this.newTraj;
		MdVector[] velocities = trj.getVelocities();
		double energy = 0.0;
		for ( int i = 0; i < trj.getSize(); i++){
			double massi = particles.get(i).getMass();
			energy += 0.5*massi*velocities[i].normsq();
		}
		return energy;
	}




	/******* constraint algorithms ********/

	public void convertHBondsToConstraints(){
		convertHBondsToConstraints(topology.getConstraints());
	}
	public void convertHBondsToConstraints(Constraints<MdSystem<T>> constraints){
		constraints.convertHBondsToConstraints(this);
	}

	public void applyPositionConstraint(){
		applyPositionConstraint( topology.getConstraints() );
	}
	
	public void applyPositionConstraint(Constraints<MdSystem<T>> constraints){
		constraints.updateAllPositions(this);
	}
	public void correctConstraintVelocity(){
		correctConstraintVelocity(topology.getConstraints());
	}
	public void correctConstraintVelocity(Constraints<MdSystem<T>> constraints){
		constraints.updateAllVelocities(this);
	}




	public void partition(Decomposition<MdSystem<T>> decomposition){
		decomposition.partition(this);
	}


	public double[] exportNewPositions(Domain domain){
		Trajectory trj = this.getNewTraj();
		return trj.exportPositions(domain);
	}

	public void importNewPositions(Domain domain, double[] positionArray){
		Trajectory trj = this.getNewTraj();
		trj.importPositions(domain, positionArray);
	}	

	
	public double[] exportNewForces(Domain domain){
		Trajectory trj = this.getNewTraj();
		return trj.exportForces(domain);
	}

	public void importNewForces(Domain domain, double[] forceArray){
		Trajectory trj = this.getNewTraj();
		trj.importForces(domain, forceArray);
	}	





	/**
	 * Build pattern for generic subclassing. 
	 * @see https://weblogs.java.net/blog/emcmanus/archive/
	 *              2010/10/25/using-builder-pattern-subclasses
	 */
	protected static abstract class Init<E extends Particle, T extends Init<E,T>>{

		private String name;
		private List<E> particles;
		private MdParameter parameters;
		private Topology<MdSystem<E>> topology;
		private int size;
		private Trajectory initialTrajectory;
		private boolean verbose = false; 

		protected abstract T self();

		public T name(String name){
			this.name = name;
			return self();
		}
		public T particles(List<E> particles){
			this.particles = particles;
			return self();
		}
		public T parameters(MdParameter prm){
			this.parameters = prm;
			return self();
		}
		public T topology(Topology<MdSystem<E>> top){
			this.topology = top;
			return self();
		}
		public T initialTrajectory(Trajectory traj){
			this.initialTrajectory = traj;
			this.size = traj.getSize();
			return self();
		} 
		public T verbose(){
			this.verbose = true;
			return self();
		}

		public MdSystem<E> build(){
			return new MdSystem<E>(this);
		}
	}

	/**
	 * Builder class provies a builder method to construct a MdSystem object.
	 * Usage example: MdSystem system = new MdSystem.Builder<Particle>("test").
	 * 				  particles(myparticles).parameters(myparameters).
	 *				  topology(mytopology).initialTrajectory(traj).build();
	 */
	public static class Builder<E extends Particle> extends Init<E, Builder<E>>{

		public Builder(String name){
			super();
			super.name(name);
		} 

		@Override protected Builder<E> self(){
			return this;
		}

	}




}
