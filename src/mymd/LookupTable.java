package mymd;

/**
 * a public interface which provides an API for lookup tables
 * implementing get() method. Lookup tables can be used to optimize
 * a function calculation at the cost of accuracy 
 *
 * @author Eunsong Choi (eunsong.choi@gmail.com)
 * @version 1.0
 */
public interface LookupTable{

	public double get(double r);

}
