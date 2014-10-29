package mymd.constraint;

import mymd.MdSystem;


/**
 * SimpleConstraint interface provides an API for implementing constraint
 * algorithms for sinle-pair constraint(not a simultaneous constraint)
 *
 * @author Eunsong Choi (eunsong.choi@gmail.com)
 * @version 1.0
 */
public interface SimpleConstraint<T extends MdSystem<?>> extends Constraint<T>{

	public int geti();
	public int getj();
	
	public double getTargetDistance();

}
