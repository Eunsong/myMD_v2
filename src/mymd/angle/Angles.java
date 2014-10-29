package mymd.angle;

import mymd.MdSystem;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;


public class Angles<T extends MdSystem<?>> implements Iterable<Angle<T>>{

    private List<Angle<T>> angles;

    public Angles(){
        this.angles = new ArrayList<Angle<T>>();
    }

    public void add(Angle<T> angle){
        this.angles.add(angle);
    }

    public Angle<T> get(int i){
        return this.angles.get(i);
    }

    public int size(){
        return this.angles.size();
    }

    public Iterator<Angle<T>> iterator(){
        return this.angles.iterator();
    }

    public void updateAllForces(T sys){
        for ( Angle<T> angle : angles ){
            angle.updateForce(sys);
        }
    }

    public double getTotalEnergy(T sys){
        double energy = 0.0;
        for ( Angle<T> angle : angles ){
            energy += angle.getEnergy(sys);
        }
        return energy;
    }


}
