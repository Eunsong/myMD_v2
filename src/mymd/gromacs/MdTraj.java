package mymd.gromacs;

// File name : MdTraj.java
// author : Eunsong Choi (eunsong.choi@gmail.com)
// Last updated : 2013-11-07


import java.util.*;

public class MdTraj {


    private int size;
    private Vector[] pos;
    private Vector[] vel;
    private Vector[] force;
    private double time;
    private Vector box;

    public MdTraj(int N) {
        this.size = N;
        this.pos = new Vector[N];
        this.vel = new Vector[N];
        this.force = new Vector[N];
        this.time = 0.0;
        this.box = new Vector();
    }
    public int getSize() {
        return this.size;
    }   

    public Vector[] getPos() {
        return this.pos;
    }
    public Vector getPos(int i) {
        return this.pos[i];
    }
    public void setPos(int i, Vector v) {
        this.pos[i] = new Vector(v);
    }


    public Vector[] getVel() {
        return this.vel;
    }
    public Vector getVel(int i) {
        return this.vel[i];
    }
    public void setVel(int i, Vector v) {
        this.vel[i] = new Vector(v);
    }


    public Vector[] getForce() {
        return this.force;
    }
    public Vector getForce(int i) {
        return this.force[i];
    }
    public void setForce(int i, Vector v) {
        this.force[i] = new Vector(v);
    }


    public double getTime() {
        return this.time;
    }
    public void setTime(double t) {
        this.time = t;
    }
    
    public Vector getBox() {
        return this.box;
    }
    public void setBox(Vector b) {
        this.box = new Vector(b);
    }
    public void setBox(double l) {
        this.box = new Vector(l, l, l);
    }


    public void initPos() {
        for ( int i = 0; i < this.size; i++ ) {
            this.pos[i] = new Vector();
        }
    }
    public void initVel() {
        for ( int i = 0; i < this.size; i++ ) {
            this.vel[i] = new Vector();
        }
    }
    public void initForce() {
        for ( int i = 0; i < this.size; i++ ) {
            this.force[i] = new Vector();
        }
    }


    /************************************** additional methods ******************************************/


    // Export trajectories into array format
    public double[] exportArray() {
    
        double[] array = new double[size*3*2 + 3]; // Position(3N components) + Force(3N) + box size(3)
    
        for ( int i = 0; i < this.size; i++ ) {

            array[i] = this.pos[i].x;
            array[this.size + i] = this.pos[i].y;
            array[this.size*2 + i] = this.pos[i].z;
        
            array[this.size*3 + i] = this.force[i].x;
            array[this.size*4 + i] = this.force[i].y;
            array[this.size*5 + i] = this.force[i].z;
            array[this.size*6] = this.box.x;
            array[this.size*6 + 1] = this.box.y;
            array[this.size*6 + 2] = this.box.z;

        }
        
        return array;
    }




    // Import trajectories from input array
    public void importArray(double[] array) {

        for ( int i = 0; i < this.size; i++ ) {
            this.pos[i].x = array[i];
            this.pos[i].y = array[this.size + i];
            this.pos[i].z = array[this.size*2 + i]; 
            this.force[i].x = array[this.size*3 + i];
            this.force[i].y = array[this.size*4 + i];
            this.force[i].z = array[this.size*5 + i];
        }

        this.box.x = array[this.size*6];
        this.box.y = array[this.size*6 + 1];
        this.box.z = array[this.size*6 + 2];
    }



    // Export position trajectories into array format
    public double[] exportPosArray() {
    
        double[] array = new double[size*3 + 3]; 
    
        for ( int i = 0; i < this.size; i++ ) {
            array[i] = this.pos[i].x;
            array[this.size + i] = this.pos[i].y;
            array[this.size*2 + i] = this.pos[i].z;
        }
        array[this.size*3] = this.box.x;
        array[this.size*3 + 1] = this.box.y;
        array[this.size*3 + 2] = this.box.z;
        return array;
    }



    // Import position trajectories from input array
    public void importPosArray(double[] array) {

        for ( int i = 0; i < this.size; i++ ) {
            this.pos[i].x = array[i];
            this.pos[i].y = array[this.size + i];
            this.pos[i].z = array[this.size*2 + i]; 
        }
        this.box.x = array[this.size*3];
        this.box.y = array[this.size*3 + 1];
        this.box.z = array[this.size*3 + 2];
    }



