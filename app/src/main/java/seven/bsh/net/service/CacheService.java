package seven.bsh.net.service;

import android.content.Intent;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import seven.bsh.model.Pagination;
import seven.bsh.net.cache.BaseCache;
import seven.bsh.net.cache.SkuCache;
import seven.bsh.net.cache.TradeObjectsCache;

public class CacheService extends BaseService implements BaseCache.OnCacheListener {
    public static final String BROADCAST_ID = "cache";
    public static final String KEY_STATUS = "status";
    public static final String KEY_PROGRESS = "progress";
    public static final String KEY_CURRENT_COUNT = "current_count";
    public static final String KEY_TOTAL_COUNT = "total_count";
    public static final String KEY_TYPE = "type";
    public static final String KEY_ERROR = "error";

    public static final int TYPE_CACHE_COMMON = 0;
    public static final int TYPE_CACHE_SKU = 1;

    public static final int COMMAND_CACHE = 0;
    public static final int COMMAND_STOP = 2;
    public static final int COMMAND_SKU_CACHE = 10;

    public static final int RESULT_STATUS = 0;
    public static final int RESULT_COMPLETED = 1;
    public static final int RESULT_ERROR = 2;

    public static final int ERROR_500 = 0;
    public static final int ERROR_PARSER = 1;
    public static final int ERROR_UNKNOWN = 2;
    public static final int ERROR_AUTH = 3;
    public static final int ERROR_NO_INTERNET = 4;

    private State state = State.PENDING;
    private TradeObjectsCache tradeObjectsCache;
    private SkuCache skuCache;
    private Pagination currentPagination;
    private BaseCache currentCache;
    private int type;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    @Override
    public void onCreate() {
        super.onCreate();
        tradeObjectsCache = new TradeObjectsCache(this, this);
        skuCache = new SkuCache(this, this);
    }

    //---------------------------------------------------------------------------
    //
    // HANDLERS
    //
    //---------------------------------------------------------------------------

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int command = intent.getIntExtra(KEY_COMMAND, COMMAND_NONE);
        switch (command) {
            case COMMAND_NONE:
                onCommandNone();
                break;

            case COMMAND_CACHE:
                onCommandCache();
                break;

            case COMMAND_SKU_CACHE:
                onCommandSkuCache();
                break;

            case COMMAND_STOP:
                onCommandStop();
                break;
        }
        return START_REDELIVER_INTENT;
    }

    private void onCommandNone() {
        Intent intent = new Intent(BROADCAST_ID);
        intent.putExtra(KEY_RESULT, RESULT_STATUS);
        intent.putExtra(KEY_STATUS, state);

        if (state == State.PROCESSING) {
            addProgressToIntent(intent, currentCache, currentPagination);
        }
        sendBroadcast(intent);
    }

    private void onCommandCache() {
        if (state != State.PENDING) {
            return;
        }

        state = State.PROCESSING;
        type = TYPE_CACHE_COMMON;
        tradeObjectsCache.createCache();
        currentCache = tradeObjectsCache;
        currentPagination = null;
    }

    private void onCommandSkuCache() {
        if (state != State.PENDING) {
            return;
        }

        state = State.PROCESSING;
        type = TYPE_CACHE_SKU;
        skuCache.createCache();
        currentCache = skuCache;
        currentPagination = null;
    }

    private void onCommandStop() {
        if (state != State.PENDING) {
            tradeObjectsCache.stop();
        }
    }

    @Override
    public void onServerError() {
        sendBroadcastError(type, ERROR_500);
    }

    @Override
    public void onParserError() {
        sendBroadcastError(type, ERROR_PARSER);
    }

    @Override
    public void onNoInternetError() {
        sendBroadcastError(type, ERROR_NO_INTERNET);
    }

    @Override
    public void onHttpUnknownError(int status, String errorBody) {
        sendBroadcastError(type, ERROR_UNKNOWN);
    }

    /*@Override
    public void onAuthFailedError() {
        sendBroadcastError(type, ERROR_AUTH);
    }

    @Override
    public void onAuth422Error(String error) {
        sendBroadcastError(type, ERROR_AUTH);
    }*/

    @Override
    public void onTokenError() {
        // TODO implemented
    }

    @Override
    public void onCacheCompleted(BaseCache sender, boolean hasData) {
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault());
        String lastUpdate = dateFormat.format(now);

        if (type == TYPE_CACHE_COMMON) {
            getLocalData().setCommonCacheLastUpdate(lastUpdate);
        } else {
            getLocalData().setSkuCacheLastUpdate(lastUpdate);
        }

        state = State.PENDING;
        sendBroadcastCompleted(type);
    }

    @Override
    public void onCacheProgress(BaseCache sender, Pagination pagination) {
        currentPagination = pagination;
        Intent intent = new Intent(BROADCAST_ID);
        intent.putExtra(KEY_RESULT, RESULT_STATUS);
        intent.putExtra(KEY_STATUS, State.PROCESSING);
        intent.putExtra(KEY_TYPE, type);
        addProgressToIntent(intent, sender, pagination);
        sendBroadcast(intent);
    }

    @Override
    public void onCacheError(BaseCache sender, Exception exception) {
        state = State.PENDING;
    }

    //---------------------------------------------------------------------------
    //
    // PRIVATE METHODS
    //
    //---------------------------------------------------------------------------

    private void sendBroadcastError(int type, int errorCode) {
        state = State.PENDING;
        Intent intent = new Intent(BROADCAST_ID);
        intent.putExtra(KEY_RESULT, RESULT_ERROR);
        intent.putExtra(KEY_TYPE, type);
        intent.putExtra(KEY_ERROR, errorCode);
        sendBroadcast(intent);
    }

    private void sendBroadcastCompleted(int type) {
        Intent intent = new Intent(BROADCAST_ID);
        intent.putExtra(KEY_RESULT, RESULT_COMPLETED);
        intent.putExtra(KEY_TYPE, type);
        sendBroadcast(intent);
    }

    private void addProgressToIntent(Intent intent, BaseCache cache, Pagination pagination) {
        if (cache == null || pagination == null) {
            return;
        }

        intent.putExtra(KEY_CURRENT_COUNT, pagination.getCurrentPage() * pagination.getPerPage());
        intent.putExtra(KEY_TOTAL_COUNT, pagination.getTotalCount());
        intent.putExtra(KEY_TYPE, type);
        float k = (float) pagination.getCurrentPage() / (float) pagination.getPageCount();
        intent.putExtra(KEY_PROGRESS, (int) (k * 100));
    }
}
