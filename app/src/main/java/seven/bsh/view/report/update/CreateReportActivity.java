package seven.bsh.view.report.update;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;

import seven.bsh.R;
import seven.bsh.db.DatabaseHelper;

public class CreateReportActivity extends BaseUpdateReportActivity {
    public static final String ARGUMENT_CHECKLIST_ID = "checklist_id";
    public static final String ARGUMENT_TRADE_OBJECT_ID = "trade_object_id";
    public static final String ARGUMENT_PROJECT_ID = "project_id";

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.activity_create_report_title);

        Intent intent = getIntent();
        int tradeObjectId = intent.getIntExtra(ARGUMENT_TRADE_OBJECT_ID, 0);
        int projectId = intent.getIntExtra(ARGUMENT_PROJECT_ID, 0);
        int checklistId = intent.getIntExtra(ARGUMENT_CHECKLIST_ID, 0);

        checklist = DatabaseHelper.getInstance()
            .getChecklistRepository()
            .get(checklistId);
        tradeObject = DatabaseHelper.getInstance()
            .getTradeObjectRepository()
            .get(tradeObjectId);

        form.setProjectId(projectId);
        form.setChecklistId(checklistId);
        form.setTradeObjectId(tradeObjectId);

        containerLayout.addView(createHeader());
        createAttributeViews();
    }
}
