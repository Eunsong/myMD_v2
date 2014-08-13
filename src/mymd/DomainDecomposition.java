package mymd;

import java.util.List;
import java.util.ArrayList;
import mymd.datatype.MdVector;

/**
 * DomainDecomposition class implements Decomposition interface for 
 * optimized parallel md simulations using messege parallel interface.
 * The key idea of the domain decomposition is to provide an efficient partitioning
 * scheme that groups particles based on their physical locations(domains)
 * such that each domain serves as a self-sufficient chunk of data for 
 * nonbonded force calculation.
 * Ultimately, one should be able to partition entire system into smaller 
 * number of particles which decreases the amount of data need 
 * to be transfered across the nodes as a result.
 * Current version assumes cubic box or at least not very far from cubic. 
 * If one uses rectangular box with one axis much longer/shorter than the others,
 * prepare the system box such that z axis is the longest. Otherwise, current 
 * implementaion will be inefficient. 
 *
 * @author Eunsong Choi (eunsong.choi@gmail.com)
 * @version 1.0
 */

public class DomainDecomposition<E extends MdSystem<?>> implements Decomposition<E>{

	private final int totalSystemSize;
	private double head_node_load = 0.8;
	private static final double CAPACITY_FACTOR = 1.2;
	private final int nPartition;
	private List<Domain> domains;
	private final double rlist;

	public DomainDecomposition(E sys, int np){
		this.totalSystemSize = sys.getSize();
		this.nPartition = np;
		this.rlist = sys.getParam().getRlist();

		double lbox = sys.getCurrTraj().getBox().getZ();
		// use capacity factor to minize array extension and memory waste 
		int numberPerDomain = (int)(CAPACITY_FACTOR*totalSystemSize*
		(1.0 - head_node_load/nPartition)/(double)(np-1));
		// get expected mean number of particles in each buffer region
		int numberPerBuffer = (int)(CAPACITY_FACTOR*totalSystemSize*(rlist/lbox));

		// initializing domains list
		this.domains = new ArrayList<Domain>(np);
		for ( int i = 0; i < np; i++){
			this.domains.add(new Domain(numberPerDomain, numberPerBuffer));
		}
	} 

	/**
	 * Exports a particle list in the specified domain and associated buffer region.
	 * The last element of the arra is the number of particles in the buffer region,
	 * and the next last element is the number of particles in the domain region.
	 * See Domain.java for implementation details and array format.
	 *
	 * @param i an identification number specifying which partition information 
	 * 	        will be retrived. Typically the rank of the node
	 * @return int array indicating particle numbers(IDs) that belong to the
	 *         specified domain. Output array format follows contracts specified
	 * 	       in Domain class. 
	 */
	public int[] exportPartition(int i){
		if ( i >= nPartition ) {		
			throw new IllegalArgumentException("i greater than nPartition!");
		}
		else {
			return domains.get(i).exportArray();
		}
	}

	public int getNP(){
		return this.nPartition;
	}


	public Domain getDomain(int i){
		if ( i >= nPartition ) {		
			throw new IllegalArgumentException("i greater than nPartition!");
		}
		else {
			return this.domains.get(i);
		}
	}

	public void partition(E sys){
		Trajectory trj = sys.getNewTraj();
		partition(trj);
	}

	public void partition(Trajectory trj){

		MdVector[] positions = trj.getPositions();
		int size = trj.getSize();
		double lbox = trj.getBox().getZ();
		double headDomainLength = head_node_load*(lbox/nPartition);
		double otherDomainLength = (lbox - headDomainLength)/(nPartition-1);

		double[] domainBegin = new double[nPartition];
		double[] domainEnd = new double[nPartition];
		double[] bufferBegin = new double[nPartition];
		double[] bufferEnd = new double[nPartition];
	
		// find domain and buffer boundary locations
		domainBegin[0] = 0.0;
		domainEnd[0] = headDomainLength;
		bufferBegin[0] = headDomainLength;
		bufferEnd[0] = headDomainLength + rlist;
		for ( int n = 1; n < nPartition; n++){
			domainBegin[n] = domainEnd[n-1]; 
			domainEnd[n] = domainBegin[n] + otherDomainLength;
			if ( n != nPartition -1 ){
				bufferBegin[n] = domainEnd[n];
				bufferEnd[n] = bufferBegin[n] + rlist;
			}
			else {
				bufferBegin[nPartition-1] = 0.0;
				bufferEnd[nPartition-1] = rlist;
			}
		}
		// PBC correction
		for ( int n = 0; n < nPartition; n++){
			if ( bufferEnd[n] > lbox ) {
				 bufferEnd[n] -= lbox;
			}
		}
		// reset previous decomposition results	
		for ( int n = 0; n < nPartition; n++){
			domains.get(n).reset();
		}	
		
		for ( int i = 0; i < size; i++){
			double zi = positions[i].getZ();
			int domainNumber = -1;
			// Determine corresponding domain region 
			if ( zi < 0 || zi > lbox ) throw new RuntimeException
			("Particle found out of the box. Checm system equilibration and PBC");
			else if ( zi < headDomainLength ){
				domains.get(0).addToDomain(i);
				domainNumber = 0;
			}
			else {
				domainNumber = 1 + (int)((zi - headDomainLength)/otherDomainLength);
//System.out.println("adding particle " + i + " to domain " + domainNumber );
//System.out.println("zi = " + zi );
				domains.get(domainNumber).addToDomain(i);
			}

			// Determine corresponding buffer regions
			int offset = nPartition - 1;
			int k = (domainNumber + offset)%nPartition;
			while ( (zi - domainEnd[k] + lbox)%lbox < rlist ){
				domains.get(k).addToBuffer(i);
				offset--;
				k = (domainNumber + offset)%nPartition;
			}
		}	

	}


}
