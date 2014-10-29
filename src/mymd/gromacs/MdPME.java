package mymd.gromacs;

// REF. : U. Essmann, L.Perera, and M.L. Berkowitz, J. Chem. Phys. 103(19), 1995, 8577

/**
 * Particle Mesh Ewald(PME) Summation class for Molecular simulations
 * supporting parallel computation
 * 
 * @author : Eunsong Choi (echoi9@wisc.edu)
 */


import java.util.*;


public class MdPME{

    public static final double f = 138.935485; // electric conversion factor
    private final int Nbin = 1000000; // number of bins in lookup table.
    private final double w; // frequently appearing constant w = 2/sqrt(pi)

    private double[] Q;
    private double[] theta_rec_Q; // convolution invF(B.C)*Q
    private double[] BsplineLookup;
    private double[] dBsplineLookup;
    private double[] bSqLookup; 
    private double[] dirForceLookup;
    private double[] corrForceLookup;
    private double[] dirEnergyLookup;

    private List<List<Integer>> exclusions; // list of exclusions 
    private int K; // numbers of grids in each direction
    private int n; // beta spline interpolation order 
    private double c; // conversion factor between
                      // Bspline input and lookup table input index

    private double beta; // Gaussian distribution width. in units of nm^-1



    public MdPME(GromacsSystem system, MdTop top, MdPrm prm, int n, int K, double beta) {

        this.n = n;
        this.K = K;
        this.beta = beta;
        this.c = (double)this.Nbin/(double)n;
        this.w = 2.0/Math.sqrt(Math.PI);


    }



    public void compDirForce(GromacsSystem sys, MdTop top, MdPrm prm, MdNbList nblist, MdTraj newTraj) {

    }


    public void compCorrForce(GromacsSystem sys, MdTop top, MdPrm prm, MdNbList nblist, MdTraj newTraj) {

    }



    public Vector dirForceFast(Vector box, double rc, double qi, double qj,  Vector ri, Vector rj) {
    
        return new Vector();
    }

    public Vector corrForceFast(Vector box, double rc, double qi, double qj,  Vector ri, Vector rj) {
        return new Vector();
    }



    public Vector dirForce(Vector box, double qi, double qj,  Vector ri, Vector rj) {
    
		return new Vector();
    }


    public Vector corrForce(Vector box, double qi, double qj,  Vector ri, Vector rj) {
    
		return new Vector();
    }



    public double compDirE(GromacsSystem sys, MdTop top, MdPrm prm, MdNbList nblist, MdTraj newTraj) {

        return 0.0;
    }
    




    public double compCorrE(GromacsSystem system, MdNbList nblist, MdTraj newTraj) {
	
		return 0.0;
    }


    public void compRecForce(GromacsSystem sys, MdTop top, MdPrm prm, MdNbList nblist, MdTraj newTraj) {

        int nthread = 4;
        int threadsBegin3D = 65636;



    }



    public double compRecE(GromacsSystem system, MdTop top, MdPrm prm, MdNbList nblist, MdTraj newTraj) {

		return 0.0;
    }






    /**
     *
     * 
     *
     *
     */
    public void compQ(GromacsSystem system, MdTraj newTraj) {

    } 




    public static double Bspline(int n, double u) {
		return 0.0;
    }


    // derivative of Bspline with respect to u, i.e. dMn(u)/du
    public static double dBspline(int n, double u) {
        return Bspline(n-1, u) - Bspline(n-1, u-1.0);
    }



    public static double bSq(int n, int K, int mi) {

        double rval = 0.0; // real part of denominator
        double ival = 0.0; // imaginary part of denominator
        for ( int k = 0; k < n-1; k++) {
            rval += Bspline(n, k+1)*Math.cos(2*Math.PI*(double)mi*(double)k/(double)K);
            ival += Bspline(n, k+1)*Math.sin(2*Math.PI*(double)mi*(double)k/(double)K);
        }
        
        return 1.0/(rval*rval + ival*ival);
    }











}
