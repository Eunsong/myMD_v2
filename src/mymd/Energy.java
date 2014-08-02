package mymd;

public interface Energy<E extends MdVector, T extends MdSystem<E>>{

	public double get(T sys, int i, int j);

}
