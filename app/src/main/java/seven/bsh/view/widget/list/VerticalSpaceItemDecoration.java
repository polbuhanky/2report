package seven.bsh.view.widget.list;

import android.graphics.Rect;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

public class VerticalSpaceItemDecoration extends RecyclerView.ItemDecoration {
    private int mSpaceHeight;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public VerticalSpaceItemDecoration(int spaceHeight) {
        mSpaceHeight = spaceHeight;
    }

    //---------------------------------------------------------------------------
    //
    // PUBLIC METHODS
    //
    //---------------------------------------------------------------------------

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.bottom = mSpaceHeight;
    }
}
