package mymd;

import mymd.datatype.*;

public class VelocityVerlet<T extends MdSystem<?>> implements Integrator<T>{

	private final double dt;

	public VelocityVerlet(double dt){
		this.dt = dt;
	}

	public void forwardPosition(T sys){
		Trajectory currTraj = sys.getCurrTraj();
		Trajectory newTraj = sys.getNewTraj();
		int N = currTraj.getSize();
		MdVector vdt = new MdVector();
		MdVector adt2 = new MdVector();
		MdVector box = currTraj.getBox();
		newTraj.setBox(box); // assumes fixed volume

		for ( int i = 0; i < N; i++){
			double m = sys.getParticle(i).getMass();
			MdVector newPosition = newTraj.getPosition(i);
			vdt.copy( currTraj.getVelocity(i) ).times(dt).done(); // v(t)*dt
			adt2.copy( currTraj.getForce(i) ).times(0.5*dt*dt/m).done(); // 0.5*a(t)+dt^2
			// x(t+dt) = x(t) + v(t)*dt + 0.5*a(t)*dt^2
			newPosition.copy(currTraj.getPosition(i)).add(vdt, adt2).done();
			pbc(box, newPosition );
		}	
	} 


	public void forwardPosition(T sys, MdParameter prm, Trajectory newTraj){
		throw new UnsupportedOperationException();
	}

	
	public void forwardVelocity(T sys){
	
		Trajectory currTraj = sys.getCurrTraj();
		Trajectory newTraj = sys.getNewTraj();
		int N = currTraj.getSize();
		MdVector adt = new MdVector(); // 0.5*a(t)*dt term
		MdVector atdtdt = new MdVector(); // 0.5*a(t+dt)*dt term

		for ( int i = 0; i < N; i ++) {
			double m = sys.getParticle(i).getMass();
			MdVector newV = newTraj.getVelocity(i); // v(t+dt)
			MdVector oldV = currTraj.getVelocity(i); // v(t)
			MdVector newF = newTraj.getForce(i); // F(t+dt)
			MdVector oldF = currTraj.getForce(i); // F(t)
			adt.copy(oldF).times(0.5*dt/m).done(); // 0.5*a(t)*dt
			atdtdt.copy(newF).times(0.5*dt/m).done(); // 0.5*a(t+dt)+dt
			// v(t+dt) = v(t)+0.5*a(t)*dt + 0.5*a(t+dt)*dt
			newV.copy(oldV).add( adt, atdtdt ).done();
		}

		// ADD RATTLE HERE
	}


    public void forwardVelocity(T sys, MdParameter prm, Trajectory newTraj){
		throw new UnsupportedOperationException();
	}



	private static void pbc(MdVector box, MdVector pos){
        if ( pos.getX() > box.getX() ) pos.setX( pos.getX() - box.getX() );
        else if ( pos.getX() < 0 ) pos.setX( pos.getX() + box.getX() );

        if ( pos.getY() > box.getY() ) pos.setY( pos.getY() - box.getY() );
        else if ( pos.getY() < 0 ) pos.setY( pos.getY() + box.getY() );

        if ( pos.getZ() > box.getZ() ) pos.setZ( pos.getZ() - box.getZ() );
        else if ( pos.getZ() < 0 ) pos.setZ( pos.getZ() + box.getZ() );
	}
}
