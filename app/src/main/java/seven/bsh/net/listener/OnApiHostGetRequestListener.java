package seven.bsh.net.listener;

import retrofit2.Call;
import retrofit2.Response;
import seven.bsh.net.response.ApiHostGetResponse;

public class OnApiHostGetRequestListener extends BaseRequestListener<ApiHostGetResponse> {
    private final OnRequestListener mListener;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public OnApiHostGetRequestListener(OnRequestListener listener) {
        super(listener);
        mListener = listener;
    }

    //---------------------------------------------------------------------------
    //
    // HANDLERS
    //
    //---------------------------------------------------------------------------

    @Override
    public void onResponse(Call<ApiHostGetResponse> call, Response<ApiHostGetResponse> response) {
        if (response.isSuccessful()) {
            mListener.onApiHostGetSuccess(response.body());
        } else if (response.code() == 404) {
            mListener.onApiHostGet404Error();
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
        void onApiHostGet404Error();

        void onApiHostGetSuccess(ApiHostGetResponse response);
    }
}
