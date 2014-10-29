package mymd.dihedral;

import mymd.datatype.MdVector;
import mymd.MdSystem;


public abstract class AbstractDihedral<T extends MdSystem<?>> implements Dihedral<T>{

	protected final int i, j, k, l;
	
	private final MdVector Rij = new MdVector();
	private final MdVector Rkj = new MdVector();
	private final MdVector Rkl = new MdVector();
	private final MdVector n1 = new MdVector();
	private final MdVector n2 = new MdVector();
	private final MdVector b2 = new MdVector();
	private final MdVector m1 = new MdVector();

	protected AbstractDihedral(int i, int j, int k, int l){
		this.i = i;
		this.j = j;
		this.k = k;
		this.l = l;
	}

    public int geti(){
        return this.i;
    }
    public int getj(){
        return this.j;
    }
    public int getk(){
        return this.k;
    }
	public int getl(){
		return this.l;
	}
	
	public double getAngleRadians(T sys){

        MdVector Ri = sys.getNewTraj().getPosition(i);
        MdVector Rj = sys.getNewTraj().getPosition(j);
        MdVector Rk = sys.getNewTraj().getPosition(k);
		MdVector Rl = sys.getNewTraj().getPosition(l);
		MdVector box = sys.getNewTraj().getBox();

		Rij.copy(Ri).sub(Rj).minImage(box);
		Rkj.copy(Rk).sub(Rj).minImage(box);
		Rkl.copy(Rk).sub(Rl).minImage(box);
	
		n1.copy(Rij).cross(Rkj).normalize();
		n2.copy(Rkj).cross(Rkl).normalize();
	
		b2.copy(Rkj).normalize();
		m1.copy(n1).crossSet(b2);	

		double x = MdVector.dot(n1, n2);
		double y = MdVector.dot(m1, n2);

		return Math.atan2(y, x);
	}
	

	public abstract void updateForce(T sys);
	
	public abstract double getEnergy(T sys);

}
