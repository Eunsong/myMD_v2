package mymd.constraint;

import mymd.datatype.MdVector;
import mymd.MdSystem;
import mymd.Trajectory;

public class SimpleShake<T extends MdSystem<?>> implements SimpleConstraint<T>{

    private final int i, j;
    private final double d0;

    private final MdVector RijOld = new MdVector();
    private final MdVector RijNew = new MdVector();
    private final MdVector RiCorrection = new MdVector();
    private final MdVector RjCorrection = new MdVector();

    private final MdVector VijNew = new MdVector();
    private final MdVector ViCorrection = new MdVector();
    private final MdVector VjCorrection = new MdVector();


    public SimpleShake(int i, int j, double d0){
        this.i = i;
        this.j = j;
        this.d0 = d0;
    }

    public int geti(){
        return this.i;
    }

    public int getj(){
        return this.j;
    }

    public double getTargetDistance(){
        return this.d0;
    }

    public void updatePosition(T sys){
        double tolerance = sys.getParam().getCTol();
        double tolSquared = tolerance*tolerance;
        int maxIteration = sys.getParam().getMaxCItr();
        double dt = sys.getParam().getDt();
        double dtSquared = dt*dt;

        Trajectory newTraj = sys.getNewTraj();
        Trajectory oldTraj = sys.getCurrTraj();
        MdVector box = newTraj.getBox();

        MdVector RiNew = newTraj.getPosition(i);
        MdVector RjNew = newTraj.getPosition(j);
        
        MdVector RiOld = oldTraj.getPosition(i);
        MdVector RjOld = oldTraj.getPosition(j);

        RijNew.copy(RiNew).sub(RjNew).minImage(box);
        RijOld.copy(RiOld).sub(RjOld).minImage(box);

        double massi_inverse = 1.0/sys.getParticle(i).getMass();
        double massj_inverse = 1.0/sys.getParticle(j).getMass();

        double rijSquared = RijNew.normsq();
        int itr = 0; 

        while ( Math.abs(rijSquared - d0*d0) > tolSquared && itr < maxIteration ){
            double denom = -2.0*MdVector.dot( RijNew, RijOld )*dtSquared*
                           (massi_inverse + massj_inverse );
            double num = d0*d0 - RijNew.normsq();
            double lambda = num/denom;
            
            RiCorrection.copy(RijOld).timesSet( lambda*dtSquared*massi_inverse );
            RjCorrection.copy(RijOld).timesSet( lambda*dtSquared*massj_inverse );
            RiNew.subSet(RiCorrection);
            RjNew.addSet(RjCorrection);
            RijNew.copy(RiNew).sub(RjNew).minImage(box);
            rijSquared = RijNew.normsq();
            itr++;
        }
        if ( itr == maxIteration ){
            throw new java.lang.RuntimeException
            ("SHAKE WARNING! simple constraint did not converge in " 
             + maxIteration + " iterations!");
        }
        // apply periodic boundary condition
        pbc( box, RiNew);
        pbc( box, RjNew);

    } 


    public void updateVelocity(T sys){
    
        double tolerance = sys.getParam().getCTol();
        double tolSquared = tolerance*tolerance;
        int maxIteration = sys.getParam().getMaxCItr();
        double dt = sys.getParam().getDt();
        double dtSquared = dt*dt;

        Trajectory newTraj = sys.getNewTraj();
        MdVector box = newTraj.getBox();

        MdVector RiNew = newTraj.getPosition(i);
        MdVector RjNew = newTraj.getPosition(j);
        RijNew.copy(RiNew).sub(RjNew).minImage(box);

        MdVector ViNew = newTraj.getVelocity(i);
        MdVector VjNew = newTraj.getVelocity(j);
        VijNew.copy(ViNew).subSet(VjNew); 

        double massi_inverse = 1.0/sys.getParticle(i).getMass();
        double massj_inverse = 1.0/sys.getParticle(j).getMass();
        
        double Rij_dot_Vij = MdVector.dot( RijNew, VijNew );
        int itr = 0;

        while ( Math.abs( Rij_dot_Vij ) > tolSquared && itr < maxIteration ){
            
            double denom = d0*d0*( massi_inverse + massj_inverse );
            double num = MdVector.dot( RijNew, VijNew.copy(ViNew).sub(VjNew) );
            double k = num/denom;
            ViCorrection.copy(RijNew).timesSet(k*massi_inverse);
            VjCorrection.copy(RijNew).timesSet(k*massj_inverse);
            ViNew.subSet(ViCorrection);
            VjNew.addSet(VjCorrection);
            VijNew.copy(ViNew).subSet(VjNew);
            Rij_dot_Vij = MdVector.dot( RijNew, VijNew );
            itr++;
        }
        if ( itr == maxIteration) {
            throw new java.lang.RuntimeException
            ("RATTLE WARNING! velocity did not converge in " + maxIteration + " iterations!");
        }

    }
    

    private static void pbc(MdVector box, MdVector pos){
        if ( pos.getX() > box.getX() ) pos.setX( pos.getX() - box.getX() );
        else if ( pos.getX() < 0 ) pos.setX( pos.getX() + box.getX() );

        if ( pos.getY() > box.getY() ) pos.setY( pos.getY() - box.getY() );
        else if ( pos.getY() < 0 ) pos.setY( pos.getY() + box.getY() );

        if ( pos.getZ() > box.getZ() ) pos.setZ( pos.getZ() - box.getZ() );
        else if ( pos.getZ() < 0 ) pos.setZ( pos.getZ() + box.getZ() );
    }
    


}
