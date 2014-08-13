package mymd;

import mymd.datatype.*;

/**
 * SubTrajectory class adds size and subSize fields to its parent Trajectory class.
 * An instance of SubTrajectory can be used as a member of SubSystem object 
 * on a slave-node when using parallel computation. 
 *
 * @author Eunsong Choi (eunsong.choi@gmail.com)
 * @version 1.0
 */
public class SubTrajectory extends Trajectory{

	// maximum size of member arrays (fixed size)
	private final int maxSize;

	// actual number of elements reside in its object(varies)	
	private int subSize;

	// contains actual particle number (e.g. particleMappingInfo[0] represents
	// particle number in the mother trajectory object of the first particle 
    // in this particular subTrajectory
	private int[] particleMappingInfo;

	public SubTrajectory(int size){
		super(size);
		this.maxSize = size;
	}
	
	public void setSubSize(int subSize){
		this.subSize = subSize;
	}
	public int getSubSize(){
		return this.subSize;
	}

	public void load(int[] particleMappingInfo){
		int inputArraySize = particleMappingInfo.length;
		if ( inputArraySize >= this.maxSize ){
			throw new ArrayIndexOutOfBoundsException
			("particle list size larger than the size of sub trajectory");
		}
		this.particleMappingInfo = particleMappingInfo;
		this.subSize = 
		
	}

	/**** Add overriding methods that checks for subsize and input parameter 
		and throw exception accordingly ***/


}
