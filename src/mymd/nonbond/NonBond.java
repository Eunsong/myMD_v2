package mymd.nonbond;

import mymd.*;
import mymd.datatype.MdVector;

public interface NonBond<T extends MdSystem<?>> extends Force<T>, Energy<T>{

	//public MdVector getForce( T sys, int i, int j);


}
