package mymd;

import mymd.datatype.*;
import java.util.Random;

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


	/**
	 * generates random velocities of speed V to all existing particles
	 * in this Trajector object.
	 *
	 * @param T temperature in Kelvin
	 */
	public void genVelocities(double v){
		for ( int i = 0; i < size; i++){
			genVelocity(i, v);
		}
	}


	
	/**
	 * generates a random velocity of speed v on a particle i
 	 *
	 * @param i particle index in which velocity will be generated to
	 * @param v target speed 
	 */
	public void genVelocity(int i, double v){
	
		Random generator = new Random();
		boolean succeeded = false;
		double r1 = 0.0, r2 = 0.0, s = 0.0;		

		while ( !succeeded ) {
			r1 = 2.0*generator.nextDouble() - 1.0;
            r2 = 2.0*generator.nextDouble() - 1.0;
            s = Math.sqrt( Math.pow(r1,2) + Math.pow(r2,2) );
            if ( s < 1 ) {
                    succeeded = true;
            }
		}

        double vx = 2*Math.sqrt(1 - Math.pow(s,2))*r1;
        double vy = 2*Math.sqrt(1 - Math.pow(s,2))*r2;
        double vz = 1 - 2*Math.pow(s,2);
		
		this.velocities[i].copySet(v*vx, v*vy, v*vz);
	}



	// Exporting methods. These methods are needed for parallel computation
	// using a Message Passing Interface implementation. (e.g. MPJ)
	public double[] exportPositions(Domain domain){
		return domain.exportPositions(this);
	}

	public void importPositions(Domain domain, double[] positionArray){
		domain.importPositions(this, positionArray);
	}	

	public double[] exportForces(Domain domain){
		return domain.exportForces(this);
	}

	public void importForces(Domain domain, double[] forceArray){
		domain.importForces(this, forceArray);
	}


}
