package seven.bsh.net.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import seven.bsh.BuildConfig;

public class RefreshTokenRequest {
    private final String mRefreshToken;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public RefreshTokenRequest(String refreshToken) {
        mRefreshToken = refreshToken;
    }

    //---------------------------------------------------------------------------
    //
    // ACCESSORS
    //
    //---------------------------------------------------------------------------

    @JsonProperty("refresh_token")
    public String getRefreshToken() {
        return mRefreshToken;
    }

    @JsonProperty("grant_type")
    public String getGrantType() {
        return "refresh_token";
    }

    @JsonProperty("client_id")
    public String getClientId() {
        return BuildConfig.API_ID;
    }

    @JsonProperty("client_secret")
    public String getClientSecret() {
        return BuildConfig.API_SECRET;
    }
}
