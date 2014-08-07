package mymd.gromacs;
//  File name : MdSite.java
//  Author : Eunsong Choi(eunsong.choi@gmail.com)
//  Last updated : 2013-10-17 

public class MdSite {

    // all IDs and nrs start from 0
    private int atomId;  // unique atom ID
    private String atomName; // atom name (e.g. OW, H, N1 etc)
    private String atomType; // atom type (e.g. SN0, opls001 etc) 
    private int atomTypeId; // needed to specify atom type quickly during the simulation
    private String resName; // residue name (e.g. BF4, SOL etc)
    private int resId; // residue ID ( e.g. 1BF4, 2BF4, 3BF4 1,2,and 3 in here are resIDs) 
    private String molName; // molecule name
    private int molId; // molecule id 
    private int molTypeId; 
    private double mass; // mass of the atom
    private double charge; // charge of the atom
    private int nr; // atom index in the molecule it is beloing to. Similar to nr defined in [ atoms ] in *.itp file of Gromacs. 
                       // needed to apply bonded potetnail appropriately. 
    private int cgnr; // charge group number. 

    private double Wii; // epsilon, C12 etc 
    private double Vii; // sigma, C6 etc


    public MdSite() {
    }
/*
    public MdSite(MdSite site) {
        this.atomId = site.atomId;
        this.atomName = new String(site.atomName);
        this.atomType = new String(site.atomType);
        this.atomTypeId = site.atomTypeId;
        this.resName = new String(site.resName);
        this.resId = site.resId;
        this.molName = new String(site.molName);
        this.molId = site.molId;
        this.mass = site.mass;
        this.charge = site.charge;
        this.nr = site.nr;
    }
*/
    public void setAtomId(int id) {
        this.atomId = id;
    }
    public int getAtomId() {
        return this.atomId;
    }

    public void setAtomName(String name) {
        this.atomName = new String(name);
    }
    public String getAtomName() {
        return this.atomName;
    }

    public void setAtomType(String type) {
        this.atomType = new String(type);
    }
    public String getAtomType() {
        return this.atomType;
    }

    public void setAtomTypeId(int id) {
        this.atomTypeId = id;
    }
    public int getAtomTypeId() {
        return this.atomTypeId;
    }


    public void setMolTypeId(int id) {
        this.molTypeId = id;
    }
    public int getMolTypeId() {
        return this.molTypeId;
    }

    public void setResId(int id) {
        this.resId = id;
    }
    public int getResId() {
        return this.resId;
    }   

    public void setResName(String type) {
        this.resName = new String(type);
    }
    public String getResName() {
        return this.resName;
    }

    public void setMolName(String name) {
        this.molName = new String(name);
    }
    public String getMolName() {
        return this.molName;
    }


    public void setMolId(int id) {
        this.molId = id;
    }
    public int getMolId() {
        return this.molId;
    }

    

    
    public void setMass(double mass) {
        this.mass = mass;
    }

    public double getMass() {
        return this.mass;
    }

    public void setCharge(double q) {
        this.charge = q;
    }

    public double getCharge() {
        return this.charge;
    }

    public void setNr(int nr) {
        this.nr = nr;
    }

    public int getNr() {
        return this.nr;
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

}
