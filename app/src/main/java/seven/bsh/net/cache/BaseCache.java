package seven.bsh.net.cache;

import android.content.Context;
import android.os.AsyncTask;

import seven.bsh.Application;
import seven.bsh.LocalData;
import seven.bsh.db.DatabaseHelper;
import seven.bsh.model.Pagination;
import seven.bsh.net.ApiConnector;
import seven.bsh.net.listener.OnFailureRequestListener;
import seven.bsh.net.listener.OnRefreshTokenRequestListener;
import seven.bsh.net.request.RefreshTokenRequest;

public abstract class BaseCache implements OnRefreshTokenRequestListener.OnRequestListener {
    protected final Context context;
    protected final OnCacheListener listener;
    protected Pagination pagination;
    protected AsyncTask bgTask;
    protected boolean isLoading;
    protected boolean hasData;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public BaseCache(Context context, OnCacheListener listener) {
        this.context = context;
        this.listener = listener;
    }

    //---------------------------------------------------------------------------
    //
    // HANDLERS
    //
    //---------------------------------------------------------------------------

    public void onParserError() {
        isLoading = false;
        listener.onParserError();
    }

    public void onHttpUnknownError(int status, String errorBody) {
        isLoading = false;
        listener.onHttpUnknownError(status, errorBody);
    }

    public void onNoInternetError() {
        isLoading = false;
        listener.onNoInternetError();
    }

    public void onServerError() {
        isLoading = false;
        listener.onServerError();
    }

    @Override
    public void onTokenError() {
        sendRequestAuth();
    }

    @Override
    public void onAuthFailed() {
        onServerError();
    }

    //---------------------------------------------------------------------------
    //
    // PRIVATE METHODS
    //
    //---------------------------------------------------------------------------

    protected void sendRequestAuth() {
        String refreshToken = getLocalData().getRefreshToken();
        RefreshTokenRequest request = new RefreshTokenRequest(refreshToken);
        getApi().refreshToken(request, new OnRefreshTokenRequestListener(this));
    }

    //---------------------------------------------------------------------------
    //
    // PUBLIC METHODS
    //
    //---------------------------------------------------------------------------

    public void createCache() {
        if (!isLoading) {
            hasData = false;
            isLoading = true;
            pagination = null;
        }
    }

    public void stop() {
        if (bgTask != null && !bgTask.isCancelled()) {
            bgTask.cancel(false);
            bgTask = null;
        }
    }

    //---------------------------------------------------------------------------
    //
    // ACCESSORS
    //
    //---------------------------------------------------------------------------

    public LocalData getLocalData() {
        return Application.instance().getLocalData();
    }

    protected DatabaseHelper getDb() {
        return DatabaseHelper.getInstance();
    }

    protected ApiConnector getApi() {
        return ((Application) context.getApplicationContext()).getApi();
    }

    //---------------------------------------------------------------------------
    //
    // INTERFACES
    //
    //---------------------------------------------------------------------------

    public interface OnCacheListener extends OnFailureRequestListener {
        void onCacheCompleted(BaseCache sender, boolean hasData);

        void onCacheProgress(BaseCache sender, Pagination pagination);

        void onCacheError(BaseCache sender, Exception exception);
    }
}
