package mymd;

public interface Energy<T extends MdSystem<?>>{

	public double get(T sys, int i, int j);

}
