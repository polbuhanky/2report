package seven.bsh.net.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import seven.bsh.BuildConfig;

public class AuthRequest {
    private final String mLogin;
    private final String mPassword;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public AuthRequest(String login, String password) {
        mLogin = login;
        mPassword = password;
    }

    //---------------------------------------------------------------------------
    //
    // ACCESSORS
    //
    //---------------------------------------------------------------------------

    @JsonProperty("username")
    public String getLogin() {
        return mLogin;
    }

    @JsonProperty("password")
    public String getPassword() {
        return mPassword;
    }

    @JsonProperty("grant_type")
    public String getGrantType() {
        return "password";
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
