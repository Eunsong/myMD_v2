package mymd.datatype;

public class Angle{

    private int[] atoms; // sequence of covalently bonded atoms  
    private int func; // function type
    private double t0; // equilibirum angle (in radians) >>NOTE: gromacs input defualt unit is in degrees. 
    private double k0; // spring constant (in kJ/mol/rad^2) 
    private boolean t0set;
    private boolean k0set;


    public Angle(int i, int j, int k, int func) {
        this.atoms = new int[3];
        this.atoms[0] = i;
        this.atoms[1] = j;
        this.atoms[2] = k;
        this.func = func;
        this.t0set = false;
        this.k0set = false;
    }
    

    public Angle(int i, int j, int k, int func, double t0, double k0) {
        this.atoms = new int[3];
        this.atoms[0] = i;
        this.atoms[1] = j;
        this.atoms[2] = k;
        this.func = func;
        this.t0 = t0;
        this.k0 = k0;
        this.t0set = true;
        this.k0set = true;
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
    public int[] getAtoms() {
        return this.atoms;
    }

    public double getT0() {
        return this.t0;
    }
    public void setT0(double t0) {
        this.t0 = t0;
        this.t0set = true;
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
        return (this.t0set && this.k0set);
    }



    
}
