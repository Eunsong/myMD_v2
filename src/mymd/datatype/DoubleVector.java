package mymd.datatype;

public class DoubleVector implements MdVector{

    private double x,y,z;

	public static DoubleVector create(){
		return new DoubleVector();
	}

    public DoubleVector() {
        this.x = 0.0;
        this.y = 0.0;
        this.z = 0.0;
    }

    public DoubleVector(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public DoubleVector(MdVector vec) {
        this.x = vec.getx();
        this.y = vec.gety();
        this.z = vec.getz();
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

    public double norm() {
        return Math.sqrt( this.x*this.x + this.y*this.y + this.z*this.z );
    }

    public double normsq() {
        return this.x*this.x + this.y*this.y + this.z*this.z;
    }

    public void copy(MdVector vec) {
        this.x = vec.getx();
        this.y = vec.gety();
        this.z = vec.getz();
    }
	public void copy(double x, double y, double z){
		this.x = x;
		this.y = y;
		this.z = z;	
	}

    public void sub(MdVector vec) {
        this.x -= vec.getx();
        this.y -= vec.gety();
        this.z -= vec.getz();
    }

    public void add(MdVector vec) {
        this.x += vec.getx();
        this.y += vec.gety();
        this.z += vec.getz();
    }


	/******** Static methods ********/

    public static double norm(MdVector vec) {
        return Math.sqrt( vec.getx()*vec.getx() + vec.gety()*vec.gety() + vec.getz()*vec.getz() );
    }

    public static DoubleVector sub(MdVector v1, MdVector v2) {
        return new DoubleVector( v1.getx() - v2.getx(), v1.gety() - v2.gety(), v1.getz() - v2.getz() );
    }

    public static DoubleVector add(MdVector v1, MdVector v2) {
        return new DoubleVector( v1.getx() + v2.getx(), v1.gety() + v2.gety(), v1.getz() + v2.getz() );
    }

    // three vector addition
    public static DoubleVector add(MdVector v1, MdVector v2, MdVector v3) {
        return new DoubleVector( v1.getx() + v2.getx() + v3.getx(), v1.gety() + v2.gety() + v3.gety(), v1.getz() + v2.getz() + v3.getz());

    }

    public static double dot(MdVector v1, MdVector v2) {
        return (v1.getx()*v2.getx() + v1.gety()*v2.gety() + v1.getz()*v2.getz());
    }
    public static DoubleVector cross(MdVector v1, MdVector v2) {
        return new DoubleVector( v1.gety()*v2.getz() - v1.getz()*v2.gety(), -v1.getx()*v2.getz() + v1.getz()*v2.getx(), v1.getx()*v2.gety() - v1.gety()*v2.getx() );
    }

    public DoubleVector times(double factor) {
        DoubleVector tmp = new DoubleVector( this.x*factor, this.y*factor, this.z*factor );
        return tmp;
    }
    public DoubleVector times(int factor) {
        DoubleVector tmp = new DoubleVector( this.x*factor, this.y*factor, this.z*factor );
        return tmp;
    }





}
