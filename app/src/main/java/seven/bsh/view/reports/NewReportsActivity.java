package seven.bsh.view.reports;

import android.content.Intent;
import android.os.Bundle;

import seven.bsh.R;
import seven.bsh.db.entity.Report;
import seven.bsh.view.report.view.ViewReportActivity;
import seven.bsh.view.reports.adapter.ReportAdapter;
import seven.bsh.view.widget.adapter.BaseAdapter;
import seven.bsh.view.widget.sideMenu.SideMenuItem;

public class NewReportsActivity extends BaseReportsActivity {

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
        getSupportActionBar().setSubtitle(R.string.activity_reports_title_new);
        getSideMenu().setActive(SideMenuItem.Id.REPORTS_NEW);

        adapter = new ReportAdapter(list);
        listView.setAdapter(adapter);
    }

    //---------------------------------------------------------------------------
    //
    // HANDLERS
    //
    //---------------------------------------------------------------------------

    @Override
    public void onItemClick(BaseAdapter adapter, int position) {
        Report model = (Report) adapter.getItem(position);
        Intent intent = new Intent(this, ViewReportActivity.class);
        intent.putExtra(ViewReportActivity.ARGUMENT_REPORT_ID, model.getId());
        startActivity(intent);
    }

    //---------------------------------------------------------------------------
    //
    // ACCESSORS
    //
    //---------------------------------------------------------------------------

    @Override
    protected int getListStatus() {
        return Report.STATUS_NEW;
    }
}
