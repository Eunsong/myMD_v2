package mymd.gromacs;

import java.util.*;

public class MdLJ{

    private final int Nbin = 1000000; // number of bins in lookup table
    
    private double[] C6ForceLookup;
    private double[] C12ForceLookup;
    private double rvdw;

    public MdLJ(GromacsSystem system, MdTop top, MdPrm prm) {

        this.rvdw = prm.getRvdw();
        
        /** Construct a lookup table for C6 and C12 Force term **/
        this.C6ForceLookup = new double[Nbin];
        this.C12ForceLookup = new double[Nbin];

        if ( prm.getVdwType().equals("cut-off") ){
            for ( int i = 0; i < this.Nbin; i++) {
                double r = rvdw*(double)(i)/(double)this.Nbin;
                this.C6ForceLookup[i] = 6.0*Math.pow( (1.0/r), 8); 
                this.C12ForceLookup[i] = 12.0*Math.pow( (1.0/r), 14); 
            }
        }
        else {
            System.out.println("ERROR! Unsupported vdw type : " + prm.getVdwType());
            System.exit(0);
        }
    }



    public Vector LJForceFast(Vector box, double rvdw, double Vij, double Wij,  Vector ri, Vector rj) {

        Vector rjImage = MdForce.minImage(box, ri, rj);
        Vector rij = Vector.sub( ri , rjImage);
        double r = rij.norm();

        double nreal = (r/rvdw)*(double)(Nbin);
        int n = (int)nreal;
        if ( n + 1 < Nbin ) {
            double frac = nreal - (double)n;
            double coeff = Wij*(C12ForceLookup[n] + frac*(C12ForceLookup[n+1] - C12ForceLookup[n]))
                            - Vij*(C6ForceLookup[n] + frac*(C6ForceLookup[n+1] - C6ForceLookup[n])); 
            return rij.times( coeff ); 
        }
        return new Vector();
    

    }




    /*********************** LJ energy *****************************************/
    public static double LJEnergy(Vector box, double C6, double C12, Vector ri, Vector rj) {

        Vector rjImage = MdForce.minImage(box, ri, rj);
        double rij2 = Vector.sub( ri, rjImage).normsq();
        double c12term = C12*Math.pow((1.0/rij2),6);
        double c6term = C6*Math.pow((1.0/rij2),3);
        return (c12term - c6term);

    }


    /*** Calculate LJ force acting on a particle at site i due to the particle at site j ***/
    public static Vector LJForce(Vector box, double C6, double C12, Vector ri, Vector rj) {

        Vector rjImage = MdForce.minImage(box, ri, rj);
        double rij2 = Vector.sub( ri , rjImage).normsq();
        Vector rij = Vector.sub( ri , rjImage);
        double c12term = C12*12.0*Math.pow((1.0/rij2),6)/rij2;
        double c6term = C6*6.0*Math.pow((1.0/rij2),3)/rij2;
        return rij.times( c12term - c6term );

    }


    /*********************** LJ energy *****************************************/
    public static double LJShiftedEnergy(Vector box, double rvdw, double C6, double C12, Vector ri, Vector rj) {

        double rvdw2 = Math.pow(rvdw,2);
        Vector rjImage = MdForce.minImage(box, ri, rj);
        double rij2 = Vector.sub( ri, rjImage).normsq();
        double c12term = C12*( Math.pow((1.0/rij2),6) - Math.pow((1.0/rvdw2),6)) ;
        double c6term = C6*( Math.pow((1.0/rij2),3) - Math.pow((1.0/rvdw2),3));
        return (c12term - c6term);

    }


    /*** Calculate LJ force acting on a particle at site i due to the particle at site j ***/
    public static Vector LJShiftedForce(Vector box, double rvdw, double C6, double C12, Vector ri, Vector rj) {

        double rvdw2 = Math.pow(rvdw,2);
        Vector rjImage = MdForce.minImage(box, ri, rj);
        double rij2 = Vector.sub( ri, rjImage).normsq();
        Vector rij = Vector.sub( ri, rjImage);
        double c12term = C12*12.0*( Math.pow((1.0/rij2),7) - Math.pow((1.0/rvdw2),7) );
        double c6term = C6*6.0*( Math.pow((1.0/rij2),4) - Math.pow((1.0/rvdw2),4) );
        return rij.times( c12term - c6term );

    }






}
