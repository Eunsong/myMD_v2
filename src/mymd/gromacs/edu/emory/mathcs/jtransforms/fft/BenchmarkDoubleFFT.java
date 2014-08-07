/* ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * The Original Code is JTransforms.
 *
 * The Initial Developer of the Original Code is
 * Piotr Wendykier, Emory University.
 * Portions created by the Initial Developer are Copyright (C) 2007-2009
 * the Initial Developer. All Rights Reserved.
 *
 * Alternatively, the contents of this file may be used under the terms of
 * either the GNU General Public License Version 2 or later (the "GPL"), or
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"),
 * in which case the provisions of the GPL or the LGPL are applicable instead
 * of those above. If you wish to allow use of your version of this file only
 * under the terms of either the GPL or the LGPL, and not to allow others to
 * use your version of this file under the terms of the MPL, indicate your
 * decision by deleting the provisions above and replace them with the notice
 * and other provisions required by the GPL or the LGPL. If you do not delete
 * the provisions above, a recipient may use your version of this file under
 * the terms of any one of the MPL, the GPL or the LGPL.
 *
 * ***** END LICENSE BLOCK ***** */

package edu.emory.mathcs.jtransforms.fft;

import java.util.Arrays;

import edu.emory.mathcs.utils.ConcurrencyUtils;
import edu.emory.mathcs.utils.IOUtils;

/**
 * Benchmark of double precision FFT's
 * 
 * @author Piotr Wendykier (piotr.wendykier@gmail.com)
 * 
 */
public class BenchmarkDoubleFFT {

    private static int nthread = 8;

    private static int niter = 200;

    private static int nsize = 16;

    private static int threadsBegin2D = 65636;

    private static int threadsBegin3D = 65636;

    private static boolean doWarmup = true;

    private static int[] sizes1D = new int[] { 65536, 131072, 262144, 524288, 1048576, 2097152, 4194304, 8388608, 10368, 27000, 75600, 165375, 362880, 1562500, 3211264, 6250000 };

    private static int[] sizes2D = new int[] { 128, 256, 512, 1024, 2048, 4096, 8192, 16384, 260, 520, 1050, 1458, 1960, 2916, 4116, 5832 };

    private static int[] sizes3D = new int[] { 8, 16, 32, 64, 128, 256, 512, 1024, 5, 17, 30, 95, 180, 270, 324, 420 };

    private static boolean doScaling = false;

    private BenchmarkDoubleFFT() {

    }

    public static void parseArguments(String[] args) {
        if (args.length > 0) {
            nthread = Integer.parseInt(args[0]);
            threadsBegin2D = Integer.parseInt(args[1]);
            threadsBegin3D = Integer.parseInt(args[2]);
            niter = Integer.parseInt(args[3]);
            doWarmup = Boolean.parseBoolean(args[4]);
            doScaling = Boolean.parseBoolean(args[5]);
            nsize = Integer.parseInt(args[6]);
            sizes1D = new int[nsize];
            sizes2D = new int[nsize];
            sizes3D = new int[nsize];
            for (int i = 0; i < nsize; i++) {
                sizes1D[i] = Integer.parseInt(args[7 + i]);
            }
            for (int i = 0; i < nsize; i++) {
                sizes2D[i] = Integer.parseInt(args[7 + nsize + i]);
            }
            for (int i = 0; i < nsize; i++) {
                sizes3D[i] = Integer.parseInt(args[7 + nsize + nsize + i]);
            }
        } else {
            System.out.println("Default settings are used.");
        }
        ConcurrencyUtils.setNumberOfThreads(nthread);
        ConcurrencyUtils.setThreadsBeginN_2D(threadsBegin2D);
        ConcurrencyUtils.setThreadsBeginN_3D(threadsBegin3D);
        System.out.println("nthred = " + nthread);
        System.out.println("threadsBegin2D = " + threadsBegin2D);
        System.out.println("threadsBegin3D = " + threadsBegin3D);
        System.out.println("niter = " + niter);
        System.out.println("doWarmup = " + doWarmup);
        System.out.println("doScaling = " + doScaling);
        System.out.println("nsize = " + nsize);
        System.out.println("sizes1D[] = " + Arrays.toString(sizes1D));
        System.out.println("sizes2D[] = " + Arrays.toString(sizes2D));
        System.out.println("sizes3D[] = " + Arrays.toString(sizes3D));
    }

