package mymd.gromacs;


public class MdNonBond {

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
