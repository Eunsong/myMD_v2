package mymd;

import mymd.datatype.*;
import mymd.nonbond.*;
import mymd.bond.*;
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


	public void update(){
		Trajectory tmp = this.pastTraj;
		this.pastTraj = this.currTraj;
		this.currTraj = this.newTraj;
		this.newTraj = tmp;
		this.newTraj.resetForces();
		this.newTraj.setTime( currTraj.getTime() + dt );
	}


	public void updateNonBondedForce
	(NonBondedForce<MdSystem<T>> nonbonded, NeighborList nblist){
		nonbonded.updateAll(this, nblist);
	}

	public void updateBondForce(){
		updateBondForce(topology.getBonds());
	}

	public void updateBondForce(Bonds<MdSystem<T>> bonds){
		bonds.updateAllForces(this);
	}

	public void partition(Decomposition decomposition){
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
