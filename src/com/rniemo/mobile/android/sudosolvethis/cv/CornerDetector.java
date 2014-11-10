package com.rniemo.mobile.android.sudosolvethis.cv;

import java.util.ArrayList;
import java.util.List;


public class CornerDetector{
	
	public enum CornerDetectionMethod {
		HARRIS,
		SHI_TOMASI,
		FAST
	}
	
	private boolean nonMaxSuppression;
	private double gaussWindowSigma;
	private double gaussDerivativeSigma;
	private CornerDetectionMethod method;
	
	private double[][] gaussSmooth;
	private double[][] gaussXDeriv;
	private double[][] gaussYDeriv;
			
			
	private CornerDetector(CornerDetectorBuilder builder){
		this.nonMaxSuppression = builder.nonMaxSuppression;
		this.gaussWindowSigma = builder.gaussWindowSigma;
		this.gaussDerivativeSigma = builder.gaussDerivativeSigma;
		this.method = builder.method;
		gaussSmooth = FilterUtils.getGaussian(gaussWindowSigma);
		gaussYDeriv = FilterUtils.getGaussianDeriv(gaussDerivativeSigma);
		gaussXDeriv = MatrixUtils.transpose(gaussYDeriv);
	}
	
	
	public List<Pixel> detectCorners(double[][] img){
		List<Pixel> corners = new ArrayList<Pixel>();
		if(method == CornerDetectionMethod.HARRIS || method == CornerDetectionMethod.SHI_TOMASI){
			double[][] xImg = FilterUtils.filter(img, gaussXDeriv);
			double[][] yImg = FilterUtils.filter(img, gaussYDeriv);
			double[][] xyImg = MatrixUtils.mult(xImg, yImg);
			// gaussian blur xImg, yImg, xyImg
			double[][] R = new double[img.length][img[0].length];
			double[][] M = new double[2][2];
			for(int row = 0; row < R.length; row++){
				for(int col = 0; col < R[0].length; col++){
					M[0][0] = Math.pow(xImg[row][col], 2);
					M[0][1] = xyImg[row][col];
					M[1][0] = M[0][1];
					M[1][1] = Math.pow(yImg[row][col], 2);
					if(method == CornerDetectionMethod.HARRIS){
						R[row][col] = harrisCornerness(M);
					}else{
						R[row][col] = shiTomasiCornerness(M);
					}
				}
			}
			
		}else if(method == CornerDetectionMethod.FAST){
			// TODO: implement FAST :)
		}
		
		
		return corners;
	}
	
	private double harrisCornerness(double[][] M){
		double x = M[0][0];
		double xy = M[1][0];
		double y = M[1][1];
		// Determinant - alpha * (trace)^2
		// if two eigenvalues are strong, result is nonzero
		// if 1-2 eigenvalues are small, determinant ~= 0, result is ~zero
		return x * y - xy * xy - .05 * (x + y) * (x + y);
	}
	
	private double shiTomasiCornerness(double[][] M){
		double x = M[0][0];
		double xy = M[1][0];
		double y = M[1][1];
		// compute smallest eigenvalue
		return ((x + y) - Math.sqrt((x - y) * (x - y) + 4 * xy * xy)) / 2;
	}
	
	public static class CornerDetectorBuilder{
		
		private boolean nonMaxSuppression;
		private double gaussWindowSigma;
		private double gaussDerivativeSigma;
		private CornerDetectionMethod method;
		
		public CornerDetectorBuilder(){
			nonMaxSuppression = true;
			gaussWindowSigma = 1;
			gaussDerivativeSigma = 1;
			method = CornerDetectionMethod.HARRIS;
		}
		
		public CornerDetector build(){
			return new CornerDetector(this);
		}
		
		public CornerDetectorBuilder nonMaxSuppression(boolean suppression){
			this.nonMaxSuppression = suppression;
			return this;
		}
		
		public CornerDetectorBuilder gaussWindowSigma(double windowSigma){
			this.gaussWindowSigma = windowSigma;
			return this;
		}
		
		public CornerDetectorBuilder gaussDerivativeSigma(double derivSigma){
			this.gaussDerivativeSigma = derivSigma;
			return this;
		}
		
		public CornerDetectorBuilder detectionMethod(CornerDetectionMethod method){
			this.method = method;
			return this;
		}
		
	}
	
}
