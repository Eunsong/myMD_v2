package mymd.datatype;

public class MdVector{

    private double x,y,z;

	public static MdVector create(){
		return new MdVector();
	}
	public static MdVector create(MdVector v){
		return new MdVector(v);
	}


    public MdVector() {
        this.x = 0.0;
        this.y = 0.0;
        this.z = 0.0;
    }

    public MdVector(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public MdVector(MdVector vec) {
        this.x = vec.x;
        this.y = vec.y;
        this.z = vec.z;
    }

	public double getX(){
		return this.x;
	}
	public double getY(){
		return this.y;
	}
	public double getZ(){
		return this.z;
	}

	public void setX(double x){
		this.x = x;
	}
	public void setY(double y){
		this.y = y;
	}
	public void setZ(double z){
		this.z = z;
	}


    public double norm() {
        return Math.sqrt( this.x*this.x + this.y*this.y + this.z*this.z );
    }

    public double normsq() {
        return this.x*this.x + this.y*this.y + this.z*this.z;
    }

	public void normalize(){
		double factor = 1.0/norm();
		this.x *= factor;
		this.y *= factor;
		this.z *= factor;
	}

    public void copySet(MdVector vec) {
        this.x = vec.x;
        this.y = vec.y;
        this.z = vec.z;
    }
	public void copySet(double x, double y, double z){
		this.x = x;
		this.y = y;
		this.z = z;
	}

    public void subSet(MdVector vec) {
        this.x -= vec.x;
        this.y -= vec.y;
        this.z -= vec.z;
    }

	public void addSet(double x, double y, double z){
		this.x += x;
		this.y += y;
		this.z += z;
	}

    public void addSet(MdVector vec) {
        this.x += vec.x;
        this.y += vec.y;
        this.z += vec.z;
    }

	public void addSet(MdVector vec1, MdVector vec2){
        this.x += (vec1.x + vec2.x );
        this.y += (vec1.y + vec2.y );
        this.z += (vec1.z + vec2.z );
    }

    public void addSet(MdVector vec1, MdVector vec2, MdVector vec3){
        this.x += (vec1.x + vec2.x + vec3.x );
        this.y += (vec1.y + vec2.y + vec3.y );
        this.z += (vec1.z + vec2.z + vec3.z );
    }

	public void crossSet(MdVector vec){
		this.x = this.y*vec.z - this.z*vec.y;
		this.y = -this.x*vec.z + this.z*vec.x;
		this.z = this.x*vec.y - this.y*vec.x;
	}


    public MdVector copy(MdVector vec) {
		copySet(vec);
		return this;
    }
	public MdVector copy(double x, double y, double z){
		copySet(x, y, z);
		return this;
	}

    public MdVector sub(MdVector vec) {
		subSet(vec);
		return this;
    }

    public MdVector add(MdVector vec) {
		addSet(vec);
		return this;
    }

	public MdVector add(MdVector vec1, MdVector vec2){
		addSet(vec1, vec2);
		return this;
    }

    public MdVector add(MdVector vec1, MdVector vec2, MdVector vec3){
		addSet(vec1, vec2, vec3);
		return this;
    }

	public MdVector cross(MdVector vec){
		crossSet(vec);
		return this;
	}



	public void timesSet(double factor){
		this.x *= factor;
		this.y *= factor;
		this.z *= factor;
	}

	public void timesSet(int factor){
		this.x *= factor;
		this.y *= factor;
		this.z *= factor;
	}


	public MdVector times(double factor){
		timesSet(factor);
		return this;
	}

	public MdVector times(int factor){
		timesSet(factor);
		return this;
	}

	public void reset(){
		this.x = 0.0;
		this.y = 0.0;
		this.z = 0.0;
	}

	/**
	 * done method is used to finalize successive operations 
	 * e.g. vector1.add(someVector).sub(anotherVcetor).done()
	 */
	public void done(){
		// do nothing	
	}	


	public void minImage(MdVector box){
        if ( this.x > box.x/2.0 ) this.x -= box.x;
        else if ( this.x < -box.x/2.0 ) this.x += box.x;

        if ( this.y > box.y/2.0 ) this.y -= box.y;
        else if ( this.y < -box.y/2.0 ) this.y += box.y;

        if ( this.z > box.z/2.0 ) this.z -= box.z;
        else if ( this.z < -box.z/2.0 ) this.z += box.z;
	}


	public void print(){
		System.out.println(this.x + ", " + this.y + ", " + this.z);
	}

	/******** Static methods ********/

    public static double norm(MdVector vec) {
        return Math.sqrt( vec.x*vec.x + vec.y*vec.y + vec.z*vec.z );
    }

    public static MdVector diff(MdVector v1, MdVector v2) {
        return new MdVector( v1.x - v2.x, v1.y - v2.y, v1.z - v2.z );
    }

    public static MdVector sum(MdVector v1, MdVector v2) {
        return new MdVector( v1.x + v2.x, v1.y + v2.y, v1.z + v2.z );
    }

    // three vector addition
    public static MdVector sum(MdVector v1, MdVector v2, MdVector v3) {
        return new MdVector( v1.x + v2.x + v3.x, v1.y + v2.y + v3.y, v1.z + v2.z + v3.z);

    }

    public static double dot(MdVector v1, MdVector v2) {
        return (v1.x*v2.x + v1.y*v2.y + v1.z*v2.z);
    }

    public static MdVector cross(MdVector v1, MdVector v2) {
        return new MdVector( v1.y*v2.z - v1.z*v2.y, -v1.x*v2.z + v1.z*v2.x, v1.x*v2.y - v1.y*v2.x );
    }

    public static MdVector times(double factor, MdVector v) {
        MdVector tmp = new MdVector( v.x*factor, v.y*factor, v.z*factor );
        return tmp;
    }
    public static MdVector times(MdVector v, double factor) {
        MdVector tmp = new MdVector( v.x*factor, v.y*factor, v.z*factor );
        return tmp;
    }

    public static MdVector times(int factor, MdVector v) {
        MdVector tmp = new MdVector( v.x*factor, v.y*factor, v.z*factor );
        return tmp;
    }
	public static MdVector times(MdVector v, int factor){
        MdVector tmp = new MdVector( v.x*factor, v.y*factor, v.z*factor );
        return tmp;
    }




}
