package com.rniemo.mobile.android.sudosolvethis;

import android.app.Activity;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.rniemo.mobile.android.sudosolvethis.camera.CamPreview;
import com.rniemo.mobile.android.sudosolvethis.camera.CameraListener;
import com.rniemo.mobile.android.sudosolvethis.cv.CornerDetector;
import com.rniemo.mobile.android.sudosolvethis.cv.FilterUtils;
import com.rniemo.mobile.android.sudosolvethis.cv.CornerDetector.CornerDetectionMethod;


public class MainActivity extends Activity implements CameraListener, DrawListener  {
	
	private final static String LOG_TAG = MainActivity.class.getSimpleName();
	
	private CamPreview camPreview;
	private CornerDetector cornerDetector;
	
	

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = getLayoutInflater();
        RelativeLayout contentView = (RelativeLayout) inflater.inflate(R.layout.activity_main, null);
        setContentView(contentView);
        camPreview = (CamPreview) contentView.findViewById(R.id.cam_preview);
        camPreview.addCameraListener(this);
        camPreview.addDrawListener(this);
        CornerDetector cornerDetector = new CornerDetector.CornerDetectorBuilder()
        					.detectionMethod(CornerDetectionMethod.HARRIS)
        					.gaussDerivativeSigma(1.0)
        					.gaussWindowSigma(.7)
        					.nonMaxSuppression(true)
        					.build();
    }
    
    public void onFrameReceived(byte[] data, int width, int height){
    	// process frame
	}
    
    public void draw(Canvas canvas){
    	// draw things
    }
    
}
