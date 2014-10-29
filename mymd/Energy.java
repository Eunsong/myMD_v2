package mymd;

public interface Energy<T extends MdSystem<?>>{

    public double getEnergy(T sys, NeighborList nblist);

}
