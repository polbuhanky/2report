package seven.bsh.view.start;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Camera;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;

import seven.bsh.R;
import seven.bsh.view.BaseActivity;
import seven.bsh.view.scanner.CameraView;
import seven.bsh.view.scanner.ScannerView;

public class QrScannerActivity extends BaseActivity implements ScannerView.ScannerViewEventListener {
    public static final String EXTRA_DATA = "data";

    private static final int CAMERA_NOT_FOUND = -1;

    private ScannerView mScanner;
    private boolean mIsFront = false;
    private int mFrontCameraId = CAMERA_NOT_FOUND;
    private int mBackCameraId = CAMERA_NOT_FOUND;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        mScanner = findViewById(R.id.scanner);
        mScanner.setScannerViewEventListener(this);

        mFrontCameraId = getCameraId(Camera.CameraInfo.CAMERA_FACING_FRONT);
        mBackCameraId = getCameraId(Camera.CameraInfo.CAMERA_FACING_BACK);

        if (mBackCameraId == -1 && mFrontCameraId != -1) {
            mIsFront = true;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mScanner.stopScanner();
    }

    @Override
    public void onResume() {
        super.onResume();
        mScanner.startScanner();
    }

    //---------------------------------------------------------------------------
    //
    // HANDLERS
    //
    //---------------------------------------------------------------------------

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_scanner, menu);
        PackageManager packageManager = getApplicationContext().getPackageManager();
        MenuItem cameraRotate = menu.findItem(R.id.action_camera_rotate);
        MenuItem flashlight = menu.findItem(R.id.action_flashlight);

        // check front camera
        if (mFrontCameraId == -1 || mBackCameraId == -1) {
            cameraRotate.setVisible(false);
        } else {
            cameraRotate.setIcon(getToolbarIcon(GoogleMaterial.Icon.gmd_camera_rear));
        }

        // check flashlight
        if (!packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
            flashlight.setVisible(false);
        } else {
            flashlight.setIcon(getToolbarIcon(GoogleMaterial.Icon.gmd_flare));
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_flashlight:
                onToggleFlashLightItemClicked();
                break;

            case R.id.action_camera_rotate:
                onCameraRotateItemClicked();
                break;

            case android.R.id.home:
                onBackPressed();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onScannerReady() {
        // ignored
    }

    @Override
    public void onScannerFailure() {
        findViewById(R.id.viewfinder).setVisibility(View.GONE);
        findViewById(R.id.error_text).setVisibility(View.VISIBLE);
    }

    @Override
    public void onCodeScanned(String data) {
        vibrate(150);
        Intent intent = new Intent();
        intent.putExtra("data", data);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void onToggleFlashLightItemClicked() {
        CameraView cameraView = mScanner.getCamera();
        cameraView.toggleTorch();
    }

    private void onCameraRotateItemClicked() {
        CameraView cameraView = mScanner.getCamera();
        cameraView.stop();

        findViewById(R.id.viewfinder).setVisibility(View.VISIBLE);
        findViewById(R.id.error_text).setVisibility(View.GONE);

        if (mIsFront) {
            cameraView.start(mBackCameraId);
        } else {
            cameraView.start(mFrontCameraId);
        }

        mIsFront = !mIsFront;
    }

    //---------------------------------------------------------------------------
    //
    // PRIVATE METHODS
    //
    //---------------------------------------------------------------------------

    private int getCameraId(int type) {
        int cameraId = CAMERA_NOT_FOUND;
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);

            if (info.facing == type) {
                cameraId = i;
                break;
            }
        }
        return cameraId;
    }

    //---------------------------------------------------------------------------
    //
    // ACCESSORS
    //
    //---------------------------------------------------------------------------

    @Override
    protected int getContentViewRes() {
        return R.layout.activity_main;
    }
}
