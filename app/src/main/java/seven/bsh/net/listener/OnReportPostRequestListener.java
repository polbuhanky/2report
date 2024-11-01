package seven.bsh.net.listener;

import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Converter;
import retrofit2.Response;
import seven.bsh.net.ApiConnector;
import seven.bsh.net.response.ReportPostResponse;
import seven.bsh.net.response.ValidateErrorResponse;

public class OnReportPostRequestListener extends BaseRequestListener<ReportPostResponse> {
    private final OnRequestListener mListener;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public OnReportPostRequestListener(OnRequestListener listener) {
        super(listener);
        mListener = listener;
    }

    //---------------------------------------------------------------------------
    //
    // HANDLERS
    //
    //---------------------------------------------------------------------------

    @Override
    public void onResponse(Call<ReportPostResponse> call, Response<ReportPostResponse> response) {
        int code = response.code();
        if (response.isSuccessful() || code == 302) {
            ReportPostResponse body = response.body();
            if (body == null) {
                mListener.onParserError();
            } else {
                mListener.onReportPostSuccess(body);
            }
            return;
        }

        switch (code) {
            case 404:
                mListener.onReportPostNotFound();
                break;

            case 422:
                try {
                    Converter<ResponseBody, ValidateErrorResponse> converter = ApiConnector.getRetrofit().responseBodyConverter(ValidateErrorResponse.class, new Annotation[0]);
                    ValidateErrorResponse error = converter.convert(response.errorBody());
                    mListener.onReportPostFailedValidation(error);
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
        void onReportPostSuccess(ReportPostResponse response);

        void onReportPostNotFound();

        void onReportPostFailedValidation(ValidateErrorResponse response);
    }
}
