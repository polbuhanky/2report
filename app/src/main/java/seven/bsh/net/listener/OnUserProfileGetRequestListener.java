package seven.bsh.net.listener;

import retrofit2.Call;
import retrofit2.Response;
import seven.bsh.net.response.UserProfileGetResponse;

public class OnUserProfileGetRequestListener extends BaseRequestListener<UserProfileGetResponse> {
    private final OnRequestListener mListener;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public OnUserProfileGetRequestListener(OnRequestListener listener) {
        super(listener);
        mListener = listener;
    }

    //---------------------------------------------------------------------------
    //
    // HANDLERS
    //
    //---------------------------------------------------------------------------

    @Override
    public void onResponse(Call<UserProfileGetResponse> call, Response<UserProfileGetResponse> response) {
        if (response.isSuccessful()) {
            mListener.onUserProfileGetSuccess(response.body());
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
        void onUserProfileGetSuccess(UserProfileGetResponse response);
    }
}
