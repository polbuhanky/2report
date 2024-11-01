package seven.bsh.view.reports;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import seven.bsh.R;
import seven.bsh.db.DatabaseHelper;
import seven.bsh.db.entity.Checklist;
import seven.bsh.db.entity.Report;
import seven.bsh.model.Pagination;
import seven.bsh.net.ApiConnector;
import seven.bsh.net.listener.OnRefreshTokenRequestListener;
import seven.bsh.net.listener.OnReportsGetRequestListener;
import seven.bsh.net.request.RefreshTokenRequest;
import seven.bsh.net.request.ReportsGetRequest;
import seven.bsh.net.response.RefreshTokenResponse;
import seven.bsh.net.response.ReportsGetResponse;
import seven.bsh.view.SideMenuActivity;
import seven.bsh.view.report.view.ViewReportActivity;
import seven.bsh.view.reports.adapter.ReportAdapter;
import seven.bsh.view.widget.adapter.BaseAdapter;
import seven.bsh.view.widget.list.CustomListView;
import seven.bsh.view.widget.list.InfinityListView;

public abstract class BaseReportsActivity extends SideMenuActivity implements
    CustomListView.OnItemClickListener,
    OnReportsGetRequestListener.OnRequestListener,
    OnRefreshTokenRequestListener.OnRequestListener,
    InfinityListView.OnLoadMoreListener {
    private static final String TAG = "BaseReportsActivity";

    protected List<Report> list;
    protected Pagination pagination;
    protected InfinityListView listView;
    protected ReportAdapter adapter;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    protected void initBaseReportsActivity() {
init();
setTitle(getString(R.string.activity_reports_title));
        setHomeAsBackIcon();
        setupToolbar();

        list = new ArrayList<>();

        listView = findViewById(R.id.list_view);
        listView.setLayoutManager(new LinearLayoutManager(this));
        listView.setEmptyView(findViewById(R.id.text_empty));
        listView.setLoadMoreListener(this);
        listView.setItemClickListener(this);
    }

    //---------------------------------------------------------------------------
    //
    // HANDLERS
    //
    //---------------------------------------------------------------------------


    @Override
    protected void onResume() {
        super.onResume();
        listView.post(new StartUpdateRunnable());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_reports, menu);
        setMenuItemIcon(menu, R.id.action_refresh, GoogleMaterial.Icon.gmd_refresh);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                showLoader();
                sendGetListRequest(1);
                return true;

            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(BaseAdapter adapter, int position) {
        Report model = (Report) adapter.getItem(position);
        Intent intent = new Intent(this, ViewReportActivity.class);
        intent.putExtra(ViewReportActivity.ARGUMENT_REPORT_ID, model.getId());
        startActivity(intent);
    }

    @Override
    public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
        if (pagination == null || !pagination.hasNextPage()) {
            return;
        }
        sendGetListRequest(pagination.getNextPage());
    }

    private class StartUpdateRunnable implements Runnable {
        @Override
        public void run() {
            try {
                showLoader();
                sendGetListRequest(1);
            } catch (Exception ex) {
                Log.e(TAG, ex.toString(), ex);
                hideLoader();
            }
        }
    }

    private class PrepareReportsTask extends AsyncTask<Void, Void, Void> {
        private final List<Report> mList;

        public PrepareReportsTask(List<Report> list) {
            mList = list;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                if (pagination.isFirstPage()) {
                    list.clear();
                    DatabaseHelper.getInstance()
                        .getReportRepository()
                        .deleteAll(getListStatus());
                }

                DatabaseHelper.getInstance()
                    .getReportRepository()
                    .saveAll(mList);

                Map<Integer, Checklist> checklists = new HashMap<>();
                for (Report model : mList) {
                    if (checklists.containsKey(model.getChecklistId())) {
                        continue;
                    }
                    checklists.put(model.getChecklistId(), model.getChecklist());
                }

                if (!checklists.isEmpty()) {
                    DatabaseHelper.getInstance()
                        .getChecklistRepository()
                        .saveAll(new ArrayList<>(checklists.values()));
                }
            } catch (Exception ex) {
                Log.e(TAG, ex.toString(), ex);
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
                    list.addAll(mList);
                    adapter.notifyDataSetChanged();
                }
            } catch (Exception ex) {
                Log.e(TAG, ex.toString(), ex);
            }
        }
    }

    @Override
    public void onGetReportsSuccess(ReportsGetResponse response) {
        pagination = response.getPagination();
        bgTask = new PrepareReportsTask(response.getReports()).execute();
    }

    @Override
    public void onTokenError() {
        super.onTokenError();
        ApiConnector api = getApi();
        if (api != null) {
            String token = getLocalData().getRefreshToken();
            RefreshTokenRequest request = new RefreshTokenRequest(token);
            api.refreshToken(request, new OnRefreshTokenRequestListener(this));
        }
    }

    @Override
    public void onAuthSuccess(RefreshTokenResponse response) {
        getLocalData().saveToken(response.getToken(), response.getRefreshToken());
        sendGetListRequest((pagination == null) ? 0 : pagination.getNextPage());
    }

    @Override
    public void onAuthFailed() {
        logout();
    }

    //---------------------------------------------------------------------------
    //
    // PRIVATE METHODS
    //
    //---------------------------------------------------------------------------

    private void sendGetListRequest(int page) {
        ApiConnector api = getApi();
        if (api != null) {
            String token = getLocalData().getAccessToken();
            ReportsGetRequest request = new ReportsGetRequest(token, page, getListStatus());
            api.getReports(request, new OnReportsGetRequestListener(this));
        }
    }

    //---------------------------------------------------------------------------
    //
    // ACCESSORS
    //
    //---------------------------------------------------------------------------

    @Override
    protected int getContentViewRes() {
        return R.layout.activity_reports;
    }

    protected abstract int getListStatus();
}
