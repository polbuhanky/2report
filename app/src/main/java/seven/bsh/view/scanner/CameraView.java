package seven.bsh.view.scanner;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.ErrorCallback;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.widget.FrameLayout;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import seven.bsh.view.scanner.decoder.zxing.AutoFocusManager;

public class CameraView extends FrameLayout implements Callback, PreviewCallback, ErrorCallback {
    private SurfaceHolder mSurfaceHolder;
    private SurfaceView mSurface;
    private Camera mCamera;
    private PreviewCallback mPreviewCallback;
    private AutoFocusManager mAutoFocusManager;
    private CameraViewListener mListener;
    private AtomicBoolean mCameraInitialized = new AtomicBoolean(false);
    private transient boolean mLive = false;
    private int mLastUsedCameraId = -1;
    private ErrorCallback mErrorCallback;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public CameraView(Context context) {
        super(context);
    }

    public CameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CameraView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    //---------------------------------------------------------------------------
    //
    // HANDLERS
    //
    //---------------------------------------------------------------------------

    public void onError(int code, Camera camera) {
        if (mListener != null) {
            mListener.onCameraError();
        }
    }

    public void surfaceCreated(SurfaceHolder holder) {
        try {
            mCamera.setPreviewDisplay(holder);
        } catch (Throwable error) {
            Log.d("DBG", "Error setting camera preview: " + error.getMessage());
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // ignored
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (mSurfaceHolder.getSurface() != null) {
            stopPreview();

            try {
                Parameters e = mCamera.getParameters();

                try {
                    mCamera.setDisplayOrientation(90);
                } catch (Throwable ignored) {
                }

                mCamera.setPreviewDisplay(mSurfaceHolder);
                mCamera.setPreviewCallback(mPreviewCallback);
                mCamera.setErrorCallback(mErrorCallback);
                Size size = getOptimalPreviewSize(e.getSupportedPreviewSizes(), getWidth(), getHeight());

                if (size != null) {
                    LayoutParams params = (LayoutParams) mSurface.getLayoutParams();
                    params.width = getWidth() > getHeight() ? size.width : size.height;
                    params.height = getWidth() > getHeight() ? size.height : size.width;
                    mSurface.setLayoutParams(params);

                    if (params.width < getWidth() || params.height < getHeight()) {
                        if (params.width < getWidth()) {
                            float k = (float) getWidth() / (float) params.width;
                            params.height *= k;
                            params.width *= k;
                        }

                        if (params.height < getHeight()) {
                            float k = (float) getHeight() / (float) params.height;
                            params.height *= k;
                            params.width *= k;
                        }
                    }

                    e.setPreviewSize(size.width, size.height);
                }

                mCamera.setParameters(e);
                startPreview();
            } catch (Exception error) {
                Log.d("DBG", "Error starting camera preview: " + error.getMessage());
            }
        }
    }

    //---------------------------------------------------------------------------
    //
    // PRIVATE METHODS
    //
    //---------------------------------------------------------------------------

    private Size getOptimalPreviewSize(List<Size> sizes, int width, int height) {
        final double ASPECT_TOLERANCE = 0.1;
        final double MAX_DOWNSIZE = 1.5;
        double targetRatio = (double) width / height;

        if (sizes == null) {
            return null;
        }

        Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        for (Size size : sizes) {
            double ratio = (double) size.width / size.height;
            double downsize = (double) size.width / width;

            if (downsize > MAX_DOWNSIZE) {
                continue;
            }

            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) {
                continue;
            }

            if (Math.abs(size.height - height) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - height);
            }
        }

        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;

