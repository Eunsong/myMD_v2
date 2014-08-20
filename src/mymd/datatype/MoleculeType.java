package mymd.datatype;



public class MoleculeType{

	private String name;
	
	private int numberOfParticles;
	private int nrexcl;
	
	private boolean[][] exclusions;


	public String getName(){
		return this.name;
	}

	public boolean checkExclusion(int i, int j){
		return this.exclusions[i][j];
	}

	protected MoleculeType(Builder builder){
		this.name = builder.name;
		this.numberOfParticles = builder.numberOfParticles;
		this.nrexcl = builder.nrexcl;
		this.exclusions = builder.exclusions;
	}


	@Override public boolean equals(Object o){
		if ( !(o instanceof MoleculeType) ) return false;
		MoleculeType cp = (MoleculeType) o;
		return cp.name.equals(this.name);
	}



    /**
     * Builder class provies a builder method to construct a MoleculeType object.
     * Usage example: MoleculeType moleculetype = new MoleculeType.Builder("H2O").
     *                numberOfParticles(3).nrexcl(2).exclusions(exclusions).build();
     */
    public static class Builder{

        private String name;
		private int numberOfParticles;
		private int nrexcl;
		private boolean[][] exclusions; 


        public Builder(String name){
			this.name = name;
        }

		public Builder numberOfParticles(int num){
			this.numberOfParticles = num;
			return this;
		}

		public Builder nrexcl(int nrexcl){
			this.nrexcl = nrexcl;
			return this;
		}

		public Builder exclusions(boolean[][] exclusions){
			this.exclusions = exclusions;	
			return this;
		}

	
		public MoleculeType build(){
			return new MoleculeType(this);
		}

	}



}
