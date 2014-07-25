package mymd.datatype;

public class Atom{

	// atom number
    private int nr;
	// atom type
    private String type;
	// atom name 
    private String name;
	// charge group number
    private int cgnr;
	// atom charge
    private double charge;
	// atom mass
    private double mass;

    public MdAtom() {
        // initialize charge and mass
        this.charge = -99.99;
        this.mass = -99.99;
    }

    public void setNr(int nr) {
        this.nr = nr;
    }
    public int getNr() {
        return this.nr;
    }

    public void setType(String type) {
        this.type = new String(type);
    }
    public String getType() {
        return this.type;
    }


}