    public static void benchmarkComplexForward_1D() {
        double[] x;
        double[] times = new double[nsize];
        for (int i = 0; i < nsize; i++) {
            System.out.println("Complex forward FFT 1D of size " + sizes1D[i]);
            DoubleFFT_1D fft = new DoubleFFT_1D(sizes1D[i]);
            x = new double[2 * sizes1D[i]];
            if (doWarmup) { // call the transform twice to warm up
                IOUtils.fillMatrix_1D(2 * sizes1D[i], x);
                fft.complexForward(x);
                IOUtils.fillMatrix_1D(2 * sizes1D[i], x);
                fft.complexForward(x);
            }
            double av_time = 0;
            long elapsedTime = 0;
            for (int j = 0; j < niter; j++) {
                IOUtils.fillMatrix_1D(2 * sizes1D[i], x);
                elapsedTime = System.nanoTime();
                fft.complexForward(x);
                elapsedTime = System.nanoTime() - elapsedTime;
                av_time = av_time + elapsedTime;
            }
            times[i] = (double) av_time / 1000000.0 / (double) niter;
            System.out.println("\tAverage execution time: " + String.format("%.2f", av_time / 1000000.0 / (double) niter) + " msec");
            x = null;
            fft = null;
            System.gc();
            ConcurrencyUtils.sleep(5000);
        }
        IOUtils.writeFFTBenchmarkResultsToFile("benchmarkDoubleComplexForwardFFT_1D.txt", nthread, niter, doWarmup, doScaling, sizes1D, times);

    }

    public static void benchmarkRealForward_1D() {
        double[] times = new double[nsize];
        double[] x;
        for (int i = 0; i < nsize; i++) {
            System.out.println("Real forward FFT 1D of size " + sizes1D[i]);
            DoubleFFT_1D fft = new DoubleFFT_1D(sizes1D[i]);
            x = new double[2 * sizes1D[i]];
            if (doWarmup) { // call the transform twice to warm up
                IOUtils.fillMatrix_1D(sizes1D[i], x);
                fft.realForwardFull(x);
                IOUtils.fillMatrix_1D(sizes1D[i], x);
                fft.realForwardFull(x);
            }
            double av_time = 0;
            long elapsedTime = 0;
            for (int j = 0; j < niter; j++) {
                IOUtils.fillMatrix_1D(sizes1D[i], x);
                elapsedTime = System.nanoTime();
                fft.realForwardFull(x);
                elapsedTime = System.nanoTime() - elapsedTime;
                av_time = av_time + elapsedTime;
            }
            times[i] = (double) av_time / 1000000.0 / (double) niter;
            System.out.println("\tAverage execution time: " + String.format("%.2f", av_time / 1000000.0 / (double) niter) + " msec");
            x = null;
            fft = null;
            System.gc();
            ConcurrencyUtils.sleep(5000);
        }
        IOUtils.writeFFTBenchmarkResultsToFile("benchmarkDoubleRealForwardFFT_1D.txt", nthread, niter, doWarmup, doScaling, sizes1D, times);

    }

    public static void benchmarkComplexForward_2D_input_1D() {
        double[] times = new double[nsize];
        double[] x;
        for (int i = 0; i < nsize; i++) {
            System.out.println("Complex forward FFT 2D (input 1D) of size " + sizes2D[i] + " x " + sizes2D[i]);
            DoubleFFT_2D fft2 = new DoubleFFT_2D(sizes2D[i], sizes2D[i]);
            x = new double[sizes2D[i] * 2 * sizes2D[i]];
            if (doWarmup) { // call the transform twice to warm up
                IOUtils.fillMatrix_2D(sizes2D[i], 2 * sizes2D[i], x);
                fft2.complexForward(x);
                IOUtils.fillMatrix_2D(sizes2D[i], 2 * sizes2D[i], x);
                fft2.complexForward(x);
            }
            double av_time = 0;
            long elapsedTime = 0;
            for (int j = 0; j < niter; j++) {
                IOUtils.fillMatrix_2D(sizes2D[i], 2 * sizes2D[i], x);
                elapsedTime = System.nanoTime();
                fft2.complexForward(x);
                elapsedTime = System.nanoTime() - elapsedTime;
                av_time = av_time + elapsedTime;
            }
            times[i] = (double) av_time / 1000000.0 / (double) niter;
            System.out.println("\tAverage execution time: " + String.format("%.2f", av_time / 1000000.0 / (double) niter) + " msec");
            x = null;
            fft2 = null;
            System.gc();
            ConcurrencyUtils.sleep(5000);
        }
        IOUtils.writeFFTBenchmarkResultsToFile("benchmarkDoubleComplexForwardFFT_2D_input_1D.txt", nthread, niter, doWarmup, doScaling, sizes2D, times);
    }

