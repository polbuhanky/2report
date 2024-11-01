package seven.bsh.view.queue;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;

import java.util.ArrayList;
import java.util.List;

import seven.bsh.R;
import seven.bsh.db.DatabaseHelper;
import seven.bsh.db.entity.QueueReport;
import seven.bsh.view.queue.adapter.QueueAdapter;
import seven.bsh.view.report.update.DraftReportActivity;
import seven.bsh.view.widget.adapter.BaseAdapter;
import seven.bsh.view.widget.sideMenu.SideMenuItem;

public class DraftReportsActivity extends BaseQueueActivity {

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drafts);
        initBaseQueueActivity();
        setTitle(R.string.activity_reports_title);
        getSupportActionBar().setSubtitle(R.string.activity_reports_title_draft);
        getSideMenu().setActive(SideMenuItem.Id.REPORTS_DRAFT);
    }

    //---------------------------------------------------------------------------
    //
    // HANDLERS
    //
    //---------------------------------------------------------------------------

    @Override
    protected void onResume() {
        super.onResume();
        List<QueueReport> temp = DatabaseHelper.getInstance()
            .getQueueRepository()
            .getDraftList();

        list.clear();
        list.addAll(temp);

        if (adapter == null) {
            adapter = new QueueAdapter(this, list);
            adapter.setActionListener(this);
            listView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
        invalidateOptionsMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_draft_reports, menu);
        setMenuItemIcon(menu, R.id.action_delete, GoogleMaterial.Icon.gmd_delete);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean enabled = !list.isEmpty();
        MenuItem deleteItem = menu.findItem(R.id.action_delete);
        deleteItem.setEnabled(enabled);

        if (enabled) {
            deleteItem.getIcon().setAlpha(255);
        } else {
            deleteItem.getIcon().setAlpha(100);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                onDeleteAction();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void onDeleteAction() {
        showSimpleDialog(
            R.string.activity_queue_popup_delete_all,
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    DatabaseHelper.getInstance()
                        .getQueueRepository()
                        .deleteAllDrafts();

                    list.clear();
                    adapter.notifyDataSetChanged();
                    invalidateOptionsMenu();
                }
            }
        );
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        MenuInflater inflater = mode.getMenuInflater();
        inflater.inflate(R.menu.menu_action_reports_draft, menu);
        setMenuItemIcon(menu, R.id.action_delete, GoogleMaterial.Icon.gmd_delete);
        return super.onCreateActionMode(mode, menu);
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                onDeleteSelectedAction();
                break;
        }
        return false;
    }

    private void onDeleteSelectedAction() {
        List<QueueReport> selectedItems = adapter.getSelectedItems();
        if (selectedItems.isEmpty()) {
            return;
        }

        showSimpleDialog(
            R.string.activity_queue_popup_delete,
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    List<Integer> ids = new ArrayList<>();
                    for (QueueReport model : selectedItems) {
                        ids.add(model.getId());
                        list.remove(model);
                    }

                    DatabaseHelper.getInstance()
                        .getQueueRepository()
                        .deleteAll(ids);
                    mActionMode.finish();
                }
            }
        );
    }

    @Override
    public void onItemClick(BaseAdapter adapter, int position) {
        QueueReport model = (QueueReport) adapter.getItem(position);
        Intent intent = new Intent(this, DraftReportActivity.class);
        intent.putExtra(DraftReportActivity.ARGUMENT_QUEUE_ID, model.getId());
        startActivity(intent);
    }

    //---------------------------------------------------------------------------
    //
    // ACCESSORS
    //
    //---------------------------------------------------------------------------

    @Override
    protected int getContentViewRes() {
        return R.layout.activity_drafts;
    }
}
