package mymd.nonbond;

public class LJCForceLookupTable extends AbstractLookupTable{

	private static final double conversionFactor = 138.935485;
	private final double C6, C12, chargeSquared;

	public double getC6(){
		return this.C6;
	}
	public double getC12(){
		return this.C12;
	}
	public double getCharge(){
		return this.chargeSquared;
	}

	public LJCForceLookupTable(double rc, double C6, double C12, double chargeSquared){
		super(rc);
		this.C6 = C6;
		this.C12 = C12;
		this.chargeSquared = chargeSquared;
		genTable();
	}

	protected void genTable(){
		super.table = new double[super.TABLE_SIZE + 1];
		for ( int i = 0; i <= super.TABLE_SIZE; i++){
			double r = super.rc*(double)i/(double)super.TABLE_SIZE;
			super.table[i] = -6.0*C6/Math.pow(r,8) + 12.0*C12/Math.pow(r,14) 
							 + conversionFactor*chargeSquared/Math.pow(r,3);
		}
	} 




}
