package mymd.gromacs;

import java.util.*;

public class MdForce {

    
    private MdLJ myLJ;
    private MdPME myPME;
    private int nonBondType; // type of nonbond potentials to be computed (e.g. LJ, PME, SAPT etc)
                             // type 1 : LJ + PME
                             // type 2 : LJ

    public MdForce(GromacsSystem system, MdTop top, MdPrm prm) {
        
    }   



    public void compNbForce(GromacsSystem sys, MdTop top, MdPrm prm, MdNbList nblist, MdTraj newTraj) {
    }



    /*** Compute total nonbonded force(LJ + PME) acting on each particle and update force ***/
    public void compNbForceType1(GromacsSystem sys, MdTop top, MdPrm prm, MdNbList nblist, MdTraj newTraj) {

    }




    /*** Compute total nonbonded force(LJ only) acting on each particle and update force ***/
    public void compNbForceType2(GromacsSystem sys, MdTop top, MdPrm prm, MdNbList nblist, MdTraj newTraj) {

    }



    public void compRecForce(GromacsSystem sys, MdTop top, MdPrm prm, MdNbList nblist, MdTraj newTraj) {
    }   


    /********************** compute bonding force **************************/
    public static void compBondForce(GromacsSystem sys, MdTop top, MdPrm prm, MdNbList nblist, MdTraj newTraj) {

    }




    /********************** compute angle force **************************/

    public static void compAngleForce(GromacsSystem sys, MdTop top, MdPrm prm, MdNbList nblist, MdTraj newTraj) {

    }





    /********************** compute angle force **************************/

    public static void compDihedralForce(GromacsSystem sys, MdTop top, MdPrm prm, MdNbList nblist, MdTraj newTraj) {

    }








    /** geometric mean combination rule 
    (OBSOLETE)
    public static double combRule1(double s1, double s2 ) {
        return Math.sqrt(s1*s2);
    }
    **/





    /** returns position of the minimum image of particle j seen by particle i **/
    public static Vector minImage(Vector box, Vector ri, Vector rj) {
    
        Vector rji = Vector.sub(rj, ri);
        Vector mImage = new Vector(rj); // clone rj     

        if ( rji.x > box.x/2.0 ) mImage.x -= box.x;
        else if ( rji.x < -box.x/2.0 ) mImage.x += box.x;
    
        if ( rji.y > box.y/2.0 ) mImage.y -= box.y;
        else if ( rji.y < -box.y/2.0 ) mImage.y += box.y;
        
        if ( rji.z > box.z/2.0 ) mImage.z -= box.z;
        else if ( rji.z < -box.z/2.0 ) mImage.z += box.z;
        
        return mImage;

    }



}
