package mymd.gromacs;

import java.util.*;

public class MdHashTable {

    private int Mx, My, Mz;

    private List<List<List<List<Integer>>>> myHashTable;

    public MdHashTable(int Mx, int My, int Mz) {
        this.Mx = Mx;
        this.My = My;
        this.Mz = Mz;
        myHashTable = new ArrayList<List<List<List<Integer>>>>();
            
        for ( int i = 0; i < Mx; i++ ) {
            
            List<List<List<Integer>>> xlist = new ArrayList<List<List<Integer>>>();

            for ( int j = 0; j < My; j++ ) {
        
                List<List<Integer>> ylist = new ArrayList<List<Integer>>();

                for ( int k = 0; k < Mz; k++ ) {
                    List<Integer> zlist = new LinkedList<Integer>();
                    ylist.add(zlist);
                }
                xlist.add(ylist);
            }
            myHashTable.add(xlist);
        }
    }


    public List<Integer> get(int i, int j, int k) {
        return this.myHashTable.get(i).get(j).get(k);
    }
    
    public int getMx() {
        return this.Mx;
    }
    public int getMy() {
        return this.My;
    }
    public int getMz() {
        return this.Mz;
    }


}
