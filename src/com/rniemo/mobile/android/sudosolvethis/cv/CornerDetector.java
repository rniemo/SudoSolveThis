package com.rniemo.mobile.android.sudosolvethis.cv;

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
	
	public List<Pixel> detectCorners(byte[] img){
		
		if(method == CornerDetectionMethod.HARRIS || method == CornerDetectionMethod.SHI_TOMASI){
			
		}else if(method == CornerDetectionMethod.FAST){
			// TODO: implement FAST :)
		}
		
		
		return null;
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
