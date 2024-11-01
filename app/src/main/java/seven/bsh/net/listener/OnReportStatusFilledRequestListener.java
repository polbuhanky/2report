package seven.bsh.net.listener;

import android.util.Log;

import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Converter;
import retrofit2.Response;
import seven.bsh.net.ApiConnector;
import seven.bsh.net.response.ReportStatusFilledResponse;
import seven.bsh.net.response.ValidateErrorResponse;

public class OnReportStatusFilledRequestListener extends BaseRequestListener<ReportStatusFilledResponse> {
    private final OnRequestListener mListener;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public OnReportStatusFilledRequestListener(OnRequestListener listener) {
        super(listener);
        mListener = listener;
    }

    //---------------------------------------------------------------------------
    //
    // HANDLERS
    //
    //---------------------------------------------------------------------------

    @Override
    public void onResponse(Call<ReportStatusFilledResponse> call, Response<ReportStatusFilledResponse> response) {
        int code = response.code();
        if (response.isSuccessful() || code == 302) {
            mListener.onReportStatusFilledSuccess(response.body());
            return;
        }

        switch (code) {
            case 404:
                mListener.onReportStatusFilledNotFound();
                break;

            case 422:
                try {
                    Converter<ResponseBody, ValidateErrorResponse> converter = ApiConnector.getRetrofit().responseBodyConverter(ValidateErrorResponse.class, new Annotation[0]);
                    ValidateErrorResponse error = converter.convert(response.errorBody());
                    mListener.onReportStatusFilledFailedValidation(error);
                } catch (Exception ex) {
                    Log.e(TAG, ex.getMessage(), ex);
                }
                break;

            default:
                super.onResponse(call, response);
        }
    }

    //---------------------------------------------------------------------------
    //
    // INTERFACES
    //
    //---------------------------------------------------------------------------

    public interface OnRequestListener extends OnFailureRequestListener {
        void onReportStatusFilledSuccess(ReportStatusFilledResponse response);

        void onReportStatusFilledNotFound();

        void onReportStatusFilledFailedValidation(ValidateErrorResponse response);
    }
}
