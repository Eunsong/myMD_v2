/**
 * FastLJForce class implements Force interface for computing
 * Lennard-Jones forces using lookup tables for a faster computation speed
 * at the cost of slight accuracy. 
 *
 * @author Eunsong Choi (eunsong.choi@gmail.com)
 * @version 1.0
 */
public class FastLJForce<E extends MdVector, T extends MdSystem<E, LJParticle>> 
											implements Force<E, T>{

	private final RnTable R8Table, R14Table;
	//private final double[] C6, C12;	// caching computed mixing terms

	public FastLJForce(int rc){
		this.R8Table = new RnTable(8, rc);
		this.R14Table = new RnTable(14, rc);
	}


	/**
	 * computes a LJ force vector acting on the particle i due to j 
	 * using the particle positions stored in the currTraj in sys
	 *
	 * @param sys 
	 * @param i index number of particle i
	 * @param j index number of particle j
	 * @param Rij MdVector object representing a vector Ri - Rj 
	 * @return MdVector object representing a force vector acting on
	 *		   the particle i due to the particle j
	 */

    public <E> E get( T sys, int i, int j, E Rij){
		double C6i = sys.getParticle(i).getC6();
		double C6j = sys.getParticle(j).getC6();
		double C6ij = combrule(C6i, C6j);
	
		double C12i = sys.getParticle(i).getC12();
		double C12j = sys.getParticle(j).getC12();
		double C12ij = combrule(C12i, C12j);

		double r = Rij.norm();
		E C6term = E.times(Rij, -6*C6ij*R8Table.get(r));
		E C6term = E.times(Rij, 12*C12ij*R14Table.get(r));
		return E.add(C6term, C12term);
	}


	/**
	 * computes LJ force vectors of all partifcles in sys due to 
	 * their neighbors specified in the given nblist and add computed
	 * forces on their current force component. 
	 *
	 * @param sys 
	 * @param nblist a NeighborList object containing index of neighboring
	 *				 particles(partjcle j) of each particle(i). Note that
	 * 				 this method assumes the size of nblist and trajectories
	 * 				 in sys are the same and they are written in the same order
	 */
    public <E> void update( T sys, NeighborList nblist ){
		if ( sys.getSize() != nblist.getSize() ){
			throw new IllegalArgumentException
					("size of input NeighborList object does not match!");
		}

		Trajectory<E> traj = sys.getCurrTraj();	

		for ( int i = 0; i < nblist.getSize(); i++){

			E Ri = traj.getPosition(i);
			double C6i = sys.getParticle(i).getC6();
			double C12i = sys.getParticle(i).getC12();
			int[] sublist = nblist.getArray(i);

			for ( int j : sublist ){

				E Rj = traj.getPosition(j);
				E Rij = E.sub( Ri, Rj);
				double C6j = sys.getParticle(j).getC6();
				double C12j = sys.getParticle(j).getC12();
	
				double C6ij = combrule(C6i, C6j);
				double C12ij = combrule(C12i, C12j);
				double r = Rij.norm();
					
				E C6term = E.times(Rij, -6*C6ij*R8Table.get(r));
				E C6term = E.times(Rij, 12*C12ij*R14Table.get(r));
				traj.addForce(i, C6term);
				traj.addForce(i, C12term);
			}
		}
	}


	private double combrule(double Ci, double Cj){
		return Math.sqrt(Ci*Cj);
	} 


}