    public static void benchmarkComplexForward_2D_input_2D() {
        double[] times = new double[nsize];
        double[][] x;
        for (int i = 0; i < nsize; i++) {
            System.out.println("Complex forward FFT 2D (input 2D) of size " + sizes2D[i] + " x " + sizes2D[i]);
            DoubleFFT_2D fft2 = new DoubleFFT_2D(sizes2D[i], sizes2D[i]);
            x = new double[sizes2D[i]][2 * sizes2D[i]];
            if (doWarmup) { // call the transform twice to warm up
                IOUtils.fillMatrix_2D(sizes2D[i], 2 * sizes2D[i], x);
                fft2.complexForward(x);
                IOUtils.fillMatrix_2D(sizes2D[i], 2 * sizes2D[i], x);
                fft2.complexForward(x);
            }
            double av_time = 0;
            long elapsedTime = 0;
            for (int j = 0; j < niter; j++) {
                IOUtils.fillMatrix_2D(sizes2D[i], 2 * sizes2D[i], x);
                elapsedTime = System.nanoTime();
                fft2.complexForward(x);
                elapsedTime = System.nanoTime() - elapsedTime;
                av_time = av_time + elapsedTime;
            }
            times[i] = (double) av_time / 1000000.0 / (double) niter;
            System.out.println("\tAverage execution time: " + String.format("%.2f", av_time / 1000000.0 / (double) niter) + " msec");
            x = null;
            fft2 = null;
            System.gc();
            ConcurrencyUtils.sleep(5000);
        }
        IOUtils.writeFFTBenchmarkResultsToFile("benchmarkDoubleComplexForwardFFT_2D_input_2D.txt", nthread, niter, doWarmup, doScaling, sizes2D, times);
    }

    public static void benchmarkRealForward_2D_input_1D() {
        double[] times = new double[nsize];
        double[] x;
        for (int i = 0; i < nsize; i++) {
            System.out.println("Real forward FFT 2D (input 1D) of size " + sizes2D[i] + " x " + sizes2D[i]);
            DoubleFFT_2D fft2 = new DoubleFFT_2D(sizes2D[i], sizes2D[i]);
            x = new double[sizes2D[i] * 2 * sizes2D[i]];
            if (doWarmup) { // call the transform twice to warm up
                IOUtils.fillMatrix_2D(sizes2D[i], sizes2D[i], x);
                fft2.realForwardFull(x);
                IOUtils.fillMatrix_2D(sizes2D[i], sizes2D[i], x);
                fft2.realForwardFull(x);
            }
            double av_time = 0;
            long elapsedTime = 0;
            for (int j = 0; j < niter; j++) {
                IOUtils.fillMatrix_2D(sizes2D[i], sizes2D[i], x);
                elapsedTime = System.nanoTime();
                fft2.realForwardFull(x);
                elapsedTime = System.nanoTime() - elapsedTime;
                av_time = av_time + elapsedTime;
            }
            times[i] = (double) av_time / 1000000.0 / (double) niter;
            System.out.println("\tAverage execution time: " + String.format("%.2f", av_time / 1000000.0 / (double) niter) + " msec");
            x = null;
            fft2 = null;
            System.gc();
            ConcurrencyUtils.sleep(5000);
        }
        IOUtils.writeFFTBenchmarkResultsToFile("benchmarkDoubleRealForwardFFT_2D_input_1D.txt", nthread, niter, doWarmup, doScaling, sizes2D, times);
    }

    public static void benchmarkRealForward_2D_input_2D() {
        double[] times = new double[nsize];
        double[][] x;
        for (int i = 0; i < nsize; i++) {
            System.out.println("Real forward FFT 2D (input 2D) of size " + sizes2D[i] + " x " + sizes2D[i]);
            DoubleFFT_2D fft2 = new DoubleFFT_2D(sizes2D[i], sizes2D[i]);
            x = new double[sizes2D[i]][2 * sizes2D[i]];
            if (doWarmup) { // call the transform twice to warm up
                IOUtils.fillMatrix_2D(sizes2D[i], sizes2D[i], x);
                fft2.realForwardFull(x);
                IOUtils.fillMatrix_2D(sizes2D[i], sizes2D[i], x);
                fft2.realForwardFull(x);
            }
            double av_time = 0;
            long elapsedTime = 0;
            for (int j = 0; j < niter; j++) {
                IOUtils.fillMatrix_2D(sizes2D[i], sizes2D[i], x);
                elapsedTime = System.nanoTime();
                fft2.realForwardFull(x);
                elapsedTime = System.nanoTime() - elapsedTime;
                av_time = av_time + elapsedTime;
            }
            times[i] = (double) av_time / 1000000.0 / (double) niter;
            System.out.println("\tAverage execution time: " + String.format("%.2f", av_time / 1000000.0 / (double) niter) + " msec");
            x = null;
            fft2 = null;
            System.gc();
            ConcurrencyUtils.sleep(5000);
        }
        IOUtils.writeFFTBenchmarkResultsToFile("benchmarkDoubleRealForwardFFT_2D_input_2D.txt", nthread, niter, doWarmup, doScaling, sizes2D, times);
    }

