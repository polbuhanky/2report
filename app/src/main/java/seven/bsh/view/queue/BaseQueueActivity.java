package seven.bsh.view.queue;

import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import seven.bsh.R;
import seven.bsh.db.entity.QueueReport;
import seven.bsh.model.Pagination;
import seven.bsh.view.SideMenuActivity;
import seven.bsh.view.queue.adapter.QueueAdapter;
import seven.bsh.view.widget.adapter.BaseAdapter;
import seven.bsh.view.widget.list.CustomListView;

public abstract class BaseQueueActivity extends SideMenuActivity implements
    CustomListView.OnItemClickListener,
    BaseAdapter.OnActionListener,
    ActionMode.Callback {
    protected List<QueueReport> list;
    protected Pagination pagination;
    protected CustomListView listView;
    protected QueueAdapter adapter;
    protected boolean checkedMode;
    protected ActionMode mActionMode;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public void initBaseQueueActivity() {
        init();
        setHomeAsBackIcon();
        list = new ArrayList<>();
        listView = findViewById(R.id.list_view);
        listView.setLayoutManager(new LinearLayoutManager(this));
        listView.setEmptyView(findViewById(R.id.text_empty));
        listView.setItemClickListener(this);
    }

    //---------------------------------------------------------------------------
    //
    // HANDLERS
    //
    //---------------------------------------------------------------------------

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        mActionMode = mode;
        adapter.setChoiceMode(true);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        checkedMode = false;
        adapter.setChoiceMode(false);
        adapter.notifyDataSetChanged();
    }

    //---------------------------------------------------------------------------
    //
    // ACCESSORS
    //
    //---------------------------------------------------------------------------

    @Override
    public void onActionItem(int position) {
        if (!checkedMode) {
            checkedMode = true;
            startActionMode(this);
        }

        List<QueueReport> list = adapter.getSelectedItems();
        mActionMode.setTitle(getString(R.string.activity_reports_title_choice, list.size()));
    }
}
