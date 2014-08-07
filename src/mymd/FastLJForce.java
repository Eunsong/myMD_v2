package mymd;

import mymd.datatype.LJParticle;
import mymd.datatype.MdVector;
import java.util.List;
import java.util.ArrayList;

/**
 * FastLJForce class implements Force interface for computing
 * Lennard-Jones forces using lookup tables for a faster computation speed
 * at the cost of slight accuracy. 
 *
 * @author Eunsong Choi (eunsong.choi@gmail.com)
 * @version 1.0
 */
public class FastLJForce<T extends MdSystem<LJParticle>> 
									 implements Force<T>{

	private final double rc;
	private final int size;
	private final LJForceLookupTable[] tables;
	private List<LJForceLookupTable> ljTempList;


	public FastLJForce(T sys){
		this.rc = sys.getParam().getRvdw();
		this.size = sys.getSize();
		this.tables = new LJForceLookupTable[size*size];
		this.ljTempList = new ArrayList<LJForceLookupTable>();
		buildTables(sys);
	}

	private void buildTables(T sys){
		for ( int i = 0; i < size; i++){

			double C6i = sys.getParticle(i).getC6();
			double C12i = sys.getParticle(i).getC12();

			for ( int j = 0; j < size; j++){ 
				double C6j = sys.getParticle(j).getC6();
				double C12j = sys.getParticle(j).getC12();
				double C6ij = combrule(C6i, C6j);
				double C12ij = combrule(C12i, C12j);

				int index = find(C6ij, C12ij);
				// if C6ij and C12ij pair don't exist already
				// create a new table and add it to ljTempList
				if ( index == -1 ){
					LJForceLookupTable newTable = 
					new LJForceLookupTable(rc, C6ij, C12ij);
					ljTempList.add(newTable);
					setTable(i, j, newTable);
				} 	
				// if C6ij and C12ij pair exist already
				else {
					setTable(i, j, ljTempList.get(index));
				}
			}
		}
	}

	private int find(double C6, double C12){
		int index = 0;
		for ( LJForceLookupTable table : ljTempList ){
			if ( table.getC6() == C6 && table.getC12() == C12 ){
				 return index;		
			}
			index++;
		}
		return -1;
	}
	private void setTable(int i, int j, LJForceLookupTable table){
		this.tables[j*size + i] = table;
	}
	private LJForceLookupTable getTable(int i, int j){
		return this.tables[j*size + i];	
	}


	/**
	 * computes a LJ force vector acting on the particle i due to j 
	 * using the particle positions stored in the newTraj in sys
	 *
	 * @param sys 
	 * @param i index number of particle i
	 * @param j index number of particle j
	 * @return MdVector object representing a force vector acting on
	 *		   the particle i due to the particle j
	 */
    public MdVector get( T sys, int i, int j){
		MdVector box = sys.getBox();

		MdVector Ri = sys.getNewTraj().getPosition(i);
		MdVector Rj = sys.getNewTraj().getPosition(j);
		MdVector Rij = new MdVector();
		Rij.copy(Ri).sub(Rj);
		Rij.minImage(box);
 
		double r = Rij.norm();
		if ( r < rc ){
			return MdVector.times(Rij, getTable(i,j).get(r));
		}
		return new MdVector();
	}


	/**
	 * computes LJ force vectors of all partifcles in sys due to 
	 * their neighbors specified in the given nblist and add computed
	 * forces on their new force component. 
	 *
	 * @param sys 
	 * @param nblist a NeighborList object containing index of neighboring
	 *				 particles(partjcle j) of each particle(i). Note that
	 * 				 this method assumes the size of nblist and trajectories
	 * 				 in sys are the same and they are written in the same order
	 */
    public void update( T sys, NeighborList nblist ){
		if ( sys.getSize() != nblist.getSize() ){
			throw new IllegalArgumentException
					("size of input NeighborList object does not match!");
		}

		Trajectory traj = sys.getNewTraj();	
		MdVector box = sys.getBox();
		MdVector Rij = new MdVector();
		MdVector F = new MdVector();
		for ( int i = 0; i < nblist.getSize(); i++){
			MdVector Ri = traj.getPosition(i);
			int[] sublist = nblist.getArray(i);
			for ( int k = 0; k < nblist.getSize(i); k++){ 
				int j = sublist[k];
				MdVector Rj = traj.getPosition(j);
				Rij.copy(Ri).sub(Rj); // Rij = Ri - Rj
				Rij.minImage(box);
				double r = Rij.norm();
				if ( r < rc ){
					F.copy(Rij).timesSet( getTable(i,j).get(r) );
					traj.addForce(i, F);
					traj.addReactionForce(j, F);
				}
			}
		}
	}


	private double combrule(double Ci, double Cj){
		return Math.sqrt(Ci*Cj);
	} 


}
