package mymd;

import mymd.datatype.MdVector;
import java.util.List;
import java.util.ArrayList;
import java.lang.ArrayIndexOutOfBoundsException;


/**
 * Domain class provides a data structure including particle numbers associated
 * with specified domain and its buffer. Internally, a single int array is used
 * to store both domain and buffer particle numbers. The first domainCapacity 
 * elements in the array are reserved for domain particles, and the later 
 * bufferCapacity elements are reserved for buffer particles. The last two elements
 * are used to store domain size and buffer size (not their capacity).
 *
 * @author Eunsong Choi (eunsong.choi@gmail.com)
 * @version 1.0
 */

public class Domain{

	private final int domainCapacity;
	private final int bufferCapacity;
	private final int capacity;
	private int domainSize;
	private int bufferSize;
	private int[] particleList;


	public Domain(int domainCap, int buffCap){
		this.domainCapacity = domainCap;
		this.bufferCapacity = buffCap;
		this.capacity = domainCapacity + bufferCapacity;
		this.particleList = new int[capacity+2];
		this.domainSize = 0;
		this.bufferSize = 0;
		this.particleList[capacity] = domainSize;
		this.particleList[capacity+1] = bufferSize;
	}


	public void reset(){
		this.domainSize = 0;
		this.bufferSize = 0;
	}

	public void addToDomain(int i){
		if ( domainSize >= domainCapacity ){
			throw new ArrayIndexOutOfBoundsException
			("Number of particles exceeds maximum domain capacity."
			+ "Check system equilibration or increase domain capacity.");
		}
		particleList[domainSize] = i;
		domainSize++;
	}


	public void addToBuffer(int i){
		if ( bufferSize >= bufferCapacity ){
			throw new ArrayIndexOutOfBoundsException
			("Number of particles exceeds maximum buffer capacity."
			+ "Check system equilibration or increase buffer capacity.");
		}
		particleList[domainCapacity + bufferSize] = i;
		bufferSize++;
	}


	public int[] exportArray(){
		return this.particleList;
	}

	public void importArray(int[] inputArray){
		if ( inputArray.length != capacity + 2 ){
			throw new ArrayIndexOutOfBoundsException
			("inputArray size does not match Domain capacity");
		}
		this.particleList = inputArray;	
		this.domainSize = inputArray[capacity];
		this.bufferSize = inputArray[capacity+1];
	} 

	public int getDomainSize(){
		return this.particleList[capacity];
	}
	public int getBufferSize(){
		return this.particleList[capacity+1];
	}
	public int getDomainCapacity(){
		return this.domainCapacity;
	}
	public int getBufferCapacity(){
		return this.bufferCapacity;
	}
	public int getCapacity(){
		return this.capacity;
	}

	/**
 	 * @return the array index of first element in buffer region
	 */
	public int firstBuffer(){
		return getDomainCapacity();
	}
	/**
	 * @return ths array index of the last element in buffer region
	 */
	public int lastBuffer(){
		return (getDomainCapacity() + getBufferSize());
	} 
	
	public int getSize(){
		return (this.particleList[capacity] + this.particleList[capacity+1]);
	}



	public double[] exportPositions(Trajectory traj){
		double[] positionArray = new double[(capacity-2)*3];
		int index = 0;
		// domain particles
		for ( int i = 0; i < domainSize; i++){
			int iActual = this.particleList[i];
			MdVector Ri = traj.getPosition(iActual);
			positionArray[index++] = Ri.getX();
			positionArray[index++] = Ri.getY();
			positionArray[index++] = Ri.getZ();
		}
		// buffer particles
		for ( int i = domainCapacity; i < domainCapacity + bufferSize; i++){
			int iActual = this.particleList[i];
			MdVector Ri = traj.getPosition(iActual);
			positionArray[index++] = Ri.getX();
			positionArray[index++] = Ri.getY();
			positionArray[index++] = Ri.getZ();
		}
		return positionArray;
	}


	public void importPositions(Trajectory traj, double[] positionArray){
		if ( positionArray.length != (domainSize+bufferSize)*3 ){
            throw new ArrayIndexOutOfBoundsException
            ("input positionArray size does not match Domain size");
        }
		MdVector[] positions = traj.getPositions();
		int index = 0;
		for ( int i = 0; i < domainSize; i++){
			int iActual = this.particleList[i];
			double x = positionArray[index++];
			double y = positionArray[index++];
			double z = positionArray[index++];
			positions[iActual].copySet(x, y, z);
		}
		for ( int i = domainCapacity; i < domainCapacity + bufferSize; i++){
			int iActual = this.particleList[i];
			double x = positionArray[index++];
			double y = positionArray[index++];
			double z = positionArray[index++];
			positions[iActual].copySet(x, y, z);
		}		
	}
	

}
