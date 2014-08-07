package mymd.gromacs;

public class MdBondType {
    
    private String[] atomTypes;
    private int func;
    private double b0;
    private double k0;

    public MdBondType() {
        atomTypes = new String[2];
    }

    public void setAtomTypes(String atomi, String atomj) {
        this.atomTypes[0] = new String(atomi);
        this.atomTypes[1] = new String(atomj);
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

    public void setB0(double b0) {
        this.b0 = b0;
    }

    public double getB0() {
        return this.b0;
    }

    public void setK0(double k0) {
        this.k0 = k0;
    }
    public double getK0() {
        return this.k0;
    }


}
