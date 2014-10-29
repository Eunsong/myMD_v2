import mymd.*;
import mymd.datatype.*;
import mymd.gromacs.*;
import java.util.*;
import java.io.PrintStream;

public class LoadTest{
	public static void main(String[] args){

		double dt =0.01;
	
		Integrator<MdSystem<LJParticle>> it = new VelocityVerlet<MdSystem<LJParticle>>(dt);

		Topology top = new Topology();
        MdSystem<LJParticle> test = MdSystem.buildFromGromacsInputs("water.gro", "topol.top");
		
		mymd.gromacs.LoadGromacsSystem tmp = new mymd.gromacs.LoadGromacsSystem("water.gro", "topol.top");
		tmp.build();
	
		System.out.println(tmp.getPositionArray(0)[0]);
		System.out.println(tmp.getSize());
		int size = tmp.getSize();
		List<LJParticle> particles = new ArrayList<LJParticle>(size);
		for ( int i = 0; i < size; i++){
			LJParticle particle = new LJParticle.Builder(i).
			name(tmp.getParticleName(i)).type(tmp.getParticleType(i)).
			residueName(tmp.getParticleResidueName(i)).residueNumber(tmp.getParticleResidueNumber(i)).
			typeNumber(tmp.getParticleTypeNumber(i)).mass(tmp.getParticleMass(i)).
			C6(tmp.getParticleC6(i)).C12(tmp.getParticleC12(i)).build();
			particles.add(particle);	
		}
		
		Trajectory trj = new Trajectory(size);
		trj.setBox( tmp.getBoxArray()[0] );
		trj.setTime(0.0);
		for ( int i = 0; i < size; i++){
			double x,y,z;
			x = tmp.getPositionArray(i)[0];
			y = tmp.getPositionArray(i)[1];
			z = tmp.getPositionArray(i)[2]; 
			trj.setPosition(i, new MdVector(x,y,z));
		}	


		MdParameter param = new MdParameter();
		param.setDt(dt);				
		param.setRvdw(1.4);

		MdSystem<LJParticle> sys = new MdSystem.Builder<LJParticle>("test").particles(particles).parameters(param).
						topology(top).initialTrajectory(trj).build();


		SimpleNeighborList<MdSystem<LJParticle>> nblist = new SimpleNeighborList<MdSystem<LJParticle>>(size, 1.4);
		FastLJForce<MdSystem<LJParticle>> myLJForce = new FastLJForce<MdSystem<LJParticle>>(sys);



				nblist.update(sys);
		try {

			PrintStream ps = new PrintStream("out.gro");

			for ( int i = 0; i < 100000; i++){
				it.forwardPosition(sys);		
				myLJForce.update( sys, nblist );
				it.forwardVelocity(sys);
				sys.update();	
//				mymd.MdIO.writeGro(sys, ps);
			}

			ps.close();
		}
		catch(Exception ex){
	
		}

	


		
	}
}