    // Export force trajectories into array format
    public double[] exportForceArray() {
    
        double[] array = new double[size*3]; 
    
        for ( int i = 0; i < this.size; i++ ) {
            array[i] = this.force[i].x;
            array[this.size + i] = this.force[i].y;
            array[this.size*2 + i] = this.force[i].z;
        }
        return array;
    }

    // Import(and add) force trajectories from input array
    public void importForceArray(double[] inForce) {
    
        for ( int i = 0; i < this.size; i++ ) {
            this.force[i].x += inForce[i];
            this.force[i].y += inForce[this.size + i];
            this.force[i].z += inForce[this.size*2 + i];
        }
    }







    /** PBC correction **/
    public static void pbc(Vector box, Vector pos) {
    
        if ( pos.x > box.x ) pos.x = pos.x - box.x;
        else if ( pos.x < 0 ) pos.x = pos.x + box.x;
        
        if ( pos.y > box.y ) pos.y = pos.y - box.y;
        else if ( pos.y < 0 ) pos.y = pos.y + box.y;
    
        if ( pos.z > box.z ) pos.z = pos.z - box.z;
        else if ( pos.z < 0 ) pos.z = pos.z + box.z;

    }







    /*******************************Hash table***************************************************/

    // hash code that maps real number x ( 0<=x<=l ) to an integer n ( 0<=n<=M ) and returns n
    public static int hashCode( double l, int M, double x ) {
        return (int)(x/(l/(double)M));
    }

    // use hashCode to map atoms into the hashTable (stored integer indicates atom ID (starting from 0) 
    public static void hashMap( Vector box, Vector[] pos, MdHashTable hashTable ) {
        // will assume hashTable[i][j][k] where i,j,k are indices for x,y,and z directions respectively 

        int Mx = hashTable.getMx();
        int My = hashTable.getMy();
        int Mz = hashTable.getMz();

//      int Mx = hashTable.length;
//      int My = hashTable[0].length;
//      int Mz = hashTable[0][0].length;
    
        for ( int n = 0; n < pos.length; n++ ) {
            hashTable.get(hashCode(box.x, Mx, pos[n].x), hashCode(box.y, My, pos[n].y), hashCode(box.z, Mz, pos[n].z)).add(n);
        }

    }


    /******************************************************************************************/







    /********************************* Neighbor List ***********************************************/

