package seven.bsh.view.widget.list;

import android.content.Context;
import android.util.AttributeSet;

import androidx.recyclerview.widget.RecyclerView;

public class InfinityListView extends CustomListView {
    private OnLoadMoreListener mLoadMoreListener;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public InfinityListView(Context context) {
        super(context);
    }

    public InfinityListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public InfinityListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //---------------------------------------------------------------------------
    //
    // PUBLIC METHODS
    //
    //---------------------------------------------------------------------------

    @Override
    public void setLayoutManager(LayoutManager layout) {
        super.setLayoutManager(layout);
        addOnScrollListener(new EndlessListViewScrollListener(layout) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (mLoadMoreListener != null) {
                    mLoadMoreListener.onLoadMore(page, totalItemsCount, view);
                }
            }
        });
    }

    //---------------------------------------------------------------------------
    //
    // ACCESSORS
    //
    //---------------------------------------------------------------------------

    public void setLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        mLoadMoreListener = loadMoreListener;
    }

    //---------------------------------------------------------------------------
    //
    // INTERFACES
    //
    //---------------------------------------------------------------------------

    public interface OnLoadMoreListener {
        void onLoadMore(int page, int totalItemsCount, RecyclerView view);
    }
}
