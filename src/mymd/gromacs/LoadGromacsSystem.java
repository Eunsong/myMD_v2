package mymd.gromacs;

import java.util.*;
import java.io.*;


public class LoadGromacsSystem{
	
	private GromacsSystem mdsystem;
	private MdTop top;

	private String inputConfFile;
	private String inputTopFile;

	public LoadGromacsSystem(String inputConfFile, String inputTopFile){
		this.inputConfFile = inputConfFile;
		this.inputTopFile = inputTopFile;
	}


	public GromacsSystem getSystem(){
		return this.mdsystem;
	}


	public double[] getPositionArray(int i){
		Vector position = this.mdsystem.getPos(i);
		double[] posArray = {position.x, position.y, position.z};
		return posArray;
	}

	public double[] getBoxArray(){
		Vector box = this.mdsystem.getBox();
		double[] boxArray = {box.x, box.y, box.z};
		return boxArray;
	}
	public int getSize(){
		return this.mdsystem.getTraj().getSize();
	}

	public int getParticleNumber(int i){
		return this.mdsystem.getSite(i).getAtomId();
	}

	public String getParticleName(int i){
		return this.mdsystem.getSite(i).getAtomName();
	}

	public String getParticleType(int i){
		return this.mdsystem.getSite(i).getAtomType();
	}
	public int getParticleTypeNumber(int i){
		return this.mdsystem.getSite(i).getAtomTypeId();
	}
	public int getParticleResidueNumber(int i){
		return this.mdsystem.getSite(i).getResId();
	}
	public String getParticleResidueName(int i){
		return this.mdsystem.getSite(i).getResName();
	}	

	public double getParticleMass(int i){
		return this.mdsystem.getSite(i).getMass();
	}
	public double getParticleCharge(int i){
		return this.mdsystem.getSite(i).getCharge();
	}

	public double getParticleC6(int i){
		return this.mdsystem.getSite(i).getVii();
	}
	public double getParticleC12(int i){
		return this.mdsystem.getSite(i).getWii();
	}
	


    public void build(){

        /** read topology from input file */
        top = new MdTop();        
        MdIO.readMdTop(inputTopFile, top);
        int N = 0;
        for ( MdMolType moltype : top.getMolTypes() ) {
            N += moltype.getNMols() * moltype.getNAtoms();
        }


        /** create an empty MDSystem array */
        mdsystem = new GromacsSystem(N);
        /** read initial configuration and resIds from input file */
        MdIO.readGromacsSystem(inputConfFile, mdsystem);

        // apply pbc correction
        Vector orgLBox = new Vector(mdsystem.getBox());
        for ( int i = 0; i < mdsystem.getTraj().getSize(); i++ ) {
            MdTraj.pbc( orgLBox, mdsystem.getTraj().getPos()[i] );
        }

        /** load run topology data **/
        MdIO.convMdTop(top, mdsystem);

        /** convert all hbonds to constraints if convertHbonds field **/
//        MdConstraint.convertHbondsToConstraints(mdsystem, top);
/*
		try {
			PrintStream ps = new PrintStream("test.gro");
			GromacsSystem.writeTraj("test", mdsystem, ps);
			ps.close();
		}
		catch (Exception ex){

		}
*/
    }
} 