    public static void compNbList( double rlist, MdHashTable hashTable, MdTop top, GromacsSystem system, MdNbList nblist ) {

        // starting index
        int n0 = nblist.getInitIndex();

        MdTraj traj = system.getTraj();
        int Mx = hashTable.getMx();
        int My = hashTable.getMy();
        int Mz = hashTable.getMz();
        Vector box = traj.getBox();
        double lx = box.x/Mx;
        double ly = box.y/My;
        double lz = box.z/Mz;
        double lsq = lx*lx + ly*ly + lz*lz; 
        double l = Math.sqrt(lsq);  
    
    
        double Lout = 2.0/Math.sqrt(3.0)*(rlist + l);

/*

        int xbound = 1; // + (int)(rlist/lx);
        int ybound = 1; //+ (int)(rlist/ly);
        int zbound = 1; //+ (int)(rlist/lz); 
*/
        /****** 2*bound + 1 must be equal or smaller than M *******/
        int xbound = 1 + (int)(Lout/(2*lx));
        int ybound = 1 + (int)(Lout/(2*ly));
        int zbound = 1 + (int)(Lout/(2*lz)); 

        if ( 2*xbound > Mx - 1 ) {
            System.out.println("ERROR! Too small hashTable size!");
            System.exit(0);
        }
        if ( 2*ybound > My - 1 ) {
            System.out.println("ERROR! Too small hashTable size!");
            System.exit(0);
        }
        if ( 2*zbound > Mz - 1 ) {
            System.out.println("ERROR! Too small hashTable size!");
            System.exit(0);
        }


        Vector[] pos = traj.getPos();

        for ( int n = 0; n < nblist.getSize(); n++ ) {

            // n is the index for neighor list which does not contain entire particle list
            // n_actual is the corresponding real particle index for n
            int n_actual = n + n0;

            // find a hash code for site n_actual
            int ni = hashCode(box.x, Mx, pos[n_actual].x);
            int nj = hashCode(box.y, My, pos[n_actual].y);
            int nk = hashCode(box.z, Mz, pos[n_actual].z); 

            // add all particles in other cells 
            // that are within rlist + l distance(distance between cells)
            for ( int i = ni - xbound; i <= ni + xbound; i++ ) {

                // pbc correction for hashTable
                int ii = i;
                if ( ii < 0 ) ii += Mx;
                else if ( ii > Mx - 1) ii -= Mx;
            
                    for ( int j = nj - ybound; j <= nj + ybound; j++ ) {

                    // pbc correction for hashTable
                    int jj = j;
                    if ( jj < 0 ) jj += My;
                    else if ( jj > My - 1 ) jj -= My;

                    for ( int k = nk - zbound; k <= nk + zbound; k++ ) {
            
                        // pbc correction for hashTable
                        int kk = k;
                        if ( kk < 0 ) kk += Mz;
                        else if ( kk > Mz - 1 ) kk -= Mz;

                        double cell_dist = Math.pow(ni-i,2)*Math.pow(lx,2) 
                                + Math.pow(nj-j,2)*Math.pow(ly,2) + Math.pow(nk-k,2)*Math.pow(lz,2);

                        // add all particles within the inner bound 
                        if ( cell_dist < 4.0/3.0*Math.pow(rlist - l, 2) ) {
                            for ( int nbid : hashTable.get(ii,jj,kk) ) {
                                /*** To avoid double counting same action-reaction force
                                    nblist only contains neighors with great atom id than the reference atom */
                                if ( nbid > n_actual ) {
                                    if ( system.getSite(n_actual).getMolId() == system.getSite(nbid).getMolId() ) {
                                        MdMolType moltype = top.getMolType( system.getSite(n_actual).getMolTypeId() );
                                        int nr_i = system.getSite(n_actual).getNr();
                                        int nr_j = system.getSite(nbid).getNr();
                                        if ( !moltype.checkExclusion(nr_i, nr_j) ) {
                                            nblist.get(n).add(nbid);
                                        }
                                    }
                                    else {
                                        nblist.get(n).add(nbid);
                                    }
                                }
                            }
                        }
                        // add particles within the outer bound only if their distance is 
                        // smaller than cutoff
                        else if ( cell_dist < 4.0/3.0*Math.pow(rlist + l, 2) ) {
                            for ( int nbid : hashTable.get(ii,jj,kk) ) {
                        
                                /*** To avoid double counting same action-reaction force
                                    nblist only contains neighors with great atom id than the reference atom */
                                if ( nbid > n_actual ) {    

                                    // calculate mirror image position 
                                    Vector rnbid = MdForce.minImage(box, pos[n_actual], pos[nbid]);
                                    double rij = (Vector.sub(pos[n_actual], rnbid)).norm();

                                    if ( rij < rlist ) {
                                        if ( system.getSite(n_actual).getMolId() == system.getSite(nbid).getMolId() ) {
                                            MdMolType moltype = top.getMolType( system.getSite(n_actual).getMolTypeId() );
                                            int nr_i = system.getSite(n_actual).getNr();
                                            int nr_j = system.getSite(nbid).getNr();
                                            if ( !moltype.checkExclusion(nr_i, nr_j) ) {
                                                nblist.get(n).add(nbid);
                                            }
                                        }
                                        else {
                                            nblist.get(n).add(nbid);
                                        }
                                    }
                                }
                            }   
                        }

                    }

                }
            } 
        }   


    }
/****************************Neighbor list end ********************************************/





    /******* Random Velocity Generator ***************/

    public static Vector genRandVel(double v) {
        
        Random generator = new Random();        

        boolean done = false;
        double r1 = 0.0, r2 = 0.0, s = 0.0;

        while (!done) {
            r1 = 2.0*generator.nextDouble() - 1.0;
            r2 = 2.0*generator.nextDouble() - 1.0;
            s = Math.sqrt( Math.pow(r1,2) + Math.pow(r2,2) );

            if ( s < 1 ) {
                    done = true;
            }
        }

        double vx = 2*Math.sqrt(1 - Math.pow(s,2))*r1;
        double vy = 2*Math.sqrt(1 - Math.pow(s,2))*r2;
        double vz = 1 - 2*Math.pow(s,2);

        return new Vector(v*vx, v*vy, v*vz);
    }






}
