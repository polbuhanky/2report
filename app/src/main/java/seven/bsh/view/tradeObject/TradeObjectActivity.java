package seven.bsh.view.tradeObject;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.widget.AutoCompleteTextView;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;

import java.util.ArrayList;
import java.util.List;

import seven.bsh.R;
import seven.bsh.db.DatabaseHelper;
import seven.bsh.db.entity.TradeObject;
import seven.bsh.db.repository.TradeObjectRepository;
import seven.bsh.model.Pagination;
import seven.bsh.net.service.CacheService;
import seven.bsh.view.SideMenuActivity;
import seven.bsh.view.checklists.ChecklistsActivity;
import seven.bsh.view.tradeObject.adapter.TradeObjectAdapter;
import seven.bsh.view.widget.adapter.BaseAdapter;
import seven.bsh.view.widget.list.CustomListView;
import seven.bsh.view.widget.list.InfinityListView;
import seven.bsh.view.widget.sideMenu.SideMenuItem;

public class TradeObjectActivity extends SideMenuActivity implements
    SearchView.OnQueryTextListener,
    CustomListView.OnItemClickListener,
    InfinityListView.OnLoadMoreListener {
    private static final String TAG = "TradeObjectActivity";

    private TradeObjectAdapter mAdapter;
    private List<TradeObject> mList;
    private TradeObjectRepository.Filter mFilter;
    private Pagination mPagination;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade_objects);
        init();
        setHomeAsMenuIcon();
        setTitle(getString(R.string.activity_trade_objects_title));
        getSideMenu().setActive(SideMenuItem.Id.TRADE_OBJECTS);

        mList = new ArrayList<>();
        mFilter = new TradeObjectRepository.Filter();
        mAdapter = new TradeObjectAdapter(mList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        InfinityListView listView = findViewById(R.id.list_view);
        listView.setLayoutManager(layoutManager);
        listView.setEmptyView(findViewById(R.id.text_empty));
        listView.setItemClickListener(this);
        listView.setLoadMoreListener(this);
        listView.setAdapter(mAdapter);
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
        if (getLocalData().hasCommonCache()) {
            startCacheService(CacheService.COMMAND_NONE);
        } else {
            showLoader();
            startCacheService(CacheService.COMMAND_CACHE);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_trade_objects, menu);
        setMenuItemIcon(menu, R.id.action_search, GoogleMaterial.Icon.gmd_search);

        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        AutoCompleteTextView searchTextView = searchView.findViewById(R.id.search_src_text);
        replaceSearchField(searchTextView);
        searchView.setOnQueryTextListener(this);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        search(newText);
        return false;
    }

    @Override
    public void onItemClick(BaseAdapter adapter, int position) {
        TradeObject item = (TradeObject) adapter.getItem(position);
        Intent intent = new Intent(this, ChecklistsActivity.class);
        intent.putExtra(ChecklistsActivity.ARGUMENT_TRADE_OBJECT_ID, item.getId());
        startActivity(intent);
    }

    @Override
    public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
        if (mPagination != null && mPagination.hasNextPage()) {
            bgTask = new GetTradeObjectsFromDbTask(mPagination.getNextPage(), mFilter).execute();
        }
    }

    @Override
    protected void onGlobalCacheCompleted() {
        super.onGlobalCacheCompleted();
        showLoader();
        bgTask = new GetTradeObjectsFromDbTask(1, mFilter).execute();
    }

    @Override
    protected void onGlobalCacheProcess() {
        super.onGlobalCacheProcess();
        mList.clear();
        mAdapter.notifyDataSetChanged();
    }

    class GetTradeObjectsFromDbTask extends AsyncTask<Void, Void, Void> {
        private final TradeObjectRepository.Filter mFilter;
        private final int mPage;
        private List<TradeObject> mList;
        private int mCount;

        public GetTradeObjectsFromDbTask(int page, TradeObjectRepository.Filter filter) {
            mPage = page;
            mFilter = filter;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                mList = DatabaseHelper.getInstance()
                    .getTradeObjectRepository()
                    .getList(mPage, mFilter);
                mCount = DatabaseHelper.getInstance()
                    .getTradeObjectRepository()
                    .count(mFilter.getQuery());
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

                if (mPagination.isFirstPage()) {
                    TradeObjectActivity.this.mList.clear();
                }

                if (mList != null) {
                    TradeObjectActivity.this.mList.addAll(mList);
                    mAdapter.setTotalCount(mPagination.getTotalCount());
                    mAdapter.notifyDataSetChanged();
                } else if (mFilter.getQuery() != null) {
                    mAdapter.notifyDataSetChanged();
                } else {
                    onNoInternetError();
                }
            } catch (Exception ex) {
                Log.d(TAG, ex.getMessage(), ex);
            }
        }
    }

    //---------------------------------------------------------------------------
    //
    // PRIVATE METHODS
    //
    //---------------------------------------------------------------------------

    /**
     * Подмена ресурса для курсора в строке поиска
     *
     * @param textView
     */
    private void replaceSearchField(AutoCompleteTextView textView) {
        try {
//            Field cursorDrawableRes = TextView.class.getField("mCursorDrawableRes");
//            cursorDrawableRes.setAccessible(true);
//            cursorDrawableRes.set(textView, R.drawable.search_cursor);
        } catch (Exception ex) {
            Log.e(TAG, ex.toString(), ex);
        }
    }

    private void search(String query) {
        if (!isGlobalCachePending) {
            return;
        }

        if (bgTask != null && bgTask.getStatus() == AsyncTask.Status.RUNNING) {
            bgTask.cancel(false);
        }

        showLoader();
        mFilter.setQuery(query);
        bgTask = new GetTradeObjectsFromDbTask(1, mFilter).execute();
    }

    private void startCacheService(int command) {
        try {
            Intent intent = new Intent(this, CacheService.class);
            intent.putExtra(CacheService.KEY_COMMAND, command);
            startService(intent);
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    //---------------------------------------------------------------------------
    //
    // ACCESSORS
    //
    //---------------------------------------------------------------------------

    @Override
    protected int getContentViewRes() {
        return R.layout.activity_trade_objects;
    }
}
