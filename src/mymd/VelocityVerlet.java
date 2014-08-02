package mymd;

public class VelocityVerlet<E extends MdVector, T extends MdSystem<?, ?>> 
												 implements Integrator<T>{

	private final double dt;

	public VelocityVerlet(double dt){
		this.dt = dt;
	}

	public void forwardPosition(T sys){
		Trajectory currTraj = sys.getCurrTraj();
		Trajectory newTraj = sys.getNewTraj();
		newTraj.resetPositions(); // reset new positions to 0
		int N = currTraj.getSize();
		E vdt = E.create();
		E adt2 = E.create();
		E box = currTraj.getBox();

		for ( int i = 0; i < N; i++){
			double m = sys.getParticle(i).getMass();
			E newPosition = newTraj.getPosition(i);
			vdt.copy( currTraj.getVel(i) );
			vdt.times(dt); // v(t)*dt
			adt2.copy( currTraj.getForce(i) );
			adt2.times(0.5*dt*dt/m); // 0.5*a(t)+dt^2
			newPosition.add( currTraj.getPosition(i), vdt, adt2);
			pbc(box, newPosition );
		}	
	} 


	public void forwardPosition(T sys, MdParam prm, Trajectory<?> newTraj){
		throw new UnsupportedOperationException();
	}

	
	public void forwardVelocity(T sys){
	
		Trajectory currTraj = sys.getCurrTraj();
		Trajectory newTraj = sys.getNewTraj();
		int N = currTraj.getSize();
		E adt = E.create(); // 0.5*a(t)*dt term
		E atdtdt = E.create(); // 0.5*a(t+dt)*dt term
		newTraj.resetVelocities(); // reset new velocities to 0

		for ( int i = 0; i < N; i ++) {
			double m = sys.getParticle(i).getMass();
			E newV = newTraj.getVelocity(i); // v(t+dt)
			E oldV = currTraj.getVelocity(i); // v(t)
			E newF = newTraj.getForce(i); // F(t+dt)
			E oldF = currTraj.getForce(i); // F(t)
			adt.copy(oldF);
			adt.times(0.5*dt/m); // 0.5*a(t)*dt
			atdtdt.copy(newF);
			atdtdt.times(0.5*dt/m); // 0.5*a(t+dt)+dt
			newV.add( oldV, adt, adtdt );
		}

		// ADD RATTLE HERE
	}


    public void forwardVelocity(E sys, MdParam prm, Trajectory<?> newTraj){
		throw new UnsupportedOperationException();
	}



	private static void pbc(E box, E pos){
        if ( pos.getX() > box.getX() ) pos.getX() = pos.getX() - box.getX();
        else if ( pos.getX() < 0 ) pos.getX() = pos.getX() + box.getX();

        if ( pos.getY() > box.getY() ) pos.getY() = pos.getY() - box.getY();
        else if ( pos.getY() < 0 ) pos.getY() = pos.getY() + box.getY();

        if ( pos.getZ() > box.getZ() ) pos.getZ() = pos.getZ() - box.getZ();
        else if ( pos.getZ() < 0 ) pos.getZ() = pos.getZ() + box.getZ();
	}
}
