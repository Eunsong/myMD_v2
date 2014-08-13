package mymd;

import mymd.datatype.MdVector;

/**
 * DomainNeighborList class implements update() method in AbstractNeighborList
 * using Domain object. As of version 1.0, this is default NeighborList sub class
 * when used with MPI implementation. 
 *
 * @author Eunsong Choi (eunsong.choi@gmail.com)
 * @version 1.0
 */
public class DomainNeighborList<T extends MdSystem<?>> 
		extends AbstractNeighborList<T> implements NeighborList<T>{

	private Domain domain;
	
	public DomainNeighborList(int numberOfParticles, double rc, Domain domain){
		super(numberOfParticles, rc);
		this.domain = domain;
	}

    /**
     * update(T sys) method uses current positions in sys and invokes
     * update(T sys, MdVector[] positions, MdVector box) method 
     *
     * @param sys MdSystem object containing system backbone information
     */
    public void update(T sys){
        MdVector[] positions = sys.getCurrTraj().getPositions();
        MdVector box = sys.getBox();
        update(sys, positions, box);
    }


    /**
     * update(T sys, MdVector[] positions, MdVector box) method finds neighbor 
     * lists using brute force distance calculation through all pairs in each domain
	 * and pairs between the domain and associated buffer region as specified in 
	 * the domain field.
     *
     * @param sys MdSystem object containing system backbone information
     * @param positions MdVector array containing position of particles
	 * @param box system box dimension
     */
    public void update(T sys, MdVector[] positions, MdVector box){

		int[] particleList = domain.exportArray();
		double rc = super.rc;
		double rc2 = rc*rc;
		MdVector Rij = new MdVector();
		int domainSize = domain.getDomainSize();
		int bufferSize = domain.getBufferSize();
		int domainCapacity = domain.getDomainCapacity();
		for ( int i = 0; i < domainSize; i++){
			int iActual = particleList[i];
			MdVector Ri = positions[iActual];
			super.nblist.get(i).reset();
			super.nblist.get(i).seti(iActual);
			// intra-domain pairs
			for ( int j = i+1; j < domainSize; j++){
				int jActual = particleList[j];
				MdVector Rj = positions[jActual];
				Rij.copy(Ri).sub(Rj).done();
				Rij.minImage(box);
				double rsq = Rij.normsq();
				if ( rsq < rc2 ){
					super.nblist.get(i).add(jActual);
				}
			}
			// inter-domain pairs (domain-buffer pairs)
			for ( int j = domain.firstBuffer(); j < domain.lastBuffer(); j++){
				int jActual = particleList[j];
				MdVector Rj = positions[jActual];
				Rij.copy(Ri).sub(Rj).done();
				Rij.minImage(box);
				double rsq = Rij.normsq();
				if ( rsq < rc2 ){
					super.nblist.get(i).add(jActual);
				}
			}	
		}		
	}




}
