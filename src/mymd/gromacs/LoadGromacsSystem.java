package mymd.gromacs;

import java.util.*;
import java.io.*;


/**
 * LoadGromacsSystem is an ad-hoc class that provides a bridge between mymd alpha
 * version and the myme version 1.0 to generate initial configuration and topologies 
 * for mymd simulations. 
 * 
 * @author Eunsong Choi (eunsong.choi@gmail.com)
 * @version 1.0
 */


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
	
	public int getBondSize(){
		return this.top.getBonds().size();
	}
	public int getBondi(int i){
		return this.top.getBonds().get(i).geti();
	}

	public int getBondj(int i){
		return this.top.getBonds().get(i).getj();
	}

	public double getBondk0(int i){
		return this.top.getBonds().get(i).getK0();
	}
	public double getBondb0(int i){
		return this.top.getBonds().get(i).getB0();
	}
	public int getBondFunc(int i){
		return this.top.getBonds().get(i).getFunc();
	} 





	public int getAngleSize(){
		return this.top.getAngles().size();
	}
	public int getAnglei(int i){
		return this.top.getAngles().get(i).geti();
	}

	public int getAnglej(int i){
		return this.top.getAngles().get(i).getj();
	}
	public int getAnglek(int i){
		return this.top.getAngles().get(i).getk();
	}

	public double getAnglek0(int i){
		return this.top.getAngles().get(i).getK0();
	}
	public double getAngleTheta0(int i){
		return this.top.getAngles().get(i).getT0();
	}
	public int getAngleFunc(int i){
		return this.top.getAngles().get(i).getFunc();
	} 







	public int getDihedralSize(){
		return this.top.getDihedrals().size();
	}
	public int getDihedrali(int i){
		return this.top.getDihedrals().get(i).geti();
	}

	public int getDihedralj(int i){
		return this.top.getDihedrals().get(i).getj();
	}
	public int getDihedralk(int i){
		return this.top.getDihedrals().get(i).getk();
	}
	public int getDihedrall(int i){
		return this.top.getDihedrals().get(i).getl();
	}

	public double getDihedralk0(int i){
		return this.top.getDihedrals().get(i).getK0();
	}
	public int getDihedraln(int i){
		return this.top.getDihedrals().get(i).getN();
	}
	public double getDihedralPhi0(int i){
		return this.top.getDihedrals().get(i).getP0();
	}
	public int getDihedralFunc(int i){
		return this.top.getDihedrals().get(i).getFunc();
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
