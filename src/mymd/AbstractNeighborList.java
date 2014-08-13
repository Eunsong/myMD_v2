package mymd;

import mymd.datatype.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * AbstractNeighborList class provides skeleton code for implementing 
 * NeighborList interface. Internal sub-lists are stored in inner class
 * SubList in an array instead of a List to optimize performance.
 *  
 *
 * @author Eunsong Choi (eunsong.choi@gmail.com)
 * @version 1.0
 */
public abstract class AbstractNeighborList<T extends MdSystem<?>> 
									   implements NeighborList<T>{

	protected List<SubList> nblist;
	protected static final int INIT_CAPACITY = 1000;
	protected static int max_capacity = INIT_CAPACITY; // max sublist size
	protected final double rc; // cut-off distance for neighbor lists

	/**
	 * constructor with an input parameter specifying total number of particles
	 * (i.e. total number of sub-neighbor lists where each sub-neighbor list 
	 *  contains list of one's entire neighbors)
	 *
	 * @param numberOfParticles total number of particles, or sub-neighborlists.
	 * @param rc cut-off distance for neighbor list 
	 */
	protected AbstractNeighborList(int numberOfParticles, double rc){
		this.nblist = new ArrayList<SubList>(numberOfParticles);
		for ( int i = 0; i < numberOfParticles; i++){
			nblist.add(new SubList(i));
		}
		this.rc = rc;
	}


	/**
	 * getSize() method returns the number of sublists which is essentially
	 * numberOfParticles value given when constructor was invoked.
	 *
	 * @return the number of sublists (i.e. number of particle i)
	 */
	public int getSize(){
		return this.nblist.size();
	}

	/**
	 * getSize(int i) method returns the number of neighbors of ith particle.
	 * When treversing through the sublist arrays, one should make sure that
	 * the iteration does not go beyond the size of each sublist array using
	 * returned value of this method.
	 *
	 * @param i particle i 
	 * @return the number of neighboring particles of the particle i
	 */
	public int getSize(int i){
		return this.nblist.get(i).getSize();
	}


	/**
	 * get(int i) method returns the i-particle identification number
	 * (i.e host praticle number of the neighbor list i) 
	 *
	 * @param i ith sub-neighbor list
	 * @return host particle identification number of ith sublist 
	 */
	public int get(int i){
		return nblist.get(i).geti();	
	}

	/**
	 * getArray(int i) method returns an array containing the list of 
	 * neighbors of particle i. 
	 *
	 * @param i ith sub-neighbor list
	 * @return ith sublist array containing i's neighbors
	 */
	public int[] getArray(int i){
		if ( i >= nblist.size() ){
			throw new IllegalArgumentException
					  ("invalid input parameter i for neighbor list");
		}
		return nblist.get(i).getList();
	}


	/**
	 *
	 * @param sys
	 */
	public abstract void update(T sys);


	/**
	 * abstract method update() needs to be implemented from a sub class
	 *
	 * @param sys
	 * @param positions
	 * @param box
	 */
	public abstract void update(T sys, MdVector[] positions, MdVector box);


	/**
	 * reset() method initialize all sublists. This method must be invoked
	 * prior to updating neighbor lists
	 */
	protected void reset(){
		for ( int i = 0; i < getSize(); i++){
			nblist.get(i).reset();
		}
	}


	/**
	 * SubList is an inner class of AbstractNeighborList which provides a wrapper object 
	 * including sub-list information. When an array becomes full, a new array of size
	 * max_capacity is created and the elements in the old array are copied to the new 
	 * array (if it's size is alread max_capacity, then double the size and
	 * update max_capacity)
	 */
	protected class SubList{

		private int index; // index number of i-particle
		private int[] sublist;
		private int size;

		public SubList(int index){
			this.index = index;
			this.size = 0;
			this.sublist = new int[max_capacity];
		}
		public int geti(){
			return this.index;
		}
		public void seti(int index){
			this.index = index;
		}
		public int[] getList(){
			return this.sublist;
		} 
		public int getSize(){
			return this.size;
		}

		public void reset(){
			this.size = 0;
		}

		/**
	 	 * add method writes an input value to the last element of the array
		 *
		 * @param value identification number of a particle to be added 
		 * 				into the sublist
		 */
		public void add(int value){
			this.sublist[size] = value;
			size++;
			if ( size >= this.sublist.length ) {
				expand();		
			}
		}
		private void expand(){
			int currLength = this.sublist.length;
			int newLength = (currLength < max_capacity)? max_capacity:2*max_capacity;
			max_capacity = newLength;
			this.sublist = Arrays.copyOf(sublist, max_capacity);
		}
	}

}
