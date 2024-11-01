package seven.bsh.net;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import seven.bsh.net.request.AuthRequest;
import seven.bsh.net.request.RefreshTokenRequest;
import seven.bsh.net.response.ApiHostGetResponse;
import seven.bsh.net.response.AuthResponse;
import seven.bsh.net.response.ChecklistGetResponse;
import seven.bsh.net.response.RefreshTokenResponse;
import seven.bsh.net.response.ReportGetResponse;
import seven.bsh.net.response.ReportPostResponse;
import seven.bsh.net.response.ReportPutResponse;
import seven.bsh.net.response.ReportStatusFilledResponse;
import seven.bsh.net.response.ReportUploadFileResponse;
import seven.bsh.net.response.ReportsGetResponse;
import seven.bsh.net.response.SettingsGetResponse;
import seven.bsh.net.response.SkuGetResponse;
import seven.bsh.net.response.TradeObjectsGetResponse;
import seven.bsh.net.response.UserProfileGetResponse;

public interface Api {
    @GET("{id}")
    Call<ApiHostGetResponse> getHost(
        @Path("id") String id
    );

    @Headers("Accept: */*; version=1.1")
    @POST("auth")
    Call<AuthResponse> auth(
        @Body AuthRequest request
    );

    @Headers("Accept: */*; version=1.1")
    @POST("auth")
    Call<RefreshTokenResponse> refreshToken(
        @Body RefreshTokenRequest request
    );

    @Headers("Accept: */*; version=1.1")
    @GET("settings")
    Call<SettingsGetResponse> getSettings(
        @Header("Authorization") String token
    );

    @Headers("Accept: */*; version=1.1")
    @GET("users")
    Call<UserProfileGetResponse> getUserProfile(
        @Header("Authorization") String token
    );

    @Headers("Accept: */*; version=1.1")
    @GET("directories/sku?per-page=50")
    Call<SkuGetResponse> getSku(
        @Header("Authorization") String token,
        @Query("page") int page
    );

    @Headers("Accept: */*; version=1.1")
    @GET("directories/sku?per-page=50")
    Call<SkuGetResponse> getSku(
        @Header("Authorization") String token,
        @Query("page") int page,
        @Query("updated_at") String lastUpdate
    );

    @Headers("Accept: */*; version=1.1")
    @GET("trade-objects?&include=projects.checklists,projects&per-page=50")
    Call<TradeObjectsGetResponse> getTradeObjects(
        @Header("Authorization") String token,
        @Query("page") int page
    );

    @Headers("Accept: */*; version=1.1")
    @GET("checklists/{id}?include=reportsCount")
    Call<ChecklistGetResponse> getChecklist(
        @Header("Authorization") String token,
        @Path("id") int id
    );

    @Headers("Accept: */*; version=1.1")
    @GET("reports?include=checklist,tradeObject,project")
    Call<ReportsGetResponse> getReports(
        @Header("Authorization") String token,
        @Query("page") int page,
        @Query("filter") String filter
    );

    @Headers("Accept: */*; version=1.1")
    @GET("reports/{id}?include=checklist,tradeObject,project")
    Call<ReportGetResponse> getReport(
        @Header("Authorization") String token,
        @Path("id") int id
    );

    @Headers({
        "Accept: */*; version=1.1",
        "Content-Type: application/vnd+json"
    })
    @POST("reports")
    Call<ReportPostResponse> createReport(
        @Header("Authorization") String token,
        @Body RequestBody body
    );

    @Headers({
        "Accept: */*; version=1.1",
        "Content-Type: application/vnd+json"
    })
    @PUT("reports/{id}")
    Call<ReportPutResponse> updateReport(
        @Header("Authorization") String token,
        @Path("id") int id,
        @Body RequestBody body
    );

    @Headers({
        "Accept: */*; version=1.1"
    })
    @Multipart
    @POST("reports/{id}/upload")
    Call<ReportUploadFileResponse> uploadFile(
        @Header("Authorization") String token,
        @Path("id") int id,
        @Part MultipartBody.Part file
    );

    @Headers({
        "Accept: */*; version=1.1",
        "Content-Type: application/vnd+json"
    })
    @PUT("reports/{id}")
    Call<ReportStatusFilledResponse> updateStatusReport(
        @Header("Authorization") String token,
        @Path("id") int id,
        @Body RequestBody body
    );
}
