package seven.bsh.net.response;

import seven.bsh.db.entity.Checklist;

@SuppressWarnings("unused")
public class ChecklistGetResponse {
    private Checklist mModel;

    //---------------------------------------------------------------------------
    //
    // PUBLIC METHODS
    //
    //---------------------------------------------------------------------------

    /*protected void parseOkResponse(JSONObject data) throws JSONException {
        ChecklistParser parser = new ChecklistParser();
        mModel = parser.parseOne(data);
    }*/

    //---------------------------------------------------------------------------
    //
    // ACCESSORS
    //
    //---------------------------------------------------------------------------

    public Checklist getModel() {
        return mModel;
    }
}
