package seven.bsh.net.response;

import org.json.JSONException;
import org.json.JSONObject;

@SuppressWarnings("unused")
public class ReportPutResponse {
    private int mId;

    //---------------------------------------------------------------------------
    //
    // PRIVATE METHODS
    //
    //---------------------------------------------------------------------------

    protected void parseOkResponse(JSONObject data) throws JSONException {
        // ignored
    }

    //---------------------------------------------------------------------------
    //
    // ACCESSORS
    //
    //---------------------------------------------------------------------------

    public int getId() {
        return mId;
    }
}
