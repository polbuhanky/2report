package seven.bsh.net.request;

import seven.bsh.net.response.ReportGetResponse;

public class ReportGetRequest {
    private final String mToken;
    private final int mReportId;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public ReportGetRequest(String token, int reportId) {
        mToken = token;
        mReportId = reportId;
    }

    //---------------------------------------------------------------------------
    //
    // PUBLIC METHODS
    //
    //---------------------------------------------------------------------------

    public ReportGetResponse getDemoResponse() {
        ReportGetResponse response = new ReportGetResponse();
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

    public int getReportId() {
        return mReportId;
    }
}
