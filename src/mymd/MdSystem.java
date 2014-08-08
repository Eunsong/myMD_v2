package mymd;


import mymd.datatype.*;
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
	private List<T> particles;
	private Trajectory currTraj, newTraj, pastTraj;
  	private final MdParameter parameters;
	private final Topology topology;
	private final double dt; 

	private MdSystem(Builder<T> builder){
		this.name = builder.name;
		this.particles = builder.particles;
		this.parameters = builder.parameters;
		this.topology = builder.topology;
		this.size = builder.size;
		this.currTraj = builder.initialTrajectory;
		this.newTraj = new Trajectory(size);
		this.pastTraj = new Trajectory(size);
		this.dt = this.parameters.getDt();
		this.newTraj.setTime(currTraj.getTime() + this.dt);
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
	public Topology getTop(){
		return this.topology;
	}

	public double getTime(){
		return this.currTraj.getTime();
	}

	public void update(){
		Trajectory tmp = this.pastTraj;
		this.pastTraj = this.currTraj;
		this.currTraj = this.newTraj;
		this.newTraj = tmp;
		this.newTraj.resetForces();
		this.newTraj.setTime( currTraj.getTime() + dt );
	}


	/**
	 * Builder class provies a builder method to construct a MdSystem object.
	 * Usage example: MdSystem system = new MdSystem.Builder("my simulation").
	 * 				  particles(myparticles).parameters(myparameters).
	 *				  topology(mytopology).initialTrajectory(traj).build();
	 */
	public static class Builder<T extends Particle>{
		private String name;
		private List<T> particles;
		private MdParameter parameters;
		private Topology topology;
		private int size;
		private Trajectory initialTrajectory;

		public Builder(String name){
			this.name= name;
		} 
		public Builder<T> particles(List<T> particles){
			this.particles = particles;
			return this;
		}
		public Builder<T> parameters(MdParameter prm){
			this.parameters = prm;
			return this;
		}
		public Builder<T> topology(Topology top){
			this.topology = top;
			return this;
		}
		public Builder<T> initialTrajectory(Trajectory traj){
			this.initialTrajectory = traj;
			this.size = traj.getSize();
			return this;
		} 

		public MdSystem<T> build(){
			return new MdSystem<T>(this);
		}

	}




}
