import mymd.*;
import mymd.datatype.*;
import java.util.*;

public class Test{
	public static void main(String[] args){

		LookupTable table = new RnTable(6, 1.4);
	
		Integrator<MdSystem<LJParticle>> it = new VelocityVerlet<MdSystem<LJParticle>>(0.1);
		final int SIZE = 100;

		List<LJParticle> particles = new ArrayList<LJParticle>();
		MdParameter param = new MdParameter();
		Topology top = new Topology();
		Trajectory trj = new Trajectory(SIZE);
	
		MdSystem<LJParticle> sys = new MdSystem.Builder<LJParticle>("test").particles(particles).parameters(param).
						topology(top).initialTrajectory(trj).build();

	}
}
