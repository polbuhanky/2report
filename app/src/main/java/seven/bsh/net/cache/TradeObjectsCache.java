package seven.bsh.net.cache;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

import seven.bsh.db.entity.Checklist;
import seven.bsh.db.entity.Project;
import seven.bsh.db.entity.TradeObject;
import seven.bsh.net.listener.OnRefreshTokenRequestListener;
import seven.bsh.net.listener.OnTradeObjectsGetRequestListener;
import seven.bsh.net.request.TradeObjectsGetRequest;
import seven.bsh.net.response.RefreshTokenResponse;
import seven.bsh.net.response.TradeObjectsGetResponse;

public class TradeObjectsCache extends BaseCache implements
    OnTradeObjectsGetRequestListener.OnRequestListener,
    OnRefreshTokenRequestListener.OnRequestListener {
    private static final String TAG = "TradeObjectsCache";

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public TradeObjectsCache(Context context, OnCacheListener listener) {
        super(context, listener);
    }

    //---------------------------------------------------------------------------
    //
    // HANDLERS
    //
    //---------------------------------------------------------------------------

    @SuppressLint("StaticFieldLeak")
    class PrepareTradeObjectsTask extends AsyncTask<Void, Void, Void> {
        private final List<TradeObject> mTradeObjects;
        private final List<Checklist> mChecklists;
        private final List<Project> mProjects;
        private final List<TradeObjectsGetResponse.ChecklistRelation> mRelations;

        PrepareTradeObjectsTask(TradeObjectsGetResponse response) {
            mTradeObjects = response.getTradeObjects();
            mChecklists = response.getChecklists();
            mRelations = response.getRelations();
            mProjects = response.getProjects();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                if (pagination.isFirstPage()) {
                    getDb().getProjectRepository().deleteAll();
                    getDb().getTradeObjectRepository().deleteAll();
                    getDb().getChecklistRepository().deleteAllRelations();
                    getDb().getChecklistRepository().deleteAll();
                }

                if (mTradeObjects != null && !mTradeObjects.isEmpty()) {
                    getDb().getProjectRepository().saveAll(mProjects);
                    getDb().getTradeObjectRepository().saveAll(mTradeObjects);
                    getDb().getChecklistRepository().saveAll(mChecklists);
                    getDb().getChecklistRepository().saveRelations(mRelations);
                    hasData = true;
                }
            } catch (NullPointerException ex) {
                cancel(false);
                Log.d(TAG, ex.getMessage(), ex);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (isCancelled()) {
                isLoading = false;
                return;
            }

            try {
                if (mTradeObjects == null || mTradeObjects.isEmpty() || !pagination.hasNextPage()) {
                    listener.onCacheCompleted(TradeObjectsCache.this, hasData);
                    isLoading = false;
                } else {
                    listener.onCacheProgress(TradeObjectsCache.this, pagination);
                    sendTradeObjectsGetRequest(pagination.getNextPage());
                }
            } catch (NullPointerException ex) {
                Log.d(TAG, ex.getMessage(), ex);
            }
        }
    }

    @Override
    public void onAuthSuccess(RefreshTokenResponse response) {
        String token = response.getToken();
        getLocalData().saveToken(token, response.getRefreshToken());
        if (pagination != null && pagination.hasNextPage()) {
            sendTradeObjectsGetRequest(token, pagination.getNextPage());
        } else {
            sendTradeObjectsGetRequest(token, 1);
        }
    }

    @Override
    public void onTradeObjectsGetSuccess(TradeObjectsGetResponse response) {
        pagination = response.getPagination();
        bgTask = new PrepareTradeObjectsTask(response).execute();
    }

    //---------------------------------------------------------------------------
    //
    // PRIVATE METHODS
    //
    //---------------------------------------------------------------------------

    private void sendTradeObjectsGetRequest(String token, int page) {
        TradeObjectsGetRequest request = new TradeObjectsGetRequest(token, page);
        getApi().getTradeObjects(request, new OnTradeObjectsGetRequestListener(this));
    }

    private void sendTradeObjectsGetRequest(int page) {
        String token = getLocalData().getAccessToken();
        sendTradeObjectsGetRequest(token, page);
    }

    //---------------------------------------------------------------------------
    //
    // PUBLIC METHODS
    //
    //---------------------------------------------------------------------------

    public void createCache() {
        if (!isLoading) {
            super.createCache();
            sendTradeObjectsGetRequest(1);
        }
    }
}
