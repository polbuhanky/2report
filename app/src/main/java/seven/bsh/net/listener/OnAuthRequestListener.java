package seven.bsh.net.listener;

import retrofit2.Call;
import retrofit2.Response;
import seven.bsh.net.response.AuthResponse;

public class OnAuthRequestListener extends BaseRequestListener<AuthResponse> {
    private final OnRequestListener mListener;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public OnAuthRequestListener(OnRequestListener listener) {
        super(listener);
        mListener = listener;
    }

    //---------------------------------------------------------------------------
    //
    // HANDLERS
    //
    //---------------------------------------------------------------------------

    @Override
    public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
        if (response.isSuccessful()) {
            mListener.onAuthSuccess(response.body());
            return;
        }

        switch (response.code()) {
            case 422:
                String error = getError(response.errorBody());
                mListener.onAuth422Error(error);
                return;

            case 404:
            case 401:
            case 400:
                mListener.onAuthFailedError();
                return;
        }
        super.onResponse(call, response);
    }

    //---------------------------------------------------------------------------
    //
    // INTERFACES
    //
    //---------------------------------------------------------------------------

    public interface OnRequestListener extends OnFailureRequestListener {
        void onAuthFailedError();

        void onAuthSuccess(AuthResponse response);

        void onAuth422Error(String text);
    }
}
