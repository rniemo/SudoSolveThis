package com.rniemo.mobile.android.sudosolvethis;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.rniemo.mobile.android.sudosolvethis.camera.CamPreview;
import com.rniemo.mobile.android.sudosolvethis.camera.CameraListener;
import com.rniemo.mobile.android.sudosolvethis.cv.CornerDetector;
import com.rniemo.mobile.android.sudosolvethis.cv.FilterUtils;
import com.rniemo.mobile.android.sudosolvethis.cv.MatrixUtils;
import com.rniemo.mobile.android.sudosolvethis.cv.CornerDetector.CornerDetectionMethod;


public class MainActivity extends Activity implements CameraListener, DrawListener  {
	
	private final static String LOG_TAG = MainActivity.class.getSimpleName();
	//	Percent of image used as margin (e.g 10%)
	private final static int MARGIN_FACTOR = 10;
	//	Percent of sudoku box's width used as search area (e.g. 10% x 10% square
	private final static int SEARCH_AREA = 10;
	
	private CamPreview camPreview;
	private CornerDetector cornerDetector;
	private Bitmap bitmaptl, bitmaptr, bitmapbl, bitmapbr, bitmaptest, bitmapfull;
	private double[][] filtx;
	private double[][] filty;
	private Paint p = new Paint();
	private int sudokuBoxSize = 0;
	private int dataWidth;
	
	

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
        filtx = FilterUtils.getGaussianDeriv(1.0);
        filty = MatrixUtils.transpose(filtx);
    }
    
    
    public void onFrameReceived(byte[] data, int width, int height){
//    	double[][] img = new double[height][width];
//    	for(int row = 0; row < height; row++){
//    		for(int col = 0; col < width; col++){
//    			img[row][col] = data[row * width + col] / 255.0;
//    		}
//    	}
    	dataWidth = width;
    	if(sudokuBoxSize == 0){
    		
    	}
    	
    	int marginx = width / MARGIN_FACTOR;
    	int marginy = height / MARGIN_FACTOR;
    	
    	double[][] test = getSubimage(data, width, height, 50, 50, 0, 0);
    	double[][] full = new double[height][width];
    	for(int row = 0; row < height; row++){
    		for(int col = 0; col < width; col++){
    			full[row][col] = data[row * width + col] / 255.0;
    		}
    	}
    	full = getSubimage(data, width, height, width, height, 0, 0);
    	
		double[][] topLeft = getSubimage(data, width, height, 50, 50, marginy, marginx);
		double[][] topRight = getSubimage(data, width, height, 50, 50, marginy, width - marginx - 50);
		double[][] bottomLeft = getSubimage(data, width, height, 50, 50, height - marginy - 50, marginx);
		double[][] bottomRight = getSubimage(data, width, height, 50, 50, height - marginy - 50, width - marginx - 50);
		
		double[][] topLeftx = FilterUtils.filter(topLeft, filtx);
		double[][] topRightx = FilterUtils.filter(topRight, filtx);
		double[][] bottomLeftx = FilterUtils.filter(bottomLeft, filtx);
		double[][] bottomRightx = FilterUtils.filter(bottomRight, filtx);  
		
		double[][] topLefty = FilterUtils.filter(topLeft, filty);
		double[][] topRighty = FilterUtils.filter(topRight, filty);
		double[][] bottomLefty = FilterUtils.filter(bottomLeft, filty);
		double[][] bottomRighty = FilterUtils.filter(bottomRight, filty);
		
    	p.setColor(Color.rgb((int) (Math.random() * 255),
    			(int) (Math.random() * 255), (int) (Math.random() * 255)));
    	bitmaptl = toBitmap(topLeft);
    	bitmaptr = toBitmap(topRight);
    	bitmapbl = toBitmap(bottomLeft);
    	bitmapbr = toBitmap(bottomRight);
    	bitmaptest = toBitmap(test);
    	//bitmapfull = toBitmap(full);
    	
	}
    
    public Bitmap toBitmap(double[][] img){
    	img = MatrixUtils.transpose(img);
    	int width = img[0].length;
    	int height = img.length;
    	int[] pixels = new int[height * width];
    	for(int row = 0; row < height; row++){
    		for(int col = 0; col < width; col++){
    			pixels[row * width + col] = 0xFF000000 |
    						((int) (img[row][col] * 255.0) * 0x00010101);
    		}
    	}
    	return Bitmap.createBitmap(pixels, width, height, Bitmap.Config.ARGB_8888);
    }
    
    public double[][] getSubimage(byte[] data,
    				int dataWidth, int dataHeight,
    				int imgWidth, int imgHeight,
    				int rowOffset, int colOffset){
    	double[][] img = new double[imgHeight][imgWidth];
    	for(int row = 0; row < imgHeight; row++){
    		for(int col = 0; col < imgWidth; col++){
    			img[row][col] = data[data.length - dataWidth - rowOffset * dataWidth 
    			                     + colOffset - row * dataWidth + col] / 255.0;
    		}
    	}
    	return img;
    }
    
    public void draw(Canvas canvas){
    	
    	double scaleFactor = 1;
    	if(dataWidth != 0){
    		scaleFactor = (double) dataWidth / canvas.getWidth();
    	}
    	int marginx = canvas.getWidth() / MARGIN_FACTOR;
    	int sudokuArea = canvas.getWidth() - 2 * marginx;
    	int marginy = (canvas.getHeight() - sudokuArea) / 2;
    	int boxSize = canvas.getWidth() / SEARCH_AREA;
    	canvas.drawRect(marginx, marginy, boxSize + marginx, boxSize + marginy, p);
    	canvas.drawRect(canvas.getWidth() - marginx - boxSize, marginy, 
    			canvas.getWidth() - marginx, boxSize + marginy, p);
    	canvas.drawRect(marginx, canvas.getHeight() - marginy, boxSize + marginx, 
    			boxSize + canvas.getHeight() - marginy, p);
    	canvas.drawRect(canvas.getWidth() - marginx - boxSize, canvas.getHeight() - marginy, 
    			canvas.getWidth() - marginx, boxSize + canvas.getHeight() - marginy, p);
    	if(bitmaptl != null){
    		bitmaptl = Bitmap.createScaledBitmap(bitmaptl, (int) (bitmaptl.getWidth() * scaleFactor), (int) (bitmaptl.getHeight() * scaleFactor), true);
    		canvas.drawBitmap(bitmaptl, marginx, marginy, new Paint());
    	}
    	if(bitmaptr != null){
    		bitmaptr = Bitmap.createScaledBitmap(bitmaptr, (int) (bitmaptr.getWidth() * scaleFactor), (int) (bitmaptr.getHeight() * scaleFactor), true);
    		canvas.drawBitmap(bitmaptr, marginx, canvas.getHeight() - marginy, new Paint());
    	}
    	if(bitmapbl != null){
    		bitmapbl = Bitmap.createScaledBitmap(bitmapbl, (int) (bitmapbl.getWidth() * scaleFactor), (int) (bitmapbl.getHeight() * scaleFactor), true);
    		canvas.drawBitmap(bitmapbl, canvas.getWidth() - marginx - boxSize, marginy, new Paint());
    	}
    	if(bitmapbr != null){
    		bitmapbr = Bitmap.createScaledBitmap(bitmapbr, (int) (bitmapbr.getWidth() * scaleFactor), (int) (bitmapbr.getHeight() * scaleFactor), true);
    		canvas.drawBitmap(bitmapbr, canvas.getWidth() - marginx - boxSize, canvas.getHeight() - marginy, new Paint());
    	}
    	if(bitmaptest != null){
    		bitmaptest = Bitmap.createScaledBitmap(bitmaptest, (int) (bitmaptest.getWidth() * scaleFactor), (int) (bitmaptest.getHeight() * scaleFactor), true);
    		canvas.drawBitmap(bitmaptest, 0, 0, new Paint());
    	}
    	if(bitmapfull != null){
    		canvas.drawBitmap(bitmapfull, 0, 0, new Paint());
    	}
    }
    
}
