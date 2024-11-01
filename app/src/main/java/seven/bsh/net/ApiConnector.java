package seven.bsh.net;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import seven.bsh.Application;
import seven.bsh.net.listener.BaseRequestListener;
import seven.bsh.net.listener.OnApiHostGetRequestListener;
import seven.bsh.net.listener.OnAuthRequestListener;
import seven.bsh.net.listener.OnChecklistGetRequestListener;
import seven.bsh.net.listener.OnRefreshTokenRequestListener;
import seven.bsh.net.listener.OnReportGetRequestListener;
import seven.bsh.net.listener.OnReportPostRequestListener;
import seven.bsh.net.listener.OnReportPutRequestListener;
import seven.bsh.net.listener.OnReportStatusFilledRequestListener;
import seven.bsh.net.listener.OnReportUploadFileRequestListener;
import seven.bsh.net.listener.OnReportsGetRequestListener;
import seven.bsh.net.listener.OnSettingsGetRequestListener;
import seven.bsh.net.listener.OnSkuGetRequestListener;
import seven.bsh.net.listener.OnTradeObjectsGetRequestListener;
import seven.bsh.net.listener.OnUserProfileGetRequestListener;
import seven.bsh.net.request.ApiHostGetRequest;
import seven.bsh.net.request.AuthRequest;
import seven.bsh.net.request.ChecklistGetRequest;
import seven.bsh.net.request.RefreshTokenRequest;
import seven.bsh.net.request.ReportGetRequest;
import seven.bsh.net.request.ReportPostRequest;
import seven.bsh.net.request.ReportPutRequest;
import seven.bsh.net.request.ReportStatusFilledRequest;
import seven.bsh.net.request.ReportUploadFileRequest;
import seven.bsh.net.request.ReportsGetRequest;
import seven.bsh.net.request.SettingsGetRequest;
import seven.bsh.net.request.SkuGetRequest;
import seven.bsh.net.request.TradeObjectsGetRequest;
import seven.bsh.net.request.UserProfileGetRequest;

public class ApiConnector {
    private static final String LICENSE_HOST = "http://api.l.2report.ru/v1.1/environments/";

    private static Retrofit mRetrofit;
    private final Application mApplication;
    private Call mRequest;
    private BaseRequestListener mListener;
    private Api mApi;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public ApiConnector(Application application) {
        mApplication = application;
    }

    //---------------------------------------------------------------------------
    //
    // PUBLIC METHODS
    //
    //---------------------------------------------------------------------------

    public void init(String host) {
        if (host == null) {
            host = LICENSE_HOST;
        }

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient httpClient = new OkHttpClient.Builder()
            .addInterceptor(new UserAgentInterceptor(mApplication))
            .addInterceptor(logging)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .followRedirects(false)
            .followSslRedirects(false)
            .build();

        ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        mRetrofit = new Retrofit.Builder()
            .baseUrl(host)
            .addConverterFactory(JacksonConverterFactory.create(objectMapper))
            .client(httpClient)
            .build();
        mApi = mRetrofit.create(Api.class);
    }

    @SuppressWarnings("unchecked")
    public void refresh() {
        if (mRequest != null && mListener != null) {
            mRequest = mRequest.clone();
            mRequest.enqueue(mListener);
        }
    }

    public void getHost(ApiHostGetRequest request, OnApiHostGetRequestListener listener) {
        mRequest = mApi.getHost(request.getEnvironmentId());
        executeRequest(mRequest, listener);
    }

    public void auth(AuthRequest request, OnAuthRequestListener listener) {
        mRequest = mApi.auth(request);
        executeRequest(mRequest, listener);
    }

    public void refreshToken(RefreshTokenRequest request, OnRefreshTokenRequestListener listener) {
        mRequest = mApi.refreshToken(request);
        executeRequest(mRequest, listener);
    }

    public void getSettings(SettingsGetRequest request, OnSettingsGetRequestListener listener) {
        mRequest = mApi.getSettings(request.getToken());
        executeRequest(mRequest, listener);
    }

    public void getUserProfile(UserProfileGetRequest request, OnUserProfileGetRequestListener listener) {
        mRequest = mApi.getUserProfile(request.getToken());
        executeRequest(mRequest, listener);
    }

    public void getSku(SkuGetRequest request, OnSkuGetRequestListener listener) {
        if (request.getLastUpdate() == null) {
            mRequest = mApi.getSku(request.getToken(), request.getPage());
        } else {
            mRequest = mApi.getSku(request.getToken(), request.getPage(), request.getLastUpdate());
        }
        executeRequest(mRequest, listener);
    }

    public void getTradeObjects(TradeObjectsGetRequest request, OnTradeObjectsGetRequestListener listener) {
        mRequest = mApi.getTradeObjects(request.getToken(), request.getPage());
        executeRequest(mRequest, listener);
    }

    public void getChecklist(ChecklistGetRequest request, OnChecklistGetRequestListener listener) {
        mRequest = mApi.getChecklist(request.getToken(), request.getChecklistId());
        executeRequest(mRequest, listener);
    }

    public void getReport(ReportGetRequest request, OnReportGetRequestListener listener) {
        mRequest = mApi.getReport(request.getToken(), request.getReportId());
        executeRequest(mRequest, listener);
    }

    public void getReports(ReportsGetRequest request, OnReportsGetRequestListener listener) {
        mRequest = mApi.getReports(request.getToken(), request.getPage(), request.getFilter());
        executeRequest(mRequest, listener);
    }

    public void postReport(ReportPostRequest request, OnReportPostRequestListener listener) {
        mRequest = mApi.createReport(request.getToken(), request.getBody());
        executeRequest(mRequest, listener);
    }

    public void putReport(ReportPutRequest request, OnReportPutRequestListener listener) {
        mRequest = mApi.updateReport(request.getToken(), request.getId(), request.getBody());
        executeRequest(mRequest, listener);
    }

    public void uploadFile(ReportUploadFileRequest request, OnReportUploadFileRequestListener listener) {
        mRequest = mApi.uploadFile(request.getToken(), request.getReportId(), request.getBody());
        executeRequest(mRequest, listener);
    }

    public void putReportStatusFilled(ReportStatusFilledRequest request, OnReportStatusFilledRequestListener listener) {
        mRequest = mApi.updateStatusReport(request.getToken(), request.getId(), request.getBody());
        executeRequest(mRequest, listener);
    }

    //---------------------------------------------------------------------------
    //
    // PRIVATE METHODS
    //
    //---------------------------------------------------------------------------

    private void executeRequest(Call request, BaseRequestListener listener) {
        if (request == null || listener == null) {
            return;
        }

        mListener = listener;
        request.enqueue(listener);
    }

    public static Retrofit getRetrofit() {
        return mRetrofit;
    }
}
