package seven.bsh.net.listener;

import android.util.Log;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class BaseRequestListener<T> implements Callback<T> {
    protected static final String TAG = "RequestListener";

    private OnFailureRequestListener mListener;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public BaseRequestListener(OnFailureRequestListener listener) {
        mListener = listener;
    }

    //---------------------------------------------------------------------------
    //
    // HANDLERS
    //
    //---------------------------------------------------------------------------

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        int code = response.code();
        String error;
        switch (code) {
            case 500:
                error = getError(response.errorBody());
                mListener.onServerError();
                return;

            case 401:
                mListener.onTokenError();
                break;

            default:
                error = getError(response.errorBody());
                mListener.onHttpUnknownError(code, error);
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable throwable) {
        Log.e("RequestListener", throwable.getMessage(), throwable.getCause());
        if (throwable instanceof UnknownHostException || throwable instanceof SocketTimeoutException) {
            mListener.onNoInternetError();
        } else if (throwable instanceof UnrecognizedPropertyException) {
            mListener.onParserError();
        } else {
            mListener.onHttpUnknownError(-1, throwable.getMessage());
        }
    }

    //---------------------------------------------------------------------------
    //
    // PRIVATE METHODS
    //
    //---------------------------------------------------------------------------

    protected String getError(ResponseBody body) {
        String error = "";
        try {
            if (body != null) {
                error = body.string();
            }
        } catch (IOException ex) {
            // ignored
        }
        return error;
    }
}
