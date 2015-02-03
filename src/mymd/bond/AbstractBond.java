package mymd.bond;

import mymd.MdSystem;

public abstract class AbstractBond<T extends MdSystem<?>> implements Bond<T>{

    protected final int i, j;
    protected final double k0, b0;

    protected AbstractBond(int i, int j, double k0, double b0){
        this.i = i;
        this.j = j;
        this.k0 = k0;
        this.b0 = b0;
    }

    public abstract void updateForce(T sys);

    public abstract double getEnergy(T sys);

    
    public int geti(){
        return this.i;
    }
    public int getj(){
        return this.j;
    }
    public double getk0(){
        return this.k0;
    }
    public double getb0(){
        return this.b0;
    }
    



}
