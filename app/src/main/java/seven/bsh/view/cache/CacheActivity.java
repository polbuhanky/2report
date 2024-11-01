package seven.bsh.view.cache;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import seven.bsh.R;
import seven.bsh.db.DatabaseHelper;
import seven.bsh.net.service.BaseService;
import seven.bsh.net.service.CacheService;
import seven.bsh.view.SideMenuActivity;
import seven.bsh.view.widget.block.CacheBlock;
import seven.bsh.view.widget.sideMenu.SideMenuItem;

public class CacheActivity extends SideMenuActivity implements CacheBlock.OnCacheBlockListener {
    private static final String TAG = "CacheFragment";

    private CacheBlock mCacheBlock;
    private CacheBlock mSkuCacheBlock;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cache);
        init();
        setTitle(getString(R.string.activity_cache_title));
        getSideMenu().setActive(SideMenuItem.Id.CACHE);

        String cacheLastUpdate = getLocalData().getCommonCacheLastUpdate();
        mCacheBlock = findViewById(R.id.cache_block);
        mCacheBlock.setLastUpdate(cacheLastUpdate);
        mCacheBlock.setListener(this);

        String skuCacheLastUpdate = getLocalData().getSkuCacheLastUpdate();
        mSkuCacheBlock = findViewById(R.id.cache_sku_block);
        mSkuCacheBlock.setLastUpdate(skuCacheLastUpdate);
        mSkuCacheBlock.setListener(this);

        startService(CacheService.COMMAND_NONE);
    }

    //---------------------------------------------------------------------------
    //
    // HANDLERS
    //
    //---------------------------------------------------------------------------

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter(CacheService.BROADCAST_ID);
        registerReceiver(receiver, intentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onGetCacheBtn(CacheBlock view) {
        if (view == mCacheBlock) {
            sendGetCacheCommand(CacheService.COMMAND_CACHE, view);
        } else {
            sendGetCacheCommand(CacheService.COMMAND_SKU_CACHE, view);
        }
    }

    @Override
    public void onClearCacheBtn(CacheBlock view) {
        enable(false);
        bgTask = new ClearCacheTask(view).execute();
    }

    private class ClearCacheTask extends AsyncTask<Void, Void, Void> {
        private final CacheBlock cacheBlock;

        public ClearCacheTask(CacheBlock cacheBlock) {
            this.cacheBlock = cacheBlock;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                if (cacheBlock == mCacheBlock) {
                    DatabaseHelper.getInstance()
                        .getChecklistRepository()
                        .deleteAllRelations();
                    DatabaseHelper.getInstance()
                        .getChecklistRepository()
                        .deleteAll();
                    DatabaseHelper.getInstance()
                        .getTradeObjectRepository()
                        .deleteAll();
                    DatabaseHelper.getInstance()
                        .getReportRepository()
                        .deleteAll();
                    getLocalData().clearCommonCache();
                } else {
                    DatabaseHelper.getInstance()
                        .getSkuRepository()
                        .deleteAll();
                    getLocalData().clearSkuCache();
                }
            } catch (Exception ex) {
                Log.e(TAG, ex.toString(), ex);
                cancel(false);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                if (isCancelled()) {
                    return;
                }

                cacheBlock.setLastUpdate(null);
                Toast.makeText(CacheActivity.this, "Кэш очищен", Toast.LENGTH_SHORT).show();
                enable(true);
            } catch (Exception ex) {
                Log.e(TAG, ex.toString(), ex);
                cancel(false);
            }
        }
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int result = intent.getIntExtra(CacheService.KEY_RESULT, -1);
            int type = intent.getIntExtra(CacheService.KEY_TYPE, -1);
            switch (result) {
                case CacheService.RESULT_STATUS:
                    onResultStatus(intent, type);
                    break;

                case CacheService.RESULT_COMPLETED:
                    onResultCompleted(type);
                    break;

                case CacheService.RESULT_ERROR:
                    onResultError(intent, type);
                    break;
            }
        }

        private void onResultError(Intent intent, int type) {
            int errorCode = intent.getIntExtra(CacheService.KEY_ERROR, -1);
            if (errorCode == CacheService.ERROR_AUTH) {
                logout();
            }

            if (type == CacheService.TYPE_CACHE_COMMON) {
                mCacheBlock.setProgress(0);
                mCacheBlock.setError(errorCode);
            } else {
                mSkuCacheBlock.setProgress(0);
                mSkuCacheBlock.setError(errorCode);
            }
            enable(true);
        }

        private void onResultCompleted(int type) {
            if (type == CacheService.TYPE_CACHE_COMMON) {
                String cacheLastUpdate = getLocalData().getCommonCacheLastUpdate();
                mCacheBlock.setLastUpdate(cacheLastUpdate);
                mCacheBlock.setProgress(0);
            } else {
                String cacheLastUpdate = getLocalData().getSkuCacheLastUpdate();
                mSkuCacheBlock.setLastUpdate(cacheLastUpdate);
                mSkuCacheBlock.setProgress(0);
            }
            enable(true);
        }

        private void onResultStatus(Intent intent, int type) {
            CacheService.State state = (CacheService.State) intent.getSerializableExtra(CacheService.KEY_STATUS);
            if (state == BaseService.State.PROCESSING) {
                int progress = intent.getIntExtra(CacheService.KEY_PROGRESS, 0);
                int currentCount = intent.getIntExtra(CacheService.KEY_CURRENT_COUNT, 0);
                int totalCount = intent.getIntExtra(CacheService.KEY_TOTAL_COUNT, 0);

                if (type == CacheService.TYPE_CACHE_COMMON) {
                    mCacheBlock.setStatus(currentCount, totalCount);
                    mCacheBlock.setProgress(progress);
                } else {
                    mSkuCacheBlock.setStatus(currentCount, totalCount);
                    mSkuCacheBlock.setProgress(progress);
                }
            } else {
                enable(true);
            }
        }
    };

    //---------------------------------------------------------------------------
    //
    // PRIVATE METHODS
    //
    //---------------------------------------------------------------------------

    private void enable(boolean enabled) {
        mCacheBlock.setEnabled(enabled);
        mSkuCacheBlock.setEnabled(enabled);
    }

    private void startService(int command) {
        Intent intent = new Intent(this, CacheService.class);
        intent.putExtra(CacheService.KEY_COMMAND, command);
        startService(intent);
    }

    private void sendGetCacheCommand(int command, CacheBlock cacheBlock) {
        enable(false);
        startService(command);
        cacheBlock.setStatus(0, 0);
    }

    //---------------------------------------------------------------------------
    //
    // ACCESSORS
    //
    //---------------------------------------------------------------------------

    @Override
    protected int getContentViewRes() {
        return R.layout.activity_cache;
    }
}
