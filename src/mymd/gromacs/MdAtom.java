package mymd.gromacs;

public class MdAtom {

	private int nr;
	private String type;
	private int resnr;
	private String resName;
	private String name;
	private int cgnr;
	private double charge;
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
	

    public void setType(String name) {
        this.type = new String(name);
    }
    public String getType() {
        return this.type;
    }



	public void setResNr(int resnr) {
		this.resnr = resnr;
	}
	public int getResNr() {
		return this.resnr;
	}


    public void setResName(String name) {
        this.resName = new String(name);
    }
    public String getResName() {
        return this.resName;
    }


    public void setName(String name) {
        this.name = new String(name);
    }
    public String getName() {
        return this.name;
    }


	public void setCgNr(int cgnr) {
		this.cgnr = cgnr;
	}
	public int getCgNr() {
		return this.cgnr;
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



}

