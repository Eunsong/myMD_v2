package mymd.constraint;

import mymd.MdSystem;


/**
 * Constraint interface provides an API for implementing various constraint
 * algorithms.
 *
 * @author Eunsong Choi (eunsong.choi@gmail.com)
 * @version 1.0
 */
public interface Constraint<T extends MdSystem<?>>{

    public void updatePosition(T sys);

    public void updateVelocity(T sys);
}
