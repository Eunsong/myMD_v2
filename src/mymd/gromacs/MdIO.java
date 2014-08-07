package mymd.gromacs;

/**
 * I/O class for Molecular simulations
 *
 * @author : Eunsong Choi (echoi9@wisc.edu)
 */


import java.util.*;
import java.io.*;


public class MdIO {

    /**
     * read initial coordinates from *.gro file (GROMACS gro file format)
     * Note the difference between readMdTraj and readGromacsSystem is that the latter also updates 
     * atom names, residue names etc while this one only updates pos, vel and resId in the system
     *
     * @param filename input file name 
     * @param system input GromacsSystem
     */
    public static void readMdTraj(String filename, GromacsSystem system) {

        // Check whether the input file exists and is readable
        File file = new File(filename);
        if ( !file.exists() || !file.canRead() ) {
            System.out.println("Error: Cannot access input file " + filename );
            System.exit(0);
        }

        Scanner inFile;

        try {

            inFile = new Scanner(file);

            String line = inFile.nextLine();
            String title = line;

            // read total number of particles
            line = inFile.nextLine().trim();
            int N = Integer.parseInt(line.replaceAll("\\s",""));

            for ( int i = 0; i < N; i++ ) {

                line = inFile.nextLine().trim();
                String[] tokens = line.split("\\s+"); // split strings by one or more spaces

                // if atomid and atomname fields are merged (i.e.when number of atoms exceeds 9,999 )
                if ( tokens.length == 5 || tokens.length == 8 ) {
                    String[] splitToken1;
                    splitToken1 = new String[2];
                    splitToken1[0] = tokens[1].substring(0, tokens[1].length()-5).trim();
                    splitToken1[1] = tokens[1].substring(tokens[1].length()-5, tokens[1].length() ).trim();
                    String[] tmpTokens = new String[tokens.length + 1];
                    tmpTokens[0] = tokens[0];
                    for ( int j = 2; j < tokens.length; j++ ) {
                        tmpTokens[j+1] = tokens[j];
                    }
                    tmpTokens[1] = splitToken1[0];
                    tmpTokens[2] = splitToken1[1];
        
                    tokens = tmpTokens;             
                }

                int indexOfFirstNonNumeric = 0;
                for ( int c = 0; c < tokens[0].length(); c++ ) {
                    if ( tokens[0].substring(c,c+1).matches("[^\\d.]") ) {
                        indexOfFirstNonNumeric = c;
                        break;
                    }
                }
                int resId = Integer.parseInt( tokens[0].substring(0, indexOfFirstNonNumeric) );
                String resName = tokens[0].substring(indexOfFirstNonNumeric); // remove resId 
                String atomName = tokens[1];
                int atomId = Integer.parseInt(tokens[2]);       
    
                double x = Double.parseDouble(tokens[3]);
                double y = Double.parseDouble(tokens[4]);       
                double z = Double.parseDouble(tokens[5]);
                Vector pos = new Vector(x, y, z);
                system.getTraj().setPos(i, pos);
                system.getSite(i).setResId( resId - 1); // resId should start from 0 in the actual code  
                // set velocities if initial velocities are given   
                try {
                    double vx = Double.parseDouble(tokens[6]);
                    double vy = Double.parseDouble(tokens[7]);      
                    double vz = Double.parseDouble(tokens[8]);
                    Vector vel = new Vector(vx, vy, vz);
                    system.getTraj().setVel(i, vel);
                }
                catch ( ArrayIndexOutOfBoundsException ex ) {
                    // skip if no velocities are defined
                }

            }

            // read box size
            line = inFile.nextLine().trim();
            String[] tokens = line.split("\\s+"); // split strings by one or more spaces

            double lx = Double.parseDouble(tokens[0]);
            double ly = Double.parseDouble(tokens[1]);
            double lz = Double.parseDouble(tokens[2]);
            system.getTraj().setBox(new Vector(lx, ly, lz));

        }
        catch(IOException ex) {
            System.out.println("IOException caught: something wrong in reading input file");
            System.exit(0);
        }
    }







    /**
     * read system information including initial coordinates from *.gro file (GROMACS gro file format)
     *  
     * @param filename input file name 
     * @param system input GromacsSystem
     */
    public static void readGromacsSystem(String filename, GromacsSystem system) {

        // Check whether the input file exists and is readable
        File file = new File(filename);
        if ( !file.exists() || !file.canRead() ) {
            System.out.println("Error: Cannot access input file " + filename);
            System.exit(0);
        }

        Scanner inFile;

        try {

            inFile = new Scanner(file);

            String line = inFile.nextLine();
            String title = line;

            // read total number of particles
            line = inFile.nextLine().trim();
            int N = Integer.parseInt(line.replaceAll("\\s",""));

            for ( int i = 0; i < N; i++ ) {

                line = inFile.nextLine().trim();
                String[] tokens = line.split("\\s+"); // split strings by one or more spaces

                // if atomid and atomname fields are merged (i.e.when number of atoms exceeds 9,999 )
                if ( tokens.length == 5 || tokens.length == 8 ) {
                    String[] splitToken1;
                    splitToken1 = new String[2];
                    splitToken1[0] = tokens[1].substring(0, tokens[1].length()-5).trim();
                    splitToken1[1] = tokens[1].substring(tokens[1].length()-5, tokens[1].length() ).trim();
                    String[] tmpTokens = new String[tokens.length + 1];
                    tmpTokens[0] = tokens[0];
                    for ( int j = 2; j < tokens.length; j++ ) {
                        tmpTokens[j+1] = tokens[j];
                    }
                    tmpTokens[1] = splitToken1[0];
                    tmpTokens[2] = splitToken1[1];
        
                    tokens = tmpTokens;             
                }

                int indexOfFirstNonNumeric = 0;
                for ( int c = 0; c < tokens[0].length(); c++ ) {
                    if ( tokens[0].substring(c,c+1).matches("[^\\d.]") ) {
                        indexOfFirstNonNumeric = c;
                        break;
                    }
                }
                int resId = Integer.parseInt( tokens[0].substring(0, indexOfFirstNonNumeric) );
                String resName = tokens[0].substring(indexOfFirstNonNumeric); // remove resId 
                String atomName = tokens[1];
                int atomId = Integer.parseInt(tokens[2]);       
                    
                double x = Double.parseDouble(tokens[3]);
                double y = Double.parseDouble(tokens[4]);       
                double z = Double.parseDouble(tokens[5]);
                Vector pos = new Vector(x, y, z);

            
                // set velocities if initial velocities are given   
                try {
                    double vx = Double.parseDouble(tokens[6]);
                    double vy = Double.parseDouble(tokens[7]);      
                    double vz = Double.parseDouble(tokens[8]);
                    Vector vel = new Vector(vx, vy, vz);
                    system.getTraj().setVel(i, vel);
                }
                catch ( ArrayIndexOutOfBoundsException ex ) {
                    // skip if no velocities are defined
                }

                // add site to system and set initial coordinate  
                MdSite site = new MdSite();
                site.setResId(resId-1); // in the actual MD code, all indices start from 0
                site.setResName(resName);
                site.setAtomName(atomName);
                site.setAtomId(atomId-1); // in the actual MD code, all indices start from 0
                system.setSite(i, site);
                system.getTraj().setPos(i, pos);

            }

            // read box size
            line = inFile.nextLine().trim();
            String[] tokens = line.split("\\s+"); // split strings by one or more spaces

            double lx = Double.parseDouble(tokens[0]);
            double ly = Double.parseDouble(tokens[1]);
            double lz = Double.parseDouble(tokens[2]);
            system.getTraj().setBox(new Vector(lx, ly, lz));

        }
        catch(IOException ex) {
            System.out.println("IOException caught: something wrong in reading input file");
            System.exit(0);
        }

    }







