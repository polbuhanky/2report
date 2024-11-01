package seven.bsh.view.widget.sideMenu;

import android.content.Context;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;


import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import seven.bsh.R;
import seven.bsh.Application;
import seven.bsh.BuildConfig;
import seven.bsh.LocalData;
import seven.bsh.view.SideMenuActivity;

public class SideMenu implements AdapterView.OnItemClickListener, View.OnClickListener {
    private final SideMenuActivity mActivity;
    private final DrawerLayout mLayout;
    private final ListView mListView;
    private final SlidingMenuAdapter mAdapter;
    private final List<SideMenuItem> mList;

    private OnSideMenuListener mListener;
    private SideMenuItem.Id mActiveId;
    private BadgeSideMenuItem mDraftReportsItem;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public SideMenu(final SideMenuActivity activity, DrawerLayout layout, SideMenuItem.Id activeId) {
        mActivity = activity;
        mLayout = layout;
        mList = getItemList(activeId);

        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mAdapter = new SlidingMenuAdapter(activity, mList);
        mListView = layout.findViewById(R.id.sliding_menu);

        // set header
        View header = createHeader(inflater);
        header.setOnClickListener(this);
        mListView.addHeaderView(header, null, false);

        // set footer
        View footer = createFooter(inflater);
        mListView.addFooterView(footer, null, false);
        mListView.setFooterDividersEnabled(false);

        mListView.setOnItemClickListener(this);
        mListView.setAdapter(mAdapter);
    }

    //---------------------------------------------------------------------------
    //
    // HANDLERS
    //
    //---------------------------------------------------------------------------

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        close();
        SideMenuItem item = mList.get(position - 1); // потому что хидер есть
        if (item.getId() == mActiveId) {
            return;
        }

