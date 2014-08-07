package mymd.gromacs;

public class MdAngleType {

    private String[] atomTypes;
    private int func;
    private double t0;
    private double k0;

    public MdAngleType() {
        atomTypes = new String[3];
    }

    public void setAtomTypes(String atomi, String atomj, String atomk) {
        this.atomTypes[0] = new String(atomi);
        this.atomTypes[1] = new String(atomj);
        this.atomTypes[2] = new String(atomk);
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

    public void setT0(double t0) {
        this.t0 = t0;
    }

    public double getT0() {
        return this.t0;
    }

    public void setK0(double k0) {
        this.k0 = k0;
    }
    public double getK0() {
        return this.k0;
    }



}
