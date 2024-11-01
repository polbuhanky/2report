package seven.bsh.view.report.update;

import static com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AlertDialog;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.CancellationTokenSource;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnTokenCanceledListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.function.Consumer;

import seven.bsh.BuildConfig;
import seven.bsh.R;
import seven.bsh.db.DatabaseHelper;
import seven.bsh.db.entity.QueueReport;
import seven.bsh.location.OneShotLocation;
import seven.bsh.model.ChecklistAttribute;
import seven.bsh.net.service.QueueService;
import seven.bsh.view.attributes.AttributeItem;
import seven.bsh.view.attributes.FileInputAttribute;
import seven.bsh.view.attributes.IInputAttribute;
import seven.bsh.view.attributes.PhotoInputAttribute;
import seven.bsh.view.custom_views.CameraPhotoActivity3;
import seven.bsh.view.fileManager.FileManagerActivity;
import seven.bsh.view.report.BaseReportActivity;
import seven.bsh.view.report.update.form.ReportForm;
import seven.bsh.view.widget.overlay.loader.GpsLoaderIndicatorView;

public abstract class BaseUpdateReportActivity extends BaseReportActivity implements
        OneShotLocation.OnOneShotLocationListener,
        PhotoInputAttribute.OnPhotoFileClickListener,
        FileInputAttribute.OnFileClickListener {
    public static final int REQUEST_CODE_FILE_MANAGER = 0;
    public static final int REQUEST_CODE_IMAGE_CAPTURE = 100;

    public static final int REQUEST_PERMISSION_FILE = 0;
    public static final int REQUEST_PERMISSION_PHOTO = 1;
    public static final int REQUEST_PERMISSION_GPS = 2;
    private final int PHOTO_REQUEST_CODE = 11;
    private FusedLocationProviderClient fusedLocationClientGoogle;


    private static final String TAG = "CreateReportActivity";

    private FusedLocationProviderClient fusedLocationClient;
    private final CancellationTokenSource cancellationTokenSource = new CancellationTokenSource();
    protected ReportForm form;
    protected OneShotLocation oneShotLocation;
    protected FileInputAttribute currentFileAttribute;
    protected File tempFile;
    protected JSONObject reportData;
    private Location location;

    private View mGpsLoaderView;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        form = new ReportForm();
        form.setPhotoLimit(getLocalData().getImageSize());
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        sendReportCV.setOnClickListener(view -> onActionSaveToQueue());

    }

    //---------------------------------------------------------------------------
    //
    // HANDLERS
    //
    //---------------------------------------------------------------------------

    @Override
    public void onPause() {
        super.onPause();
        hideGpsLoader();
        hideLoader();
        if (oneShotLocation != null) {
            oneShotLocation.setListener(null);
            oneShotLocation.stopListening();
            oneShotLocation = null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onActionSaveAsDraft();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    protected void onActionSaveAsDraft() {
        try {
            hideKeyboard();
            form.prepareDataValues();
            if (isDraft() || form.isChanged()) {
                showLoader();
                bgTask = new CreateReportActivity.PrepareAttributeTask(true).execute();
            } else {
                finish();
            }
        } catch (Exception ex) {
            Log.e(TAG, ex.toString(), ex);
            Toast.makeText(this, R.string.activity_update_report_error_draft, Toast.LENGTH_LONG).show();
            hideLoader();
        }
    }

    protected void onActionSaveToQueue() {
        hideKeyboard();
        form.prepareDataValues();
        if (!form.validate()) {
            showSimpleDialog(getString(R.string.activity_update_report_error_validate));
            return;
        }

        if (checklist.isGps()) {
            if (!checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) || !checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION,
                        },
                        REQUEST_PERMISSION_GPS
                );
                return;
            }

            getGeoLocation();
        } else {
            showLoader();
            bgTask = new PrepareAttributeTask().execute();
        }
    }

    private void getGeoLocation() {
        if (oneShotLocation == null) {
            oneShotLocation = new OneShotLocation(getApplicationContext(), getPackageManager());
            oneShotLocation.setListener(this);
        }

//        if (oneShotLocation.isAvailable()) {
//            if (!oneShotLocation.isProvidersEnabled()) {
                showSimpleDialog(getString(R.string.gps_error_services), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent viewIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(viewIntent);
                    }
                });
