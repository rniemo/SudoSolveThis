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
		double sum = 0;
		for(int x = -halfLen; x <= halfLen; x++){
			for(int y = -halfLen; y <= halfLen; y++){
				gauss[x + halfLen][y + halfLen] = 
						-divConst * x * Math.exp(-(x * x + y * y) / (twoSigmaSq));
				sum += gauss[x + halfLen][y + halfLen];
			}
		}
		for(int x = 0; x < gauss.length; x++){
			for(int y = 0; y < gauss.length; y++){
				gauss[x][y] /= sum;
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
	
	public static double[][] filter(double[][] img, double[][] filter){
		double[][] filtered = new double[img.length][img[0].length];
		int halfFiltLength = filter.length / 2;
		for(int row = 0; row < filtered.length; row++){
			for(int col = 0; col < filtered[0].length; col++){
				double filteredValue = 0;
				for(int frow = 0; frow < filter.length; frow++){
					for(int fcol = 0; fcol < filter[0].length; fcol++){
						int rowIndex = Math.abs(row - halfFiltLength + frow);
						int colIndex = Math.abs(col - halfFiltLength + fcol);
						if(rowIndex >= filtered.length){
							rowIndex -= frow;
						}
						if(colIndex >= filtered[0].length){
							colIndex -= fcol;
						}
						filteredValue += img[rowIndex][colIndex] * filter[frow][fcol];
					}
				}
				filtered[row][col] = filteredValue;
			}
		}
		return filtered;
	}

}