        if (mListener != null) {
            switch (item.getId()) {
                case TRADE_OBJECTS:
                    mListener.onTradeObjectsSideMenuItemClick();
                    break;

                case CACHE:
                    mListener.onCacheSideMenuItemClick();
                    break;

                case QUEUE:
                    mListener.onQueueSideMenuItemClick();
                    break;

                case LOGOUT:
                    mListener.onLogoutSideMenuItemClick();
                    break;

                case REPORTS_APPROVED:
                    mListener.onApprovedReportsSideMenuItemClick();
                    break;

                case REPORTS_NEW:
                    mListener.onNewReportsSideMenuItemClick();
                    break;

                case REPORTS_REJECTED:
                    mListener.onRejectedReportsSideMenuItemClick();
                    break;

                case REPORTS_FOR_IMPROVED:
                    mListener.onForImprovedReportsSideMenuItemClick();
                    break;

                case REPORTS_DRAFT:
                    mListener.onDraftReportsSideMenuItemClick();
                    break;
            }
        }
    }

    @Override
    public void onClick(View view) {
        close();
        mListener.onHeaderSideMenuClick();
    }

    //---------------------------------------------------------------------------
    //
    // PRIVATE METHODS
    //
    //---------------------------------------------------------------------------

    private ArrayList<SideMenuItem> getItemList(SideMenuItem.Id activeId) {
        ArrayList<SideMenuItem> list = new ArrayList<>();
        mActiveId = activeId;

        list.add(
            new SideMenuItem(
                "{gmd-storage}",
                mActivity.getString(R.string.menu_item_trade_objects),
                SideMenuItem.Id.TRADE_OBJECTS,
                activeId == SideMenuItem.Id.TRADE_OBJECTS
            )
        );

        list.add(
            new SideMenuItem(
                "{gmd-cloud-download}",
                mActivity.getString(R.string.menu_item_cache),
                SideMenuItem.Id.CACHE,
                activeId == SideMenuItem.Id.CACHE
            )
        );

        list.add(
            new SideMenuItem(
                "{gmd-playlist-add-check}",
                mActivity.getString(R.string.menu_item_queue),
                SideMenuItem.Id.QUEUE,
                activeId == SideMenuItem.Id.QUEUE
            )
        );

        list.add(
            new CategoryMenuItem(
                mActivity.getString(R.string.menu_item_reports)
            )
        );

        mDraftReportsItem = new BadgeSideMenuItem(
            "{gmd-assignment}",
            mActivity.getString(R.string.menu_item_reports_draft),
            0,
            ContextCompat.getColor(mActivity, R.color.menu_item_badge_bg_primary),
            SideMenuItem.Id.REPORTS_DRAFT,
            activeId == SideMenuItem.Id.REPORTS_DRAFT
        );
        list.add(mDraftReportsItem);

        list.add(
            new SideMenuItem(
                "{gmd-assignment-return}",
                mActivity.getString(R.string.menu_item_reports_forImprovement),
                SideMenuItem.Id.REPORTS_FOR_IMPROVED,
                activeId == SideMenuItem.Id.REPORTS_FOR_IMPROVED
            )
        );

        list.add(
            new SideMenuItem(
                "{gmd-assignment-returned}",
                mActivity.getString(R.string.menu_item_reports_new),
                SideMenuItem.Id.REPORTS_NEW,
                activeId == SideMenuItem.Id.REPORTS_NEW
            )
        );

        list.add(
            new SideMenuItem(
                "{gmd-assignment-turned-in}",
                mActivity.getString(R.string.menu_item_reports_approved),
                SideMenuItem.Id.REPORTS_APPROVED,
                activeId == SideMenuItem.Id.REPORTS_APPROVED
            )
        );

        list.add(
            new SideMenuItem(
                "{gmd-assignment-late}",
                mActivity.getString(R.string.menu_item_reports_rejected),
                SideMenuItem.Id.REPORTS_REJECTED,
                activeId == SideMenuItem.Id.REPORTS_APPROVED
            )
        );

        list.add(new SeparatorMenuItem());
        list.add(
            new SideMenuItem(
                "{gmd-exit-to-app}",
                mActivity.getString(R.string.menu_item_logout),
                SideMenuItem.Id.LOGOUT,
                false
            )
        );
        return list;
    }

    private View createHeader(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.item_side_menu_header, mListView, false);
        String userName = getLocalDate().getLogin();
        if (userName == null) {
            userName = "";
        }

        TextView nameField = view.findViewById(R.id.name);
        nameField.setText(userName);

        TextView workField = view.findViewById(R.id.work);
        workField.setText("полевой работник");
        return view;
    }

    private View createFooter(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.item_side_menu_footer, mListView, false);
        TextView versionField = view.findViewById(R.id.label);
        versionField.setText(mActivity.getString(R.string.menu_item_version, BuildConfig.VERSION_NAME)  + "\n(с) Еланд.рф");
        return view;
    }

    //---------------------------------------------------------------------------
    //
    // PUBLIC METHODS
    //
    //---------------------------------------------------------------------------

    public void open() {
        mLayout.openDrawer(GravityCompat.START);
    }

    public void close() {
        mLayout.closeDrawer(GravityCompat.START);
    }

    public void toggle() {
        if (isOpened()) {
            close();
        } else {
            open();
        }
    }

    public void setActive(SideMenuItem.Id id) {
        if (mActiveId == id) {
            return;
        }

        for (SideMenuItem item : mList) {
            SideMenuItem.Id itemId = item.getId();
            if (itemId == mActiveId) {
                item.setActive(false);
            } else if (itemId == id) {
                item.setActive(true);
            }
        }

        mActiveId = id;
        mAdapter.notifyDataSetChanged();
    }

    public void setDraftReportCount(int count) {
        mDraftReportsItem.setBadge(count);
        mAdapter.notifyDataSetChanged();
    }

    //---------------------------------------------------------------------------
    //
    // ACCESSORS
    //
    //---------------------------------------------------------------------------

    private LocalData getLocalDate() {
        return Application.instance().getLocalData();
    }

    public void setOnItemClickListener(OnSideMenuListener listener) {
        mListener = listener;
    }

    public boolean isOpened() {
        return mLayout.isDrawerOpen(GravityCompat.START);
    }

    //---------------------------------------------------------------------------
    //
    // INTERFACES
    //
    //---------------------------------------------------------------------------

    public interface OnSideMenuListener {
        void onTradeObjectsSideMenuItemClick();

        void onCacheSideMenuItemClick();

        void onQueueSideMenuItemClick();

        void onLogoutSideMenuItemClick();

        void onHeaderSideMenuClick();

        void onApprovedReportsSideMenuItemClick();

        void onNewReportsSideMenuItemClick();

        void onRejectedReportsSideMenuItemClick();

        void onForImprovedReportsSideMenuItemClick();

        void onDraftReportsSideMenuItemClick();
    }
}
