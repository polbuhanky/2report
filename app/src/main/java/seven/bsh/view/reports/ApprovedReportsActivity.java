package seven.bsh.view.reports;

import android.os.Bundle;

import seven.bsh.R;
import seven.bsh.db.entity.Report;
import seven.bsh.view.reports.adapter.ReportAdapter;
import seven.bsh.view.widget.sideMenu.SideMenuItem;

public class ApprovedReportsActivity extends BaseReportsActivity {

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);
        initBaseReportsActivity();
        getSupportActionBar().setSubtitle(R.string.activity_reports_title_approved);
        getSideMenu().setActive(SideMenuItem.Id.REPORTS_APPROVED);

        adapter = new ReportAdapter(list);
        listView.setAdapter(adapter);
    }

    //---------------------------------------------------------------------------
    //
    // ACCESSORS
    //
    //---------------------------------------------------------------------------

    @Override
    protected int getListStatus() {
        return Report.STATUS_APPROVED;
    }
}
