package mymd;

import mymd.datatype.*;

/**
 * Trajectory class contains dynamic simulation system information 
 * such as position, velocity, force, and etc that changes over time
 * In order to optimize intensive computation on these data, arrays are 
 * used to store instances of MdVector that contain primitive data types
 *
 * @author Eunsong Choi (eunsong.choi@gmail.com)
 * @version 1.0
 */

public class Trajectory{

    private int size;
    private MdVector[] positions;
    private MdVector[] velocities;
    private MdVector[] forces;
    private double time;
    private MdVector box;

	// The positions, velocities, and forces can only contain
	// MdVector instances set from set methods. Thus we can ensure type safety. 
//	@SuppressWarnings("unchecked")
    public Trajectory(int N) {
        this.size = N;
        this.positions = new MdVector[N];
        this.velocities = new MdVector[N];
        this.forces = new MdVector[N];
		initPositions();
		initVelocities();
		initForces();
        this.time = 0.0;
        this.box = new MdVector();
    }

    public int getSize() {
        return this.size;
    }   
    public MdVector[] getPositions() {
        return this.positions;
    }
    public MdVector getPosition(int i) {
        return this.positions[i];
    }
    public void setPosition(int i, MdVector v) {
        this.positions[i].copySet(v);
    }
	public void resetPositions(){
		for ( int i = 0; i < size; i++){
			this.positions[i].reset();
		}
	}


    public MdVector[] getVelocities() {
        return this.velocities;
    }
    public MdVector getVelocity(int i) {
        return this.velocities[i];
    }
    public void setVel(int i, MdVector v) {
        this.velocities[i].copySet(v);
    }
	public void resetVelocities(){
		for ( int i = 0; i < size; i++){
			this.velocities[i].reset();
		}
	}


    public MdVector[] getForces() {
        return this.forces;
    }
    public MdVector getForce(int i) {
        return this.forces[i];
    }
    public void setForce(int i, MdVector v) {
        this.forces[i].copySet(v);
    }
	public void addForce(int i, MdVector v){
		this.forces[i].addSet(v);
	}
	public void addReactionForce(int i, MdVector v){
		this.forces[i].subSet(v);
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
    
    public MdVector getBox() {
        return this.box;
    }
    public void setBox(MdVector b) {
        this.box.copySet(b);
    }
    public void setBox(double l) {
        this.box.copySet(l, l, l);
    }


    private void initPositions() {
        for ( int i = 0; i < this.size; i++ ) {
            this.positions[i] = new MdVector();
        }
    }
    private void initVelocities() {
        for ( int i = 0; i < this.size; i++ ) {
            this.velocities[i] = new MdVector();
        }
    }
    private void initForces() {
        for ( int i = 0; i < this.size; i++ ) {
            this.forces[i] = new MdVector();
        }
    }



	// Exporting methods. These methods are needed for parallel computation
	// using a Message Passing Interface implementation. (e.g. MPJ)
	public double[] exportPositions(Domain domain){
		return domain.exportPositions(this);
	}

	public void importPositions(Domain domain, double[] positionArray){
		domain.importPositions(this, positionArray);
	}	

/*
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
			this.positions[id].copySet(x, y, z); 
		}
	}
	
	public void importPositions(double[] positionArray){
		int id = 0;
		for ( int i = 0; i < positionArray.length; ){
			double x = positionArray[i++];
			double y = positionArray[i++];
			double z = positionArray[i++];
			this.positions[id++].copySet(x, y, z); 
		}
	}

*/

}
