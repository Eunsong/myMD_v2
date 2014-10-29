package mymd.angle;
	
import mymd.MdSystem;

public interface Angle<T extends MdSystem<?>>{

	public int geti();
	public int getj();
	public int getk();

	public double getk0();
	
	public double getTheta0();

	/**
	 * getAngleRadians() method returns an angle i-j-k in radians
	 */
	public double getAngleRadians(T sys); 

	public void updateForce(T sys);

	public double getEnergy(T sys); 


}
