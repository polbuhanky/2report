package seven.bsh.net.request;

public class SettingsGetRequest {
    private final String mToken;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public SettingsGetRequest(String token) {
        mToken = token;
    }

    //---------------------------------------------------------------------------
    //
    // ACCESSORS
    //
    //---------------------------------------------------------------------------

    public String getToken() {
        return "Bearer " + mToken;
    }
}
