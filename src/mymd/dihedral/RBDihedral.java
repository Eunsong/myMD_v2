package mymd.dihedral;

import mymd.datatype.MdVector;
import mymd.MdSystem;
import mymd.Trajectory;


public class RBDihedral<T extends MdSystem<?>> extends AbstractDihedral<T>{

	private final double[] C;

    private final MdVector Rij = new MdVector();
    private final MdVector Rkj = new MdVector();
    private final MdVector Rkl = new MdVector();
    private final MdVector n1 = new MdVector();
    private final MdVector n2 = new MdVector();

    private final MdVector Fi = new MdVector();
    private final MdVector Fj = new MdVector();
	private final MdVector Fk = new MdVector();
	private final MdVector Fl = new MdVector();
	
	private final MdVector temp1 = new MdVector();
	private final MdVector temp2 = new MdVector();

	public RBDihedral(int i, int j, int k, int l, double[] C){
		super(i, j, k, l);
		if ( C.length != 6 ) throw new IllegalArgumentException
		("input array for RBDihedral has incorrect number of elements." +
		 " Must have six components.");
		this.C = C;
	}


	/**
	 * updateForce method computes force components of this dihedral object
	 * and add results to force fields in newTraj.
	 */
	@Override public void updateForce(T sys){
		Trajectory traj = sys.getNewTraj();
		MdVector box = traj.getBox();

        MdVector Ri = traj.getPosition(i);
        MdVector Rj = traj.getPosition(j);
        MdVector Rk = traj.getPosition(k);
        MdVector Rl = traj.getPosition(l);

        Rij.copy(Ri).sub(Rj).minImage(box);
        Rkj.copy(Rk).sub(Rj).minImage(box);
        Rkl.copy(Rk).sub(Rl).minImage(box);

        n1.copy(Rij).crossSet(Rkj);
        n2.copy(Rkj).crossSet(Rkl);


		double phi = super.getAngleRadians(sys);
		double psi = phi - Math.PI;
		double Fphi = 0.0;
		for ( int n = 1; n < 6; n++){
			Fphi += C[n]*n*Math.pow( Math.cos(psi), n-1 )*Math.sin(psi);
		}

		Fi.copy(n1).times( -Fphi*Rkj.norm()/n1.normsq() );
		Fl.copy(n2).times( Fphi*Rkj.norm()/n2.normsq() );

		double RkjNormSquared = Rkj.normsq();		
		double Rij_dot_Rkj_term = MdVector.dot( Rij, Rkj)/RkjNormSquared;
		double Rkl_dot_Rkj_term = MdVector.dot( Rkl, Rkj)/RkjNormSquared;
	
		temp1.copy(Fi).timesSet( Rij_dot_Rkj_term - 1.0 );
		temp2.copy(Fl).timesSet( Rkl_dot_Rkj_term);
		Fj.copy(temp1).subSet(temp2);
		
		temp1.copy(Fl).timesSet( Rkl_dot_Rkj_term - 1.0);
		temp2.copy(Fi).timesSet( Rij_dot_Rkj_term);
		Fk.copy(temp1).subSet(temp2);

		traj.addForce(i, Fi);
		traj.addForce(j, Fj);
		traj.addForce(k, Fk);
		traj.addForce(l, Fl);
	}


	
	@Override public double getEnergy(T sys){
		double phi = super.getAngleRadians(sys);
		double psi = phi - Math.PI;
		double energy = 0.0;
		for ( int n = 0; n < 6; n++){
			energy += C[n]*Math.pow( Math.cos(psi), n);
		}
		return energy;
	}



}
