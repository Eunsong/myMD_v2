package mymd.gromacs;

// File name : MdBond.java
// Author : Eunsong Choi (eunsong.choi@gmail.com)
// Last updated : 2013-11-08
// description : A class that contains necessary information to compute bond potential 

public class MdBond{

    private int[] atoms; // bonded atom pair 
    private int func; // function type
    private double b0; // equilibirum bond length
    private double k0; // spring constant
    private boolean b0set; // whether b0 value is given 
    private boolean k0set; // whetehr k0 value is given

    public MdBond(int i, int j, int func) {
        this.atoms = new int[2];
        this.atoms[0] = i;
        this.atoms[1] = j;
        this.func = func;
        this.b0set = false;
        this.k0set = false;
    }
    

    public MdBond(int i, int j, int func, double b0, double k0) {
        this.atoms = new int[2];
        this.atoms[0] = i;
        this.atoms[1] = j;
        this.func = func;
        this.b0 = b0;
        this.k0 = k0;
        this.b0set = true;
        this.k0set = true;
    }


    public int geti() {
        return this.atoms[0];
    }
    public int getj() {
        return this.atoms[1];
    }
    public int[] getAtoms() {
        return this.atoms;
    }
    public double getB0() {
        return this.b0;
    }
    public void setB0(double b0) {
        this.b0 = b0;
        this.b0set = true;
    }

    public double getK0() {
        return this.k0;
    }
    public void setK0(double k0) {
        this.k0 = k0;
        this.k0set = true;
    }
    
    public int getFunc() {
        return this.func;
    }

    public boolean isComplete() {
        return (this.b0set && this.k0set);
    }



    /************************* Bond Potential and Force functions ******************************/

    // type 1 bond potential (harmonic potnetial)
    public static double energyType1(Vector box, double b0, double k0, Vector ri, Vector rj) {

        Vector rjImage = MdForce.minImage(box, ri, rj);
        double rij = Vector.sub( ri, rjImage).norm();
        return 0.5*k0*Math.pow((rij-b0),2);

    }


    // type 1 bond force (harmonic type)
    // Force actiong on a particle at site i due to the particle at site j
    public static Vector forceType1(Vector box, double b0, double k0, Vector ri, Vector rj) {

        Vector rjImage = MdForce.minImage(box, ri, rj);
        Vector rij = Vector.sub( ri, rjImage);
        double r = rij.norm();
        return rij.times( -k0*(r-b0)/r );
    
    }

}