    /**
     * read run parameters from given *.prm file
     *
     * @param filename input file name 
     * @param prm input MdPrm
     */
    public static void readMdPrm(String filename, MdPrm prm) {

        // Check whether the input file exists and is readable
        File file = new File(filename);
        if ( !file.exists() || !file.canRead() ) {
            System.out.println("Error: Cannot access input file " + filename );
            System.exit(0);
        }

        Scanner inFile;

        try {
            inFile = new Scanner(file);
            String line = inFile.nextLine();
            String title = line;

            while ( inFile.hasNext() ) {
                line = removeComment(inFile.nextLine()).trim();
                String[] tokens = line.split("\\s+");
//              for ( String tmp : tokens ) tmp = tmp.trim();

                if ( tokens[0].equals("hashTS")) { 
                    prm.setHashTS(Integer.parseInt(tokens[2]));
                }
                else if ( tokens[0].equals("dt")) {
                    prm.setDt(Double.parseDouble(tokens[2]));
                }
                else if ( tokens[0].equals("nsteps")) {
                    prm.setNsteps(Integer.parseInt(tokens[2]));
                }
                else if ( tokens[0].equals("rc")) {
                    prm.setRc(Double.parseDouble(tokens[2]));
                }
                else if ( tokens[0].equals("rvdw")) {
                    prm.setRvdw(Double.parseDouble(tokens[2]));
                }
                else if ( tokens[0].equals("rlist")) {
                    prm.setRlist(Double.parseDouble(tokens[2]));
                }
                else if ( tokens[0].equals("nstlist")) {
                    prm.setNstlist(Integer.parseInt(tokens[2]));
                }
                else if ( tokens[0].equals("nstxout")) {
                    prm.setNstxout(Integer.parseInt(tokens[2])); 
                }
                else if ( tokens[0].equals("nstvout")) {
                    prm.setNstvout(Integer.parseInt(tokens[2])); 
                }
                else if ( tokens[0].equals("nstenergy")) {
                    prm.setNstenergy(Integer.parseInt(tokens[2])); 
                }
                else if ( tokens[0].equals("T0")) {
                    prm.setT0(Double.parseDouble(tokens[2]));
                }
                else if ( tokens[0].equals("refT")) {
                    prm.setRefT(Double.parseDouble(tokens[2]));
                }
                else if ( tokens[0].equals("tauT")) {
                    prm.setTauT(Double.parseDouble(tokens[2]));
                }
                else if ( tokens[0].equals("pme")) {
                    if ( tokens[2].equals("true") ) prm.setPME(true);
                    else if ( tokens[2].equals("false")) prm.setPME(false);
                    else {  
                        System.out.println("ERROR! Invalid entry for PME(should be true or false)");
                        System.exit(0);
                    }
                }
                else if ( tokens[0].equals("Lennard_Jones")) {
                    if ( tokens[2].equals("true") ) prm.setLennard_Jones(true);
                    else if ( tokens[2].equals("false")) prm.setLennard_Jones(false);
                    else {  
                        System.out.println("ERROR! Invalid entry for Lennard_Jones(should be true or false)");
                        System.exit(0);
                    }
                }
                else if ( tokens[0].equals("n_pme")) {
                    prm.setN_pme(Integer.parseInt(tokens[2])); 
                }
                else if ( tokens[0].equals("K_pme")) {
                    prm.setK_pme(Integer.parseInt(tokens[2])); 
                }
                else if ( tokens[0].equals("beta_pme")) {
                    prm.setBeta_pme(Double.parseDouble(tokens[2])); 
                }
                else if ( tokens[0].equals("coulombType")) {
                    prm.setCoulombType(new String(tokens[2])); 
                }
                else if ( tokens[0].equals("vdwType")) {
                    prm.setVdwType(new String(tokens[2])); 
                }
                else if ( tokens[0].equals("convertHbonds")) {
                    if ( tokens[2].equals("true") ) prm.setConvertHbonds(true);
                    else if ( tokens[2].equals("false")) prm.setConvertHbonds(false);
                    else {  
                        System.out.println("ERROR! Invalid entry for convertHbonds(should be true or false)");
                        System.exit(0);
                    }
                }
                else if ( tokens[0].equals("maxCItr")) {
                    prm.setMaxCItr(Integer.parseInt(tokens[2])); 
                }
                else if ( tokens[0].equals("CTol")) {
                    prm.setCTol(Double.parseDouble(tokens[2]));
                }
                else if ( tokens[0].equals("thermostat")) {
                    if ( tokens[2].equals("berendsen") || tokens[2].equals("none") ) {
                        prm.setThermostat(tokens[2]);
                    }
                    else {
                        System.out.println("ERROR! Unsupported thermostat type " + tokens[2]);
                        System.exit(0);
                    }
                }
                else if ( tokens[0].equals("")) {
                    // empty line. do nothing
                }
                else {
                    System.out.println(String.format(
                    "WARNING! Invalid entry [%s] in the prm file", tokens[0]));
                }
            }
        }
        catch(IOException ex) {
            System.out.println(String.format(
            "IOException caught: something wrong in reading input file %s", filename));
            System.exit(0);
        }

    }