    public static void benchmarkComplexForward_3D_input_1D() {
        double[] times = new double[nsize];
        double[] x;
        for (int i = 0; i < nsize; i++) {
            if (sizes3D[i] < 1024) {
                System.out.println("Complex forward FFT 3D (input 1D) of size " + sizes3D[i] + " x " + sizes3D[i] + " x " + sizes3D[i]);
                DoubleFFT_3D fft3 = new DoubleFFT_3D(sizes3D[i], sizes3D[i], sizes3D[i]);
                x = new double[sizes3D[i] * sizes3D[i] * 2 * sizes3D[i]];
                if (doWarmup) { // call the transform twice to warm up
                    IOUtils.fillMatrix_3D(sizes3D[i], sizes3D[i], 2 * sizes3D[i], x);
                    fft3.complexForward(x);
                    IOUtils.fillMatrix_3D(sizes3D[i], sizes3D[i], 2 * sizes3D[i], x);
                    fft3.complexForward(x);
                }
                double av_time = 0;
                long elapsedTime = 0;
                for (int j = 0; j < niter; j++) {
                    IOUtils.fillMatrix_3D(sizes3D[i], sizes3D[i], 2 * sizes3D[i], x);
                    elapsedTime = System.nanoTime();
                    fft3.complexForward(x);
                    elapsedTime = System.nanoTime() - elapsedTime;
                    av_time = av_time + elapsedTime;
                }
                times[i] = (double) av_time / 1000000.0 / (double) niter;
                System.out.println("\tAverage execution time: " + String.format("%.2f", av_time / 1000000.0 / (double) niter) + " msec");
                x = null;
                fft3 = null;
                System.gc();
                ConcurrencyUtils.sleep(5000);
            }
        }
        IOUtils.writeFFTBenchmarkResultsToFile("benchmarkDoubleComplexForwardFFT_3D_input_1D.txt", nthread, niter, doWarmup, doScaling, sizes3D, times);
    }

    public static void benchmarkComplexForward_3D_input_3D() {
        double[] times = new double[nsize];
        double[][][] x;
        for (int i = 0; i < nsize; i++) {
            System.out.println("Complex forward FFT 3D (input 3D) of size " + sizes3D[i] + " x " + sizes3D[i] + " x " + sizes3D[i]);
            DoubleFFT_3D fft3 = new DoubleFFT_3D(sizes3D[i], sizes3D[i], sizes3D[i]);
            x = new double[sizes3D[i]][sizes3D[i]][2 * sizes3D[i]];
            if (doWarmup) { // call the transform twice to warm up
                IOUtils.fillMatrix_3D(sizes3D[i], sizes3D[i], 2 * sizes3D[i], x);
                fft3.complexForward(x);
                IOUtils.fillMatrix_3D(sizes3D[i], sizes3D[i], 2 * sizes3D[i], x);
                fft3.complexForward(x);
            }
            double av_time = 0;
            long elapsedTime = 0;
            for (int j = 0; j < niter; j++) {
                IOUtils.fillMatrix_3D(sizes3D[i], sizes3D[i], 2 * sizes3D[i], x);
                elapsedTime = System.nanoTime();
                fft3.complexForward(x);
                elapsedTime = System.nanoTime() - elapsedTime;
                av_time = av_time + elapsedTime;
            }
            times[i] = (double) av_time / 1000000.0 / (double) niter;
            System.out.println("\tAverage execution time: " + String.format("%.2f", av_time / 1000000.0 / (double) niter) + " msec");
            x = null;
            fft3 = null;
            System.gc();
            ConcurrencyUtils.sleep(5000);
        }
        IOUtils.writeFFTBenchmarkResultsToFile("benchmarkDoubleComplexForwardFFT_3D_input_3D.txt", nthread, niter, doWarmup, doScaling, sizes3D, times);
    }

