package mymd;

import mymd.datatype.LJParticle;
import mymd.datatype.MdVector;

/**
 *
 * @author Eunsong Choi (eunsong.choi@gmail.com)
 * @version 1.0
 */
public class LJForce<T extends MdSystem<LJParticle>> 
									 implements NonBondedForce<T>{

	private final double rc;

	public LJForce(double rc){
		this.rc = rc;
	}


	/**
	 * computes a LJ force vector acting on the particle i due to j 
	 * using the particle positions stored in the currTraj in sys
	 *
	 * @param sys 
	 * @param i index number of particle i
	 * @param j index number of particle j
	 * @return MdVector object representing a force vector acting on
	 *		   the particle i due to the particle j
	 */

    public MdVector get( T sys, int i, int j){
		MdVector box = sys.getBox();

		double C6i = sys.getParticle(i).getC6();
		double C6j = sys.getParticle(j).getC6();
		double C6ij = combrule(C6i, C6j);

		double C12i = sys.getParticle(i).getC12();
		double C12j = sys.getParticle(j).getC12();
		double C12ij = combrule(C12i, C12j);

		MdVector Ri = sys.getCurrTraj().getPosition(i);
		MdVector Rj = sys.getCurrTraj().getPosition(j);
		MdVector Rij = new MdVector();
		Rij.copy(Ri).sub(Rj);
		Rij.minImage(box);
 
		double rsq = Rij.normsq();
		if ( rsq < rc*rc ){
			MdVector C6term = MdVector.times(Rij, -6*C6ij*R8(rsq));
			MdVector C12term = MdVector.times(Rij, 12*C12ij*R14(rsq));
			return MdVector.sum(C6term, C12term);
		}
		return new MdVector();
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
    public void updateAll( T sys, NeighborList nblist ){
		if ( sys.getSize() != nblist.getSize() ){
			throw new IllegalArgumentException
					("size of input NeighborList object does not match!");
		}

		Trajectory traj = sys.getCurrTraj();	
		MdVector box = sys.getBox();
		MdVector Rij = new MdVector();
		MdVector C6term = new MdVector();
		MdVector C12term = new MdVector();

		for ( int i = 0; i < nblist.getSize(); i++){

			MdVector Ri = traj.getPosition(i);
			double C6i = sys.getParticle(i).getC6();
			double C12i = sys.getParticle(i).getC12();
			int[] sublist = nblist.getArray(i);

			for ( int k = 0; k < nblist.getSize(i); k++){ 
				int j = sublist[k];
				MdVector Rj = traj.getPosition(j);
				Rij.copy(Ri).sub(Rj); // Rij = Ri - Rj
				Rij.minImage(box);
				double C6j = sys.getParticle(j).getC6();
				double C12j = sys.getParticle(j).getC12();
	
				double C6ij = combrule(C6i, C6j);
				double C12ij = combrule(C12i, C12j);
				double rsq = Rij.normsq();

				if ( rsq < rc*rc ){
					C6term.copy(Rij).timesSet(-6*C6ij*R8(rsq));
					C12term.copy(Rij).timesSet(12*C12ij*R14(rsq));	
					traj.addForce(i, C6term);
					traj.addForce(i, C12term);
					traj.addReactionForce(j, C6term);
					traj.addReactionForce(j, C12term);
				}
			}
		}
	}


	private double combrule(double Ci, double Cj){
		return Math.sqrt(Ci*Cj);
	} 

	private double R8(double rsq){
		return 1.0/(rsq*rsq*rsq*rsq);

	}

	private double R14(double rsq){
		return 1.0/(rsq*rsq*rsq*rsq*rsq*rsq*rsq);

	}

}
