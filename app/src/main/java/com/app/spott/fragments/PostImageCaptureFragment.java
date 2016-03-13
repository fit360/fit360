package com.app.spott.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.percent.PercentLayoutHelper;
import android.support.percent.PercentRelativeLayout;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.spott.R;
import com.app.spott.utils.Utils;
import com.app.spott.views.ImageSurfaceView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PostImageCaptureFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PostImageCaptureFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostImageCaptureFragment extends Fragment {

    //TODO use Camera2 API
    //TODO normalize orientation to straight for camera preview, its different for different devices
    //TODO implement auto focus


    @Bind(R.id.cameraPreview1)
    FrameLayout cameraPreviewLayout1;

    @Bind(R.id.cameraPreview2)
    FrameLayout cameraPreviewLayout2;

    @Bind(R.id.ivCaptureImage1)
    ImageView ivCapturedImage1;

    @Bind(R.id.ivCaptureImage2)
    ImageView ivCapturedImage2;

    @Bind(R.id.btnFull)
    Button btnFull;

    @Bind(R.id.btnSplit)
    Button btnSplit;

    @Bind(R.id.btnGallery)
    Button btnGallery;

    @Bind(R.id.btnTakePicture)
    Button btnTakePicture;

    @Bind(R.id.btnClear)
    Button btnClear;

    @Bind(R.id.ivTest)
    ImageView ivTest;

    private ImageSurfaceView mImageSurfaceView1;
    private ImageSurfaceView mImageSurfaceView2;
    public final static int PICK_PHOTO_CODE = 1046;

    private Camera camera;

    private int MODE_FULL = 0;
    private int MODE_SPLIT = 1;
    private int CAMERA_SELECT_1 = 0;
    private int CAMERA_SELECT_2 = 1;
    public static final String FINAL_IMAGE_NAME = "post_image.png";
    private final String TEMP_IMAGE_NAME = "tmp_post_image.png";

    private int mMode;
    private int mCameraSelect;

    private Bitmap mFinalBitmap;
    private Bitmap mCapturedBitmap1;
    private Bitmap mCapturedBitmap2;

    private OnFragmentInteractionListener mListener;
    private Context mContext;

    public PostImageCaptureFragment() {
        // Required empty public constructor
    }

    public static PostImageCaptureFragment newInstance() {
        PostImageCaptureFragment fragment = new PostImageCaptureFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_image_capture, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        btnTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera.takePicture(null, null, pictureCallback);
            }
        });

        btnFull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PercentRelativeLayout.LayoutParams layoutParams = (PercentRelativeLayout.LayoutParams) cameraPreviewLayout1.getLayoutParams();
                PercentLayoutHelper.PercentLayoutInfo layoutInfo = layoutParams.getPercentLayoutInfo();
                layoutInfo.widthPercent = 1f;
                cameraPreviewLayout1.requestLayout();
                cameraPreviewLayout2.setVisibility(View.GONE);
                mMode = MODE_FULL;
            }
        });

        btnSplit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PercentRelativeLayout.LayoutParams layoutParams = (PercentRelativeLayout.LayoutParams) cameraPreviewLayout1.getLayoutParams();
                PercentLayoutHelper.PercentLayoutInfo percentLayoutInfo = layoutParams.getPercentLayoutInfo();
                percentLayoutInfo.widthPercent = 0.5f;
                cameraPreviewLayout1.requestLayout();
                cameraPreviewLayout2.setVisibility(View.VISIBLE);
                mMode = MODE_SPLIT;
                verifyReadyToSubmit();

            }
        });

        cameraPreviewLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraPreviewLayout1.removeView(mImageSurfaceView1);
                cameraPreviewLayout2.removeView(mImageSurfaceView2);
                camera = checkDeviceCamera();
                mImageSurfaceView1 = new ImageSurfaceView(mContext, camera);
                cameraPreviewLayout1.addView(mImageSurfaceView1);
                mCameraSelect = CAMERA_SELECT_1;
                cameraPreviewLayout1.setBackground(getResources().getDrawable(R.drawable.drawable_placeholder_cam_preview_selected));
                cameraPreviewLayout2.setBackground(getResources().getDrawable(R.drawable.drawable_placeholder_cam_preview));
            }
        });

        cameraPreviewLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraPreviewLayout1.removeView(mImageSurfaceView1);
                cameraPreviewLayout2.removeView(mImageSurfaceView2);
                camera = checkDeviceCamera();
                mImageSurfaceView2 = new ImageSurfaceView(mContext, camera);
                cameraPreviewLayout2.addView(mImageSurfaceView2);
                mCameraSelect = CAMERA_SELECT_2;
                cameraPreviewLayout1.setBackground(getResources().getDrawable(R.drawable.drawable_placeholder_cam_preview));
                cameraPreviewLayout2.setBackground(getResources().getDrawable(R.drawable.drawable_placeholder_cam_preview_selected));
            }
        });

        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPickPhoto();
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clear();
            }
        });

        mMode = MODE_FULL;
        mCameraSelect = CAMERA_SELECT_1;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public void onPickPhoto() {
        // Create intent for picking a photo from the gallery
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(mContext.getPackageManager()) != null) {
            // Bring up gallery to select a photo
            startActivityForResult(intent, PICK_PHOTO_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            if (requestCode == PICK_PHOTO_CODE) {

                Uri photoUri = data.getData();
                // Do something with the photo based on Uri
                Bitmap selectedImage = null;
                try {
                    selectedImage = BitmapFactory.decodeStream(mContext.getContentResolver().openInputStream(photoUri));
                    selectedImage = scaleDownBitmapImage(selectedImage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                setBitmap(selectedImage);
            }

        }
    }

    private Camera checkDeviceCamera() {
        Camera mCamera = null;
        try {
            mCamera = Camera.open();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mCamera;
    }

    Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            if (bitmap == null) {
                Toast.makeText(mContext, "Captured image is empty", Toast.LENGTH_LONG).show();
                return;
            }
            bitmap = normalizeCameraImage(bitmap);
            setBitmap(bitmap);
        }
    };

    private Bitmap scaleDownBitmapImage(Bitmap bitmap) {
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 600, 600, true);
        return resizedBitmap;
    }


    private void setBitmap(Bitmap bitmap) {
        if (mCameraSelect == CAMERA_SELECT_1) {
            mCapturedBitmap1 = bitmap;
            ivCapturedImage1.setImageBitmap(bitmap);
            ivCapturedImage1.setVisibility(View.VISIBLE);
            if (mImageSurfaceView1 != null) {
                mImageSurfaceView1.setVisibility(View.GONE);
            }
        } else {
            mCapturedBitmap2 = bitmap;
            ivCapturedImage2.setImageBitmap(bitmap);
            ivCapturedImage2.setVisibility(View.VISIBLE);
            if (mImageSurfaceView2 != null) {
                mImageSurfaceView2.setVisibility(View.GONE);
            }
        }

        verifyReadyToSubmit();
    }

    private void verifyReadyToSubmit(){
        if (mMode == MODE_FULL) {
            if (ivCapturedImage1.getDrawable() != null) {
                mFinalBitmap = mCapturedBitmap1;
                btnTakePicture.setText("Next");
                btnTakePicture.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        saveImage(mFinalBitmap, FINAL_IMAGE_NAME);
                        mListener.startComposeFragment();
                    }
                });
            } else {
                btnTakePicture.setText("Click");
                btnTakePicture.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        camera.takePicture(null, null, pictureCallback);
                    }
                });
            }
        } else {
            if (ivCapturedImage1.getDrawable() != null && ivCapturedImage2.getDrawable() != null) {
                mFinalBitmap = combineImages(mCapturedBitmap1, mCapturedBitmap2);
                btnTakePicture.setText("Next");
                btnTakePicture.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        saveImage(mFinalBitmap, FINAL_IMAGE_NAME);
                        mListener.startComposeFragment();
                    }
                });
            } else {
                btnTakePicture.setText("Click");
                btnTakePicture.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        camera.takePicture(null, null, pictureCallback);
                    }
                });
            }
        }
    }

    private String saveImage(Bitmap bitmap, String name){
        Uri uri = Utils.getPhotoFileUri(mContext, name);
        File file = new File(uri.getPath());
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return uri.getPath();
    }

    private void clear() {
        mMode = MODE_FULL;
        mCameraSelect = CAMERA_SELECT_1;
        PercentRelativeLayout.LayoutParams layoutParams = (PercentRelativeLayout.LayoutParams) cameraPreviewLayout1.getLayoutParams();
        PercentLayoutHelper.PercentLayoutInfo layoutInfo = layoutParams.getPercentLayoutInfo();
        layoutInfo.widthPercent = 1f;
        cameraPreviewLayout1.requestLayout();
        cameraPreviewLayout2.setVisibility(View.GONE);
        ivCapturedImage1.setVisibility(View.GONE);
        btnTakePicture.setText("Click");
        btnTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera.takePicture(null, null, pictureCallback);
            }
        });
    }

    private Bitmap combineImages(Bitmap a, Bitmap b) {

        Bitmap c = Bitmap.createBitmap(a.getWidth() + b.getWidth(), a.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas comboImage = new Canvas(c);

        comboImage.drawBitmap(a, 0f, 0f, null);
        comboImage.drawBitmap(b, a.getWidth(), 0f, null);

        return scaleDownBitmapImage(c);
    }

    private Bitmap normalizeCameraImage(Bitmap bitmap){
        Bitmap transformedBitmap = scaleDownBitmapImage(bitmap);
        transformedBitmap = rotateBitmapOrientation(camera, transformedBitmap);
        return transformedBitmap;
    }

    public Bitmap rotateBitmapOrientation(Camera camera, Bitmap bitmap) {
        int rotationAngle = getRotationAngleFix(camera);
        Matrix matrix = new Matrix();
        matrix.setRotate(rotationAngle, (float) bitmap.getWidth() / 2, (float) bitmap.getHeight() / 2);
        Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        // Return result
        return rotatedBitmap;
    }

    private int getRotationAngleFix(Camera camera){
        Camera.Parameters parameters = camera.getParameters();

        android.hardware.Camera.CameraInfo camInfo =
                new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(getBackFacingCameraId(), camInfo);


        Display display = ((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int rotation = display.getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (camInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (camInfo.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (camInfo.orientation - degrees + 360) % 360;
        }
        return result;
    }

    public void setCameraDisplayOrientation(android.hardware.Camera camera) {
        int result = getRotationAngleFix(camera);
        camera.setDisplayOrientation(result);
    }

    private int getBackFacingCameraId() {
        int cameraId = -1;
        // Search for the front facing camera
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {

                cameraId = i;
                break;
            }
        }
        return cameraId;
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void startComposeFragment();
    }

}