    /**
     * Read topology from input file 
     * Known issue : Current version does not support #define and #ifndef entries in topology files
     *               It will just ignore those lines and override previous entires with the latest ones 
     *               if there is any duplicate entires 
     *
     * @param filename input file name 
     * @param top input topology
     */
    public static void readMdTop(String filename, MdTop top) {

        // Check whether the input file exists and is readable
        File file = new File(filename);
        if ( !file.exists() || !file.canRead() ) {
            System.out.println("Error: Cannot access input file " + filename);
            System.exit(0);
        }

        Scanner inFile;

        // input order of molecules in the input topology file
        int rank = 0;
        
        Field currField = Field.unsupported; 

        try {
            inFile = new Scanner(file);
            
            while ( inFile.hasNext() ) {

                String line = removeComment(inFile.nextLine()).trim();

                int indexOfOpenBracket = line.indexOf("[");
                int indexOfLastBracket = line.lastIndexOf("]");

                String[] tokens = line.split("\\s+");

                // determine field option [ field ] 
                if ( indexOfOpenBracket != -1 && indexOfLastBracket != -1 ) {
                    String fieldType = line.substring(indexOfOpenBracket+1, indexOfLastBracket).trim();
                        
                    // [ system ] field
                    if ( fieldType.equals("system") ) {
                        currField = Field.system;
                    }
                    // [ molecules ] field
                    else if ( fieldType.equals("molecules") ) {
                        currField = Field.molecules;
                    }
                    // [ defaults ] field
                    else if ( fieldType.equals("defaults") ) {
                        currField = Field.defaults;
                    }
                    // [ atomtypes ] field
                    else if ( fieldType.equals("atomtypes") ) {
                        currField = Field.atomtypes;
                    }
                    // [ bondtypes ] field
                    else if ( fieldType.equals("bondtypes") ) {
                        currField = Field.bondtypes;
                    }
                    // [ angletypes ] field
                    else if ( fieldType.equals("angletypes") ) {
                        currField = Field.angletypes;
                    }
                    // [ dihedraltypes ] field
                    else if ( fieldType.equals("dihedraltypes") ) {
                        currField = Field.dihedraltypes;
                    }
                    // [ moleculetype ] field
                    else if ( fieldType.equals("moleculetype") ) {
                        currField = Field.moleculetype;
                    }
                    // [ atoms ] field
                    else if ( fieldType.equals("atoms") ) {
                        currField = Field.atoms;
                    }
                    // [ exclusions ] field
                    else if ( fieldType.equals("exclusions") ) {
                        currField = Field.exclusions;
                    }
                    // [ polarization ] field
                    else if ( fieldType.equals("polarization") ) {
                        currField = Field.polarization;
                    }
                    // [ thole_polarization ] field
                    else if ( fieldType.equals("thole_polarization") ) {
                        currField = Field.thole_polarization;
                    }
                    // [ bonds ] field
                    else if ( fieldType.equals("bonds") ) {
                        currField = Field.bonds;
                    }
                    // [ angles ] field
                    else if ( fieldType.equals("angles") ) {
                        currField = Field.angles;
                    }
                    // [ dihedrals ] field
                    else if ( fieldType.equals("dihedrals") ) {
                        currField = Field.dihedrals;
                    }
                    // [ nonbond_params ] field
                    else if ( fieldType.equals("nonbond_params")) {
                        currField = Field.nonbond_params;
                    }
                    // [ constraints ] field
                    else if ( fieldType.equals("constraints")) {
                        currField = Field.constraints;
                    }
                    // currently unsupported field directives
                    else {
                        currField = Field.unsupported;
                    }
                }


                // recursive call to other included toplogy files
                else if ( tokens[0].equals("#include") ) {
                    readMdTop(tokens[1].replace("\"",""), top);
                }   




                else {
                    try {
                        switch (currField) {
                            case system:
                                top.setSystemName(line);

                                break;

                            case molecules:
                                
                                // corresponding moleculetypes must be defined before   
//                              MdMolType moltype = new MdMolType();
//                              moltype.setType(tokens[0]);
                                for ( MdMolType moltype : top.getMolTypes() ) {
                                    if ( moltype.getType().equals(tokens[0]) ) {
                                        moltype.setNMols(Integer.parseInt(tokens[1]));
                                        moltype.setRank(rank);
                                        rank++;
                                    }
                                }
                                break;

                            case defaults:
                                top.setNbFunc(Integer.parseInt(tokens[0]));
                                top.setCombRule(Integer.parseInt(tokens[1]));
                                try {
                                    top.setFudgeLJ(Double.parseDouble(tokens[3]));
                                    top.setFudgeQQ(Double.parseDouble(tokens[4]));
                                }
                                catch ( ArrayIndexOutOfBoundsException ex ) { 
                                    // if no fudge values were given
                                }   
                                break;

                            case atomtypes:
                                if ( tokens[3].equals("A") || tokens[3].equals("S") || tokens[3].equals("D") ) {
                                    MdAtomType atom = new MdAtomType();
                                    atom.setType(tokens[0]);
                                    atom.setMass(Double.parseDouble(tokens[1]));
                                    atom.setCharge(Double.parseDouble(tokens[2]));
                                    atom.setPType(tokens[3]);
                                    atom.setVii(Double.parseDouble(tokens[4]));
                                    atom.setWii(Double.parseDouble(tokens[5]));
                                    // remove previously inserted information on the same atom type if there is any 
                                    int duplicateEntry = -1;
                                    for ( int i = 0; i < top.getAtomTypes().size(); i++ ) {
                                        if ( top.getAtomTypes().get(i).getType().equals(tokens[0]) ) {
                                            System.out.println("Duplicate atomtype entry found in " + filename + ". Will use last entry.");
                                            duplicateEntry = i;
                                        }
                                    }
                                    if ( duplicateEntry != -1 ) {
                                        top.getAtomTypes().remove(duplicateEntry);
                                    }
                                    top.addAtomType(atom);
                                }
                                else if ( tokens[4].equals("A") || tokens[4].equals("S") || tokens[4].equals("D") ) { 
                                    MdAtomType atom = new MdAtomType();
                                    atom.setType(tokens[0]);
                                    atom.setMass(Double.parseDouble(tokens[2]));
                                    atom.setCharge(Double.parseDouble(tokens[3]));
                                    atom.setPType(tokens[4]);
                                    atom.setVii(Double.parseDouble(tokens[5]));
                                    atom.setWii(Double.parseDouble(tokens[6]));
                                    // remove previously inserted information on the same atom type if there is any 
                                    int duplicateEntry = -1;
                                    for ( int i = 0; i < top.getAtomTypes().size(); i++ ) {
                                        if ( top.getAtomTypes().get(i).getType().equals(tokens[0]) ) {
                                            System.out.println("Duplicate atomtype entry found in " + filename + ". Will use last entry.");
                                            duplicateEntry = i;
                                        }
                                    }
                                    if ( duplicateEntry != -1 ) {
                                        top.getAtomTypes().remove(duplicateEntry);
                                    }
                                    top.addAtomType(atom);
                                }
                                else if ( tokens[5].equals("A") || tokens[5].equals("S") || tokens[5].equals("D") ) { 
                                    MdAtomType atom = new MdAtomType();
                                    atom.setType(tokens[0]);
                                    atom.setAtomName(tokens[1]);
                                    atom.setMass(Double.parseDouble(tokens[3]));
                                    atom.setCharge(Double.parseDouble(tokens[4]));
                                    atom.setPType(tokens[5]);
                                    atom.setVii(Double.parseDouble(tokens[6]));
                                    atom.setWii(Double.parseDouble(tokens[7]));
                                    // remove previously inserted information on the same atom type if there is any 
                                    int duplicateEntry = -1;
                                    for ( int i = 0; i < top.getAtomTypes().size(); i++ ) {
                                        if ( top.getAtomTypes().get(i).getType().equals(tokens[0]) ) {
                                            System.out.println("Duplicate atomtype entry found in " + filename + ". Will use last entry.");
                                            duplicateEntry = i;
                                        }
                                    }
                                    if ( duplicateEntry != -1 ) {
                                        top.getAtomTypes().remove(duplicateEntry);
                                    }
                                    top.addAtomType(atom);
                                }
                                else if ( tokens.length != 0 ) {
                                    System.out.println("Unkown [ atomtypes ] entry in " + filename);
                                    System.exit(0);
                                } 
 
                                break;

                            case bondtypes:
                                MdBondType bondtype = new MdBondType();
                                bondtype.setAtomTypes( tokens[0], tokens[1] );
                                bondtype.setFunc( Integer.parseInt(tokens[2]) );
                                bondtype.setB0( Double.parseDouble(tokens[3]));
                                bondtype.setK0( Double.parseDouble(tokens[4]));

                                // remove previously inserted information on the same bondtype if there is any 
                                int duplicateEntry = -1;
                                for ( int i = 0; i < top.getBondTypes().size(); i++ ) {
                                    MdBondType tmpType = top.getBondTypes().get(i); // already inserted bond type
                                    String[] tmpnames = tmpType.getAtomTypes(); 
                                    int tmpFtype = tmpType.getFunc(); // function type of temporary bondType
                                    if ( ( tmpnames[0].equals(tokens[0]) && tmpnames[1].equals(tokens[1]) 
                                        && tmpFtype == bondtype.getFunc()) ||
                                         ( tmpnames[1].equals(tokens[0]) && tmpnames[0].equals(tokens[1]) 
                                        && tmpFtype == bondtype.getFunc()) ) {
                                        System.out.println(String.format(
                                        "Duplicate bondtype entry for %s and %s found in %s. Will use last entry.",tokens[0], tokens[1], filename));
                                        duplicateEntry = i;
                                    }
                                }
                                if ( duplicateEntry != -1 ) {
                                    top.getBondTypes().remove(duplicateEntry);
                                }
                                
                                top.addBondType(bondtype);
    
                                break;

                            case angletypes:
                                MdAngleType angletype = new MdAngleType();
                                angletype.setAtomTypes( tokens[0], tokens[1], tokens[2] );
                                angletype.setFunc( Integer.parseInt(tokens[3]));
                                angletype.setT0( Math.toRadians(Double.parseDouble(tokens[4]))); // convert to radians
                                angletype.setK0( Double.parseDouble(tokens[5]));

                                // remove previously inserted information on the same angletype if there is any 
                                duplicateEntry = -1;
                                for ( int i = 0; i < top.getAngleTypes().size(); i++ ) {
                                    MdAngleType tmpType = top.getAngleTypes().get(i); // already inserted angle type
                                    String[] tmp = tmpType.getAtomTypes();
                                    int tmpFtype = tmpType.getFunc();
                                    if ( ( tmp[0].equals(tokens[0]) && tmp[1].equals(tokens[1]) && tmp[2].equals(tokens[2]) 
                                        && tmpFtype == angletype.getFunc() ) ||
                                         ( tmp[2].equals(tokens[0]) && tmp[1].equals(tokens[1]) && tmp[0].equals(tokens[2]) 
                                        && tmpFtype == angletype.getFunc() ) ) {
                                        System.out.println("Duplicate angletype entry found in " + filename + ". Will use last entry.");
                                        duplicateEntry = i;
                                    }
                                }
                                if ( duplicateEntry != -1 ) {
                                    top.getAngleTypes().remove(duplicateEntry);
                                }

                                top.addAngleType(angletype);                            
    
                                break;

                            case dihedraltypes:
                                // type 1
                                if ( Integer.parseInt(tokens[4]) == 1 ) {
                                    MdDihedralType dihedraltype = new MdDihedralType();
                                    dihedraltype.setAtomTypes( tokens[0], tokens[1], tokens[2], tokens[3] );
                                    dihedraltype.setFunc(1);
                                    dihedraltype.setP0( Math.toRadians(Double.parseDouble(tokens[5])) ); // convert to radians
                                    dihedraltype.setK0( Double.parseDouble(tokens[6]) );
                                    dihedraltype.setN( Integer.parseInt(tokens[7]) );

                                    // remove previously inserted information on the same angletype if there is any 
                                    duplicateEntry = -1;
                                    for ( int i = 0; i < top.getDihedralTypes().size(); i++ ) {
                                        MdDihedralType tmpType = top.getDihedralTypes().get(i); // already inserted diheral type
                                        String[] tmp = tmpType.getAtomTypes();
                                        int tmpFtype = tmpType.getFunc();
                                        if ( ( tmp[0].equals(tokens[0]) && tmp[1].equals(tokens[1]) && tmp[2].equals(tokens[2])
                                            && tmp[3].equals(tokens[3]) && tmpFtype == dihedraltype.getFunc() ) ||
                                             ( tmp[3].equals(tokens[0]) && tmp[2].equals(tokens[1]) && tmp[1].equals(tokens[2])
                                            && tmp[0].equals(tokens[3]) && tmpFtype == dihedraltype.getFunc() ) ) {
                                            System.out.println(String.format(
                                            "Duplicate dihedraltype entry for %s-%s-%s-%s found in %s. Will use last entry.", 
                                            tokens[0], tokens[1], tokens[2], tokens[3], filename));
                                            duplicateEntry = i;
                                        }
                                    }
                                    if ( duplicateEntry != -1 ) {
                                        top.getDihedralTypes().remove(duplicateEntry);
                                    }
                                    top.addDihedralType(dihedraltype);          
                                }
                                // type 3
                                else if ( Integer.parseInt(tokens[4]) == 3 ) {
                                    MdDihedralType dihedraltype = new MdDihedralType();
                                    dihedraltype.setAtomTypes( tokens[0], tokens[1], tokens[2], tokens[3] );
                                    dihedraltype.setFunc(3);
                                    double[] C = new double[6];
                                    for ( int i = 0; i < 6; i++ ) {
                                        C[i] = Double.parseDouble(tokens[5 + i]);
                                    }   
                                    dihedraltype.setC(C);

                                    // remove previously inserted information on the same angletype if there is any 
                                    duplicateEntry = -1;
                                    for ( int i = 0; i < top.getDihedralTypes().size(); i++ ) {
                                        MdDihedralType tmpType = top.getDihedralTypes().get(i); // already inserted diheral type
                                        String[] tmp = tmpType.getAtomTypes();
                                        int tmpFtype = tmpType.getFunc();
                                        if ( ( tmp[0].equals(tokens[0]) && tmp[1].equals(tokens[1]) && tmp[2].equals(tokens[2])
                                            && tmp[3].equals(tokens[3]) && tmpFtype == dihedraltype.getFunc() ) ||
                                             ( tmp[3].equals(tokens[0]) && tmp[2].equals(tokens[1]) && tmp[1].equals(tokens[2])
                                            && tmp[0].equals(tokens[3]) && tmpFtype == dihedraltype.getFunc() ) ) {
                                            System.out.println(String.format(
                                            "Duplicate dihedraltype entry for %s-%s-%s-%s found in %s. Will use last entry.", 
                                            tokens[0], tokens[1], tokens[2], tokens[3], filename));
                                            duplicateEntry = i;
                                        }
                                    }
                                    if ( duplicateEntry != -1 ) {
                                        top.getDihedralTypes().remove(duplicateEntry);
                                    }
                                    top.addDihedralType(dihedraltype);          
                                }
                                else {
                                    System.out.println("Not supported dihedral type " + tokens[4]);
                                }   
                                break;

                            case moleculetype:
                                MdMolType moltype = new MdMolType();
                                moltype.setType(tokens[0]);
                                moltype.setNrExcl(Integer.parseInt(tokens[1]));
                                top.addMolType(moltype);                        
                                break;

                            case atoms:
                                // atoms are beloning to the moleculetype that is inserted to molTypes last
                                int nr = Integer.parseInt(tokens[0]) - 1; // starts from 0 in the actual code
                                // update total number of atoms
                                top.getMolTypes().get( top.getMolTypes().size() - 1 ).setNAtoms(nr+1); 
                                String type = tokens[1];
                                int resnr = Integer.parseInt(tokens[2]) - 1; //starts from 0 in the actual code
                                String resName = tokens[3];
                                String atomName = tokens[4];
                                int cgnr = Integer.parseInt(tokens[5]) - 1; // starts from 0 in the actual code

                                MdAtom atom = new MdAtom();
                                atom.setNr(nr);
                                atom.setType(type);
                                atom.setResNr(resnr);
                                atom.setResName(resName);
                                atom.setName(atomName);
                                atom.setCgNr(cgnr);

                                try { // optional
                                    double charge = Double.parseDouble(tokens[6]);
                                    atom.setCharge(charge);
                                }
                                catch ( Exception ex) {
System.out.println("no charge in [ atoms ]");
                                }
                                try { // optional
                                    double mass = Double.parseDouble(tokens[7]);
                                    atom.setMass(mass);
                                }
                                catch ( Exception ex ) {
                                }

                                top.getMolTypes().get(top.getMolTypes().size() - 1).addAtom(atom);
                                break;


                            case exclusions:
                
                                break;

                            case polarization:

                                break;

                            case thole_polarization:

                                break;

                            case bonds:
                                // bonds are belonging to the moleculetype that is inserted to molTypes last
                                int ai = Integer.parseInt(tokens[0]) - 1; // index starts from 0                                
                                int aj = Integer.parseInt(tokens[1]) - 1; // index starts from 0
                                int func = Integer.parseInt(tokens[2]);
                                
                                MdBond bond = new MdBond(ai, aj, func);

                                try { // optional
                                    double b0 = Double.parseDouble(tokens[3]);
                                    double k0 = Double.parseDouble(tokens[4]);
                                    bond.setB0(b0);
                                    bond.setK0(k0);
                                }           
                                catch ( Exception ex) {
                                }
                                top.getMolTypes().get( top.getMolTypes().size() - 1 ).addBond(bond);

                                break;

                            case angles:

                                // angles are belonging to the moleculetype that is inserted to molTypes last
                                ai = Integer.parseInt(tokens[0]) - 1; // index starts from 0                                
                                aj = Integer.parseInt(tokens[1]) - 1; // index starts from 0
                                int ak = Integer.parseInt(tokens[2]) - 1; // index starts from 0
                                func = Integer.parseInt(tokens[3]);
                                MdAngle angle = new MdAngle(ai, aj, ak, func);
                                try { // optional
                                    double t0 = Math.toRadians(Double.parseDouble(tokens[4])); // conert to radians
                                    double k0 = Double.parseDouble(tokens[5]);
                                    angle.setT0(t0);
                                    angle.setK0(k0);
                                }           
                                catch ( Exception ex) {
                                }
                                top.getMolTypes().get( top.getMolTypes().size() - 1 ).addAngle(angle);

                                break;

                            case dihedrals:

                                // dihedrals are belonging to the moleculetype that is inserted to molTypes last
                                ai = Integer.parseInt(tokens[0]) - 1; // all indices starts from 0                          
                                aj = Integer.parseInt(tokens[1]) - 1;
                                ak = Integer.parseInt(tokens[2]) - 1;
                                int al = Integer.parseInt(tokens[3]) - 1;
                                func = Integer.parseInt(tokens[4]);

                                MdDihedral dihedral = new MdDihedral(ai, aj, ak, al, func);

                                double[] C;
                                
                                if ( func == 1 ) {
                                    try { // optional
                                        double p0 = Math.toRadians(Double.parseDouble(tokens[5]));
                                        double k0 = Double.parseDouble(tokens[6]);
                                        int n = Integer.parseInt(tokens[7]);
                                        dihedral.setParam(p0, k0, n);
                                    }           
                                    catch ( Exception ex) {
                                    }
                                    top.getMolTypes().get( top.getMolTypes().size() - 1 ).addDihedral(dihedral);
                                }
                                else if ( func == 3 ) {
                                    C = new double[6];

                                    try { // optional
                                        for ( int ci = 0; ci < 6; ci++ ) {
                                            C[ci] = Double.parseDouble(tokens[5 + ci]);
                                        }
                                        dihedral.setC(C);
                                    }           
                                    catch ( Exception ex) {
                                    }
                                    top.getMolTypes().get( top.getMolTypes().size() - 1 ).addDihedral(dihedral);
                                }
                                else if ( func == 99 ) {

                                    top.getMolTypes().get( top.getMolTypes().size() - 1 ).addDihedral(dihedral);
                                }

                                break;

                            case nonbond_params:

                                String typei = tokens[0];
                                String typej = tokens[1];
                                int ftype = Integer.parseInt(tokens[2]); // comb rule type
                                double Vij = Double.parseDouble(tokens[3]); 
                                double Wij = Double.parseDouble(tokens[4]);
                                // if inputs are sigma/epsilon convert them to C6 and C12
                                // (for computational convenience)
                                if ( ftype == 2 || ftype == 3 ) { 
                                    double sigmaij = Vij;
                                    double epsilonij = Wij;
                                    Vij = 4*epsilonij*Math.pow(sigmaij,6);
                                    Wij = 4*epsilonij*Math.pow(sigmaij,12);
                                }
                                MdNonBondParam nonbond_param = new MdNonBondParam(typei, typej, ftype, Vij, Wij);
                                top.addNonBondParam(nonbond_param);                     

                                break;

                            case constraints:
                                // constraints are belonging to the moleculetype that is inserted to molTypes last
                                ai = Integer.parseInt(tokens[0]) - 1; // index starts from 0                                
                                aj = Integer.parseInt(tokens[1]) - 1; // index starts from 0
                                ftype = Integer.parseInt(tokens[2]);
                                double b0 = Double.parseDouble(tokens[3]);

                                MdConstraint constraint = new MdConstraint(ai, aj, ftype, b0);
                                top.getMolTypes().get( top.getMolTypes().size() - 1 ).addConstraint(constraint);

                                break;

                            case unsupported:

                                break;
                        }
                    }
                    catch ( Exception ex ) {
                        // this menas a blank line 
                    }               
                }

            }

        }
        catch(IOException ex) {
            System.out.println("IOException caught: something wrong in " + filename);
            System.exit(0);
        }

    }

