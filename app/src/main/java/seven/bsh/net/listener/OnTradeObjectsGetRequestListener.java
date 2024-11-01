package seven.bsh.net.listener;

import retrofit2.Call;
import retrofit2.Response;
import seven.bsh.net.response.TradeObjectsGetResponse;

public class OnTradeObjectsGetRequestListener extends BaseRequestListener<TradeObjectsGetResponse> {
    private final OnRequestListener mListener;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public OnTradeObjectsGetRequestListener(OnRequestListener listener) {
        super(listener);
        mListener = listener;
    }

    //---------------------------------------------------------------------------
    //
    // HANDLERS
    //
    //---------------------------------------------------------------------------

    @Override
    public void onResponse(Call<TradeObjectsGetResponse> call, Response<TradeObjectsGetResponse> response) {
        if (response.isSuccessful()) {
            mListener.onTradeObjectsGetSuccess(response.body());
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
        void onTradeObjectsGetSuccess(TradeObjectsGetResponse response);
    }
}
