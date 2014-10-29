package mymd.gromacs;

public class MdAtomType {

    private String typeName;
    private String atomName; // optional. Some topology file defines atom name in atomtype field
    private int typeId;
    private double Wii; // epsilon, C12 etc
    private double Vii; // sigma, C6 etc
    private double mass;
    private double charge;
    private String ptype;

    public void setType(String name) {
        this.typeName = new String(name);
    }

    public String getType() {
        return this.typeName;
    }

    public void setTypeId(int id) {
        this.typeId = id;
    }

    public int getTypeId() {
        return this.typeId;
    }
    
    public void setAtomName(String name) {
        this.atomName = new String(name);
    }
    public String getAtomName() {
        return this.atomName;
    }
    
    public void setWii(double wii) {
        this.Wii = wii;
    }
    public double getWii() {
        return this.Wii;
    }

    public void setVii(double vii) {
        this.Vii = vii;
    }
    public double getVii() {
        return this.Vii;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }
    public double getMass() {
        return this.mass;
    }
    
    public void setCharge(double charge) {
        this.charge = charge;
    }
    public double getCharge() {
        return this.charge;
    }

    public void setPType(String type) {
        this.ptype = type;
    }
    public String getPType() {
        return this.ptype;
    }


}
