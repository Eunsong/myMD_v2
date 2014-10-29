package mymd.datatype;

public class DoubleVector implements MdVector{

    private double x,y,z;

    public static DoubleVector create(){
        return new DoubleVector();
    }
    public static DoubleVector create(MdVector v){
        return new DoubleVector(v);
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

    public double norm() {
        return Math.sqrt( this.x*this.x + this.y*this.y + this.z*this.z );
    }

    public double normsq() {
        return this.x*this.x + this.y*this.y + this.z*this.z;
    }

    public void copy(MdVector vec) {
        this.x = vec.getX();
        this.y = vec.getY();
        this.z = vec.getZ();
    }
    public void copy(double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z; 
    }

    public void sub(MdVector vec) {
        this.x -= vec.getX();
        this.y -= vec.getY();
        this.z -= vec.getZ();
    }

    public void add(MdVector vec) {
        this.x += vec.getX();
        this.y += vec.getY();
        this.z += vec.getZ();
    }

    public void add(MdVector vec1, MdVector vec2){
        this.x += (vec1.getX() + vec2.getX() );
        this.y += (vec1.getY() + vec2.getY() );
        this.z += (vec1.getZ() + vec2.getZ() );
    }

    public void add(MdVector vec1, MdVector vec2, MdVector vec3){
        this.x += (vec1.getX() + vec2.getX() + vec3.getX() );
        this.y += (vec1.getY() + vec2.getY() + vec3.getY() );
        this.z += (vec1.getZ() + vec2.getZ() + vec3.getZ() );
    }



    public void times(double factor){
        this.x *= factor;
        this.y *= factor;
        this.z *= factor;
    }

    public void times(int factor){
        this.x *= factor;
        this.y *= factor;
        this.z *= factor;
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

    public static DoubleVector sub(MdVector v1, MdVector v2) {
        return new DoubleVector( v1.getX() - v2.getX(), v1.getY() - v2.getY(), v1.getZ() - v2.getZ() );
    }

    public static DoubleVector add(MdVector v1, MdVector v2) {
        return new DoubleVector( v1.getX() + v2.getX(), v1.getY() + v2.getY(), v1.getZ() + v2.getZ() );
    }

    // three vector addition
    public static DoubleVector add(MdVector v1, MdVector v2, MdVector v3) {
        return new DoubleVector( v1.getX() + v2.getX() + v3.getX(), v1.getY() + v2.getY() + v3.getY(), v1.getZ() + v2.getZ() + v3.getZ());

    }

    public static double dot(MdVector v1, MdVector v2) {
        return (v1.getX()*v2.getX() + v1.getY()*v2.getY() + v1.getZ()*v2.getZ());
    }
    public static DoubleVector cross(MdVector v1, MdVector v2) {
        return new DoubleVector( v1.getY()*v2.getZ() - v1.getZ()*v2.getY(), -v1.getX()*v2.getZ() + v1.getZ()*v2.getX(), v1.getX()*v2.getY() - v1.getY()*v2.getX() );
    }

    public static DoubleVector times(double factor, MdVector v) {
        DoubleVector tmp = new DoubleVector( v.x*factor, v.y*factor, v.z*factor );
        return tmp;
    }
    public static DoubleVector times(MdVector v, double factor) {
        DoubleVector tmp = new DoubleVector( v.x*factor, v.y*factor, v.z*factor );
        return tmp;
    }

    public static DoubleVector times(int factor, MdVector v) {
        DoubleVector tmp = new DoubleVector( v.x*factor, v.y*factor, v.z*factor );
        return tmp;
    }
    public static DoubleVector times(MdVector v, int factor){
        DoubleVector tmp = new DoubleVector( v.x*factor, v.y*factor, v.z*factor );
        return tmp;
    }




}
