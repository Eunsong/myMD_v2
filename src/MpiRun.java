import java.util.*;
import mpi.*;

public class MpiRun{
	public static void main(String[] args){


		/**** Build a Topology object ****/
		Topology top = new Topology();
		
		/**** Build a Parameter object ****/
		Parameter param = new Parameter();

		/**** Build initial trajectory from input file ****/
		Trajectory<DoubleVector> initTraj = new Trajectory<DoubleVector>();

		/**** Build an Integrator object ****/
		Integrator integrator<MdSystem> =  


		MPI.Init(args);
		final int rank = MPI.COMM_WORLD.Rank();
		final int np = MPI.COMM_WORLD.Size();





	}
}
