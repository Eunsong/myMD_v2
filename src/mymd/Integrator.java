package mymd;

/**
 * a public interface providing an API for md integrator classes
 * implementing forwardPosition() and forwardVelocity() method
 *
 * @author Eunsong Choi (eunsong.choi@gmail.com)
 * @version 1.0
 */
public interface Integrator<E extends MdVector>{

	/**
	 * using position, velocity, and(or) force at time t stored in sys
	 * derive a new position at t+dt and store the result in newTraj
	 *
	 * @param sys MdSystem instance containing all necessary information 
	 *			  at current time t
	 * @param top Topology instance containing all static 
	 * 			  molecular/atomistic information
	 * @param prm MdParam instance containing simulation setup parameters
	 * @param newTraj Trajectory instance for storing computed result
	 */
	public <E> void forwardPosition<E>(MdSystem<E> sys, Topology top, 
										MdParam prm, Trajectory newTraj);


	/**
	 * using position, velocity, and(or) force at time t stored in sys
	 * and possibly some information(force for example) at time t+dt stored
	 * in newTraj, derive a new velocity at t+dt and store the result in newTraj
	 *
	 * @param sys MdSystem instance containing all necessary information 
	 *			  at current time t
	 * @param top Topology instance containing all static 
	 * 			  molecular/atomistic information
	 * @param prm MdParam instance containing simulation setup parameters
	 * @param newTraj Trajectory instance for storing computed result
	 */
	public <E> void forwardVelocity<E>(MdSystem<E> sys, Topology top, 
                                        MdParam prm, Trajectory newTraj);

}
