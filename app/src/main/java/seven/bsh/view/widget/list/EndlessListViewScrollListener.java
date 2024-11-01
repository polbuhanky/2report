package seven.bsh.view.widget.list;


import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

public abstract class EndlessListViewScrollListener extends RecyclerView.OnScrollListener {
    private int mVisibleThreshold = 5;
    private int mCurrentPage = 0;
    private int mPrevTotalItemCount = 0;
    private int mStartingPageIndex = 0;
    private boolean mLoading = true;
    private RecyclerView.LayoutManager mLayoutManager;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public EndlessListViewScrollListener(RecyclerView.LayoutManager layoutManager) {
        mLayoutManager = layoutManager;
        if (layoutManager instanceof GridLayoutManager) {
            mVisibleThreshold *= ((GridLayoutManager) layoutManager).getSpanCount();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            mVisibleThreshold *= ((StaggeredGridLayoutManager) layoutManager).getSpanCount();
        }
    }

    //---------------------------------------------------------------------------
    //
    // HANDLERS
    //
    //---------------------------------------------------------------------------

    @Override
    public void onScrolled(RecyclerView view, int dx, int dy) {
        int lastVisibleItemPosition = 0;
        int totalItemCount = mLayoutManager.getItemCount();

        if (mLayoutManager instanceof StaggeredGridLayoutManager) {
            int[] lastVisibleItemPositions = ((StaggeredGridLayoutManager) mLayoutManager).findLastVisibleItemPositions(null);
            lastVisibleItemPosition = getLastVisibleItem(lastVisibleItemPositions);
        } else if (mLayoutManager instanceof GridLayoutManager) {
            lastVisibleItemPosition = ((GridLayoutManager) mLayoutManager).findLastVisibleItemPosition();
        } else if (mLayoutManager instanceof LinearLayoutManager) {
            lastVisibleItemPosition = ((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition();
        }

        if (totalItemCount < mPrevTotalItemCount) {
            mCurrentPage = mStartingPageIndex;
            mPrevTotalItemCount = totalItemCount;
            if (totalItemCount == 0) {
                mLoading = true;
            }
        }

        if (mLoading && (totalItemCount > mPrevTotalItemCount)) {
            mLoading = false;
            mPrevTotalItemCount = totalItemCount;
        }

        if (!mLoading && (lastVisibleItemPosition + mVisibleThreshold) > totalItemCount) {
            mCurrentPage++;
            onLoadMore(mCurrentPage, totalItemCount, view);
            mLoading = true;
        }
    }

    public abstract void onLoadMore(int page, int totalItemsCount, RecyclerView view);

    //---------------------------------------------------------------------------
    //
    // PUBLIC METHODS
    //
    //---------------------------------------------------------------------------

    public int getLastVisibleItem(int[] lastVisibleItemPositions) {
        int maxSize = 0;
        for (int i = 0; i < lastVisibleItemPositions.length; i++) {
            if (i == 0) {
                maxSize = lastVisibleItemPositions[i];
            } else if (lastVisibleItemPositions[i] > maxSize) {
                maxSize = lastVisibleItemPositions[i];
            }
        }
        return maxSize;
    }
}
