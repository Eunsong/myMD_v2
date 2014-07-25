package mymd;

public interface Force<E extends MdVector>{

	public <E> E get( MdSystem<E> sys, Topology top, int i, int j);

}
