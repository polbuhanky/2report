package seven.bsh.net.request;

public class ApiHostGetRequest {
    private final String mEnvironmentId;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public ApiHostGetRequest(String environmentId) {
        mEnvironmentId = environmentId;
    }

    public String getEnvironmentId() {
        return mEnvironmentId;
    }
}
