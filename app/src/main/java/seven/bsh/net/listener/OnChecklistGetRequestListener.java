package seven.bsh.net.listener;

import retrofit2.Call;
import retrofit2.Response;
import seven.bsh.net.response.ChecklistGetResponse;

public class OnChecklistGetRequestListener extends BaseRequestListener<ChecklistGetResponse> {
    private final OnRequestListener mListener;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public OnChecklistGetRequestListener(OnRequestListener listener) {
        super(listener);
        mListener = listener;
    }

    //---------------------------------------------------------------------------
    //
    // HANDLERS
    //
    //---------------------------------------------------------------------------

    @Override
    public void onResponse(Call<ChecklistGetResponse> call, Response<ChecklistGetResponse> response) {
        if (response.isSuccessful()) {
            mListener.onChecklistGetSuccess(response.body());
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
        void onChecklistGetSuccess(ChecklistGetResponse response);
    }
}
