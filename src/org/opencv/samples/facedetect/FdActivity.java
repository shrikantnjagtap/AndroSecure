package org.opencv.samples.facedetect;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.samples.facedetect.FaceEntity;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.highgui.*;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

public class FdActivity extends Activity implements CvCameraViewListener2 {

	public final static String EXTRA_MESSAGE = "org.opencv.samples.facedetect.FdActivity.MESSAGE";

	private static final String    TAG                 = "OCVSample::Activity";
	private static final Scalar    FACE_RECT_COLOR     = new Scalar(0, 255, 0, 255);
	public static final int        JAVA_DETECTOR       = 0;
	public static final int        NATIVE_DETECTOR     = 1;

	private MenuItem               mItemFace50;
	private MenuItem               mItemFace40;
	private MenuItem               mItemFace30;
	private MenuItem               mItemFace20;
	private MenuItem               mItemType;

	private Mat                    mRgba;
	private Mat                    mGray;
	private File                   mCascadeFile;
	private CascadeClassifier      mJavaDetector;
	private DetectionBasedTracker  mNativeDetector;

	private int                    mDetectorType       = JAVA_DETECTOR;
	private String[]               mDetectorName;

	private float                  mRelativeFaceSize   = 0.2f;
	private int                    mAbsoluteFaceSize   = 0;

	private CameraBridgeViewBase   mOpenCvCameraView;

	private BaseLoaderCallback  mLoaderCallback = new BaseLoaderCallback(this) {
		@Override
		public void onManagerConnected(int status) {
			switch (status) {
			case LoaderCallbackInterface.SUCCESS:
			{
				Log.i(TAG, "OpenCV loaded successfully");

				// Load native library after(!) OpenCV initialization
				System.loadLibrary("detection_based_tracker");

				try {
					// load cascade file from application resources
					InputStream is = getResources().openRawResource(R.raw.lbpcascade_frontalface);
					File cascadeDir = getDir("cascade", Context.MODE_PRIVATE);
					mCascadeFile = new File(cascadeDir, "lbpcascade_frontalface.xml");
					FileOutputStream os = new FileOutputStream(mCascadeFile);

					byte[] buffer = new byte[4096];
					int bytesRead;
					while ((bytesRead = is.read(buffer)) != -1) {
						os.write(buffer, 0, bytesRead);
					}
					is.close();
					os.close();

					mJavaDetector = new CascadeClassifier(mCascadeFile.getAbsolutePath());
					if (mJavaDetector.empty()) {
						Log.e(TAG, "Failed to load cascade classifier");
						mJavaDetector = null;
					} else
						Log.i(TAG, "Loaded cascade classifier from " + mCascadeFile.getAbsolutePath());

					mNativeDetector = new DetectionBasedTracker(mCascadeFile.getAbsolutePath(), 0);

					cascadeDir.delete();

				} catch (IOException e) {
					e.printStackTrace();
					Log.e(TAG, "Failed to load cascade. Exception thrown: " + e);
				}

				mOpenCvCameraView.enableView();
			} break;
			default:
			{
				super.onManagerConnected(status);
			} break;
			}
		}
	};

	public FdActivity() {
		mDetectorName = new String[2];
		mDetectorName[JAVA_DETECTOR] = "Java";
		mDetectorName[NATIVE_DETECTOR] = "Native (tracking)";

		Log.i(TAG, "Instantiated new " + this.getClass());
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "called onCreate");
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		setContentView(R.layout.face_detect_surface_view);

		mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.fd_activity_surface_view);
		mOpenCvCameraView.setCvCameraViewListener(this);
	}

	@Override
	public void onPause()
	{
		super.onPause();
		if (mOpenCvCameraView != null)
			mOpenCvCameraView.disableView();
	}

	@Override
	public void onResume()
	{
		super.onResume();
		OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_3, this, mLoaderCallback);
	}



	public void onDestroy() {
		super.onDestroy();
		mOpenCvCameraView.disableView();
	}

	public void onCameraViewStarted(int width, int height) {
		Log.i("onCameraViewStarted","FdActivity");
		mGray = new Mat();
		mRgba = new Mat();
	}

	public void onCameraViewStopped() {
		mGray.release();
		mRgba.release();
	}

	public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
		Log.i("onCameraFrame","FdActivity");
		mRgba = inputFrame.rgba();
		mGray = inputFrame.gray();

		if (mAbsoluteFaceSize == 0) {
			int height = mGray.rows();
			if (Math.round(height * mRelativeFaceSize) > 0) {
				mAbsoluteFaceSize = Math.round(height * mRelativeFaceSize);
			}
			mNativeDetector.setMinFaceSize(mAbsoluteFaceSize);
		}

		MatOfRect faces = new MatOfRect();
		//Check for face in captured frame
		if (mDetectorType == JAVA_DETECTOR) {
			if (mJavaDetector != null)
				mJavaDetector.detectMultiScale(mGray, faces, 1.1, 2, 2, // TODO: objdetect.CV_HAAR_SCALE_IMAGE
						new Size(mAbsoluteFaceSize, mAbsoluteFaceSize), new Size());
		}
		else if (mDetectorType == NATIVE_DETECTOR) {
			if (mNativeDetector != null)
				mNativeDetector.detect(mGray, faces);
		}
		else {
			Log.e(TAG, "Detection method is not selected!");
		}

		//Highlight the face area by drawing rectangle
		Rect[] facesArray = faces.toArray();
		boolean faceDetetcted=false;
		for (int i = 0; i < facesArray.length; i++)
		{
			Core.rectangle(mRgba, facesArray[i].tl(), facesArray[i].br(), FACE_RECT_COLOR, 3);
			faceDetetcted=true;
		}
		String imgPath="";
		if (faceDetetcted){
			//Check there is minimum free memory is available
			if(FreeMemory()<5){
				// Low memory: Image directory with files to be deleted
				ContextWrapper cw = new ContextWrapper(getApplicationContext());
				File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);

				//check if dir is not null
				if (directory != null){

					//list all files
					File[] filenames = directory.listFiles();

					// loop through each file and delete
					for (File tmpImages : filenames){
						tmpImages.delete();
					}
				}
			}
			
			//get image directory and make image file name
			ContextWrapper cw = new ContextWrapper(getApplicationContext());
			File directory = cw.getDir("imageDir",Context.MODE_WORLD_READABLE);
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss");
			String date = dateFormat.format(new Date());
			String photoFile = "/Picture_" + date + ".jpg";
			imgPath=directory + photoFile;
			
			//Write the image file in internal storage
			boolean bool=Highgui.imwrite(imgPath,mRgba);
			if (bool)
			{
				Log.i(TAG, "SUCCESS writing Intruder image to internal storage");
				
				//return control to controllerActivity
				//and pass face entity object to with path to saved image
				Intent fdData = new Intent();

				Intent intent = getIntent();
				//keep = intent.getExtras().getBoolean("keep");
				FaceEntity face =(FaceEntity) intent.getSerializableExtra("faceEntity");
				face.path=imgPath;
				Log.i("face.path=",face.path);
				fdData.putExtra("faceEntity", face);
				fdData.putExtra("RESULT_FACEACTIVITY", "success");
				// Activity finished ok, return the data
				setResult(RESULT_OK, fdData);
				Log.i("FdActivity","stop");
				finish();
			}
			else
				Log.i(TAG, "Fail writing Intruder image to internal storage");


		}

		/* Intent intent = new Intent(this, controllerActivity.class);

        String message = imgPath;
        intent.putExtra(EXTRA_MESSAGE,message);
        startActivity(intent); */

		return mRgba;
	}

	public long FreeMemory()
	{
		StatFs statFs = new StatFs(Environment.getRootDirectory().getAbsolutePath());
		long Free  = (statFs.getAvailableBlocks() *  (long) statFs.getBlockSize()) / 1048576;
		return Free;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		Log.i(TAG, "called onCreateOptionsMenu");
		mItemFace50 = menu.add("Face size 50%");
		mItemFace40 = menu.add("Face size 40%");
		mItemFace30 = menu.add("Face size 30%");
		mItemFace20 = menu.add("Face size 20%");
		mItemType   = menu.add(mDetectorName[mDetectorType]);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.i(TAG, "called onOptionsItemSelected; selected item: " + item);
		if (item == mItemFace50)
			setMinFaceSize(0.5f);
		else if (item == mItemFace40)
			setMinFaceSize(0.4f);
		else if (item == mItemFace30)
			setMinFaceSize(0.3f);
		else if (item == mItemFace20)
			setMinFaceSize(0.2f);
		else if (item == mItemType) {
			mDetectorType = (mDetectorType + 1) % mDetectorName.length;
			item.setTitle(mDetectorName[mDetectorType]);
			setDetectorType(mDetectorType);
		}
		return true;
	}

	private void setMinFaceSize(float faceSize) {
		mRelativeFaceSize = faceSize;
		mAbsoluteFaceSize = 0;
	}

	private void setDetectorType(int type) {
		if (mDetectorType != type) {
			mDetectorType = type;

			if (type == NATIVE_DETECTOR) {
				Log.i(TAG, "Detection Based Tracker enabled");
				mNativeDetector.start();
			} else {
				Log.i(TAG, "Cascade detector enabled");
				mNativeDetector.stop();
			}
		}
	}
}