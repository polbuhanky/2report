package seven.bsh.net.request;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import seven.bsh.db.entity.QueueReport;
import seven.bsh.net.response.ReportPostResponse;

public class ReportPostRequest {
    private final String mToken;
    private final QueueReport mModel;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public ReportPostRequest(String token, QueueReport model) {
        mToken = token;
        mModel = model;
    }

    //---------------------------------------------------------------------------
    //
    // PUBLIC METHODS
    //
    //---------------------------------------------------------------------------

    public ReportPostResponse getDemoResponse() {
        ReportPostResponse response = new ReportPostResponse();
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

    public RequestBody getBody() {
        try {
            JSONObject json = new JSONObject();
            json.put("project_id", mModel.getProjectId());
            json.put("checklist_id", mModel.getChecklistId());
            json.put("trade_object_id", mModel.getTradeObjectId());

            JSONObject attributes = mModel.getDataJson();
            Iterator<String> keys = attributes.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                json.put(key, attributes.get(key));
            }

            if (mModel.getLatitude() != 0 && mModel.getLongitude() != 0) {
                json.put("latitude", mModel.getLatitude());
                json.put("longitude", mModel.getLongitude());
            }

            return RequestBody.create(MediaType.parse("application/vnd+json"), json.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