    // Field directives enumeration
    public enum Field {
        system(1), molecules(99), defaults(2), atomtypes(3), bondtypes(4),
        angletypes(5), dihedraltypes(6), moleculetype(7), atoms(8), exclusions(9),
        polarization(10), thole_polarization(11), bonds(12), angles(13), dihedrals(14),
        nonbond_params(15), constraints(16), unsupported(0);

        private int value;
        private Field(int value) {
            this.value = value;
        }
    }



    /****************************** End of readMdTop ************************************************/
    /************************************************************************************************/










    /**
	 * Convert input tolopogies into more usable format and load it to the mdsystem 
     *
     * @param top input topology file. converted information will be added in it.
     * @param system input GromacsSystem providing system information
     */

    public static void convMdTop(MdTop top, GromacsSystem system) {

        // re-arrange moleculetypes defined in top in an order they are written in [ molecules ] 
        top.reOrderMolTypes();

        // build particle information defined in mdsites in system  
        int iatom = 0; // atom index
        int imol_init = 0; 
        int molCount = 0; // number of molcules
        int iatomType = 0; // atom type index

        for ( int itype = 0; itype < top.getMolTypes().size(); itype++ ) {
    
            MdMolType moltype = top.getMolTypes().get(itype);

            for ( int imol = 0; imol < moltype.getNMols(); imol++ ) {
                for ( MdAtom atom : moltype.getAtoms() ) {
                    MdSite site = new MdSite();
                    site.setMolId(molCount);
                    site.setAtomType(atom.getType());
                    // add it to atomTypesInUse field if has not been added
                    boolean alreadyAdded = false;
                    for ( MdAtomType addedtype : top.getAtomTypesInUse() ) {
                        if ( atom.getType().equals(addedtype.getType()) ) {
                            alreadyAdded = true;
                        }
                    }
                    if ( !alreadyAdded ) { 
                        for ( MdAtomType newtype : top.getAtomTypes() ) {
                            if ( atom.getType().equals(newtype.getType() ) ) {
                                newtype.setTypeId(iatomType);
                                top.getAtomTypesInUse().add(newtype);
                                iatomType++;                                    
                            }
                        }
                    }

                    site.setMolTypeId(itype);

                    // update mass, charge, LJ sigma, LJ epsilon if matching atomtype is available
                    for ( MdAtomType atype : top.getAtomTypes() ) {
                        if ( atom.getType().equals( atype.getType() ) ) {
                            site.setMass( atype.getMass() );
                            site.setCharge( atype.getCharge() );
                            site.setWii( atype.getWii() );
                            site.setVii( atype.getVii() );
                        }
                    }
                
                    site.setNr(atom.getNr() );
                    String resName = atom.getResName();
                    site.setResName(atom.getResName() );
                    String atomName = atom.getName();
                    site.setAtomName(atom.getName() );
                    int atomId = iatom;
                    site.setAtomId(iatom);

                    // if some of the site information are loaded already (e.g. from MdIO.MdReadSystem() )

                    try {
                        if ( !system.getSite(iatom).getResName().equals(resName) ) {
                            System.out.println("Residue name in topology file is different from initial configuration file");
                            System.out.println("Using residue names from topology file");
                        }
                        if ( !system.getSite(iatom).getAtomName().equals(atomName) ) {
                            System.out.println("Atom name in topology file is different from initial configuration file");
                            System.out.println("Using atom names from topology file");
                        }
                        if ( system.getSite(iatom).getAtomId() != atomId ) {
                            System.out.println("Atom ID in topology file is different from initial configuration file");
                            System.out.println("Using atom ID from topology file");
                        }
                        site.setResId(system.getSite(iatom).getResId());
                    }
                    catch ( Exception ex) {
                        System.out.println("WARNING! Initial configuration has not been properly read before calling MdIO.convMdTOp()");
                    } 


                    // override mass and charge if the values were defined in atoms field
                    if ( atom.getMass() != -99.99 ) {
                        site.setMass(atom.getMass() );
                    }
                    if ( atom.getCharge() != -99.99 ) {
                        site.setCharge(atom.getCharge() );
                    }

                    system.setSite(iatom, site);
                    iatom++;
                }
                
                molCount++;
            }
            imol_init += moltype.getNMols();
        }   

        // set atom type Id for sites from atomTypesInUse list
        for ( MdSite site : system.getSites() ) {
            String siteAtomType = site.getAtomType();
            for ( MdAtomType atype : top.getAtomTypesInUse() ) {
                if ( siteAtomType.equals( atype.getType() ) ) {
                    site.setAtomTypeId( atype.getTypeId() );                
                }
            }
        }
    

            
        // build bond information 
        imol_init = 0;      
        int iatom_init = 0; 
        for ( int itype = 0; itype < top.getMolTypes().size(); itype++ ) {

            MdMolType moltype = top.getMolTypes().get(itype);

            for ( int imol = 0; imol < moltype.getNMols(); imol++ ) {
                int index_offset = iatom_init + imol*moltype.getNAtoms();
                for ( MdBond bond : moltype.getBonds() ) {
                    MdBond newBond = new MdBond( bond.geti() + index_offset, bond.getj() + index_offset, bond.getFunc() );
                    boolean matchFound = false;
                    // if b0 and k0 values are defined in [ bonds ] section
                    if ( bond.isComplete() ) {
                        newBond.setB0(bond.getB0());
                        newBond.setK0(bond.getK0());
                        top.addBond(newBond);
                        matchFound = true;
                    }
                    // if not, look for matching bondtype   
                    else {
                        String ai_type = new String(moltype.getAtoms().get(bond.geti()).getType());
                        String aj_type = new String(moltype.getAtoms().get(bond.getj()).getType());
                        int ftype = bond.getFunc();
                    
                        String ai_name = new String(moltype.getAtoms().get(bond.geti()).getName());
                        String aj_name = new String(moltype.getAtoms().get(bond.getj()).getName());
    
                        for ( MdBondType bondtype : top.getBondTypes() ) {

                            if ( ( bondtype.getAtomTypes()[0].equals(ai_type) && bondtype.getAtomTypes()[1].equals(aj_type) 
                                && (bondtype.getFunc()==ftype) ) 
                            || ( bondtype.getAtomTypes()[1].equals(ai_type) && bondtype.getAtomTypes()[0].equals(aj_type) 
                                && (bondtype.getFunc()==ftype) ) ) {
                                newBond.setB0( bondtype.getB0() );
                                newBond.setK0( bondtype.getK0() );
                                top.addBond(newBond);
                                matchFound = true;
                                
                                // update bond information (in order to avoid searching for bondtypes again )
                                bond.setB0( bondtype.getB0() );
                                bond.setK0( bondtype.getK0() );
                            }
                            // if no matches are found, check also bondtypes defined using atom names
                            else if ( ( bondtype.getAtomTypes()[0].equals(ai_name) && bondtype.getAtomTypes()[1].equals(aj_name) 
                                && (bondtype.getFunc()==ftype) ) 
                            || ( bondtype.getAtomTypes()[1].equals(ai_name) && bondtype.getAtomTypes()[0].equals(aj_name) 
                                && (bondtype.getFunc()==ftype) ) ) {
                                newBond.setB0( bondtype.getB0() );
                                newBond.setK0( bondtype.getK0() );
                                top.addBond(newBond);
                                matchFound = true;

                                // update bond information (in order to avoid searching for bondtypes again )
                                bond.setB0( bondtype.getB0() );
                                bond.setK0( bondtype.getK0() );
                            }
                            // if still no matches are found, then try using atom names defined in atomtypes field
                            else {

                                for ( MdAtomType atype : top.getAtomTypes() ) {
                                    try {
                                        if ( atype.getType().equals(ai_type) ) {
                                            ai_name = new String( atype.getAtomName() );
                                        }
                                        if ( atype.getType().equals(aj_type) ) {
                                            aj_name = new String( atype.getAtomName() );
                                        }
                                    }
                                    catch ( Exception ex ) {
                                        // atom name is not defined in atype
                                    }
                                }

                                if ( ( bondtype.getAtomTypes()[0].equals(ai_name) && bondtype.getAtomTypes()[1].equals(aj_name) 
                                    && (bondtype.getFunc()==ftype) ) 
                                    || ( bondtype.getAtomTypes()[1].equals(ai_name) && bondtype.getAtomTypes()[0].equals(aj_name) 
                                    && (bondtype.getFunc()==ftype) ) ) {
                                    newBond.setB0( bondtype.getB0() );
                                    newBond.setK0( bondtype.getK0() );
                                    top.addBond(newBond);
                                    matchFound = true;
                                    // update bond information (in order to avoid searching for bondtypes again )
                                    bond.setB0( bondtype.getB0() );
                                    bond.setK0( bondtype.getK0() );
                                }
                            }

                        }
                    }
                
                    if ( !matchFound ) {                
                        System.out.println(
                        String.format("ERROR!! Unmatched bond(%d,%d) exists!", bond.geti()+1, bond.getj()+1));
                    }

                }
            }

            imol_init += moltype.getNMols();            
            iatom_init += moltype.getNMols()*moltype.getNAtoms();
        }





        // build constraint information 
        imol_init = 0;      
        iatom_init = 0; 
        for ( int itype = 0; itype < top.getMolTypes().size(); itype++ ) {

            MdMolType moltype = top.getMolTypes().get(itype);

            for ( int imol = 0; imol < moltype.getNMols(); imol++ ) {
                int index_offset = iatom_init + imol*moltype.getNAtoms();
                MdConstraintGroup constraintGroup = new MdConstraintGroup();

                for ( MdConstraint constraint : moltype.getConstraints() ) {
                    
                    MdConstraint newConstraint = 
                    new MdConstraint(constraint.geti() + index_offset, constraint.getj() + index_offset, constraint.getFunc(), constraint.getDij() );
                    constraintGroup.addConstraint(newConstraint);
//                  top.addConstraint(newConstraint);
                }
                top.addConstraintGroup(constraintGroup);
            }

            imol_init += moltype.getNMols();            
            iatom_init += moltype.getNMols()*moltype.getNAtoms();
        }



        // build angle information 
        imol_init = 0;      
        iatom_init = 0; 
        for ( int itype = 0; itype < top.getMolTypes().size(); itype++ ) {

            MdMolType moltype = top.getMolTypes().get(itype);

            for ( int imol = 0; imol < moltype.getNMols(); imol++ ) {
                int index_offset = iatom_init + imol*moltype.getNAtoms();
                for ( MdAngle angle : moltype.getAngles() ) {
                    
                    MdAngle newAngle = 
                    new MdAngle( angle.geti() + index_offset, angle.getj() + index_offset, angle.getk() + index_offset, angle.getFunc() );
                    boolean matchFound = false;

                    // if t0 and k0 values are defined in [ angles ] section
                    if ( angle.isComplete() ) {
                        newAngle.setT0(angle.getT0());
                        newAngle.setK0(angle.getK0());
                        top.addAngle(newAngle);
                        matchFound = true;
                    }
                    // if not, look for matching bondtype   
                    else {
                        String ai_type = new String(moltype.getAtoms().get(angle.geti()).getType());
                        String aj_type = new String(moltype.getAtoms().get(angle.getj()).getType());
                        String ak_type = new String(moltype.getAtoms().get(angle.getk()).getType());
                        int ftype = angle.getFunc();
                    
                        String ai_name = new String(moltype.getAtoms().get(angle.geti()).getName());
                        String aj_name = new String(moltype.getAtoms().get(angle.getj()).getName());
                        String ak_name = new String(moltype.getAtoms().get(angle.getk()).getName());
    
                        for ( MdAngleType angletype : top.getAngleTypes() ) {
                            if ( ( angletype.getAtomTypes()[0].equals(ai_type) && angletype.getAtomTypes()[1].equals(aj_type) 
                                && angletype.getAtomTypes()[2].equals(ak_type) && (angletype.getFunc()==ftype) ) 
                            || ( angletype.getAtomTypes()[2].equals(ai_type) && angletype.getAtomTypes()[1].equals(aj_type) 
                                && angletype.getAtomTypes()[0].equals(ak_type) && (angletype.getFunc()==ftype) ) ) {
                                newAngle.setT0( angletype.getT0() );
                                newAngle.setK0( angletype.getK0() );
                                top.addAngle(newAngle);
                                matchFound = true;

                                // update angle information (in order to avoid searching for angletypes again )
                                angle.setT0( angletype.getT0());
                                angle.setK0( angletype.getK0());
                            }
                            // if no matches are found, check also angletypes defined using atom names
                            else if ( ( angletype.getAtomTypes()[0].equals(ai_name) && angletype.getAtomTypes()[1].equals(aj_name) 
                                && angletype.getAtomTypes()[2].equals(ak_name) && (angletype.getFunc()==ftype) ) 
                            || ( angletype.getAtomTypes()[2].equals(ai_name) && angletype.getAtomTypes()[1].equals(aj_name) 
                                && angletype.getAtomTypes()[0].equals(ak_name) && (angletype.getFunc()==ftype) ) ) {
                                newAngle.setT0( angletype.getT0() );
                                newAngle.setK0( angletype.getK0() );
                                top.addAngle(newAngle);
                                matchFound = true;

                                // update angle information (in order to avoid searching for angletypes again )
                                angle.setT0( angletype.getT0());
                                angle.setK0( angletype.getK0());
                            }
                            // if still no matches are found, then try using atom names defined in atomtypes field
                            else {
                                for ( MdAtomType atype : top.getAtomTypes() ) {
                                    try {
    
                                        if ( atype.getType().equals(ai_type) ) {
                                            ai_name = new String( atype.getAtomName() );
                                        }
                                        if ( atype.getType().equals(aj_type) ) {
                                            aj_name = new String( atype.getAtomName() );
                                        }
                                        if ( atype.getType().equals(ak_type) ) {
                                            ak_name = new String( atype.getAtomName() );
                                        }
                                    }
        
                                    catch( Exception ex) {
                                        // atom name is not defined in atype
                                    }
                                }

                                if ( ( angletype.getAtomTypes()[0].equals(ai_name) && angletype.getAtomTypes()[1].equals(aj_name) 
                                    && angletype.getAtomTypes()[2].equals(ak_name) && (angletype.getFunc()==ftype) ) 
                                || ( angletype.getAtomTypes()[2].equals(ai_name) && angletype.getAtomTypes()[1].equals(aj_name) 
                                    && angletype.getAtomTypes()[0].equals(ak_name) && (angletype.getFunc()==ftype) ) ) {
                                    newAngle.setT0( angletype.getT0() );
                                    newAngle.setK0( angletype.getK0() );
                                    top.addAngle(newAngle);
                                    matchFound = true;

                                    // update angle information (in order to avoid searching for angletypes again )
                                    angle.setT0( angletype.getT0());
                                    angle.setK0( angletype.getK0());
                                }
                            }


                        }
                    }

                    if ( !matchFound) {
                        System.out.println(
                        String.format("ERROR!!!!!! Unmatched angle(%d,%d,%d) exists", angle.geti()+1, angle.getj()+1, angle.getk()+1));
                    }

                }
            }

            imol_init += moltype.getNMols();            
            iatom_init += moltype.getNMols()*moltype.getNAtoms();
        }




        // build dihedral information 
        imol_init = 0;      
        iatom_init = 0; 
        for ( int itype = 0; itype < top.getMolTypes().size(); itype++ ) {

            MdMolType moltype = top.getMolTypes().get(itype);

            for ( int imol = 0; imol < moltype.getNMols(); imol++ ) {
                int index_offset = iatom_init + imol*moltype.getNAtoms();
                for ( MdDihedral dihedral : moltype.getDihedrals() ) {
                    
                    MdDihedral newDihedral = new MdDihedral( 
                    dihedral.geti() + index_offset, dihedral.getj() + index_offset, dihedral.getk() + index_offset, 
                    dihedral.getl() + index_offset, dihedral.getFunc() );
                    boolean matchFound = false;

                    // if parameters are defined in [ dihedrals ] section
                    if ( dihedral.isComplete() ) {
                        try { // if it contains C type parameters (e.g. type 3)
                            newDihedral.setC(dihedral.getC());
                        }
                        catch ( Exception ex ) {
                        }
                        try { // if it contains type 1 like parameters
                            newDihedral.setParam( dihedral.getP0(), dihedral.getK0(), dihedral.getN() );
                        }
                        catch ( Exception ex ) {
                        }
                        top.addDihedral(newDihedral);
                        matchFound = true;
                    }
                    // if not, look for matching bondtype   
                    else {
                        String ai_type = new String(moltype.getAtoms().get(dihedral.geti()).getType());
                        String aj_type = new String(moltype.getAtoms().get(dihedral.getj()).getType());
                        String ak_type = new String(moltype.getAtoms().get(dihedral.getk()).getType());
                        String al_type = new String(moltype.getAtoms().get(dihedral.getl()).getType());
                        int ftype = dihedral.getFunc();

                        String ai_name = new String(moltype.getAtoms().get(dihedral.geti()).getName());
                        String aj_name = new String(moltype.getAtoms().get(dihedral.getj()).getName());
                        String ak_name = new String(moltype.getAtoms().get(dihedral.getk()).getName());
                        String al_name = new String(moltype.getAtoms().get(dihedral.getl()).getName());
                        
                        for ( MdDihedralType dihedraltype : top.getDihedralTypes() ) {
                            if ( ( dihedraltype.getAtomTypes()[0].equals(ai_type) && dihedraltype.getAtomTypes()[1].equals(aj_type) 
                                && dihedraltype.getAtomTypes()[2].equals(ak_type) && dihedraltype.getAtomTypes()[3].equals(al_type)
                                && (dihedraltype.getFunc()==ftype) ) ||  
                               ( dihedraltype.getAtomTypes()[3].equals(ai_type) && dihedraltype.getAtomTypes()[2].equals(aj_type) 
                                && dihedraltype.getAtomTypes()[1].equals(ak_type) && dihedraltype.getAtomTypes()[0].equals(al_type)
                                && (dihedraltype.getFunc()==ftype) ) ) {

                                try {
                                    newDihedral.setC( dihedraltype.getC() );
                                }
                                catch ( Exception ex ) {
                                }
                                try {
                                    newDihedral.setParam( dihedraltype.getP0(), dihedraltype.getK0(), dihedraltype.getN() );
                                }
                                catch ( Exception ex ) {
                                }
                                top.addDihedral(newDihedral);
                                matchFound = true;
                                
                                // update dihedral information (in order to avoid searching for dihedraltypes again)
                                if ( ftype == 1 ) {
                                    dihedral.setParam( dihedraltype.getP0(), dihedraltype.getK0(), dihedraltype.getN() );
                                }
                                else if ( ftype == 3) {
                                    dihedral.setC( dihedraltype.getC() );
                                }

                            }
                            // if no matches are found, check also dihedrals defined using atom names
                            else if ( ( dihedraltype.getAtomTypes()[0].equals(ai_name) && dihedraltype.getAtomTypes()[1].equals(aj_name) 
                                && dihedraltype.getAtomTypes()[2].equals(ak_name) && dihedraltype.getAtomTypes()[3].equals(al_name)
                                && (dihedraltype.getFunc()==ftype) ) ||
                               ( dihedraltype.getAtomTypes()[3].equals(ai_name) && dihedraltype.getAtomTypes()[2].equals(aj_name) 
                                && dihedraltype.getAtomTypes()[1].equals(ak_name) && dihedraltype.getAtomTypes()[0].equals(al_name)
                                && (dihedraltype.getFunc()==ftype) ) ) {

                                try {
                                    newDihedral.setC( dihedraltype.getC() );
                                }
                                catch ( Exception ex ) {
                                }
                                try {
                                    newDihedral.setParam( dihedraltype.getP0(), dihedraltype.getK0(), dihedraltype.getN() );
                                }
                                catch ( Exception ex ) {
                                }
                                top.addDihedral(newDihedral);
                                matchFound = true;

                                // update dihedral information (in order to avoid searching for dihedraltypes again)
                                if ( ftype == 1 ) {
                                    dihedral.setParam( dihedraltype.getP0(), dihedraltype.getK0(), dihedraltype.getN() );
                                }
                                else if ( ftype == 3) {
                                    dihedral.setC( dihedraltype.getC() );
                                }

                            }
                            // if still no matches are found, then try using atom names defined in atomtypes field
                            else {
                                for ( MdAtomType atype : top.getAtomTypes() ) {
                                    try {
                                        if ( atype.getType().equals(ai_type) ) {
                                            ai_name = new String( atype.getAtomName() );
                                        }
                                        if ( atype.getType().equals(aj_type) ) {
                                            aj_name = new String( atype.getAtomName() );
                                        }
                                        if ( atype.getType().equals(ak_type) ) {
                                            ak_name = new String( atype.getAtomName() );
                                        }
                                        if ( atype.getType().equals(al_type) ) {
                                            al_name = new String( atype.getAtomName() );
                                        }
                                    }
                                    catch( Exception ex) {
                                        // atom name is not defined in atype
                                    }
                                }

                                if ( ( dihedraltype.getAtomTypes()[0].equals(ai_name) && dihedraltype.getAtomTypes()[1].equals(aj_name) 
                                    && dihedraltype.getAtomTypes()[2].equals(ak_name) && dihedraltype.getAtomTypes()[3].equals(al_name)
                                    && (dihedraltype.getFunc()==ftype) ) ||
                                    ( dihedraltype.getAtomTypes()[3].equals(ai_name) && dihedraltype.getAtomTypes()[2].equals(aj_name) 
                                    && dihedraltype.getAtomTypes()[1].equals(ak_name) && dihedraltype.getAtomTypes()[0].equals(al_name)
                                    && (dihedraltype.getFunc()==ftype) ) ) {

                                    try {
                                        newDihedral.setC( dihedraltype.getC() );
                                    }
                                    catch ( Exception ex ) {
                                    }
                                    try {
                                        newDihedral.setParam( dihedraltype.getP0(), dihedraltype.getK0(), dihedraltype.getN() );
                                    }
                                    catch ( Exception ex ) {
                                    }
                                    top.addDihedral(newDihedral);
                                    matchFound = true;

                                    // update dihedral information (in order to avoid searching for dihedraltypes again)
                                    if ( ftype == 1 ) {
                                        dihedral.setParam( dihedraltype.getP0(), dihedraltype.getK0(), dihedraltype.getN() );
                                    }
                                    else if ( ftype == 3) {
                                        dihedral.setC( dihedraltype.getC() );
                                    }

                                }

                            }



                        }
                    }

                    if ( !matchFound) {
                        System.out.println(
                        String.format("ERROR!!!!!! Unmatched dihedral(%d,%d,%d,%d) exists", dihedral.geti()+1, dihedral.getj()+1, dihedral.getk()+1, dihedral.getl()+1));
                    }
    
                }
            }

            imol_init += moltype.getNMols();            
            iatom_init += moltype.getNMols()*moltype.getNAtoms();
        }





        // build list of exclusions
        for ( MdMolType moltype : top.getMolTypes() ) {
            getExclusions(moltype);
        }




        // build nonbonded table for interaction parameters 
        int numAtomTypes = top.getAtomTypesInUse().size();
        List<List<MdNonBondParam>> nonbondTable = top.getNonBondTable();
        int defaultNbType = top.getNbFunc(); // use default nb type if not specifically defined
        int defaultCombRule = top.getCombRule(); 
        for ( int i = 0; i < numAtomTypes; i++ ) {
            String typei = top.getAtomTypesInUse().get(i).getType();
            double Vii = top.getAtomTypesInUse().get(i).getVii();
            double Wii = top.getAtomTypesInUse().get(i).getWii();
            List<MdNonBondParam> col = new ArrayList<MdNonBondParam>();
            for ( int j = 0; j < numAtomTypes; j++ ) {
                String typej = top.getAtomTypesInUse().get(j).getType();
                double Vjj = top.getAtomTypesInUse().get(j).getVii();
                double Wjj = top.getAtomTypesInUse().get(j).getWii();

                double Vij=0.0, Wij=0.0;
    
                if ( defaultCombRule == 1 || defaultCombRule == 3 ) {
                    Vij = combRule1(Vii, Vjj);
                    Wij = combRule1(Wii, Wjj);
                }
                else if ( defaultCombRule == 2 ) {
                    Vij = combRule2(Vii, Vjj); // use algebraic mean for sigma 
                    Wij = combRule1(Wii, Wjj); // use geometric mean for epsilon
                }
                else {
                    System.out.println("Undefined combination rule type!");
                    System.exit(0);
                }


                // convert epsilon and sigma to C6 and C12 if combRule was either 2 or 3
                if ( defaultCombRule == 2 || defaultCombRule == 3 ) {
                    double C6 = 4*Wij*Math.pow(Vij,6);
                    double C12 = 4*Wij*Math.pow(Vij,12);
                    Vij = C6;
                    Wij = C12;
                }

                MdNonBondParam nonbondparam = new MdNonBondParam(typei, typej, defaultNbType, Vij, Wij);
                col.add(nonbondparam);               
            }
            nonbondTable.add(col);
        }

        // Override with nonbond types defined in [ nonbond_params ] field
        for ( int i = 0; i < numAtomTypes; i++ ) {
             for ( int j = 0; j < numAtomTypes; j++ ) {
                String itype = nonbondTable.get(i).get(j).getiType();
                String jtype = nonbondTable.get(i).get(j).getjType();               
                for ( MdNonBondParam nonbondparm : top.getNonBondParams() ) {
                    if ( (itype.equals(nonbondparm.getiType()) && jtype.equals( nonbondparm.getjType()))
                      || (jtype.equals(nonbondparm.getiType()) && itype.equals( nonbondparm.getjType()))){
                        nonbondTable.get(i).set(j, nonbondparm);        
                    }
                }   
            }
        }





    }
    /*********************************** End of confMdTop() ********************************************************/
    /***************************************************************************************************************/



