package mymd.thermostat;

import mymd.MdSystem;


public abstract class AbstractThermostat<T extends MdSystem<?>> 
										implements Thermostat<T>{

	protected static final double kb = 0.0083144621; // Boltzmann constant(kJ/mol/K)
	protected final double TRef; // reference temperature (in Kelvin)

	protected AbstractThermostat(double TRef){
		this.TRef = TRef;
	} 

	public double getRefTemp(){
		return this.TRef;
	}

	/**
	 * getTemp method computes kinetic energy of sys using newTraj field,
	 * returns the result in a temperature unit(Kelvin).
	 *
	 * @param sys
	 * @return 
	 */
	public double getTemp(T sys){
        double kineticEnergy = sys.getKineticEnergy();
        double T = convertEnergyToTemp(sys, kineticEnergy);
		return T;
	}

	
	protected static <T extends MdSystem<?>> double convertEnergyToTemp
												 (T sys, double energy){
		int N = sys.getSize();
		return 2.0/(3.0*kb)*energy/(double)N;
	}


	public abstract void apply(T sys);

}
