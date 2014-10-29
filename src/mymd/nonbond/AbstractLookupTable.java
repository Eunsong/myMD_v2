package mymd.nonbond;

/**
 * AbstractLookupTable abstract class provides skeleton code for implementing
 * LookupTable interface. Subclass in which extending this class should implement
 * genTable() method.  
 *
 * @author Eunsong Choi (eunsong.choi@gmail.com)
 * @version 1.0 
 */

public abstract class AbstractLookupTable implements LookupTable{
	
	protected final double rc; // cut-off distance
	protected static final int DEFAULT_TABLE_SIZE = 100000;
	protected final int TABLE_SIZE;
	protected double[] table;

	protected AbstractLookupTable(double rc){
		this.rc = rc;
		this.TABLE_SIZE = this.DEFAULT_TABLE_SIZE;
	}

	protected abstract void genTable();

	/** 
	 * get method returns an approximate value of a function, 
	 * specified in the genTable() method, at given r using linear interpolation.
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
