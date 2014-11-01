package com.rniemo.mobile.android.sudosolvethis;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

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
        LayoutInflater inflater = getLayoutInflater();
        RelativeLayout contentView = (RelativeLayout) inflater.inflate(R.layout.activity_main, null);
        setContentView(contentView);
        camPreview = (CamPreview) contentView.findViewById(R.id.cam_preview);
        camPreview.addCameraListener(this);
        hdetector = new HarrisDetector();
    }
    
    public void onFrameReceived(byte[] data){
//	    Bitmap map = BitmapFactory.decodeByteArray(data, 0, data.length);
//	    int[] pixels = new int[50];
//	    map.setPixels(pixels, 0, camPreview.getPreviewWidth(), 0, 0, 50, 50);
//	    map.compress(format, quality, stream)
	}
    
}
