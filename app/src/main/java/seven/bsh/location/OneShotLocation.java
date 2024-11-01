package seven.bsh.location;

import static com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.CancellationTokenSource;
import com.google.android.gms.tasks.Task;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

import java.util.Timer;
import java.util.TimerTask;

//import fr.quentinklein.slt.LocationTracker;
//import fr.quentinklein.slt.LocationUtils;

public class OneShotLocation {
    private static final String TAG = "OneShotLocation";
    private static final int MAX_GPS_COUNT = 4;

    private final Context mContext;
    private final CancellationTokenSource cancellationTokenSource = new CancellationTokenSource();

    private final PackageManager mPackageManager;

//    private LocationTracker mTracker;
    private OnOneShotLocationListener mListener;
    private Location mLocation;
    private Timer mTimer;
    private int mCount;
    private FusedLocationProviderClient fusedLocationClient;


    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public OneShotLocation(Context context, PackageManager packageManager) {
        mContext = context;
        mPackageManager = packageManager;
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(mContext);
        createTracker();
    }

    //---------------------------------------------------------------------------
    //
    // HANDLERS
    //
    //---------------------------------------------------------------------------

    private class LocationTimeoutTask extends TimerTask {
        @Override
        public void run() {
            if (mListener != null) {
                if (mLocation == null) {
                    mListener.onLocationTimeout();
                } else {
                    mListener.onLocationFound(mLocation);
                }
            }

//            mTracker.stopListening();
            stopTimer();
            cancel();
        }
    }

    //---------------------------------------------------------------------------
    //
    // HANDLERS
    //
    //---------------------------------------------------------------------------

    private void stopTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer.purge();
        }
    }

    //---------------------------------------------------------------------------
    //
    // PUBLIC METHODS
    //
    //---------------------------------------------------------------------------

    public void startListening() {
        try {
//            mTracker.startListening();
            if (mTimer == null) {
                mTimer = new Timer();
                // mTimer.schedule(new LocationTimeoutTask(), TIMEOUT);
            }
        } catch (SecurityException ex) {
            Log.e(TAG, ex.toString());
        }
    }

    public void stopListening() {
        try {
            stopTimer();
//            if (mTracker != null) {
//                mTracker.stopListening();
//            }
        } catch (Exception ex) {
            Log.e(TAG, ex.toString());
        }
    }

    public void createTracker() {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

//        final TrackerSettings trackerSettings = new TrackerSettings();
//        trackerSettings.setUsePassive(true);
//        trackerSettings.setUseGPS(mPackageManager.hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS));
//        trackerSettings.setUseNetwork(mPackageManager.hasSystemFeature(PackageManager.FEATURE_LOCATION_NETWORK));
//        trackerSettings.setTimeBetweenUpdates(1000);
        Task<Location> currentLocationTask = fusedLocationClient.getCurrentLocation(
                PRIORITY_HIGH_ACCURACY,
                cancellationTokenSource.getToken()
        );
        currentLocationTask.addOnCompleteListener((task -> {
                    FirebaseCrashlytics.getInstance().log("location found");
                    String result = "";
                    if (task.isSuccessful() && task.getResult() != null) {
                        if (mListener != null) {
                            mListener.onLocationFound(task.getResult());
                        }
                    } else {
                        mListener.onLocationTimeout();
                    }

                    Log.d(TAG, "getCurrentLocation() result: " + result);
                })
        );
    }
    //---------------------------------------------------------------------------
    //
    // ACCESSORS
    //
    //---------------------------------------------------------------------------

    public void setListener(OnOneShotLocationListener listener) {
        mListener = listener;
    }

//    public boolean isAvailable() {
//        return mTracker != null;
//    }
//
//    public boolean isProvidersEnabled() {
//        if (mPackageManager.hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS)) {
//            return LocationUtils.isGpsProviderEnabled(mContext);
//        }
//        return LocationUtils.isNetworkProviderEnabled(mContext);
//    }

    //---------------------------------------------------------------------------
    //
    // INTERFACES
    //
    //---------------------------------------------------------------------------

    public interface OnOneShotLocationListener {
        void onLocationFound(@NonNull Location location);

        void onLocationTimeout();
    }
}
