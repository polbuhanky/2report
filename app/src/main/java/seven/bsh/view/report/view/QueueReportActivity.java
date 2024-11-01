package seven.bsh.view.report.view;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import seven.bsh.R;
import seven.bsh.db.entity.QueueFile;
import seven.bsh.db.entity.QueueReport;
import seven.bsh.db.entity.TradeObject;
import seven.bsh.model.ChecklistAttribute;
import seven.bsh.view.attributes.AttributeItem;
import seven.bsh.view.attributes.FileInputAttribute;
import seven.bsh.view.attributes.IInputAttribute;
import seven.bsh.view.report.BaseReportActivity;

public class QueueReportActivity extends BaseReportActivity {
    public static final String ARGUMENT_QUEUE_ID = "queue_id";

    private QueueReport mReport;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.activity_create_report_title_queue);

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

        fillContainer();
    }

    //---------------------------------------------------------------------------
    //
    // PRIVATE METHODS
    //
    //---------------------------------------------------------------------------

    protected void fillContainer() {
        containerLayout.addView(createHeader());
        AttributeItem prevItem = null;
        attributeItems = new ArrayList<>();

        for (ChecklistAttribute attribute : checklist.getAttributeList()) {
            AttributeItem item = AttributeItem.Builder.create(this, attribute);
            if (item == null) {
                continue;
            }

            if (item instanceof IInputAttribute) {
                parseDataJson((IInputAttribute) item, mReport.getDataJson());

                if (item instanceof FileInputAttribute) {
                    FileInputAttribute fileInput = (FileInputAttribute) item;
                    for (QueueFile file : mReport.getFileList()) {
                        if (fileInput.getName().equals(file.getFieldName())) {
                            fileInput.parseFileModel(file);
                            break;
                        }
                    }
                }
            }

            View itemView = item.createValueView();
            containerLayout.addView(itemView);
            prepareItemLayout(prevItem, item);
            prevItem = item;
        }
    }

    @Override
    protected int getContentViewRes() {
        return R.layout.activity_report;
    }
}
