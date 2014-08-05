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
        this.x = vec.getX();
        this.y = vec.getY();
        this.z = vec.getZ();
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

    public MdVector copy(MdVector vec) {
        this.x = vec.getX();
        this.y = vec.getY();
        this.z = vec.getZ();
		return this;
    }
	public MdVector copy(double x, double y, double z){
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	}

    public MdVector sub(MdVector vec) {
        this.x -= vec.getX();
        this.y -= vec.getY();
        this.z -= vec.getZ();
		return this;
    }

    public MdVector add(MdVector vec) {
        this.x += vec.getX();
        this.y += vec.getY();
        this.z += vec.getZ();
		return this;
    }

	public MdVector add(MdVector vec1, MdVector vec2){
        this.x += (vec1.getX() + vec2.getX() );
        this.y += (vec1.getY() + vec2.getY() );
        this.z += (vec1.getZ() + vec2.getZ() );
		return this;
    }

    public MdVector add(MdVector vec1, MdVector vec2, MdVector vec3){
        this.x += (vec1.getX() + vec2.getX() + vec3.getX() );
        this.y += (vec1.getY() + vec2.getY() + vec3.getY() );
        this.z += (vec1.getZ() + vec2.getZ() + vec3.getZ() );
		return this;
    }



	public MdVector times(double factor){
		this.x *= factor;
		this.y *= factor;
		this.z *= factor;
		return this;
	}

	public MdVector times(int factor){
		this.x *= factor;
		this.y *= factor;
		this.z *= factor;
		return this;
	}

	public void reset(){
		this.x = 0.0;
		this.y = 0.0;
		this.z = 0.0;
	}

	


	/******** Static methods ********/

    public static double norm(MdVector vec) {
        return Math.sqrt( vec.getX()*vec.getX() + vec.getY()*vec.getY() + vec.getZ()*vec.getZ() );
    }

    public static MdVector diff(MdVector v1, MdVector v2) {
        return new MdVector( v1.getX() - v2.getX(), v1.getY() - v2.getY(), v1.getZ() - v2.getZ() );
    }

    public static MdVector sum(MdVector v1, MdVector v2) {
        return new MdVector( v1.getX() + v2.getX(), v1.getY() + v2.getY(), v1.getZ() + v2.getZ() );
    }

    // three vector addition
    public static MdVector sum(MdVector v1, MdVector v2, MdVector v3) {
        return new MdVector( v1.getX() + v2.getX() + v3.getX(), v1.getY() + v2.getY() + v3.getY(), v1.getZ() + v2.getZ() + v3.getZ());

    }

    public static double dot(MdVector v1, MdVector v2) {
        return (v1.getX()*v2.getX() + v1.getY()*v2.getY() + v1.getZ()*v2.getZ());
    }

    public static MdVector cross(MdVector v1, MdVector v2) {
        return new MdVector( v1.getY()*v2.getZ() - v1.getZ()*v2.getY(), -v1.getX()*v2.getZ() + v1.getZ()*v2.getX(), v1.getX()*v2.getY() - v1.getY()*v2.getX() );
    }

    public static MdVector times(double factor, MdVector v) {
        MdVector tmp = new MdVector( v.getX()*factor, v.getY()*factor, v.getZ()*factor );
        return tmp;
    }
    public static MdVector times(MdVector v, double factor) {
        MdVector tmp = new MdVector( v.getX()*factor, v.getY()*factor, v.getZ()*factor );
        return tmp;
    }

    public static MdVector times(int factor, MdVector v) {
        MdVector tmp = new MdVector( v.getX()*factor, v.getY()*factor, v.getZ()*factor );
        return tmp;
    }
	public static MdVector times(MdVector v, int factor){
        MdVector tmp = new MdVector( v.getX()*factor, v.getY()*factor, v.getZ()*factor );
        return tmp;
    }




}
