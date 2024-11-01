package seven.bsh.net.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import androidx.annotation.Nullable;

import seven.bsh.Application;
import seven.bsh.LocalData;
import seven.bsh.db.DatabaseHelper;
import seven.bsh.net.ApiConnector;

public abstract class BaseService extends Service {
    public static final String KEY_COMMAND = "command";
    public static final String KEY_RESULT = "result";

    public static final int COMMAND_NONE = -1;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    @Override
    public void onCreate() {
        super.onCreate();
    }

    //---------------------------------------------------------------------------
    //
    // HANDLERS
    //
    //---------------------------------------------------------------------------

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    //---------------------------------------------------------------------------
    //
    // ACCESSORS
    //
    //---------------------------------------------------------------------------

    protected DatabaseHelper getDb() {
        return DatabaseHelper.getInstance();
    }

    protected LocalData getLocalData() {
        return Application.instance().getLocalData();
    }

    protected ApiConnector getApi() {
        return Application.instance().getApi();
    }

    //---------------------------------------------------------------------------
    //
    // ENUMS
    //
    //---------------------------------------------------------------------------

    public enum State {
        PENDING,
        PROCESSING,
    }
}