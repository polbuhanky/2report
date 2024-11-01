package seven.bsh.view.widget.list;

import android.content.Context;
import android.graphics.Color;

import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import seven.bsh.utils.Converter;
import seven.bsh.view.widget.adapter.BaseAdapter;

public class CustomListView extends RecyclerView implements BaseAdapter.OnListener {
    private View mEmptyView;
    private OnItemClickListener mItemClickListener;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public CustomListView(Context context) {
        super(context);
        init(context);
    }

    public CustomListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    //---------------------------------------------------------------------------
    //
    // HANDLERS
    //
    //---------------------------------------------------------------------------

    private AdapterDataObserver emptyObserver = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            BaseAdapter<?> adapter = (BaseAdapter<?>) getAdapter();
            if (adapter == null || mEmptyView == null) {
                return;
            }

            if ((adapter.hasHeader() && adapter.getItemCount() == 1) || adapter.getItemCount() == 0) {
                mEmptyView.setVisibility(View.VISIBLE);
                setVisibility(View.GONE);
            } else {
                mEmptyView.setVisibility(View.GONE);
                setVisibility(View.VISIBLE);
            }
        }
    };

    @Override
    public void onItemClick(int position) {
        if (mItemClickListener != null) {
            mItemClickListener.onItemClick((BaseAdapter) getAdapter(), position);
        }
    }

    //---------------------------------------------------------------------------
    //
    // PRIVATE METHODS
    //
    //---------------------------------------------------------------------------

    protected void init(Context context) {
        int space = Converter.dip2Px(context, 8);
        VerticalSpaceItemDecoration dividerItemDecoration = new VerticalSpaceItemDecoration(space);
        addItemDecoration(dividerItemDecoration);
    }

    protected IconicsDrawable getActionBarIcon(GoogleMaterial.Icon icon) {
        return new IconicsDrawable(getContext(), icon)
            .sizeDp(18)
            .paddingDp(1)
            .color(Color.WHITE);
    }

    protected void setMenuItemIcon(Menu menu, int id, GoogleMaterial.Icon icon) {
        MenuItem item = menu.findItem(id);
        if (item != null) {
            item.setIcon(getActionBarIcon(icon));
        }
    }

    //---------------------------------------------------------------------------
    //
    // ACCESSORS
    //
    //---------------------------------------------------------------------------

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        if (adapter != null) {
            BaseAdapter adp = (BaseAdapter) adapter;
            adp.registerAdapterDataObserver(emptyObserver);
            adp.setListener(this);
        }
        emptyObserver.onChanged();
    }

    public void setEmptyView(View emptyView) {
        mEmptyView = emptyView;
    }

    public void setItemClickListener(OnItemClickListener listener) {
        mItemClickListener = listener;
    }

    //---------------------------------------------------------------------------
    //
    // INTERFACES
    //
    //---------------------------------------------------------------------------

    public interface OnItemClickListener {
        void onItemClick(BaseAdapter adapter, int position);
    }
}
