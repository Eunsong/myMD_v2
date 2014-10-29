package mymd.gromacs;


// File name : MdDihedral.java
// Author : Eunsong Choi (eunsong.choi@gmail.com)
// Last updated : 2013-11-08
// description : A class that contains necessary information to compute dihedral potential 

public class MdDihedral{

    private int[] atoms; // sequence of covalently bonded atoms  
    private int func;
    private double p0; // phi_s in type 1 dihedral 
    private double k0;
    private int n; // n in type 1 dihedral
    private double[] C; // equilibirum angles
    private boolean complete;


    public MdDihedral(int i, int j, int k, int l, int func) {
        this.atoms = new int[4];
        this.atoms[0] = i;
        this.atoms[1] = j;
        this.atoms[2] = k;
        this.atoms[3] = l;
        this.func = func;
        this.complete = false;
        if ( func == 99 ) {
            this.complete = true;
        }
    }
    



    // constructor for dihedral types that uses p0, k0 and n (e.g. type 1 )
    public MdDihedral(int i, int j, int k, int l, int func, double p0, double k0, int n) {
        this.atoms = new int[4];
        this.atoms[0] = i;
        this.atoms[1] = j;
        this.atoms[2] = k;
        this.atoms[3] = l;
        this.func = func;
        if ( func == 1 ) {
            this.p0 = p0;
            this.k0 = k0;
            this.n = n;
            this.complete = true;
        }
        else {
            System.out.println("Inappropriate constructor arguments for dihedral type " + func);
            System.exit(0);
        }
    }



    // constructor for dihedral types that uses C values (e.g. type 3 )
    public MdDihedral(int i, int j, int k, int l, int func, double[] C) {
        this.atoms = new int[4];
        this.atoms[0] = i;
        this.atoms[1] = j;
        this.atoms[2] = k;
        this.atoms[3] = l;
        this.func = func;
        if ( func == 3 ) {
            this.C = new double[C.length];
            for ( int n = 0; n < C.length; n++ ) {
                    this.C[n] = C[n];
            }
            this.complete = true;
        }
        else {
            System.out.println("Inappropriate constructor arguments for dihedral type " + func);
            System.exit(0);
        }
    }


    public int geti() {
        return this.atoms[0];
    }
    public int getj() {
        return this.atoms[1];
    }
    public int getk() {
        return this.atoms[2];
    }
    public int getl() {
        return this.atoms[3];
    }
    public int[] getAtoms() {
        return this.atoms;
    }

    public double[] getC() {
        return this.C;
    }
    
    public double getP0() {
        return this.p0;
    }

    public double getK0() {
        return this.k0;
    }

    public int getN() {
        return this.n;
    }

    public void setC(double[] C) {
        this.C = new double[C.length];
        for ( int i = 0; i < C.length; i++ ) {
            this.C[i] = C[i];
        }
        this.complete = true;
    }

    public void setParam(double p0, double k0, int n) {
        this.p0 = p0;
        this.k0 = k0;
        this.n = n;
        if ( this.func == 1 ) {
            this.complete = true;
        }
    }

    public int getFunc() {
        return this.func;
    }

    public boolean isComplete() {
        return this.complete;
    }








}
