package com.rniemo.mobile.android.sudosolvethis;

import android.app.Activity;
import android.hardware.Camera;
import android.os.Bundle;

import com.rniemo.mobile.android.sudosolvethis.camera.CamPreview;
import com.rniemo.mobile.android.sudosolvethis.camera.CameraListener;
import com.rniemo.mobile.android.sudosolvethis.cv.HarrisDetector;


public class MainActivity extends Activity implements CameraListener  {
	
	private final static String LOG_TAG = MainActivity.class.getSimpleName();
	
	private CamPreview camPreview;
	private HarrisDetector hdetector;
	

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        camPreview = (CamPreview) this.getLayoutInflater().inflate(R.id.cam_preview, null);
        camPreview.safeCameraOpen(Camera.CameraInfo.CAMERA_FACING_BACK);
        hdetector = new HarrisDetector();
    }
    
    public void onFrameReceived(byte[] data){
		
	}
    
    

}
