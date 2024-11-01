package seven.bsh.net.response;

import org.json.JSONException;
import org.json.JSONObject;

import seven.bsh.db.entity.Report;
import seven.bsh.net.parser.ReportParser;

@SuppressWarnings("unused")
public class ReportGetResponse {
    private Report mModel;

    //---------------------------------------------------------------------------
    //
    // PUBLIC METHODS
    //
    //---------------------------------------------------------------------------

    protected void parseOkResponse(JSONObject data) throws JSONException {
        ReportParser parser = new ReportParser();
        mModel = parser.parseOne(data);
    }

    //---------------------------------------------------------------------------
    //
    // ACCESSORS
    //
    //---------------------------------------------------------------------------

    public Report getModel() {
        return mModel;
    }
}
