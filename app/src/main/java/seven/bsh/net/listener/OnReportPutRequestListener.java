package seven.bsh.net.listener;

import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Converter;
import retrofit2.Response;
import seven.bsh.net.ApiConnector;
import seven.bsh.net.response.ReportPutResponse;
import seven.bsh.net.response.ValidateErrorResponse;

public class OnReportPutRequestListener extends BaseRequestListener<ReportPutResponse> {
    private final OnRequestListener mListener;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public OnReportPutRequestListener(OnRequestListener listener) {
        super(listener);
        mListener = listener;
    }

    //---------------------------------------------------------------------------
    //
    // HANDLERS
    //
    //---------------------------------------------------------------------------

    @Override
    public void onResponse(Call<ReportPutResponse> call, Response<ReportPutResponse> response) {
        int code = response.code();
        if (response.isSuccessful() || code == 302) {
            mListener.onReportPutSuccess();
            return;
        }

        switch (code) {
            case 404:
                mListener.onReportPutNotFound();
                break;

            case 422:
                try {
                    Converter<ResponseBody, ValidateErrorResponse> converter = ApiConnector.getRetrofit().responseBodyConverter(ValidateErrorResponse.class, new Annotation[0]);
                    ValidateErrorResponse error = converter.convert(response.errorBody());
                    mListener.onReportPutFailedValidation(error);
                } catch (Exception ex) {
                    ex.printStackTrace();
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
        void onReportPutSuccess();

        void onReportPutNotFound();

        void onReportPutFailedValidation(ValidateErrorResponse response);
    }
}
