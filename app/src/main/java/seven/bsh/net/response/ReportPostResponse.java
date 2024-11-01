package seven.bsh.net.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import seven.bsh.net.entity.ReportEntity;

@SuppressWarnings("unused")
public class ReportPostResponse {
    private ReportEntity mReport;

    //---------------------------------------------------------------------------
    //
    // ACCESSORS
    //
    //---------------------------------------------------------------------------

    @JsonProperty("data")
    public void setData(ReportEntity data) {
        mReport = data;
    }

    public int getId() {
        return mReport.getId();
    }
}
