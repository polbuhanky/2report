package seven.bsh.net;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;

import java.io.IOException;
import java.util.Locale;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import seven.bsh.BuildConfig;

public class UserAgentInterceptor implements Interceptor {
    private Context mContext;

    public UserAgentInterceptor(Context context) {
        mContext = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request requestWithUserAgent = request.newBuilder()
            .header("User-Agent", getUserAgent())
            .build();
        return chain.proceed(requestWithUserAgent);
    }

    private String getUserAgent() {
        String str = String.format(
            Locale.getDefault(),
            "Android = %s (%d), Device = %s (%s), AppVersion = %s (%d)",
            Build.VERSION.RELEASE,
            Build.VERSION.SDK_INT,
            Build.MANUFACTURER,
            Build.MODEL,
            BuildConfig.VERSION_NAME,
            BuildConfig.VERSION_CODE
        );

        try {
            String uuid = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
            str += ", UUID = " + uuid;
        } catch (Exception ex) {
            // ignored
        }
        return str;
    }
}
