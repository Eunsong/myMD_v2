package mymd;

import mymd.datatype.*;
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


public class MdSystem<E extends MdVector, T extends Particle>{

	private final String name;
	private final int size;
	private List<T> particles;
	private Trajectory<E> currTraj, newTraj, pastTraj;
  	private final MdParameter parameters;
	private final Topology topology;


	private MdSystem(Builder builder){
		this.name = builder.name;
		this.particles = builder.particles;
		this.parameters = builder.parameters;
		this.topology = builder.topology;
		this.size = builder.size;
		this.currTraj = builder.initialTrajectory;
		this.newTraj = new Trajectory<E>(size);
		this.pastTraj = new Trajectory<E>(size);
	}
	
	public List<T> getParticles(){
		return this.particles;
	}
	public T getParticle(int i){
		return this.particles.get(i);
	}

	public E getBox(){
		return this.currTraj.getBox();
	}
	public int getSize(){
		return this.size;
	}

	public Trajectory<E> getCurrTraj(){
		return this.currTraj;
	}
	public Trajectory<E> getNewTraj(){
		return this.newTraj;
	}
	public Trajectory<E> getPastTraj(){
		return this.pastTraj;
	}
	
	public MdParameter getParam(){
		return this.parameters;
	}
	public Topology getTop(){
		return this.topology;
	}


	public void update(){
		this.pastTraj = this.currTraj;
		this.currTraj = this.newTraj;
		this.newTraj.resetForces();	
	}


	/**
	 * Builder class provies a builder method to construct a MdSystem object.
	 * Usage example: MdSystem<E> system = new MdSystem<E>.Builder("my simulation").
	 * 				  particles(myparticles).parameters(myparameters).
	 *				  topology(mytopology).initialTrajectory(traj).build();
	 */
	public static class Builder{
		private String name;
		private List<T> particles;
		private MdParameter parameters;
		private Topology topology;
		private int size;
		private Trajectory<E> initialTrajectory;

		public Builder(String name){
			this.name= name;
		} 
		public Builder particles(List<T> particles){
			this.particles = particles;
			return this;
		}
		public Builder parameters(MdParameter prm){
			this.parameters = prm;
			return this;
		}
		public Builder topology(Topology top){
			this.topology = top;
			return this;
		}
		public Builder initialTrajectory(Trajectory<E> traj){
			this.initialTrajectory = traj;
			this.size = traj.getSize();
			return this;
		} 

		public MdSystem<E> build(){
			return new MdSystem<E>(this);
		}
	}



}
