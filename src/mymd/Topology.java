package mymd;

import mymd.datatype.*;
import java.util.List;
import java.util.ArrayList;

/**
 * An instance of Topology class contains static information 
 * for mymd simulations typically those required for computing force
 * and energy. 
 *
 * @author Eunsong Choi (eunsong.choi@gmail.com)
 * @version 1.0
 */
public class Topology {

    // List of bond pairs 
    private List<Bond> bonds;
    // List of angle triplets
    private List<Angle> angles;
    // List of dihedral quartets
    private List<Dihedral> dihedrals;
    // List of nonbond_parameters
    private List<List<LJPairType>> ljPairTypes;


    // List of particle types used in the system
    // (instead of the entire particletype list defined in the topology)
    private List<ParticleType> particleTypesInUse;


    // List of exclusions 
    private List<List<Integer>> exclusions;

    // List of single-pair constraints (e.g. H-bond constraints)
    private List<Constraint> constraints;

    // List of particle pairs groups to be constrained
    private List<ConstraintGroup> constraintGroups;



    public Topology() {
        this.bonds = new ArrayList<Bond>();
        this.angles = new ArrayList<Angle>();
        this.dihedrals = new ArrayList<Dihedral>();
        this.ljPairTypes = new ArrayList<List<LJPairType>>();
        this.exclusions = new ArrayList<List<Integer>>();
        this.particleTypesInUse = new ArrayList<ParticleType>();
        this.constraints = new ArrayList<Constraint>();
        this.constraintGroups = new ArrayList<ConstraintGroup>();
    }

		

}
