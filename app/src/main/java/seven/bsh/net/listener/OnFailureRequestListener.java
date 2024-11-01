package seven.bsh.net.listener;

public interface OnFailureRequestListener {
    void onParserError();

    void onNoInternetError();

    void onServerError();

    void onTokenError();

    void onHttpUnknownError(int status, String errorBody);
}
