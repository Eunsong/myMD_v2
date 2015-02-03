package mymd;

public class LJForceLookupTable extends AbstractLookupTable{

    private final double C6, C12;

    public double getC6(){
        return this.C6;
    }
    public double getC12(){
        return this.C12;
    }

    public LJForceLookupTable(double rc, double C6, double C12){
        super(rc);
        this.C6 = C6;
        this.C12 = C12;
        genTable();
    }

    protected void genTable(){
        super.table = new double[super.TABLE_SIZE + 1];
        for ( int i = 0; i <= super.TABLE_SIZE; i++){
            double r = super.rc*(double)i/(double)super.TABLE_SIZE;
            super.table[i] = -6.0*C6/Math.pow(r,8) + 12.0*C12/Math.pow(r,14);
        }
    } 




}
