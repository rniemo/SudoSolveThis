package com.rniemo.mobile.android.sudosolvethis.camera;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;

public class CamPreview extends ViewGroup implements SurfaceHolder.Callback, Camera.PreviewCallback{
	
	private final static String LOG_TAG = CamPreview.class.getSimpleName();
	
	private Camera cam;
	private SurfaceView sview;
	private SurfaceHolder sholder;
	private Size previewSize;
	private List<CameraListener> camListeners = new ArrayList<CameraListener>();
	
	
	public CamPreview(Context context) {
		super(context);
		sview = new SurfaceView(context);
        addView(sview);

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        sholder = sview.getHolder();
        sholder.addCallback(this);
	}
	
	public boolean addCameraListener(CameraListener listener){
		return camListeners.add(listener);
	}
	
	public boolean removeCameraListener(CameraListener listener){
		return camListeners.remove(listener);
	}
	
	public boolean safeCameraOpen(int id) {
        boolean qOpened = false;
      
        try {
            releaseCameraAndPreview();
            cam = Camera.open(id);
            qOpened = cam != null;
        } catch (Exception e) {
            Log.e(LOG_TAG, "Failed to open Camera");
            e.printStackTrace();
        }
        
        if(qOpened){
        	setCamera(cam);
        }
        
        return qOpened;    
    }
    
    private void releaseCameraAndPreview() {
        setCamera(null);
        if (cam != null) {
            cam.release();
            cam = null;
        }
    }
    
    public void setCamera(Camera camera) {
        if (cam == camera) { return; }
        
        stopPreviewAndFreeCamera();
        
        cam = camera;
        
        if (cam != null) {
            List<Size> localSizes = cam.getParameters().getSupportedPreviewSizes();
            previewSize = localSizes.get(0);
            requestLayout();
          
            try {
            	cam.setPreviewDisplay(sholder);
            } catch (IOException e) {
                e.printStackTrace();
            }
          
            // Important: Call startPreview() to start updating the preview
            // surface. Preview must be started before you can take a picture.
            cam.startPreview();
        }
    }

    /**
     * When this function returns, cam will be null.
     */
    private void stopPreviewAndFreeCamera() {

        if (cam != null) {
            // Call stopPreview() to stop updating the preview surface.
        	cam.stopPreview();
        
            // Important: Call release() to release the camera for use by other
            // applications. Applications should release the camera immediately
            // during onPause() and re-open() it during onResume()).
        	cam.release();
        	cam = null;
        }
    }
    
    @Override
	public void onPreviewFrame(byte[] data, Camera camera) {
		for(CameraListener listener : camListeners){
			listener.onFrameReceived(data);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// Now that the size is known, set up the camera parameters and begin
	    // the preview.
	    Camera.Parameters parameters = cam.getParameters();
	    parameters.setPreviewSize(previewSize.width, previewSize.height);
	    requestLayout();
	    cam.setParameters(parameters);

	    // Important: Call startPreview() to start updating the preview surface.
	    // Preview must be started before you can take a picture.
	    cam.startPreview();
	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		if (cam != null) {
            // Call stopPreview() to stop updating the preview surface.
            cam.stopPreview();
        }
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		
	}

}
