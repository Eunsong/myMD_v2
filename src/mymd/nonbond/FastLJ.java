package mymd.nonbond;

import mymd.*;
import mymd.datatype.LJParticle;
import mymd.datatype.LJParticleType;
import mymd.datatype.MdVector;
import java.util.List;
import java.util.ArrayList;
import java.lang.ArrayIndexOutOfBoundsException;

/**
 * FastLJ class implements NonBond interface for computing Lennard-Jones 
 * forces and energies using lookup tables for a faster computation speed
 * at the cost of slight accuracy. 
 *
 * @author Eunsong Choi (eunsong.choi@gmail.com)
 * @version 1.0
 */
public class FastLJ<T extends MdSystem<LJParticle>> implements NonBond<T>{

	private final double rc;
	private final int size;
	private final int numberOfDistinctTypes;
	private final LJForceLookupTable[] forceTables;
	private final LJEnergyLookupTable[] energyTables;

	public FastLJ(T sys){
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

		forceTables = new LJForceLookupTable[numberOfDistinctTypes*numberOfDistinctTypes];
		energyTables = new LJEnergyLookupTable[numberOfDistinctTypes*numberOfDistinctTypes];
		buildTables(sys);

	
		if ( sys.verbose() ){
			System.out.println("FastLJ lookup tables created..");
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
				if ( getForceTable( iType.getNumber(), jType.getNumber()) == null ){
					double C6ij = combrule(C6i, C6j);
					double C12ij = combrule(C12i, C12j);
					LJForceLookupTable newForceTable =
					new LJForceLookupTable(rc, C6ij, C12ij);
					setForceTable( iType.getNumber(), jType.getNumber(), newForceTable);

					LJEnergyLookupTable newEnergyTable =
					new LJEnergyLookupTable(rc, C6ij, C12ij);
					setEnergyTable(iType.getNumber(), jType.getNumber(), newEnergyTable); 

				}
			}
		}
	
		// test correctness of input particleType settings
		for ( int i = 0; i < forceTables.length; i++){
			if ( forceTables[i] == null ) {
				throw new RuntimeException("Error! LJParticle type number mismatch!");
			}
		}

	}

	private void setForceTable(int i, int j, LJForceLookupTable table){
		this.forceTables[j*numberOfDistinctTypes + i] = table;
		this.forceTables[i*numberOfDistinctTypes + j] = table; // symmetric pair 
	}

	private void setEnergyTable(int i, int j, LJEnergyLookupTable table){
		this.energyTables[j*numberOfDistinctTypes + i] = table;
		this.energyTables[i*numberOfDistinctTypes + j] = table; // symmetric pair 
	}

	private LJForceLookupTable getForceTable(int i, int j){
		return this.forceTables[j*numberOfDistinctTypes + i];	
	}
	private LJEnergyLookupTable getEnergyTable(int i, int j){
		return this.energyTables[j*numberOfDistinctTypes + i];	
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
			return MdVector.times(Rij, getForceTable(itype,jtype).get(r));
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
    public void updateAllForces( T sys, NeighborList nblist ){

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
					F.copy(Rij).timesSet( getForceTable(itype,jtype).get(r) );
					traj.addForce(iActual, F);
					traj.addReactionForce(jActual, F);
				}
			}
		}
	}


	public double getTotalEnergy(T sys, NeighborList nblist){

		Trajectory traj = sys.getNewTraj();	
		MdVector box = traj.getBox();
		MdVector Rij = new MdVector();
		double energy = 0.0;
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
					energy += getEnergyTable(itype, jtype).get(r);
				}
			}
		}
		return energy;
	}

	private double combrule(double Ci, double Cj){
		return Math.sqrt(Ci*Cj);
	} 


}
