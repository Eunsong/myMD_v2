package mymd;

import mymd.datatype.*;


public class SubSystem<T extends Particle> extends MdSystem<T>{

	private SubTrajectory traj;

	protected SubSystem(Init<T, ?> init){
		super(init);
		this.traj = init.traj;		
	}

	protected static abstract class Init<T extends Particle, E extends Init<T,E>> 
									                   extends MdSystem.Init<T,E>{ 
		private SubTrajectory traj;

		public E subTrajectory(SubTrajectory traj){
			this.traj = traj;
			return self();
		}
	
		@Override public SubSystem<T> build(){
			return new SubSystem<T>(this);
		}

		@Override public E initialTrajectory(Trajectory traj){
			throw new UnsupportedOperationException
			("MdSubSystm object cannot have initial trajectory");
		}
	}

	public static class Builder<T extends Particle> extends Init<T, Builder<T>>{

		@Override protected Builder<T> self(){
			return this;
		}

	}
	

}
