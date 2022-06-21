package com.talktome.talktomeapp;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.SurfaceView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.opencv.android.CameraActivity;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.Collections;
import java.util.List;

public class VideoActivity extends CameraActivity {

    private static String LOGTAG = "OpenCv_Log";
    private CameraBridgeViewBase mOpenCvCameraView;

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status){
                case LoaderCallbackInterface
                        .SUCCESS:{
                    Log.d(LOGTAG, "OpenCv Loaded");
                    mOpenCvCameraView.enableView();
                }break;
                default:
                {
                    super.onManagerConnected(status);
                }break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        //OpenCv Camera
        mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.opencv_surface_view);
        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(cvCameraViewListener);

        //Navigation menu
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        //set translate home page selected
        bottomNavigationView.setSelectedItemId(R.id.nav_video);

        //selected navigation item
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_translate:
                        startActivity(new Intent(getApplicationContext(), SecondActivity.class));
                        overridePendingTransition(0,0);

                    case R.id.nav_video:
                        return true;

                    case R.id.nav_camera:
                        startActivity(new Intent(getApplicationContext(), CameraActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.nav_folder:
                        startActivity(new Intent(getApplicationContext(), FolderActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    protected List<?extends CameraBridgeViewBase> getCameraViewList(){
        return Collections.singletonList(mOpenCvCameraView);
    }

    private CameraBridgeViewBase.CvCameraViewListener2 cvCameraViewListener = new CameraBridgeViewBase.CvCameraViewListener2() {
        @Override
        public void onCameraViewStarted(int width, int height) {

        }

        @Override
        public void onCameraViewStopped() {

        }

        @Override
        public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
            Mat input_rgba = inputFrame.rgba();
            Mat input_gray = inputFrame.gray();

            MatOfPoint corners = new MatOfPoint();
            Imgproc.goodFeaturesToTrack(input_gray, corners, 20, 0.01, 10, new Mat(), 3, false);
            Point[] cornersArr = corners.toArray();

            for(int i = 0; i <corners.rows(); i++){
                Imgproc.circle(input_rgba, cornersArr[i], 10, new Scalar(0, 255, 0), 2);
            }
            return input_rgba;
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        if(mOpenCvCameraView != null){
            mOpenCvCameraView.disableView();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!OpenCVLoader.initDebug()){
            Log.d(LOGTAG, "OpenCV não encontrado, inicializando.");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION, this, mLoaderCallback);
        }else{
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mOpenCvCameraView != null){
            mOpenCvCameraView.disableView();
        }
    }
}