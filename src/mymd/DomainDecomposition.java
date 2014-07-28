package mymd;

/**
 * DomainDecomposition class implements Decomposition interface for 
 * optimized parallel md simulations using messege parallel interface
 * The key idea of the domain decomposition is to provide an efficient partitioning
 * scheme that groups particles based on their physical locations(domains)
 * such that each domain serves as a self-sufficient chunk of data for 
 * nonbonded force calculation
 * Ultimately, one should be able to partition entire system into smaller 
 * number of particles which consequently decreases the amount of data need 
 * to be transfered across the nodes
 * 
 * @author Eunsong Choi (eunsong.choi@gmail.com)
 * @version 1.0
 */

public class DomainDecomposition implements Decomposition{

	private static final double CAPACITY_FACTOR = 1.2;
	private final int npartition;
	private List<Domain> domains;

	public DomainDecomposition(MdSystem<?> sys, int np){
		this.npartition = np;
		// get total number of particles from sys
		int totalNumber = /***** FILL IN ******/
		// use capacity factor to minize array extension and memory waste 
		int numberPerDomain = (int)(CAPACITY_FACTOR*totalNumber/(double)np);
		// get expected mean number of particles in each buffer region
		int numberPerBuffer = /***** FILL IN *****/

		// initializing domains list
		this.domains = new ArrayList<Domain>(np);
		for ( int i = 0; i < np; i++){
			this.domains.add(new Domain(numberPerDomain, numberPerBuffer));
		}
	} 

	public List<List<Integer>> getPartitions(){
		return this.domains;
	}

	public List<Integer> getPartition(int i){
		if ( i >= npartition ) {
			throw new IllegalArgumentException("i greater than npartition!");
		}
		return this.domains.get(i);
	}

	public void partition(MdSystem sys){
		// Need to be implemented!
	}


	/**
	 * inner class that is only used within DomainDecomposition class
	 * This class binds domain and buffer particle list together
	 * in its sole instance
	 */
	private class Domain{
		private List<Integer> domain; // primary domain region
		private List<Integer> buffer; // buffer region

		// default constructor without parameter
		public Domain(){
			this.domain = new ArrayList<Integer>();
			this.buffer = new ArrayList<Integer>();		
		}
		/**
		 * optional constructor with the parameters 
		 * determining initial array capacity
		 *
		 * @param domainInitCapacity initial capacity of domain array
		 *							 use at least about 20% larger value than 
	  	 *							 initially assignable particle numbers
		 * @param buffInitCapacity initial capacity of buffer arary
		 *						   use at least about 20% larger value than
		 *						   initially assginable particle numbers 
		 */
		public Domain(int domainInitCapacity, int buffInitCapacity){
			this.domain = new ArrayList<Integer>(domainInitCapacity);
			this.buffer = new ArrayList<Integer>(buffInitCapacity);
		}

		// accessor methods
		public List<Integer> getDomain(){
			return this.domain;
		}
		public List<Integer> getBuffer(){
			return this.buffer;
		}
		public void addToDomain(int id){
			this.domain.add(id);
		}
		public void addToBuffer(int id){
			this.buffer.add(id);
		}
	}

}
