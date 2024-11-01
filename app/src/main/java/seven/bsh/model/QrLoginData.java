package seven.bsh.model;

public class QrLoginData {
    private String mLogin;
    private String mPassword;
    private String mEnvironmentId;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public QrLoginData(String login, String password, String environmentId) {
        mLogin = login;
        mPassword = password;
        mEnvironmentId = environmentId;
    }

    //---------------------------------------------------------------------------
    //
    // ACCESSORS
    //
    //---------------------------------------------------------------------------

    public String getLogin() {
        return mLogin;
    }

    public String getPassword() {
        return mPassword;
    }

    public String getEnvironmentId() {
        return mEnvironmentId;
    }
}
