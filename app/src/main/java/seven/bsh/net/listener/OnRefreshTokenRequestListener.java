package seven.bsh.net.listener;

import retrofit2.Call;
import retrofit2.Response;
import seven.bsh.net.response.RefreshTokenResponse;

public class OnRefreshTokenRequestListener extends BaseRequestListener<RefreshTokenResponse> {
    private final OnRequestListener mListener;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public OnRefreshTokenRequestListener(OnRequestListener listener) {
        super(listener);
        mListener = listener;
    }

    //---------------------------------------------------------------------------
    //
    // HANDLERS
    //
    //---------------------------------------------------------------------------

    @Override
    public void onResponse(Call<RefreshTokenResponse> call, Response<RefreshTokenResponse> response) {
        if (response.isSuccessful()) {
            mListener.onAuthSuccess(response.body());
        } else if (response.code() == 400) {
            mListener.onAuthFailed();
        } else {
            super.onResponse(call, response);
        }
    }

    //---------------------------------------------------------------------------
    //
    // INTERFACES
    //
    //---------------------------------------------------------------------------

    public interface OnRequestListener extends OnFailureRequestListener {
        void onAuthSuccess(RefreshTokenResponse response);

        void onAuthFailed();
    }
}
