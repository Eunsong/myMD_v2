package mymd.dihedral;

import mymd.MdSystem;

public interface Dihedral<T extends MdSystem<?>>{

	public int geti();
	public int getj();
	public int getk();
	public int getl();

	public double getAngleRadians(T sys);

	public void updateForce(T sys);

	public double getEnergy(T sys);


}