//            } else {
//                showGpsLoader();
//                oneShotLocation.startListening();
//            }
//        } else {
//            showLoader();
//            bgTask = new PrepareAttributeTask().execute();
//        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        try {
            switch (requestCode) {
                case REQUEST_PERMISSION_FILE:
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        onFileClick(currentFileAttribute);
                    }
                    break;

                case REQUEST_CODE_IMAGE_CAPTURE:
                    if (grantResults.length == 2 &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                            grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                        onImageCaptureClick((PhotoInputAttribute) currentFileAttribute);
                    }
                    break;

                case REQUEST_PERMISSION_GPS:
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        getGeoLocation();
                    }
                    break;
            }
        } catch (Exception e) {
            Toast.makeText(BaseUpdateReportActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onLocationFound(final @NonNull Location location) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hideGpsLoader();
                float accuracy = location.getAccuracy();
                int epsilon = checklist.getGpsEpsilon();
                if (epsilon == -1 || location.getAccuracy() <= epsilon) {
                    showLoader();
                    form.setLocation(location);
                    bgTask = new PrepareAttributeTask().execute();
                    return;
                }

                new AlertDialog.Builder(BaseUpdateReportActivity.this)
                        .setMessage(getString(R.string.activity_report_popup_gps_accuracy, (int) accuracy))
                        .setPositiveButton(getString(R.string.dialog_button_yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                showLoader();
                                form.setLocation(location);
                                bgTask = new PrepareAttributeTask().execute();
                            }
                        })
                        .setNegativeButton(getString(R.string.dialog_button_no), null)
                        .create()
                        .show();
            }
        });
    }

    @Override
    public void onLocationTimeout() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hideGpsLoader();
                new AlertDialog.Builder(BaseUpdateReportActivity.this)
                        .setMessage(getString(R.string.activity_report_popup_no_gps))
                        .setPositiveButton(getString(R.string.dialog_button_yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                showLoader();
                                bgTask = new PrepareAttributeTask().execute();
                            }
                        })
                        .setNegativeButton(getString(R.string.dialog_button_no), null)
                        .create()
                        .show();
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    class PrepareAttributeTask extends AsyncTask<Void, Void, Void> {
        private final boolean draft;
        private int queueId;


        public PrepareAttributeTask(boolean draft) {
            this.draft = draft;
        }

        public PrepareAttributeTask() {
            this(false);
        }

        @Override
        protected Void doInBackground(Void... params) {
            if (checklist != null) {
                queueId = prepareReportData(this, draft);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            try {
                if (isCancelled()) {
                    return;
                }

                if (checklist != null && !draft && hasNetwork()) {
                    Intent intent = new Intent(BaseUpdateReportActivity.this, QueueService.class);
                    intent.putExtra(QueueService.KEY_COMMAND, QueueService.COMMAND_ADD);
                    intent.putExtra(QueueService.KEY_LIST, new int[]{queueId});
                    startService(intent);
                }

                if (draft && !isDraft()) {
                    Toast.makeText(BaseUpdateReportActivity.this, R.string.activity_report_popup_draft, Toast.LENGTH_LONG).show();
                }
                finish();
            } catch (Exception ex) {
                Log.e(TAG, ex.toString(), ex);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        currentFileAttribute.setActivityResult(requestCode, resultCode, data, tempFile);
    }

    @Override
    public void onFileClick(FileInputAttribute target) {
        currentFileAttribute = target;
        if (!checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    },
                    REQUEST_PERMISSION_FILE
            );
            return;
        }

        Intent intent = new Intent(this, FileManagerActivity.class);
        startActivityForResult(intent, REQUEST_CODE_FILE_MANAGER);
    }

    @Override
    public void onImageCaptureClick(PhotoInputAttribute target) {
        currentFileAttribute = target;
        if (!checkPermission(Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA,
                    },
                    REQUEST_PERMISSION_PHOTO
            );
            return;
        }

        tempFile = getOutputMediaFile();
        Uri uriFile;

        if (android.os.Build.VERSION.SDK_INT >= 24) {
//            uriFile = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", tempFile);
            uriFile = Uri.fromFile(tempFile);

        } else {
            uriFile = Uri.fromFile(tempFile);
        }
        try {
            FirebaseCrashlytics.getInstance().log("cameraPrepare");
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uriFile);
            startCamera();
        } catch (Exception e) {
            Toast.makeText(BaseUpdateReportActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void startCamera() {
        Log.d(TAG, "requestCurrentLocation()");
        showLoader();
        FirebaseCrashlytics.getInstance().log("startCamera()");
        // Request permission
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            FirebaseCrashlytics.getInstance().log("have not write permission");
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            hideLoader();
            return;
        }
        getLocation();
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            // Main code
            Task<Location> currentLocationTask = fusedLocationClient.getCurrentLocation(
                    PRIORITY_HIGH_ACCURACY,
                    cancellationTokenSource.getToken()
            );
            SimpleDateFormat watermarkDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault());
            fusedLocationClient.getLastLocation().addOnSuccessListener((task -> {
                        Location location;
                        if (task == null) {
                            LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
                            locationManager.getCurrentLocation(
                                    LocationManager.NETWORK_PROVIDER,
                                    null,
                                    getApplication().getMainExecutor(),
                                    new Consumer<Location>() {
                                        @Override
                                        public void accept(Location location) {
                                            hideLoader();
                                            String result = "";
                                            currentFileAttribute.waterText = watermarkDateFormat.format(new Date()) + "\n" +
                                                    location.getLongitude() + " " + location.getLatitude();
                                            startActivityForResult(new Intent(BaseUpdateReportActivity.this, CameraPhotoActivity3.class), REQUEST_CODE_IMAGE_CAPTURE);
                                            Log.d(TAG, "getCurrentLocation() result: " + result);
                                        }
                                    }
                            );

                        } else {
                            location = task;
                            hideLoader();
                            String result = "";
                            currentFileAttribute.waterText = watermarkDateFormat.format(new Date()) + "\n" +
                                    location.getLongitude() + " " + location.getLatitude();
                            startActivityForResult(new Intent(BaseUpdateReportActivity.this, CameraPhotoActivity3.class), REQUEST_CODE_IMAGE_CAPTURE);
                            Log.d(TAG, "getCurrentLocation() result: " + result);
                        }
                    })
            );
            currentLocationTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    hideLoader();
                    Toast.makeText(BaseUpdateReportActivity.this, "Произошла ошибка при определении координат", Toast.LENGTH_LONG).show();
                    currentFileAttribute.waterText = watermarkDateFormat.format(new Date());
                    FirebaseCrashlytics.getInstance().log("startCamera without location");
                    startActivityForResult(new Intent(BaseUpdateReportActivity.this, CameraPhotoActivity3.class), REQUEST_CODE_IMAGE_CAPTURE);
                }
            });
            //clickMyLocation();
            FirebaseCrashlytics.getInstance().log("listener init");

        } else {
            // TODO: Request fine location permission
            FirebaseCrashlytics.getInstance().log("have not location permission");
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        }
    }

    private void getLocation() {

        // Get the location manager
        LocationManager locationManager = (LocationManager)
                getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, false);
        Double longitude, latitude;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        Location location = locationManager.getLastKnownLocation(bestProvider);

        try {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        } catch (NullPointerException e) {
            latitude = -1.0;
            longitude = -1.0;
        }
    }

    @Override
    public void onGalleryClick(PhotoInputAttribute target) {
        try {
            currentFileAttribute = target;
            Intent intent = new Intent(Intent.ACTION_PICK, Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(intent, PhotoInputAttribute.REQUEST_CODE_GALLERY);
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage(), ex);
            Toast.makeText(this, R.string.activity_report_popup_gallery_unavailable, Toast.LENGTH_LONG).show();
        }
    }

    //---------------------------------------------------------------------------
    //
    // PRIVATE METHODS
    //
    //---------------------------------------------------------------------------

    protected void showGpsLoader() {
        if (mGpsLoaderView != null) {
            return;
        }

        ViewGroup root = getWindow().getDecorView().findViewById(android.R.id.content);
        GpsLoaderIndicatorView view = new GpsLoaderIndicatorView(this);
        mGpsLoaderView = view.inflate(root);
        keepScreenOn();
    }

    private void clickMyLocation() {
        try {
            LocationManager locationManager = (LocationManager) getApplication().getSystemService(LOCATION_SERVICE);

            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
                fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location == null) {
                            if (ActivityCompat.checkSelfPermission(BaseUpdateReportActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(BaseUpdateReportActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                return;
                            }
                            fusedLocationClient.getCurrentLocation(
                                    LocationRequest.PRIORITY_HIGH_ACCURACY, new CancellationToken() {
                                        @NonNull
                                        @Override
                                        public CancellationToken onCanceledRequested(@NonNull OnTokenCanceledListener onTokenCanceledListener) {
                                            return new CancellationTokenSource().getToken();
                                        }

                                        @Override
                                        public boolean isCancellationRequested() {
                                            return false;
                                        }
                                    }).addOnSuccessListener(new OnSuccessListener<Location>() {
                                @Override
                                public void onSuccess(Location location) {
                                    setInTheCurrentPosition(location);
                                }
                            });


                        } else {
                            setInTheCurrentPosition(location);
                        }
                    }

                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void setInTheCurrentPosition(Location location) {

    }

    protected void hideGpsLoader() {
        keepScreenOff();
        if (mGpsLoaderView == null) {
            return;
        }

        ViewGroup root = (ViewGroup) mGpsLoaderView.getParent();
        root.removeView(mGpsLoaderView);
        mGpsLoaderView = null;
    }

    protected File getOutputMediaFile() {
        File mediaStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "2report"
        );
//        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
//            return null;
//        }

        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timestamp + ".jpg");
    }

    protected void createAttributeViews() {
        AttributeItem prevItem = null;
        for (ChecklistAttribute attribute : checklist.getAttributeList()) {
            AttributeItem item = AttributeItem.Builder.create(this, attribute);
            if (item == null) {
                continue;
            }

            if (item instanceof IInputAttribute) {
                IInputAttribute inputItem = (IInputAttribute) item;
                try {
                    String key = inputItem.getName();
                    if (reportData != null && reportData.has(key)) {
                        Object data = reportData.get(key);
                        inputItem.setData(data);
                    }
                } catch (JSONException ex) {
                    Log.e(TAG, ex.getMessage(), ex);
                }
            }

            View itemView = item.createFieldView();
            if (itemView == null) {
                continue;
            }

            if (item instanceof FileInputAttribute) {
                ((FileInputAttribute) item).setOnFileClickListener(this);
                prepareFileAttribute((FileInputAttribute) item);
            }

            containerLayout.addView(itemView);
            prepareItemLayout(prevItem, item);
            prevItem = item;
        }

        form.setAttributeItems(attributeItems);
    }

    protected void prepareFileAttribute(FileInputAttribute item) {
        // ignored
    }

    protected int prepareReportData(AsyncTask asyncTask, boolean draft) {
        try {
            JSONObject reportData = form.serialize(BaseUpdateReportActivity.this);
            QueueReport model = new QueueReport();
            model.setProjectId(form.getProjectId());
            model.setChecklistId(checklist.getId());
            model.setChecklistName(checklist.getName());
            model.setTradeObjectId(tradeObject.getId());
            model.setTradeObjectName(tradeObject.getName());
            model.setTradeObjectAddress(tradeObject.getAddress());
            model.setData(reportData.toString());
            model.setLatitude(form.getLatitude());
            model.setLongitude(form.getLongitude());
            model.setFileList(form.getFiles());

            return DatabaseHelper.getInstance()
                    .getQueueRepository()
                    .save(model, draft);
        } catch (JSONException ex) {
            Log.e(TAG, ex.toString(), ex);
            asyncTask.cancel(false);
            return 0;
        }
    }

    //---------------------------------------------------------------------------
    //
    // ACCESSORS
    //
    //---------------------------------------------------------------------------

    @Override
    protected int getContentViewRes() {
        return R.layout.activity_report;
    }

    protected boolean isDraft() {
        return false;
    }

    private static class MyLocationListener implements LocationListener, Consumer<Location> {

        @Override
        public void onLocationChanged(Location loc) {

            String longitude = "Longitude: " + loc.getLongitude();
            Log.v(TAG, longitude);
            String latitude = "Latitude: " + loc.getLatitude();
            Log.v(TAG, latitude);


        }

        @Override
        public void onProviderDisabled(String provider) {}

        @Override
        public void onProviderEnabled(String provider) {}

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}

        @Override
        public void accept(Location location) {

        }
    }
}
