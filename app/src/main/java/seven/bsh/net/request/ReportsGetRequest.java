package seven.bsh.net.request;

import org.json.JSONException;
import org.json.JSONObject;

import seven.bsh.net.response.ReportsGetResponse;

public class ReportsGetRequest {
    private final String mToken;
    private final int mStatus;
    private final int mPage;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public ReportsGetRequest(String token, int page, int status) {
        mToken = token;
        mStatus = status;
        mPage = page;
    }

    //---------------------------------------------------------------------------
    //
    // PUBLIC METHODS
    //
    //---------------------------------------------------------------------------

    public ReportsGetResponse getDemoResponse() {
        ReportsGetResponse response = new ReportsGetResponse();
        return response;
    }

    //---------------------------------------------------------------------------
    //
    // ACCESSORS
    //
    //---------------------------------------------------------------------------

    public String getToken() {
        return "Bearer " + mToken;
    }

    public int getStatus() {
        return mStatus;
    }

    public int getPage() {
        return mPage;
    }

    public String getFilter() {
        JSONObject filter = new JSONObject();
        try {
            filter.put("status", mStatus);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return filter.toString();
    }
}
