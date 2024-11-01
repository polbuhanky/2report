package seven.bsh.net.listener;

import retrofit2.Call;
import retrofit2.Response;
import seven.bsh.net.response.ReportUploadFileResponse;

public class OnReportUploadFileRequestListener extends BaseRequestListener<ReportUploadFileResponse> {
    private final OnRequestListener mListener;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public OnReportUploadFileRequestListener(OnRequestListener listener) {
        super(listener);
        mListener = listener;
    }

    //---------------------------------------------------------------------------
    //
    // HANDLERS
    //
    //---------------------------------------------------------------------------

    @Override
    public void onResponse(Call<ReportUploadFileResponse> call, Response<ReportUploadFileResponse> response) {
        int code = response.code();
        if (response.isSuccessful() || code == 302) {
            mListener.onReportUploadFileSuccess(response.body());
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
        void onReportUploadFileSuccess(ReportUploadFileResponse response);
    }
}