    /******************* Generate list of exclusions from given MdMolType values **********************/
    public static void getExclusions(MdMolType moltype){

        int nrexcl = moltype.getNrExcl();
        boolean[][] directBonds = new boolean[moltype.getNAtoms()][moltype.getNAtoms()];
        for ( boolean[] row : directBonds ) Arrays.fill(row, false);    

        for ( MdBond bond : moltype.getBonds() ) {
            directBonds[bond.geti()][bond.getj()] = true;
            directBonds[bond.getj()][bond.geti()] = true;
        }
        // also check constraints
        for ( MdConstraint constraint : moltype.getConstraints() ) {
            if ( constraint.getFunc() == 1 ) {
                directBonds[constraint.geti()][constraint.getj()] = true;
                directBonds[constraint.getj()][constraint.geti()] = true;
            }
        }

        boolean[][] exclusions = moltype.getExclusions();       

        for ( int i = 0; i < moltype.getNAtoms(); i++ ) {
            for ( int j = i+1; j < moltype.getNAtoms(); j++ ) {
    
                exclusions[i][j] = checkConnectivity(directBonds, nrexcl, i, j);
                exclusions[j][i] = exclusions[i][j];
            }
        }

    }


    // for a given list of bond pairs and number of exclusions(nrexcl), 
    // find if there is a path from site i to site j within the distance of nrexcl bonds away
    // (e.g. if i and j are directly connected by bonded potential, then checkConnectivity(bonds,1, i,j) 
    // should return true )
    public static boolean checkConnectivity(boolean[][] directBonds, int nrexcl, int i, int j) { 
        
        boolean isConnected = false;
    
        // base case
        if ( nrexcl == 0 ) {
            isConnected = false;
        }
        else if ( nrexcl == 1 ) {
            isConnected = directBonds[i][j];
        }
        else if ( nrexcl > 1 ) {
            if ( directBonds[i][j] ) {
                isConnected = true;
            }
            else {
                for ( int k = 0; k < directBonds.length; k++ ) {
                    if ( directBonds[i][k] ) {
                        if ( checkConnectivity(directBonds, nrexcl -1, k, j) ) {
                            isConnected = true;
                        }
                    }
                }
            }
        }

        return isConnected;

    }


    
    /**************** returns a String with the comments removed  ********************************/
    public static String removeComment(String str) {
    
        int offset = str.indexOf(";"); // remove everything which follows ; symbol (GROMACS style)
        if ( -1 != offset ) {
            str = str.substring(0, offset);
        }
        return str;
    }
    



    /********************** combination rules ********************************/
    // comb rule 1 geometric mean
    public static double combRule1(double Cii, double Cjj) {
        return Math.sqrt(Cii*Cjj);  
    }
    // comb rule 2 algebraic mean   
    public static double combRule2(double Cii, double Cjj) {
        return 0.5*(Cii + Cjj);
    }


}
