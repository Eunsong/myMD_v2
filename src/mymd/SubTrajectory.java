package mymd;

import mymd.datatype.*;


public class SubTrajectory extends Trajectory{

	private int subSize;

    // The positions, velocities, and forces can only contain
    // MdVector instances set from set methods. Thus we can ensure type safety.
	@SuppressWarnings("unchecked")
	public SubTrajectory(int N){
		super(N);
	}
	
	public void setSubSize(int size){
		this.subSize = size;
	}
	public int getSubSize(){
		return this.subSize;
	}


	/**** Add overriding methods that checks for subsize and input parameter 
		and throw exception accordingly ***/


}
