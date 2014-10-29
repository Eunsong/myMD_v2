package mymd.gromacs;

public class Vector {

    public double x,y,z;

    public Vector() {
        this.x = 0.0;
        this.y = 0.0;
        this.z = 0.0;
    }

    public Vector(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector(Vector vec) {
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

    public void copy(Vector vec) {
        this.x = vec.x;
        this.y = vec.y;
        this.z = vec.z;
    }

    public void sub(Vector vec) {
        this.x -= vec.x;
        this.y -= vec.y;
        this.z -= vec.z;
    }

    public void add(Vector vec) {
        this.x += vec.x;
        this.y += vec.y;
        this.z += vec.z;
    }



    public static double norm(Vector vec) {
        return Math.sqrt(Math.pow(vec.x,2) + Math.pow(vec.y,2) + Math.pow(vec.z,2) );
    }


    public static Vector sub(Vector v1, Vector v2) {
        return new Vector( v1.x - v2.x, v1.y - v2.y, v1.z - v2.z );
    }

    public static Vector add(Vector v1, Vector v2) {
        return new Vector( v1.x + v2.x, v1.y + v2.y, v1.z + v2.z );
    } 

    // three vector addition
    public static Vector add(Vector v1, Vector v2, Vector v3) {
        return new Vector( v1.x + v2.x + v3.x, v1.y + v2.y + v3.y, v1.z + v2.z + v3.z);

    }


    public static double dot(Vector v1, Vector v2) {
        return (v1.x*v2.x + v1.y*v2.y + v1.z*v2.z);
    }
    public static Vector cross(Vector v1, Vector v2) {
        return new Vector( v1.y*v2.z - v1.z*v2.y, -v1.x*v2.z + v1.z*v2.x, v1.x*v2.y - v1.y*v2.x );
    }

    public Vector times(double factor) {
        Vector tmp = new Vector( this.x*factor, this.y*factor, this.z*factor );
        return tmp;
    }   


    

}   



