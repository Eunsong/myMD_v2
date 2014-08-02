package mymd;

/**
 * AbstractNeighborList class provides skeleton code for implementing 
 * NeighborList interface. Internal sub-lists are stored in inner class
 * SubList in an array rather than List to optimize performance.
 *  
 *
 * @author Eunsong Choi (eunsong.choi@gmail.com)
 * @version 1.0
 */
public abstract class AbstractNeighborList<E extends MdVector, 
			 T extends MdSystem<E, ?>> implements NeighborList<E, T>{

	private List<SubList> nblist;
	private static final int INIT_CAPACITY = 1000;
	private static int max_capacity = INIT_CAPACITY; // max sublist size

	/**
	 * constructor with an input parameter specifying total number of particles
	 * (i.e. total number of sub-neighbor lists where each sub-neighbor list 
	 *  contains list of one's entire neighbors)
	 *
	 * @param numberOfParticles total number of particles, or sub-neighborlists. 
	 */
	public NeighborList(int numberOfParticles){
		this.nblist = new ArrayList<SubList>(numberOfParticles);
		for ( int i = 0; i < numberOfParticles; i++){
			nblist.add(new SubList());
		}
	}

	public int getSize(){
		return this.nblist.size();
	}

	public int[] getArray(int i){
		if ( i >= nblist.size() ){
			throw new IllegalArgumentException
					  ("invalid input parameter i for neighbor list");
		return nblist.get(i).getList();
	}

	/**
	 * abstract method update() needs to be implemented from a sub class
	 *
	 * @param sys
	 * @param prm 
	 * @param positions
	 */
	public abstract void update(T sys, E[] positions);



	/**
	 * SubList is an inner class of AbstractNeighborList which provides a wrapper object 
	 * including sub-list information. When an array becomes full, a new array of size
	 * max_capacity is created and the elements in the old array are copied to the new 
	 * array (if it's size is alread max_capacity, then double the size and
	 * update max_capacity)
	 */
	private class SubList{

		private int[] sublist;
		private int size;

		public SubList(){
			this.size = 0;
			this.sublist = new int[max_capacity];
		}
		public getList(){
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
		public void expand(){
			int currLength = this.sublist.length;
			int newLength = (currLength < max_capacity)? max_capacity:2*max_capacity;
			max_capacity = newLength;
			this.sublist = Arrays.copyOf(sublist, max_capacity);
		}
	}

}
