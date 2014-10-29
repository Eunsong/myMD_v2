package mymd.datatype;

public class LJParticle implements Particle{

	// number is unique particle number (no two particles have the same number)
	private final int number; 

	// id is particle identification number within its molecule type
	// e.g. two oxygen atoms in different water molecule have the same id
	private final int id; 
	private final String name;

	private final LJParticleType type;
	private final int typeNumber;

	private final String residueName;
	private final int residueNumber;

	/**
	 * moleculeNumber is unique number specifying a molecule where the particle 
	 * belongs to. It is not molTypeId that was used in the alpha
	 * version. No two molecules have the same moleculeNumber. 
	 * (this condition is required in order to figure out intra-molecular 
	 * interactions in run time. For example, when checking for exclusions,
	 * one should first check if two atoms have same moleculeNumber, and if then
	 * look for exclusions defined in their moleculeType using their id numbers)
	 */
	private final int moleculeNumber;
	private final MoleculeType moleculeType;

    private final double mass;
    private final double charge;
	private final int chargeGroup;
	private final boolean isShell;
	private final boolean isCharged;

	private final double C6;
	private final double C12;


	private LJParticle(Builder builder){
		this.number = builder.number;
		this.id = builder.id;
		this.name = builder.name;
		this.type = builder.type;
		this.typeNumber = builder.type.getNumber();
		this.residueName = builder.residueName;
		this.residueNumber = builder.residueNumber;
		this.moleculeNumber = builder.moleculeNumber;
		this.moleculeType = builder.moleculeType;
		this.mass = builder.mass;
		this.charge = builder.charge;
		this.chargeGroup = builder.chargeGroup;
		this.isShell = builder.isShell;
		this.isCharged = builder.isCharged;
		this.C6 = builder.C6;
		this.C12 = builder.C12;
	}



	public int getNumber(){
		return this.number;
	}
	public int getId(){
		return this.id;
	}
	
	public String getName(){
		return this.name;
	}
	
	public LJParticleType getType(){
		return this.type;
	}

	public int getTypeNumber(){
		return this.typeNumber;
	}

	public String getResidueName(){
		return this.residueName;
	}

	public int getResidueNumber(){
		return this.residueNumber;
	}

	public int getMoleculeNumber(){
		return this.moleculeNumber;
	}

	public MoleculeType getMoleculeType(){
		return this.moleculeType;
	}
	
	public double getMass(){
		return this.mass;
	}
		
	public double getCharge(){
		return this.charge;
	}
	public boolean isCharged(){
		return this.isCharged;
	}
	
	public boolean isShell(){
		return this.isShell;
	}

	public double getC6(){
		return this.C6;
	}

	public double getC12(){
		return this.C12;
	}


    /**
     * Builder class provies a builder method to construct a LJParticle object.
     * Usage example: LJParticle myparticle = new LJParticle.Builder(1).
     *                mass(72.00).C6(0.21558).C12(0.0023238).build();
     */
	public static class Builder{

		private int number;
		private int id;
		private String name = "unnamedParticle";
		private LJParticleType type;
		private String residueName = "unnamedResidue";
		private int residueNumber = -1;
		private MoleculeType moleculeType;
		private int moleculeNumber = -1;

		private double mass = 0.0;
		private double charge = 0.0;
		private int chargeGroup = -1;
		private boolean isShell = false;
		private boolean isCharged = false;

		private double C6 = 0.0;
		private double C12 = 0.0;;	

		public Builder(int number, LJParticleType type){
			this.number = number;
			this.type = type;
			this.mass = type.getMass();
			this.charge = type.getCharge();
			if ( charge != 0.0 ){
				this.isCharged = true;
			}
			this.C6 = type.getC6();
			this.C12 = type.getC12();
			this.isShell = type.isShell();
		}

		/******** essential builder settings ********/
		public Builder moleculeType(MoleculeType type){
			this.moleculeType = type;
			return this;
		}
		public Builder moleculeNumber(int number){
			this.moleculeNumber = number;
			return this;
		}
		public Builder id(int id){
			this.id = id;
			return this;
		}
		/**
		 * mass and charge values are copied from given LJParticleType by default.
		 * To overload these values, use charge() and mass() method prior to build()
		 * method call. If the particle is declared as shell type with isShell() method
		 * mass value is ignored. If charge value is not overwritten, and default value 
		 * is 0, build method will create a non-charged type LJParticle object.
		 */
 		public Builder charge(double charge){
			this.isCharged = true;
			this.charge = charge;
			return this;
		}
		public Builder mass(double mass){
			this.mass = mass;
			return this;
		}
		/**
		 * These methods override C6 and C12 value. Defaults are those specified
		 * the type object(otherwise, 0.0)
		 */
		public Builder C6(double C6){
			this.C6 = C6;
			return this;
		}
		public Builder C12(double C12){
			this.C12 = C12;
			return this;
		}
		// Shell type is also overridable 
		public Builder isShell(){
			this.isShell = true;
			return this;
		}




		/******* optional settings *******/
		public Builder name(String name){
			this.name = name;
			return this;
		}
		public Builder residueNumber(int number){
			this.residueNumber = number;
			return this;
		}
		public Builder residueName(String name){
			this.residueName = name;
			return this;
		}

		public Builder chargeGroup(int groupNumber){
			this.chargeGroup = groupNumber;
			return this;
		}




		public LJParticle build(){
			if ( !this.isShell ){
				if ( this.mass == 0.0 ){
					throw new RuntimeException
					("uninitialized mass value for non-shell particle type!");
				}
			}
			if ( this.isCharged ){
				if ( this.charge == 0.0 ){
					throw new RuntimeException
					("uninitialized charge value for charged type particle!");
				}
			}
			return new LJParticle(this);
		}
		
	}


}
