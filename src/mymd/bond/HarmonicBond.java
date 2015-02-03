package mymd.bond;

import mymd.datatype.MdVector;
import mymd.MdSystem;
import mymd.Trajectory;

public class HarmonicBond<T extends MdSystem<?>> extends AbstractBond<T>{

    private final MdVector Rij = new MdVector();
    private final MdVector Fij = new MdVector();

    public HarmonicBond(int i, int j, double k0, double b0){
        super(i, j, k0, b0);
    } 

    public void updateForce(T sys){
        Trajectory traj = sys.getNewTraj();
        MdVector box = traj.getBox();
        MdVector Ri = traj.getPosition(super.i);
        MdVector Rj = traj.getPosition(super.j);
        Rij.copy(Ri).sub(Rj).minImage(box);
        double r = Rij.norm();
        Fij.copy(Rij).times( -super.k0 * ( r - super.b0 )/r );
        traj.addForce(super.i, Fij);
        traj.addReactionForce(super.j, Fij);
    }

    public double getEnergy(T sys){
        Trajectory traj = sys.getNewTraj();
        MdVector box = traj.getBox();
        MdVector Ri = traj.getPosition(super.i);
        MdVector Rj = traj.getPosition(super.j);
        Rij.copy(Ri).sub(Rj).minImage(box);
        double r = Rij.norm();
        return 0.5*super.k0*(r - super.b0)*(r - super.b0);
    }

}
