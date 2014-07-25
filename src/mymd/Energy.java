package mymd;

public interface Energy<E extends MdVector>{

	public double get(MdSystem<E> sys, Topology top, int i, int j);

}
