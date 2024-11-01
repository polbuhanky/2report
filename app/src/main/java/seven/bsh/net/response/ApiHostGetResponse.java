package seven.bsh.net.response;

import com.fasterxml.jackson.annotation.JsonProperty;

@SuppressWarnings("unused")
public class ApiHostGetResponse {
    private String mHost;
    private String mProjectName;
    private String mProjectId;

    //---------------------------------------------------------------------------
    //
    // ACCESSORS
    //
    //---------------------------------------------------------------------------

    @JsonProperty("api_host")
    public String getHost() {
        return mHost;
    }

    @JsonProperty("api_host")
    public void setHost(String mHost) {
        this.mHost = mHost;
    }

    @JsonProperty("project_name")
    public String getProjectName() {
        return mProjectName;
    }

    @JsonProperty("project_name")
    public void setProjectName(String mProjectName) {
        this.mProjectName = mProjectName;
    }

    @JsonProperty("project_id")
    public String getProjectId() {
        return mProjectId;
    }

    @JsonProperty("project_id")
    public void setProjectId(String mProjectId) {
        this.mProjectId = mProjectId;
    }
}
