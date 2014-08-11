package mymd.datatype;

public class LJParticle implements Particle{

	private final int number;
	private final String name;
	private final String type;
	private final int typeNumber;
	private final String residueName;
	private final int residueNumber;
	private final int moleculeNumber;
	private final String moleculeType;

	private final double mass;
	private final double charge;
	private final int chargeGroup;
	private final boolean isShell;
	private final boolean isCharged;

	private final double C6;
	private final double C12;



	private LJParticle(Builder builder){
		this.number = builder.number;
		this.name = builder.name;
		this.type = builder.type;
		this.typeNumber = builder.typeNumber;
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
	
	public String getName(){
		return this.name;
	}
	
	public String getType(){
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

	public String getMoleculeType(){
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
		private String name = "unnamedParticle";
		private String type = "unnamedType";
		private int typeNumber = -1;
		private String residueName = "unnamedResidue";
		private int residueNumber = -1;
		private String moleculeType = "unnamedMolecule";
		private int moleculeNumber = -1;

		private double mass = 0.0;
		private double charge = 0.0;
		private int chargeGroup = -1;
		private boolean isShell = false;
		private boolean isCharged = false;
	
		private double C6 = 0.0;
		private double C12 = 0.0;


		public Builder(int number){
			this.number = number;
		}


		/**
		 * mass and charge value must be given at construction unless 
		 * decalred as shell particle(isShell = true) or as uncharged 
		 * type(isCharged = false)
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


		// optional settings
		public Builder name(String name){
			this.name = name;
			return this;
		}
		public Builder type(String name){
			this.type = name;
			return this;
		}
		public Builder typeNumber(int number){
			this.typeNumber = number;
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
		public Builder moleculeType(String type){
			this.moleculeType = type;
			return this;
		}
		public Builder moleculeNumber(int number){
			this.moleculeNumber = number;
			return this;
		}

		public Builder isShell(){
			this.isShell = true;
			return this;
		}
		public Builder C6(double C6){
			this.C6 = C6;
			return this;
		}
		public Builder C12(double C12){
			this.C12 = C12;
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
