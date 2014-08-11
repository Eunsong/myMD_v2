package mymd.datatype;

public class LJParticleType extends ParticleType{

	private final double C6;
	private final double C12;

	public double getC6(){
		return this.C6;
	}
	public double getC12(){
		return this.C12;
	}

	private LJParticleType(Builder builder){
		super(builder.name, builder.number, builder.mass, builder.charge);
		this.C6 = builder.C6;
		this.C12 = builder.C12;
	}


	/**
	 * Builder nested class for custructing LJParticleType object.
	 * Usage example : LJParticleType lj = new LJParticleType.Builder("name").
	 * number(1).mass(12.01).charge(0.89).C6(0.46).C12(0.00228).build();
	 */
	public static class Builder{

		private String name;
		private int number;
		private double mass;
		private double charge;
		private double C6;
		private double C12;

		public Builder(String name){
			this.name = name;
		}
		public Builder number(int number){
			this.number = number;
			return this;
		}
		public Builder mass(double mass){
			this.mass = mass;
			return this;
		}
		public Builder charge(double charge){
			this.charge = charge;
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

		public LJParticleType build(){
			return new LJParticleType(this);
		}		
	}

}
