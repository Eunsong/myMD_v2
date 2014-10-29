package mymd;

public interface Force<T extends MdSystem<?>>{

    public void updateForce(T sys, NeighborList nblist);

}
