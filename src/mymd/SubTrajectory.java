package mymd;

import mymd.datatype.*;


public class SubTrajectory extends Trajectory{

	// size of member arrays (fixed size)
	private final int size;

	// actual number of elements reside in its object(varies)	
	private int subSize;

	public SubTrajectory(int size){
		super(size);
		this.size = size;
	}
	
	public void setSubSize(int subSize){
		this.subSize = subSize;
	}
	public int getSubSize(){
		return this.subSize;
	}


	/**** Add overriding methods that checks for subsize and input parameter 
		and throw exception accordingly ***/


}
