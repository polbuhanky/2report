package seven.bsh.net.listener;

import retrofit2.Call;
import retrofit2.Response;
import seven.bsh.net.response.SettingsGetResponse;

public class OnSettingsGetRequestListener extends BaseRequestListener<SettingsGetResponse> {
    private final OnRequestListener mListener;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public OnSettingsGetRequestListener(OnRequestListener listener) {
        super(listener);
        mListener = listener;
    }

    //---------------------------------------------------------------------------
    //
    // HANDLERS
    //
    //---------------------------------------------------------------------------

    @Override
    public void onResponse(Call<SettingsGetResponse> call, Response<SettingsGetResponse> response) {
        if (response.isSuccessful()) {
            mListener.onSettingsGetSuccess(response.body());
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
        void onSettingsGetSuccess(SettingsGetResponse response);
    }
}
