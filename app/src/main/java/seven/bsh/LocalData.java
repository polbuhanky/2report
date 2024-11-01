package seven.bsh;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class LocalData {
    private static final String LAST_DIR_PATH = "last_dir_path";
    private static final String LAST_DIR_NESTING = "last_dir_nesting";
    private static final String HOST = "host";
    private static final String LOGIN = "login";
    private static final String PASSWORD = "password";
    private static final String ENVIRONMENT_ID = "environment_id";
    private static final String REMEMBER_LOGIN = "remember_login";
    private static final String REFRESH_TOKEN = "refresh_token";
    private static final String ACCESS_TOKEN = "token";
    private static final String IS_LOGIN = "is_login";
    private static final String IS_DEMO = "is_demo";
    private static final String IMAGE_MAX_SIZE = "image_max_size";
    private static final String COMMON_CACHE_LAST_UPDATE = "cache_last_update";
    private static final String SKU_CACHE_LAST_UPDATE = "cache_sku_last_update";
    private static final String USER_ID = "user_id";
    private static final String USER_NAME = "user_name";
    private static final String USER_REGISTERED_AT = "registered_at";
    private static final String PROJECT_ID = "project_id";
    private static final String PROJECT_NAME = "project_name";

    private SharedPreferences mPreferences;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public LocalData(Application application) {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(application);
    }

    //---------------------------------------------------------------------------
    //
    // PUBLIC METHODS
    //
    //---------------------------------------------------------------------------

    public void saveEnvironmentData(String host, String projectId, String projectName) {
        mPreferences.edit()
            .putString(HOST, host)
            .putString(PROJECT_ID, projectId)
            .putString(PROJECT_NAME, projectName)
            .apply();
    }

    public void saveLoginData(String login, String password, String environmentId, boolean rememberMe) {
        mPreferences.edit()
            .putString(LOGIN, login)
            .putString(PASSWORD, password)
            .putString(ENVIRONMENT_ID, environmentId)
            .putBoolean(REMEMBER_LOGIN, rememberMe)
            .apply();
    }

    public void saveToken(String token, String refreshToken) {
        mPreferences.edit()
            .putString(ACCESS_TOKEN, token)
            .putString(REFRESH_TOKEN, refreshToken)
            .apply();
    }

    public void logout() {
        mPreferences.edit()
            .putBoolean(IS_LOGIN, false)
            .remove(REFRESH_TOKEN)
            .remove(ACCESS_TOKEN)
            .remove(COMMON_CACHE_LAST_UPDATE)
            .remove(SKU_CACHE_LAST_UPDATE)
            .remove(IMAGE_MAX_SIZE)
            .remove(IS_DEMO)
            .remove(HOST)
            .apply();
    }

    public void saveUser(String name, String id, String registeredAt) {
        mPreferences.edit()
            .putBoolean(IS_LOGIN, true)
            .putString(USER_NAME, name)
            .putString(USER_ID, id)
            .putString(USER_REGISTERED_AT, registeredAt)
            .apply();


    }

    public boolean hasCommonCache() {
        return mPreferences.contains(COMMON_CACHE_LAST_UPDATE);
    }

    public void clearCommonCache() {
        mPreferences.edit()
            .remove(COMMON_CACHE_LAST_UPDATE)
            .apply();
    }

    public void clearSkuCache() {
        mPreferences.edit()
            .remove(SKU_CACHE_LAST_UPDATE)
            .apply();
    }

    public void clearLastDir() {
        mPreferences.edit()
            .remove(LAST_DIR_NESTING)
            .remove(LAST_DIR_PATH)
            .apply();
    }

    //---------------------------------------------------------------------------
    //
    // ACCESSORS
    //
    //---------------------------------------------------------------------------

    public boolean isDemo() {
        return mPreferences.getBoolean(IS_DEMO, false);
    }

    public boolean isLogin() {
        return mPreferences.getBoolean(IS_LOGIN, false);
    }

    public String getHost() {
        return mPreferences.getString(HOST, null);
    }

    public String getLogin() {
        return mPreferences.getString(LOGIN, null);
    }

    public String getPassword() {
        return mPreferences.getString(PASSWORD, "");
    }

    public String getEnvironmentId() {
        return mPreferences.getString(ENVIRONMENT_ID, null);
    }

    public boolean isRememberLogin() {
        return mPreferences.getBoolean(REMEMBER_LOGIN, false);
    }

    public String getUserId() {
        return mPreferences.getString(USER_ID, "");
    }

    public String getUserName() {
        return mPreferences.getString(USER_NAME, "");
    }

    public String getUserRegisteredAt() {
        return mPreferences.getString(USER_REGISTERED_AT, "");
    }

    public String getProjectId() {
        return mPreferences.getString(PROJECT_ID, "");
    }

    public String getProjectName() {
        return mPreferences.getString(PROJECT_NAME, "");
    }

    public String getAccessToken() {
        return mPreferences.getString(ACCESS_TOKEN, "");
    }

    public String getRefreshToken() {
        return mPreferences.getString(REFRESH_TOKEN, "");
    }

    public long getImageSize() {
        return mPreferences.getLong(LocalData.IMAGE_MAX_SIZE, 0);
    }

    public void setImageMaxSize(int imageMaxSize) {
        mPreferences.edit()
            .putLong(IMAGE_MAX_SIZE, imageMaxSize)
            .apply();
    }

    public String getCommonCacheLastUpdate() {
        return mPreferences.getString(COMMON_CACHE_LAST_UPDATE, null);
    }

    public void setCommonCacheLastUpdate(String lastUpdate) {
        mPreferences.edit()
            .putString(COMMON_CACHE_LAST_UPDATE, lastUpdate)
            .apply();
    }

    public String getSkuCacheLastUpdate() {
        return mPreferences.getString(SKU_CACHE_LAST_UPDATE, null);
    }

    public void setSkuCacheLastUpdate(String lastUpdate) {
        mPreferences.edit()
            .putString(SKU_CACHE_LAST_UPDATE, lastUpdate)
            .apply();
    }

    public int getLastDirNesting() {
        return mPreferences.getInt(LAST_DIR_NESTING, 0);
    }

    public void setLastDirNesting(int nesting) {
        mPreferences.edit()
            .putInt(LAST_DIR_NESTING, nesting)
            .apply();
    }

    public String getLastDirPath() {
        return mPreferences.getString(LAST_DIR_PATH, null);
    }

    public void setLastDirPath(String path) {
        mPreferences.edit()
            .putString(LAST_DIR_PATH, path)
            .apply();
    }
}
