package mymd.dihedral;

import mymd.MdSystem;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;


public class Dihedrals<T extends MdSystem<?>> implements Iterable<Dihedral<T>>{

	private List<Dihedral<T>> dihedrals;

	public Dihedrals(){
		this.dihedrals = new ArrayList<Dihedral<T>>();
	}

	public void add(Dihedral<T> dihedral){
		this.dihedrals.add(dihedral);
	}

	public Dihedral<T> get(int i){
		return this.dihedrals.get(i);
	}

	public int size(){
		return this.dihedrals.size();
	}

	public Iterator<Dihedral<T>> iterator(){
		return this.dihedrals.iterator();
	}

	public void updateAllForces(T sys){
		for ( Dihedral<T> dihedral : dihedrals ){
			dihedral.updateForce(sys);
		}
	}
	
    public double getTotalEnergy(T sys){
        double energy = 0.0;
        for ( Dihedral<T> dihedral : dihedrals ){
            energy += dihedral.getEnergy(sys);
        }
        return energy;
    }
			














}
