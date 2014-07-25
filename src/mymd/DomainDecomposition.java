package mymd;

public class DomainDecomposition<E extends MdVector> implements Decomposition<E>{

	private final int npartition;
	private List<List<Integer>> domains;

	public DomainDecomposition(int np){
		this.npartition = np;
		// initializing domains list
		this.domains = new ArrayList<List<Integer>>();
		for ( int i = 0; i < np; i++){
			this.domains.add(new LinkedList<Integer>());
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

	public void partition(MdSystem<E> sys){

	}

}
