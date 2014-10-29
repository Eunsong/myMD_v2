package mymd.angle;

import mymd.datatype.MdVector;
import mymd.MdSystem;
import mymd.Trajectory;


public class HarmonicAngle<T extends MdSystem<?>> extends AbstractAngle<T>{

    private final MdVector Rij = new MdVector();
    private final MdVector Rkj = new MdVector();
    private final MdVector Uik = new MdVector();
    private final MdVector Uij = new MdVector();
    private final MdVector Uki = new MdVector();
    private final MdVector Ukj = new MdVector();

    private final MdVector Fi = new MdVector();
    private final MdVector Fk = new MdVector();

    private final MdVector Ui_theta = new MdVector(); //i-site tangential vector
    private final MdVector Uk_theta = new MdVector(); //k-site tangential vector

    public HarmonicAngle(int i, int j, int k, double k0, double theta0){
        super(i, j, k, k0, theta0);
    }


    @Override public void updateForce(T sys){
        Trajectory traj = sys.getNewTraj();
        MdVector box = traj.getBox();
        double theta = super.getAngleRadians(sys);
        double Ftheta = -super.k0*(theta - super.theta0);

        MdVector Ri = traj.getPosition(i);
        MdVector Rj = traj.getPosition(j);
        MdVector Rk = traj.getPosition(k);
        Rij.copy(Ri).sub(Rj).minImage(box);
        Rkj.copy(Rk).sub(Rj).minImage(box); 

        Uik.copy(Ri).sub(Rk).minImage(box);
        Uik.normalize();
        Uij.copy(Rij).normalize();
        Uki.copy(Rk).sub(Ri).minImage(box);
        Uki.normalize();
        Ukj.copy(Rkj).normalize();
        
        // tangential vector of site i
        Ui_theta.copy(Uij).times(-MdVector.dot(Uij, Uik));
        Ui_theta.add(Uik).normalize();
        Fi.copySet( Ui_theta.times( Ftheta/(Rij.norm()) ) );
        traj.addForce(i, Fi);

        Uk_theta.copy(Ukj).times(-MdVector.dot(Ukj, Uki));
        Uk_theta.add(Uki).normalize();
        Fk.copySet( Uk_theta.times( Ftheta/(Rkj.norm()) ) );
        traj.addForce(k, Fk);

        traj.addReactionForce(j, Fi);
        traj.addReactionForce(j, Fk);       

    }


    @Override public double getEnergy(T sys){
        double theta = super.getAngleRadians(sys);
        return 0.5*super.k0*( theta - super.theta0 )*( theta - super.theta0 );
    }


}
