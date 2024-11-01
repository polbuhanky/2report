package seven.bsh.net.request;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import seven.bsh.db.entity.QueueReport;

public class ReportPutRequest {
    private final String mToken;
    private final QueueReport mModel;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public ReportPutRequest(String token, QueueReport model) {
        mToken = token;
        mModel = model;
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
        return mModel.getInnerId();
    }

    public RequestBody getBody() {
        try {
            JSONObject json = new JSONObject();
            if (mModel.getLatitude() != 0 && mModel.getLongitude() != 0) {
                json.put("latitude", mModel.getLatitude());
                json.put("longitude", mModel.getLongitude());
            }

            JSONObject attributes = mModel.getDataJson();
            Iterator<String> keys = attributes.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                json.put(key, attributes.get(key));
            }
            return RequestBody.create(MediaType.parse("application/vnd+json"), json.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}