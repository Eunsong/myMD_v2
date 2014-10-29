package mymd.nonbond;

public class LJCEnergyLookupTable extends AbstractLookupTable{

    private static final double conversionFactor = 138.935485;
    private final double C6, C12, chargeSquared;

    public double getC6(){
        return this.C6;
    }
    public double getC12(){
        return this.C12;
    }
    public double getChargeSquared(){
        return this.chargeSquared;
    }

    public LJCEnergyLookupTable(double rc, double C6, double C12, double chargeSquared){
        super(rc);
        this.C6 = C6;
        this.C12 = C12;
        this.chargeSquared = chargeSquared;
        genTable();
    }

    protected void genTable(){
        super.table = new double[super.TABLE_SIZE + 1];
        for ( int i = 0; i <= super.TABLE_SIZE; i++){
            double r = super.rc*(double)i/(double)super.TABLE_SIZE;
            super.table[i] = -C6/Math.pow(r,6) + C12/Math.pow(r,12) 
                             + conversionFactor*chargeSquared/Math.pow(r,1);
        }
    } 




}
