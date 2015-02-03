package mymd.bond;

import mymd.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public class Bonds<T extends MdSystem<?>> implements Iterable<Bond<T>>{

    private List<Bond<T>> bonds;
    
    public Bonds(){
        this.bonds = new ArrayList<Bond<T>>();
    }   

    public void add(Bond<T> bond){
        this.bonds.add(bond);
    }

    public void remove(int i){
        this.bonds.remove(i);
    }
    
    public Bond<T> get(int i){
        return this.bonds.get(i);
    }

    public int size(){
        return this.bonds.size();
    }

    public Iterator<Bond<T>> iterator(){
        return this.bonds.iterator();
    }

    public void updateAllForces(T sys){
        for ( Bond<T> bond : bonds ){
            bond.updateForce(sys);
        }
    }

    public double getTotalEnergy(T sys){
        double energy = 0.0;
        for ( Bond<T> bond : bonds ){
            energy += bond.getEnergy(sys);
        }
        return energy;
    }
    

}
