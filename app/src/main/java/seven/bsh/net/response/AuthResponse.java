package seven.bsh.net.response;

import com.fasterxml.jackson.annotation.JsonProperty;

@SuppressWarnings("unused")
public class AuthResponse {
    private String mToken;
    private String mRefreshToken;

    //---------------------------------------------------------------------------
    //
    // ACCESSORS
    //
    //---------------------------------------------------------------------------

    public String getToken() {
        return mToken;
    }

    @JsonProperty("access_token")
    public void setToken(String token) {
        mToken = token;
    }

    public String getRefreshToken() {
        return mRefreshToken;
    }

    @JsonProperty("refresh_token")
    public void setRefreshToken(String refreshToken) {
        mRefreshToken = refreshToken;
    }
}
