import java.util.*;

public class loop{
	public static void main(String[] args){

		final int ARRAY_SIZE = 100000;
		double[] double_array = new double[ARRAY_SIZE];
		float[] float_array = new float[ARRAY_SIZE];
		Double[] Double_array = new Double[ARRAY_SIZE];
		Float[] Float_array = new Float[ARRAY_SIZE];
		WrappedDouble[] WDouble_array = new WrappedDouble[ARRAY_SIZE];
		Arrays.fill(WDouble_array, new WrappedDouble() );

		double[] double_array_dest = new double[ARRAY_SIZE];
		float[] float_array_dest = new float[ARRAY_SIZE];
		Double[] Double_array_dest = new Double[ARRAY_SIZE];
		Float[] Float_array_dest = new Float[ARRAY_SIZE];
		WrappedDouble[] WDouble_array_dest = new WrappedDouble[ARRAY_SIZE];
		Arrays.fill(WDouble_array_dest, new WrappedDouble() );

		for ( int i = 0; i < ARRAY_SIZE; i++){
			double val = Math.random();
			double_array[i] = val;
			float_array[i] = (float)val;
			Double_array[i] = val;
			Float_array[i] = (float)val;			
			WDouble_array[i].value = val;
		}

		long time_i;
		long time_f;

		time_i = System.nanoTime();
		for ( int i = 0; i < ARRAY_SIZE; i++){
			double_array_dest[i] = double_array[i]*double_array[i];	
		}
		time_f = System.nanoTime();
		System.out.println("computing " + ARRAY_SIZE + " elements using double_array took " + (double)(time_f - time_i)*0.000001 + "ms" );


		time_i = System.nanoTime();
		for ( int i = 0; i < ARRAY_SIZE; i++){
			float_array_dest[i] = float_array[i]*float_array[i];	
		}
		time_f = System.nanoTime();
		System.out.println("computing " + ARRAY_SIZE + " elements using float_array took " + (double)(time_f - time_i)*0.000001 + "ms" );

	
		time_i = System.nanoTime();
		for ( int i = 0; i < ARRAY_SIZE; i++){
			Double_array_dest[i] = Double_array[i]*Double_array[i];	
		}
		time_f = System.nanoTime();
		System.out.println("computing " + ARRAY_SIZE + " elements using Double_array took " + (double)(time_f - time_i)*0.000001 + "ms" );


		time_i = System.nanoTime();
		for ( int i = 0; i < ARRAY_SIZE; i++){
			Float_array_dest[i] = Float_array[i]*Float_array[i];	
		}
		time_f = System.nanoTime();
		System.out.println("computing " + ARRAY_SIZE + " elements using Float_array took " + (double)(time_f - time_i)*0.000001 + "ms" );

		time_i = System.nanoTime();
		for ( int i = 0; i < ARRAY_SIZE; i++){
			WDouble_array_dest[i].value = WDouble_array[i].value*WDouble_array[i].value;	
		}
		time_f = System.nanoTime();
		System.out.println("computing " + ARRAY_SIZE + " elements using WDouble_array took " + (double)(time_f - time_i)*0.000001 + "ms" );


	}
}
