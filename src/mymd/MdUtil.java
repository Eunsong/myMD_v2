package mymd;

import mymd.datatype.MdVector;

public class MdUtil{

	private static MdVector Rij = new MdVector();
	private 

    public static void minImage(MdVector box, MdVector Rij){

        if ( Rij.getX() > box.getX()/2.0 ) Rij.x -= box.getX();
        else if ( Rij.getX() < -box.getX()/2.0 ) Rij.x += box.getX();

        if ( Rij.getY() > box.getY()/2.0 ) Rij.y -= box.getY();
        else if ( Rij.getY() < -box.getY()/2.0 ) Rij.y += box.getY();

        if ( Rij.getZ() > box.getZ()/2.0 ) Rij.z -= box.getZ();
        else if ( Rij.getZ() < -box.getZ()/2.0 ) Rij.z += box.getZ();

    }


}
