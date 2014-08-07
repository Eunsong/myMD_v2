package mymd.gromacs;


public class MdNonBondParam{

    private String typei;
    private String typej;
    private int func;
//  private int i; // atomic index(atomTypeId) of atom type i
//  private int j; // atomic index(atomTypeId) of atom type j

    /** note that Vij and Wij in atomtypes and sites can be either sigma/Vii and epsilon/Wii
    /* but for computational convenience, sigma and epsilon are converted to C6 and C12 in here **/
    private double Vij; // C6 if LJ 
    private double Wij; // C12 if LJ  

    public MdNonBondParam(String typei, String typej, int func, double Vij, double Wij) {
        this.typei = new String(typei);
        this.typej = new String(typej);
        this.func = func;
        this.Vij = Vij;
        this.Wij = Wij;
    }

    public String getiType(){
        return this.typei;
    }
    public String getjType(){
        return this.typej;
    }
//  public void seti(int i) {
//      this.i = i;
//  }
//  public void setj(int j) {
//      this.j = j;
//  }
//  public int geti() {
//      return this.i;
//  }
//  public int getj() {
//      return this.j;
//  }

    public int getFunc() {
        return this.func;
    }

    public double getVij() {
        return this.Vij;
    }
    public double getWij() {
        return this.Wij;
    }

}
