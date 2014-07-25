package mymd.datatype;

public class MdVector{

    public double x,y,z;

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

    public double norm() {
        return Math.sqrt( this.x*this.x + this.y*this.y + this.z*this.z );
    }

    public double normsq() {
        return this.x*this.x + this.y*this.y + this.z*this.z;
    }

    public void copy(MdVector vec) {
        this.x = vec.x;
        this.y = vec.y;
        this.z = vec.z;
    }
	public void copy(double x, double y, double z){
		this.x = x;
		this.y = y;
		this.z = z;	
	}

    public void sub(MdVector vec) {
        this.x -= vec.x;
        this.y -= vec.y;
        this.z -= vec.z;
    }

    public void add(MdVector vec) {
        this.x += vec.x;
        this.y += vec.y;
        this.z += vec.z;
    }


	/******** Static methods ********/

    public static double norm(MdVector vec) {
        return Math.sqrt( vec.x*vec.x + vec.y*vec.y + vec.z*vec.z );
    }

    public static MdVector sub(MdVector v1, MdVector v2) {
        return new MdVector( v1.x - v2.x, v1.y - v2.y, v1.z - v2.z );
    }

    public static MdVector add(MdVector v1, MdVector v2) {
        return new MdVector( v1.x + v2.x, v1.y + v2.y, v1.z + v2.z );
    }

    // three vector addition
    public static MdVector add(MdVector v1, MdVector v2, MdVector v3) {
        return new MdVector( v1.x + v2.x + v3.x, v1.y + v2.y + v3.y, v1.z + v2.z + v3.z);

    }

    public static double dot(MdVector v1, MdVector v2) {
        return (v1.x*v2.x + v1.y*v2.y + v1.z*v2.z);
    }
    public static MdVector cross(MdVector v1, MdVector v2) {
        return new MdVector( v1.y*v2.z - v1.z*v2.y, -v1.x*v2.z + v1.z*v2.x, v1.x*v2.y - v1.y*v2.x );
    }

    public MdVector times(double factor) {
        MdVector tmp = new MdVector( this.x*factor, this.y*factor, this.z*factor );
        return tmp;
    }
    public MdVector times(int factor) {
        MdVector tmp = new MdVector( this.x*factor, this.y*factor, this.z*factor );
        return tmp;
    }





}
