package mymd;

import mymd.datatype.*;


public class SubSystem<T extends Particle> extends MdSystem<T>{

	private Trajectory subTraj;

	protected SubSystem(Init<T, ?> init){
		super(init);
		this.subTraj = init.subTraj;		
	}

	protected static abstract class Init<T extends Particle, E extends Init<T,E>> 
									                   extends MdSystem.Init<T,E>{ 
		private Trajectory subTraj;

		public E subTrajectory(Trajectory subTraj){
			this.subTraj = subTraj;
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
