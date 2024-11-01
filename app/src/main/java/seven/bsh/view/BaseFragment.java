package seven.bsh.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;

import seven.bsh.Application;
import seven.bsh.LocalData;
import seven.bsh.R;
import seven.bsh.net.ApiConnector;

public class BaseFragment extends Fragment {
    protected LocalBroadcastManager localBroadcastManager;
    protected AsyncTask bgTask;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        localBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    //---------------------------------------------------------------------------
    //
    // HANDLERS
    //
    //---------------------------------------------------------------------------

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return false;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause() {
        super.onPause();
        localBroadcastManager.unregisterReceiver(backReceiver);
        if (bgTask != null && !bgTask.isCancelled()) {
            bgTask.cancel(true);
        }
    }

    public void onServerError() {
        Context context = getContext();
        if (context != null) {
            hideLoader();
            showSimpleDialog(getContext().getString(R.string.network_error_500));
        }
    }

    public void onHttpUnknownError(int status, String errorBody) {
        Context context = getContext();
        if (context != null) {
            hideLoader();
            showSimpleDialog(getContext().getString(R.string.network_error_unknown, status));
        }
    }

    public void onParserError() {
        Context context = getContext();
        if (context != null) {
            showSimpleDialog(getContext().getString(R.string.network_error_parse));
        }
    }

    public void onNoInternetError() {
        Context context = getContext();
        if (context != null) {
            hideLoader();
            showSimpleDialog(getContext().getString(R.string.network_error_no_internet));
        }
    }

    protected final BroadcastReceiver backReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            onBackPressed();
        }
    };

    protected void onBackPressed() {
        FragmentActivity activity = getActivity();
        if (activity == null) {
            return;
        }

        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        } else {
            activity.finish();
        }
    }

    //---------------------------------------------------------------------------
    //
    // PRIVATE METHODS
    //
    //---------------------------------------------------------------------------

    protected void showSimpleDialog(String text) {
        BaseActivity activity = (BaseActivity) getActivity();
        if (activity != null) {
            activity.showSimpleDialog(text);
        }
    }

    protected void showSimpleDialog(String text, DialogInterface.OnClickListener listener) {
        BaseActivity activity = (BaseActivity) getActivity();
        if (activity != null) {
            activity.showSimpleDialog(text, listener);
        }
    }

    protected void setMenuItemIcon(Menu menu, int id, GoogleMaterial.Icon icon) {
        BaseActivity activity = (BaseActivity) getActivity();
        if (activity != null) {
            activity.setMenuItemIcon(menu, id, icon);
        }
    }

    protected boolean checkPermission(String permission) {
        return ActivityCompat.checkSelfPermission(getActivity(), permission) == PackageManager.PERMISSION_GRANTED;
    }

    protected void showPermissionSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getContext().getPackageName()));
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    protected void showLoader() {
        BaseActivity activity = getBaseActivity();
        if (activity != null) {
            activity.showLoader();
        }
    }

    protected void hideLoader() {
        BaseActivity activity = getBaseActivity();
        if (activity != null) {
            activity.hideLoader();
        }
    }

    //---------------------------------------------------------------------------
    //
    // ACCESSORS
    //
    //---------------------------------------------------------------------------

    protected BaseActivity getBaseActivity() {
        return (BaseActivity) getActivity();
    }

    public LocalData getLocalData() {
        return Application.instance().getLocalData();
    }

    public ApiConnector getApi() {
        BaseActivity activity = getBaseActivity();
        if (activity == null) {
            return null;
        }
        return activity.getApi();
    }
}
