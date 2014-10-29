package mymd.datatype;

import java.util.*;

public class Constraint{

    private int atomi;
    private int atomj;
    private int ftype;
    private double dij;
    private double lambda; 

    public Constraint(int ai, int aj, int ftype, double b0){
        this.atomi = ai;
        this.atomj = aj;
        this.ftype = ftype;
        this.dij = b0;
    }

    // convert all bonds involving hydrogen atom to constraints and add them to Top
    public static void convertHbondsToConstraints(System sys, Top top) {
    
        // store bond index which contains H atom (and remove these from bond list
        List<Integer> hbonds = new ArrayList<Integer>();
        int bond_index = 0;

        for ( Bond bond : top.getBonds() ) {
            int i = bond.geti();
            int j = bond.getj();
            String atomi = new String(sys.getSite(i).getAtomName());
            String atomj = new String(sys.getSite(j).getAtomName());
            double b0 = bond.getB0();

            // if at least one of the atoms' name start with "H" 
            if ( atomi.substring(0,1).equals("H") || atomj.substring(0,1).equals("H")) {
                Constraint newConstraint = new Constraint(i, j, 1, b0);
                top.addConstraint(newConstraint);
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

    public int geti(){
        return this.atomi;
    }
    public int getj(){
        return this.atomj;
    }
    public int[] getij(){
        int[] ij = {this.atomi, this.atomj};
        return ij;
    }
    public double getDij(){
        return this.dij;
    }
    public int getFunc(){
        return this.ftype;
    }
    public double getLambda(){
        return this.lambda;
    }
    public void setLambda(double lambda){
        this.lambda = lambda;
    }

    //public void applyConstraints(System sys, Top top, Prm prm, Traj newTraj);


    public static void applySingleConstraint(System sys, Top top, Prm prm, Traj newTraj){

        List<Constraint> constraints = top.getConstraints();
        double tol = prm.getCTol();
        double tolSq = tol*tol;     
        double maxItr = prm.getMaxCItr();
        double dt = prm.getDt();
        double dtSq = dt*dt;
        Vector box = sys.getBox();

        for ( Constraint constraint : constraints ) {

            int i = constraint.geti();
            int j = constraint.getj();
            double b = constraint.getDij();
    
            Vector ri_old = sys.getPos(i);
            Vector rj_old = sys.getPos(j);
            Vector rjImage_old = Force.minImage(box, ri_old, rj_old);
            Vector ri_new = newTraj.getPos(i);
            Vector rj_new = newTraj.getPos(j);
            Vector rjImage_new = Force.minImage(box, ri_new, rj_new);
            Vector rij_new = Vector.sub(ri_new, rjImage_new);
            Vector rij_old = Vector.sub(ri_old, rjImage_old);

            double mi_inv = 1.0/sys.getSite(i).getMass();
            double mj_inv = 1.0/sys.getSite(j).getMass();

            double rijSq = rij_new.normsq();
            int itr = 0; // number of iterations so far

            while ( Math.abs(rijSq - b*b) > tolSq && itr < maxItr) {

                double denom = -2.0*Vector.dot( rij_new, rij_old)*dtSq*(mi_inv + mj_inv);
                double num = b*b - rij_new.normsq();
                constraint.setLambda(num/denom);
                double lambda = constraint.getLambda();

                ri_new.sub(rij_old.times(lambda*dtSq*mi_inv));
                rj_new.add(rij_old.times(lambda*dtSq*mj_inv)); 
                rjImage_new = Force.minImage(box, ri_new, rj_new);
                rij_new = Vector.sub( ri_new, rjImage_new );
                rijSq = rij_new.normsq();
                itr++;
//System.out.println(String.format("Iteration %d : r(%d,%d)-d = %f", itr, i, j, Math.abs(rijSq - b*b)));
            }
            if ( itr == maxItr) {
                System.out.println("SHAKE WARNING! a (single)constraint did not converge in " + maxItr + " iterations!");
            }
        
            // apply periodic boundary condition
            Traj.pbc(box, ri_new);
            Traj.pbc(box, rj_new);

        }   


    }

}
