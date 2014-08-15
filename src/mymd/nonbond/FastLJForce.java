package mymd.nonbond;

import mymd.*;
import mymd.datatype.LJParticle;
import mymd.datatype.LJParticleType;
import mymd.datatype.MdVector;
import java.util.List;
import java.util.ArrayList;
import java.lang.ArrayIndexOutOfBoundsException;

/**
 * FastLJForce class implements Force interface for computing
 * Lennard-Jones forces using lookup tables for a faster computation speed
 * at the cost of slight accuracy. 
 *
 * @author Eunsong Choi (eunsong.choi@gmail.com)
 * @version 1.0
 */
public class FastLJForce<T extends MdSystem<LJParticle>> 
									 implements NonBondedForce<T>{

	private final double rc;
	private final int size;
	private final int numberOfDistinctTypes;
	private final LJForceLookupTable[] tables;

	public FastLJForce(T sys){
		rc = sys.getParam().getRvdw();
		size = sys.getSize();

		List<LJParticleType> LJTypesInUse = new ArrayList<LJParticleType>();
		for ( LJParticle particle : sys.getParticles() ){
			if ( !LJTypesInUse.contains(particle.getType()) ){
				LJTypesInUse.add(particle.getType());
			}
		}
		numberOfDistinctTypes = LJTypesInUse.size();
		// verify if particle type numbers are correctly assigned
		for ( LJParticleType type : LJTypesInUse ){
			if ( type.getNumber() >= numberOfDistinctTypes ) {
				throw new ArrayIndexOutOfBoundsException("Check LJParticleType assignments");
			}
		}		

		tables = new LJForceLookupTable[numberOfDistinctTypes*numberOfDistinctTypes];
		buildTables(sys);
	
		if ( sys.verbose() ){
			System.out.println("FastLJForce lookup tables created..");
			System.out.print(String.format(
			"There are %d distinctive non-bonded LJ types including :", numberOfDistinctTypes));
			for ( LJParticleType type : LJTypesInUse ){
				System.out.print(String.format(" %s", type.getName()));
			}
			System.out.print("\n");
		}

	}

	private void buildTables(T sys){

		for ( int i = 0; i < size; i++){
		
			LJParticleType iType = sys.getParticle(i).getType();
			double C6i = iType.getC6();
			double C12i = iType.getC12();

			for ( int j = 0; j < size; j++){ 
				LJParticleType jType = sys.getParticle(j).getType();
				double C6j = jType.getC6();
				double C12j = jType.getC12();

				// if C6ij and C12ij pair don't exist already
				// create a new table 
				if ( getTable( iType.getNumber(), jType.getNumber()) == null ){
					double C6ij = combrule(C6i, C6j);
					double C12ij = combrule(C12i, C12j);
					LJForceLookupTable newTable =
					new LJForceLookupTable(rc, C6ij, C12ij);
					setTable( iType.getNumber(), jType.getNumber(), newTable);
				}
			}
		}
	
		// test correctness of input particleType settings
		for ( int i = 0; i < tables.length; i++){
			if ( tables[i] == null ) {
				throw new RuntimeException("Error! LJParticle type number mismatch!");
			}
		}

	}

	private void setTable(int i, int j, LJForceLookupTable table){
		this.tables[j*numberOfDistinctTypes + i] = table;
		this.tables[i*numberOfDistinctTypes + j] = table; // symmetric pair 
	}
	private LJForceLookupTable getTable(int i, int j){
		return this.tables[j*numberOfDistinctTypes + i];	
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
		Trajectory traj = sys.getNewTraj();
		MdVector box = traj.getBox();
		MdVector Ri = traj.getPosition(i);
		MdVector Rj = traj.getPosition(j);
		MdVector Rij = new MdVector();
		Rij.copy(Ri).sub(Rj);
		Rij.minImage(box);
 
		double r = Rij.norm();
		if ( r < rc ){
			int itype = sys.getParticle(i).getTypeNumber();
			int jtype = sys.getParticle(j).getTypeNumber();
			return MdVector.times(Rij, getTable(itype,jtype).get(r));
		}
		return new MdVector();
	}


	/**
	 * computes LJ force vectors of all partifcles in sys due to 
	 * their neighbors specified in the given nblist and add computed
	 * forces to their new force component. 
	 *
	 * @param sys 
	 * @param nblist a NeighborList object containing index of neighboring
	 *				 particles(partjcle j) of each particle(i). Note that
	 * 				 this method assumes the size of nblist and trajectories
	 * 				 in sys are the same and they are written in the same order
	 */
    public void updateAll( T sys, NeighborList nblist ){

		Trajectory traj = sys.getNewTraj();	
		MdVector box = traj.getBox();
		MdVector Rij = new MdVector();
		MdVector F = new MdVector();
		for ( int i = 0; i < nblist.getSize(); i++){
			int iActual = nblist.get(i);
			int[] sublist = nblist.getArray(i);
			MdVector Ri = traj.getPosition(iActual);
			for ( int j = 0; j < nblist.getSize(i); j++){ 
				int jActual = sublist[j];
				MdVector Rj = traj.getPosition(jActual);
				Rij.copy(Ri).sub(Rj); // Rij = Ri - Rj
				Rij.minImage(box);
				double r = Rij.norm();
				if ( r < rc ){
					int itype = sys.getParticle(iActual).getTypeNumber();
					int jtype = sys.getParticle(jActual).getTypeNumber();
					F.copy(Rij).timesSet( getTable(itype,jtype).get(r) );
					traj.addForce(iActual, F);
					traj.addReactionForce(jActual, F);
				}
			}
		}
	}


	private double combrule(double Ci, double Cj){
		return Math.sqrt(Ci*Cj);
	} 


}
