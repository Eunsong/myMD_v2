package mymd.angle;

import mymd.datatype.MdVector;
import mymd.MdSystem;

public abstract class AbstractAngle<T extends MdSystem<?>> implements Angle<T>{

	protected final int i, j, k;
	protected final double k0, theta0; // theta0 should be expressed in radians

	private final MdVector Uij = new MdVector();
	private final MdVector Ukj = new MdVector();

	protected AbstractAngle(int i, int j, int k, double k0, double theta0){
		this.i = i;
		this.j = j;
		this.k = k;
		this.k0 = k0;
		this.theta0 = theta0;
	}


	public double getAngleRadians(T sys){
		MdVector Ri = sys.getNewTraj().getPosition(i);
		MdVector Rj = sys.getNewTraj().getPosition(j);
		MdVector Rk = sys.getNewTraj().getPosition(k);
		MdVector box = sys.getNewTraj().getBox();

		Uij.copy(Ri).sub(Rj).minImage(box);
		Uij.normalize();
		Ukj.copy(Rk).sub(Rj).minImage(box);
		Ukj.normalize();
		double x = MdVector.dot(Uij, Ukj);
		double y = Math.sqrt( 1.0 - x*x);

		return Math.atan2(y, x);
	}

	public abstract void updateForce(T sys);

	public abstract double getEnergy(T sys);


	public int geti(){
		return this.i;
	}
    public int getj(){
        return this.j;
    }
	public int getk(){
		return this.k;
	}	
    public double getk0(){
        return this.k0;
	}
	public double getTheta0(){
		return this.theta0;
	}


}
