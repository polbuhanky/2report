package seven.bsh.net.request;

public class TradeObjectsGetRequest {
    private final String mToken;
    private final int mPage;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public TradeObjectsGetRequest(String token, int page) {
        mToken = token;
        mPage = page;
    }

    //---------------------------------------------------------------------------
    //
    // ACCESSORS
    //
    //---------------------------------------------------------------------------

    public String getToken() {
        return "Bearer " + mToken;
    }

    public int getPage() {
        return mPage;
    }
}
