import java.util.*;

public class table{
	public static void main(String[] args){

		final int BIN_SIZE = 1000000;
	
		double[] exponential = new double[BIN_SIZE];
		for ( int i = 0; i < BIN_SIZE; i++){
			exponential[i] = Math.exp((double)i/(double)BIN_SIZE);
		}
		long timei, timef;
		timei = System.nanoTime();
		for ( int i = 0; i < BIN_SIZE; i++){
			double r = (double)i/(double)BIN_SIZE;
			double value = exponential[(int)(r*BIN_SIZE)];
		}	
		timef = System.nanoTime();
		System.out.println("Look-up table with BIN_SIZE: " + BIN_SIZE + " took " + (double)(timef - timei )*0.000001 + "ms" );

	
		timei = System.nanoTime();
		for ( int i = 0; i < BIN_SIZE; i++){
			double r = (double)i/(double)BIN_SIZE;
			double value = Math.exp(r);
		}	
		timef = System.nanoTime();
		System.out.println("Math.exp() took " + (double)(timef - timei )*0.000001 + "ms" );




	}
}
