package seven.bsh.net.cache;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

import seven.bsh.db.entity.Sku;
import seven.bsh.net.listener.OnSkuGetRequestListener;
import seven.bsh.net.request.SkuGetRequest;
import seven.bsh.net.response.RefreshTokenResponse;
import seven.bsh.net.response.SkuGetResponse;
import seven.bsh.utils.Formatter;

public class SkuCache extends BaseCache implements OnSkuGetRequestListener.OnRequestListener {
    private static final String TAG = "SkuCache";

    private String mLastUpdate;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public SkuCache(Context context, OnCacheListener listener) {
        super(context, listener);
    }

    //---------------------------------------------------------------------------
    //
    // HANDLERS
    //
    //---------------------------------------------------------------------------

    class PrepareSkuTask extends AsyncTask<Void, Void, Void> {
        private final List<Sku> mSkus;
        private Exception mException;

        PrepareSkuTask(SkuGetResponse response) {
            pagination = response.getPagination();
            mSkus = response.getSkus();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                if (mSkus != null && !mSkus.isEmpty()) {
                    getDb().getSkuRepository().saveAll(mSkus);
                    hasData = true;
                }
            } catch (NullPointerException ex) {
                cancel(false);
                mException = ex;
                Log.d(TAG, ex.getMessage(), ex);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (isCancelled() && mException == null) {
                isLoading = false;
                return;
            }

            try {
                if (mException != null) {
                    listener.onCacheError(SkuCache.this, mException);
                    isLoading = false;
                } else if (mSkus == null || mSkus.isEmpty() || !pagination.hasNextPage()) {
                    listener.onCacheCompleted(SkuCache.this, hasData);
                    isLoading = false;
                } else {
                    listener.onCacheProgress(SkuCache.this, pagination);
                    sendSkuGetRequest(pagination.getNextPage(), mLastUpdate);
                }
            } catch (NullPointerException ex) {
                Log.d(TAG, ex.getMessage(), ex);
            }
        }
    }

    @Override
    public void onSkuGetSuccess(SkuGetResponse response) {
        pagination = response.getPagination();
        bgTask = new PrepareSkuTask(response).execute();
    }

    @Override
    public void onAuthSuccess(RefreshTokenResponse response) {
        String token = response.getToken();
        getLocalData().saveToken(token, response.getRefreshToken());
        if (pagination != null && pagination.hasNextPage()) {
            sendSkuGetRequest(token, pagination.getNextPage(), mLastUpdate);
        } else {
            sendSkuGetRequest(token, 1, mLastUpdate);
        }
    }

    //---------------------------------------------------------------------------
    //
    // PRIVATE METHODS
    //
    //---------------------------------------------------------------------------

    private void sendSkuGetRequest(String token, int page, String date) {
        SkuGetRequest request = new SkuGetRequest(token, page, date);
        getApi().getSku(request, new OnSkuGetRequestListener(this));
    }

    private void sendSkuGetRequest(int page, String date) {
        String token = getLocalData().getAccessToken();
        sendSkuGetRequest(token, page, date);
    }

    private String getLastUpdate() {
        String temp = getLocalData().getSkuCacheLastUpdate();
        if (temp != null) {
            return Formatter.convertDateString(temp, "dd.MM.yyyy HH:mm:ss", "yyyy.MM.dd HH:mm:ss");
        }
        return null;
    }

    //---------------------------------------------------------------------------
    //
    // PUBLIC METHODS
    //
    //---------------------------------------------------------------------------

    public void createCache() {
        if (!isLoading) {
            super.createCache();
            mLastUpdate = getLastUpdate();
            sendSkuGetRequest(1, mLastUpdate);
        }
    }
}
