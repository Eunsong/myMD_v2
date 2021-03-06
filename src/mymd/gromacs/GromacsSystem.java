package mymd.gromacs;

import java.util.*;
import java.io.*;

class GromacsSystem {

    private MdSite[] sites; 
    private MdTraj traj;


    public GromacsSystem(int N) {
        this.sites = new MdSite[N];
        this.traj = new MdTraj(N);
    } 


    public MdSite[] getSites() {
         return sites;
    }
    public MdSite[] get() {
        return sites;
    }
    public MdSite getSite(int i) {
        return sites[i];
    }


    public MdTraj getTraj() {
        return this.traj;
    }   
    public void setTraj(MdTraj traj) {
        this.traj = traj;
    }


    public Vector[] getPos() {
        return this.traj.getPos();
    }
    public Vector getPos(int i) {
        return this.traj.getPos(i);
    }


    public Vector[] getVel() {
        return this.traj.getVel();
    }
    public Vector getVel(int i) {
        return this.traj.getVel(i);
    }


    public Vector[] getForce() {
        return this.traj.getForce();
    }
    public Vector getForce(int i) {
        return this.traj.getForce(i);
    }
    

    public Vector getBox() {
         return this.traj.getBox();
    }
    public double getTime() {
        return this.traj.getTime();
    }

    public void setSite(int i, MdSite newSite) {
        this.sites[i] = newSite;
    }



    /*********************** output methods **********************************/
    
    public static void writeTraj(String mdName, GromacsSystem sys, PrintStream ps) {

        double dt = sys.getTime();
        int N = sys.getTraj().getSize();
        ps.println(String.format("Generated by myMdCode : %s t=%5.5f", mdName,dt ));
        ps.println(String.format(" %d", N));
        Vector[] pos = sys.getTraj().getPos();
        Vector[] vel = sys.getTraj().getVel();
        Vector box = sys.getBox();

        for ( int i = 0; i < sys.getTraj().getSize(); i++ ) {
            MdSite site = sys.getSite(i);
            ps.println( String.format("%5d%-5s%5s%5d%8.3f%8.3f%8.3f%8.4f%8.4f%8.4f",
                    (site.getResId()+1), site.getResName(), site.getAtomName(), (site.getAtomId()+1),
                    pos[i].x, pos[i].y, pos[i].z, vel[i].x, vel[i].y, vel[i].z ) );
        }
        
        ps.println( String.format( "%4.5f  %4.5f  %4.5f", box.x, box.y, box.z ));

    }





    









/*
    public void setBox(Vector boxSize) {
        this.traj.setBox(boxSize);
    }
    public void setBox(double x, double y, double z) {
        this.traj.setBox(new Vector(x,y,z));
    } 
    public void setBox(double l) {
        this.traj.setBox(l);
    }

    public void setTime(double t) {
        this.traj.setTime(t);
    }   
*/
}
