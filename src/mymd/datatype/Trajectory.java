package mymd.datatype;

/**
 * Trajectory class contains dynamic simulation system information 
 * such as position, velocity, force, and etc that changes over time
 * In order to optimize intensive computation on these data, arrays are 
 * used to store instances of E that contain primitive data types
 *
 * @author Eunsong Choi (eunsong.choi@gmail.com)
 * @version 1.0
 */

public class Trajectory<E extends MdVector> {

    private int size;
    private E[] positions;
    private E[] velocities;
    private E[] forces;
    private double time;
    private E box;

    // The positions, velocities, and forces can only contain
    // E instances set from set methods. Thus we can ensure type safety. 
    @SuppressWarnings("unchecked")
    public MdTraj(int N) {
        this.size = N;
        this.positions = (E[]) new Object[N];
        this.velocities = (E[]) new Object[N];
        this.forces = (E[]) new Object[N];
        initPositions();
        initVelocities();
        initForces();
        this.time = 0.0;
        this.box = new E();
    }

    public int getSize() {
        return this.size;
    }   
    public E[] getPositions() {
        return this.positions;
    }
    public E getPosition(int i) {
        return this.positions[i];
    }
    public void setPosition(int i, E v) {
        this.positions[i].copy(v);
    }


    public E[] getVelocities() {
        return this.velocities;
    }
    public E getVelocity(int i) {
        return this.velocities[i];
    }
    public void setVel(int i, E v) {
        this.velocities[i].copy(v);
    }


    public E[] getForces() {
        return this.forces;
    }
    public E getForce(int i) {
        return this.forces[i];
    }
    public void setForce(int i, E v) {
        this.forces[i].copy(v);
    }


    public double getTime() {
        return this.time;
    }
    public void setTime(double t) {
        this.time = t;
    }
    
    public E getBox() {
        return this.box;
    }
    public void setBox(E b) {
        this.box.copy(b);
    }
    public void setBox(double l) {
        this.box.copy(l, l, l);
    }


    private void initPositions() {
        for ( int i = 0; i < this.size; i++ ) {
            this.positions[i] = E.create();
        }
    }
    private void initVelocities() {
        for ( int i = 0; i < this.size; i++ ) {
            this.velocities[i] = E.create();
        }
    }
    private void initForces() {
        for ( int i = 0; i < this.size; i++ ) {
            this.forces[i] = E.create();
        }
    }


    /************************************** additional methods ******************************************/


    // Export trajectories into array format
    public double[] exportArray() {
    
        double[] array = new double[size*3*2 + 3]; // Position(3N components) + Force(3N) + box size(3)
    
        for ( int i = 0; i < this.size; i++ ) {

            array[i] = this.positions[i].x;
            array[this.size + i] = this.positions[i].y;
            array[this.size*2 + i] = this.positions[i].z;
        
            array[this.size*3 + i] = this.forces[i].x;
            array[this.size*4 + i] = this.forces[i].y;
            array[this.size*5 + i] = this.forces[i].z;
            array[this.size*6] = this.box.x;
            array[this.size*6 + 1] = this.box.y;
            array[this.size*6 + 2] = this.box.z;

        }
        
        return array;
    }




    // Import trajectories from input array
    public void importArray(double[] array) {

        for ( int i = 0; i < this.size; i++ ) {
            this.positions[i].x = array[i];
            this.positions[i].y = array[this.size + i];
            this.positions[i].z = array[this.size*2 + i]; 
            this.forces[i].x = array[this.size*3 + i];
            this.forces[i].y = array[this.size*4 + i];
            this.forces[i].z = array[this.size*5 + i];
        }

        this.box.x = array[this.size*6];
        this.box.y = array[this.size*6 + 1];
        this.box.z = array[this.size*6 + 2];
    }



    // Export positionsition trajectories into array format
    public double[] exportPosArray() {
    
        double[] array = new double[size*3 + 3]; 
    
        for ( int i = 0; i < this.size; i++ ) {
            array[i] = this.positions[i].x;
            array[this.size + i] = this.positions[i].y;
            array[this.size*2 + i] = this.positions[i].z;
        }
        array[this.size*3] = this.box.x;
        array[this.size*3 + 1] = this.box.y;
        array[this.size*3 + 2] = this.box.z;
        return array;
    }



    // Import positionsition trajectories from input array
    public void importPosArray(double[] array) {

        for ( int i = 0; i < this.size; i++ ) {
            this.positions[i].x = array[i];
            this.positions[i].y = array[this.size + i];
            this.positions[i].z = array[this.size*2 + i]; 
        }
        this.box.x = array[this.size*3];
        this.box.y = array[this.size*3 + 1];
        this.box.z = array[this.size*3 + 2];
    }


    // Export forces trajectories into array format
    public double[] exportForceArray() {
    
        double[] array = new double[size*3]; 
    
        for ( int i = 0; i < this.size; i++ ) {
            array[i] = this.forces[i].x;
            array[this.size + i] = this.forces[i].y;
            array[this.size*2 + i] = this.forces[i].z;
        }
        return array;
    }

    // Import(and add) forces trajectories from input array
    public void importForceArray(double[] inForce) {
    
        for ( int i = 0; i < this.size; i++ ) {
            this.forces[i].x += inForce[i];
            this.forces[i].y += inForce[this.size + i];
            this.forces[i].z += inForce[this.size*2 + i];
        }
    }

