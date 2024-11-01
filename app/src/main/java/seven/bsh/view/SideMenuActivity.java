package seven.bsh.view;

import android.content.Intent;

import androidx.drawerlayout.widget.DrawerLayout;

import android.view.MenuItem;

import seven.bsh.R;
import seven.bsh.db.DatabaseHelper;
import seven.bsh.view.cache.CacheActivity;
import seven.bsh.view.profile.ProfileActivity;
import seven.bsh.view.queue.DraftReportsActivity;
import seven.bsh.view.queue.QueueActivity;
import seven.bsh.view.reports.ApprovedReportsActivity;
import seven.bsh.view.reports.ForImprovementReportsActivity;
import seven.bsh.view.reports.NewReportsActivity;
import seven.bsh.view.reports.RejectedReportsActivity;
import seven.bsh.view.tradeObject.TradeObjectActivity;
import seven.bsh.view.widget.sideMenu.SideMenu;
import seven.bsh.view.widget.sideMenu.SideMenuItem;

public abstract class SideMenuActivity extends BaseActivity implements SideMenu.OnSideMenuListener {
    private SideMenu mSideMenu;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    protected void init() {
        setupSideMenu();
        setupToolbar();
    }

    //---------------------------------------------------------------------------
    //
    // HANDLERS
    //
    //---------------------------------------------------------------------------

    @Override
    protected void onResume() {
        super.onResume();
        int count = DatabaseHelper.getInstance()
            .getQueueRepository()
            .countDraft();
        mSideMenu.setDraftReportCount(count);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                toggleSlidingMenu();
                return false;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLogoutSideMenuItemClick() {
        logout();
    }

    @Override
    public void onHeaderSideMenuClick() {
        startActivity(new Intent(this, ProfileActivity.class));
    }

    @Override
    public void onBackPressed() {
        if (mSideMenu.isOpened()) {
            mSideMenu.close();
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void onTradeObjectsSideMenuItemClick() {
        Intent intent = new Intent(this, TradeObjectActivity.class);
        startActivity(intent);
    }

    @Override
    public void onCacheSideMenuItemClick() {
        Intent intent = new Intent(this, CacheActivity.class);
        startActivity(intent);
    }

    @Override
    public void onQueueSideMenuItemClick() {
        Intent intent = new Intent(this, QueueActivity.class);
        startActivity(intent);
    }

    @Override
    public void onApprovedReportsSideMenuItemClick() {
        Intent intent = new Intent(this, ApprovedReportsActivity.class);
        startActivity(intent);
    }

    @Override
    public void onNewReportsSideMenuItemClick() {
        Intent intent = new Intent(this, NewReportsActivity.class);
        startActivity(intent);
    }

    @Override
    public void onRejectedReportsSideMenuItemClick() {
        Intent intent = new Intent(this, RejectedReportsActivity.class);
        startActivity(intent);
    }

    @Override
    public void onForImprovedReportsSideMenuItemClick() {
        Intent intent = new Intent(this, ForImprovementReportsActivity.class);
        startActivity(intent);
    }

    @Override
    public void onDraftReportsSideMenuItemClick() {
        Intent intent = new Intent(this, DraftReportsActivity.class);
        startActivity(intent);
    }

    //---------------------------------------------------------------------------
    //
    // PUBLIC METHODS
    //
    //---------------------------------------------------------------------------

    public void toggleSlidingMenu() {
        mSideMenu.toggle();
    }

    //---------------------------------------------------------------------------
    //
    // PRIVATE METHODS
    //
    //---------------------------------------------------------------------------

    protected void setupSideMenu() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        mSideMenu = new SideMenu(this, drawer, SideMenuItem.Id.TRADE_OBJECTS);
        mSideMenu.setOnItemClickListener(this);
    }

    //---------------------------------------------------------------------------
    //
    // ACCESSORS
    //
    //---------------------------------------------------------------------------

    public SideMenu getSideMenu() {
        return mSideMenu;
    }
}
