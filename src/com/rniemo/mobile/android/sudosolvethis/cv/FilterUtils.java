package com.rniemo.mobile.android.sudosolvethis.cv;

public class FilterUtils {
	
	// Gives 2D Gaussian derivative filter in the y direction
	// transpose matrix for x direction derivative
	public static double[][] getGaussianDeriv(double sigma){
		int size = (int) Math.ceil(3 * sigma) * 2 + 1;
		double[][] gauss = new double[size][size];
		
		int halfLen = gauss.length / 2;
		double twoSigmaSq = 2 * sigma * sigma;
		double divConst = 1.0 / (Math.PI * twoSigmaSq * sigma * sigma); 
		for(int x = -halfLen; x <= halfLen; x++){
			for(int y = -halfLen; y <= halfLen; y++){
				gauss[x + halfLen][y + halfLen] = 
						-divConst * x * Math.exp(-(x * x + y * y) / (twoSigmaSq));
			}
		} 
		return gauss;
	}
	
	
	public static double[][] getGaussian(double sigma){
		int size = (int) Math.ceil(3 * sigma) * 2 + 1;
		double[][] gauss = new double[size][size];
		
		
		int halfLen = gauss.length / 2;
		double twoSigmaSq = 2 * sigma * sigma;
		double divConst = 1.0 / (Math.PI * twoSigmaSq); 
		for(int x = -halfLen; x <= halfLen; x++){
			for(int y = -halfLen; y <= halfLen; y++){
				gauss[x + halfLen][y + halfLen] = 
						divConst * Math.exp(-(x * x + y * y) / (twoSigmaSq));
			}
		}
		return gauss;
	}

}