            for (Size size : sizes) {
                double downsize = (double) size.width / width;

                if (downsize > MAX_DOWNSIZE) {
                    continue;
                }

                if (Math.abs(size.height - height) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - height);
                }
            }
        }

        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;

            for (Size size : sizes) {
                if (Math.abs(size.height - height) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - height);
                }
            }
        }

        return optimalSize;
    }

    private void offFlash() {
        Camera camera = getCamera();
        Parameters parameters = camera.getParameters();

        if (mLive && parameters.getSupportedFlashModes() != null &&
            parameters.getFlashMode() != null &&
            parameters.getSupportedFlashModes().size() > 0) {

            if (!parameters.getFlashMode().equals("off")) {
                parameters.setFlashMode("off");
                camera.setParameters(parameters);
            }
        }
    }

    //---------------------------------------------------------------------------
    //
    // PUBLIC METHODS
    //
    //---------------------------------------------------------------------------

    public synchronized void stop() {
        if (mLive) {
            offFlash();
        }

        mLive = false;

        if (mSurfaceHolder != null) {
            mSurfaceHolder.removeCallback(this);
            stopPreview();
        }

        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.setErrorCallback(null);
            mCamera.release();
            mCamera = null;
        }
    }

    public void start() {
        start(findDefaultCameraId());
    }

    public synchronized void start(int cameraId) {
        try {
            mCamera = setupCamera(cameraId);
            mPreviewCallback = this;
            mErrorCallback = this;
            mCameraInitialized.set(false);

            removeAllViews();
            mSurface = new SurfaceView(getContext());
            addView(mSurface);

            LayoutParams layoutParams = (LayoutParams) mSurface.getLayoutParams();
            layoutParams.gravity = Gravity.CENTER;

            mSurface.setLayoutParams(layoutParams);
            mSurfaceHolder = mSurface.getHolder();
            mSurfaceHolder.addCallback(this);

            mLastUsedCameraId = cameraId;
            mLive = true;
        } catch (RuntimeException ex) {
            onError(0, null);
        }
    }

    public void stopPreview() {
        if (mCamera != null) {
            if (mAutoFocusManager != null) {
                mAutoFocusManager.stop();
                mAutoFocusManager = null;
            }

            try {
                mCamera.stopPreview();
            } catch (Exception ignored) {
            }
        }
    }

    private void startPreview() {
        if (mCamera != null) {
            mCamera.startPreview();
            mAutoFocusManager = new AutoFocusManager(mCamera);
        }
    }

    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (mLive) {
            postDelayed(new Runnable() {
                public void run() {
                    stop();
                    postDelayed(new Runnable() {
                        public void run() {
                            try {
                                if (mLastUsedCameraId >= 0) {
                                    start(mLastUsedCameraId);
                                } else {
                                    start();
                                }
                            } catch (Throwable error) {
                                Log.e(getClass().getSimpleName(), "Failed to re-open the camera after configuration change: " + error.getMessage(), error);
                            }
                        }
                    }, 100L);
                }
            }, 50L);
        }
    }

    public void onPreviewFrame(byte[] data, Camera camera) {
        if (mListener != null) {
            if (!mCameraInitialized.getAndSet(true)) {
                mListener.onCameraReady(camera);
            }

            try {
                mListener.onPreviewData(data, 17, camera.getParameters().getPreviewSize());
            } catch (Throwable ignored) {
            }
        }
    }

    protected int findDefaultCameraId() {
        return findCamera();
    }

    @TargetApi(9)
    private int findCamera() {
        int camerasCount = Camera.getNumberOfCameras();
        CameraInfo cameraInfo = new CameraInfo();

        for (int id = 0; id < camerasCount; ++id) {
            Camera.getCameraInfo(id, cameraInfo);

            if (cameraInfo.facing == 0) {
                return id;
            }
        }

        if (camerasCount > 0) {
            return 0;
        }

        throw new RuntimeException("Did not find camera on this device");
    }

    protected Camera setupCamera(int cameraId) {
        try {
            return openCamera(cameraId);
        } catch (Throwable error) {
            throw new RuntimeException("Failed to open a camera with id " + cameraId + ": " + error.getMessage(), error);
        }
    }

    @TargetApi(9)
    private Camera openCamera(int cameraId) {
        return Camera.open(cameraId);
    }

    public Camera getCamera() {
        return mCamera;
    }

    public void setListener(CameraViewListener listener) {
        mListener = listener;
    }

    public void toggleTorch() {
        Camera camera = getCamera();

        if (camera == null) {
            return;
        }

        Parameters params = camera.getParameters();

        if (params.getFlashMode().equals(Parameters.FLASH_MODE_TORCH)) {
            params.setFlashMode(Parameters.FLASH_MODE_OFF);
        } else {
            params.setFlashMode(Parameters.FLASH_MODE_TORCH);
        }

        stopPreview();
        camera.setParameters(params);
        startPreview();
    }

    public interface CameraViewListener {
        void onCameraReady(Camera camera);

        void onCameraError();

        void onPreviewData(byte[] bytes, int var2, Size size);
    }
}
