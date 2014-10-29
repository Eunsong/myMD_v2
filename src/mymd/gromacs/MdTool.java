package mymd.gromacs;

import java.util.*;
import java.io.*;

public class MdTool{

    /**
     * Computing Distribution of Dihedral angles(in radians)
     *
     * @param angles : an array(in series) that contains dihedral angles(-PI to PI)
     * @param hist : a histogram that stores dihedral angle distribution
     */

    public static void compDihedDist( double[] angles,  double[] hist){
    
        int Nbin = hist.length; // number of bins
        int Nangles = angles.length; // number of input angles
        double dw = 2*Math.PI/(double)Nbin; // bin width
        Arrays.fill(hist, 0.0); // initialize histograom

        for ( int i = 0; i < angles.length; i++ ) {
            double angle = angles[i];
            int binId = (int)((angle+Math.PI)/dw);
//          if ( binId < 0 ) {
//              binId = (int)((2*Math.PI + angle)/dw);
//          }
            try {
                hist[binId] += 1.0/(double)(Nangles*dw);
            }
            catch ( ArrayIndexOutOfBoundsException ex) {
                System.out.println("ERROR in MdTool.compDihedDist():Input dihedral angle out of range");
            }

        }
    }


    /**
     * read Dihedral distribution from input file(in degrees) and store it in hist
     * 
     */
    public static void readDihedralDist( String filename, double[] hist) {

        // Check whether the input file exists and is readable
        File file = new File(filename);
        if ( !file.exists() || !file.canRead() ) {
            System.out.println("Error: Cannot access input file " + filename );
            System.exit(0);
        }

        Arrays.fill(hist, 0.0);

        Scanner inFile;
        try {

            inFile = new Scanner(file);
            int cnt = 0;
            while ( inFile.hasNext() ){
                String line = inFile.nextLine();
                String[] tokens = line.trim().split("\\s+"); 
                try {

                    hist[cnt] = (180/Math.PI)*Double.parseDouble(tokens[1]);
                    cnt++;
/*
                    if ( cnt > hist.length/2 - 1) {
                        hist[cnt - hist.length/2] = (Math.PI/180.0)*Double.parseDouble(tokens[1]);
                        cnt++;
                    }
                    else {
                        hist[hist.length - 1 - cnt] = (Math.PI/180.0)*Double.parseDouble(tokens[1]);
                        cnt++;
                    }
*/
                }
                catch ( ArrayIndexOutOfBoundsException ex) {
                    System.out.println("Size of input dihedral distribution and output array do not match");
                    System.out.println("Input file should only have two columns(first col: angle, second col: probabiliy)");
                    System.exit(0);
                }
            }

        }
        catch(IOException ex) {
            System.out.println("IOException caught: something wrong in reading input file");
            System.exit(0);
        }


    }


}
