package mymd;

/**
 * a public interface providing an API for md integrator classes
 * implementing forwardPosition() and forwardVelocity() method
 *
 * @author Eunsong Choi (eunsong.choi@gmail.com)
 * @version 1.0
 */
public interface Integrator<E extends MdSystem<?, ?>>{



	/**
	 * using position, velocity, and(or) force at time t stored in sys
	 * derive a new position at t+dt and update sys(specific
	 * implement may vary) 
	 *
	 * @param sys MdSystem object containing all necessary information 
	 *			  at current time t
	 */
	public void forwardPosition(E sys);


	/**
	 * using position, velocity, and(or) force at time t stored in sys
	 * derive a new position at t+dt and store the result in newTraj
	 *
	 * @param sys MdSystem object containing all necessary information 
	 *			  at current time t
	 * @param prm MdParameter object containing simulation setup parameters
	 * @param newTraj Trajectory object for storing computed result
	 */
	public void forwardPosition(E sys, MdParameter prm, Trajectory<?> newTraj);




	/**
	 * using position, velocity, and(or) force at time t stored in sys
	 * and possibly some information(force for example) at time t+dt stored
	 * in sys, derive a new velocity at t+dt and update sys(specific
	 * implementation may vary)
	 *
	 * @param sys MdSystem object containing all necessary information 
	 *			  at current time t
	 */
	public void forwardVelocity(E sys);


	/**
	 * using position, velocity, and(or) force at time t stored in sys
	 * and possibly some information(force for example) at time t+dt stored
	 * in newTraj, derive a new velocity at t+dt and store the result in newTraj
	 *
	 * @param sys MdSystem object containing all necessary information 
	 *			  at current time t
	 * @param prm MdParameter object containing simulation setup parameters
	 * @param newTraj Trajectory object for storing computed result
	 */
	public void forwardVelocity(E sys, MdParameter prm, Trajectory<?> newTraj);

}
