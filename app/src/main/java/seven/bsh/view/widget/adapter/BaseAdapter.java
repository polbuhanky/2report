package seven.bsh.view.widget.adapter;

import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

public abstract class BaseAdapter<M> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    protected static final int TYPE_ITEM = 0;
    protected static final int TYPE_HEADER = 1;

    protected OnListener listener;
    protected OnActionListener actionListener;

    //---------------------------------------------------------------------------
    //
    // ACCESSORS
    //
    //---------------------------------------------------------------------------

    public void setListener(OnListener listener) {
        this.listener = listener;
    }

    public void setActionListener(OnActionListener actionListener) {
        this.actionListener = actionListener;
    }

    public abstract RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType);

    public abstract M getItem(int position);

    public boolean hasHeader() {
        return getItemViewType(0) == TYPE_HEADER;
    }

    //---------------------------------------------------------------------------
    //
    // INTERFACES
    //
    //---------------------------------------------------------------------------

    public interface OnListener {
        void onItemClick(int position);
    }

    public interface OnActionListener {
        void onActionItem(int position);
    }
}
