package seven.bsh.view.report.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import seven.bsh.R;
import seven.bsh.db.DatabaseHelper;
import seven.bsh.db.entity.TradeObject;
import seven.bsh.net.ApiConnector;
import seven.bsh.net.listener.OnChecklistGetRequestListener;
import seven.bsh.net.listener.OnReportGetRequestListener;
import seven.bsh.net.request.ChecklistGetRequest;
import seven.bsh.net.request.ReportGetRequest;
import seven.bsh.net.response.ChecklistGetResponse;
import seven.bsh.net.response.RefreshTokenResponse;
import seven.bsh.net.response.ReportGetResponse;

public class ViewReportActivity extends BaseViewReportActivity implements
    OnChecklistGetRequestListener.OnRequestListener,
    OnReportGetRequestListener.OnRequestListener {

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        int reportId = intent.getIntExtra(ARGUMENT_REPORT_ID, 0);
        report = DatabaseHelper.getInstance()
            .getReportRepository()
            .get(reportId);

        if (report == null) {
            Toast.makeText(this, R.string.activity_reports_no_report, Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        try {
            checklist = getDb().getChecklistRepository()
                .get(report.getChecklistId());

            tradeObject = new TradeObject();
            tradeObject.setAddress(report.getTradeObjectAddress());
            tradeObject.setName(report.getTradeObjectName());
            tradeObject.setId(report.getTradeObjectId());
            fillContainer();
        } catch (Exception ex) {
            Log.e("ex", ex.getMessage());
        }
    }

    @Override
    public void onAuthSuccess(RefreshTokenResponse response) {
        String token = response.getToken();
        getLocalData().saveToken(token, response.getRefreshToken());
        if (report == null) {
            sendReportGetRequest(token);
        } else {
            sendGetChecklistRequest(token);
        }
    }

    @Override
    public void onTokenError() {
        sendAuthRequest();
    }

    @Override
    public void onReportGetSuccess(ReportGetResponse response) {
        report = response.getModel();
        DatabaseHelper.getInstance()
            .getReportRepository()
            .save(report);

        String token = getLocalData().getAccessToken();
        sendGetChecklistRequest(token);
    }

    @Override
    public void onChecklistGetSuccess(ChecklistGetResponse response) {
        checklist = response.getModel();
        DatabaseHelper.getInstance()
            .getChecklistRepository()
            .save(checklist);
        fillContainer();
    }

    //---------------------------------------------------------------------------
    //
    // PRIVATE METHODS
    //
    //---------------------------------------------------------------------------

    private void sendReportGetRequest(String token) {
        ApiConnector api = getApi();
        if (api != null) {
            ReportGetRequest request = new ReportGetRequest(token, report.getId());
            api.getReport(request, new OnReportGetRequestListener(this));
        }
    }

    private void sendGetChecklistRequest(String token) {
        ApiConnector api = getApi();
        if (api != null) {
            ChecklistGetRequest request = new ChecklistGetRequest(token, report.getChecklistId());
            api.getChecklist(request, new OnChecklistGetRequestListener(this));
        }
    }
}
