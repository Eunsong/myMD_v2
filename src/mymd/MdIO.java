package mymd;

import mymd.datatype.*;
import java.io.PrintStream;


/**
 * MdIO class provides static methods that can be used for file I/O 
 * while running simulations.
 *
 * @author Eunsong Choi(eunsong.choi@gmail.com)
 * @version 1.0
 */
public class MdIO{

	public static <T extends MdSystem<?>> void writeGro(T sys, PrintStream ps){
		String jobName = sys.getName();
		double time = sys.getTime();
		int N = sys.getSize();
		ps.println(String.format("Generated from mymd package : %s t=%5.5f", jobName, time ));
        ps.println(String.format(" %d", N));
		MdVector[] positions = sys.getCurrTraj().getPositions();
		MdVector[] velocities = sys.getCurrTraj().getVelocities();
		MdVector box = sys.getBox();
		
        for ( int i = 0; i < N; i++ ) {
			Particle particle = sys.getParticle(i);
            ps.println( String.format("%5d%-5s%5s%5d%8.3f%8.3f%8.3f%8.4f%8.4f%8.4f",
					particle.getResidueNumber()+1, particle.getResidueName(),
					particle.getName(), particle.getNumber() + 1, 
					positions[i].getX(), positions[i].getY(), positions[i].getZ(),
					velocities[i].getX(), velocities[i].getY(), velocities[i].getZ() ));
        }

        ps.println( String.format( "%4.5f  %4.5f  %4.5f", 
						box.getX(), box.getY(), box.getZ() ));

	}



}
