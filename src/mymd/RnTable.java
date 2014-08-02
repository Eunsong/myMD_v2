package mymd;

/**
 * Lookup table class representing 1/r^n function 
 *
 * @author Eunsong Choi (eunsong.choi@gmail.com)
 * @version 1.0 
 */

public class RnTable implements LookupTable{
	
	private final int order; // order of the function
	private final double rc; // cut-off distance
	private static final int DEFAULT_TABLE_SIZE = 1000000;
	private final int TABLE_SIZE;
	private final double[] table;
	private final double C; // optional coefficient

	/**
	 * Constructors 
	 * 
	 * @param order order n of function
	 * @param rc cut-off distance (i.e. max allowed distance)
	 * @param C optional coefficient multiplied to the 1/r^n factor
	 * @param table_size optional table_size.
	 */
	public RnTable(int order, double rc){
		this.order = order;
		this.rc = rc;
		this.C = 1.0;
		this.TABLE_SIZE = this.DEFAULT_TABLE_SIZE;
		genTable();
	}
	public RnTable(int order, double rc, double C){
		this.order = order;
		this.rc = rc;
		this.C = C;
		this.TABLE_SIZE = this.DEFAULT_TABLE_SIZE;
		genTable();
	}
	public RnTable(int order, double rc, double C, int table_size){
		this.order = order;
		this.rc = rc;
		this.C = C;
		this.TABLE_SIZE = table_size;
		genTable();
	}


	private void genTable(){
		this.table = new double[this.TABLE_SIZE + 1];
		for ( int i = 0; i <= this.TABLE_SIZE; i++){
			double r = rc*(double)i/(double)TABLE_SIZE;
			table[i] = C*Math.pow( 1.0/r, order);
		}
	}

	/** 
	 * get method which returns an approximate value 
	 * of a function 1/r^n at given r using linear interpolation
	 *
	 * @param r input distance
	 * @return	approximate function value at r, linearly interpolated 
	 *          between two most appropriate adjacent table elements
	 */
	public double get(double r){
		double nReal = (r/rc)*(double)TABLE_SIZE;	
		int n = (int)nReal;
		double frac = nReal - n;
		// linear interpolation
		return table[n] + frac*(table[n+1] - table[n]);
	}

}
