package seven.bsh.view.checklists;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import seven.bsh.R;
import seven.bsh.db.DatabaseHelper;
import seven.bsh.db.entity.Checklist;
import seven.bsh.db.entity.TradeObject;
import seven.bsh.model.Pagination;
import seven.bsh.net.service.CacheService;
import seven.bsh.view.SideMenuActivity;
import seven.bsh.view.checklists.adapter.ChecklistAdapter;
import seven.bsh.view.report.update.CreateReportActivity;
import seven.bsh.view.widget.adapter.BaseAdapter;
import seven.bsh.view.widget.list.CustomListView;
import seven.bsh.view.widget.list.InfinityListView;

public class ChecklistsActivity extends SideMenuActivity implements
    CustomListView.OnItemClickListener,
    InfinityListView.OnLoadMoreListener {
    public static final String ARGUMENT_TRADE_OBJECT_ID = "tradeObjectId";

    private static final String TAG = "ChecklistsActivity";

    private List<Checklist> mList;
    private ChecklistAdapter mAdapter;
    private Pagination mPagination;
    private TradeObject mTradeObject;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklists);
        init();
        getSideMenu().setActive(null);
        setTitle(getString(R.string.activity_checklists_title));
        setHomeAsBackIcon();

        Intent intent = getIntent();
        int tradeObjectId = intent.getIntExtra(ARGUMENT_TRADE_OBJECT_ID, 0);
        mTradeObject = DatabaseHelper.getInstance()
            .getTradeObjectRepository()
            .get(tradeObjectId);

        mList = new ArrayList<>();
        mAdapter = new ChecklistAdapter(this, mList);
        mAdapter.setTradeObject(mTradeObject);

        InfinityListView listView = findViewById(R.id.list_view);
        listView.setLayoutManager(new LinearLayoutManager(this));
        listView.setEmptyView(findViewById(R.id.text_empty));
        listView.setAdapter(mAdapter);
        listView.setItemClickListener(this);
        listView.setLoadMoreListener(this);
    }

    //---------------------------------------------------------------------------
    //
    // HANDLERS
    //
    //---------------------------------------------------------------------------

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter(CacheService.BROADCAST_ID);
        registerReceiver(receiver, intentFilter);
        startService(CacheService.COMMAND_NONE);
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(BaseAdapter adapter, int position) {
        Checklist item = mAdapter.getItem(position);
        Intent intent = new Intent(this, CreateReportActivity.class);
        intent.putExtra(CreateReportActivity.ARGUMENT_CHECKLIST_ID, item.getId());
        intent.putExtra(CreateReportActivity.ARGUMENT_TRADE_OBJECT_ID, mTradeObject.getId());
        intent.putExtra(CreateReportActivity.ARGUMENT_PROJECT_ID, item.getProjectId());
        startActivity(intent);
    }

    @Override
    public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
        if (mPagination != null && mPagination.hasNextPage()) {
            bgTask = new GetChecklistsFromDbTask(mTradeObject.getId(), mPagination.getNextPage()).execute();
        }
    }

    @Override
    protected void onGlobalCacheCompleted() {
        super.onGlobalCacheCompleted();
        showLoader();
        bgTask = new GetChecklistsFromDbTask(mTradeObject.getId(), 1).execute();
    }

    @Override
    protected void onGlobalCacheProcess() {
        super.onGlobalCacheProcess();
        mList.clear();
        mAdapter.notifyDataSetChanged();
    }

    class GetChecklistsFromDbTask extends AsyncTask<Void, Void, Void> {
        private final int mTradeObjectId;
        private final int mPage;
        private List<Checklist> mList;
        private int mCount;

        public GetChecklistsFromDbTask(int tradeObjectId, int page) {
            mTradeObjectId = tradeObjectId;
            mPage = page;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                mList = DatabaseHelper.getInstance()
                    .getChecklistRepository()
                    .getList(mTradeObjectId, mPage);
                mCount = DatabaseHelper.getInstance()
                    .getChecklistRepository()
                    .count(mTradeObjectId);
                mPagination = Pagination.create(mCount, mPage);
            } catch (Exception ex) {
                Log.e(TAG, ex.getMessage(), ex);
                cancel(false);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                hideLoader();
                if (isCancelled()) {
                    return;
                }

                if (mList != null) {
                    ChecklistsActivity.this.mList.clear();
                    ChecklistsActivity.this.mList.addAll(mList);
                    mAdapter.notifyDataSetChanged();
                }
            } catch (Exception ex) {
                Log.e(TAG, ex.getMessage(), ex);
            }
        }
    }

    //---------------------------------------------------------------------------
    //
    // PRIVATE METHODS
    //
    //---------------------------------------------------------------------------

    private void startService(int command) {
        Intent intent = new Intent(this, CacheService.class);
        intent.putExtra(CacheService.KEY_COMMAND, command);
        startService(intent);
    }

    //---------------------------------------------------------------------------
    //
    // ACCESSORS
    //
    //---------------------------------------------------------------------------

    @Override
    protected int getContentViewRes() {
        return R.layout.activity_checklists;
    }
}
