package seven.bsh.net.request;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import seven.bsh.db.entity.Report;
import seven.bsh.net.response.ReportStatusFilledResponse;

public class ReportStatusFilledRequest {
    private final String mToken;
    private final int mId;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public ReportStatusFilledRequest(String token, int id) {
        mToken = token;
        mId = id;
    }

    //---------------------------------------------------------------------------
    //
    // PUBLIC METHODS
    //
    //---------------------------------------------------------------------------

    public ReportStatusFilledResponse getDemoResponse() {
        ReportStatusFilledResponse response = new ReportStatusFilledResponse();
        return response;
    }

    //---------------------------------------------------------------------------
    //
    // PRIVATE METHODS
    //
    //---------------------------------------------------------------------------

    public String getToken() {
        return "Bearer " + mToken;
    }

    public int getId() {
        return mId;
    }

    public RequestBody getBody() {
        try {
            JSONObject data = new JSONObject();
            data.put("status", Report.STATUS_NEW);
            return RequestBody.create(MediaType.parse("application/vnd+json"), data.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