    public static void benchmarkRealForward_3D_input_1D() {
        double[] times = new double[nsize];
        double[] x;
        for (int i = 0; i < nsize; i++) {
            if (sizes3D[i] < 1024) {
                System.out.println("Real forward FFT 3D (input 1D) of size " + sizes3D[i] + " x " + sizes3D[i] + " x " + sizes3D[i]);
                DoubleFFT_3D fft3 = new DoubleFFT_3D(sizes3D[i], sizes3D[i], sizes3D[i]);
                x = new double[sizes3D[i] * sizes3D[i] * 2 * sizes3D[i]];
                if (doWarmup) { // call the transform twice to warm up
                    IOUtils.fillMatrix_3D(sizes3D[i], sizes3D[i], sizes3D[i], x);
                    fft3.realForwardFull(x);
                    IOUtils.fillMatrix_3D(sizes3D[i], sizes3D[i], sizes3D[i], x);
                    fft3.realForwardFull(x);
                }
                double av_time = 0;
                long elapsedTime = 0;
                for (int j = 0; j < niter; j++) {
                    IOUtils.fillMatrix_3D(sizes3D[i], sizes3D[i], sizes3D[i], x);
                    elapsedTime = System.nanoTime();
                    fft3.realForwardFull(x);
                    elapsedTime = System.nanoTime() - elapsedTime;
                    av_time = av_time + elapsedTime;
                }
                times[i] = (double) av_time / 1000000.0 / (double) niter;
                System.out.println("\tAverage execution time: " + String.format("%.2f", av_time / 1000000.0 / (double) niter) + " msec");
                x = null;
                fft3 = null;
                System.gc();
                ConcurrencyUtils.sleep(5000);
            }
        }
        IOUtils.writeFFTBenchmarkResultsToFile("benchmarkDoubleRealForwardFFT_3D_input_1D.txt", nthread, niter, doWarmup, doScaling, sizes3D, times);
    }

    public static void benchmarkRealForward_3D_input_3D() {
        double[] times = new double[nsize];
        double[][][] x;
        for (int i = 0; i < nsize; i++) {
            System.out.println("Real forward FFT 3D (input 3D) of size " + sizes3D[i] + " x " + sizes3D[i] + " x " + sizes3D[i]);
            DoubleFFT_3D fft3 = new DoubleFFT_3D(sizes3D[i], sizes3D[i], sizes3D[i]);
            x = new double[sizes3D[i]][sizes3D[i]][2 * sizes3D[i]];
            if (doWarmup) { // call the transform twice to warm up
                IOUtils.fillMatrix_3D(sizes3D[i], sizes3D[i], sizes3D[i], x);
                fft3.realForwardFull(x);
                IOUtils.fillMatrix_3D(sizes3D[i], sizes3D[i], sizes3D[i], x);
                fft3.realForwardFull(x);
            }
            double av_time = 0;
            long elapsedTime = 0;
            for (int j = 0; j < niter; j++) {
                IOUtils.fillMatrix_3D(sizes3D[i], sizes3D[i], sizes3D[i], x);
                elapsedTime = System.nanoTime();
                fft3.realForwardFull(x);
                elapsedTime = System.nanoTime() - elapsedTime;
                av_time = av_time + elapsedTime;
            }
            times[i] = (double) av_time / 1000000.0 / (double) niter;
            System.out.println("\tAverage execution time: " + String.format("%.2f", av_time / 1000000.0 / (double) niter) + " msec");
            x = null;
            fft3 = null;
            System.gc();
            ConcurrencyUtils.sleep(5000);
        }
        IOUtils.writeFFTBenchmarkResultsToFile("benchmarkDoubleRealForwardFFT_3D_input_3D.txt", nthread, niter, doWarmup, doScaling, sizes3D, times);
    }

    public static void main(String[] args) {
        parseArguments(args);
        benchmarkComplexForward_1D();
        benchmarkRealForward_1D();
        benchmarkComplexForward_2D_input_1D();
        benchmarkComplexForward_2D_input_2D();
        benchmarkRealForward_2D_input_1D();
        benchmarkRealForward_2D_input_2D();
        benchmarkComplexForward_3D_input_1D();
        benchmarkComplexForward_3D_input_3D();
        benchmarkRealForward_3D_input_1D();
        benchmarkRealForward_3D_input_3D();
        System.exit(0);

    }
}
