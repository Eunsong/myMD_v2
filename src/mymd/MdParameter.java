package mymd;

import mymd.datatype.*;


public class MdParameter{

    // size of the hash table for neighbor search
    private int hashTS;
    // time step size (in ps)
    private double dt;
    // total number of time steps
    private int nsteps;

	// particle system type
	private enum particleSystem{
		Lennard_Jones, Lennard_Jones_Coulomb, SAPT;
	}

    // coulomb cut-off distance
    private double rc;
    // PME enabled
    private boolean pme;
    // PME interpolation order
    private int n_pme;
    // PME number of grids  
    private int K_pme;
    // PME beta value
    private double beta_pme;    

    // vdw type
    private String vdwType;
    // vdw cut-off distance
    private double rvdw;
    // neighbor list cut-off
    private double rlist;
    // neighbor list update frequency 
    private int nstlist;
    // reference temperature for T coupling
    private double refT;
    // initial temperature (if generating velocities)
    private double T0;
    // temperature coupling parameter
    private double tauT;


    // record frequencies
    private int nstxout;
    private int nstvout;
//  private int nstfout; 
    private int nstenergy;


    // tolerance for Constraint dynamics
    private double cTol;
    // maximum number of iterations for constraint relaxation
    private int maxCItr;
    // whether to convert hbonds to constraints
    private boolean convertHbonds;

    // Thermostat type
    private String thermostat; // e.g. Berendsen, none 



    public MdParameter() {
        // default pme setup 
        this.pme = true;
        // default hbonds conversion(false)
        this.convertHbonds = false;
    }

    public void setHashTS(int hash){
        this.hashTS = hash;
    }
    public int getHashTS(){
        return this.hashTS;
    }

    public void setDt(double dt) {
        this.dt = dt;
    }
    public double getDt() {
        return this.dt;
    }


    public void setNsteps(int nsteps) {
        this.nsteps = nsteps;
    }
    public int getNsteps() {
        return this.nsteps;
    }

    

    public void setRc(double rc) {
        this.rc = rc;
    }
    public double getRc() {
        return this.rc;
    }


    public void setVdwType(String type){
        this.vdwType = type;
    }
    public String getVdwType(){
        return this.vdwType;
    }

    public void setRvdw(double rvdw) {
        this.rvdw = rvdw;
    }
    public double getRvdw() {
        return this.rvdw;
    }

    public void setRlist(double rlist) {
        this.rlist = rlist;
    }
    public double getRlist() {  
        return this.rlist;
    }

    public void setNstlist(int nstlist) {
        this.nstlist = nstlist;
    }
    public int getNstlist() {
        return this.nstlist;
    }

    public void setNstxout(int nstxout) {
        this.nstxout = nstxout;
    }
    public int getNstxout() {
        return this.nstxout;
    }   

    public void setNstvout(int nstvout) {   
        this.nstvout = nstvout;
    }
    public int getNstvout() {
        return this.nstvout;
    }

    public void setNstenergy(int nstenergy) {   
        this.nstenergy = nstenergy;
    }
    public int getNstenergy() {
        return this.nstenergy;
    }

    public void setRefT(double T) {
        this.refT = T;
    }
    public double getRefT() {
        return this.refT;
    }

    public void setT0(double T0) {
        this.T0 = T0;
    }
    public double getT0() {
        return this.T0;
    }

    public void setTauT(double tau) {
        this.tauT = tau;
    }
    public double getTauT() {
        return this.tauT;
    }

    public void setN_pme(int n){
        this.n_pme = n;
    }
    public void setK_pme(int K){
        this.K_pme = K;
    }
    public void setBeta_pme(double beta){
        this.beta_pme = beta;
    }

    public int getN_pme(){
        return this.n_pme;
    }
    public int getK_pme(){
        return this.K_pme;
    }
    public double getBeta_pme(){
        return this.beta_pme;
    }
    
    public void setPME(boolean bool){
        this.pme = bool;
    }

    public boolean PME_enabled(){
        return this.pme;
    }

    public void setCTol(double tol){
        this.cTol = tol;
    }
    public double getCTol(){
        return this.cTol;
    }
    public void setMaxCItr(int itr){
        this.maxCItr = itr;
    }
    public int getMaxCItr(){
        return this.maxCItr;
    }
    
    public void setConvertHbonds(){
        this.convertHbonds = true;
    }
    public void setConvertHbonds(boolean yesno){
        this.convertHbonds = yesno;
    }
    public boolean convertHbonds(){
        return this.convertHbonds;
    }


    public void setThermostat(String thermostat){
        this.thermostat = new String(thermostat);
    }
    public String getThermostat(){
        return this.thermostat;
    }


}
