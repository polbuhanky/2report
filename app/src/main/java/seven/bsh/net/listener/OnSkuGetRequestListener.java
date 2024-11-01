package seven.bsh.net.listener;

import retrofit2.Call;
import retrofit2.Response;
import seven.bsh.net.response.SkuGetResponse;

public class OnSkuGetRequestListener extends BaseRequestListener<SkuGetResponse> {
    private final OnRequestListener mListener;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public OnSkuGetRequestListener(OnRequestListener listener) {
        super(listener);
        mListener = listener;
    }

    //---------------------------------------------------------------------------
    //
    // HANDLERS
    //
    //---------------------------------------------------------------------------

    @Override
    public void onResponse(Call<SkuGetResponse> call, Response<SkuGetResponse> response) {
        if (response.isSuccessful()) {
            mListener.onSkuGetSuccess(response.body());
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
        void onSkuGetSuccess(SkuGetResponse response);
    }
}
