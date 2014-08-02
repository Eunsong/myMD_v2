package mymd;

import mymd.datatype.*;

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
    public Trajectory(int N) {
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
	public void resetPositions(){
		for ( int i = 0; i < size; i++){
			this.positions[i].reset();
		}
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
	public void resetVelocities(){
		for ( int i = 0; i < size; i++){
			this.velocities[i].reset();
		}
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
	public void addForce(int i, E v){
		this.forces[i].add(v);
	}
	public void resetForces(){
		for ( int i = 0; i < size; i++){
			this.forces[i].reset();
		}
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



	// Exporting methods. These methods are needed for parallel computation
	// using a Message Passing Interface implementation. (e.g. MPJ)
	public double[] exportPositions(int[] particleList){
		int size = particleList.length;
		double[] positionArray = new double[3*size];
		int i = 0;
		for ( int id : particleList ){
			positionArray[i++] = this.positions[id].getX();
			positionArray[i++] = this.positions[id].getY();
			positionArray[i++] = this.positions[id].getZ();
		}
		return positionArray;		
	}

	public float[] exportFloatPositions(int[] particleList){
		int size = particleList.length;
		float[] positionArray = new float[3*size];
		int i = 0;
		for ( int id : particleList ){
			positionArray[i++] = (float)this.positions[id].getX();
			positionArray[i++] = (float)this.positions[id].getY();
			positionArray[i++] = (float)this.positions[id].getZ();
		}
		return positionArray;		
	}

	public void importPositions(double[] positionArray, int[] particleList){
		int i = 0;
		for ( int id : particleList ){
			double x = positionArray[i++];
			double y = positionArray[i++];
			double z = positionArray[i++];
			this.positions[id].copy(x, y, z); 
		}
	}
	
	public void importPositions(double[] positionArray){
		int id = 0;
		for ( int i = 0; i < positionArray.length; ){
			double x = positionArray[i++];
			double y = positionArray[i++];
			double z = positionArray[i++];
			this.positions[id++].copy(x, y, z); 
		}
	}

}
