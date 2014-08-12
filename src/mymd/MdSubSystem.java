package mymd;

import mymd.datatype.*;
import java.util.List;


public class MdSubSystem<T extends Particle> extends MdSystem<T>{

	public MdSubSystem(MdSystem<T> motherSystem, int subSystemSize){
		super();		
		String name = "subSystem";
		Topology<MdSystem<T>> top = motherSystem.getTopology();
		MdParameter parameters = motherSystem.getParam();
		List<T> particles = motherSystem.getParticles();
		super.name = name;
		super.topology = top;
		super.parameters = parameters;
		super.particles = particles;
	}



}
