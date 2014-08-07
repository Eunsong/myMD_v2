package mymd.gromacs;

import java.util.*;

public class MdMolType {

    private String typeName;
    private int nmols; // number of molecules in this type 
    private int natoms; // number of total atoms in a molecule
    private int nrexcl; 
    private int rank; // order of this moltype defined in the [ molecules ] field

    // List of atoms under this specific moleculetype
    private List<MdAtom> atoms;

    // List of bonds under this specific moleculetype
    private List<MdBond> bonds;

    // List of angles under this specific moleculetype
    private List<MdAngle> angles;

    // List of dihedrals under this specific moleculetype
    private List<MdDihedral> dihedrals;

    // List of exclusions (true if given pair should be excluded)
    private boolean[][] exclusions;

    // List of constraints under this specific moleculetype
    private List<MdConstraint> constraints;

    public MdMolType() {
        this.atoms = new ArrayList<MdAtom>();
        this.bonds = new ArrayList<MdBond>();
        this.angles = new ArrayList<MdAngle>();
        this.dihedrals = new ArrayList<MdDihedral>();
        this.constraints = new ArrayList<MdConstraint>();
        this.rank = -1; // initial value
        this.nmols = 0;
        this.natoms = 0;

    }

    public void setType(String name) {
        this.typeName = new String(name);
    }
    public String getType() {
        return this.typeName;
    }

    public void setNMols(int n) {
        this.nmols = n;
    }
    public int getNMols() {
        return this.nmols;
    }

    // setting NAtoms also initiates exclusions 
    public void setNAtoms(int n) {
        this.natoms = n;
        exclusions = new boolean[n][n];
    }

    public int getNAtoms() {
        return this.natoms;
    }

    public void setNrExcl(int nrexcl) {
        this.nrexcl = nrexcl;
    }

    public int getNrExcl() {
        return this.nrexcl;
    }

    public boolean[][] getExclusions() {
        return this.exclusions;
    }

    public boolean checkExclusion(int i, int j) {
        return this.exclusions[i][j];
    }

    
    public void addAtom(MdAtom atom) {
        this.atoms.add(atom);
    }
    public List<MdAtom> getAtoms() {
        return this.atoms;
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

    public void setRank(int rank) {
        this.rank = rank;
    }
    public int getRank() {
        return this.rank;
    }

    public void addConstraint(MdConstraint constraint) {
        this.constraints.add(constraint);
    }
    public List<MdConstraint> getConstraints() {
        return this.constraints;
    }

}
