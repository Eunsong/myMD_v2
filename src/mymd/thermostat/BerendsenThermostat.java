package mymd.thermostat;

import mymd.MdSystem;
import mymd.Trajectory;

public class BerendsenThermostat<T extends MdSystem<?>> extends AbstractThermostat<T>{

	private final double tauT; // coupling time constant (in ps)

	public BerendsenThermostat(double TRef, double tauT){
		super(TRef);
		this.tauT = tauT;
	}

	
	/**
	 * updates velocities in newTraj in sys using Berendsen's algorithm.
	 * This method must be invoked after updating new velocities in sys,
	 * and prior to calling update() method in sys. 
	 */
	@Override public void apply(T sys){
		Trajectory traj = sys.getNewTraj();
		double dt = sys.getParam().getDt();
		double kineticEnergy = sys.getKineticEnergy();
		double Tnew = super.convertEnergyToTemp(sys, kineticEnergy);
		double lambda = Math.sqrt( 1.0 + dt/tauT * ( TRef/Tnew - 1.0 ) ); 

		for ( int i = 0; i < traj.getSize(); i++){
			traj.getVelocity(i).timesSet( lambda );
		}		
	}



}
