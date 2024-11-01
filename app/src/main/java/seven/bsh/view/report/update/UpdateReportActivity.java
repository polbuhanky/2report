package seven.bsh.view.report.update;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import seven.bsh.R;
import seven.bsh.db.DatabaseHelper;
import seven.bsh.db.entity.QueueReport;
import seven.bsh.db.entity.Report;
import seven.bsh.db.entity.TradeObject;

public class UpdateReportActivity extends BaseUpdateReportActivity {
    public static final String ARGUMENT_REPORT_ID = "report_id";

    private static final String TAG = "UpdateReportActivity";

    private Report mReport;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.activity_create_report_title_update);

        Intent intent = getIntent();
        int reportId = intent.getIntExtra(ARGUMENT_REPORT_ID, 0);
        mReport = getDb()
            .getReportRepository()
            .get(reportId);

        if (mReport == null) {
            Toast.makeText(this, R.string.activity_report_not_found, Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        checklist = getDb()
            .getChecklistRepository()
            .get(mReport.getChecklistId());

        tradeObject = new TradeObject();
        tradeObject.setName(mReport.getTradeObjectName());
        tradeObject.setAddress(mReport.getTradeObjectAddress());

        if (checklist == null) {
            Toast.makeText(this, R.string.activity_report_checklist_not_found, Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        form.setProjectId(mReport.getProjectId());
        form.setChecklistId(mReport.getChecklistId());
        form.setTradeObjectId(mReport.getTradeObjectId());

        reportData = mReport.getDataJson();
        containerLayout.addView(createHeader());
        createAttributeViews();
    }

    //---------------------------------------------------------------------------
    //
    // HANDLERS
    //
    //---------------------------------------------------------------------------

    //---------------------------------------------------------------------------
    //
    // PUBLIC METHODS
    //
    //---------------------------------------------------------------------------

    @Override
    protected int prepareReportData(AsyncTask asyncTask, boolean draft) {
        try {
            JSONObject reportData = form.serialize(UpdateReportActivity.this);
            QueueReport model = new QueueReport();
            model.setInnerId(mReport.getId());
            model.setProjectId(form.getProjectId());
            model.setChecklistId(checklist.getId());
            model.setChecklistName(checklist.getName());
            model.setTradeObjectId(tradeObject.getId());
            model.setTradeObjectName(tradeObject.getName());
            model.setTradeObjectAddress(tradeObject.getAddress());
            model.setData(reportData.toString());
            model.setLatitude(form.getLatitude());
            model.setLongitude(form.getLongitude());
            model.setFileList(form.getFiles());
            model.setUpdated(true);

            return DatabaseHelper.getInstance()
                .getQueueRepository()
                .save(model, draft);
        } catch (JSONException ex) {
            Log.e(TAG, ex.toString(), ex);
            asyncTask.cancel(false);
            return 0;
        }
    }

    //---------------------------------------------------------------------------
    //
    // PRIVATE METHODS
    //
    //---------------------------------------------------------------------------

    //---------------------------------------------------------------------------
    //
    // ACCESSORS
    //
    //---------------------------------------------------------------------------
}
