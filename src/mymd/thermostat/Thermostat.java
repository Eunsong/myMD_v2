package mymd.thermostat;

import mymd.MdSystem;

public interface Thermostat<T extends MdSystem<?>>{

    public double getRefTemp();


    /**
     * updates velocities in newTraj field in sys 
     */ 
    public void apply(T sys);



    public double getTemp(T sys);


}
