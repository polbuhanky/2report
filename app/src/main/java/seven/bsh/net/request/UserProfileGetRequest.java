package seven.bsh.net.request;

public class UserProfileGetRequest {
    private final String mToken;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public UserProfileGetRequest(String token) {
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
