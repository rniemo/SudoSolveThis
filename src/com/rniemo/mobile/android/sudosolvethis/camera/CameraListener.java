package com.rniemo.mobile.android.sudosolvethis.camera;

public interface CameraListener {

	public void onFrameReceived(byte[] data, int width, int height);
	
}
