package seven.bsh;

import android.content.Context;
import androidx.multidex.MultiDex;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

import net.danlew.android.joda.JodaTimeAndroid;

import io.reactivex.rxjava3.plugins.RxJavaPlugins;
import seven.bsh.net.ApiConnector;

public class Application extends android.app.Application {
    private static Application sInstance;
    private LocalData mLocalData;
    private ApiConnector mApi;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    @Override
    public final void onCreate() {
        super.onCreate();
        sInstance = this;
        JodaTimeAndroid.init(this);
        FlowManager.init(new FlowConfig.Builder(this).build());
        mLocalData = new LocalData(this);
        mApi = new ApiConnector(this);
        mApi.init(mLocalData.getHost());
        RxJavaPlugins.setErrorHandler(Throwable::printStackTrace); // nothing or some logging

    }

    //---------------------------------------------------------------------------
    //
    // PUBLIC METHODS
    //
    //---------------------------------------------------------------------------

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }



    //---------------------------------------------------------------------------
    //
    // ACCESSORS
    //
    //---------------------------------------------------------------------------

    public ApiConnector getApi() {
        return mApi;
    }

    public LocalData getLocalData() {
        return mLocalData;
    }

    public static Application instance() {
        return sInstance;
    }
}
