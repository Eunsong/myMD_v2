package mymd.datatype;

public class LJPairType{

    private String typei;
    private String typej;

    private double C6;  
    private double C12;

    public LJPairType(String typei, String typej, double C6, double C12) {
        this.typei = new String(typei);
        this.typej = new String(typej);
        this.C6 = C6;
        this.C12 = C12;
    }

    public String getiType(){
        return this.typei;
    }
    public String getjType(){
        return this.typej;
    }

    public double getC6() {
        return this.C6;
    }
    public double getC12() {
        return this.C12;
    }

}
