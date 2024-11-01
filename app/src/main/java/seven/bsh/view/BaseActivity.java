package seven.bsh.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;

import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import seven.bsh.Application;
import seven.bsh.LocalData;
import seven.bsh.R;
import seven.bsh.db.DatabaseHelper;
import seven.bsh.net.ApiConnector;
import seven.bsh.net.listener.OnFailureRequestListener;
import seven.bsh.net.service.BaseService;
import seven.bsh.net.service.CacheService;
import seven.bsh.view.start.StartActivity;
import seven.bsh.view.widget.overlay.NoInternetErrorView;
import seven.bsh.view.widget.overlay.ServerErrorView;
import seven.bsh.view.widget.overlay.loader.LoaderIndicatorView;

public abstract class BaseActivity extends AppCompatActivity implements
    ServerErrorView.OnServerErrorListener,
    NoInternetErrorView.OnNoInternetErrorListener,
    OnFailureRequestListener {
    private static final String TAG = "BaseActivity";

    protected AsyncTask bgTask;
    protected boolean isGlobalCachePending;

    private View mErrorView;
    private View mLoaderView;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------


    //---------------------------------------------------------------------------
    //
    // HANDLERS
    //
    //---------------------------------------------------------------------------


    @Override
    protected void onPause() {
        super.onPause();
        if (bgTask != null && !bgTask.isCancelled()) {
            bgTask.cancel(true);
        }
    }

    @Override
    public void onUpdateRequestClick() {
        onRetryRequestClick();
    }

    @Override
    public void onRetryRequestClick() {
        hideError();
        showLoader();
        getApi().refresh();
    }

    @Override
    public void onCloseErrorClick() {
        finish();
    }

    @Override
    public void onParserError() {
        onParserError(this);
    }

    @Override
    public void onNoInternetError() {
        onNoInternetError(this);
    }

    @Override
    public void onServerError() {
        onServerError(this);
    }

    @Override
    public void onTokenError() {
        // ignored
    }

    @Override
    public void onHttpUnknownError(int status, String errorBody) {
        onHttpUnknownError(status, errorBody, this);
    }

    public void onServerError(ServerErrorView.OnServerErrorListener listener) {
        hideLoader();
        ViewGroup root = getWindow().getDecorView().findViewById(android.R.id.content);
        ServerErrorView view = new ServerErrorView(this, listener);
        mErrorView = view.inflate(root);
    }

    public void onParserError(ServerErrorView.OnServerErrorListener listener) {
        hideLoader();
        onHttpUnknownError(0, null, listener);
    }

    public void onNoInternetError(NoInternetErrorView.OnNoInternetErrorListener listener) {
        hideLoader();
        ViewGroup root = getWindow().getDecorView().findViewById(android.R.id.content);
        NoInternetErrorView view = new NoInternetErrorView(this, listener);
        mErrorView = view.inflate(root);
    }

    public void onHttpUnknownError(int status, String errorBody, ServerErrorView.OnServerErrorListener listener) {
        hideLoader();
        ViewGroup root = getWindow().getDecorView().findViewById(android.R.id.content);
        ServerErrorView view = new ServerErrorView(this, listener);
        mErrorView = view.inflate(root);
    }

    protected final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int result = intent.getIntExtra(CacheService.KEY_RESULT, -1);
            switch (result) {
                case CacheService.RESULT_STATUS:
                    onResultStatus(intent);
                    break;

                case CacheService.RESULT_COMPLETED:
                    onResultCompleted();
                    break;

                case CacheService.RESULT_ERROR:
                    onResultError();
                    break;
            }
        }

        private void onResultError() {
            onGlobalCacheCompleted();
        }

        private void onResultCompleted() {
            onGlobalCacheCompleted();
        }

        private void onResultStatus(Intent intent) {
            CacheService.State state = (CacheService.State) intent.getSerializableExtra(CacheService.KEY_STATUS);
            if (state == BaseService.State.PENDING) {
                onGlobalCacheCompleted();
            } else {
                onGlobalCacheProcess();
            }
        }
    };

    protected void onGlobalCacheProcess() {
        isGlobalCachePending = false;
    }

    protected void onGlobalCacheCompleted() {
        isGlobalCachePending = true;
    }

    //---------------------------------------------------------------------------
    //
    // PUBLIC METHODS
    //
    //---------------------------------------------------------------------------

    /**
     * Скрытие попапа с ошибкой
     */
    public void hideError() {
        if (mErrorView == null) {
            return;
        }

        ViewGroup root = (ViewGroup) mErrorView.getParent();
        root.removeView(mErrorView);
        mErrorView = null;
    }

    /**
     * Показ индикатора загрузки
     */
    public void showLoader() {
        if (mLoaderView != null) {
            return;
        }

        ViewGroup root = getWindow().getDecorView().findViewById(android.R.id.content);
        LoaderIndicatorView view = new LoaderIndicatorView(this);
        mLoaderView = view.inflate(root);
    }

    /**
     * Скрытие индикатора загрузки
     */
    public void hideLoader() {
        if (mLoaderView == null) {
            return;
        }

        ViewGroup root = (ViewGroup) mLoaderView.getParent();
        root.removeView(mLoaderView);
        mLoaderView = null;
    }

    /**
     * Закрытие экранной клавиатуры
     */
    public void hideKeyboard() {
        try {
            InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(
                getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS
            );
        } catch (Exception ex) {
            // ignored
        }
    }

    public void logout() {
        getLocalData().logout();
        Intent intent = new Intent(this, StartActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        ActivityCompat.finishAffinity(this);
    }

    /**
     * Получение ассета из иконки для тулбара
     *
     * @param icon иконка из GMD
     */
    public IconicsDrawable getToolbarIcon(GoogleMaterial.Icon icon) {
        return new IconicsDrawable(this, icon)
            .sizeDp(20)
            .paddingDp(1)
            .color(Color.WHITE);
    }

    public void setMenuItemIcon(Menu menu, int id, GoogleMaterial.Icon icon) {
        MenuItem item = menu.findItem(id);
        if (item != null) {
            item.setIcon(getToolbarIcon(icon));
        }
    }

    /**
     * Добавление фрагмента в отображение
     *
     * @param cls Класс фрагмента
     * @param intoBackStack true, если надо добавить в бэк стек
     */
    public void showFragment(Class<? extends BaseFragment> cls, boolean intoBackStack) {
        showFragment(cls, null, intoBackStack);
    }

    /**
     * Добавление фрагмента в отображение
     *
     * @param cls Класс фрагмента
     * @param params Передаваемые фрагменту параметры
     * @param intoBackStack true, если надо добавить в бэк стек
     */
    public void showFragment(Class<? extends BaseFragment> cls, Bundle params, boolean intoBackStack) {
        try {
            BaseFragment fragment = cls.newInstance();
            if (params != null) {
                fragment.setArguments(params);
            }

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);
            transaction.replace(R.id.fragment_container, fragment);
            if (intoBackStack) {
                transaction.addToBackStack(null);
            }
            transaction.commit();
        } catch (IllegalAccessException | InstantiationException ex) {
            Log.e(TAG, ex.toString(), ex);
        }
    }

    /**
     * Выставление home иконки "меню"
     */
    public void setHomeAsMenuIcon() {
        setHomeAsIcon(GoogleMaterial.Icon.gmd_menu);
    }

    /**
     * Выставление home иконки "назад"
     */
    public void setHomeAsBackIcon() {
        setHomeAsIcon(GoogleMaterial.Icon.gmd_arrow_back);
    }

    public void showSimpleDialog(String text) {
        new AlertDialog.Builder(this)
            .setPositiveButton(getString(R.string.dialog_button_ok), null)
            .setMessage(text)
            .show();
    }

    public void showSimpleDialog(String text, DialogInterface.OnClickListener okClickListener) {
        new AlertDialog.Builder(this)
            .setPositiveButton(getString(R.string.dialog_button_ok), okClickListener)
            .setNegativeButton(R.string.dialog_button_cancel, null)
            .setMessage(text)
            .show();
    }

    public void showSimpleDialog(int text, DialogInterface.OnClickListener okClickListener) {
        showSimpleDialog(getString(text), okClickListener);
    }

    //---------------------------------------------------------------------------
    //
    // PRIVATE METHODS
    //
    //---------------------------------------------------------------------------

    /**
     * Указание иконки для кнопки Home
     */
    private void setHomeAsIcon(GoogleMaterial.Icon icon) {
        try {
            getSupportActionBar().setHomeAsUpIndicator(getToolbarIcon(icon));
        } catch (NullPointerException ex) {
            Log.e(TAG, ex.toString(), ex);
        }
    }

    /**
     * Конфигурация ActionBar
     */
    protected void setupToolbar() {

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED
        );
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    /**
     * Выключение автоматического отключения подсветки экрана
     */
    protected void keepScreenOn() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    /**
     * Включение автоматического отключения подсветки экрана
     */
    protected void keepScreenOff() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    /**
     * Проверка наличия прав доступа
     */
    protected boolean checkPermission(String permission) {
        return ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    protected void vibrate(int duration) {
        try {
            Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(duration);
        } catch (Exception ex) {
            // ignored
        }
    }

    //---------------------------------------------------------------------------
    //
    // ACCESSORS
    //
    //---------------------------------------------------------------------------

    protected abstract int getContentViewRes();

    /**
     * Проверка наличия интернета
     */
    public boolean hasNetwork() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public LocalData getLocalData() {
        return Application.instance().getLocalData();
    }

    public ApiConnector getApi() {
        return Application.instance().getApi();
    }

    public DatabaseHelper getDb() {
        return DatabaseHelper.getInstance();
    }
}
