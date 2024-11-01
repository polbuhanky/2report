package seven.bsh.net.request;

public class SkuGetRequest {
    private final String mToken;
    private final String mLastUpdate;
    private final int mPage;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public SkuGetRequest(String token, int page, String lastUpdate) {
        mToken = token;
        mPage = page;
        mLastUpdate = lastUpdate;
    }

    //---------------------------------------------------------------------------
    //
    // ACCESSORS
    //
    //---------------------------------------------------------------------------

    public String getToken() {
        return "Bearer " + mToken;
    }

    public String getLastUpdate() {
        return mLastUpdate;
    }

    public int getPage() {
        return mPage;
    }
}
