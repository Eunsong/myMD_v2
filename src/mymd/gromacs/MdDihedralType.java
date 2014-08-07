package mymd.gromacs;


public class MdDihedralType {

    private String[] atomTypes;
    private int func;
    private double p0; // phi_s in type 1 dihedral 
    private double k0; 
    private int n; // n in type 1 dihedral
    private double[] C; 

    public MdDihedralType() {
        atomTypes = new String[4];
    }

    public void setAtomTypes(String atomi, String atomj, String atomk, String atoml) {
        this.atomTypes[0] = new String(atomi);
        this.atomTypes[1] = new String(atomj);
        this.atomTypes[2] = new String(atomk);
        this.atomTypes[3] = new String(atoml);
    }
    public String[] getAtomTypes() {
        return this.atomTypes;
    }

    public void setFunc(int func) {
        this.func = func;
    }
    public int getFunc() {
        return this.func;
    }

    public void setP0(double p0) {
        this.p0 = p0;
    }

    public double getP0() {
        return this.p0;
    }

    public void setK0(double k0) {
        this.k0 = k0;
    }
    public double getK0() {
        return this.k0;
    }

    public void setN(int n) {
        this.n = n;
    }
    public int getN() {
        return this.n;  
    }


    public void setC(double[] C) {
        this.C = new double[C.length];
        for ( int i = 0; i < C.length; i++ ) {
            this.C[i] = C[i];
        }
    }
    public double[] getC() {
        return this.C;
    }

}
