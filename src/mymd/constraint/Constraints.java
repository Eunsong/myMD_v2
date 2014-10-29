package mymd.constraint;

import mymd.*;
import mymd.bond.Bonds;
import mymd.bond.Bond;
import mymd.datatype.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;


public class Constraints<T extends MdSystem<?>> implements Iterable<Constraint<T>>{

    private List<Constraint<T>> constraints;

    public Constraints(){
        this.constraints = new ArrayList<Constraint<T>>();
    }

    public void add(Constraint<T> constraint){
        this.constraints.add(constraint);
    }

    public Constraint<T> get(int i){
        return this.constraints.get(i);
    }

    public int size(){
        return this.constraints.size();
    }

    public Iterator<Constraint<T>> iterator(){
        return this.constraints.iterator();
    }

    @SuppressWarnings("unchecked")
    // uses SimpleShake 
    public void convertHBondsToConstraints(T sys){

        Topology top = sys.getTopology();
        Bonds<T> bonds = top.getBonds();
        Constraints<T> constraints = top.getConstraints();
        List<Integer> hbonds = new ArrayList<Integer>();
        int bond_index = 0;
        for ( Bond<T> bond : bonds ){
            int i = bond.geti();
            int j = bond.getj();
            String namei = sys.getParticle(i).getName();
            String namej = sys.getParticle(j).getName();
            if ( namei.substring(0,1).equals("H") || 
                 namej.substring(0,1).equals("H")){
                double b0 = bond.getb0();
                Constraint<T> constraint = new SimpleShake<T>(i, j, b0);
                constraints.add(constraint);        
                hbonds.add(bond_index);
            }   
            bond_index++;
        }

        // remove bonds those were converted to constraints from the bond list
        for ( int i = hbonds.size() - 1; i >= 0; i-- ) {
            int hbond_index = hbonds.get(i);
            top.getBonds().remove(hbond_index);
        }
    }

    public void updateAllPositions(T sys){
        for ( Constraint<T> constraint : constraints ){
            constraint.updatePosition(sys);
        }
    }


    public void updateAllVelocities(T sys){
        for ( Constraint<T> constraint : constraints ){
            constraint.updateVelocity(sys);
        }
    }



}
