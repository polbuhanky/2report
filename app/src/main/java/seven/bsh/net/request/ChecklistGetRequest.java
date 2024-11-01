package seven.bsh.net.request;

import seven.bsh.net.response.ChecklistGetResponse;

public class ChecklistGetRequest {
    private final String mToken;
    private final int mChecklistId;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public ChecklistGetRequest(String token, int checklistId) {
        mToken = token;
        mChecklistId = checklistId;
    }

    //---------------------------------------------------------------------------
    //
    // PUBLIC METHODS
    //
    //---------------------------------------------------------------------------

    public ChecklistGetResponse getDemoResponse() {
        ChecklistGetResponse response = new ChecklistGetResponse();
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

    public int getChecklistId() {
        return mChecklistId;
    }
}
