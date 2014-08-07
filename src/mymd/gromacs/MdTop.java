package mymd.gromacs;
// File name : MdTop.java
// Author : Eunsong Choi (eunsong.choi@gmail.com)
// Last updated : 2013-10-17
// This JAVA class contains topologies for MD simulation 

import java.util.*;

public class MdTop {

	private String systemName;

	// List of molecule types to be used in the simulation
	private List<MdMolType> molTypes;
	// List of atom types to be used in the simulation
	private List<MdAtomType> atomTypes;
	// List of bondtypes
	private List<MdBondType> bondTypes;
	// List of angletypes
	private List<MdAngleType> angleTypes;
	// List of dihedraltypes
	private List<MdDihedralType> dihedralTypes;

	// List of bond pairs 
	private List<MdBond> bonds;
	// List of angle triplets
	private List<MdAngle> angles;
	// List of dihedral quartets
	private List<MdDihedral> dihedrals;
	// List of nonbond_parameters
	private List<MdNonBondParam> nonbond_params;

	// Look up table for nonbonded interaction parameters
	// (used in the actual simulation to lookup nonbonded interaction parameters)
	private List<List<MdNonBondParam>> nonBondTable;


	// List of atom types used in the system
	// (instead of the entire atomtype list defined in the topology)
	private List<MdAtomType> atomTypesInUse;


	// List of exclusions 
	private List<List<Integer>> exclusions;

	// List of single-pair constraints (e.g. H-bond constraints)
	private List<MdConstraint> constraints;

	// List of atom pairs groups to be constrained
	private List<MdConstraintGroup> constraintGroups;

	private int nbFunc;
	private int combRule;
	private double fudgeLJ;
	private double fudgeQQ;

//	private List<String> ifndef;


	
	public MdTop() {
		this.molTypes = new ArrayList<MdMolType>();
		this.atomTypes = new ArrayList<MdAtomType>();	
		this.bondTypes = new ArrayList<MdBondType>();
		this.angleTypes = new ArrayList<MdAngleType>();
		this.dihedralTypes = new ArrayList<MdDihedralType>();
		this.bonds = new LinkedList<MdBond>();
		this.angles = new LinkedList<MdAngle>();
		this.dihedrals = new LinkedList<MdDihedral>();
		this.nonbond_params = new LinkedList<MdNonBondParam>();
		this.nonBondTable = new ArrayList<List<MdNonBondParam>>();
		this.exclusions = new ArrayList<List<Integer>>();
		this.atomTypesInUse = new ArrayList<MdAtomType>();
        this.constraints = new ArrayList<MdConstraint>();
		this.constraintGroups = new ArrayList<MdConstraintGroup>();
//		this.ifndef = new LinkedList<String>();
	}

	public void setSystemName(String name) {
		this.systemName = name;
	}

	public String getSystemName() {
		return this.systemName;
	}

	public List<MdMolType> getMolTypes() {
		return this.molTypes;
	}
	public MdMolType getMolType(int i) {
		return this.molTypes.get(i);
	}

	public void addMolType(MdMolType moltype) {
		this.molTypes.add(moltype);
	}

	public void addAtomType(MdAtomType atomtype) {
		this.atomTypes.add(atomtype);
	}
	public List<MdAtomType> getAtomTypes() {
		return this.atomTypes;
	}

	public void addAtomTypeInUse(MdAtomType atomtype) {
		this.atomTypesInUse.add(atomtype);
	}
	public List<MdAtomType> getAtomTypesInUse() {
		return this.atomTypesInUse;
	}
	
	
	public void addBondType(MdBondType bondtype) {
		this.bondTypes.add(bondtype);
	}
	public List<MdBondType> getBondTypes() {
		return this.bondTypes;
	}

	public void addAngleType(MdAngleType angletype) {
		this.angleTypes.add(angletype);
	}
	public List<MdAngleType> getAngleTypes() {
		return this.angleTypes;
	}


	public void addDihedralType(MdDihedralType dihedraltype) {
		this.dihedralTypes.add(dihedraltype);
	}
	public List<MdDihedralType> getDihedralTypes() {
		return this.dihedralTypes;
	}

    public void addBond(MdBond bond) {
        this.bonds.add(bond);
    }
    public List<MdBond> getBonds() {
        return this.bonds;
    }

    public void addAngle(MdAngle angle) {
        this.angles.add(angle);
    }
    public List<MdAngle> getAngles() {
        return this.angles;
    }

    public void addDihedral(MdDihedral dihedral) {
        this.dihedrals.add(dihedral);
    }
    public List<MdDihedral> getDihedrals() {
        return this.dihedrals;
    }

	public void addNonBondParam(MdNonBondParam nonbond_param) {
		this.nonbond_params.add(nonbond_param);
	}
	public List<MdNonBondParam> getNonBondParams() {
		return this.nonbond_params;
	}

	public List<List<MdNonBondParam>> getNonBondTable() {
		return this.nonBondTable;
	}
	public MdNonBondParam getNonBondParam(int i, int j) {
		return this.nonBondTable.get(i).get(j);
	}


	public void setNbFunc( int n ) {
		this.nbFunc = n;
	}
	public int getNbFunc() {
		return this.nbFunc;
	}


	public void setCombRule( int n ) {
		this.combRule = n;
	}
	public int getCombRule() {
		return this.combRule;
	}

	public void setFudgeLJ(double val) {
		this.fudgeLJ = val;
	}
	public double getFudgeLJ() {
		return this.fudgeLJ;
	}
	
	public void setFudgeQQ(double val) {
		this.fudgeQQ = val;
	}
	public double getFudgeQQ() {
		return this.fudgeQQ;
	}

	public void addConstraintGroup(MdConstraintGroup constraintGroup){
		this.constraintGroups.add(constraintGroup);
	}
	public List<MdConstraintGroup> getConstraintGroups(){
		return this.constraintGroups;
	}
	public MdConstraintGroup getConstraintGroup(int i){
		return this.constraintGroups.get(i);
	}

    public void addConstraint(MdConstraint constraint){
        this.constraints.add(constraint);
    }
    public List<MdConstraint> getConstraints(){
        return this.constraints;
    }
    public MdConstraint getConstraint(int i){
        return this.constraints.get(i);
    }



	// rearrange moleculetypes in moltypes by their rank
	public void reOrderMolTypes() {

		List<MdMolType> newMolTypes = new ArrayList<MdMolType>();
		for ( int i = 0; i < this.molTypes.size(); i++ ) {

			int currRank = this.molTypes.get(i).getRank();

			if ( currRank != -1 ) { // if this molType exists in the system
				int index = 0;
				for ( int j = 0; j < newMolTypes.size(); j++ ) {
					if ( currRank > newMolTypes.get(j).getRank() ) {
						index++;
					}
				}
				newMolTypes.add(index, this.molTypes.get(i) );
			}
		}

		// replace this.molTypes with newMolTypes
		this.molTypes = newMolTypes;

	}

}
