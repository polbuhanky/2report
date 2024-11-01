package seven.bsh.view.report.update;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import seven.bsh.R;
import seven.bsh.db.entity.QueueFile;
import seven.bsh.db.entity.QueueReport;
import seven.bsh.db.entity.TradeObject;
import seven.bsh.view.attributes.FileInputAttribute;

public class DraftReportActivity extends BaseUpdateReportActivity {
    public static final String ARGUMENT_QUEUE_ID = "queue_id";

    private static final String TAG = "DraftReportActivity";

    private QueueReport mReport;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.activity_create_report_title_draft);

        Intent intent = getIntent();
        int reportId = intent.getIntExtra(ARGUMENT_QUEUE_ID, 0);
        mReport = getDb()
            .getQueueRepository()
            .get(reportId);

        if (mReport == null) {
            Toast.makeText(this, R.string.activity_report_not_found, Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        List<QueueFile> files = getDb()
            .getQueueRepository()
            .getFiles(mReport.getId());
        mReport.setFileList(files);

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
    // PRIVATE METHODS
    //
    //---------------------------------------------------------------------------

    @Override
    protected void prepareFileAttribute(FileInputAttribute item) {
        List<QueueFile> files = mReport.getFileList();
        for (QueueFile file : files) {
            if (file.getFieldName().equals(item.getName())) {
                item.parseFileModel(file);
                break;
            }
        }
    }

    @Override
    protected int prepareReportData(AsyncTask asyncTask, boolean draft) {
        try {
            JSONObject reportData = form.serialize(DraftReportActivity.this);
            mReport.setData(reportData.toString());
            mReport.setLatitude(form.getLatitude());
            mReport.setLongitude(form.getLongitude());
            mReport.setFileList(form.getFiles());

            return getDb()
                .getQueueRepository()
                .save(mReport, draft);
        } catch (JSONException ex) {
            Log.e(TAG, ex.toString(), ex);
            asyncTask.cancel(false);
            return 0;
        }
    }

    @Override
    protected boolean isDraft() {
        return true;
    }
}
