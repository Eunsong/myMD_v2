package mymd;

//import mymd.datatype.MdVector;
import mymd.nonbond.*;
import mymd.bond.Bonds;
import mymd.angle.Angles;
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
public class Topology<T extends MdSystem<?>> {

	private final Bonds<T> bonds;
	private final Angles<T> angles;

	private Topology(Builder<T> builder){
		this.bonds = builder.bonds;
		this.angles = builder.angles;
	}


	public Bonds<T> getBonds(){
		return this.bonds;
	}

	public Angles<T> getAngles(){
		return this.angles;
	}

	public static class Builder<E extends MdSystem<?>>{

		private Bonds<E> bonds;
		private Angles<E> angles;
	
		public Builder(){
		}

		public Builder<E> bonds(Bonds<E> bonds){
			this.bonds = bonds;
			return this;
		}
	
		public Builder<E> angles(Angles<E> angles){
			this.angles = angles;
			return this;
		}		

		public Topology<E> build(){
			return new Topology<E>(this);
		}


	}
/*
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
        this.angles = new ArrayList<Angle>();
        this.dihedrals = new ArrayList<Dihedral>();
        this.ljPairTypes = new ArrayList<List<LJPairType>>();
        this.exclusions = new ArrayList<List<Integer>>();
        this.particleTypesInUse = new ArrayList<ParticleType>();
        this.constraints = new ArrayList<Constraint>();
        this.constraintGroups = new ArrayList<ConstraintGroup>();
    }

	*/	

}
