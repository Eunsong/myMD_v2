package mymd.gromacs;

// File name : MdNbList.java
// Author : Eunsong Choi ( eunsong.choi@gmail.com )
// Last updated : 2013-11-04


import java.util.*;

public class MdNbList {

    private int initIndex; // initial index (needed for parallelized run)
    private int size;
    private List<List<Integer>> nblist;
//  private List<Integer> atomId;

    public MdNbList( int N, int index ) {
        initIndex = index;
        this.size = N;
        nblist = new ArrayList<List<Integer>>();
//      atomId = new ArrayList<Integer>();
    
        for ( int i = 0; i < N; i++ ) {
            List<Integer> tmpList = new ArrayList<Integer>();
            nblist.add(tmpList);
        }
    }

    public List<List<Integer>> getNbList() {
        return this.nblist;
    }

    public List<Integer> get(int i) {
        return this.nblist.get(i);
    }

    public int getSize() {
        return this.size;
    }

    public int getInitIndex() {
        return this.initIndex;
    }

}
