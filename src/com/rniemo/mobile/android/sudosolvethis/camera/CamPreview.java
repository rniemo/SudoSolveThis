package com.rniemo.mobile.android.sudosolvethis.camera;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.rniemo.mobile.android.sudosolvethis.DrawListener;

public class CamPreview extends SurfaceView implements SurfaceHolder.Callback, Camera.PreviewCallback{
	
	private final static String LOG_TAG = CamPreview.class.getSimpleName();
	
	private Camera cam;
	private SurfaceHolder sholder;
	private Size previewSize;
	private int bitsPerPixel;
	private List<CameraListener> camListeners = new ArrayList<CameraListener>();
	private List<DrawListener> drawListeners = new ArrayList<DrawListener>();
	
	public CamPreview(Context context) {
		super(context);
		init();
	}
	
	public CamPreview(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public void init(){

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
		setWillNotDraw(false);
    	sholder = getHolder();
    	sholder.addCallback(this);
	}
	
	public boolean addCameraListener(CameraListener listener){
		return camListeners.add(listener);
	}
	
	public boolean removeCameraListener(CameraListener listener){
		return camListeners.remove(listener);
	}	
	
	public boolean addDrawListener(DrawListener listener){
		return drawListeners.add(listener);
	}
	
	public boolean removeDrawListener(DrawListener listener){
		return drawListeners.remove(listener);
	}
	
	public int getPreviewWidth(){
		return previewSize.width;
	}
	
	public int getPreviewHeight(){
		return previewSize.height;
	}
	
	public int getBitsPerPixel(){
		return bitsPerPixel;
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
        
        cam = camera;
        
        if (cam != null) {
          
            try {
            	cam.setPreviewDisplay(sholder);
                cam.setPreviewCallback(this);
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

    	//Bitmap.createBitmap(pixels, width, height, Bitmap.Config.ARGB_8888);
    	Size prevSize = camera.getParameters().getPreviewSize();
    	// grayscale image
    	byte[] img = Arrays.copyOfRange(data, 0, prevSize.width * prevSize.height);
		for(CameraListener listener : camListeners){
			listener.onFrameReceived(img, prevSize.width, prevSize.height);
		}
		invalidate();
		
	}
    
//    public int[] yuvToGrayscale(byte[] yuv, int width, int height) {
//
//        int[] img = new int[width * height];
//
//        for (int i = 0; i < width * height; i++) {
//            int gray = yuv[i];
//            img[i] = 0xFF000000 | (gray * 0x00010101);
//        }
//
//        return img;
//
//      }
    
    @Override
    public void onDraw(Canvas canvas){
    	super.onDraw(canvas);
    	for(DrawListener listener : drawListeners){
    		listener.draw(canvas);
    	}
    }

	@Override
	public void surfaceChanged(SurfaceHolder holder, int arg1, int arg2, int arg3) {
		// Now that the size is known, set up the camera parameters and begin
	    // the preview.
		sholder = holder;
	    Camera.Parameters parameters = cam.getParameters();
	    parameters.setPreviewSize(previewSize.width, previewSize.height);
	    requestLayout();
	    cam.setParameters(parameters);

	    // Important: Call startPreview() to start updating the preview surface.
	    // Preview must be started before you can take a picture.
	    cam.startPreview();
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Log.e(LOG_TAG, "surface created");
		sholder = holder;
		try {
			boolean open = safeCameraOpen(Camera.CameraInfo.CAMERA_FACING_BACK);
			if(open){
	            previewSize = cam.getParameters().getPreviewSize();
	            cam.setDisplayOrientation(90);
	            requestLayout();
        		cam.setPreviewDisplay(sholder);
            	cam.setPreviewCallback(this);
			}
        } catch (IOException e) {
            e.printStackTrace();
        }
		
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
